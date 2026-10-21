# Scientific Evidence Brief — RES-02 v1.0-rc

Owner: AG-04 Research Analyst · Task: T-M1-008 · Reviewer: AG-07 · Date: 2026-10-21

## 0. Method and limits (read first)
- **36 peer-reviewed sources**, each with a DOI. Every DOI was checked against OpenAlex on 2026-10-21: the registered title, year and first author matched the intended paper. Two candidates were dropped because their DOI did not resolve to the expected paper.
- **Findings are extracted from abstracts only.** Full texts were not read. Effect sizes are quoted only where the abstract states them. Anything needing more detail is marked *full text needed*.
- Claims are labelled **[finding]** (stated by the source) or **[inference]** (our reading for BCSR).
- Quality is rated by design: **A** systematic review / meta-analysis · **B** randomised trial or large multi-site study · **C** single-site observational, n ≥ 40 · **D** small study (n < 40), pilot or narrative review.

## 1. Research questions
- RQ1: Which passive smartphone signals are associated with stress, anxiety, low mood, poor sleep and loneliness? What effect sizes are reported?
- RQ2: What labelling methods (EMA, validated scales) are standard? Which instruments are licence-free and validated in Urdu?
- RQ3: Population vs personalised models: what improvement does personalisation bring?
- RQ4: What evidence exists for the effects of religious/spiritual coping, dhikr and prayer on stress (including Muslim populations)?
- RQ5: What are best practices for just-in-time adaptive interventions (JITAI): timing, receptivity, burden?

## 2. Answers

### RQ1 — Passive signals
- **[finding]** Reviews agree that sleep, physical activity, mobility/location and phone-use features are the most consistent correlates of depressive mood, but studies are small, short, heterogeneous and poorly standardised [S11, S12, S4]. de Angel et al. (51 studies) say pooled results should be taken "purely as a starting point for hypothesis generation" [S12].
- **[finding]** Direction is consistent for home stay, sleep duration and vigorous activity, but **inconsistent across studies for SMS sent, time between locations and screen-activity frequency** [S4].
- **[finding]** Reported effect sizes are moderate in small samples, e.g. circadian movement r = −.63 and location entropy r = −.58 with PHQ-9 in 28 adults [S2].
- **[finding]** Stress: daily stress associated with geospatial activity, sleep duration and its variability (47 young adults, 10 weeks) [S6]; stress associated with activity level, SMS and screen on/off patterns, > 75% binary accuracy with screen, mobility, call or activity data (18 people, 5 days) [S7]; stress rises and sleep, conversation and activity fall as student workload grows [S1].
- **[finding]** Sleep: phone-use patterns detect and predict individual sleep duration and deprivation (9 people, 97 days) [S8].
- **[finding]** Loneliness: 80.2% accuracy for high/low loneliness from phone + Fitbit data; lonelier students spent less evening time off campus and at social venues (160 students) [S9]. Changes in loneliness tracked kinesthetic activity [S6].
- **[finding]** Models trained on one population barely beat "guess the majority" on another; individual differences are the main obstacle (4 datasets, > 500 users) [S3]. Reviews stress that sensor-to-state relationships are not simple and differ between people [S5, S10].
- **[inference]** For BCSR's signals (app use, screen/unlock, notification counts, activity), **screen and app-use features have the weakest and least consistent evidence**; mobility, the strongest signal in the literature, is mostly excluded by PR-04 (no precise location). Expect weak associations, especially for CS-02 worry, CS-05 anger and CS-07 loneliness. **Anxiety-specific passive evidence was not found** in this search; most studies use depression as the outcome.

### RQ2 — Labels and instruments
- **[finding]** EMA, repeated real-time self-report in daily life, reduces recall bias and is the standard ground truth for momentary states [S13, S14].
- **[finding]** Across 477 EMA studies (N = 677,536): typically 6 prompts a day for 7 days, mean compliance 79%. The number of prompts per day did not predict compliance or dropout; financial incentives raised compliance [S17].
- **[finding]** The PSS (Perceived Stress Scale) measures appraised stress, with adequate reliability and validity, and a 4-item version exists [S15].
- **[finding]** The Brief RCOPE (14 items) measures positive and negative religious coping [S21]; the PMIR is a 60-item Islam-specific measure with seven factors [S24].
- **Gap — Urdu validation:** no peer-reviewed Urdu validation of the PSS-10 could be located in this search; one Pakistani study used an Urdu PSS but validated a different scale. **[assumption]** An Urdu PSS-10 validation exists; to be checked in T-M1-013 before the instrument is chosen.
- **Gap — licences:** the licence terms of PSS, Brief RCOPE and PMIR were not verified here (not in the abstracts). Also for T-M1-013.

