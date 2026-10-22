# Data Protection Impact Assessment (DPIA) — D1 / D2 and the app

Task: T-M5-018 · Owner: AG-09 · Approver: Project Lead · Date: 2026-10-21 · Inputs: ARC-03 flows, ARC-04 data dictionary, ARC-05 threats, RES-04, PR-01..PR-07, ethics draft.

Not legal advice. The applicable law (Pakistan data-protection obligations; GDPR if EU users, charter §9) must be confirmed by the Project Lead before D2.

## 1. Processing
| Purpose | Data | Lawful basis (to confirm) | Where | Retention |
|---|---|---|---|---|
| Suggest du'a at helpful moments (product) | App-usage metadata (category, times), screen/unlock, notification counts, activity state, check-in answers, feedback | Consent in onboarding | On the device only | Raw 30 d, derived 90 d, labels until deleted (ARC-04) |
| Improve models (research, D1/D2) | Pseudonymised feature aggregates and check-in labels | Separate research consent per data type (PR-03) + ethics approval | Research store (T-M5-012) | Set in the ethics application |
| Safety pathway | A local counter only | — | Device | Until delete-all |

Never processed: message content, typed text, screen content, audio, precise location (PR-01, PR-04), package names in the clear (TH-09).

## 2. Necessity and proportionality
- Each signal has a requirement and an evidence level (GOV-02 §1); signals with no G2 "Go" are not enabled (T-M5-003).
- Inference only triggers a question; the user's answer is the label (ask, don't assert).
- Users can pause sensing, export, and delete everything at any time (FR-25, FR-26).

## 3. Risks (ARC-05) and measures
| Risk | Likelihood | Severity | Measures | Residual |
|---|---|---|---|---|
| Device access exposes behavioural data (TH-01) | Low | High | SQLCipher, Keystore-wrapped key, no backup/transfer (`data_extraction_rules.xml`), delete-all removes the key | Low |
| Inferred emotional state leaks (TH-06, TH-11) | Low | High | Neutral, private notifications; estimates never shown as statements; encrypted | Low |
| Re-identification from research data (TH-04) | Medium | High | Opt-in per type; categories only; time-shifting; no device IDs; k-anonymity checks before export | Medium — review at ethics approval |
| Crisis handled badly (TH-05) | Low | Very high | Safety pathway SAF-01; verified helplines only; no MH professional (D-011) | **Medium — accepted (R-05)** |
| Stale helplines (TH-12) | Medium | High | 90-day freshness; release blocked (`releaseBlockers`) | Low |
| Third-party code (TH-07) | Low | High | Licence register, pinned versions and actions, wrapper validation, SBOM per build | Low |

## 4. Conclusion
Processing can proceed for **D1** once the safety helpline is fully verified (SAF-02) and consent text is final. **D2** additionally needs ethics approval (T-M1-012) and the research store with its own review (T-M5-012, T-M5-019). Revisit this DPIA when sensing (T-M5-003) is enabled.

## Sign-off
| Role | Name | Date | Decision |
|---|---|---|---|
| Project Lead | | | Pending |
