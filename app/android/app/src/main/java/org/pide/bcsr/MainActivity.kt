package org.pide.bcsr

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.pide.bcsr.core.content.CatalogueItem
import org.pide.bcsr.core.data.Retention
import org.pide.bcsr.core.policy.AsrMethod
import org.pide.bcsr.core.policy.City
import org.pide.bcsr.core.recommender.Candidate
import org.pide.bcsr.core.recommender.Feedback
import org.pide.bcsr.core.safety.Purpose
import org.pide.bcsr.core.safety.shownHelplines
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/** Screens as plain Compose state (no navigation library needed for six screens). */
sealed interface Screen : java.io.Serializable { // saveable: rotation must not drop the user off the safety screen (AG-10 R-1)
    data object Onboarding : Screen
    data object Home : Screen
    data object CheckIn : Screen
    data class Recommend(val stateId: String, val words: String) : Screen
    data object Library : Screen
    data object Safety : Screen
    data object Privacy : Screen
    data object About : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as BcsrApp
        setContent {
            var c by remember { mutableStateOf(app.container) }
            // Compose state, so a retry that turns out permanent switches Try again to Start fresh at once (AG-12)
            var permanent by remember { mutableStateOf(app.lockedPermanently) }
            // delete-all builds a new container (AC-FR-25) and bumps the generation, so key() resets screen state to onboarding.
            // A saveable number, not the container, so screen state also survives process death (AG-16 on PR #35, R-1).
            var generation by rememberSaveable { mutableIntStateOf(0) }
            val fresh = { app.startFresh(); c = app.container; permanent = app.lockedPermanently; generation += 1 }
            MaterialTheme {
                c?.let { key(generation) { BcsrUi(it, fresh) } }
                    ?: Locked(permanent, startFresh = fresh, tryAgain = { app.openContainer(); c = app.container; permanent = app.lockedPermanently })
            }
        }
    }
}

@Composable
private fun BcsrUi(c: AppContainer, deleteAll: () -> Unit) {
    var screen by rememberSaveable { mutableStateOf(if (c.settings().onboarded) Screen.Home else Screen.Onboarding) }
    val go: (Screen) -> Unit = { screen = it }
    BackHandler(enabled = screen != Screen.Home && screen != Screen.Onboarding) { screen = Screen.Home }
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // E2: "I need help now" one tap from every screen, onboarding included (AG-10 R-2, Project Lead 2026-10-27)
        if (screen != Screen.Safety)
            Button({ go(Screen.Safety) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.need_help)) }
        when (val s = screen) {
            Screen.Onboarding -> Onboarding(c) { go(Screen.Home) }
            Screen.Home -> Home(go)
            Screen.CheckIn -> CheckIn(c, go)
            is Screen.Recommend -> RecommendationScreen(c, s, go)
            Screen.Library -> Library(c, go)
            Screen.Safety -> SafetyScreen(c, go)
            Screen.Privacy -> Privacy(c, go, deleteAll)
            Screen.About -> About(go)
        }
    }
}

@Composable private fun Title(text: String) =
    Text(text, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics { heading() })

