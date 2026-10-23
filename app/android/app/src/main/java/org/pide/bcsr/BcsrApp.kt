package org.pide.bcsr

import android.app.Application
import android.content.Context
import org.pide.bcsr.core.content.BundleVerifier
import org.pide.bcsr.core.content.CatalogueRepository
import org.pide.bcsr.core.data.BcsrStore
import org.pide.bcsr.core.data.StoreUnavailable
import org.pide.bcsr.core.policy.AsrMethod
import org.pide.bcsr.core.policy.City
import org.pide.bcsr.core.recommender.Feedback
import org.pide.bcsr.core.recommender.Recommender
import java.io.File
import java.time.LocalTime

class BcsrApp : Application() {
    /** Null when the store cannot be opened; the UI then shows the locked screen (T-M5-025). */
    var container: AppContainer? = null
        private set
    /** Why [container] is null: true = the data can never be read (offer start fresh), false = try again. */
    var lockedPermanently = false
        private set

    override fun onCreate() {
        super.onCreate()
        openContainer()
    }

    fun openContainer() {
        container = try {
            AppContainer(this)
        } catch (e: StoreUnavailable) {
            lockedPermanently = e.permanent
            null
        } catch (e: Exception) {
            lockedPermanently = false // any other start-up failure: never crash past the helplines, never offer deletion
            null
        }
    }

    /** Delete-all from the Privacy Center or "start fresh" from the locked screen, then an empty store (or Locked). */
    fun startFresh() {
        runCatching { BcsrStore.deleteEverything(this, container?.store) } // a broken Keystore must not crash here either
        openContainer()
    }
}

/** Manual dependency wiring (ARC-02 §7: no DI framework until the module count needs one). */
class AppContainer(private val context: Context) {
    init {
        BcsrStore.purgeExports(File(context.cacheDir, "export"), System.currentTimeMillis())
    }
    val store: BcsrStore = BcsrStore.open(context)
    val catalogue: CatalogueRepository = loadCatalogue(context)
    val recommender: Recommender = rebuildRecommender()

    fun settings() = AppSettings(store)

    /** Replays stored feedback so the bandit survives restarts (FR-16) without storing its state separately. */
    private fun rebuildRecommender(): Recommender = Recommender().also { r ->
        store.recommendations().forEach { row ->
            // an unknown value (old enum name, bad row) is skipped, never a crash at start
            row.feedback?.let { f -> runCatching { Feedback.valueOf(f) }.getOrNull() }?.let { r.feedback(row.stateId, row.itemId, it) }
        }
    }

    companion object {
        /**
         * Loads `assets/catalogue/catalogue.json` + `.sig` against the public keys (`.pub` files) in `assets/catalogue-keys/`
         * (ADR-0005). None ship yet: no item is scholar-approved, so the library is empty, which is the correct state.
         */
        private fun loadCatalogue(context: Context): CatalogueRepository {
            val assets = context.assets
            // a malformed key file is skipped, not fatal: bundles signed with it are then rejected
            val keys = assets.list("catalogue-keys").orEmpty().filter { it.endsWith(".pub") }.mapNotNull { name ->
                runCatching { BundleVerifier.parsePublicKey(assets.open("catalogue-keys/$name").bufferedReader().readText()) }.getOrNull()
            }.toMap()
            val repo = CatalogueRepository(BundleVerifier(keys))
            val files = assets.list("catalogue").orEmpty()
            if ("catalogue.json" in files && "catalogue.sig" in files) {
                repo.offer(assets.open("catalogue/catalogue.json").readBytes(), assets.open("catalogue/catalogue.sig").bufferedReader().readText())
            }
            return repo
        }
    }
}

/** Typed view over the `setting` table. */
class AppSettings(private val store: BcsrStore) {
    var onboarded: Boolean
        get() = store.setting("onboarded") == "1"
        set(v) = store.putSetting("onboarded", if (v) "1" else "0")
    var city: City
        get() = store.setting("city")?.let { runCatching { City.valueOf(it) }.getOrNull() } ?: City.LAHORE
        set(v) = store.putSetting("city", v.name)
    var asr: AsrMethod
        get() = store.setting("asr")?.let { runCatching { AsrMethod.valueOf(it) }.getOrNull() } ?: AsrMethod.STANDARD
        set(v) = store.putSetting("asr", v.name)
    var quietStart: LocalTime
        get() = LocalTime.parse(store.setting("quiet_start") ?: "23:00")
        set(v) = store.putSetting("quiet_start", v.toString())
    var quietEnd: LocalTime
        get() = LocalTime.parse(store.setting("quiet_end") ?: "06:00")
        set(v) = store.putSetting("quiet_end", v.toString())
    var paused: Boolean
        get() = store.setting("paused") == "1"
        set(v) = store.putSetting("paused", if (v) "1" else "0")
    /** Epoch ms until which no recommendation or notification is sent after the safety screen (SAF-01 §3). */
    var quietAfterSafetyUntil: Long
        get() = store.setting("safety_until")?.toLongOrNull() ?: 0L
        set(v) = store.putSetting("safety_until", v.toString())
    var safetyCount: Int
        get() = store.setting("safety_count")?.toIntOrNull() ?: 0
        set(v) = store.putSetting("safety_count", v.toString())
    var lastRetentionMs: Long
        get() = store.setting("retention_at")?.toLongOrNull() ?: 0L
        set(v) = store.putSetting("retention_at", v.toString())
    fun sentOn(day: String): Int = store.setting("sent_$day")?.toIntOrNull() ?: 0
    fun markSent(day: String) = store.putSetting("sent_$day", (sentOn(day) + 1).toString())
}
