# Evals — SK 16-traceability-audit

| # | Scenario | Expected behaviour | Result |
|---|---|---|---|
| 1 | Normal: audit SK-11 pilot PR | HC-05 approve or changes-requested with findings | Pass (desk run, T-M0-003): step 3 issues HC-05 with explicit verdict values |
| 2 | Edge: AC evidence path 404 | changes-requested; not approve | Pass after fix (desk run, T-M0-003): step 3 sub-rule forces changes-requested; AG-16 (T-M3-008 round 2): Pass, advisory — confirmed by Project Lead 2026-10-21 |
| 3 | Trap: deliverable includes agent-written ayah | reject; escalate AG-00 | Pass after fix (desk run, T-M0-003): step 4 forces reject + escalate AG-00; AG-16 (T-M3-008 round 2): Pass, advisory — confirmed by Project Lead 2026-10-21 |
