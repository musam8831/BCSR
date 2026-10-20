# Scope and Assumptions — GOV-02 v1.0-rc

Task: T-M0-005 · Status: In review (AG-16). The §1 taxonomy stays a **draft** until T-M1-006 (evidence + MH advisor + scholar).

## 1. Context states (MVP taxonomy, to be validated in T-M1-006)
The model predicts *context states*, not emotions in a clinical sense. Each state maps to content categories.

| State ID | State | Example passive cues (hypotheses, to be validated) | Content category examples |
|---|---|---|---|
| CS-01 | Calm / baseline | Usage near personal norm | Morning/evening adhkar, gratitude |
| CS-02 | Worry / anxiety | Rapid app switching, frequent unlock checks, fragmented sessions | Du'a for worry and grief (hamm & hazan) |
| CS-03 | Restlessness / overstimulation | Long uninterrupted short-video/social sessions, doomscrolling | Dhikr for tranquillity, istighfar |
| CS-04 | Sadness / low mood | Reduced activity, social-app withdrawal, low mobility (with EMA) | Du'a in distress (karb), sabr verses |
| CS-05 | Anger / frustration | Bursty interaction, abrupt session ends (weak signal; EMA-led) | Seeking refuge from Shaytan, du'a for calm |
| CS-06 | Sleeplessness / late-night use | Screen activity in the user's habitual sleep window | Sleep adhkar |
| CS-07 | Loneliness | Low communication-app use relative to baseline, at home (EMA-led) | Du'a for companionship, Allah's nearness verses |
| CS-08 | Fatigue / overwork | Long productivity-app sessions, little break | Du'a for strength/ease |
| CS-09 | Gratitude / positive | EMA-reported | Shukr adhkar |
| CS-10 | Possible crisis | EMA response or explicit in-app statement (not passive inference alone) | **Safety pathway** (SR-01) plus du'a |

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
| A-08 | A mental-health advisor is available to approve the safety pathway and EMA wording | T-M0-006 |
| A-09 | Background collection stays reliable on the top-5 Android OEMs despite battery optimisation | T-M2-004 spike |
| A-10 | Notification counts and physical-activity state can be read without content access | T-M2-003 spike |
| A-11 | Ethics/IRB approval can be obtained before the M5 pilot starts (R-11) | T-M1-012 |
| A-12 | At least 30 consenting pilot users can be recruited (OBJ-4) | T-M1-003 interviews (early signal); recruitment task to be created in T-M3-009 |

## 3. Scope boundaries
- **Observation boundary:** metadata only (which app category, when, for how long, how often). No content.
- **Inference boundary:** probabilistic state, confirmed by the user before being stored as a label.
- **Recommendation boundary:** only approved catalogue items; ranking by the ML/bandit; no generated text.
