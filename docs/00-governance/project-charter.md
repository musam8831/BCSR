# Project Charter — BCSR

| Field | Value |
|---|---|
| Project | Behavioral & Contextual Stress Releaser (BCSR) |
| Document ID | GOV-01 · v1.0-rc (T-M0-004, for Gate G0) |
| Sponsor / Project Lead | Muhammad Usman |
| Status | Pending Project Lead signature |

## 1. Purpose
Build an Android app that understands, privately and on the device, the user's likely **context of mind**, **behavioural patterns** and **feelings** from how they use their phone. At the right moments it recommends authentic, context-appropriate supplications and adhkar from the Qur'an and Sahih Hadith, grounded in Qur'an 13:28: hearts find rest in the remembrance of Allah.

## 2. Problem statement
Mobile users experience stress, worry, restlessness and disturbed sleep that is often visible in their phone behaviour: compulsive switching between apps, late-night scrolling, bursts of notifications, social withdrawal. Existing Islamic apps are passive libraries that the user must remember to open. Existing wellbeing apps are secular and do not draw on the user's faith as a source of calm. BCSR closes this gap by being a *timely, respectful reminder*, not another library.

## 3. Objectives (SMART)
Each objective is specific to one outcome, measured as stated, time-bound to a milestone in `docs/10-project-management/milestones.md`, and relevant to the purpose in §1. Achievability is re-checked at G2 (feasibility) and on every gate.

| ID | Objective | Measure | By | Verified by |
|---|---|---|---|---|
| OBJ-1 | Infer a small set of context states from passive on-device signals | Macro-F1 above the personal-baseline heuristic on held-out user-weeks; the numeric target is set in EP-001 (T-M2-010, due M2) | M6 exit | EP-001 ship criteria; AG-07 + Tech Lead |
| OBJ-2 | Recommend contextually appropriate verified du'a | ≥ 70% of recommendations rated "relevant" by users | M9 closed pilot | Pilot exit report; AG-07 |
| OBJ-3 | Respect privacy | 0 raw behavioural records leave the device without research consent (PR-03) | From M5 (first data-collecting build), continuously | Threat model review at G2; privacy audit at M9; AG-09 |
| OBJ-4 | Deliver an MVP to a closed pilot | Pilot build installed by ≥ 30 consenting users | M9 | Pilot exit report; Project Lead |
| OBJ-5 | 100% content traceability | Every item shown in the app has an approved scholar review record | From M4 (catalogue v1), every build | Catalogue CI (T-M3-005); Scholar reviewer |

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
| Sponsor | Muhammad Usman | | Pending (T-M0-004) |
