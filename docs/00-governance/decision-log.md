# Decision Log — GOV-04 v1.0

Task: T-M0-007 · Last reviewed: 2026-10-20 (AG-01; approved by Project Lead via PR #4). Proposed decisions D-002 and D-003 are confirmed at G2; D-008 confirmed by Tech Lead 2026-10-21.

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
| D-008 | 2026-10-20 | Keep `musam8831/BCSR` private on GitHub Free; `main` branch protection deferred (T-M0-001 deviation) | Privacy of governance/research docs outweighs enforced protection; Free plan blocks protection on private repos (HTTP 403). Risk R-14 | Accepted (Project Lead 2026-10-20; Tech Lead 2026-10-21) with compensating controls: Claude Code guard hook + deny rules (`.claude/settings.json`, `scripts/hooks/guard_git.py`), git pre-push hook (`.githooks/`, enable with `git config core.hooksPath .githooks`), CI audit of every `main` push. **Expires: revisit before M4 starts (G3).** | — |
| D-009 | 2026-10-21 | Muhammad Usman holds both the Project Lead and Tech Lead roles | Small team; no separate Tech Lead available. Separation of duties at G2/G3 and Tech Lead reviews is therefore absent (R-17) | Accepted (Project Lead / Tech Lead) | — |
