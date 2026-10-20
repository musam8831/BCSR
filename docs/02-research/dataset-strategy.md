# Dataset Strategy — RES-04 v0.1

## 1. Phased approach
| Phase | Data | Purpose | Gate |
|---|---|---|---|
| D0 (M1–M2) | Public research datasets (candidates: StudentLife, GLOBEM, ExtraSensory — verify the licence and access terms of each) | Feature prototyping, sanity checks, baseline models | Licence approved in the register |
| D1 (M5) | Internal dogfooding: team members, consented, 2 weeks | Pipeline validation, battery, EMA burden | Internal consent form |
| D2 (M5–M6) | Pilot study: ≥ 30 participants, 4 weeks, EMA 3×/day + passive features | Train and evaluate the population model, personalisation | **Ethics/IRB approval** + PR-03 consent |
| D3 (post-MVP) | Federated, opt-in | Continuous improvement without raw data | Privacy review |

## 2. Labels
- EMA: valence (5-point), arousal/energy (5-point), and a state picker (CS-02..CS-09 in plain Urdu/English words) with an optional "other".
- Weekly: PSS-10 (if an Urdu-validated version is licensed and approved by the MH advisor).
- Implicit: recommendation feedback (helpful / not now / not relevant) is a bandit reward, **not** a state label.

## 3. Splits and leakage rules
- Split **by user** (population model) and **by time** (personal model). Never random-split windows from the same user across train and test.
- Record every dataset version in `docs/04-ai-ml/experiment-registry.md` with a hash.

## 4. Storage rules
`ml/datasets/` holds only scripts, schemas and data cards. Raw pilot data lives in an encrypted, access-controlled store outside Git, referenced by ID.