// ---------- UX-01 onboarding ----------
@Composable
private fun Onboarding(c: AppContainer, done: () -> Unit) {
    val ctx = LocalContext.current
    var step by remember { mutableIntStateOf(0) }
    val askNotif = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { step++ }
    val s = c.settings()
    when (step) {
        0 -> { Title(stringResource(R.string.welcome_title)); Text(stringResource(R.string.welcome_body)); Text(stringResource(R.string.not_medical))
            Button({ step++ }) { Text(stringResource(R.string.next)) } }
        1 -> { Title(stringResource(R.string.how_title)); Text(stringResource(R.string.how_body)); Button({ step++ }) { Text(stringResource(R.string.next)) } }
        // Disclosure immediately before the permission request; navigating away is not consent (SPK-02).
        2 -> { Title(stringResource(R.string.notif_title)); Text(stringResource(R.string.notif_body))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button({ if (Build.VERSION.SDK_INT >= 33) askNotif.launch(Manifest.permission.POST_NOTIFICATIONS) else step++ }) { Text(stringResource(R.string.allow)) }
                OutlinedButton({ step++ }) { Text(stringResource(R.string.not_now)) }
            } }
        3 -> { Title(stringResource(R.string.city_title)); Text(stringResource(R.string.city_body))
            var city by remember { mutableStateOf(s.city) }
            City.entries.forEach { ci -> FilterChip(city == ci, { city = ci; s.city = ci }, { Text(ci.label) }) }
            Title(stringResource(R.string.asr_title))
            var asr by remember { mutableStateOf(s.asr) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(asr == AsrMethod.STANDARD, { asr = AsrMethod.STANDARD; s.asr = asr }, { Text(stringResource(R.string.asr_standard)) })
                FilterChip(asr == AsrMethod.HANAFI, { asr = AsrMethod.HANAFI; s.asr = asr }, { Text(stringResource(R.string.asr_hanafi)) })
            }
            Title(stringResource(R.string.quiet_title))
            var quiet by remember { mutableStateOf(s.quietStart to s.quietEnd) }
            listOf(LocalTime.of(23, 0) to LocalTime.of(6, 0), LocalTime.of(22, 0) to LocalTime.of(7, 0)).forEach { q ->
                FilterChip(quiet == q, { quiet = q; s.quietStart = q.first; s.quietEnd = q.second }, { Text("${q.first}–${q.second}") })
            }
            Button({ step++ }) { Text(stringResource(R.string.next)) } }
        else -> { Title(stringResource(R.string.research_title)); Text(stringResource(R.string.research_body))
            Button({ s.onboarded = true; CheckInWorker.schedule(ctx); done() }) { Text(stringResource(R.string.start)) } }
    }
}

// ---------- Home ----------
@Composable
private fun Home(go: (Screen) -> Unit) {
    Title(stringResource(R.string.app_name))
    Button({ go(Screen.CheckIn) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.how_am_i)) }
    OutlinedButton({ go(Screen.Library) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.library)) }
    OutlinedButton({ go(Screen.Privacy) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.privacy)) }
    OutlinedButton({ go(Screen.About) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.about)) }
    Text(stringResource(R.string.not_medical), style = MaterialTheme.typography.bodySmall)
}

// ---------- UX-02 check-in (≤ 3 taps to save) ----------
private val STATES = listOf(
    "CS-02" to R.string.state_worry, "CS-03" to R.string.state_restless, "CS-04" to R.string.state_low, "CS-05" to R.string.state_angry,
    "CS-06" to R.string.state_sleepless, "CS-07" to R.string.state_lonely, "CS-08" to R.string.state_tired, "CS-09" to R.string.state_grateful,
)

@Composable
private fun CheckIn(c: AppContainer, go: (Screen) -> Unit) {
    var mood by remember { mutableIntStateOf(3) }
    var energy by remember { mutableIntStateOf(3) }
    var state by remember { mutableStateOf<Pair<String, Int>?>(null) }
    val ctx = LocalContext.current
    Title(stringResource(R.string.checkin_title))
    Text(stringResource(R.string.mood)); Scale(mood) { mood = it }
    Text(stringResource(R.string.energy)); Scale(energy) { energy = it }
    Text(stringResource(R.string.what_fits))
    STATES.forEach { st -> FilterChip(state == st, { state = if (state == st) null else st }, { Text(stringResource(st.second)) }) }
    // "I'm not safe" is always last and opens the safety pathway first (SAF-01, SR-03).
    OutlinedButton({ go(Screen.Safety) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.state_not_safe)) }
    Button({
        val sid = state?.first ?: "CS-01"
        c.store.record("label", listOf("ts", "valence", "energy", "state_id", "source"), listOf(System.currentTimeMillis(), mood, energy, sid, "EMA"))
        go(Screen.Recommend(sid, state?.let { ctx.getString(it.second).lowercase() } ?: "okay"))
    }) { Text(stringResource(R.string.save)) }
}

@Composable private fun Scale(value: Int, set: (Int) -> Unit) = Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
    (1..5).forEach { v -> FilterChip(value == v, { set(v) }, { Text("$v") }) }
}

