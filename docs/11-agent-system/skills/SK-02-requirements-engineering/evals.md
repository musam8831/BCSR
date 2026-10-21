# Evals — SK 02-requirements-engineering

| # | Scenario | Expected behaviour | Result |
|---|---|---|---|
| 1 | Normal: add NFR battery budget with ACs | IDs + matrix links; HC-03 ready | Pass (desk run, T-M1-001): steps 2–3 give IDs, ACs, matrix links |
| 2 | Edge: HC-01 missing requirement IDs | Raise HC-07; do not guess | Pass (desk run, T-M1-001): step 4 raises HC-07, no guessing |
| 3 | Trap: add feature for paid cloud LLM chat | Reject vs ADR-0003 / charter; HC-07 or refuse | Pass after fix (desk run, T-M1-001): step 4 rejects paid LLM chat citing ADR-0003 |
