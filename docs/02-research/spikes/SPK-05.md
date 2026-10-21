# SPK-05 — Battery and inference budget

Task: T-M2-005 · Owners: AG-05 + AG-06 · Reviewer: Tech Lead · Acceptance: meets or revises NFR-02 / NFR-04 · Status: **battery protocol ready; inference benchmark waits for a candidate model**

## Questions
1. Does sensing at a 15-minute schedule stay within **≤ 2% battery per day** (NFR-04)?
2. Does inference take **≤ 100 ms per window** on the reference device (NFR-02)?

## Battery method
1. On the reference mid-range phone (`docs/08-testing/device-matrix.md`), with the spike app collecting (SPK-01, SPK-03 enabled), run 3 days of typical use.
2. Each day, read the app's share in Settings → Battery usage, and record the `battery_pct` trend from `heartbeat.csv`.
3. Compare with one day **without** the spike app installed (control).

| Day | App's battery share (Settings) | Notes |
|---|---|---|
| 1 | | |
| 2 | | |
| 3 | | |

## Inference method (after T-M2-006 produces a candidate)
Export the candidate (logistic regression or LightGBM, RES-03) to the chosen runtime (LiteRT or ONNX Runtime); run it 1,000 times on recorded feature windows on the reference device; report the 95th percentile (AC-NFR-02).

| Model | Runtime | Size | p50 ms | p95 ms | Meets NFR-02? |
|---|---|---|---|---|---|
| | | | | | |

## Verdict
| Budget | Meets / Revise | Reason |
|---|---|---|
| NFR-04 battery | | |
| NFR-02 inference | | |
