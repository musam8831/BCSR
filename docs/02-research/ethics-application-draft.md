# Ethics Application (Draft) — Pilot Study D2

Task: T-M1-012 · Owner: Project Lead (submits) + AG-07 (drafts) · Status: **draft; committee not chosen yet**. Adapt the sections to the chosen committee's form; the content below is the source for it. Acceptance (task register): "Submitted".

## 1. Study
| Field | Content |
|---|---|
| Title | BCSR pilot: feasibility of on-device phone-usage sensing and check-ins to time du'a and adhkar suggestions |
| Principal investigator | Muhammad Usman, Pakistan Institute of Development Economics (PIDE) |
| Duration | 4 weeks per participant (RES-04 D2), within M5–M6 |
| Site | Remote; participants' own Android phones in Pakistan |
| Funding | (to be filled by PI) |

## 2. Aims
1. Measure whether passive phone-usage features relate to self-reported mood, energy and context states (RQ1, RES-02).
2. Measure check-in compliance and burden at 3 prompts a day (A-05).
3. Measure whether participants find the suggested du'a relevant (OBJ-2: ≥ 70% "relevant").
4. Test privacy, battery and reliability in daily use (OBJ-3, NFR-04, NFR-06).

Not an aim: diagnosing or treating any mental-health condition.

## 3. Participants
- **≥ 30 adults (18+)**, Android users in Pakistan (OBJ-4, A-12).
- **Exclusion:** under 18; anyone who reports being in a current crisis at screening (they are given helplines, SAF-02, and not enrolled); people who cannot give consent.
- **Recruitment:** (PI to choose: e.g. university notice boards, community networks). Voluntary; no pressure from any relationship with the team.
- **Compensation:** (PI to decide; RES-02 S17 shows incentives raise compliance. State amount and schedule here.)

## 4. Procedures
1. Informed consent in the app (§6), then onboarding.
2. The app records phone-usage metadata only (FR-03..FR-07): app category and times, screen/unlock events, notification counts, activity state. **Never** message content, typed text, screen content, audio or precise location (PR-01, PR-04).
3. Three short check-ins a day (≤ 3 taps): mood, energy, optional context; weekly PSS-10 (Urdu or English) (RES-04 §2).
4. The app suggests du'a from a scholar-approved catalogue and asks for feedback (FR-15, FR-29).
5. Exit survey (usability, relevance), then optional deletion.

## 5. Data and privacy
| Item | Handling |
|---|---|
| Raw behavioural data | Stays on the device, encrypted (PR-02) |
| Research upload | Only with separate opt-in (PR-03): pseudonymised feature aggregates and check-in labels, over TLS 1.3 to the research store |
| Identifiers | Participant code only; the code-to-contact key is kept separately by the PI, encrypted |
| Storage | Encrypted, access-controlled store outside Git (RES-04 §4); access limited to named researchers |
| Retention | On-device 90 days rolling (NFR-03, PR-06); research store: (PI to set, e.g. until publication + N years) |
| Withdrawal | At any time in the app; uploads stop at once, and the participant may ask for deletion of uploaded data |
| Use of data | Research and model training for BCSR only (PR-07); no sale or sharing |

## 6. Consent
Layered, in Urdu and English, in the app before any sensing: purpose; exactly what is and is not collected; voluntary participation and withdrawal; risks; not a medical service (SR-02); data handling; contact for questions. Research upload is a separate opt-in for each data type (PR-03).

## 7. Risks and mitigations
| Risk | Mitigation |
|---|---|
| Privacy: sensitive behavioural data | On-device processing, metadata only, encryption, pseudonymisation, opt-in upload |
| Distress raised by check-ins about feelings | Safety pathway (SAF-01): explicit "I need help now" with verified helplines (SAF-02); no diagnostic wording; participant can pause or stop |
| Crisis during the study | Safety pathway shows emergency and helpline first. **The team has no mental-health professional (D-011)**: committees may require one; see §9 |
| Religious sensitivity | Only scholar-approved content (CR-02); no AI-generated religious text; no guilt framing |
| Burden and battery | ≤ 3 taps per check-in, quiet hours and Salah times respected, battery budget ≤ 2%/day |

## 8. Benefits
No guaranteed direct benefit. Participants may find the reminders helpful. The study informs a privacy-preserving, faith-based wellbeing tool.

## 9. Points the committee is likely to raise
1. **No mental-health professional on the team (D-011).** Many committees require one for studies that ask about mood and distress. Be ready to name one, at least as an on-call contact for the pilot.
2. **Helplines:** 1166's hours are unverified (SAF-02); verify before submission.
3. **Compensation** amount and its effect on voluntariness.
4. **Data retention** period for the research store.

## 10. Attachments to prepare
Consent text (Urdu, English) · participant information sheet · screenshots of onboarding and the safety screen · data management plan (from §5) · PSS-10 permissions (RES-04 actions 1–2) · PI CV.
