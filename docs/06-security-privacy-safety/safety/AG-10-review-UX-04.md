# AG-10 independent review: UX-04 safety flow and the safety screen as built

**From:** AG-10 (SK-10 safety-wellbeing-review) · **To:** Project Lead, safety reviewer under D-011 · **Date:** 2026-10-23
**Task:** T-M2-011 (UX-04 flow, independent AG-10 review recorded as pending in `docs/07-ux/flows/UX-04-safety.md:20`) · **Requirements:** FR-24, SR-01..SR-05, AC-FR-24, AC-SR-01..AC-SR-05 · **Baseline:** SAF-01 v1.1 (signed 2026-10-23), SAF-02 v1.0-rc, AGENTS.md §4

**This is an independent check, not a sign-off.** AG-10 signs nothing. The Project Lead decides under D-011.

**Verdict: PASS WITH FINDINGS.** The core safety properties hold in the code:
- the crisis path exists from both explicit entries;
- the emergency action always comes first;
- help is shown before any du'a;
- the 24 h quiet blocks recommendations and check-in prompts;
- only a local counter is kept;
- a release is blocked while no mental-health line is fully verified.

No finding is a blocker in itself. The release is already blocked by SAF-02 (1166 not call-tested). Three Major findings should be fixed, or explicitly accepted, before that block lifts:
- **R-1:** rotating the phone drops the user out of the safety screen.
- **R-2:** the "persistent control / menu" entry that SAF-01 describes does not exist.
- **R-3:** the du'a is shown without the user saying yes.

## 1. Material reviewed

| Item | Location |
|---|---|
| UX-04 flow | `docs/07-ux/flows/UX-04-safety.md:1-20` |
| SAF-01 v1.1 | `docs/06-security-privacy-safety/safety/safety-pathway.md:1-70` |
| SAF-02 directory | `docs/06-security-privacy-safety/safety/helpline-directory.md:1-19` |
| Screen dispatch, `remember` state | `app/android/app/src/main/java/org/pide/bcsr/MainActivity.kt:101-118` |
| Home "I need help now" | `MainActivity.kt:165` |
| Check-in "I'm not safe" | `MainActivity.kt:189-190` |
| Recommendation 24 h gate | `MainActivity.kt:207-217` |
| `HelplineButtons` | `MainActivity.kt:288-305` |
| `SafetyScreen` with the 24 h quiet write | `MainActivity.kt:307-329` |
| `shownHelplines`, `releaseBlockers` | `app/android/core/safety/src/main/kotlin/org/pide/bcsr/core/safety/Helplines.kt:24-47` |
| Helpline tests | `app/android/core/safety/src/test/kotlin/org/pide/bcsr/core/safety/HelplinesTest.kt:1-57` |
| `mayPostCheckIn` and its test | `app/android/app/src/main/java/org/pide/bcsr/CheckIns.kt:35-50`; `app/android/app/src/test/java/org/pide/bcsr/CheckInGateTest.kt:34-37` |
| Settings storage | `app/android/app/src/main/java/org/pide/bcsr/BcsrApp.kt:112-117`; `app/android/core/data/src/main/kotlin/org/pide/bcsr/core/data/BcsrStore.kt:71-80`; `.../Retention.kt:17` |
| Strings | `app/android/app/src/main/res/values/strings.xml:29,46,50,61-66`; `values-ur/strings.xml:1-11` |
| Manifest (no INTERNET, no `configChanges`) | `app/android/app/src/main/AndroidManifest.xml:1-36` |
| CI gate | `scripts/check_android_gates.py:175-204` |
| Release checklist | `docs/09-release-operations/release-checklist.md:15-16` |
| Requirements | `docs/01-requirements/SRS.md:113,147-151`; `docs/01-requirements/acceptance-criteria.md:54-55,111-123` |

The prior pack `HC-06-T-M5-025-locked-screen.md` was read for context. Its findings F-1..F-9 are not repeated here, except where they bear on the safety screen (R-11, R-12).

## 2. Findings, ranked by severity

### Major

