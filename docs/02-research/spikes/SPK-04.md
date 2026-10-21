# SPK-04 — Background scheduling on the top-5 manufacturers

Task: T-M2-004 · Owner: AG-05 · Reviewer: AG-03 · Acceptance: gap rates per OEM · Status: **protocol and app ready; not yet run** · Risk: R-16, assumption A-09

## Question
Does a 15-minute WorkManager job keep running on popular phones in Pakistan, or does battery optimisation stop it?

## Method
1. On one phone from each of the **five most-used manufacturers in Pakistan** (confirm the list from a current market-share source before buying or borrowing; record the source and date here), install the spike app and start collection (button 4).
2. Run **72 h**: day 1 normal use; day 2 normal use with the phone left idle overnight; day 3 with battery saver on.
3. Do **not** exempt the app from battery optimisation for the first 48 h; on day 3, also repeat once with the app exempted, to measure the difference.
4. Tap 6 for the summary; pull `heartbeat.csv`.
5. **Gap** = an interval > 30 min (2 × the 15-min schedule), counted by `heartbeatGaps` (unit-tested in the spike app). **Gap rate** = gaps / intervals.

## Results
| Maker | Model | Android | Heartbeats | Gap rate (normal) | Gap rate (idle overnight) | Gap rate (battery saver) | Gap rate (exempted) | Max gap | Standby bucket seen |
|---|---|---|---|---|---|---|---|---|---|
| | | | | | | | | | |

## Decision rule (proposed)
- Gap rate ≤ 10% in normal use → **Go** for that maker.
- 10–30%, or acceptable only when exempted → **Conditional**: onboarding asks the user to exempt the app, with maker-specific steps.
- > 30% even when exempted → **No-go** for passive sensing on that maker; check-in-only mode (FR-02).

Features must tolerate gaps (FS-001): usage events are read retrospectively from UsageStatsManager, so a late job loses timing precision, not events. Confirm this in SPK-01 data.
