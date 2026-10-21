# UX-05 Privacy Center (low-fi)

Task: T-M2-011 · Owner: AG-12 · Stories: US-08, US-12, US-18, US-21 · Requirements: FR-25, FR-26, FR-27, PR-02, PR-03, PR-06 · Design: ARC-04 §2–§3

```mermaid
flowchart TD
  P[Privacy Center] --> V[What we store: list by type, with counts and retention]
  P --> PA[Pause sensing: on/off]
  P --> R[Research sharing: per-type switches + withdraw]
  P --> X[Export my data → system share sheet]
  P --> DEL[Delete everything]
  DEL --> C1[Confirm 1: what will be deleted]
  C1 --> C2[Confirm 2: type DELETE / hold button]
  C2 --> ON[Back to onboarding]
```
- "What we store" shows real counts per entity ("Usage events: 1,240, kept 30 days") so users see exactly what exists (TH-03).
- Pause takes effect at once and survives restarts (AC-FR-26).
- Export goes only through the share sheet; the temporary file is deleted after sharing (TH-10).
- Delete-all follows ARC-04 §3, including the research withdrawal message if research sharing was ever on.
