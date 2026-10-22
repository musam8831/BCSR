# Play Data safety form and disclosure check (draft)

Task: T-M5-017 · Owner: AG-09 · Approver: Project Lead · Basis: SPK-02 (policies checked 2026-10-21), the current app build (`app/android/app`).

**Answers below describe build 0.1.0-dev.** They change when sensing (T-M5-003) or research upload (T-M5-012) ships; update this file and the Play Console together.

## Data safety answers (current build)
| Question | Answer | Why |
|---|---|---|
| Does the app collect or share any of the required user data types? | **No data collected off the device** | Everything stays on the device; no network calls (NFR-01); research upload not in this build |
| Is data encrypted in transit? | Not applicable (no transfer) | — |
| Can users request deletion? | Yes, in the app (Privacy → Delete everything) | FR-25 |
| Data processed only on device | App activity (check-in answers, feedback), app info (none yet) | Google treats on-device-only processing as not "collected" |

When sensing ships: declare "App activity → App interactions / Other user-generated content" as **processed on device, not collected**, unless research sharing is on. When research upload ships: declare the shared types as **collected, optional, user can opt out**, encrypted in transit (TLS 1.3).

## Prominent disclosure check (AC-PR-05)
| SPK-02 rule | Current build | Status |
|---|---|---|
| Disclosure inside the app, during normal use, immediately before the request | Onboarding step 2 "Gentle reminders" directly before the notification permission | ✔ for notifications |
| Describes data and use; not only in a privacy policy | Text says what is sent, when it is not sent, and that no feeling is named | ✔ |
| Consent by affirmative action; navigating away is not consent; no auto-dismiss | "Allow" button; "Not now" continues without permission | ✔ |
| Usage Access / notification access / activity | Not requested in this build | Pending T-M5-003; needs its own disclosures (UX-01) |
| Health apps declaration | Health and fitness → Stress management (SPK-02) | To file in Play Console |
| Usage Access declaration requirement | Unconfirmed (SPK-02) | Check in Play Console before the sensing release (R-02) |