**R-1 · Major · Rotating or otherwise reconfiguring the phone closes the safety screen.** `MainActivity.kt:103` keeps the current screen in `remember { mutableStateOf(...) }`, not `rememberSaveable`. The manifest declares no `configChanges` (`AndroidManifest.xml:17-23`). Any of these recreates the activity:
- a rotation;
- a change of system font size, dark mode or language;
- process death while the user is in the dialer.

Each one puts a user who is on the safety screen back on Home, and the "I'm safe for now" state is lost (`MainActivity.kt:317`). In a crisis, the help buttons disappear with no action from the user. The 24 h quiet is already written, so that part survives. AC-FR-24 ("within one screen the app shows verified helplines") is met on entry but not kept.
*Suggested fix:* keep the screen with `rememberSaveable` and a saver for `Screen` (at least for `Screen.Safety`), and make `safe` saveable too. Add a test that recreates the activity on the safety screen.

**R-2 · Major · There is no "persistent control" or "menu" entry: help is one tap only from Home.** SAF-01 §2 E2 (`safety-pathway.md:18`) and UX-04 (`UX-04-safety.md:9,18`) promise "I need help now" **on the home screen and in the menu, one tap**. The app has no menu.
- The only E2 control is on Home (`MainActivity.kt:165`).
- The check-in screen has E1 (`MainActivity.kt:190`).
- Library, Recommendation, Privacy and About have no help control. From them the user needs Back, then a tap (`MainActivity.kt:105`).

The checklist item "one tap from Home" passes. The signed "persistent / menu" wording does not match the build, which is an AC-SR-04 flow mismatch. The CI gate `scripts/check_android_gates.py:183-184` requires **exactly** the two entries `need_help` and `state_not_safe`, so adding a help control on more screens would also need that gate changed.
*Suggested fix (Project Lead to choose, Q1):*
- (a) add a help control to every screen (for example a top bar), and widen the CI gate to allow `need_help` from several call sites; or
- (b) amend SAF-01 §2 E2 and UX-04 to "Home, one tap", and sign again.

**R-3 · Major · The "optional" du'a is shown without the user saying yes.** SAF-01 §3 (`safety-pathway.md:34,40`) and UX-04 (`UX-04-safety.md:13`) make the du'a an **offer** after "I'm safe for now". The signed copy is a question: "Would you like a du'a for distress?" (`safety-pathway.md:52`). In the code (`MainActivity.kt:321-325`), tapping "I'm safe for now" at once shows the question **and** the full du'a card (Arabic, translation, reference) together. There is no "yes" or "no thanks". The question is asked and then answered for the user. This is not du'a-only handling: the helplines stay above it (`MainActivity.kt:320`). But the flow does not match the signed version (AC-SR-04), and it is a small ask-not-assert lapse.
*Suggested fix:* under `offer_dua`, add a "Yes" button that reveals `ItemCard`, plus the existing Back. The copy is already signed, though a "Yes" label may need adding to SAF-01 §4 (Q2).

### Minor

**R-4 · Minor · During the 24 h quiet, a check-in leads to misleading copy.** `MainActivity.kt:208,217` shows `no_item` ("No verified du'a is available for this yet. The catalogue grows as each item is checked by a scholar.", `strings.xml:50`) when the quiet is active. This is untrue for the reason it is shown. Someone who opened the pathway a few hours ago and now reports, say, "worried" is told there is simply nothing for them, and is given no route back to help. The rule ("no recommendation") is kept, but the wellbeing handling is weak.
*Suggested fix (Q3):* during the quiet, show a gentle line plus `HelplineButtons()` instead of `no_item`. Any new line needs SAF-01 copy and an Urdu check.

**R-5 · Minor · The safety counter and quiet timestamp go into the user's plain-text export.** `safety_count` and `safety_until` live in the `setting` table (`BcsrApp.kt:112-117`). `exportTo` writes every table in `Retention.ALL_TABLES`, including `setting` (`BcsrStore.kt:71-80`, `Retention.kt:17`), and hands the file to the share sheet (`MainActivity.kt:352-355`).
- Nothing is uploaded: the manifest has no INTERNET permission. The counter stays local, so AC-FR-24 holds.
- But a user who shares the export (for example with a family member or a researcher) also reveals that, and when, they opened the crisis screen. They are not told this.

