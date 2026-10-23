# HC-06 Human Review Pack: Locked screen (T-M5-025)

**From:** AG-10 (SK-10 safety-wellbeing-review) · **To:** Project Lead, as safety reviewer under D-011 (no mental-health advisor) · **Prepared:** 2026-10-23
**Task:** T-M5-025 (closes T-M5-019 finding S-06) · **Requirements:** SR-01..SR-05, FR-24, PR-02 · **Related:** SAF-01 v1.0 (signed), SAF-02 v1.0-rc, AGENTS.md §4, SK-10 step 3 and step 4
**Why this pack exists:** T-M5-025 adds a new screen that shows safety copy and helplines. SAF-01 §5 says any change to the pathway's flow or copy needs the Project Lead's signature again (SR-04, AC-SR-04).

**AG-10 verdict: fit to present for sign-off. Nothing blocks presentation.** The Locked screen keeps help one screen away, makes no claims about how the user feels, offers no du'a, and repeats the signed SAF-01 lines word for word. Two major findings (F-1 and F-2) are shared with the existing safety screen and must be fixed or accepted before **release**. One major finding (F-3) is a governance question: whether this pack is enough or SAF-01 must be amended. AG-10 does not approve its own review; the decisions in §5 are the Project Lead's.

---

## 1. Screens under review

### 1.1 The Locked screen and its helper (`app/android/app/src/main/java/org/pide/bcsr/MainActivity.kt`)

When it shows (MainActivity.kt:85-86): the app shows the Locked screen whenever `app.container` is null, meaning the encrypted store could not be opened.

```kotlin
                c?.let { key(it) { BcsrUi(it, fresh) } }
                    ?: Locked(app.lockedPermanently, startFresh = fresh, tryAgain = { app.openContainer(); c = app.container })
```

Lines 256-291, verbatim:

```kotlin
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
    fun dial(number: String) = ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    lines.filter { it.purpose == Purpose.EMERGENCY }.forEach { h ->
        Button({ dial(h.number) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.call_emergency, h.number)) }
    }
    lines.filter { it.purpose == Purpose.MENTAL_HEALTH }.forEach { h ->
        Button({ dial(h.number) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.talk_to, "${h.name} ${h.number}")) }
    }
}
```

The order on screen is: title, explanation, safety line, emergency and helpline buttons, Start fresh or Try again, then the not-medical line. Start fresh opens a confirmation dialog.

### 1.2 The existing safety screen, for comparison (MainActivity.kt:293-309, verbatim)

```kotlin
// ---------- UX-04 / SAF-01 safety ----------
@Composable
private fun SafetyScreen(c: AppContainer, go: (Screen) -> Unit) {
    remember { c.settings().apply { safetyCount += 1; quietAfterSafetyUntil = System.currentTimeMillis() + 24 * 3_600_000L } }
    var safe by remember { mutableStateOf(false) }
    Title(stringResource(R.string.safety_heading))
    Text(stringResource(R.string.safety_body))
    HelplineButtons()
    if (!safe) OutlinedButton({ safe = true }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.safe_for_now)) }
    else {
        // Only after help is offered, and only an approved item mapped to the crisis state (SAF-01 §3).
        val dua = c.catalogue.approvedItemsFor("CS-10").firstOrNull()
        if (dua != null) { Text(stringResource(R.string.offer_dua)); ItemCard(c, dua) }
        TextButton({ go(Screen.Home) }) { Text(stringResource(R.string.back)) }
    }
    Text(stringResource(R.string.not_medical), style = MaterialTheme.typography.bodySmall)
}
```

| Aspect | SafetyScreen (signed SAF-01 flow) | Locked (new) |
|---|---|---|
| Opened by | Explicit user input (E1 check-in crisis answer, E2 "I need help now") | A storage failure at start-up; no user input |
| Heading | `safety_heading` | `locked_title` (technical, no feeling named) |
| `safety_body` and `HelplineButtons()` | Yes | Yes, the same composable |
| "I'm safe for now", then the du'a offer | Yes, du'a only after help | **No.** No du'a anywhere (the catalogue is not loaded) |
| Local counter and 24 h quiet | Yes | No (no store; logs nothing, which is consistent with AC-FR-24) |
| `not_medical` | Yes | Yes |

