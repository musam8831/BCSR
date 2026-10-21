# SPK-02 — Google Play policy review

Task: T-M2-002 · Owner: AG-05 · Reviewer: AG-09 · Acceptance: policy text quoted and dated · Checked: **2026-10-21** (pages show no update date; footer "©2026 Google"). Quotes are short extracts; read the full pages before submission. Policies change: re-check before every Play submission.

## 1. Sources
| ID | Page (Play Console Help) | URL |
|---|---|---|
| P1 | User Data — "Prominent Disclosure & Consent Requirement" section | support.google.com/googleplay/android-developer/answer/10144311 |
| P2 | Permissions and APIs that Access Sensitive Information | support.google.com/googleplay/android-developer/answer/9888170 |
| P3 | Provide information for the Health apps declaration form | support.google.com/googleplay/android-developer/answer/14738291 |

## 2. Findings per permission or feature
| BCSR use | Permission / API | Policy finding | Quote (dated 2026-10-21) | BCSR requirement / action |
|---|---|---|---|---|
| App-usage and screen/unlock events (FR-03, FR-04) | `PACKAGE_USAGE_STATS` (Usage Access, granted in Settings) | Background collection of usage data is outside what users would reasonably expect, so P1's prominent disclosure applies. **P2's fetched text did not mention Usage Access** (page was truncated): whether a declaration form is needed is **unconfirmed** | P1: disclosure needed when collection "may not be within the reasonable expectation of the user" | Disclosure screen before the Settings hand-off (FR-01, PR-05, AC-PR-05). Re-check P2 and the Play Console declaration list at submission |
| Notification counts (FR-05) | `NotificationListenerService` | Same P1 disclosure duty. **Not found in the fetched P2 text**; secondary sources say it is treated as a restricted, core-feature-only permission (unconfirmed) | — | FR-05 is a Should: keep it optional and off by default until P2 is confirmed; count only, never read content (PR-01) |
| Activity state (FR-06) | `ACTIVITY_RECOGNITION` (runtime) | Not found in the fetched P2 text | — | Runtime request after an in-app explanation; optional |
| Not used | `AccessibilityService` | Non-accessibility tools need a declaration, disclosure and consent | P2: "complete an accessibility declaration in Play Console" | Already excluded by ADR-0002; confirms that decision |
| Not used | `QUERY_ALL_PACKAGES` | Allowed only when finite, targeted queries cannot meet the need | P2: "Don't request QUERY_ALL_PACKAGES if your need can be met with finite, targeted queries" | Category lookup uses `getApplicationInfo` for packages seen in usage events; **do not request QUERY_ALL_PACKAGES** (confirm in SPK-01 that lookups work on Android 11+ package visibility) |
| All sensitive data | Prominent disclosure and consent | Disclosure in the app, during normal use, immediately before the request; describes the data and its use; not only in a privacy policy; consent by an affirmative action; navigating away is not consent; no auto-dismissing messages | P1: "immediately preceded by an in-app disclosure"; "Must require affirmative user action"; "Must not use auto-dismissing or expiring messages" | AC-FR-01 and AC-PR-05 already require this; onboarding flow (UX-01) implements it |
| Health apps declaration | Play Console form | Every app with a Play track must complete it. **Stress management** is listed under Health and fitness; "mental and behavioral health" is under Medical | P3: categories include stress management under Health and fitness | Declare **Health and fitness → Stress management** (and Sleep management only if a sleep feature beyond adhkar is added). Do **not** declare Medical: BCSR is not a medical service (SR-02). For the pilot, consider "Health research" (P3) if the pilot build is distributed through Play testing tracks |

## 3. Verdict for G2
| Signal / permission | Policy verdict | Condition |
|---|---|---|
| Usage Access (FR-03, FR-04) | **Conditional go** | Prominent disclosure (done in design); confirm no declaration form is needed, or file it |
| Notification counts (FR-05) | **Conditional go (optional feature)** | Confirm P2 treatment; off by default |
| Activity recognition (FR-06) | **Go (optional)** | Runtime explanation |
| AccessibilityService, custom IME, QUERY_ALL_PACKAGES | **Not used** | — |

Risk R-02 (15) stays open until the Usage Access treatment in P2 is confirmed and, if needed, a declaration is accepted.
