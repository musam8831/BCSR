# Decision Log — GOV-04

Short index of decisions. Architectural decisions link to full ADRs in `docs/03-architecture/adr/`.

| ID | Date | Decision | Rationale | Status | ADR |
|---|---|---|---|---|---|
| D-001 | 2026-10-15 | Documentation-first repository is the single source of truth | Prevents requirement invention by agents | Accepted | — |
| D-002 | 2026-10-15 | Android-first MVP; iOS deferred | iOS does not expose cross-app usage data to third-party apps in a usable form | Proposed (confirm at G2) | ADR-0001 |
| D-003 | 2026-10-15 | No AccessibilityService or custom keyboard for observation | Play policy restricts these; privacy intrusion; content capture risk | Proposed | ADR-0002 |
| D-004 | 2026-10-15 | On-device inference by default; no paid LLMs | Privacy, cost, offline use | Accepted | ADR-0003 |
| D-005 | 2026-10-15 | Religious content is curated and verified, never generated | Accuracy and trust are paramount | Accepted | ADR-0004 |
| D-006 | 2026-10-15 | Agent workforce defined and validated (M3) before implementation (M4+) | User directive; controlled quality | Accepted | — |
| D-007 | 2026-10-20 | Governed Owner–Collaborator–Gate orchestration (AGS-02 v0.2 + AGS-04); AG-00 process authority only | Hub-and-spoke bottlenecked M4+; domain Owners already own write paths; human gates preserved | Accepted | — |
| D-008 | 2026-10-20 | Keep `musam8831/BCSR` private on GitHub Free; `main` branch protection deferred (T-M0-001 deviation) | Privacy of governance/research docs outweighs enforced protection; Free plan blocks protection on private repos (HTTP 403). Risk R-14 | Accepted (Project Lead); Tech Lead to confirm | — |