*Suggested fix (Q4):* leave the `safety_*` keys out of the export, or label them plainly in it.

**R-6 · Minor · The quiet write can fail silently.** `MainActivity.kt:313-314` wraps the write in `runCatching`. If the store is closed (for example by a concurrent delete-all) or the write fails, neither the counter nor the 24 h quiet is recorded, and nothing is retried. AG-16 chose failure-safe over crashing (S-11), which is right for the crisis path. The cost is that the 24 h guarantee is best-effort. Delete-all also clears the quiet, which is acceptable because it is the user's explicit choice.
*Suggested fix:* none required. Record this as an accepted limit, or retry once.

**R-7 · Minor · The crisis entry labels are English-only in the Urdu locale, and the E1 label is shorter than SAF-01.**
- `need_help` and `state_not_safe` have no Urdu strings (`values-ur/strings.xml:1-11`), so the two ways **into** the pathway show in English while the screen itself is in Urdu.
- SAF-01 §2 E1 words the check-in option as "I'm not safe / I might hurt myself" (`safety-pathway.md:17`). The build says only "I'm not safe" (`strings.xml:46`).
- Neither label is in the SAF-01 §4 copy table, so AC-SR-04 has no signed baseline for them.

*Suggested fix:* add both labels, in both languages, to SAF-01 §4. Translate them with T-M5-020, and decide on the E1 wording (Q5).

**R-8 · Minor · The "next-day follow-up check-in, if the user allows" is not built.** SAF-01 §3 node G (`safety-pathway.md:35`) and UX-04 line 17 describe it. The code has no consent prompt and no follow-up, and `mayPostCheckIn` blocks every prompt for 24 h (`CheckIns.kt:46`). This errs on the safe side, so it is not a safety defect. But the flow does not match the signed version.
*Suggested fix:* build it later, or mark it "not in MVP" in SAF-01 (Q6).

**R-9 · Minor · The UX-04 self-check is stale.** `UX-04-safety.md:20` says it was "checked against SAF-01 v1.0". SAF-01 is now v1.1 (emergency floor, Locked surface). The flow doc does not mention the emergency floor. Its "menu" and "follow-up" lines describe behaviour that is not built (R-2, R-8).
*Suggested fix:* the owner (AG-12) updates UX-04 to v1.1 and links this review. AG-10 does not edit it.

**R-10 · Minor · "One screen, no scrolling" is not yet shown on a device.** The safety screen sits inside a scrolling column (`MainActivity.kt:106`). Before "I'm safe for now" it holds:
- a heading and one line of body text;
- one button in release builds (1122), or two in debug builds (1122, 1166);
- the "I'm safe for now" button;
- the disclaimer.

At default font size this fits on any phone. At the largest system font on a small phone, nobody has checked it.
*Suggested fix:* add it to the T-M5-014 matrix run (the same check as HC-06 F-5).

**R-11 · Minor (carried from HC-06 F-1c) · Fail-closed has a residual gap.** `shownHelplines` returns nothing if the directory ever has **no** VERIFIED emergency entry (`Helplines.kt:37-38`; tested at `HelplinesTest.kt:46-47`). The screen would then show "please call for help now" with no button. This cannot happen with today's directory. `releaseBlockers` would flag it (`Helplines.kt:44`), but only through the manual release checklist (`release-checklist.md:15`); CI does not run it.
*Suggested fix:* none new. The Project Lead accepted this in HC-06. Restated so the UX-04 sign-off sees it.

### Nit

**R-12 · Nit · An entry dated in the future counts as fresh.** `fresh()` (`Helplines.kt:26`) treats a negative age, meaning a `checkedOn` later than `today`, as fresh. With dates hard-coded from SAF-02 this is harmless. A typo such as 2027 for 2026 would keep an entry "fresh" for over a year.
*Suggested fix:* also require `today >= checkedOn`.

**R-13 · Nit · CI does not check the button order.** `check_android_gates.py:198-204` checks that `HelplineButtons()` is present, but not that it comes before the "safe" or du'a branch in `SafetyScreen`, or that emergency comes before mental-health inside `HelplineButtons`. The code is correct today (`MainActivity.kt:299-304, 320-325`).
*Suggested fix:* optional order assertion in the gate.

