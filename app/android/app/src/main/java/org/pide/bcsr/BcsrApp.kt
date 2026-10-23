package org.pide.bcsr

import android.app.Application
import android.content.Context
import org.pide.bcsr.core.content.BundleVerifier
import org.pide.bcsr.core.content.CatalogueRepository
import org.pide.bcsr.core.data.BcsrStore
import org.pide.bcsr.core.policy.AsrMethod
import org.pide.bcsr.core.policy.City
import org.pide.bcsr.core.recommender.Feedback
import org.pide.bcsr.core.recommender.Recommender
import java.io.File
import java.time.LocalTime

class BcsrApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

/** Manual dependency wiring (ARC-02 §7: no DI framework until the module count needs one). */
class AppContainer(private val context: Context) {
    init {
        File(context.cacheDir, "export").deleteRecursively() // TH-10: a plaintext export does not outlive the session that shared it
    }
    var store: BcsrStore = BcsrStore.open(context)
        private set
    val catalogue: CatalogueRepository = loadCatalogue(context)
    var recommender: Recommender = rebuildRecommender()
        private set

    fun settings() = AppSettings(store)

    /** Replays stored feedback so the bandit survives restarts (FR-16) without storing its state separately. */
    private fun rebuildRecommender(): Recommender = Recommender().also { r ->
        store.recommendations().forEach { row -> row.feedback?.let { f -> r.feedback(row.stateId, row.itemId, Feedback.valueOf(f)) } }
    }

    /** Delete-all (FR-25, ARC-04 §3), then a fresh empty store for onboarding. */
    fun deleteEverything() {
        BcsrStore.deleteEverything(context, store)
        store = BcsrStore.open(context)
        recommender = Recommender() // learned preferences are personal data too (AC-FR-25)
    }

    companion object {
        /**
         * Loads `assets/catalogue/catalogue.json` + `.sig` against the public keys (`.pub` files) in `assets/catalogue-keys/`
         * (ADR-0005). None ship yet: no item is scholar-approved, so the library is empty, which is the correct state.
         */
        private fun loadCatalogue(context: Context): CatalogueRepository {
            val assets = context.assets
            val keys = assets.list("catalogue-keys").orEmpty().filter { it.endsWith(".pub") }.associate { name ->
                BundleVerifier.parsePublicKey(assets.open("catalogue-keys/$name").bufferedReader().readText())
            }
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
