# Scope and Assumptions — GOV-02 v1.0

Task: T-M0-005 · Status: Approved by Project Lead 2026-10-20 via PR #3 (in place of the AG-16 review). §1 taxonomy v1 from T-M1-006 (evidence-based); scholar sign-off pending.

## 1. Context states (MVP taxonomy v1, T-M1-006)
The model estimates *context states*, not emotions in a clinical sense. Each state maps to content categories. v1 adds, for each state, the strength of evidence that phone signals detect it (RES-02) and how BCSR detects it:
- **Passive prompt:** phone signals may trigger a check-in; the state is stored only after the user confirms it (FR-14, "ask, don't assert").
- **EMA-only:** never inferred from phone signals; the user picks it in a check-in.
- **Explicit only:** only the user's own words or answers (SR-03).

| State ID | State | Example passive cues (hypotheses) | Evidence for passive detection (RES-02) | Detection mode | Content category examples |
|---|---|---|---|---|---|
| CS-01 | Calm / baseline | Usage near personal norm | Defined relative to the personal baseline, not detected as such [S3, S19] | Passive (default state) | Morning/evening adhkar, gratitude |
| CS-02 | Worry / stress | Rapid app switching, frequent unlock checks, fragmented sessions | **Moderate for stress, none found for anxiety**: stress linked to activity, sleep, SMS and screen on/off [S6, S7, S1]; screen features inconsistent across studies [S4] | Passive prompt | Du'a for worry and grief (hamm & hazan) |
| CS-03 | Restlessness / overstimulation | Long uninterrupted short-video/social sessions, doomscrolling | **Weak**: app-use and screen features inconsistent [S4]; more notifications ↔ more negative emotion [S36] | Passive prompt (low confidence) | Dhikr for tranquillity, istighfar |
| CS-04 | Sadness / low mood | Reduced activity, social-app withdrawal (mobility limited by PR-04) | **Moderate**: sleep, activity and home stay are the most consistent depression correlates [S4, S12]; the strongest, mobility, is limited by PR-04 | Passive prompt | Du'a in distress (karb), sabr verses |
| CS-05 | Anger / frustration | — | **None found** | **EMA-only** (was passive, weak) | Seeking refuge from Shaytan, du'a for calm |
| CS-06 | Sleeplessness / late-night use | Screen activity in the user's habitual sleep window | **Strongest**: phone use predicts sleep duration and deprivation [S8]; sleep is a consistent correlate [S4, S12] | Passive prompt | Sleep adhkar |
| CS-07 | Loneliness | — | **Weak without location**: detected mainly from mobility and evening time off campus [S9] and physical activity [S6]; location is limited by PR-04 | **EMA-only** for the MVP (was EMA-led) | Du'a for companionship, Allah's nearness verses |
| CS-08 | Fatigue / overwork | Long productivity-app sessions, little break | **Weak**: workload ↔ stress and less sleep [S1]; no direct evidence for app-session cues | Passive prompt (low confidence) | Du'a for strength/ease |
| CS-09 | Gratitude / positive | — | Not applicable (self-report) | EMA-only (unchanged) | Shukr adhkar |
| CS-10 | Possible crisis | EMA response or explicit in-app statement | Not applicable; never inferred passively (SR-03) | Explicit only (unchanged) | **Safety pathway** (SR-01) plus du'a |

Changes from v0.1 (2026-10-21): CS-02 renamed "Worry / stress" (no anxiety-specific passive evidence); CS-05 and CS-07 become EMA-only; evidence and detection-mode columns added. Content category examples are unchanged: they are the scholar reviewer's to approve (CR-05, D-010).

**Requirement impact (HC-07 change request to AG-02, for T-M1-004):** FR-10 says the model estimates a distribution over CS-01..CS-09. Proposed wording: "over the passively detectable states CS-01..CS-04, CS-06 and CS-08; CS-05, CS-07 and CS-09 come only from check-ins". Not applied here, because changing a requirement belongs to AG-02 with Project Lead approval.

Sign-off: Project Lead (in place of the MH advisor, D-011): **approved 2026-10-21 (PR #16)**. Scholar reviewer, for the content mappings: **pending until a reviewer is confirmed (T-M0-006)**.

## 2. Assumptions
| ID | Assumption | Validated by |
|---|---|---|
| A-01 | Android allows reading app-usage statistics with the user-granted Usage Access permission | T-M2-001 spike |
| A-02 | Google Play accepts the declared use of Usage Access for a digital-wellbeing purpose | T-M2-002 policy review |
| A-03 | Passive signals combined with EMA labels can predict some states better than chance per user | T-M1-008 literature review; T-M2-006 baseline experiment (confirmed in M6) |
| A-04 | Qualified scholar reviewer(s) are available for content approval | T-M0-006 |
| A-05 | Pilot users will complete 2–4 EMA prompts a day for at least 2–4 weeks | T-M1-013 EMA instrument selection; T-M1-003 interviews (confirmed in the M5 pilot) |
| A-06 | On-device inference fits a mid-range phone budget (<1% battery/day for inference) | T-M2-005 |
| A-07 | Open-source Qur'an text/translation and Hadith references can be licensed for app use | T-M1-010 (content licences); T-M1-009 (licence register) |
| A-08 | ~~A mental-health advisor is available to approve the safety pathway and EMA wording~~ Dropped: no advisor (D-011) | — |
| A-09 | Background collection stays reliable on the top-5 Android OEMs despite battery optimisation | T-M2-004 spike |
| A-10 | Notification counts and physical-activity state can be read without content access | T-M2-003 spike |
| A-11 | Ethics/IRB approval can be obtained before the M5 pilot starts (R-11) | T-M1-012 |
| A-12 | At least 30 consenting pilot users can be recruited (OBJ-4) | T-M1-003 interviews (early signal); recruitment task to be created in T-M3-009 |

## 3. Scope boundaries
- **Observation boundary:** metadata only (which app category, when, for how long, how often). No content.
- **Inference boundary:** probabilistic state, confirmed by the user before being stored as a label.
- **Recommendation boundary:** only approved catalogue items; ranking by the ML/bandit; no generated text.
