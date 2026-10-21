# Data Flow — ARC-03 v1.0-rc

## Inference & recommendation loop
1. **Collect** (every 15 min, WorkManager): pull UsageEvents since the last cursor and map package → category; screen/unlock events; activity transitions. Store as `usage_event` / `device_event` rows (encrypted).
2. **Featurise:** compute features for the last 15 min / 1 h / day windows; update rolling personal baselines; write `feature_window`.
3. **Estimate:** the StateEstimator outputs P over CS-01..CS-04, CS-06 and CS-08 (taxonomy v1; CS-05, CS-07, CS-09 come only from check-ins) using a cold-start rules model until the personal-data threshold is met, then the population model plus personal calibration.
4. **Decide (InterventionPolicy):** if max P ≥ threshold, and the cooldown has elapsed, and it is not quiet hours, the Salah window or a driving state → schedule a **check-in question** (never a statement).
5. **Check-in:** the user confirms or corrects the state (label stored), or dismisses (no label).
6. **Safety gate:** if the user selects a crisis option → Safety screen first (SR-01).
7. **Recommend:** the bandit selects from approved items allowed for the confirmed state, with a "why" explanation.
8. **Feedback:** helpful / not now / not relevant → bandit reward; dwell time is logged.
9. **Learn:** nightly on-device personalisation job (charging + idle).

## Retention
Raw events 30 days → features and estimates 90 days → labels until the user deletes them (ARC-04 §1). All configurable down; delete-all wipes everything (FR-25).

## Research path (opt-in only)
Feature windows + labels → pseudonymise (random participant ID, time-shifted) → TLS upload. No package names, only categories.
