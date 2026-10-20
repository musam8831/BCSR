# T-M3-011 recovery: restore SKILL.md files wiped by encoding fix
from pathlib import Path

base = Path(__file__).resolve().parents[1] / "docs" / "11-agent-system" / "skills"

ALWAYS = """## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product."""

skills: dict[str, str] = {}

skills["SK-00-agent-skill-authoring"] = f"""---
name: agent-skill-authoring
description: Create or revise BCSR agent definitions and SKILL.md expert skills; use when M3 tasks ask for a new agent, a new skill, or a skill improvement after a retrospective.
---

# SK-00 · agent-skill-authoring

**Version:** 0.1 · **Status:** Draft (validated in T-M3-006)

## Purpose
Design agents and skills that are narrow, testable and safe.

## Inputs
HC-01 task packet; the documents it lists; `AGENTS.md`.

## Procedure
1. Read agent-registry.md, orchestration-policy.md (AGS-02), agent-collaboration.md (AGS-04) and the target milestone tasks.
2. Define the agent: mission, collab_role (Owner/Collaborator/Gate/Implementer/Control), inputs, outputs, may_write paths, must_not list, reviewers, handoffs (use the registry template). Align path ownership with the AGS-04 matrix.
3. Write SKILL.md: YAML frontmatter (`name`, `description` saying *when* to use it), then Purpose, Inputs, Procedure (numbered), Outputs, Quality checklist, Prohibitions, References.
4. Keep each SKILL.md under ~200 lines; move long reference material into `references/` files inside the skill folder.
5. Write 3 evaluation scenarios in `evals.md`: a normal task, an edge case, and a rule-violation trap (e.g., a request to 'improve the translation of a hadith'). State the expected behaviour for each.
6. Dry-run the skill on its pilot task (T-M3-006) and record results; revise until all evals pass.
7. Register the skill version in the registry and CHANGELOG.

## Outputs
- `SKILL.md`
- `evals.md`
- `updated agent-registry.md`

## Quality checklist
- [ ] Description states trigger conditions
- [ ] Procedure is executable without hidden knowledge
- [ ] Prohibitions include the AGENTS.md §2–§4 rules relevant to the role
- [ ] Evals include a trap case and it passes

{ALWAYS}

## References
`AGENTS.md` · `docs/11-agent-system/agent-registry.md` · `docs/11-agent-system/agent-collaboration.md` · `docs/11-agent-system/orchestration-policy.md` · `docs/11-agent-system/handoff-contracts.md`
"""

skills["SK-01-sdlc-governance"] = f"""---
name: sdlc-governance
description: Produce and maintain project governance artefacts (charter, scope, risks, decisions, gate packs); use for any M0 work or gate preparation.
---

# SK-01 · sdlc-governance

**Version:** 0.1 · **Status:** Draft (validated in T-M3-006)

## Purpose
Keep the project controlled: clear scope, visible risks, recorded decisions, gate-ready evidence.

## Inputs
HC-01 task packet; the documents it lists; `AGENTS.md`.

## Procedure
1. When acting as process support for AG-00: read AGS-04 first; select Mode/Owner/C/G with minimum-sufficient experts; record considered-but-not-needed always-on items; emit HC-01 with those fields.
2. Draft or update charter/scope from Project Lead input; mark every unknown as an assumption with a validating task.
3. Maintain the risk register weekly: rescore L×I, add new risks from task reports, and escalate scores ≥ 15.
4. Record decisions in decision-log.md; create an ADR stub for architectural ones and hand it to AG-03.
5. Before each gate, assemble a gate pack: exit-criteria checklist, evidence links, open risks, recommendation (go / no-go / conditional).
6. Run a short retrospective at each milestone end; feed skill improvements to SK-00.

## Outputs
- `docs/00-governance/*`
- `gate packs in docs/10-project-management/gates/`

## Quality checklist
- [ ] Every objective is measurable
- [ ] Every risk has an owner and a mitigation
- [ ] Gate pack links evidence, not claims

{ALWAYS}

## References
`AGENTS.md` · `docs/11-agent-system/handoff-contracts.md` · `docs/11-agent-system/agent-collaboration.md` · `docs/11-agent-system/orchestration-policy.md` · `docs/01-requirements/SRS.md`
"""

