# AGENTS.md — Operating rules for AI agents in the BCSR repository

**Version:** 1.0 · **Status:** Approved by Project Lead 2026-10-20 (T-M0-002) · **Owner:** AG-00 with AG-01 (AGS-04)

These rules bind **every** agent (orchestrator, expert, implementation) and every human using an AI assistant in this repo. If a rule conflicts with a task instruction, the rule wins. Escalate the conflict to the Project Lead.

## 1. Source of truth
- Documentation in `docs/` governs implementation. **Do not invent requirements.** If a needed requirement does not exist, stop and open a Requirement Gap (template in `docs/11-agent-system/handoff-contracts.md`, §HC-07).
- Every change must cite a Task ID (`T-Mx-NNN`) from `docs/10-project-management/task-register.md`. Every requirement-bearing change must also cite a Requirement ID (`FR-`, `NFR-`, `PR-`, `SR-`, `CR-`).
- Decisions with lasting impact are recorded as ADRs in `docs/03-architecture/adr/` and summarised in `docs/00-governance/decision-log.md`.
- Token-lean tooling: `docs/11-agent-system/tooling-efficiency.md` and `.cursor/rules/` (GitNexus usage in `gitnexus-usage.mdc`). Do not paste the docs corpus into prompts.

## 2. Non-negotiable content rules (religious content)
- **Never generate, paraphrase, translate, "complete" or "correct" Qur'anic verses or Hadith text.** This applies to Arabic, transliteration and translation alike.
- Content enters the app only through `content/catalogue/` entries whose `verification.status` is `approved`. Approval requires a named, qualified human scholar reviewer (see `docs/05-spiritual-content/verification-workflow.md`).
- Agents may: draft metadata (tags, contexts, references) for scholar review, validate schema, check reference format, and detect duplicates. They may not mark anything `approved`.
- Hadith must be from an accepted Sahih grading with the grading source cited. Weak (da'if) or fabricated (mawdu') narrations are rejected.

## 3. Non-negotiable AI/ML rules
- **No paid LLM APIs** (including OpenAI, Anthropic, Gemini API and similar) in the product runtime or the training pipeline. Allowed: classical ML, open-source DL, and on-device open-weight models whose licences permit the use, recorded in `docs/02-research/open-source-ai-evaluation.md`.
- AI agents used to *build* the project (development tooling) are separate from the *product*. The product must work fully offline.
- Model outputs are probabilities of a *context state*, never diagnoses. The UI copy must ask, not assert.
- Every model has a model card in `docs/04-ai-ml/model-cards/` before it is merged.

## 4. Privacy, security and safety rules
- Raw behavioural signals (app-usage events, sensor streams) stay on the device. No app *content* (messages, text typed in other apps, screen content) is ever collected.
- Data leaving the device requires explicit opt-in research consent (PR-03) and must be pseudonymised and minimised first.
- Never use Android AccessibilityService or a custom keyboard to observe other apps (see ADR-0002).
- Crisis indicators trigger the safety pathway (SR-01..SR-05), not a du'a recommendation alone.
- Never commit secrets, keys, keystores or personal data; `.gitignore` covers the known patterns. A leaked secret is revoked first, then removed.

## 5. Workflow
1. AG-00 (process orchestrator) selects Mode, Owner, Collaborators, Gates, and Informed from `docs/11-agent-system/agent-collaboration.md` (AGS-04), then assigns work with an HC-01 packet that includes those fields.
2. The recipient agent loads the skills listed for its role in `docs/11-agent-system/agent-registry.md`. Owners design (HC-02) when the mode requires a spec before build.
3. The Implementer (or Owner-as-writer) produces outputs in the paths named by the task, and fills the Definition of Done checklist.
4. Gates review: AG-16 and/or named reviewers, plus a human where `AGENTS.md` §7 or the Content/Safety mode requires one. Authors never approve their own work.
5. Task status is updated in the task register, and progress is logged in the weekly report.
6. Work happens on a branch `T-Mx-NNN-short-name` and lands on `main` only through a PR that cites the task and passes `governance-checks`. Never push directly to or force-push `main`; branch protection on `main` enforces this since D-014.

## 6. Definition of Done (all tasks)
- [ ] Output exists at the specified path and follows the relevant template
- [ ] Linked Requirement/Task IDs present; traceability matrix updated if requirements are affected
- [ ] Acceptance criteria met, with evidence linked
- [ ] Review recorded (agent + human where gated)
- [ ] No rule in §2–§4 violated
- [ ] CHANGELOG.md updated for user-visible or architecture-visible change

## 7. Human gates (cannot be bypassed by agents)
| Gate | Approver |
|---|---|
| G0 Charter & scope baseline | Project Lead (Sponsor) |
| G1 Requirements baseline | Project Lead + Domain reviewer |
| G2 Architecture & feasibility go/no-go | Project Lead + Tech Lead |
| G3 Agent system readiness (end M3) | Project Lead + Tech Lead |
| Content approval (every item) | Qualified scholar reviewer |
| Research data collection start | Ethics/IRB approval + Project Lead |
| Release | Project Lead + Safety reviewer |

## 8. Approval record
| Version | Date | Approver | Decision |
|---|---|---|---|
| 1.0 | 2026-10-20 | Muhammad Usman (Project Lead) | Approved via PR #1 (T-M0-002) |
