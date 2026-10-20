# BCSR — Behavioral & Contextual Stress Releaser

> "Those who believe, and whose hearts find rest in the remembrance of Allah. Truly, it is in the remembrance of Allah that hearts find rest." — Qur'an, Surah Ar-Ra'd 13:28
>
> *Allah ney apney zikr mein sukoon rakha hai.*

BCSR is an Android mobile application that observes the user's **context**, **behaviour** and likely **feelings** from on-device phone-usage signals. It uses classical machine learning and deep learning that run on the device, with **no paid LLM services**. When a state such as worry, restlessness, sadness, anger or late-night sleeplessness is likely, it gently checks in with the user and recommends **verified** supplications (du'a) and adhkar from the **Qur'an and Sahih Hadith**, matched to that context.

## Founding principles

1. **Remembrance as the remedy.** The app's purpose is to bring the user back to dhikr. The AI only chooses *when* and *which* dhikr.
2. **Verified content only.** No Qur'anic or Hadith text is ever generated, paraphrased or translated by AI. Every item comes from a scholar-verified catalogue with full references (see `docs/05-spiritual-content/`).
3. **Ask, don't assert.** Inferences from phone usage are probabilistic. The app offers ("Feeling restless? Here is a du'a for ease") and never diagnoses.
4. **Privacy by default.** Raw behavioural data stays on the device. Training data leaves the device only with explicit, revocable, informed consent.
5. **Not a medical device.** The app has a clear crisis-escalation path to human help.
6. **Documentation governs code.** Nothing is built without an approved requirement, a design, and an acceptance criterion traced in `docs/01-requirements/traceability-matrix.md`.

## Repository map

| Path | Purpose |
|---|---|
| `AGENTS.md` | Operating rules for every AI agent working in this repo. **Read first.** |
| `docs/00-governance` | Charter, scope, risks, decisions |
| `docs/01-requirements` | SRS, requirements catalogue, personas, stories, acceptance criteria, traceability |
| `docs/02-research` | Platform feasibility, scientific evidence, open-source AI evaluation, dataset strategy |
| `docs/03-architecture` | System context, components, data flow, data dictionary, threat model, API contracts, ADRs |
| `docs/04-ai-ml` | Feature specs, model cards, evaluation plans, experiment registry |
| `docs/05-spiritual-content` | Content schema, source policy, verification workflow, scholar review records |
| `docs/06`–`09` | Security/privacy/safety, UX, testing, release (populated from M2 onward) |
| `docs/10-project-management` | Milestones, task register, dependencies, weekly reports |
| `docs/11-agent-system` | Agent registry, orchestration (AGS-02), collaboration matrix (AGS-04), tooling efficiency (AGS-05), handoff contracts, expert skills |
| `.cursor/rules` | Lean Cursor rules (docs-first, android, ml, content-safety, gitnexus) |
| `app/` | Android app (Kotlin) and optional backend (from M5) |
| `ml/` | Datasets (never raw PII), experiments, training, evaluation |
| `content/` | Machine-readable du'a/adhkar catalogue + validators |

## Current status

**Phase: M0 — Inception.** See `docs/10-project-management/milestones.md` and `task-register.md`.
