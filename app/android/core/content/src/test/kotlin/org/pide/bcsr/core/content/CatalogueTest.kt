package org.pide.bcsr.core.content

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogueTest {
    private fun res(name: String) = javaClass.getResource("/fixture/$name")!!.readBytes()
    private val keyLine = res("test-fixture-key.pub").toString(Charsets.UTF_8)
    private val parsedKey = BundleVerifier.parsePublicKey(keyLine)
    private val keyId = parsedKey.first
    private val key = parsedKey.second
    private val bundle = res("catalogue-fixture.json")
    private val sig = res("catalogue-fixture.sig").toString(Charsets.US_ASCII)

    /** api-contracts §2 vectors: the Kotlin canonical form reproduces the Python digests byte for byte. */
    @Test fun hashTestVectors() {
        val v1 = Json.parseToJsonElement(
            """[{"id":"DUA-0001","title":{"en":"Ease","ur":"آسانی"},"verification":{"status":"approved","record_id":"RR-0001"}},""" +
                """{"id":"DUA-0002","repetitions":3,"note":"line\nbreak \"q\""}]""",
        )
        assertEquals("8fed260e4d68a9f7ff9263d63e9550a8aa0e2c8600db8fa16496cbf5ca036549", sha256Hex(canonicalJson(v1)))
        val v2 = Json.parseToJsonElement("""[{"id":"DUA-0003","note":"a\u0001b\u007fc\u0085d e\tf"}]""")
        assertEquals("85e786a052de2576949b0b6c49231c168361b4fdf2c5fcbdd6b3075dca774d38", sha256Hex(canonicalJson(v2)))
    }

    /** Cross-language: a bundle signed by scripts/sign_bundle.py verifies in Kotlin. */
    @Test fun pythonSignedBundleLoads() {
        val r = BundleVerifier(mapOf(keyId to key)).load(bundle, sig)
        assertTrue(r.toString(), r is LoadResult.Loaded)
        assertEquals(listOf("DUA-9001", "DUA-9002"), (r as LoadResult.Loaded).items.map { it.id })
    }

    @Test fun tamperedOrWrongKeyIsRejected() {
        val v = BundleVerifier(mapOf(keyId to key))
        val tampered = bundle.toString(Charsets.UTF_8).replace("Fixture A", "Fixture B").toByteArray()
        assertEquals(LoadResult.Rejected("signature does not match"), v.load(tampered, sig))
        val wrong = BundleVerifier(mapOf(keyId to ByteArray(32) { 7 }))
        assertEquals(LoadResult.Rejected("signature does not match"), wrong.load(bundle, sig))
        assertEquals(LoadResult.Rejected("unknown key_id $keyId"), BundleVerifier(emptyMap()).load(bundle, sig))
    }

    /** ADR-0005 §5: a rejected bundle never replaces the last good one. */
    @Test fun badBundleKeepsLastGood() {
        val repo = CatalogueRepository(BundleVerifier(mapOf(keyId to key)))
        repo.offer(bundle, sig)
        val before = repo.current
        repo.offer("{}".toByteArray(), sig)
        assertEquals(before, repo.current)
        assertEquals(2, repo.approvedItems().size)
    }

    /** ADR-0005 §6: the runtime filter drops a non-approved item even if one is inside a valid bundle. */
    @Test fun runtimeFilterAndStateLookup() {
        val repo = CatalogueRepository(BundleVerifier(mapOf(keyId to key)))
        assertTrue(repo.approvedItems().isEmpty()) // no bundle yet → nothing to show
        repo.offer(bundle, sig)
        assertEquals(listOf("DUA-9001"), repo.approvedItemsFor("CS-02").map { it.id })
        assertEquals(listOf("DUA-9001"), repo.search("fixture a").map { it.id })
        assertNull(repo.approvedItems().firstOrNull { !it.approved })
    }
}