DOMAIN = [
    (
        "SK-02-requirements-engineering",
        "requirements-engineering",
        "Produce and maintain SRS, user stories, acceptance criteria and the traceability matrix; use for M1 baseline and any change request.",
        "Produce a complete, testable, traceable requirements baseline within charter scope.",
        [
            "Read charter, scope, personas and research briefs named in the HC-01 packet.",
            "Draft or update SRS sections; every Must/Should gets a Requirement ID (FR-/NFR-/PR-/SR-/CR-).",
            "Write user stories and acceptance criteria; link each Must to ACs in the traceability matrix.",
            "Open HC-07 for gaps; never invent scope beyond the charter.",
            "Submit via HC-03 for AG-16 / Project Lead review.",
        ],
        "- `docs/01-requirements/SRS.md`\n- `user-stories.md`, `acceptance-criteria.md`, `traceability-matrix.md`",
        "- [ ] Every Must has an AC\n- [ ] No requirement outside charter scope\n- [ ] Religious-content requirements reference CR-*",
        "`AGENTS.md` · `docs/11-agent-system/handoff-contracts.md` · `docs/01-requirements/SRS.md`",
    ),
    (
        "SK-03-solution-architecture",
        "solution-architecture",
        "Produce C4 views, ADRs, API contracts and module boundaries; use for M2 architecture work and boundary disputes.",
        "Keep the architecture coherent, documented and feasible against NFRs and platform constraints.",
        [
            "Read requirements and research spikes named in HC-01.",
            "Update C4 / component / data-flow views; keep paths aligned with AGS-04 Owners.",
            "Draft ADRs for lasting decisions; link from decision-log.md.",
            "Define API contracts and data dictionary entries as needed.",
            "Resolve Owner boundary disputes; fail-closed on privacy/safety constraints (ADR-0002, ADR-0003).",
        ],
        "- `docs/03-architecture/*`\n- ADR files under `docs/03-architecture/adr/`",
        "- [ ] Every ADR has status and consequences\n- [ ] NFR budgets are addressed\n- [ ] No AccessibilityService / paid-LLM product paths",
        "`AGENTS.md` · `docs/03-architecture/` · `docs/11-agent-system/agent-collaboration.md`",
    ),
    (
        "SK-04-research-synthesis",
        "research-synthesis",
        "Synthesise scientific evidence, open-source AI and licence evaluation; use for M1–M2 research briefs.",
        "Ground product choices in cited evidence and compatible licences.",
        [
            "Gather sources listed in HC-01; prefer peer-reviewed and primary docs.",
            "Write evidence briefs with citations; separate claim from inference.",
            "Maintain open-source AI evaluation and licence notes; flag conflicts (R-10).",
            "Hand ML-relevant findings to AG-07; architecture implications to AG-03.",
        ],
        "- `docs/02-research/scientific-evidence.md`\n- `docs/02-research/open-source-ai-evaluation.md`",
        "- [ ] Every claim has a citation\n- [ ] Licence conflicts are explicit\n- [ ] No invented study results",
        "`AGENTS.md` · `docs/02-research/`",
    ),
    (
        "SK-05-android-platform-feasibility",
        "android-platform-feasibility",
        "Run and document Android sensing / OEM / Play-policy spikes; use for M2 feasibility tasks.",
        "Prove or refute sensing signals and Play policy constraints with spike evidence.",
        [
            "Scope the spike to the signal or policy question in HC-01.",
            "Implement spike code only under `app/android/spikes/` unless packet says otherwise.",
            "Record OEM variance, permission UX, battery impact and Play-policy notes.",
            "Recommend go / no-go / conditional with evidence for G2.",
        ],
        "- Spike reports under `docs/02-research/`\n- Spike code under `app/android/spikes/`",
        "- [ ] Device/API versions recorded\n- [ ] No AccessibilityService or IME observation\n- [ ] Battery/permission impact noted",
        "`AGENTS.md` · ADR-0001 · ADR-0002 · `docs/02-research/platform-feasibility.md`",
    ),
    (
        "SK-06-ml-modeling",
        "ml-modeling",
        "Build on-device feature pipelines, models and bandit/runtime code; use for M5–M7 ML implementation.",
        "Deliver classical/open-weight on-device models with honest uncertainty — never diagnoses.",
        [
            "Read FS-001 / model card stubs and evaluation plan named in HC-01.",
            "Implement features and training under `ml/`; no paid LLM APIs in product or training pipeline.",
            "Keep raw behavioural signals on-device; document offline assumptions.",
            "Produce model card draft; hand evaluation to AG-07.",
        ],
        "- `ml/` artefacts\n- Model-facing notes in `docs/04-ai-ml/`",
        "- [ ] Model card path exists\n- [ ] No paid LLM dependency\n- [ ] UI-facing copy remains ask-not-assert",
        "`AGENTS.md` · ADR-0003 · `docs/04-ai-ml/`",
    ),
    (
        "SK-07-ml-evaluation",
        "ml-evaluation",
        "Define dataset strategy, evaluation plans, model cards and bias checks; use for M1–M7 evaluation work.",
        "Ensure models are evaluated with clear metrics, subgroups and honest limitations.",
        [
            "Align EP-001 and dataset strategy with requirements and ethics constraints.",
            "Define metrics, splits, baselines and subgroup checks.",
            "Review or co-author model cards before merge.",
            "Block ship language that asserts diagnoses or overclaims accuracy.",
        ],
        "- `docs/04-ai-ml/evaluation-plans/`\n- `docs/04-ai-ml/model-cards/`\n- `ml/evaluation/`",
        "- [ ] Metrics match acceptance criteria\n- [ ] Subgroup/bias checks listed\n- [ ] Limitations are explicit",
        "`AGENTS.md` · `docs/04-ai-ml/` · `docs/02-research/dataset-strategy.md`",
    ),
    (
        "SK-08-islamic-content-curation",
        "islamic-content-curation",
        "Draft catalogue metadata, validate schema and prepare scholar review packs; never generate religious text.",
        "Curate verified Qur'an/Hadith catalogue entries for scholar approval only.",
        [
            "Validate items against `content/schema` and source policy.",
            "Draft metadata/tags/contexts only; never generate, paraphrase, translate or complete verse/Hadith text.",
            "Prepare HC-06 review pack for the Scholar; leave `verification.status` for the human.",
            "Reject da'if or mawdu' narrations; require Sahih grading with source cited.",
        ],
        "- `content/catalogue/` metadata\n- HC-06 packs under `docs/05-spiritual-content/review-records/`",
        "- [ ] Schema valid\n- [ ] No agent-authored scripture text\n- [ ] Scholar gate not bypassed",
        "`AGENTS.md` §2 · ADR-0004 · `docs/05-spiritual-content/`",
    ),
    (
        "SK-09-privacy-security-engineering",
        "privacy-security-engineering",
        "Maintain threat model, data minimisation, encryption and Play Data Safety; use for M2–M10 security work.",
        "Keep privacy-by-default and security fail-closed across architecture and release.",
        [
            "Update threat model and data dictionary for the change in HC-01.",
            "Enforce on-device raw signals; no other-app content collection.",
            "Review authz, storage, consent (PR-03) and Play Data Safety impacts.",
            "Gate Done when privacy/security risks are unmitigated.",
        ],
        "- `docs/06-security-privacy-safety/`\n- Threat model updates",
        "- [ ] Threats mapped to mitigations\n- [ ] No AccessibilityService/IME observation\n- [ ] Consent path clear when data leaves device",
        "`AGENTS.md` §4 · ADR-0002 · `docs/03-architecture/threat-model.md`",
    ),
    (
        "SK-10-safety-wellbeing-review",
        "safety-wellbeing-review",
        "Maintain safety pathway, check-in tone and helpline directory; prepare MH advisor review packs.",
        "Ensure crisis indicators trigger human-help pathways, not du'a-only responses.",
        [
            "Review UX copy and flows for SR-01..SR-05 compliance.",
            "Update helpline directory with verified regional sources.",
            "Prepare HC-06 for MH advisor when safety content changes.",
            "Fail-closed: block ship if crisis path is missing or soft-pedalled.",
        ],
        "- `docs/06-security-privacy-safety/safety/`\n- HC-06 packs",
        "- [ ] Crisis path present\n- [ ] Tone asks, does not diagnose\n- [ ] MH advisor gate not bypassed",
        "`AGENTS.md` §4 · `docs/06-security-privacy-safety/`",
    ),
    (
        "SK-11-android-development",
        "android-development",
        "Implement Kotlin/Compose Android modules under approved specs; use for M5–M8 app work.",
        "Build Android features that meet ACs, NFRs and privacy constraints.",
        [
            "Confirm HC-02/spec and Mode/O/C/G on HC-01 before coding.",
            "Implement under `app/android/`; respect single-writer and architecture boundaries.",
            "No AccessibilityService or custom keyboard for observation; no paid LLM in product.",
            "Add/adjust tests with AG-14; submit HC-03 for review.",
        ],
        "- `app/android/` changes\n- Linked tests as required",
        "- [ ] ACs evidenced\n- [ ] Battery/permission budgets considered\n- [ ] No prohibited observation APIs",
        "`AGENTS.md` · ADR-0001 · ADR-0002 · `docs/03-architecture/`",
    ),
    (
        "SK-12-ux-design",
        "ux-design",
        "Design flows, wireframes and microcopy (Urdu/English/Arabic RTL) with accessibility; use for M2/M8 UX.",
        "Deliver calm, accessible UX that asks rather than asserts.",
        [
            "Design from personas and stories in HC-01.",
            "Provide RTL-aware copy guidance; never invent scripture text.",
            "Coordinate with AG-10 on emotional/crisis surfaces.",
            "Hand specs to AG-11; request AG-10/Project Lead review as gated.",
        ],
        "- `docs/07-ux/`",
        "- [ ] Accessibility considered\n- [ ] Ask-not-assert copy\n- [ ] Safety surfaces reviewed with AG-10 when relevant",
        "`AGENTS.md` · `docs/07-ux/` · `docs/01-requirements/personas.md`",
    ),
    (
        "SK-13-backend-development",
        "backend-development",
        "Implement optional research ingestion and catalogue signing pipelines; use for M4–M5 backend tasks.",
        "Build minimal backend/scripts that preserve privacy and catalogue integrity.",
        [
            "Implement only what HC-01/spec requires under `app/backend/` or `scripts/`.",
            "Never accept raw behavioural PII; research export requires consent path.",
            "Catalogue signing must reject unapproved verification.status.",
            "Coordinate security review with AG-09.",
        ],
        "- `app/backend/`, `scripts/`\n- Content validation hooks as specified",
        "- [ ] No raw signal exfiltration\n- [ ] Unapproved catalogue items rejected\n- [ ] AG-09 concerns addressed",
        "`AGENTS.md` · `content/validation/` · `docs/06-security-privacy-safety/`",
    ),
    (
        "SK-14-qa-testing",
        "qa-testing",
        "Produce test strategy, cases, automation and device matrix; use for M2–M10 QA.",
        "Prove acceptance criteria with evidence on a realistic device matrix.",
        [
            "Map ACs to test cases; cover privacy, safety and content gates.",
            "Maintain device matrix and automation under `tests/` and `docs/08-testing/`.",
            "Record evidence links for HC-03/HC-05.",
            "Escalate gaps via HC-07; never mark Done without evidence.",
        ],
        "- `docs/08-testing/`\n- `tests/`",
        "- [ ] Must ACs covered\n- [ ] Device matrix current\n- [ ] Evidence paths linked",
        "`AGENTS.md` · `docs/08-testing/` · `docs/01-requirements/acceptance-criteria.md`",
    ),
    (
        "SK-15-devops-release",
        "devops-release",
        "Own CI/CD, signing, SBOM and Play release tracks; use for M3–M10 DevOps work.",
        "Keep release pipelines enforcing task IDs, schema checks and secure signing.",
        [
            "Implement/maintain workflows under `.github/workflows/`.",
            "Enforce task-ID, docs-link and content-schema checks as specified.",
            "Document release operations; coordinate secrets handling with AG-09.",
            "Never bypass human Release gate.",
        ],
        "- `.github/workflows/`\n- `docs/09-release-operations/`",
        "- [ ] Blocking checks for task IDs/schema\n- [ ] Signing/SBOM path documented\n- [ ] Release gate respected",
        "`AGENTS.md` · `docs/09-release-operations/`",
    ),
    (
        "SK-16-traceability-audit",
        "traceability-audit",
        "Independently audit deliverables against requirements and DoD; issue HC-05 verdicts.",
        "Provide four-eyes assurance that outputs match requirements, IDs and AGENTS.md rules.",
        [
            "Read HC-03 and listed outputs; do not author the work under review.",
            "Check Task/Requirement IDs, DoD checklist, AGS-04 Gates and human-gate needs.",
            "File HC-04 findings; issue HC-05 verdict to AG-00.",
            "Trap cases (scripture generation, paid LLM, gate bypass) → reject and escalate.",
        ],
        "- HC-04 findings\n- HC-05 verdict\n- Traceability matrix notes as needed",
        "- [ ] Independent of author\n- [ ] Evidence checked, not assumed\n- [ ] Verdict explicit",
        "`AGENTS.md` · `docs/11-agent-system/handoff-contracts.md` · `docs/01-requirements/traceability-matrix.md`",
    ),
]

for folder, name, desc, purpose, steps, outputs, checklist, refs in DOMAIN:
    sid = folder.split("-", 1)[0]
    proc = "\n".join(f"{i + 1}. {s}" for i, s in enumerate(steps))
    skills[folder] = f"""---
name: {name}
description: {desc}
---

# {sid} · {name}

**Version:** 0.1 · **Status:** Draft (validated in T-M3-006)

## Purpose
{purpose}

## Inputs
HC-01 task packet; the documents it lists; `AGENTS.md`.

## Procedure
{proc}

## Outputs
{outputs}

## Quality checklist
{checklist}

{ALWAYS}

## References
{refs}
"""

for folder, content in skills.items():
    path = base / folder / "SKILL.md"
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8", newline="\n")
    print(f"Wrote {folder}: {path.stat().st_size} bytes")

print("Done", len(skills))