### 1.3 The logic behind the screen

The packet points to `BcsrStore.kt` for `openContainer` and `startFresh`. They are in fact in `app/android/app/src/main/java/org/pide/bcsr/BcsrApp.kt`.

- `BcsrApp.openContainer` (BcsrApp.kt:30-42): if `StoreUnavailable` is thrown, it sets `lockedPermanently = e.permanent`. Any other exception sets `lockedPermanently = false`, so the user sees Try again and is never offered deletion. The container is null in both cases. The app never crashes before the helplines.
- `BcsrApp.startFresh` (BcsrApp.kt:45-48): `runCatching { BcsrStore.deleteEverything(...) }`, then `openContainer()`. A deletion failure is swallowed; this is recorded as S-13 (Low, open, scheduled with T-M5-020).
- `BcsrStore.open` (core/data/.../BcsrStore.kt:92-102): turns any open failure into `StoreUnavailable(e, permanent = isPermanentKeyFailure(e))`.
- `isPermanentKeyFailure` (BcsrStore.kt:162-168): walks up to 10 causes looking for `AEADBadTagException`, `KeyPermanentlyInvalidatedException`, `UnrecoverableKeyException`, or the message "file is not a database". Unit-tested in `core/data/src/test/.../RetentionTest.kt:36-41`. Disk full, "database is locked" and Keystore `ProviderException` count as temporary.
- Background check-in worker (CheckIns.kt:59): returns early when the container is null, so a locked store sends no notifications.

### 1.4 Strings under review (verbatim)

English, `app/android/app/src/main/res/values/strings.xml`:

| Line | Key | Text |
|---|---|---|
| 91 | `locked_title` | `Your saved data can\'t be opened` |
| 92 | `locked_body` | `This phone can no longer unlock what BCSR saved, so it cannot be read by anyone, including you. You can start fresh: BCSR will delete the unreadable data and set up again.` |
| 93 | `locked_retry_body` | `BCSR could not open your saved data just now. Nothing has been deleted. Please try again in a moment.` |
| 94 | `try_again` | `Try again` |
| 95 | `start_fresh` | `Start fresh` |
| 96 | `start_fresh_confirm` | `This deletes the unreadable data and starts BCSR again from the beginning. It cannot be undone.` |
| 62 | `safety_body` | `If you might be in danger, please call for help now.` |
| 5 | `not_medical` | `BCSR is not a medical or counselling service.` |
| 63 | `call_emergency` (used by HelplineButtons) | `Call emergency (%1$s)` |
| 64 | `talk_to` (used by HelplineButtons) | `Talk to someone: %1$s` |
| 89 | `cancel` (dialog) | `Cancel` |

Urdu draft, `app/android/app/src/main/res/values-ur/strings.xml`:

| Line | Key | Text |
|---|---|---|
| 13 | `locked_title` | `آپ کا محفوظ ڈیٹا نہیں کھل سکا` |
| 14 | `locked_body` | `یہ فون اب BCSR کا محفوظ کیا ہوا ڈیٹا نہیں کھول سکتا، اس لیے اسے کوئی نہیں پڑھ سکتا، آپ بھی نہیں۔ آپ نئے سرے سے شروع کر سکتے ہیں: BCSR یہ ڈیٹا حذف کر کے دوبارہ ترتیب دے گا۔` |
| 15 | `locked_retry_body` | `BCSR ابھی آپ کا محفوظ ڈیٹا نہیں کھول سکا۔ کچھ بھی حذف نہیں ہوا۔ براہِ کرم تھوڑی دیر بعد دوبارہ کوشش کریں۔` |
| 16 | `try_again` | `دوبارہ کوشش کریں` |
| 17 | `start_fresh` | `نئے سرے سے شروع کریں` |
| 18 | `start_fresh_confirm` | `اس سے یہ ڈیٹا حذف ہو جائے گا اور BCSR شروع سے دوبارہ کھلے گا۔ یہ واپس نہیں ہو سکتا۔` |
| 7 | `safety_body` | `اگر آپ کو خطرہ ہو سکتا ہے تو ابھی مدد کے لیے کال کریں۔` |
| 5 | `not_medical` | `BCSR کوئی طبی یا کاؤنسلنگ سروس نہیں ہے۔` |
| 8 | `call_emergency` | `ایمرجنسی کال کریں (%1$s)` |
| 9 | `talk_to` | `کسی سے بات کریں: %1$s` |
| 19 | `cancel` | `منسوخ کریں` |

