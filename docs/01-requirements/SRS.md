# Software Requirements Specification — REQ-01 v0.3 (§1–§3, §5 catalogue complete for G1; persona input pending)

Structure follows ISO/IEC/IEEE 29148. Task: T-M1-002 (§1–§3), approved by Project Lead 2026-10-21 (PR #12). Sections marked **[M1]** or **[M2]** are completed later. The requirements catalogue in §5 is the **initial baseline**; it is frozen at Gate G1.

## 1. Introduction
### 1.1 Purpose
This SRS states what BCSR must do and the constraints it must meet. It is the reference for design (`docs/03-architecture`), tests (`docs/08-testing`) and acceptance at each gate. Readers: Project Lead / Tech Lead, the scholar reviewer, and the agent workforce (AGENTS.md).

### 1.2 Scope
BCSR (Behavioral & Contextual Stress Releaser) is an Android app that estimates the user's likely context state from on-device phone-usage metadata, checks in with the user, and recommends du'a and adhkar from the Qur'an and Sahih Hadith matched to that state. It works offline, uses no paid LLM services, and never diagnoses. In and out of scope: charter GOV-01 §4–§5. Scope boundaries and context states: GOV-02.

### 1.3 Definitions
| Term | Meaning |
|---|---|
| Context state | One of CS-01..CS-10 (GOV-02 §1): a probabilistic estimate of the user's situation, not a clinical emotion or diagnosis |
| EMA | Ecological Momentary Assessment: a short in-app check-in where the user reports mood and energy; the source of labels |
| Du'a item | A catalogue entry (du'a, dhikr or verse) with Arabic text, transliteration, translations and source reference (`content/schema/catalogue.schema.json`) |
| Approved item | A du'a item whose `verification.status` is `approved` by a named scholar reviewer with a review record (AGENTS.md §2, CR-02) |
| Personal baseline | The user's own rolling norm for each feature (≥ 7 days, FR-09), against which deviations are measured |
| Manhaj | The content methodology: Qur'an and Sahih Hadith per the Ahl-e-Hadith tradition (D-010) |
| Safety pathway | Crisis response with verified helplines and an emergency option (SR-01..SR-05) |

### 1.4 References
GOV-01 project charter · GOV-02 scope and assumptions · GOV-03 risk register · GOV-04 decision log (D-001..D-011) · ADR-0001..ADR-0004 · REQ-02 personas · user stories · acceptance criteria · traceability matrix · `docs/05-spiritual-content/` source policy and verification workflow · AGENTS.md · ISO/IEC/IEEE 29148:2018.

### 1.5 Overview
§2 describes the product and its constraints, §3 the modules, §4 external interfaces (M2), §5 the requirements catalogue, and §6 verification.

## 2. Overall description
### 2.1 Product perspective
A standalone, offline-first Android app (Kotlin, Jetpack Compose; min SDK 28 per ADR-0001, to be confirmed in M2). All sensing, feature extraction, inference and recommendation run on the device (ADR-0003). An optional research backend receives only consented, pseudonymised aggregates (MOD-RS, PR-03). iOS is out of scope for the MVP (D-002).

### 2.2 Product functions
1. Explain and request permissions one at a time; run in check-in-only mode if all are declined (FR-01, FR-02).
2. Collect passive usage metadata without any content (FR-03..FR-07, PR-01).
3. Compute windowed features and personal baselines (FR-08, FR-09).
4. Estimate a probability over context states (FR-10..FR-12).
5. Check in with the user, asking rather than asserting (FR-13, FR-14).
6. Recommend approved du'a items for the state and moment, and learn from feedback (FR-15..FR-17, FR-29).
7. Let the user browse, search and save catalogue items, with optional recitation and a dhikr counter (FR-18..FR-20, FR-22).
8. Time reminders around quiet hours, a daily cap and Salah times (FR-21).
9. Show weekly non-clinical insights (FR-23).
10. Route crisis input to the safety pathway (FR-24, SR-01..SR-05).
11. Give the user control over their data, and an optional research upload (FR-25..FR-27, PR-02..PR-06).
12. Offer Urdu, English and Arabic UI with RTL (FR-28).

### 2.3 User classes
Practising and returning Muslims in Pakistan who use Android phones: personas P1–P4 and the anti-persona in REQ-02 (to be validated by T-M1-003). A secondary class is pilot research participants who opt in to data sharing (PR-03).

### 2.4 Operating environment
Android 9 (API 28) or later on mid-range devices from the main OEMs in Pakistan; works fully offline (NFR-01). UI in Urdu, English and Arabic with RTL (FR-28).

### 2.5 Design constraints
- No paid LLM APIs in the product or the training pipeline; open-source, on-device models only (AGENTS.md §3, ADR-0003).
- No AccessibilityService or custom keyboard for observation (ADR-0002).
- Religious text is never generated, paraphrased or translated by AI; only approved catalogue items are shown (ADR-0004, D-010).
- Google Play policy for sensitive permissions (Usage Access) and prominent disclosure (PR-05).
- Pakistan data-protection obligations, and GDPR if EU users (charter §9).
- Budgets: inference ≤ 100 ms per window and battery ≤ 2% per day (NFR-02, NFR-04).

