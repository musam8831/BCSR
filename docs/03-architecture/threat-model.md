# Threat Model — ARC-05 v0.1 (STRIDE + LINDDUN, scaffold)

Owner: AG-09 Privacy & Security. Completed in T-M2-007.

| ID | Threat | Category | Asset | Mitigation | Req |
|---|---|---|---|---|---|
| TH-01 | Another app or person with device access reads behavioural data | Information disclosure | Encrypted DB | SQLCipher with a Keystore-wrapped key; `allowBackup=false`; app lock option | PR-02 |
| TH-02 | Tampered catalogue introduces false religious content | Tampering | Catalogue | Signed bundle (Ed25519), verified on load; approved-only filter | CR-02, FR-15 |
| TH-03 | User feels surveilled / linkability of behaviour | Unawareness, linkability | Trust | Transparency screen showing exactly what is stored; pause; delete-all | FR-25, FR-26 |
| TH-04 | Research upload re-identifies users | Identifiability | Pilot data | Categories only, time-shifting, k-anonymity checks, no device IDs | PR-03 |
| TH-05 | Crisis user gets inadequate response | Safety | User wellbeing | SafetyGuard pathway, advisor-approved copy, verified helplines | SR-01..05 |
| TH-06 | Notification on the lock screen reveals emotional state | Disclosure | Privacy | Neutral lock-screen copy ("A moment of dhikr?") | PR-* |
| TH-07 | Dependency supply-chain compromise | Tampering | Build | Dependency pinning, verification metadata, Dependabot, SBOM | NFR-09 |

## Safety pathway (summary)
Trigger: explicit crisis selection or "I need help now" → full-screen calm page → verified local helplines + emergency dial + "talk to someone you trust" → optional du'a for distress shown *below* the help options. No passive-only trigger (SR-03).
