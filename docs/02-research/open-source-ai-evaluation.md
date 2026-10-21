# Open-Source AI Evaluation — RES-03 v1.0

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

## 3. Licence register (T-M1-009, verified 2026-10-21; approved by Tech Lead 2026-10-21, PR #15)
Method: code licences are read from each project's GitHub repository (SPDX licence ID; the LICENSE file where GitHub reports none). Versions are the latest release on 2026-10-21; exact versions are pinned when the build is set up (M5) and re-checked then. All entries below allow commercial use and modification under their licence terms.

### 3.1 Software
| Component | Use | Latest release (2026-10-21) | Licence | Commercial use OK? | Attribution needed | Notes |
|---|---|---|---|---|---|---|
| LiteRT (google-ai-edge/LiteRT) | On-device inference (MOD-SI) | v2.3.0 | Apache-2.0 | Yes | Licence + NOTICE in app | — |
| ONNX Runtime (microsoft/onnxruntime) | Alternative on-device runtime | v1.31.0 | MIT | Yes | Licence notice | Pick one runtime in M2 (T-M2-005) |
| MediaPipe (google-ai-edge/mediapipe) | Only if on-device generative models are evaluated (§2) | v1.1.1 | Apache-2.0 | Yes | Licence + NOTICE | Not used in MVP |
| scikit-learn | Training (research) | 1.9.1 | BSD-3-Clause | Yes | Licence notice if redistributed | Training only, not shipped |
| LightGBM (microsoft/LightGBM) | Population model training | v4.7.0 | MIT | Yes | Licence notice if redistributed | Model exported to ONNX/LiteRT |
| XGBoost (dmlc/xgboost) | Alternative to LightGBM | v3.4.2 | Apache-2.0 | Yes | Licence + NOTICE if redistributed | — |
| PyTorch | Sequence models (research) | v2.14.1 | BSD-3-Clause-style (LICENSE file; GitHub reports NOASSERTION) | Yes | Licence notice if redistributed | Training only |
| SHAP (shap/shap) | Offline explanations (NFR-08) | v0.54.1 | MIT | Yes | Licence notice if redistributed | Training only |
| pandas | Research data handling | v3.0.6 | BSD-3-Clause | Yes | Licence notice if redistributed | Training only |
| Flower (adap/flower) | Federated learning (D3, post-MVP) | framework-1.41.0 | Apache-2.0 | Yes | Licence + NOTICE | Post-MVP |
| Opacus (pytorch/opacus) | Differential privacy (post-MVP) | v1.6.0 | Apache-2.0 | Yes | Licence + NOTICE | Post-MVP |
| Adhan (batoulapps/adhan-kotlin) | Salah times (FR-21) | adhan2-v0.0.7 | MIT | Yes | Licence notice in app | The older `batoulapps/Adhan` repo has had no push since 2023-03; use adhan-kotlin |
| SQLCipher for Android (sqlcipher/sqlcipher-android) | Encrypted local DB (PR-02) | v4.19.1 | BSD-3-Clause (LICENSE file text; GitHub reports NOASSERTION) | Yes (Community Edition) | Copyright notice in app docs | Commercial editions also exist; Community Edition is enough |

No copyleft (GPL/AGPL) component is planned. Any new dependency is added to this table before it is merged (NFR-09).

### 3.2 Content and text sources
| Source | Use | Licence / terms | Commercial use OK? | Attribution needed | Notes |
|---|---|---|---|---|---|
| Tanzil Qur'an text (tanzil.net) | Arabic Qur'an text (CR-03) | Creative Commons Attribution 3.0; **verbatim copies only, changing is not allowed** | Not addressed on the terms page; CC BY 3.0 itself permits it | Yes: name the Tanzil Project, link to tanzil.net, keep the copyright notice | Verbatim-only matches AGENTS.md §2. Edition choice and scholar sign-off belong to T-M1-010 |
| Hadith text, translations, transliteration | CR-03, CR-04 | **Not chosen yet** | — | — | Source selection is T-M1-010 (content policy, scholar) |

### 3.3 Datasets and instruments
Full terms in RES-04 §2 and §5.
| Item | Terms | Commercial / product use | Status |
|---|---|---|---|
| GLOBEM (PhysioNet) | Credentialed Health Data License 1.5.0 + DUA; research only, no sharing | **No** for the shipped model; D0 research only | Credentials pending (RES-04 action 3) |
| ExtraSensory | Free download, citation required; no licence stated | Unclear → research only | Optional |
| StudentLife | Not verified (site unreachable 2026-10-21) | Unknown | On hold |
| PSS-10 | Copyright S. Cohen; permission via MAPI ePROVIDE, free | Per permission granted | Permission pending (RES-04 action 1) |
| Urdu PSS-10 (Mariam et al., IIU Islamabad) | Translator's permission required | Per permission granted | Permission pending (RES-04 action 2) |
| Pre-trained ML models | None used: all models are trained by the project | — | — |

### 3.4 Conflicts (R-10)
None among the software. Two data limits: GLOBEM and ExtraSensory cannot feed the shipped model (RES-04 §1 rule), and the PSS and Urdu PSS cannot be used until permission is granted.
