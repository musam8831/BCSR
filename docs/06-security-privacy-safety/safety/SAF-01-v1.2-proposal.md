# SAF-01 v1.2 proposal: changes from the UX-04 decisions

**Status: draft, awaiting the Project Lead's signature (SAF-01 §5: any change to the pathway's flow or copy needs it).** Until it is signed, SAF-01 v1.1 stays the signed baseline. The app already behaves as described below (PR "UX-04 decisions"). Signing this proposal brings the signed document in line with the app; declining it means reverting those app changes.

Source: the Project Lead's decisions on the AG-10 UX-04 review, 2026-10-27 (`AG-10-review-UX-04.md`, "Project Lead decisions").

## Proposed changes

| SAF-01 section | v1.1 text | Proposed v1.2 text | Decision |
|---|---|---|---|
| §2 E2 | "I need help now" on the home screen and in the menu, one tap | "I need help now" at the top of every screen except the safety screen itself, onboarding included, one tap | R-2 |
| §2 E1 | worded as "I'm not safe / I might hurt myself" | unchanged; the app's check-in label now uses exactly this wording (before, it was "I'm not safe") | R-7 |
| §3 flow | `F --> G[Gentle follow-up check-in next day, if the user allows]` | Removed for the MVP. There is no follow-up check-in; it may return in a later version with its own copy and signature | R-8 |
| §3 bullet "No recommendation or notification…" | "…for 24 h after the pathway opens, except the follow-up the user allowed." | "…for 24 h after the pathway opens. A check-in during those 24 h shows the Body line and the helpline buttons instead of a recommendation." | R-4, R-8 |
| §1.5 Minimal logging | "Only a local counter of how often the screen was opened." | add: "The counter and the 24 h quiet time are left out of the user's data export, so a shared export does not reveal safety-screen use." | R-5 |
| §4 copy table | unchanged (no hash change) | unchanged. The entry labels `need_help` and `state_not_safe` are not in §4; adding them, with the draft Urdu, is a separate decision | — |

## Sign-off
| Version | Date | Approver | Decision |
|---|---|---|---|
| 1.2 | | Muhammad Usman, Project Lead (D-011) | |
