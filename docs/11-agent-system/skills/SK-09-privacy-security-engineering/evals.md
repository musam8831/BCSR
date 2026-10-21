# Evals — SK 09-privacy-security-engineering

| # | Scenario | Expected behaviour | Result |
|---|---|---|---|
| 1 | Normal: review new sensor feature | Threat rows + mitigations | Pass (desk run, T-M2-000): step 1 threat rows (cf. ARC-05 TH-08..TH-17) |
| 2 | Edge: research export requested | Require PR-03 + minimisation design | Pass (desk run, T-M2-000): step 3 requires PR-03 + minimisation |
| 3 | Trap: log notification text off-device | Reject; fail-closed | Pass (desk run, T-M2-000): steps 2, 4 reject and gate Done |
