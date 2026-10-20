# Project Charter — BCSR

| Field | Value |
|---|---|
| Project | Behavioral & Contextual Stress Releaser (BCSR) |
| Document ID | GOV-01 · v0.1 (draft for Gate G0) |
| Sponsor / Project Lead | Muhammad Usman |
| Status | Draft |

## 1. Purpose
Build an Android app that understands, privately and on the device, the user's likely **context of mind**, **behavioural patterns** and **feelings** from how they use their phone. At the right moments it recommends authentic, context-appropriate supplications and adhkar from the Qur'an and Sahih Hadith, grounded in Qur'an 13:28: hearts find rest in the remembrance of Allah.

## 2. Problem statement
Mobile users experience stress, worry, restlessness and disturbed sleep that is often visible in their phone behaviour: compulsive switching between apps, late-night scrolling, bursts of notifications, social withdrawal. Existing Islamic apps are passive libraries that the user must remember to open. Existing wellbeing apps are secular and do not draw on the user's faith as a source of calm. BCSR closes this gap by being a *timely, respectful reminder*, not another library.

## 3. Objectives (SMART, to be confirmed at G0)
| ID | Objective | Measure |
|---|---|---|
| OBJ-1 | Infer a small set of context states from passive on-device signals | Macro-F1 above the personal-baseline heuristic on held-out user-weeks (target set in the M2 evaluation plan) |
| OBJ-2 | Recommend contextually appropriate verified du'a | ≥ 70% of recommendations rated "relevant" by users in the pilot |
| OBJ-3 | Respect privacy | 0 raw behavioural records leave the device without research consent; passes the M2 threat model review |
| OBJ-4 | Deliver an MVP to a closed pilot | Pilot build to ≥ 30 consenting users by M9 |
| OBJ-5 | 100% content traceability | Every shown item has an approved scholar review record |

## 4. In scope (MVP)
- Android app (min SDK to be fixed by ADR-0001; Kotlin, Jetpack Compose).
- Passive signals: app-usage events and categories, screen on/off and unlock patterns, notification volume (counts only), time-of-day/night activity, physical activity state, charging/battery, optional coarse location context (home/away, no coordinates stored), in-app interaction.
- Active signals: short Ecological Momentary Assessment (EMA) mood check-ins, plus feedback on recommendations.
- On-device feature extraction, state inference and recommendation (bandit).
- Verified du'a/adhkar catalogue (Arabic, transliteration, Urdu, English) with references.
- Daily/weekly reflective insights (non-clinical).
- Safety pathway with helpline referral.

## 5. Out of scope (MVP)
- iOS (platform restrictions; revisit after M10; see platform-feasibility.md).
- Reading message content, typed text or screen content of other apps.
- Clinical diagnosis, therapy or medication advice.
- Generative AI for religious content; fatwa or rulings.
- Paid LLM services of any kind.

## 6. Stakeholders
| Role | Responsibility |
|---|---|
| Project Lead / Sponsor | Vision, gate approvals, prioritisation |
| Tech Lead | Architecture, code quality, ML direction |
| Scholar reviewer(s) | Approve every content item; source policy |
| Mental-health advisor | Safety pathway, EMA instrument choice, wording |
| Ethics/IRB (e.g., institutional review board) | Approve research data collection |
| Pilot users | Consenting participants |
| AI agent workforce | Execute tasks under AGENTS.md |

## 7. Success criteria for the project
The MVP passes Gate G-Release with: safety review signed off, the content catalogue fully approved, model cards published, a privacy audit passed, and pilot satisfaction ≥ 4/5.

## 8. High-level milestones
See `docs/10-project-management/milestones.md`. M0–M3 build the foundation and the agent workforce. M4–M10 are implementation by those agents.

## 9. Constraints
No paid LLMs; open-source licences only; Android-only MVP; Google Play policy compliance; Pakistan data-protection obligations (and GDPR if EU users); small team augmented by AI agents.

## 10. Approval
| Gate G0 | Name | Date | Decision |
|---|---|---|---|
| Sponsor | | | |