### RQ3 — Personalisation
- **[finding]** Personalised multitask models give "large performance improvements" over one-size-fits-all models for next-day mood, stress and health [S20].
- **[finding]** N-of-1 models: the best model type differed for each person, and individually chosen models had significantly lower error than a common ensemble [S19].
- **[finding]** Per-person mobility models predicted changes in depressive mood [S18]; population models generalise poorly across cohorts [S3].
- **[inference]** Supports FR-09 (personal baselines) and FR-12 (on-device personalisation), with a rule-based cold start (FR-11). The size of the gain is not stated in the abstracts (*full text needed*).

### RQ4 — Religious coping, dhikr and Qur'an
- **[finding]** Meta-analysis of 49 studies (105 effect sizes): positive religious coping relates to better adjustment to stress, negative religious coping to worse adjustment [S22].
- **[finding]** Meta-analysis of 23 RCTs: religious/spiritual interventions significantly reduced anxiety symptoms [S35].
- **[finding]** Systematic review of 28 RCTs and quasi-experiments: listening to Qur'an recitation reduced anxiety in various settings [S30]; scoping reviews of 15 and 44 studies found reductions in anxiety, stress and depression from recitation, listening, Salah and supplication [S31, S34].
- **[finding]** Dhikr: one RCT (80 CABG patients) found lower anxiety after reciting a praise (Zikr) than routine care on all three days (P ≤ .001) [S32]. A Qur'an-listening study in neurosurgery candidates reported lower anxiety [S33].
- **[finding]** A broad review links religion/spirituality to better mental-health outcomes overall [S16].
- **[finding]** Empirical psychology of Islam: Islam's role is mostly positive, but studies of Muslims are scarce, use convenience samples, and some forms of religiousness are negative [S27, S23].
- **[inference]** Evidence that du'a/dhikr *reduces* stress is **promising but low-to-moderate quality**: mostly small trials in Iranian hospital patients, with listening rather than self-recited dhikr, and few trials of everyday stress in community samples. No study tested a *phone-prompted* du'a. The dhikr trial [S32] used praises (Hazrat Zahra's tasbih) that the catalogue would include only if they are Sahih and approved under D-010. BCSR should **not claim a clinical effect** and should evaluate its own effect (MOD-RE feedback, pilot outcomes). Negative religious coping [S21, S22] means copy must avoid guilt or punishment framing.

### RQ5 — JITAI timing and burden
- **[finding]** JITAIs adapt support to a person's changing state; the key components are decision points, tailoring variables, intervention options and decision rules, and many JITAIs were built with little evidence [S25].
- **[finding]** Micro-randomised trials estimate the causal effect of each prompt and how it varies with time and context [S28].
- **[finding]** Receptivity to mHealth prompts depends on the person (age, personality, device type); models predicted receptivity with up to 77% better F1 than a biased random classifier (189 people, 6 weeks) [S26].
- **[finding]** Notifications cause disruption even when useful; response and perceived disruption depend on alert type, sender and the current task [S29]. People received 63.5 notifications a day, and more notifications went with more negative emotion [S36].
- **[inference]** Supports FR-21 (quiet hours, daily cap, Salah times) and FR-17 (no repeats). A micro-randomised design fits the pilot's evaluation of whether prompts help (EP-001, M5).

## 3. Evidence table
| # | Source (DOI) | Population | Signals / intervention | Outcome / label | Method | Key result (abstract) | Quality | Relevance |
|---|---|---|---|---|---|---|---|---|
| S1 | Wang et al. 2014, StudentLife, UbiComp ([10.1145/2632048.2632054](https://doi.org/10.1145/2632048.2632054)) | 48 US students, 10 wks | Phone sensing | Stress, sleep, mood, sociability | Observational | Significant sensor–outcome correlations; stress rises with workload; public dataset | C | CS-02, CS-06, CS-08 |
| S2 | Saeb et al. 2015, JMIR ([10.2196/jmir.4273](https://doi.org/10.2196/jmir.4273)) | 28 adults, 2 wks | GPS, phone use | PHQ-9 | Observational | Circadian movement r = −.63, entropy r = −.58 | D | CS-04 |
| S3 | Xu et al. 2022, GLOBEM, IMWUT ([10.1145/3569485](https://doi.org/10.1145/3569485)) | > 500 users, 4 datasets | Phone + wearable | Depression | Cross-dataset benchmark | Prior models barely beat majority guess across datasets | B | CS-04; FR-11, FR-12 |
| S4 | Rohani et al. 2018, JMIR mHealth ([10.2196/mhealth.9691](https://doi.org/10.2196/mhealth.9691)) | 46 studies | 85 features | Depressive mood | Systematic review | Home stay, sleep, vigorous activity consistent; SMS, screen activity inconsistent | A | CS-04 |
| S5 | Mohr et al. 2017, Annu Rev Clin Psychol ([10.1146/annurev-clinpsy-032816-044949](https://doi.org/10.1146/annurev-clinpsy-032816-044949)) | — | Personal sensing | Mental health | Critical review | Layered sensor → behaviour → state model; privacy, dimensionality challenges | D (review) | All; FS-001 |
| S6 | Ben-Zeev et al. 2015, Psychiatr Rehabil J ([10.1037/prj0000130](https://doi.org/10.1037/prj0000130)) | 47 young adults, 10 wks | GPS, accelerometer, sleep, speech | Daily stress, PHQ-9, PSS, UCLA | Observational | Geospatial activity, sleep linked to stress; kinesthetic activity to loneliness | C | CS-02, CS-04, CS-07 |
| S7 | Sano & Picard 2013, ACII ([10.1109/ACII.2013.117](https://doi.org/10.1109/ACII.2013.117)) | 18 people, 5 days | Wrist sensor, calls, SMS, screen | Stress | Observational + ML | > 75% binary accuracy; stress linked to SMS, screen on/off | D | CS-02 |
| S8 | Abdullah et al. 2014, UbiComp ([10.1145/2632048.2632100](https://doi.org/10.1145/2632048.2632100)) | 9 people, 97 days | Phone use | Sleep, chronotype | Observational | Phone use predicts sleep duration, deprivation | D | CS-06 |
| S9 | Doryab et al. 2019, JMIR mHealth ([10.2196/13209](https://doi.org/10.2196/13209)) | 160 students | Phone + Fitbit | UCLA loneliness | Observational + ML | 80.2% accuracy; less evening time out | C | CS-07 |
| S10 | Sheikh et al. 2021, Front Digit Health ([10.3389/fdgth.2021.662811](https://doi.org/10.3389/fdgth.2021.662811)) | — | Wearable, phone, environment | Mental health | Narrative review | Relationships not simple; individual differences matter | D (review) | All |
| S11 | Cornet & Holden 2017, J Biomed Inform ([10.1016/j.jbi.2017.12.008](https://doi.org/10.1016/j.jbi.2017.12.008)) | — | Smartphone passive sensing | Health, wellbeing | Systematic review | *Abstract not available; full text needed* | A | All |
| S12 | de Angel et al. 2022, npj Digit Med ([10.1038/s41746-021-00548-8](https://doi.org/10.1038/s41746-021-00548-8)) | 51 studies | Phone + wearable | Depression | Systematic review | Sleep, activity, location, phone use associated; small samples, poor reporting | A | CS-04 |
| S13 | Trull & Ebner-Priemer 2012, Annu Rev Clin Psychol ([10.1146/annurev-clinpsy-050212-185510](https://doi.org/10.1146/annurev-clinpsy-050212-185510)) | — | Ambulatory assessment | — | Review | Real-time methods reduce bias; compliance, privacy, ethics issues | D (review) | FR-13 |
| S14 | Shiffman et al. 2007, Annu Rev Clin Psychol ([10.1146/annurev.clinpsy.3.022806.091415](https://doi.org/10.1146/annurev.clinpsy.3.022806.091415)) | — | EMA | — | Review | EMA reduces recall bias, raises ecological validity | D (review) | FR-13, FR-14 |
| S15 | Cohen et al. 1983, J Health Soc Behav ([10.2307/2136404](https://doi.org/10.2307/2136404)) | 3 samples | — | PSS | Validation | Adequate reliability, validity; 4-item version | C | Label for CS-02 |
| S16 | Koenig 2012, ISRN Psychiatry ([10.5402/2012/278730](https://doi.org/10.5402/2012/278730)) | Studies 1872–2010 | Religion / spirituality | Mental and physical health | Review | Reviews R/S links to well-being, depression, anxiety, suicide and health | D (review) | Core premise |
| S17 | Wrzus & Neubauer 2022, Assessment ([10.1177/10731911211067538](https://doi.org/10.1177/10731911211067538)) | 477 studies, N = 677,536 | EMA designs | Compliance | Meta-analysis | Mean 6/day × 7 days, 79% compliance; prompts/day not linked to compliance | A | FR-13; A-05 |
| S18 | Canzian & Musolesi 2015, UbiComp ([10.1145/2750858.2805845](https://doi.org/10.1145/2750858.2805845)) | Not in abstract | GPS mobility | Daily depressive mood | Observational + ML | Mobility correlates with mood; per-person prediction of changes | D | CS-04; FR-12 |
| S19 | Shah et al. 2021, Transl Psychiatry ([10.1038/s41398-021-01445-0](https://doi.org/10.1038/s41398-021-01445-0)) | Not in abstract, 1 month | EMA, EEG, wearables | Depressed mood | N-of-1 ML | Best model differs per person; individual models beat ensemble | D | FR-12 |
| S20 | Taylor et al. 2017, IEEE Trans Affect Comput ([10.1109/TAFFC.2017.2784832](https://doi.org/10.1109/TAFFC.2017.2784832)) | Not in abstract | Surveys, wearables, phone logs, weather | Next-day mood, stress, health | Multitask ML | Personalised MTL "large" gains vs population models | C | FR-12 |
| S21 | Pargament et al. 2011, Religions ([10.3390/rel2010051](https://doi.org/10.3390/rel2010051)) | — | Brief RCOPE | Religious coping | Psychometric review | Positive / negative subscales valid | C | Copy tone; label option |
| S22 | Ano & Vasconcelles 2005, J Clin Psychol ([10.1002/jclp.20049](https://doi.org/10.1002/jclp.20049)) | 49 studies | Religious coping | Adjustment to stress | Meta-analysis | Positive coping ↔ better, negative ↔ worse adjustment | A | Core premise |
| S23 | Abu-Raiya & Pargament 2014, Psychol Relig Spiritual ([10.1037/a0037652](https://doi.org/10.1037/a0037652)) | — | Religious coping across religions | — | Review | *Conclusions not in abstract; full text needed* | D (review) | Core premise |
| S24 | Abu-Raiya et al. 2008, Int J Psychol Relig ([10.1080/10508610802229270](https://doi.org/10.1080/10508610802229270)) | 340 Muslims (international) | PMIR | Islamic religiousness | Scale development | 7 reliable factors; valid against health criteria | C | Label / covariate option |
| S25 | Nahum-Shani et al. 2016, Ann Behav Med ([10.1007/s12160-016-9830-8](https://doi.org/10.1007/s12160-016-9830-8)) | — | JITAI | — | Framework | Decision points, tailoring variables, options, rules | D (framework) | MOD-RE, MOD-NT |
| S26 | Künzler et al. 2019, IMWUT ([10.1145/3369805](https://doi.org/10.1145/3369805)) | 189 people, 6 wks | Chatbot JITAI | Receptivity | Field study + ML | Person traits affect receptivity; ML up to +77% F1 | B | FR-14, FR-21 |
| S27 | Abu-Raiya & Pargament 2010, Ment Health Relig Cult ([10.1080/13674670903426482](https://doi.org/10.1080/13674670903426482)) | — | Islam and psychology | — | Systematic review | Mostly positive role; Muslim studies scarce, convenience samples | A | Core premise; R-13 |
| S28 | Klasnja et al. 2015, Health Psychol ([10.1037/hea0000305](https://doi.org/10.1037/hea0000305)) | — | Micro-randomised trial | — | Methods | Causal, time-varying effect of each prompt | D (methods) | EP-001, M5 pilot |
| S29 | Mehrotra et al. 2016, CHI ([10.1145/2858036.2858566](https://doi.org/10.1145/2858036.2858566)) | 20 users, 10,372 notifications | Notifications | Response time, disruption | In-situ + ESM | Useful notifications still disrupt; context matters | D | FR-21 |
| S30 | Ghiasi & Keramat 2018, Iran J Nurs Midwifery Res ([10.4103/ijnmr.ijnmr_173_17](https://doi.org/10.4103/ijnmr.ijnmr_173_17)) | 28 trials | Listening to Qur'an recitation | Anxiety (mostly STAI) | Systematic review | Positive effect on anxiety | A | CS-02 |
| S31 | Moulaei et al. 2023, Health Sci Rep ([10.1002/hsr2.1751](https://doi.org/10.1002/hsr2.1751)) | 15 studies, all in Asia | Qur'an recitation / listening | Anxiety, stress, depression | Scoping review | Studied mainly anxiety (45%), stress (30%), depression (25%) | A (scoping) | CS-02, CS-04 |
| S32 | Nasiri et al. 2016, Res Cardiovasc Med ([10.5812/cardiovascmed.41388](https://doi.org/10.5812/cardiovascmed.41388)) | 80 CABG patients, Iran | Reciting Zikr | HADS anxiety | RCT | Lower anxiety vs routine care, all 3 days (P ≤ .001) | B | CS-02 |
| S33 | Salari et al. 2025, Health Sci Rep ([10.1002/hsr2.70629](https://doi.org/10.1002/hsr2.70629)) | Neurosurgery candidates, Iran | Listening to Qur'an | STAI, vital signs | Controlled pre/post | Lower anxiety in intervention group | C | CS-02 |
| S34 | Owens et al. 2023, J Ment Health ([10.1080/09638237.2023.2232449](https://doi.org/10.1080/09638237.2023.2232449)) | 44 studies | Salah, supplication, Qur'an | Anxiety, depression, stress, QoL | Scoping review | Reductions reported; mostly biomedical, little from the West | A (scoping) | All |
| S35 | Gonçalves et al. 2015, Psychol Med ([10.1017/s0033291715001166](https://doi.org/10.1017/s0033291715001166)) | 23 RCTs | Religious/spiritual interventions | Anxiety, depression | Meta-analysis | Significant effect on anxiety | A | CS-02 |
| S36 | Pielot et al. 2014, MobileHCI ([10.1145/2628363.2628364](https://doi.org/10.1145/2628363.2628364)) | 15 users, 1 wk | Notifications | Emotion, connectedness | In-situ + diary | 63.5/day; more notifications ↔ more negative emotion | D | FR-21; MOD-SN notification counts |

## 4. Implications for design
1. **Ask, don't assert, is confirmed.** Passive associations are weak, inconsistent across studies and do not generalise across populations [S3, S4, S12]. Inference should only trigger a check-in (FR-14), never a stated feeling. Supports the existing "ask, don't assert" rule and R-03 (16).
2. **Personal baselines and personalisation are essential** [S3, S19, S20]: keep FR-09, FR-11, FR-12.
3. **Weakest evidence is for the signals BCSR relies on most.** Screen and app-use features are inconsistent [S4], and mobility, the strongest signal, is limited by PR-04. **T-M1-006** should consider merging or dropping states with no passive evidence (CS-05 anger, CS-07 loneliness without mobility, CS-09 gratitude) or making them EMA-only. *Recommendation for AG-07, not a scope change.*
4. **Labels:** EMA at a moderate rate is feasible; compliance does not fall with more prompts per day [S17], so A-05 (2–4 prompts a day) is conservative. Incentives help compliance; relevant to the M5 pilot design and IRB (T-M1-012).
5. **Religious content claims must stay modest.** The evidence is promising but small, mostly hospital-based and Iranian, and mostly about *listening* to recitation [S30–S34]. No claim of a therapeutic effect in app copy or marketing (AGENTS.md §3); measure helpfulness in the pilot (FR-29).
6. **Avoid negative religious framing** (guilt, punishment), which is linked to worse adjustment [S21, S22]; input for SK-12 copy rules and the T-M1-010 content policy.
7. **Notification burden is real** [S29, S36]: keep FR-21's cap and quiet hours. Use a micro-randomised design in the pilot to test whether prompts help [S28].
8. **Generalisation risk to Pakistani users** (R-13): nearly all sensing studies are of Western students. Expect lower accuracy; plan subgroup evaluation (EP-001).

## 5. Open items and validating tasks
| Item | Status | Validating task |
|---|---|---|
| Urdu validation of PSS-10 | Not found; assumption | T-M1-013 |
| Licence terms of PSS, Brief RCOPE, PMIR | Not verified | T-M1-013 |
| Anxiety-specific passive-sensing evidence | Not found in this search | T-M1-006 (taxonomy) |
| Effect sizes for personalisation gain | Not in abstracts | Full-text read, T-M2-006 |
| Evidence for phone-prompted du'a | None found | M5 pilot (EP-001) |
| Full-text extraction of A-rated sources | Not done | AG-07 review of this brief |