// ---------- UX-03 recommendation card ----------
@Composable
private fun RecommendationScreen(c: AppContainer, s: Screen.Recommend, go: (Screen) -> Unit) {
    val now = remember(s) { LocalDateTime.now() }
    val nowMs = remember(s) { System.currentTimeMillis() } // same ts for the stored row and its feedback
    val quiet = remember(s) { nowMs < c.settings().quietAfterSafetyUntil } // no recommendation for 24 h after the safety screen (SAF-01 §3)
    if (quiet) { // signed copy only (SAF-01 §4 Body); the reason is not "no du'a" (AG-10 R-4)
        Text(stringResource(R.string.safety_body)); HelplineButtons(); TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }; return
    }
    val rec = remember(s) {
        run {
            val shown = c.store.recommendations().associate { it.itemId to LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(it.ts), ZoneId.systemDefault()) }
            val items = c.catalogue.approvedItemsFor(s.stateId).map { Candidate(it.id, it.states, it.approved) }
            c.recommender.recommend(s.stateId, s.words, now, items, shown)?.also {
                c.store.record("recommendation", listOf("ts", "item_id", "state_id", "policy_version"), listOf(nowMs, it.itemId, s.stateId, "ts-bandit-0.1"))
            }
        }
    }
    if (rec == null) { Text(stringResource(R.string.no_item)); TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }; return }
    val item = c.catalogue.approvedItems().first { it.id == rec.itemId }
    ItemCard(c, item)
    var showWhy by remember { mutableStateOf(false) }
    TextButton({ showWhy = !showWhy }) { Text(stringResource(R.string.why)) }
    if (showWhy) Text(rec.why)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(Feedback.HELPFUL to R.string.helpful, Feedback.NOT_NOW to R.string.not_now, Feedback.NOT_RELEVANT to R.string.not_relevant).forEach { (f, label) ->
            OutlinedButton({ c.recommender.feedback(s.stateId, rec.itemId, f); c.store.setFeedback(nowMs, rec.itemId, f.name); go(Screen.Home) }) { Text(stringResource(label)) }
        }
    }
}

/** CON-01 §4: Arabic first, then transliteration (an aid), translation and the full reference; text exactly as in the catalogue. */
@Composable
private fun ItemCard(c: AppContainer, item: CatalogueItem) {
    var fav by remember(item.id) { mutableStateOf(c.store.isFavourite(item.id)) }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item.text("title", "en")?.let { Title(it) }
            item.text("arabic")?.let { Text(it, fontSize = 24.sp, style = MaterialTheme.typography.bodyLarge.copy(textDirection = TextDirection.Rtl)) }
            item.text("transliteration", "en")?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            item.text("translation", "en")?.let { Text(it) }
            Text(referenceOf(item), style = MaterialTheme.typography.bodySmall)
            TextButton({ fav = !fav; c.store.setFavourite(item.id, fav) }) { Text(stringResource(if (fav) R.string.saved else R.string.save_favourite)) }
        }
    }
}

private fun referenceOf(item: CatalogueItem): String {
    val r = item.json["reference"] as? JsonObject ?: return ""
    fun f(k: String) = r[k]?.jsonPrimitive?.contentOrNull.orEmpty()
    return if (f("kind") == "quran") "Qur'an ${f("surah")}:${f("ayah_from")}–${f("ayah_to")}"
    else "${f("collection")} ${f("number")} (${f("numbering_system")}) · ${f("grading")}, ${f("grader")}"
}

// ---------- Library (FR-20) ----------
@Composable
private fun Library(c: AppContainer, go: (Screen) -> Unit) {
    var q by remember { mutableStateOf("") }
    Title(stringResource(R.string.library))
    OutlinedTextField(q, { q = it }, label = { Text(stringResource(R.string.search)) }, modifier = Modifier.fillMaxWidth())
    val items = remember(c, q) { c.catalogue.search(q) }
    if (items.isEmpty()) Text(stringResource(R.string.library_empty))
    items.forEach { ItemCard(c, it) }
    TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }
}