`safety_body`, `not_medical`, `call_emergency` and `talk_to` match the SAF-01 §4 table exactly, in English and in Urdu. The six `locked_*` / `try_again` / `start_fresh*` strings are **new** and do not appear in SAF-01.

## 2. Sources attached

- SAF-01 `docs/06-security-privacy-safety/safety/safety-pathway.md` (v1.0, signed 2026-10-21, PR #18)
- SAF-02 `docs/06-security-privacy-safety/safety/helpline-directory.md` (v1.0-rc): 1122 Verified; 1166 Partly verified; Umang and 15 Not verified; release blocked until 1166 is call-tested
- `app/android/core/safety/src/main/kotlin/org/pide/bcsr/core/safety/Helplines.kt` (`shownHelplines`, `releaseBlockers`, `MAX_AGE_DAYS = 90`)
- `docs/01-requirements/SRS.md:113,147-151` (FR-24, SR-01..SR-05) and `docs/01-requirements/acceptance-criteria.md` (AC-FR-24, AC-SR-01..05)
- `docs/06-security-privacy-safety/security-review-M5.md` S-06 (High, fixed in code) and S-13 (Low, open)
- `scripts/check_android_gates.py:198-204`: CI gate requires `HelplineButtons()` in both `SafetyScreen` and `Locked`, and `not_medical` on the listed screens
- `docs/09-release-operations/release-checklist.md:15`: manual release item "`releaseBlockers()` returns nothing for today's date"

## 3. Checks

### 3.1 SR-01..SR-05 and FR-24 (SK-10 checklist)

| Item | Result | Evidence |
|---|---|---|
| Crisis indicators defined | N/A for this screen, which is not a crisis entry. It does not count as one, set the 24 h quiet, or infer anything (SR-03 holds) | MainActivity.kt:258-277; SAF-01 §2 |
| SR-01 / AC-FR-24: helplines and emergency action shown immediately | **Pass with conditions.** On this screen they take no taps at all. Conditions: F-1 (empty list at runtime) and F-2 (crash with no dialer app) | MainActivity.kt:264-265, 281-291 |
| SR-02: not a medical service | Pass | MainActivity.kt:269; en strings.xml:5 |
| SR-03: explicit input only | Pass. The screen appears on a storage failure, never on inferred mood, and no pathway state is recorded | BcsrApp.kt:30-42 |
| SR-04 / AC-SR-04: copy approved by the Project Lead and matching the signed document | **Pending.** This pack asks for it. The new strings are not in SAF-01 (F-3) | §1.4 |
| SR-05 / AC-SR-01: helplines current, within 90 days | Pass at runtime (the filter is applied), but see F-1 for what happens after the window passes | Helplines.kt:24-33 |
| Human-help CTA present | Pass (same buttons as the safety screen) | MainActivity.kt:265 |
| Helplines verified | Directory-level: only 1122 is fully verified; release is already blocked by SAF-02 | helpline-directory.md:9-19 |
| No diagnostic language | Pass | §1.4 |

### 3.2 Tone and content

- **Ask, not assert; no feeling named.** Pass. The `locked_*` strings describe the device and the data ("This phone can no longer unlock…"), never the user's state. The only safety sentence is the signed conditional "If you might be in danger…".
- **No blame.** Pass. The cause is put on the phone, not the user. Nothing religious is said and nothing suggests the user did something wrong.
- **No du'a-only handling.** Pass. The screen has no du'a, no catalogue access, and no Qur'an or Hadith text. Help is the only content beyond the technical message.
- **No contradiction of SAF-01.** No contradiction in wording. The gaps are in what SAF-01 *covers* (F-3) and in its "still shows the emergency action" rule (F-1).
- **Fail-closed when no verified entry exists (SK-10 step 4).** **Not met at runtime**; see F-1. At release time it is covered only by a manual checklist item.

## 4. Findings, ranked

| # | Severity | Location | Finding | Suggested fix |
|---|---|---|---|---|
| F-1 | **Major** (release-blocking unless accepted; shared with SafetyScreen) | MainActivity.kt:283-290; Helplines.kt:26, 32-33 | `HelplineButtons` shows only what `shownHelplines` returns, and has no fallback. On an installed release build, every directory entry ages out 90 days after its check date (from 2027-01-19 for the 2026-10-21 entries). After that the Locked and safety screens say "please call for help now" with **no button and no number**. SAF-01 §3 says that if no verified helpline exists, the screen "still shows the emergency action". SK-10 step 4 requires failing closed. Today a release build shows only 1122; 1166 appears only in debug builds. The release block itself depends on a manual checklist item (release-checklist.md:15); `releaseBlockers()` is not run in CI. | Always render the emergency action, even when the directory is empty or stale: either keep 1122 as a hard-coded floor, or show a plain-text instruction to dial the local emergency number. Add a CI step or unit test that fails the release build when `releaseBlockers(buildDate)` is not empty. |
| F-2 | **Major** (shared with SafetyScreen) | MainActivity.kt:284 | `ctx.startActivity(Intent.ACTION_DIAL …)` has no `try` and no `resolveActivity` check. On a device with no dialer (Wi-Fi tablets, some work profiles) it throws `ActivityNotFoundException` and the app crashes, which is the failure T-M5-025 set out to remove. The number stays readable on the button label, but the crash still happens on the crisis path. | Catch `ActivityNotFoundException` and keep the number visible as text, or offer to copy it. |
| F-3 | **Major** (governance) | safety-pathway.md:14-20, 36-47, 51 | SAF-01 lists only two ways into the pathway (E1, E2) and only the safety-screen copy. The Locked screen is a new surface that shows helplines and `safety_body` without explicit user input, and it adds six new strings. AC-SR-04 compares release copy with "the version signed". Without an amendment, the Locked copy has no signed baseline to check against. | The Project Lead decides (Q1) whether signing this pack is enough, or SAF-01 moves to v1.1 with a "Locked (store unavailable)" surface and its strings, and is signed again. AG-10 recommends v1.1. |
| F-4 | Minor | MainActivity.kt:262-265; en strings.xml:62 | The crisis sentence "If you might be in danger, please call for help now." appears to **every** user whose store fails, right after a technical message. Most of them are not in crisis. It is a conditional and does not assert anything, but the sudden change in topic may alarm some readers. Without it, though, the helpline buttons would appear with no context. | Q2: keep the line as it is, or add a softer bridging line (new copy, needs SAF-01 wording and an Urdu check). AG-10 slightly prefers keeping the signed line rather than writing new safety copy. |
| F-5 | Minor | en strings.xml:92; MainActivity.kt:262 | In the permanent case, the explanation (two sentences) comes before the helplines. On a small screen with large fonts, the buttons may fall below the fold. SAF-01 asks for "one screen, no scrolling needed" for the safety screen. | Q3: check on a small device at the largest font size (T-M5-014 device run), or move `safety_body` and the buttons above the explanation. |
| F-6 | Minor | MainActivity.kt:289; Helplines.kt:20 | The helpline name ("National Mental Health Helpline") is an English literal, so the Urdu label mixes scripts. Pre-existing. In a release build today it would not show, because 1166 is only partly verified. | Add Urdu names to the directory before 1166 becomes Verified. |
| F-7 | Minor (recorded as S-13) | BcsrApp.kt:45-48 | If start fresh fails silently, the user may land back on Locked with no explanation. When the failure is temporary, the user only ever gets Try again, with no way out after repeated failures. The helplines stay reachable in both cases, so this is a wellbeing and UX problem, not a safety one. | Already scheduled with T-M5-020; no new action. |
| F-8 | Minor (Urdu: needs a native speaker) | values-ur/strings.xml:13-18 | See §4.1. AG-10 does not assert that the Urdu is fluent. | A native-speaker check before release (SAF-01 §4 already requires this). |
| F-9 | Nit | scripts/check_android_gates.py:188-189 | The automated feeling-word check covers only `notif_text`. The new `locked_*` strings pass a manual check, but nothing would stop a later edit from adding feeling words. | Optionally extend the check to the `locked_*` keys. |

### 4.1 Urdu items for a native speaker (not asserted by AG-10)

| Key (ur line) | What to check |
|---|---|
| `locked_title` (13) | `محفوظ` means both "saved" and "safe". `آپ کا محفوظ ڈیٹا` may read as "your safe/secure data". The same word means "safe" in `safe_for_now` (`میں ابھی محفوظ ہوں`) on the safety screen. Would `محفوظ کیا ہوا ڈیٹا` (as in line 14) be clearer? |
| `locked_body` (14) | Is `دوبارہ ترتیب دے گا` natural for "set up again"? Is the sentence too long for a distressed reader? Is `آپ بھی نہیں` idiomatic for "including you"? |
| `start_fresh_confirm` (18) | The English says "the **unreadable** data"; the Urdu says `یہ ڈیٹا` ("this data"). Is that drift acceptable? Does `شروع سے دوبارہ کھلے گا` ("will open again from the start") carry "starts again from the beginning"? Does `یہ واپس نہیں ہو سکتا` clearly mean "cannot be undone"? |
| `locked_retry_body` (15) | Is the tone gentle? Is the masculine `سکا` right with BCSR as subject? |
| 14, 15, 18, 5 | The Latin "BCSR" sits inside right-to-left sentences. Check on a device that the punctuation and the word order render correctly. |
| `start_fresh`, `try_again`, `cancel` (17, 16, 19) | Check that these are the usual button wording. |

## 5. Questions for the Project Lead

1. **Q1 (F-3):** Does signing this pack approve the Locked screen copy and surface? Or should SAF-01 be amended to v1.1 (adding a "Locked" surface to §2 and its strings to §4) and signed again, so that AC-SR-04 has a baseline?
2. **Q2 (F-4):** Should every user whose store fails see "If you might be in danger, please call for help now."? Or do you want different bridging copy on this screen?
3. **Q3 (F-5):** Should the helplines come **before** the technical explanation on the Locked screen, so they are never below the fold?
4. **Q4 (F-1):** When the directory has no current verified entry, what must the screen show: a hard-coded 1122 floor, a plain "dial your local emergency number" line, or something else? Should a stale directory fail the build in CI, rather than rely on the manual release checklist?
5. **Q5 (F-2):** Do you accept fixing the no-dialer crash as a condition of release?
6. **Q6:** Is the wording of `locked_body` ("cannot be read by anyone, including you") the honest, reassuring privacy message you want? Or might it alarm users into thinking their data was lost or taken?
7. **Q7 (F-8):** Who will do the native Urdu check, and by when? It is required before release by SAF-01 §4.

## 6. Decision table (for the Project Lead to fill)

Options: **Approve / Approve with changes / Reject**. AG-10 has left every decision blank.

| # | Item | Decision | Changes required / notes |
|---|---|---|---|
| D-1 | `locked_title` (en 91 / ur 13) | | |
| D-2 | `locked_body` (en 92 / ur 14) | | |
| D-3 | `locked_retry_body` (en 93 / ur 15) | | |
| D-4 | `try_again` (en 94 / ur 16) | | |
| D-5 | `start_fresh` (en 95 / ur 17) | | |
| D-6 | `start_fresh_confirm` (en 96 / ur 18) | | |
| D-7 | Reuse of `safety_body` on the Locked screen (en 62 / ur 7), Q2 | | |
| D-8 | Reuse of `not_medical` on the Locked screen (en 5 / ur 5) | | |
| D-9 | Locked screen layout and order (MainActivity.kt:258-277), Q3 | | |
| D-10 | Temporary/permanent split: start fresh only when the data can never be read (BcsrApp.kt:30-48, BcsrStore.kt:162-168) | | |
| D-11 | F-1: fail-closed fallback when no verified helpline exists, Q4 | | |
| D-12 | F-2: no-dialer crash, Q5 | | |
| D-13 | F-3: this pack alone, or SAF-01 v1.1, Q1 | | |
| D-14 | Urdu draft subject to the native-speaker check (§4.1), Q7 | | |

## 7. Sign-off

Project Lead (safety reviewer, D-011): ______________________________  Date: ______________

Decision recorded in PR: ______________
