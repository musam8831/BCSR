# Threat Model — ARC-05 v1.0-rc (STRIDE + LINDDUN)

Task: T-M2-007 · Owner: AG-09 (SK-09) · Reviewer: Tech Lead · Flows are the data-flow steps in ARC-03 plus the catalogue and research paths. Acceptance: every flow has threats (§2).

## 1. Threats
| ID | Threat | Category | Asset | Mitigation | Req |
|---|---|---|---|---|---|
| TH-01 | Another app or person with device access reads behavioural data | Information disclosure | Encrypted DB | SQLCipher with a Keystore-wrapped key; `allowBackup=false`; optional app lock | PR-02 |
| TH-02 | Tampered catalogue introduces false religious content | Tampering | Catalogue | Signed bundle (Ed25519), verified on load; approved-only filter twice (build and runtime) — ADR-0005 | CR-02, FR-15 |
| TH-03 | User feels surveilled; behaviour becomes linkable | Unawareness, linkability | Trust | Transparency screen of exactly what is stored; pause; delete-all | FR-25, FR-26 |
| TH-04 | Research upload re-identifies users | Identifiability | Pilot data | Categories only, time-shifting, k-anonymity checks, no device IDs; separate opt-in | PR-03 |
| TH-05 | Crisis user gets an inadequate response | Safety | User wellbeing | Safety pathway SAF-01 signed by the Project Lead (D-011); verified helplines (SAF-02); explicit trigger only | SR-01..SR-05 |
| TH-06 | Notification on the lock screen reveals emotional state | Disclosure | Privacy | Neutral lock-screen copy ("A moment of dhikr?"); `VISIBILITY_PRIVATE` notifications | PR-01 |
| TH-07 | Dependency supply-chain compromise | Tampering | Build | Pinned versions, Gradle dependency verification, Dependabot, SBOM; licence register RES-03 | NFR-09 |
| TH-08 | Notification listener reads or stores notification content | Information disclosure | Other apps' messages | Listener reads only `postTime` and package → category (spike code shows the pattern); code review and a lint rule ban `Notification.extras` access | PR-01 |
| TH-09 | Package names or usage history leak (e.g. reveal health or dating apps) | Disclosure, identifiability | Usage events | Store category plus salted package hash only; raw package names stay in the local category cache | PR-01 |
| TH-10 | Export file (FR-25) copied by another app or synced to cloud | Information disclosure | Export | Export only through the system share sheet on user action; file in app-private cache, deleted after sharing | FR-25 |
| TH-11 | Inferred "low mood" or "crisis" stored and later exposed | Disclosure, stigmatisation | State estimates, labels | Estimates are probabilities, not stated diagnoses; encrypted; never in notifications; delete-all | PR-02, SR-02 |
| TH-12 | Stale helpline numbers in a crisis | Safety | Helpline directory | 3-month review (SR-05); release blocked on stale entries | SR-05 |
| TH-13 | Research endpoint spoofed or intercepted | Spoofing, tampering | Upload | TLS 1.3 with certificate pinning; signed batches; no upload without consent | PR-03 |
| TH-14 | Catalogue signing key stolen | Spoofing | Catalogue trust | Offline key held by the Project Lead; key rotation plan; app pins the public key — ADR-0005 | CR-02 |
| TH-15 | Model trained on data without consent or with research-only terms | Non-compliance | Shipped model | PR-07: shipped model trained only on D1–D3; model card lists training data (AC-PR-07) | PR-07 |
| TH-16 | Battery optimisation gaps produce misleading features → wrong prompts | Safety / quality | Inference | Gap-aware features (FS-001), confidence threshold, check-in asks rather than asserts | FR-14 |
| TH-17 | Shared phone: another family member sees insights or check-in history | Disclosure | Privacy | Optional app lock; insights behind the lock; neutral notifications | PR-02 |

## 2. Flow coverage
| Flow (ARC-03) | Threats |
|---|---|
| F1 Collect usage, screen, activity events | TH-01, TH-03, TH-09, TH-16 |
| F1b Count notifications | TH-08 |
| F2 Featurise and baselines | TH-01, TH-16 |
| F3 Estimate state | TH-11, TH-15 |
| F4 Decide check-in | TH-06, TH-16 |
| F5 Check-in and label | TH-11, TH-17 |
| F6 Safety gate | TH-05, TH-12 |
| F7 Recommend from catalogue | TH-02 |
| F8 Feedback | TH-01 |
| F9 Nightly personalisation | TH-01, TH-15 |
| F10 Catalogue build and load (ADR-0005) | TH-02, TH-07, TH-14 |
| F11 Research upload (opt-in) | TH-04, TH-13 |
| F12 Export and delete-all | TH-10, TH-03 |
| F13 Insights screen | TH-11, TH-17 |

Every flow has at least one threat with a mitigation. Residual risk: TH-05 (no mental-health professional review, R-05 accepted).