### 2.6 Assumptions and dependencies
Assumptions A-01..A-12 and their validating tasks are in GOV-02 §2 (A-08 dropped by D-011). External dependencies: a scholar reviewer (T-M0-006, R-15), ethics/IRB approval before the pilot (T-M1-012, R-11), and Play policy acceptance (T-M2-002, R-02).

## 3. Product modules
| Module | Code | Summary | Main inputs → outputs | Requirements |
|---|---|---|---|---|
| Onboarding & Consent | MOD-ON | Explain purpose, request permissions one at a time, consent tiers | User choices → granted permissions, consent record, mode (full / check-in only) | FR-01, FR-02, PR-03, PR-05, SR-02 |
| Context Sensing | MOD-SN | Collect passive metadata signals on device | Android usage, screen, notification-count and activity APIs → encrypted local event store | FR-03..FR-07, PR-01, PR-02, PR-04 |
| Feature Engineering | MOD-FE | Windowed features, personal baselines | Event store → feature windows and baselines (FS-001) | FR-08, FR-09 |
| State Inference | MOD-SI | ML models that estimate the context state | Features → probability over CS-01..CS-09 | FR-10..FR-12, NFR-02 |
| Check-in (EMA) | MOD-EM | Short mood check-ins; label capture | Inference confidence and user taps → confirmed labels; crisis input to MOD-SF | FR-13, FR-14, SR-03 |
| Recommendation Engine | MOD-RE | Select the verified du'a for the state and context; contextual bandit | State, time, history, feedback, approved items → ranked item with a reason | FR-15..FR-17, FR-29, NFR-08, CR-05 |
| Content Catalogue | MOD-CC | Verified du'a/adhkar store, search, favourites | Approved catalogue → item display, browse, search, recitation | FR-18..FR-20, CR-01..CR-04 |
| Reminder & Notification | MOD-NT | Timing, rate limits, quiet hours, prayer-time awareness | Recommendation, settings, Salah times → a notification or none; dhikr counter | FR-21, FR-22 |
| Insights | MOD-IN | Non-clinical reflections on patterns | A week of features → plain-language insight | FR-23 |
| Safety | MOD-SF | Crisis pathway, helplines | Explicit crisis input → helplines and emergency option first, then du'a | FR-24, SR-01..SR-05 |
| Privacy Center | MOD-PC | Transparency, export, delete, consent management | User requests → data view, export file, deletion, pause | FR-25, FR-26, PR-06 |
| Research Sync (optional) | MOD-RS | Consented, pseudonymised upload for model improvement | Consent and aggregates → TLS upload to the research backend | FR-27, PR-03 |
| Settings & Localisation | MOD-ST | Languages (Urdu, English, Arabic), themes, prayer-time method | User preferences → UI locale, RTL, theme, Salah method | FR-28, NFR-05 |

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
| FR-10 | MOD-SI | The system shall estimate a probability distribution over the passively detectable context states CS-01..CS-04, CS-06 and CS-08 (GOV-02 §1 v1). CS-05, CS-07 and CS-09 are taken only from check-ins. | M |
| FR-11 | MOD-SI | The system shall operate with a rule-based cold-start model until enough personal data exists. | M |
| FR-12 | MOD-SI | The system shall personalise the model on device using confirmed labels. | S |
| FR-13 | MOD-EM | The system shall offer a check-in of ≤ 3 taps (mood, energy, optional context tags). | M |
| FR-14 | MOD-EM | The system shall trigger a contextual check-in when inferred-state confidence exceeds a configurable threshold (default set in EP-001). | M |
| FR-15 | MOD-RE | The system shall recommend only items with `verification.status = approved`. | M |
| FR-16 | MOD-RE | The system shall rank items using state, time of day, recent history and feedback (contextual bandit). | M |
| FR-17 | MOD-RE | The system shall avoid repeating the same item within a configurable window (default 24 h). | S |
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
| PR-07 | Models shipped in the app are trained only on data BCSR collected with its own consent (D1–D3), never on public research datasets whose terms are research-only or unstated (RES-04 §1). | M |

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
| CR-06 | Every item follows the content manhaj: Qur'an and Sahih Hadith per the Ahl-e-Hadith tradition (D-010). | M |
| CR-07 | The app shows every attribution its content sources require (e.g. Tanzil Project name and link, RES-03 §3.2). | M |

## 5.6 Catalogue change log (T-M1-004, 2026-10-21)
| Change | Source |
|---|---|
| FR-10 limited to passively detectable states | HC-07 from T-M1-006 (taxonomy v1) |
| FR-14 threshold and FR-17 window made testable (configurable, with stated defaults) | Testability pass |
| PR-07 added | RES-04 §1 (approved 2026-10-21) |
| CR-06 added | D-010 |
| CR-07 added | RES-03 §3.2 (Tanzil terms) |

Interview findings (T-M1-003) are not yet included; new needs from them arrive as HC-07 requests before the G1 freeze.

## 6. Verification
Each requirement maps to user stories (REQ-03), acceptance criteria (REQ-04) and, from M2, test cases (`docs/08-testing`) via the traceability matrix (REQ-05). Every Must requirement has at least one acceptance criterion. Methods: **T** test (unit, instrumented, property), **I** inspection (manifest, code, copy review), **A** analysis (benchmark, audit), **D** demonstration. The method for each AC is stated in REQ-04.
