# Traceability Matrix — REQ-05 v1.0-rc

Maintained by AG-02 (T-M1-007). Generated from SRS §5, REQ-03 and REQ-04; every requirement has a story, every Must has an acceptance criterion. A requirement may not reach "Done" with an empty Test column; tests are added from M2 (T-M2-012).

Coverage: 57 requirements, 46 Must, all with stories and Must ACs.

| Req | Priority | Story | Acceptance | Design (doc/ADR) | Task(s) | Test | Status |
|---|---|---|---|---|---|---|---|
| FR-01 | M | US-01 | AC-FR-01 | component-architecture §Onboarding | T-M1-005, M8 | — (from M2, T-M2-012) | Baseline |
| FR-02 | M | US-02 | AC-FR-02 | component-architecture §Modes | T-M1-005 | — (from M2, T-M2-012) | Baseline |
| FR-03 | M | US-13 | AC-FR-03 | ADR-0002, data-dictionary §usage_event | T-M2-001 | — (from M2, T-M2-012) | Baseline |
| FR-04 | M | US-13 | AC-FR-04 | SRS §3 MOD-SN | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-05 | S | US-13 | — | SRS §3 MOD-SN | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-06 | S | US-13 | — | SRS §3 MOD-SN | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-07 | M | US-13 | AC-FR-07 | SRS §3 MOD-SN | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-08 | M | US-14 | AC-FR-08 | SRS §3 MOD-FE | T-M2-009 | `ml/features/test_fs001.py` (reference impl.; device side M5) | Baseline |
| FR-09 | M | US-14 | AC-FR-09 | SRS §3 MOD-FE | T-M2-009 | `ml/features/test_fs001.py` (robust_z; CR-REQ-0002 open) | Baseline |
| FR-10 | M | US-03 | AC-FR-10 | ADR-0003, data-flow §Inference | T-M2-006 | — (from M2, T-M2-012) | Baseline |
| FR-11 | M | US-14 | AC-FR-11 | SRS §3 MOD-SI | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-12 | S | US-14 | — | SRS §3 MOD-SI | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-13 | M | US-15 | AC-FR-13 | SRS §3 MOD-EM | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-14 | M | US-03, US-04 | AC-FR-14 | data-flow §Check-in | T-M1-005 | — (from M2, T-M2-012) | Baseline |
| FR-15 | M | US-23 | AC-FR-15 | ADR-0004, content-schema | T-M2-008 | TC-FR-15-01..03 `tests/test_build_catalogue_bundle.py` (build side); TC-FR-15-04 M5 | Baseline |
| FR-16 | M | US-03, US-07 | AC-FR-16 | SRS §3 MOD-RE | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-17 | S | US-16 | — | SRS §3 MOD-RE | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-18 | M | US-05 | AC-FR-18 | content-schema | T-M1-010 | — (from M2, T-M2-012) | Baseline |
| FR-19 | C | US-06 | — | SRS §3 MOD-CC | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-20 | M | US-16 | AC-FR-20 | SRS §3 MOD-CC | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-21 | M | US-03, US-10 | AC-FR-21 | component-architecture §Notifier | T-M1-005 | TC-FR-21-01..03 `:core:policy` tests; TC-FR-21-04 M5 | Baseline |
| FR-22 | S | US-17 | — | SRS §3 MOD-NT | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-23 | S | US-11 | — | SRS §3 MOD-IN | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-24 | M | US-09 | AC-FR-24 | threat-model §Safety | T-M1-011 | — (from M2, T-M2-012) | Baseline |
| FR-25 | M | US-08 | AC-FR-25 | data-dictionary §Retention | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| FR-26 | M | US-18 | AC-FR-26 | SRS §3 MOD-PC | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-27 | C | US-12 | — | SRS §3 MOD-RS | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-28 | M | US-06 | AC-FR-28 | SRS §3 MOD-ST | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| FR-29 | M | US-07 | AC-FR-29 | SRS §3 MOD-RE | T-M2-009 | — (from M2, T-M2-012) | Baseline |
| NFR-01 | M | US-19 | AC-NFR-01 | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-02 | M | US-19 | AC-NFR-02 | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-03 | S | US-19 | — | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-04 | M | US-19 | AC-NFR-04 | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-05 | M | US-20 | AC-NFR-05 | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-06 | S | US-19 | — | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-07 | S | US-19 | — | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-08 | M | US-04, US-25 | AC-NFR-08 | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| NFR-09 | M | US-24 | AC-NFR-09 | ADR-0003, RES-03 | T-M2-005, T-M2-012 | — (from M2, T-M2-012) | Baseline |
| PR-01 | M | US-13 | AC-PR-01 | ADR-0002 | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| PR-02 | M | US-21 | AC-PR-02 | ADR-0002, threat-model, data-dictionary | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| PR-03 | M | US-12 | AC-PR-03 | ADR-0002, threat-model, data-dictionary | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| PR-04 | M | US-13 | AC-PR-04 | ADR-0002, threat-model, data-dictionary | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| PR-05 | M | US-01 | AC-PR-05 | ADR-0002, threat-model, data-dictionary | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| PR-06 | M | US-21 | AC-PR-06 | ADR-0002, threat-model, data-dictionary | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| PR-07 | M | US-12 | AC-PR-07 | ADR-0002, threat-model, data-dictionary | T-M2-007 | — (from M2, T-M2-012) | Baseline |
| SR-01 | M | US-09 | AC-SR-01 | threat-model §Safety | T-M1-011 | — (from M2, T-M2-012) | Baseline |
| SR-02 | M | US-22 | AC-SR-02 | safety-pathway (SAF-01) | T-M1-011 | — (from M2, T-M2-012) | Baseline |
| SR-03 | M | US-22 | AC-SR-03 | safety-pathway (SAF-01) | T-M1-011 | — (from M2, T-M2-012) | Baseline |
| SR-04 | M | US-22 | AC-SR-04 | safety-pathway (SAF-01) | T-M1-011 | — (from M2, T-M2-012) | Baseline |
| SR-05 | M | US-22 | AC-SR-05 | safety-pathway (SAF-01) | T-M1-011 | — (from M2, T-M2-012) | Baseline |
| CR-01 | M | US-05 | AC-CR-01 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
| CR-02 | M | US-23 | AC-CR-02 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
| CR-03 | M | US-23 | AC-CR-03 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
| CR-04 | M | US-23 | AC-CR-04 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
| CR-05 | M | US-23 | AC-CR-05 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
| CR-06 | M | US-23 | AC-CR-06 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
| CR-07 | M | US-23 | AC-CR-07 | source-policy, verification-workflow, ADR-0004 | T-M1-010, T-M3-005 | — (from M2, T-M2-012) | Baseline |
