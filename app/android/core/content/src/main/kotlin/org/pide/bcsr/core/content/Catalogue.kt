package org.pide.bcsr.core.content

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import java.security.MessageDigest
import java.util.Base64

/** A catalogue item as loaded from a verified bundle; the JSON is kept as-is so scripture text is never re-typed. */
data class CatalogueItem(val id: String, val states: Set<String>, val status: String, val json: JsonObject) {
    val approved: Boolean get() = status == "approved"
    fun text(vararg path: String): String? =
        path.fold(json as JsonElement?) { e, k -> (e as? JsonObject)?.get(k) }?.let { (it as? JsonPrimitive)?.contentOrNull }
}

sealed interface LoadResult {
    data class Loaded(val version: String, val items: List<CatalogueItem>) : LoadResult
    data class Rejected(val reason: String) : LoadResult
}

/** Public keys pinned in the app, by key_id (ADR-0005 §5, §8: two keys during rotation). */
class BundleVerifier(private val publicKeys: Map<String, ByteArray>) {

    /** ADR-0005 §5: signature over the exact bytes with the pinned key for the manifest's key_id, then count and hash. */
    fun load(bundleBytes: ByteArray, signatureBase64: String): LoadResult = try {
        loadOrThrow(bundleBytes, signatureBase64)
    } catch (e: Exception) {
        LoadResult.Rejected("malformed bundle: ${e.javaClass.simpleName}") // a bad shape must reject, never crash the app (S-08)
    }

    private fun loadOrThrow(bundleBytes: ByteArray, signatureBase64: String): LoadResult {
        val root = try {
            Json.parseToJsonElement(bundleBytes.toString(Charsets.UTF_8)).jsonObject
        } catch (e: Exception) {
            return LoadResult.Rejected("not valid JSON")
        }
        val manifest = root["manifest"] as? JsonObject ?: return LoadResult.Rejected("no manifest")
        val keyId = manifest["key_id"]?.jsonPrimitive?.contentOrNull ?: return LoadResult.Rejected("no key_id")
        val key = publicKeys[keyId] ?: return LoadResult.Rejected("unknown key_id $keyId")
        val sig = try {
            Base64.getDecoder().decode(signatureBase64.trim())
        } catch (e: IllegalArgumentException) {
            return LoadResult.Rejected("signature not base64")
        }
        if (!verifyEd25519(key, bundleBytes, sig)) return LoadResult.Rejected("signature does not match")
        val items = root["items"] as? JsonArray ?: return LoadResult.Rejected("no items")
        if (manifest["item_count"]?.jsonPrimitive?.intOrNull != items.size) return LoadResult.Rejected("item_count mismatch")
        if (manifest["items_sha256"]?.jsonPrimitive?.contentOrNull != sha256Hex(canonicalJson(items))) {
            return LoadResult.Rejected("items_sha256 mismatch")
        }
        val parsed = items.map { el ->
            val o = el.jsonObject
            CatalogueItem(
                id = o["id"]?.jsonPrimitive?.contentOrNull ?: return LoadResult.Rejected("item without id"),
                states = (o["states"] as? JsonArray)?.mapNotNull { it.jsonPrimitive.contentOrNull }?.toSet() ?: emptySet(),
                status = (o["verification"] as? JsonObject)?.get("status")?.jsonPrimitive?.contentOrNull ?: "",
                json = o,
            )
        }
        return LoadResult.Loaded(manifest["version"]?.jsonPrimitive?.contentOrNull ?: "?", parsed)
    }

    private fun verifyEd25519(key: ByteArray, data: ByteArray, sig: ByteArray): Boolean = try {
        Ed25519Signer().run {
            init(false, Ed25519PublicKeyParameters(key, 0))
            update(data, 0, data.size)
            verifySignature(sig)
        }
    } catch (e: Exception) {
        false
    }

    companion object {
        /** Reads a `.pub` file as written by scripts/sign_bundle.py: "<key_id> <base64 raw key>". */
        fun parsePublicKey(line: String): Pair<String, ByteArray> =
            line.trim().split(Regex("\\s+")).let { (id, b64) -> id to Base64.getDecoder().decode(b64) }
    }
}

/**
 * Keeps the last good catalogue and serves approved items only (ADR-0005 §5–§6). A bundle that fails verification
 * never replaces the current one, and a non-approved item is dropped even inside a valid bundle (defence in depth).
 */
class CatalogueRepository(private val verifier: BundleVerifier) {
    private var approved: List<CatalogueItem> = emptyList() // filtered once per load, not on every call
    var current: LoadResult.Loaded? = null
        private set(v) {
            field = v
            approved = v?.items.orEmpty().filter { it.approved }
        }

    fun offer(bundleBytes: ByteArray, signatureBase64: String): LoadResult =
        verifier.load(bundleBytes, signatureBase64).also { if (it is LoadResult.Loaded) current = it }

    fun approvedItems(): List<CatalogueItem> = approved

    fun approvedItemsFor(stateId: String): List<CatalogueItem> = approvedItems().filter { stateId in it.states }

    fun search(query: String): List<CatalogueItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return approvedItems()
        return approvedItems().filter { item -> listOf("en", "ur").any { item.text("title", it)?.lowercase()?.contains(q) == true } }
    }
}

/**
 * Canonical JSON from api-contracts §2: keys sorted at every level, no whitespace, non-ASCII as itself,
 * only `"`, `\` and U+0000–U+001F escaped. Must reproduce scripts/build_catalogue_bundle.items_digest byte for byte.
 */
fun canonicalJson(e: JsonElement): String = StringBuilder().also { write(e, it) }.toString()

// shortcut: keys compared as UTF-16 code units; equals code-point order for the schema's ASCII keys, upgrade if keys go non-BMP.
private fun write(e: JsonElement, sb: StringBuilder) {
    when (e) {
        is JsonObject -> {
            sb.append('{')
            e.keys.sorted().forEachIndexed { i, k ->
                if (i > 0) sb.append(',')
                quote(k, sb); sb.append(':'); write(e.getValue(k), sb)
            }
            sb.append('}')
        }
        is JsonArray -> {
            sb.append('[')
            e.forEachIndexed { i, x -> if (i > 0) sb.append(','); write(x, sb) }
            sb.append(']')
        }
        JsonNull -> sb.append("null")
        is JsonPrimitive -> if (e.isString) quote(e.content, sb) else sb.append(e.content)
    }
}

private fun quote(s: String, sb: StringBuilder) {
    sb.append('"')
    for (ch in s) {
        when {
            ch == '"' -> sb.append("\\\"")
            ch == '\\' -> sb.append("\\\\")
            ch == '\b' -> sb.append("\\b")
            ch == '\u000C' -> sb.append("\\f")
            ch == '\n' -> sb.append("\\n")
            ch == '\r' -> sb.append("\\r")
            ch == '\t' -> sb.append("\\t")
            ch < ' ' -> sb.append("\\u%04x".format(ch.code))
            else -> sb.append(ch)
        }
    }
    sb.append('"')
}

fun sha256Hex(s: String): String =
    MessageDigest.getInstance("SHA-256").digest(s.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }
