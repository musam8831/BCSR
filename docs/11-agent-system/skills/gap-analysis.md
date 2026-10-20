# Skill gap analysis — M4–M10 → skills

> **Task:** T-M3-001  
> **Decision:** No new AG-17+ agents. Every M4–M10 task type maps to an existing skill. Deferred candidates noted below.

## 1. Summary

| Question | Answer |
|---|---|
| Missing Owner for a write path? | No — AGS-04 covers all registry paths |
| Missing skill for an M4–M10 task type? | No — deepen stubs; add `references/` packs |
| New agents required before G3? | **No** |
| Collaborator-only stress points | i18n/RTL (AG-12), IRB/consent packs (AG-09 + human gate) |

## 2. Milestone → skill map

| Milestone / task type | Owner | Primary skill | Collaborators / Gates |
|---|---|---|---|
| M4 Catalogue item metadata + schema | AG-08 | SK-08 | Scholar (G); AG-16 |
| M4 Catalogue signing / validation pipeline | AG-13 | SK-13 | AG-08, AG-09; AG-16 |
| M4 Content CI | AG-15 | SK-15 | AG-08, AG-13 |
| M5 Sensing app modules | AG-11 | SK-11 | AG-03, AG-05, AG-09; AG-16 |
| M5 Pilot study ops / consent UX | AG-09 + AG-12 | SK-09, SK-12 | IRB + Project Lead (human); AG-10 |
| M5 Research ingestion (optional) | AG-13 | SK-13 | AG-09 |
| M6 Feature engine / models | AG-06 | SK-06 | AG-07, AG-09; AG-16 |
| M6 Evaluation / model cards | AG-07 | SK-07 | Tech Lead; AG-16 |
| M7 Bandit / intervention policy | AG-06 | SK-06 | AG-07, AG-10; AG-16 |
| M8 UX flows / RTL / a11y | AG-12 | SK-12 | AG-10; Project Lead |
| M8 Compose UI integration | AG-11 | SK-11 | AG-12, AG-14 |
| M9 System test / device matrix | AG-14 | SK-14 | AG-16 |
| M9 Privacy audit | AG-09 | SK-09 | Tech Lead; AG-16 |
| M10 Release tracks / SBOM / CI | AG-15 | SK-15 | AG-09; Release human gate |
| Cross-cutting: requirements CRs | AG-02 | SK-02 | AG-16, Project Lead |
| Cross-cutting: ADRs / boundaries | AG-03 | SK-03 | Tech Lead |
| Cross-cutting: research / licences | AG-04 | SK-04 | AG-07 |
| Cross-cutting: platform spikes | AG-05 | SK-05 | AG-03 |
| Cross-cutting: safety pathway | AG-10 | SK-10 | MH advisor |
| Cross-cutting: governance / gates | AG-01 | SK-01 | Project Lead |
| Cross-cutting: orchestration / register | AG-00 | SK-00, SK-01 | Project Lead |
| Cross-cutting: independent audit | AG-16 | SK-16 | Project Lead |

## 3. Skill maturity actions (this programme)

| Skill | Action |
|---|---|
| SK-11, SK-13, SK-15 | Deepen + `references/` (T-M3-003) |
| SK-06, SK-07 | Deepen + `references/` (T-M3-004) |
| SK-08..SK-10, SK-14 | Deepen + role traps; SK-08 schema refs |
| SK-00, SK-01, SK-16 | Deepen orchestration / gate / audit procedures |
| SK-02..SK-05, SK-12 | Deepen procedures + light references |
| Tooling | GitNexus + lean Cursor rules (T-M3-012) |

## 4. Deferred agent candidates (do not create now)

| Candidate | Trigger to reopen |
|---|---|
| Localisation / i18n Owner | M8 volume exceeds AG-12 capacity after pilot |
| Research Ethics / Consent Owner | Recurring IRB/PR-03 pack authorship beyond AG-09 |

Until then: keep as Collaborator checklist items on AG-12 and AG-09 respectively (AGS-04 always-on rows).

## 5. Acceptance (T-M3-001)

- [x] Every M4–M10 task type maps to a skill
- [x] No new agent IDs required for G3
- [x] Deferred candidates documented with reopen triggers
