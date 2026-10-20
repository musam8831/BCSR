# Agent Registry — AGS-01 v0.2

The BCSR agent workforce has **one process orchestrator** (AG-00), **expert agents** (domain Owners who define and review), **implementation agents** (who build under Owner design), and **independent control** (AG-16). Collaboration roles (Owner / Collaborator / Gate / Implementer / Informed) and path ownership are defined in [`agent-collaboration.md`](agent-collaboration.md) (AGS-04). Agents are tool-agnostic role definitions: they can be run with any coding-agent tool that reads `AGENTS.md` and skill folders (`SKILL.md` with YAML frontmatter). The product runtime never uses these agents (ADR-0003).

Every agent: reads `AGENTS.md` → loads its skills → works only on assigned HC-01 tasks (Mode/O/C/G from AGS-04) → hands off via the contracts in `handoff-contracts.md`.

| ID | Agent | Tier | Mission | Primary skills | Writes to | Reviewed by | Active milestones |
|---|---|---|---|---|---|---|---|
| AG-00 | Process Orchestrator / Program Manager | Control | Route/sequence via AGS-04; own register; enforce gates; never builds; never owns domain correctness | SK-00, SK-01, SK-16 | `docs/10-project-management/` | Project Lead | M0–M10 |
| AG-01 | SDLC Governance Lead | Expert | Charter, scope, risks, decisions, gate packs | SK-01 | `docs/00-governance/` | Project Lead | M0–M10 |
| AG-02 | Requirements Engineer | Expert | SRS, stories, acceptance criteria, traceability | SK-02, SK-16 | `docs/01-requirements/` | AG-16, Project Lead | M1, change requests |
| AG-03 | Solution Architect | Expert | C4 views, ADRs, API contracts, module boundaries | SK-03 | `docs/03-architecture/` | Tech Lead | M2, reviews later |
| AG-04 | Research Analyst | Expert | Evidence briefs, open-source and licence evaluation | SK-04 | `docs/02-research/` | AG-07, Tech Lead | M1–M2 |
| AG-05 | Android Platform Feasibility Engineer | Expert | Spikes on sensing APIs, OEM behaviour, Play policy | SK-05, SK-11 | `docs/02-research/`, `app/android/spikes/` | AG-03 | M2 |
| AG-06 | ML Engineer | Implementation | Feature engine, models, on-device runtime, bandit | SK-06 | `ml/`, `docs/04-ai-ml/` | AG-07 | M2, M5–M7 |
| AG-07 | Data & Evaluation Scientist | Expert | Dataset strategy, evaluation plans, model cards, bias checks | SK-07 | `docs/04-ai-ml/`, `ml/evaluation/` | Tech Lead | M1–M7 |
| AG-08 | Islamic Content Curator | Expert (human-gated) | Draft metadata, validate schema, prepare review packs | SK-08 | `content/`, `docs/05-spiritual-content/` | **Scholar (mandatory)** | M1, M4 |
| AG-09 | Privacy & Security Engineer | Expert | Threat model, data minimisation, encryption, Play Data Safety | SK-09 | `docs/06-security-privacy-safety/`, threat-model | Tech Lead | M2–M10 |
| AG-10 | Safety & Wellbeing Reviewer | Expert (human-gated) | Safety pathway, copy tone, helpline directory | SK-10 | `docs/06-security-privacy-safety/safety/` | **MH advisor (mandatory)** | M1–M9 |
| AG-11 | Android Developer | Implementation | Kotlin/Compose app modules | SK-11 | `app/android/` | AG-03, AG-14 | M2 spikes, M5–M8 |
| AG-12 | UX Designer | Expert | Flows, wireframes, microcopy (Urdu/English/Arabic RTL), accessibility | SK-12 | `docs/07-ux/` | AG-10, Project Lead | M2, M8 |
| AG-13 | Backend Developer | Implementation | Optional research ingestion, catalogue signing pipeline | SK-13 | `app/backend/`, `scripts/` | AG-09 | M4–M5 |
| AG-14 | QA & Test Engineer | Implementation | Test strategy, test cases, automation, device matrix | SK-14 | `docs/08-testing/`, `tests/` | AG-16 | M2–M10 |
| AG-15 | DevOps & Release Engineer | Implementation | CI/CD, signing, SBOM, Play release tracks | SK-15 | `.github/workflows/`, `docs/09-release-operations/` | AG-09 | M3–M10 |
| AG-16 | Traceability & Quality Auditor | Control (Gate) | Independent review that outputs match requirements and DoD | SK-16 | review comments, traceability matrix | Project Lead | M0–M10 |

## Agent definition template (each agent gets one file in `agents/AG-xx.md` during T-M3-002)
```yaml
id: AG-02
name: Requirements Engineer
collab_role: Owner   # Owner | Collaborator | Gate | Implementer | Control
mission: >
  Produce a complete, testable, traceable requirements baseline.
skills: [SK-02, SK-16]
inputs: [GOV-01, GOV-02, personas interviews, research briefs]
outputs: [SRS.md, user-stories.md, acceptance-criteria.md, traceability-matrix.md]
may_write: [docs/01-requirements/**]
must_not: [invent scope beyond charter, change approved baseline without CR]
handoff_in: HC-01
handoff_out: HC-02
reviewers: [AG-16, Project Lead]
escalate_to: AG-00
```

Path → Owner / C / G rows: see AGS-04. Orchestration rules: see AGS-02 v0.2.
