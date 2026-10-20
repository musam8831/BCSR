# Handoff Contracts — AGS-03 v0.2

Every handoff is a Markdown block (in the PR description or task comment) with these exact fields.  
Mode / Owner / Collaborators / Gates are selected from [`agent-collaboration.md`](agent-collaboration.md) (AGS-04).

## HC-01 Task Packet (Orchestrator → agent)
```
task_id: T-Mx-NNN
title:
mode: fast-path | standard-workstream | cross-domain-epic | governance-epic | content-safety-gated
owner: AG-xx
collaborators: [AG-xx, ...]
gates: [AG-xx | Scholar | MH advisor | Project Lead | Tech Lead, ...]
informed: [AG-xx, ...]
agent: AG-xx
skills: [SK-xx]
objective:
inputs: [paths/IDs]
outputs: [exact paths]
requirements: [FR-/NFR-/...]
acceptance_criteria: [...]
dependencies_done: [T-...]
constraints: [AGENTS.md rules that apply]
reviewer: AG-xx / human
due:
considered_not_needed: [always-on checklist items skipped + reason]
```

`agent` is the recipient of this packet (often the Owner or the Implementer for a singleton subtask). `owner` / `collaborators` / `gates` describe the full squad for the change.

## HC-02 Specification Handoff (Owner / expert → implementation)
`spec_ref`, interfaces, data contracts, non-functional budgets, open questions (must be empty before Ready), test hints.

## HC-03 Deliverable Submission (agent → reviewer / Gate)
`task_id`, outputs produced (paths), how each acceptance criterion is met (evidence links), DoD checklist, known limitations, follow-up tasks proposed (not created).

## HC-04 Review Comment
`severity: blocker|major|minor|nit`, location, finding, rule/requirement violated, suggested fix.

## HC-05 Verdict (Gate / reviewer → Orchestrator)
`approve | approve-with-minors | changes-requested | reject`, summary, findings list.

## HC-06 Human Review Pack (AG-08 → Scholar; AG-10 → MH advisor)
Items/screens under review, sources attached, specific questions, decision table to fill, signature/date line.

## HC-07 Requirement Gap / Change Request
`raised_by`, task_id, the gap/change, impact (scope, schedule, risk), proposed requirement text, alternatives. Routed AG-00 → AG-02 → Project Lead.
