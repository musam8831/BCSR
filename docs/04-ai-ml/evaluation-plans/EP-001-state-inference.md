# EP-001 State Inference Evaluation Plan v0.1

- **Labels:** EMA state picks within ±30 min of a window.
- **Baselines:** (B0) majority class per user; (B1) cold-start rules; (B2) time-of-day-only model.
- **Candidates:** logistic regression, LightGBM, 1D-CNN/GRU on event sequences, each ± personal calibration.
- **Splits:** leave-users-out (population) and forward-chaining in time (personal).
- **Metrics:** macro-F1, per-state recall, AUROC per state (one-vs-rest), Brier score / expected calibration error, **check-in precision** (share of triggered check-ins the user confirms).
- **Subgroups:** gender, age band, device tier, OEM.
- **Ship criterion (G-M6):** beats B1 and B2 on macro-F1 and check-in precision with 95% bootstrap CIs not overlapping; ECE ≤ 0.1; meets NFR-02.
- **Recommendation (bandit):** offline replay evaluation of logged pilot feedback; online metric = helpful rate.
