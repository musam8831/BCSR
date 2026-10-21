# Software Requirements Specification — REQ-01 v0.1 (outline + baseline catalogue)

Structure follows ISO/IEC/IEEE 29148. Sections marked **[M1]** are completed by the Requirements Engineer agent (AG-02) in Milestone 1. The requirements catalogue in §5 is the **initial baseline**; it is frozen at Gate G1.

## 1. Introduction
1.1 Purpose · 1.2 Scope (see GOV-02) · 1.3 Definitions (context state, EMA, du'a item, approved item, personal baseline) · 1.4 References · 1.5 Overview

## 2. Overall description **[M1]**
2.1 Product perspective (standalone Android app, optional research backend) · 2.2 Product functions · 2.3 User classes (see personas.md) · 2.4 Operating environment · 2.5 Design constraints (no paid LLM, on-device, Play policy) · 2.6 Assumptions & dependencies (GOV-02 §2)

## 3. Product modules
| Module | Code | Summary |
|---|---|---|
| Onboarding & Consent | MOD-ON | Explain purpose, request permissions one at a time, consent tiers |
| Context Sensing | MOD-SN | Collect passive metadata signals on device |
| Feature Engineering | MOD-FE | Windowed features, personal baselines |
| State Inference | MOD-SI | ML models that estimate the context state |
| Check-in (EMA) | MOD-EM | Short mood check-ins; label capture |
| Recommendation Engine | MOD-RE | Select the verified du'a for the state and context; contextual bandit |
| Content Catalogue | MOD-CC | Verified du'a/adhkar store, search, favourites |
| Reminder & Notification | MOD-NT | Timing, rate limits, quiet hours, prayer-time awareness |
| Insights | MOD-IN | Non-clinical reflections on patterns |
| Safety | MOD-SF | Crisis pathway, helplines |
| Privacy Center | MOD-PC | Transparency, export, delete, consent management |
| Research Sync (optional) | MOD-RS | Consented, pseudonymised upload for model improvement |
| Settings & Localisation | MOD-ST | Languages (Urdu, English, Arabic), themes, prayer-time method |

## 4. External interfaces **[M2]**
User interface (see 07-ux), hardware (sensors), software (Android APIs: UsageStatsManager, ActivityRecognition, NotificationListenerService — subject to ADR), communication (TLS 1.3 to the research backend only).

## 5. Requirements catalogue (baseline v0.1)
Priority uses MoSCoW: M = Must, S = Should, C = Could, W = Won't (this release).

### 5.1 Functional requirements
| ID | Module | Requirement | Priority |
|---|---|---|---|
| FR-01 | MOD-ON | The system shall explain each data signal and its purpose before requesting the related permission. | M |
| FR-02 | MOD-ON | The system shall function in a reduced "check-in only" mode if the user declines all passive-sensing permissions. | M |
| FR-03 | MOD-SN | The system shall record app-usage events (package, category, start/end time) via the Usage Access permission. | M |
| FR-04 | MOD-SN | The system shall record screen on/off and unlock events. | M |
| FR-05 | MOD-SN | The system shall record notification arrival counts per app category, without content. | S |
| FR-06 | MOD-SN | The system shall record physical activity state (still/walking/vehicle) when permitted. | S |
| FR-07 | MOD-SN | The system shall map app packages to categories using an on-device category table. | M |
| FR-08 | MOD-FE | The system shall compute windowed features (e.g., 15 min, 1 h, daily) defined in the feature specifications. | M |
| FR-09 | MOD-FE | The system shall maintain a rolling personal baseline (≥ 7 days) per feature. | M |
| FR-10 | MOD-SI | The system shall estimate a probability distribution over context states CS-01..CS-09. | M |
| FR-11 | MOD-SI | The system shall operate with a rule-based cold-start model until enough personal data exists. | M |
| FR-12 | MOD-SI | The system shall personalise the model on device using confirmed labels. | S |
| FR-13 | MOD-EM | The system shall offer a check-in of ≤ 3 taps (mood, energy, optional context tags). | M |
| FR-14 | MOD-EM | The system shall trigger a contextual check-in when inferred-state confidence exceeds a threshold. | M |
| FR-15 | MOD-RE | The system shall recommend only items with `verification.status = approved`. | M |
| FR-16 | MOD-RE | The system shall rank items using state, time of day, recent history and feedback (contextual bandit). | M |
| FR-17 | MOD-RE | The system shall avoid repeating the same item within a configurable window. | S |
| FR-18 | MOD-CC | The system shall display each item with Arabic text, transliteration, translation (Urdu/English) and full source reference. | M |
| FR-19 | MOD-CC | The system shall provide audio recitation where licensed audio exists. | C |
| FR-20 | MOD-CC | The system shall allow browsing by situation, favourites and search. | M |
| FR-21 | MOD-NT | The system shall respect quiet hours, a daily notification cap and Salah times. | M |
| FR-22 | MOD-NT | The system shall support a dhikr counter (tasbih) for recommended adhkar. | S |
| FR-23 | MOD-IN | The system shall show weekly non-clinical insights (e.g., "late-night use was higher this week"). | S |
| FR-24 | MOD-SF | The system shall present the safety pathway when the user indicates crisis or self-harm intent. | M |
| FR-25 | MOD-PC | The system shall let users view, export and delete all locally stored data. | M |
| FR-26 | MOD-PC | The system shall let users pause sensing at any time. | M |
| FR-27 | MOD-RS | The system shall upload only consented, pseudonymised feature aggregates and labels. | C |
| FR-28 | MOD-ST | The system shall support Urdu, English and Arabic UI, including RTL layout. | M |
| FR-29 | MOD-RE | The system shall collect "helpful / not now / not relevant" feedback on each recommendation. | M |

### 5.2 Non-functional requirements
| ID | Category | Requirement | Priority |
|---|---|---|---|
| NFR-01 | Offline | All core functions work without network access. | M |
| NFR-02 | Performance | On-device inference ≤ 100 ms per window on a reference mid-range device. | M |
| NFR-03 | Storage | Local behavioural store ≤ 50 MB with rolling retention (default 90 days). | S |
| NFR-04 | Battery | Total app battery use ≤ 2% per day on the reference device. | M |
| NFR-05 | Accessibility | WCAG 2.2 AA equivalent; font scaling; TalkBack labels. | M |
| NFR-06 | Reliability | Crash-free sessions ≥ 99.5% in the pilot. | S |
| NFR-07 | Maintainability | Modular architecture; ≥ 80% unit-test coverage on domain logic. | S |
| NFR-08 | Explainability | Every recommendation shows a "why am I seeing this" reason. | M |
| NFR-09 | Licensing | All dependencies, models, datasets and content have compatible licences recorded. | M |

### 5.3 Privacy requirements
| ID | Requirement | Priority |
|---|---|---|
| PR-01 | No message, typed-text, screen or audio content is collected. | M |
| PR-02 | Behavioural data is encrypted at rest (SQLCipher or an equivalent encrypted DB). | M |
| PR-03 | Research upload requires separate, granular, revocable opt-in consent. | M |
| PR-04 | Precise location is never stored; at most derived "home/away" flags. | M |
| PR-05 | A privacy notice compliant with Play's prominent-disclosure rules is shown. | M |
| PR-06 | Data retention limits are enforced automatically. | M |

### 5.4 Safety requirements
| ID | Requirement | Priority |
|---|---|---|
| SR-01 | Crisis indicators show region-appropriate, verified helplines and an emergency option immediately. | M |
| SR-02 | The app states clearly that it is not a medical or therapeutic service. | M |
| SR-03 | Crisis detection relies on explicit user input, not passive inference alone. | M |
| SR-04 | Safety copy and pathway are approved by the Project Lead (D-011; no mental-health advisor), with helplines checked against official sources. | M |
| SR-05 | Helpline data is reviewed for accuracy at least quarterly. | M |

### 5.5 Content requirements
| ID | Requirement | Priority |
|---|---|---|
| CR-01 | Every item has a primary source reference (Surah:Ayah, or collection + hadith number + grading + grader). | M |
| CR-02 | Every item has an approval record by a named scholar reviewer. | M |
| CR-03 | Arabic text is sourced from a verified digital mushaf/hadith source with its licence recorded. | M |
| CR-04 | Translations are from licensed, attributed sources or are produced by qualified human translators. | M |
| CR-05 | Content-to-state mappings are approved by the scholar reviewer. | M |

## 6. Verification **[M1]**
Each requirement maps to acceptance criteria (acceptance-criteria.md) and test cases (08-testing) via the traceability matrix.