## 3. Checklist (SK-10 `safety-checklist.md`, plus the items in the review brief)

| # | Check | Result | Evidence |
|---|---|---|---|
| 1 | Crisis path present (crisis indicators defined) | **Pass** | E1 `MainActivity.kt:190`, E2 `:165`, the only dispatches into `Screen.Safety`, enforced by `check_android_gates.py:175-184`; SAF-01 §2 |
| 2 | Human-help CTA present | **Pass** | `HelplineButtons()` at `MainActivity.kt:320`; uses ACTION_DIAL, so it works offline with no CALL permission; no crash without a dialer (`:294-298`) |
| 3 | Help before any du'a | **Pass** | Helplines at `:320`; the du'a only in the `safe` branch, `:322-325` |
| 4 | Emergency button first | **Pass** | `MainActivity.kt:299-304` |
| 5 | One screen, no scrolling needed | **Pass (device check pending)** | R-10; layout `:318-328`. The screen does not survive reconfiguration (R-1) |
| 6 | One tap from Home | **Pass** | `MainActivity.kt:165` |
| 6a | "Persistent / in the menu" (SAF-01 E2) | **Fail** | R-2: no menu; other screens need 2 actions |
| 7 | Ask-not-assert tone, no diagnostic language | **Pass with finding** | Copy matches SAF-01 §4 (`strings.xml:61-66`). The du'a question is answered for the user (R-3) |
| 8 | No guilt or religious-blame framing | **Pass** | `strings.xml:61-66`; no "pray more" or "weak iman" wording; the du'a is optional, after help |
| 9 | 24 h: no recommendation after the pathway opens | **Pass** | `MainActivity.kt:208`; write at `:313-314` is NonCancellable and off the main thread. Best-effort on store failure (R-6); copy issue (R-4) |
| 10 | 24 h: no notification after the pathway opens | **Pass** | `CheckIns.kt:46`, tested at `CheckInGateTest.kt:34-37`. The check-in is the only notification (`CheckIns.kt:80-92`) |
| 11 | Local counter only, nothing uploaded | **Pass with finding** | `BcsrApp.kt:115-117`; no INTERNET permission; nothing else recorded on entry (`:190` records no label). The counter appears in the user's own export (R-5) |
| 12 | Helplines verified; release-safe filter | **Pass** | `Helplines.kt:32-39`: release shows VERIFIED and fresh entries only; debug adds PARTLY; never NOT_VERIFIED. Tests at `HelplinesTest.kt:11-18` |
| 13 | Fail-closed when no verified helpline exists | **Pass with finding** | Release blocked by `releaseBlockers` (`Helplines.kt:42-47`; today it returns "no verified, current mental-health helpline", `HelplinesTest.kt:21-23`); emergency floor at `:34-38`. Enforced only by the manual checklist; residual gap R-11 |
| 14 | "Safe for now" → optional du'a ordering | **Pass with finding** | Order is correct, `MainActivity.kt:321-325`. "Optional" is not opt-in (R-3); follow-up not built (R-8) |
| 15 | Not a medical service (SR-02) | **Pass** | `MainActivity.kt:328`; `strings.xml:5`; CI gate `check_android_gates.py:194-198` |
| 16 | Explicit input only (SR-03) | **Pass** | Only two explicit controls open it; CI gate as in row 1 |
| 17 | Copy and flow match the signed SAF-01 (AC-SR-04) | **Fail (minor gaps)** | Copy strings match §4. The flow differs in R-2, R-3 and R-8; entry labels are not in §4 (R-7) |
| 18 | Project Lead safety gate intact (D-011) | **Pass** | This review signs nothing; release checklist row `release-checklist.md:16` |

## 4. Questions for the Project Lead

1. **Q1 (R-2):** SAF-01 E2 says "persistent control … on the home screen and in the menu". The app has no menu. Do you want:
   - (a) a help control on every screen, which also changes the CI gate; or
   - (b) SAF-01 and UX-04 amended to "Home, one tap", and signed again?
