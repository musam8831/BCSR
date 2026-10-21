# EP-001 State Inference Evaluation Plan v1.0-rc

Task: T-M2-010 · Owner: AG-07 (SK-07) · Reviewer: Tech Lead · Sets the numeric target for charter OBJ-1. **Numbers in §5 are proposed by AG-07 for Tech Lead approval.**

## 1. What is evaluated
- **State model (FR-10):** probabilities over the passively detectable states CS-01..CS-04, CS-06, CS-08 (taxonomy v1). CS-05, CS-07, CS-09 are EMA-only and not evaluated as model outputs.
- **Check-in policy (FR-14):** whether triggered check-ins are confirmed by the user.
- **Recommender (FR-16, FR-29):** whether suggested du'a are rated helpful/relevant.

## 2. Data
- D0 (GLOBEM, research only, RES-04): method development and cross-dataset sanity checks; **never used to train the shipped model** (PR-07).
- D1 dogfooding and D2 pilot (≥ 30 participants, 4 weeks): the evaluation that counts for shipping.
- **Labels:** EMA state picks within ± 30 min of a window. A window with no EMA in that range is unlabelled.

## 3. Models
- **Baselines:** (B0) majority class per user; (B1) cold-start rules (FR-11); (B2) time-of-day-only model.
- **Candidates:** logistic regression, LightGBM, small 1D-CNN/GRU on event sequences, each ± personal calibration (FR-12).

## 4. Splits and metrics
- **Splits:** leave-users-out (population model) and forward-chaining in time per user (personal model). No random window splits (RES-04 §3). Report cross-dataset results separately (RES-02 S3).
- **Metrics:** macro-F1; per-state recall; AUROC per state (one-vs-rest); Brier score and expected calibration error (ECE); **check-in precision** (share of triggered check-ins the user confirms); check-in **compliance** (share answered); prompts per day; 95% bootstrap CIs by user.
- **Subgroups (R-13):** gender, age band, device tier, OEM, language.

## 5. Ship criteria (G-M6) — proposed
| # | Criterion | Threshold | Why |
|---|---|---|---|
| S1 | Beats baselines | Macro-F1 ≥ max(B1, B2) **+ 0.05**, with 95% CIs not overlapping | Evidence predicts weak signals (RES-02); a small but real gain is the honest bar |
| S2 | Check-in precision | **≥ 0.50** | At least half of model-triggered check-ins should match what the user reports; below that, the prompts feel random |
| S3 | Calibration | ECE **≤ 0.10** | Thresholds (FR-14) rely on probabilities meaning what they say |
| S4 | Burden | Median **≤ 3** prompts per participant-day (all prompts); compliance **≥ 60%** | A-05; RES-02 S17 (mean 79% across studies) |
| S5 | Fairness | No subgroup's macro-F1 more than **0.10** below the overall value; any gap is reported in the model card | R-13 |
| S6 | Speed | p95 inference ≤ 100 ms on the reference device | NFR-02, SPK-05 |
| S7 | Relevance (OBJ-2) | **≥ 70%** of rated recommendations "helpful" or not "not relevant" | Charter OBJ-2 |

If S1–S3 fail, the app ships in **check-in-only mode** (FR-02): passive inference stays off, and the check-ins and recommendations still work. This is a valid outcome, not a failure of the product.

## 6. Recommendation evaluation
Offline replay of logged pilot feedback for bandit policies; online metric = helpful rate (S7).

## 7. Reporting
Every run goes into `docs/04-ai-ml/experiment-registry.md` with a dataset hash; the shipped model gets a model card (`docs/04-ai-ml/model-cards/`) with subgroup results and training data sources (AC-PR-07).
