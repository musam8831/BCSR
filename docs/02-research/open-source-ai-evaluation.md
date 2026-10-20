# Open-Source AI Evaluation — RES-03 v0.1

Constraint: **no paid LLM services**. All components must be open-source or self-built, with licences recorded. Owner: AG-04 with AG-06.

## 1. Technique map per module
| Module | Technique (candidate) | Why | Tooling |
|---|---|---|---|
| MOD-FE | Windowed statistical features, entropy of app switching, circadian features, deviation from personal baseline (z-scores) | Interpretable, cheap | Kotlin on device; pandas for research |
| MOD-SI cold start | Rule-based scorer from literature-derived heuristics | Works from day 1 | Kotlin |
| MOD-SI population model | Gradient-boosted trees (LightGBM/XGBoost) or logistic regression | Strong on tabular data; small; explainable (SHAP offline) | scikit-learn, LightGBM → ONNX/Kotlin |
| MOD-SI sequence model | Small 1D-CNN / GRU / Temporal Convolutional Network over event sequences | Captures temporal patterns | PyTorch → LiteRT/ONNX |
| MOD-SI personalisation | Fine-tune the last layer on device, or per-user calibration (Platt/isotonic), or a per-user anomaly detector (Isolation Forest / robust z-score) | Individual differences dominate | LiteRT on-device training, or a simple Kotlin update |
| MOD-SI semi-supervised | Self-supervised pre-training on unlabelled sequences (contrastive / masked event modelling) | Labels are scarce | PyTorch |
| MOD-RE | Contextual bandit (LinUCB or Thompson sampling) over approved items, with scholar-approved state→category constraints | Learns preference while staying safe | Kotlin |
| MOD-IN | Change-point detection, trend summaries via templates (no LLM) | Deterministic, safe copy | Kotlin |
| Privacy | Federated learning (later) and differential privacy for aggregates | Improve models without raw data | Flower (Apache-2.0), Opacus (Apache-2.0) |
| Optional NLU of user free-text in check-in | Small on-device multilingual text classifier (e.g., fine-tuned open model distilled to LiteRT) **or skip in MVP** | Urdu/English mixed text is hard; risky | Evaluate in M6; default: skip, use tap-based check-ins |

## 2. On-device open-weight generative models
Open-weight small LLMs (for example models runnable via MediaPipe LLM Inference) are *technically* free. They are **excluded from any religious-content path** by ADR-0004. They may be evaluated later only for non-religious UI microcopy, and only if their licence permits it. Default: not used in the MVP.

## 3. Licence register (fill in during T-M1-009)
| Component | Version | Licence | Commercial use OK? | Attribution needed | Notes |
|---|---|---|---|---|---|
| LiteRT | | Apache-2.0 | | | |
| ONNX Runtime | | MIT | | | |
| scikit-learn | | BSD-3 | | | |
| LightGBM | | MIT | | | |
| PyTorch | | BSD-3 | | | |
| Prayer times library (e.g., Adhan) | | verify | | | |
| Qur'an text source (e.g., Tanzil) | | verify terms (verbatim, attribution) | | | |
