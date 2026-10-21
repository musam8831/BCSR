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
| D-010 | 2026-10-21 | Content follows the Ahl-e-Hadith manhaj: du'a, adhkar and verses only from the Qur'an and Sahih Hadith, chosen for their context | Project Lead direction. Reference authorities: Ahl-e-Hadith ulama named by the Project Lead (e.g. Syed Talibur Rehman Shah, Tayyab-ur-Rehman Shah, Abdullah Nasir Rehmani, Hafiz Muhammad Salfi; Abdul Rehman Makki, late, by his works). Naming them as authorities does not make them reviewers: every item still needs a named living reviewer who agreed (AGENTS.md §2, T-M0-006) | Accepted (Project Lead) | ADR-0004 |
| D-011 | 2026-10-21 | No mental-health advisor. The safety pathway (SR-01..SR-05, AGENTS.md §4) stays; wherever docs name the "MH advisor" as approver, the Project Lead approves instead | Project Lead decision after being told crisis copy and helplines then get no professional review; risk R-05 raised to 15 and accepted | Accepted (Project Lead) | — |
| D-012 | 2026-10-21 | M2 runs in parallel with G1: M2 desk work and the spike app start before G1 is decided; G1 must still pass before G2 is decided, and any G1 change is reworked into M2 outputs | The scholar (G1 domain reviewer) is not confirmed until 2026-11-15; waiting would idle M2 for weeks. Risk R-18 | Accepted (Project Lead) | — |
| D-013 | 2026-10-21 | When an agent co-gate (e.g. AG-09, AG-07) was not run, the Tech Lead may take that gate; each substitution is recorded in the task's gate record with name, date and what was checked | T-M3-006 pilot: AG-16 required the substitution to be recorded by AG-00 and evidenced, not just planned. Weakens separation of duties further (R-17) | Accepted (AG-00 process; confirmed by Tech Lead 2026-10-21) | — |
