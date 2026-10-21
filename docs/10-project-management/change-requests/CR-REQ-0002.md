# CR-REQ-0002 — Baseline statistic and checking-session definition

| Field | Value |
|---|---|
| raised_by | AG-06 (pilot P4) |
| task_id | T-M3-006 |
| routed | AG-00 → AG-02 (with AG-07) → Project Lead |
| status | Open |

**Gap 1.** AC-FR-09 says the baseline has a "rolling mean and spread"; FS-001 defines z-scores with the **median** and **MAD** over 14 days. The two disagree. P4 follows FS-001 (unscaled MAD).

**Proposed AC text.** "…a rolling median and median absolute deviation exist for each feature (FS-001)…". Question for AG-07: should MAD be scaled by 1.4826 so z-scores are comparable to standard deviations?

**Gap 2.** FS-001 defines `checking_sessions` as "sessions < 30 s" without saying what a session is. P4 uses SCREEN_ON → next SCREEN_OFF, counted in the window where it starts; a SCREEN_ON with no SCREEN_OFF before the next SCREEN_ON is not counted.

**Proposed FS-001 text.** Adopt P4's definition as written in `ml/features/fs001.py`.

**Impact.** Feature values and AC-FR-09 tests; no scope change.

**Alternatives.** (a) Change FS-001 to mean and standard deviation to match AC-FR-09 (less robust to outlier days). (b) Keep median/MAD but scale MAD by 1.4826. (c) For checking sessions, count unlock-to-lock instead of screen-on-to-off.