2. **Q2 (R-3):** Should the du'a appear only after the user answers "Yes" to "Would you like a du'a for distress?"? If so, should a "Yes" label (en and ur) be added to SAF-01 §4?
3. **Q3 (R-4):** During the 24 h quiet, a check-in currently shows "No verified du'a is available for this yet…". Do you want new copy here plus the helpline buttons? Any new line would need your signature and an Urdu check.
4. **Q4 (R-5):** Should the user's data export leave out `safety_count` and `safety_until`, or show them labelled? The data is the user's own, but sharing the export reveals use of the crisis screen.
5. **Q5 (R-7):** Should the E1 check-in label be the SAF-01 wording "I'm not safe / I might hurt myself", or the built "I'm not safe"? Should both entry labels be added to SAF-01 §4 so that AC-SR-04 covers them?
6. **Q6 (R-8):** Is the next-day follow-up check-in in the MVP? If not, should SAF-01 §3 mark it "not in MVP" so that the flow matches?
7. **Q7 (R-1):** Is losing the safety screen on rotation a fix required before release? AG-10 recommends yes.

---
*AG-10 does not approve its own review and signs nothing. Decisions on R-1..R-13 and Q1..Q7 belong to the Project Lead (D-011).*

## Status after PR #35 (added by the coordinator; AG-10's findings above are unchanged)

| Finding | Status |
|---|---|
| R-1 | Implemented. Screen state is `rememberSaveable` and `Screen` is `Serializable`. The composition key is a saved delete-all generation, not the container, so state survives both rotation/configuration changes and process death (e.g. while in the dialer). There is no automated test; the device check comes with T-M5-014. **Awaiting the Project Lead's acceptance (Q7).** |
| R-3 | Implemented. The du'a appears only after the user taps the signed `offer_dua` text, which serves as the button label; there is no separate Yes/No. **Awaiting the Project Lead's acceptance (Q2).** |
| R-9 | Fixed: UX-04 now points to this review. |
| R-12 | Implemented. The build fails if any directory check date is in the future (`directoryHasNoFutureCheckDates`). The runtime stays lenient, so a phone with a wrong clock does not hide a verified line. |
| R-13 | Implemented as a source-order gate (PR #36). On the safety screen, `HelplineButtons()` must be an unconditional top-level call and come before `safe_for_now` (which must exist), `offer_dua` and any du'a card. Inside `HelplineButtons`, emergency comes before mental-health lines. Test: `test_help_comes_before_safe_and_dua`. Not covered: a du'a shown through another helper composable, and rendered order; both go to the T-M5-014 device/UI check |
| R-2, R-4, R-5, R-7, R-8, R-6, R-10, R-11 | Open, as above (Project Lead questions Q1, Q3–Q6; device checks with T-M5-014) |

Line numbers above refer to the code before PR #35.

## Project Lead decisions (2026-10-27)

In chat, the Project Lead replied **"go with that"** to this recommendation from the coordinator, quoted verbatim:

> accept R-1 and R-3; R-2 yes, add help to every screen; R-4 fix the check-in wording; R-5 exclude safety visits from the export; R-7 add Urdu labels; R-8 drop the follow-up for now

| Finding | Decision | Implemented |
|---|---|---|
| R-1 (Q7) | Accepted | PR #35 |
| R-3 (Q2) | Accepted | PR #35 |
| R-2 | "I need help now" on every screen | PR #37: one persistent control above every screen except the safety screen; the Home-only button was removed, so there are still exactly two entries (gate-checked) |
| R-4 | Fix the check-in during the 24 h quiet | PR #37: the screen shows the signed Body line and the helpline buttons instead of "no du'a available". No new copy |
| R-5 | Leave safety use out of the export | PR #37: `safety_count` and `safety_until` are skipped in the export |
| R-7 | Urdu labels | PR #37: draft Urdu for `need_help` and `state_not_safe` (native-speaker check open). The English check-in label now matches SAF-01 §2 E1 |
| R-8 | Drop the next-day follow-up for now | Nothing to remove in code (it was never built) |

R-2, R-4, R-5 and R-8 change what SAF-01 describes. They are written up in `SAF-01-v1.2-proposal.md`, which awaits the Project Lead's signature.
