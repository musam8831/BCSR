# AG-12 copy review: Locked screen and `export_failed` (T-M5-025)

**Reviewer:** AG-12 (SK-12 ux-design) · **Date:** 2026-10-23 · **Task:** T-M5-025 (closes T-M5-019 S-06) · **Requirements:** SR-01, SR-04, FR-24, PR-02, NFR-05
**Scope:** English only, `app/android/app/src/main/res/values/strings.xml` and the `Locked` composable in `app/android/app/src/main/java/org/pide/bcsr/MainActivity.kt`. The Urdu draft waits for its native-speaker check (SAF-01 §4, HC-06 §4.1).
**Baseline:** SAF-01 v1.1 §4 (signed by the Project Lead 2026-10-23), HC-06-T-M5-025-locked-screen.md, UX-04, UX-05, `AGENTS.md` §3 (ask, not assert).

> The six Locked strings are signed in SAF-01 v1.1 §4, and `scripts/check_android_gates.py` (lines 43-45) checks them. **Changing any of them needs a new SAF-01 signature by the Project Lead (SR-04, AC-SR-04).** `export_failed` is not in SAF-01, so it can change without a signature.

## 1. Verdict per string

What I checked for each string: clarity, reading level (short sentences, everyday words), tone (no blame, no feeling named, ask not assert), and consistency with the other strings and UX-04/UX-05.

| Key | English (verbatim) | Verdict | Notes |
|---|---|---|---|
| `locked_title` | "Your saved data can't be opened" | **OK** | Six plain words. Describes the data, not the user. Works for both the temporary and the permanent case. The contraction matches the other headings ("You don't have to go through this alone."). It has no full stop, like the other titles. |
| `locked_body` | "This phone can no longer unlock what BCSR saved, so it cannot be read by anyone, including you. You can start fresh: BCSR will delete the unreadable data and set up again." | **Suggest change (optional, not blocking)** | Proposed: "This phone can no longer unlock the data BCSR saved. No one can read it now, including you. You can start fresh: BCSR will delete it and set up again." Reason: the first sentence is 20 words long, and its "it" could refer to "this phone" or to "what BCSR saved". Three short sentences (at most 10 words each) are easier to read under stress. The meaning, the blame-free framing ("This phone…") and the wording the Project Lead approved ("including you", Q6) stay the same. Not worth re-signing on its own; fold it into the T-M5-020 microcopy pass, which re-signs SAF-01 §4 anyway. |
| `locked_retry_body` | "BCSR could not open your saved data just now. Nothing has been deleted. Please try again in a moment." | **OK** | Three short sentences. "Nothing has been deleted" is the reassurance a user needs first. "Please try again" matches `export_failed` and the `try_again` button. |
| `try_again` | "Try again" | **OK** | Verb-first button label, the same wording as the body text above it. |
| `start_fresh` | "Start fresh" | **OK** | Matches the body ("You can start fresh: …") and is reused as the dialog's confirm button, so the user sees the same action name twice. The destructive part is spelled out in `start_fresh_confirm`. What is deleted can never be read, so a gentler label than "Delete everything" (Privacy Center) is right here. |
| `start_fresh_confirm` | "This deletes the unreadable data and starts BCSR again from the beginning. It cannot be undone." | **OK** | Follows the same pattern as `delete_confirm1` ("This deletes … It cannot be undone."). "From the beginning" tells the user that onboarding comes back. One confirmation step (Privacy Center has two) is right, because nothing readable is lost. |
| `export_failed` | "Export did not finish. Please try again." | **OK** | Neutral, no blame, says what to do next. Wording matches `locked_retry_body`. Note: it is shown as a short `Toast` (MainActivity.kt:359). TalkBack reads toasts, so this is acceptable for a two-sentence message. |

Strings the Locked screen reuses, which are already signed in SAF-01 §4 and not re-reviewed here: `safety_body`, `call_emergency`, `talk_to`, `not_medical`, `cancel`.

**Tone check (all seven strings): pass.** None names a feeling, none blames the user (the cause is "this phone"), none is religious, and none diagnoses. The only safety sentence is the signed conditional `safety_body`.

## 2. Accessibility (NFR-05; SK-12 rtl-a11y checklist)

**TalkBack order.** It follows the `Column` order in `Locked` (MainActivity.kt:267-285):
1. `locked_title`, announced as a heading
2. `locked_body` or `locked_retry_body`
3. `safety_body`
4. the emergency button, then the helpline buttons (`HelplineButtons()`)
5. "Start fresh" or "Try again"
6. `not_medical`

Help comes before the destructive or retry action, as in UX-04. That order is right.

**Heading semantics.** `Title()` (MainActivity.kt:120-121) adds `Modifier.semantics { heading() }`, so TalkBack users can jump straight to the title with heading navigation. The screen has only one heading, which is right for a single-purpose screen. The `AlertDialog` has no title, only `text`, the same as the Privacy Center dialogs. TalkBack moves focus into the dialog and reads the text, then "Start fresh" and "Cancel".

**Touch targets.** Every action is a Material 3 `Button`, `OutlinedButton` or `TextButton`. Material 3 enforces a minimum 48 dp touch target (`minimumInteractiveComponentSize`). The screen buttons are also full width, with 12 dp spacing, so no target is too small or too close to another. Labels are visible text, so they need no extra `contentDescription`.

**Font scaling.** The column scrolls, so text never clips at the largest font size. Whether the helplines stay above the fold is HC-06 F-5, checked on the T-M5-014 device run.

**Contrast.** The screen uses the default `MaterialTheme` colours, the same as every other screen. This review does not add a separate contrast check.

**Observations outside the signed copy (for AG-11 and T-M5-020; not blocking this review):**
- **No feedback after a failed "Try again".** `tryAgain` sets `c = app.container`. If the store still fails, that value stays `null`, so nothing recomposes and the screen does not change. `lockedPermanently` is a plain `var` (BcsrApp.kt:22), not Compose state. So if a retry fails *permanently*, "Start fresh" does not appear until the app restarts. Sighted users and TalkBack users get no sign that the tap did anything. Suggested fix: make the locked state observable, so the body and button update. A short "Still can't open it" announcement (live region) would need new copy and a SAF-01 signature.
- **The number in "Call emergency (1122)".** TalkBack may read the number as "one thousand one hundred twenty-two". To check on the T-M5-014 device run. If it does, a digit-by-digit `contentDescription` would help. The copy on screen would not change.

## 3. Overall verdict

**Approve the English copy as signed in SAF-01 v1.1. No change is required before release.** One optional change, to `locked_body` (shorter sentences, a clearer "it"), is suggested for the T-M5-020 microcopy pass. Making it would need a new SAF-01 §4 signature and a matching update to the Urdu draft. `export_failed` is approved and needs no signature. The two observations in §2 go to AG-11. They are about behaviour, not copy.

AG-12 does not approve its own work. This review goes to AG-16 under HC-03.
