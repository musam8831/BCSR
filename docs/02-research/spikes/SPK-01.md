# SPK-01 — Usage events and screen/unlock events

Task: T-M2-001 · Owner: AG-05 · Reviewer: AG-03 · Acceptance: captured on ≥ 5 devices; verdict · Status: **protocol and app ready; not yet run**

## Question
Can BCSR read app-usage events (FR-03), screen on/off and unlock events (FR-04) through UsageStatsManager on Android 9+ across common phones in Pakistan, with complete and correctly timed data?

## Method
1. Install the spike app (`app/android/spikes/README.md`) on ≥ 5 phones covering ≥ 4 manufacturers and Android 9–15 (device matrix: `docs/08-testing/device-matrix.md`).
2. Grant usage access (button 1). Use the phone normally for **24 h**.
3. Keep a short manual log of 5 known actions with times: open a named app, lock, unlock, screen off/on.
4. Tap 5, then 6; pull the files.
5. Check: each manual action appears in `usage_events.csv` within ± 5 s; counts of each event type are non-zero; no hours with zero events while the phone was in use.

## Results (one row per device)
| Device (maker, model) | Android / API | Usage access granted | app_resumed / app_paused | screen_on / screen_off | unlock | Manual actions matched (of 5) | Notes |
|---|---|---|---|---|---|---|---|
| | | | | | | | |

## Verdict (fill after ≥ 5 devices)
| Signal | Go / Conditional / No-go | Reason |
|---|---|---|
| App usage events (FR-03) | | |
| Screen on/off (FR-04) | | |
| Unlock (FR-04) | | |

## Known risks
- Usage access is a special permission granted in Settings, not a runtime dialog; onboarding must guide the user (FR-01, SPK-02).
- Some manufacturers' settings screens differ; record the path on each device.
