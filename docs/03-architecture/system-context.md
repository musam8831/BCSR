# System Context — ARC-01 v0.1 (C4 level 1)

```mermaid
flowchart LR
  U([User]) -->|taps, check-ins, feedback| APP[BCSR Android App]
  OS[Android OS<br/>UsageStats, screen, activity] -->|metadata events| APP
  APP -->|notifications, du'a, insights| U
  APP -.->|opt-in, pseudonymised aggregates| RB[(Research Backend<br/>optional, M5+)]
  CMS[Content Pipeline<br/>scholar-approved catalogue] -->|signed catalogue bundle| APP
  SCH([Scholar Reviewer]) --> CMS
  HL[Helpline directory<br/>verified] --> APP
```

## Boundaries
- **Trust boundary 1:** device ↔ network. Only the signed catalogue (inbound) and consented aggregates (outbound) cross it.
- **Trust boundary 2:** content pipeline. Only items approved by a scholar are packaged into the signed bundle.
- The app is fully functional offline. The research backend is optional and does not exist in the MVP critical path.
