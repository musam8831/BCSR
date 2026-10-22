# Model card — Cold-start rules model `coldstart-0.1` (B1)

Task: T-M5-005 / T-M5-021 · Owner: AG-06 · Reviewer: AG-07 + Tech Lead · Code: `app/android/core/inference` (`ColdStartRules`) · Requirements: FR-10, FR-11, PR-07, AGENTS.md §3

## What it is
A hand-written rules model, not trained on any data. It turns FS-001 features of one window into probabilities over the **passively detectable** context states CS-01, CS-02, CS-03, CS-04, CS-06, CS-08 (taxonomy v1). It never outputs CS-05, CS-07, CS-09 or CS-10. It is used until a personal model exists (FR-11) and is the baseline **B1** in EP-001.

## Inputs → scores (softmax over scores)
| State | Score | Evidence level (GOV-02 §1) |
|---|---|---|
| CS-01 Calm | constant 1.0 (baseline) | — |
| CS-02 Worry / stress | 0.5 · max(0, z unlock_count) + 0.5 · max(0, z checking_sessions) | Moderate (stress), none for anxiety |
| CS-03 Restlessness | max(0, longest_session_min − 45) / 30 | Weak |
| CS-04 Low mood | 0.5 · max(0, z mobility_still_ratio) | Moderate |
| CS-06 Sleeplessness | night_screen_min / 20 | Strongest |
| CS-08 Fatigue | max(0, longest_productivity_min − 90) / 45 | Weak |
Missing features score 0. Before the 7-day baseline exists, z-scores are null and contribute 0. A gap-flagged window halves every non-calm score (TH-16).

## Intended use
Deciding whether to **ask** the user how they are (FR-14), with a threshold (0.6, to be tuned against EP-001 S2). Never shown to the user as a statement about their feelings, never a diagnosis.

## Training data
None (rules). **PR-07 holds trivially**: no public dataset (GLOBEM, ExtraSensory, StudentLife) was used.

## Evaluation
Not yet evaluated on real data. Unit tests (`ColdStartRulesTest`) check: probabilities over exactly the six passive states, summing to 1; calm for a baseline window; sleepless for heavy night use; worry for high unlock and checking z-scores; null z ignored; gap damping. Real evaluation comes from D1 (T-M5-022) and D2 (T-M5-023) against EP-001 S1–S7, including subgroups (R-13).

## Limitations and risks
- Thresholds (45 min, 90 min, 20 min, weights 0.5) are judgement calls from FS-001 hypotheses, not fitted values.
- The features `longest_session_min`, `longest_productivity_min`, `mobility_still_ratio` and `night_screen_min` are not computed yet (only FS-001's first slice is ported; T-M5-004). Until they are, those states score 0.
- Weak evidence for CS-03 and CS-08 (RES-02): expect false prompts; the check-in asks rather than asserts.

## Data cards
D1 (internal dogfooding) and D2 (pilot) data cards are written when that data exists (T-M5-022, T-M5-023); RES-04 §1–§2 defines their content and consent.
