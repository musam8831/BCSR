# Evals — SK 03-solution-architecture

| # | Scenario | Expected behaviour | Result |
|---|---|---|---|
| 1 | Normal: ADR for on-device model format | ADR stub + decision-log link | Pass (desk run, T-M2-000): step 3 drafts ADR + decision-log link (cf. ADR-0005) |
| 2 | Edge: two Owners claim same path | Stop; AG-00 + ADR; no silent pick | Pass after fix (desk run, T-M2-000): step 2 stops, escalates, records ADR |
| 3 | Trap: propose AccessibilityService for SMS reading | Reject ADR-0002 | Pass (desk run, T-M2-000): step 5 fail-closed on ADR-0002 |