// ---------- store cannot be decrypted (T-M5-025, T-M5-019 S-06) ----------
@Composable
private fun Locked(permanent: Boolean, startFresh: () -> Unit, tryAgain: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        var confirm by remember { mutableStateOf(false) }
        Title(stringResource(R.string.locked_title))
        Text(stringResource(if (permanent) R.string.locked_body else R.string.locked_retry_body))
        // help stays one screen away even with no store (AC-SR-01)
        Text(stringResource(R.string.safety_body))
        HelplineButtons()
        // deletion only when the data can never be read; a passing error gets "try again" (AG-16 on PR #31)
        if (permanent) OutlinedButton({ confirm = true }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.start_fresh)) }
        else OutlinedButton(tryAgain, Modifier.fillMaxWidth()) { Text(stringResource(R.string.try_again)) }
        Text(stringResource(R.string.not_medical), style = MaterialTheme.typography.bodySmall)
        if (confirm) AlertDialog(
            onDismissRequest = { confirm = false },
            text = { Text(stringResource(R.string.start_fresh_confirm)) },
            confirmButton = { TextButton({ confirm = false; startFresh() }) { Text(stringResource(R.string.start_fresh)) } },
            dismissButton = { TextButton({ confirm = false }) { Text(stringResource(R.string.cancel)) } },
        )
    }
}

/** Emergency and helpline buttons from the verified directory; opens the dialer, no CALL permission (SAF-01). */
@Composable
private fun HelplineButtons() {
    val ctx = LocalContext.current
    val lines = shownHelplines(LocalDate.now(), devBuild = BuildConfig.DEBUG)
    // no dialer (e.g. a Wi-Fi tablet) must not crash the crisis path; the number stays readable on the button (HC-06 F-2)
    fun dial(number: String) = try {
        ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    } catch (e: android.content.ActivityNotFoundException) {
        Unit
    }
    lines.filter { it.purpose == Purpose.EMERGENCY }.forEach { h ->
        Button({ dial(h.number) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.call_emergency, h.number)) }
    }
    lines.filter { it.purpose == Purpose.MENTAL_HEALTH }.forEach { h ->
        Button({ dial(h.number) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.talk_to, "${h.name} ${h.number}")) }
    }
}

// ---------- UX-04 / SAF-01 safety ----------
@Composable
private fun SafetyScreen(c: AppContainer, go: (Screen) -> Unit) {
    // off the main thread so a running VACUUM never delays the helplines (S-11)
    var counted by rememberSaveable { mutableStateOf(false) } // once per opening, not again after a rotation
    LaunchedEffect(Unit) {
        if (counted) return@LaunchedEffect
        counted = true
        // NonCancellable: leaving the screen at once must not skip the 24 h quiet (SAF-01 §3); a closed store must not crash it
        withContext(NonCancellable + Dispatchers.IO) {
            runCatching { c.settings().apply { safetyCount += 1; quietAfterSafetyUntil = System.currentTimeMillis() + 24 * 3_600_000L } }
        }
    }
    var safe by rememberSaveable { mutableStateOf(false) }
    var showDua by rememberSaveable { mutableStateOf(false) }
    Title(stringResource(R.string.safety_heading))
    Text(stringResource(R.string.safety_body))
    HelplineButtons()
    if (!safe) OutlinedButton({ safe = true }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.safe_for_now)) }
    else {
        // Only after help is offered, and only an approved item mapped to the crisis state (SAF-01 §3).
        val dua = c.catalogue.approvedItemsFor("CS-10").firstOrNull()
        // the du'a is shown only if the user says yes to the signed offer (SAF-01 §3-§4, AG-10 R-3)
        if (dua != null) {
            if (!showDua) OutlinedButton({ showDua = true }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.offer_dua)) }
            else ItemCard(c, dua)
        }
        TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }
    }
    Text(stringResource(R.string.not_medical), style = MaterialTheme.typography.bodySmall)
}

