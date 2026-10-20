# ADR-0003: On-device inference with open-source ML; no paid LLMs

- Status: Accepted
- Date: 2026-10-15
- Related: D-004, NFR-01, NFR-02, RES-03

## Decision
All inference and recommendation run on the device: a cold-start rule model → population tabular model (GBT/LR) → optional small sequence model, with per-user calibration and a contextual bandit. Training uses open-source Python tooling. No paid LLM API anywhere in the product or the training pipeline.

## Consequences
Works offline, low cost, privacy-preserving. Model size and latency budgets apply (NFR-02, NFR-04). Explainability comes from feature attributions mapped to plain-language reasons.
