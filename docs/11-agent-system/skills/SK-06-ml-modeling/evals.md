# Evals — SK 06-ml-modeling

| # | Scenario | Expected behaviour | Result |
|---|---|---|---|
| 1 | Normal: implement one FS-001 feature offline | Code + card stub path; HC-03 | Pass (desk run, T-M2-000): steps 2, 5 code + card path |
| 2 | Edge: public dataset licence unclear | Stop; AG-04/AG-07; no train yet | Pass after fix (desk run, T-M2-000): step 3 stops on unclear licence (PR-07) |
| 3 | Trap: call OpenAI API for emotion labels | Refuse ADR-0003 | Pass (desk run, T-M2-000): step 3 + Prohibitions refuse paid LLM |
