# Verification Workflow — CON-03 v0.1

```mermaid
flowchart LR
  A[Draft metadata<br/>AG-08] --> B[Automated checks<br/>schema, reference format, duplicates]
  B --> C[Text entry from licensed source<br/>human editor]
  C --> D[Scholar review<br/>text, grading, translation, state mapping]
  D -->|approve| E[approved + review record]
  D -->|changes| C
  D -->|reject| F[rejected + reason]
  E --> G[Signed catalogue build]
```

## Review record (`review-records/RR-NNNN.md`)
Item ID · reviewer name & qualification · date · checks (Arabic exact ✔, reference ✔, grading ✔ with source, translation ✔, mapping ✔) · decision · notes.

## Rules
- Two-person rule for Arabic text: the editor enters it and the scholar verifies it.
- Any edit to an approved item resets its status to `in_review`.
- Quarterly re-audit of 10% of items.
