# Retrospective M0–M3 (T-M3-008)

Facilitator: AG-00 · Date: 2026-10-21 · Inputs: PRs #1–#21, AG-16 audits (`audits/T-M3-006-audit.md`, `audits/T-M3-008-skill-evals.md`), decision log D-008..D-013, risk register.

## 1. What worked
- **Docs as the source of truth.** Every change cited a task ID and passed CI; 21 PRs, no direct pushes to `main` after D-008 (one before it, 2bc25e1).
- **Verification before claiming.** DOIs checked against OpenAlex (2 bad ones caught), helplines and policies read from official pages, licences read from repositories, code built and tested. Fabrication was refused where people were needed (interviews, scholar sign-off, ethics submission).
- **Independent audit found real defects.** AG-16 as a separate subagent caught two majors (undefined hash format; an HC-07 claimed but never filed), a loop bug under extreme settings, and two failing edge evals, none of which the author's desk runs found.
- **Generated artefacts with built-in checks** (traceability matrix, M4/M5 backlog, catalogue validator, docs check) kept documents consistent.

## 2. What did not work
| Problem | Evidence | Consequence |
|---|---|---|
| Desk runs graded by the author | 4 of 9 M0 evals, 7 of 18 M1, 5 of 18 M2 "pass after fix"; AG-16 later failed 2 more | Desk runs are not validation |
| Agent gates replaced by the Project Lead | AG-16 reviews substituted on PRs #3–#20; AG-09/AG-07 co-gates not run in the pilot | Separation of duties weak (R-09, R-17); D-013 now requires each substitution to be recorded with evidence |
| One person in every human role | D-009; D-011 removed the MH advisor | Few real second opinions on safety and architecture |
| Human dependencies stall the plan | Scholar (T-M0-006), interviews (T-M1-003), ethics (T-M1-012), phone spikes (T-M2-00x), PhysioNet credentials | G1 and G2 undecided; only 2 of 10 agents have 2 weeks of Ready M4/M5 work |
| Tooling defects | Guard hook's relative path broke all shell commands; hook misreads chained `switch && push`; shell quoting in large edits | Lost time; both hook issues fixed or documented |
| Claims ahead of evidence | "HC-07 recorded" before it existed (P3); test counts not updated | Caught by AG-16; now an explicit SK-16 check |

## 3. Changes adopted
1. **Skills:** independent trap evals are required for v1.0 (SK-00 step 6 reworded); claims in HC-03 must point at an existing artefact (SK-16 already checks evidence; reinforced in SK-00).
2. **Gates:** a substituted gate is recorded with name, date and what was checked (D-013).
3. **Process:** human-dependent tasks are tracked as Backlog, not Ready (register definition, T-M3-009).
4. **Carried forward:** SHA-pin GitHub Actions (T-M5-015); CR-REQ-0001/0002 open with AG-02 before G1; AC-FR-21 48 h drop rule follows CR-REQ-0001's proposal.

## 4. Actions for the Project Lead (blocking M4/M5)
| Action | Unblocks |
|---|---|
| Confirm the scholar reviewer (plan: by 2026-11-15) | T-M1-010, T-M4-001..005, G1 |
| Run the 8 interviews | T-M1-003, T-M1-004, T-M5-020 |
| Choose the ethics committee and submit | T-M1-012, T-M5-012, T-M5-023 |
| Run the spike app on ≥ 5 phones | T-M2-001/003/004/005, G2, T-M5-003 |
| Apply for PhysioNet credentials | T-M2-006 |
| Consider a second technical reviewer for G2/G3 | R-17 |
