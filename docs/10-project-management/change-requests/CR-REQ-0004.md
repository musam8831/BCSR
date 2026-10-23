# CR-REQ-0004 — AC-SR-01: the emergency number is always shown

| Field | Value |
|---|---|
| raised_by | AG-10 (HC-06 pack for T-M5-025, finding F-1), on an AG-16 finding (PR #32) |
| task_id | T-M5-025 |
| routed | AG-00 → AG-02 → Project Lead |
| status | Open: Project Lead decision D-11 in `docs/06-security-privacy-safety/safety/HC-06-T-M5-025-locked-screen.md` |

**Gap.** Two documents conflict:
- AC-SR-01 says "every helpline shown has a verification date within the last 90 days".
- SAF-01 §3 (signed v1.0) says the safety screen "still shows the emergency action" when no verified helpline exists.

Before PR #32, once an entry passed 90 days, an installed release build showed "please call for help now" with no number at all. PR #32 fixes this by showing the most recently verified emergency number (1122) past its review date. Mental-health lines still drop out. That code meets SAF-01 §3, but it is an exception to AC-SR-01 as written.

**Impact.**
- **Safety:** removes a crisis-path dead end in installed builds.
- **Release:** still blocked by `releaseBlockers()`, which today is enforced through the release checklist, not CI.
- Scope: small; schedule: none; risk: low.

**Proposed AC text.** "…every helpline shown has a verification date within the last 90 days in the helpline directory, except the emergency number. The emergency number is always shown, using the most recently verified entry when none is within 90 days. An entry past 90 days blocks the release (AC-SR-05)."

**Alternatives.**
- (a) Keep AC-SR-01 strict and show no number when the entry is stale. This contradicts SAF-01 §3 and leaves a dead end.
- (b) Show a plain-text "dial your local emergency number" line instead of the stale number. This needs new signed copy (SAF-01 §5).
- (c) Add a CI check that fails a build when `releaseBlockers()` is non-empty for the build date, in addition to the proposed text.
