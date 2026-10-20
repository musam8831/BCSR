# Milestones — PM-01 v0.1

Indicative plan starting Monday 2026-10-19. Durations are estimates for a small team augmented by agents; AG-00 re-baselines them at each gate.

| MS | Name | Weeks | Indicative dates | Exit gate |
|---|---|---|---|---|
| **M0** | Inception & Governance | 2 | 19 Oct – 30 Oct 2026 | G0 |
| **M1** | Requirements & Research | 4 | 2 Nov – 27 Nov 2026 | G1 |
| **M2** | Architecture & Feasibility | 4 | 30 Nov – 25 Dec 2026 | G2 |
| **M3** | Agent Workforce & Expert Skills | 3 | 28 Dec 2026 – 15 Jan 2027 | G3 |
| M4 | Content Catalogue v1 (scholar-verified) | 4 (parallel from M2) | — | Content baseline |
| M5 | Sensing & Data Collection App + Pilot Study | 6 | — | Data quality review |
| M6 | Model Development & Evaluation | 5 | — | Model ship criteria (EP-001) |
| M7 | Recommendation Engine & Intervention Policy | 3 | — | Offline replay + safety review |
| M8 | UX Integration, Localisation, Accessibility | 4 | — | Usability review |
| M9 | System Testing, Privacy Audit, Closed Pilot | 4 | — | Pilot exit report |
| M10 | Release Readiness & Launch | 2 | — | G-Release |

**Principle:** M0–M3 produce the controlled foundation and the agent workforce. Implementation (M4+) is executed *by those agents*, task by task, through the register.

---

## M0 — Inception & Governance
**Goal:** a controlled project start.
**Deliverables:** charter, scope & assumptions, risk register, decision log, repository and conventions, agent rules (AGENTS.md), stakeholder confirmation (scholar, MH advisor), tooling choices.
**Agents:** AG-00, AG-01, AG-16 (bootstrap versions of their skills; first job of SK-00).
**Exit criteria (G0):** charter signed; scholar reviewer and MH advisor confirmed (or a dated plan to recruit them); repo live with branch protection; risk register reviewed; M1 plan Ready.

## M1 — Requirements & Research
**Goal:** a frozen, testable requirements baseline grounded in evidence.
**Deliverables:** SRS v1.0, personas validated by ≥ 8 interviews, user stories, acceptance criteria for all Musts, traceability matrix, context-state taxonomy v1, scientific evidence brief, open-source & licence register, dataset strategy, content source policy (scholar-approved), safety pathway v1 (advisor-approved), ethics/IRB application submitted.
**Agents:** AG-02, AG-04, AG-07, AG-08, AG-10, AG-12 (interviews), AG-16.
**Exit criteria (G1):** SRS baselined; 100% of Musts have ACs; source policy and safety pathway approved by the human reviewers; no open blocker risks.

## M2 — Architecture & Feasibility
**Goal:** proven feasibility and an approved architecture.
**Deliverables:** spike reports (usage events, screen/unlock, notifications, activity, background/OEM, battery), Play policy review, ADR-0001..0004 accepted plus new ADRs, C4 diagrams, data dictionary v1, threat model v1, API contracts v1, feature spec FS-001 v1, evaluation plan EP-001 v1, baseline experiment on public data, UX flows (low-fi), test strategy, catalogue pipeline design.
**Agents:** AG-03, AG-05, AG-06, AG-07, AG-09, AG-11, AG-12, AG-14, AG-16.
**Exit criteria (G2):** go/no-go on each sensing signal; NFR budgets shown to be feasible; threat model reviewed; the architecture is approved by the Tech Lead.

## M3 — Agent Workforce & Expert Skills
**Goal:** a validated agent workforce that can implement M4–M10 without inventing requirements.
**Deliverables:**
1. Agent definitions finalised for AG-00..AG-16 (`docs/11-agent-system/agents/`).
2. Expert skills SK-00..SK-16 completed, each with procedures, checklists, references and evals.
3. Skill references packs (e.g., SK-11: Kotlin conventions and module templates; SK-06: feature-spec → code templates; SK-08: schema validator usage).
4. Orchestration: AGS-02 v0.2 + AGS-04 collaboration matrix (Mode/O/C/G); mode-aware HC-01 task-packet template; PR template with DoD; CI checks (schema, links, traceability IDs); T-M3-012 GitNexus + lean Cursor rules (AGS-05).
5. **Pilot run:** each implementation agent completes one small real task from M4/M5 (dry run) and is audited by AG-16.
6. Skill revisions from the pilot retrospective; v1.0 of every skill.
7. M4–M10 backlog decomposed into Ready-able tasks with agents assigned.
**Exit criteria (G3):** all skill evals pass, including the trap cases; pilot tasks approved by AG-16 and the Tech Lead; CI enforces task IDs and the content schema; M4 and M5 backlogs Ready.