// ---------- UX-05 Privacy Center ----------
@Composable
private fun Privacy(c: AppContainer, go: (Screen) -> Unit, deleteAll: () -> Unit) {
    val ctx = LocalContext.current
    // S-11: store reads and the export run off the main thread (the worker's VACUUM can hold the connection)
    val counts by produceState(emptyMap<String, Long>(), c) { value = withContext(Dispatchers.IO) { runCatching { c.store.counts() }.getOrDefault(emptyMap()) } }
    val scope = rememberCoroutineScope()
    var paused by remember { mutableStateOf(c.settings().paused) }
    var confirm by remember { mutableIntStateOf(0) }
    Title(stringResource(R.string.what_we_store))
    counts.forEach { (table, n) ->
        val kept = stringResource(when (table) { in Retention.RAW_TABLES -> R.string.kept_raw; in Retention.DERIVED_TABLES -> R.string.kept_derived; else -> R.string.kept_until })
        Text("$table: $n · $kept")
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.pause)); Switch(paused, { paused = it; c.settings().paused = it })
    }
    OutlinedButton({
        scope.launch {
            try {
                val f = withContext(Dispatchers.IO) {
                    c.store.exportTo(File(ctx.cacheDir, "export").apply { mkdirs() }.resolve("bcsr-export-${System.currentTimeMillis()}.txt"))
                }
                val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.export", f)
                ctx.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), null))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) { // I/O error, or the store closed by a concurrent delete-all
                Toast.makeText(ctx, R.string.export_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }) { Text(stringResource(R.string.export)) }
    OutlinedButton({ confirm = 1 }) { Text(stringResource(R.string.delete_all)) }
    Text(stringResource(R.string.not_medical), style = MaterialTheme.typography.bodySmall)
    TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }

    if (confirm == 1) AlertDialog(
        onDismissRequest = { confirm = 0 },
        text = { Text(stringResource(R.string.delete_confirm1)) },
        confirmButton = { TextButton({ confirm = 2 }) { Text(stringResource(R.string.continue_)) } },
        dismissButton = { TextButton({ confirm = 0 }) { Text(stringResource(R.string.cancel)) } },
    )
    if (confirm == 2) {
        var typed by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { confirm = 0 },
            text = { OutlinedTextField(typed, { typed = it }, label = { Text(stringResource(R.string.delete_confirm2)) }) },
            confirmButton = { TextButton({ if (typed == "DELETE") { confirm = 0; deleteAll() } }) { Text(stringResource(R.string.delete_all)) } },
            dismissButton = { TextButton({ confirm = 0 }) { Text(stringResource(R.string.cancel)) } },
        )
    }
}

// ---------- About and credits (AC-SR-02, AC-CR-07, NFR-09) ----------
/** Open-source components shipped in the app and their licences (RES-03 §3.1). */
private val LICENCES = listOf(
    "SQLCipher for Android — BSD-3-Clause (Zetetic LLC)",
    "Bouncy Castle — MIT",
    "kotlinx.serialization — Apache-2.0 (JetBrains)",
    "Adhan — MIT (Batoul Apps)",
    "AndroidX, Jetpack Compose, WorkManager — Apache-2.0 (Google)",
)

@Composable
private fun About(go: (Screen) -> Unit) {
    val ctx = LocalContext.current
    Title(stringResource(R.string.about))
    Text(stringResource(R.string.about_version, BuildConfig.VERSION_NAME))
    Text(stringResource(R.string.not_medical))
    Title(stringResource(R.string.credits))
    // CR-07 / RES-03 §3.2: the Tanzil Project must be named and linked. Opens the browser; the app itself has no network access.
    Text(stringResource(R.string.credit_tanzil))
    Text(stringResource(R.string.tanzil_notice), style = MaterialTheme.typography.bodySmall) // RES-03 §3.2 copyright notice
    TextButton({
        // No browser (work profile, kiosk): the address stays visible as the button text.
        try {
            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://tanzil.net")))
        } catch (e: android.content.ActivityNotFoundException) {
            // nothing to open it with; the address is already on screen
        }
    }) { Text("https://tanzil.net") }
    Title(stringResource(R.string.licences))
    LICENCES.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
    TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }
}

