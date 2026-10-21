# Dataset Strategy — RES-04 v1.0

Task: T-M1-013 · Owner: AG-07 · Reviewer: Tech Lead · Date: 2026-10-21 · Approved by Tech Lead 2026-10-21 (PR #14; D-009). Licence and access terms below were checked on 2026-10-21 against the sources named; re-check before use.

## 1. Phased approach
| Phase | Data | Purpose | Gate |
|---|---|---|---|
| D0 (M1–M2) | Public research datasets (§5) | Feature prototyping, sanity checks, baseline models (T-M2-006) | Access terms met (§5); entry in the licence register (T-M1-009) |
| D1 (M5) | Internal dogfooding: team members, consented, 2 weeks | Pipeline validation, battery, EMA burden | Internal consent form |
| D2 (M5–M6) | Pilot study: ≥ 30 participants, 4 weeks, EMA 3×/day + passive features | Train and evaluate the population model, personalisation | **Ethics/IRB approval** (T-M1-012) + PR-03 consent |
| D3 (post-MVP) | Federated, opt-in | Continuous improvement without raw data | Privacy review |

**The shipped model is trained only on D1–D3 data**, i.e. data BCSR collects with its own consent. D0 data is for research and prototyping only: its access terms are research-only or unstated (§5).

## 2. Labels and instruments
| Instrument | When | Use | Licence / permission | Urdu version | Status |
|---|---|---|---|---|---|
| EMA valence (5-point) | 3×/day (D2) | Mood label | Project-authored item; no licence needed | Project translation (en/ur), reviewed in T-M1-003 interviews | Selected |
| EMA energy / arousal (5-point) | 3×/day | Arousal label | Project-authored | Project translation | Selected |
| EMA state picker (CS-02..CS-09 in plain words, optional "other") | 3×/day | Context-state label | Project-authored | Project translation | Selected; state list follows T-M1-006 |
| PSS-10, Perceived Stress Scale (Cohen et al. 1983, DOI [10.2307/2136404](https://doi.org/10.2307/2136404)) | Weekly | Stress outcome; validity check for CS-02 | **Permission required** via MAPI Research Trust ePROVIDE; free of charge (CMU Laboratory for the Study of Stress, Immunity, and Disease, scales page) | Urdu PSS-10 by Aneeqa Mariam et al., International Islamic University Islamabad, listed on the CMU page; **translator's permission also required** | Selected, **pending two permissions** |
| Recommendation feedback (helpful / not now / not relevant) | Each recommendation | Bandit reward, **not** a state label (FR-29) | Project-authored | Project translation | Selected |

Not selected for the MVP:
- **PHQ-9, GAD-7 and other clinical screeners:** with no mental-health advisor (D-011), BCSR does not use diagnostic screening instruments. This also follows "ask, don't assert" (AGENTS.md §3).
- **Brief RCOPE, PMIR** (religious coping, RES-02 S21, S24): not needed for labels; revisit for the pilot's secondary outcomes once licence terms are checked.

Evidence for the EMA design (RES-02): across 477 EMA studies, mean compliance was 79% and the number of prompts per day did not predict compliance [S17]; 3×/day is within A-05 (2–4). Incentives raised compliance [S17], so the IRB application (T-M1-012) should state whether pilot participants are compensated.

Limitations: the project-authored EMA items are not psychometrically validated in Urdu. Their wording is checked in interviews (T-M1-003), and their test-retest reliability is reported from D1/D2.

## 3. Splits and leakage rules
- Split **by user** (population model) and **by time** (personal model). Never random-split windows from the same user across train and test.
- Cross-dataset evaluation: report results trained on one D0 dataset and tested on another, because single-dataset results overstate generalisation (RES-02 S3).
- No D0 participant identifiers or demographics are joined to BCSR data.
- Record every dataset version in `docs/04-ai-ml/experiment-registry.md` with a hash.

## 4. Storage rules
`ml/datasets/` holds only scripts, schemas and data cards. Raw pilot data and any D0 files live in an encrypted, access-controlled store outside Git, referenced by ID. D0 files obtained under a data use agreement are never shared beyond the named credentialed user.

## 5. Public datasets (D0)
| Dataset | Content | Access and licence (checked 2026-10-21) | Fit for BCSR | Decision |
|---|---|---|---|---|
| GLOBEM (PhysioNet, v1.1) | Feature-level data from 497 participants, 705 person-years (2018–2021): location, phone usage, calls, Bluetooth, steps, sleep, WiFi; weekly EMA, depression labels | **Credentialed:** PhysioNet account, CITI "Data or Specimens Only Research" training, signed PhysioNet Credentialed Health Data Use Agreement 1.5.0. Licence: PhysioNet Credentialed Health Data License 1.5.0: research use only, no re-identification, no sharing, publications must contribute code | Best fit: phone-usage features, EMA and mental-health labels, multi-year | **Use for D0 baselines**; Project Lead or a named team member applies for credentials |
| ExtraSensory (UC San Diego) | Phone and watch features with context and activity labels from 60 users | Free download; citation of Vaizman et al. 2017 required. **No licence stated** on the site | Weak: activity/context labels, not mental-health states | Optional; use only for feature-pipeline tests, cite as required |
| StudentLife (Dartmouth, 2014) | 48 students, 10 weeks, phone sensing, EMA, PHQ-9, PSS | **Not verified:** the project site refused connections on 2026-10-21; a Kaggle mirror exists, licence not checked | Good fit (stress, sleep, mood labels) | **Hold** until terms are verified |

## 6. Actions
| # | Action | Owner | Needed before |
|---|---|---|---|
| 1 | Request PSS permission on MAPI ePROVIDE | Project Lead | D1 (M5) |
| 2 | Request permission for the Urdu PSS-10 from its translator (contact per CMU scales page) | Project Lead | D1 (M5) |
| 3 | Apply for PhysioNet credentials and the GLOBEM DUA (incl. CITI training) | Project Lead or named team member | T-M2-006 |
| 4 | Verify StudentLife terms (site or Kaggle mirror) | AG-04 | Before any StudentLife use |
| 5 | Add GLOBEM, ExtraSensory and PSS to the licence register | AG-04 | Done in RES-03 §3.3 (T-M1-009) |
