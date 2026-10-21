# UX-02 Check-in (low-fi)

Task: T-M2-011 · Owner: AG-12 · Stories: US-03, US-04, US-15, US-22 · Requirements: FR-13, FR-14, SR-03, AC-FR-13

```mermaid
flowchart LR
  N[Notification as a question<br/>"A moment for yourself? How are you?"] --> M[Tap 1: mood, 5 faces]
  M --> E[Tap 2: energy, 5 steps]
  E --> T[Tap 3 (optional): what fits best?<br/>worry · restless · low · angry · can't sleep · lonely · tired · grateful · other · I'm not safe]
  T -->|I'm not safe| SF[UX-04 Safety]
  T --> R[UX-03 Recommendation]
```
- **Three taps maximum** to save (FR-13); the context step can be skipped.
- Trigger copy always **asks** ("How are you?"), never states ("You seem stressed"). The interview guide (Q14) tests which wording people prefer.
- State words are placeholders until the T-M1-003 interviews give the Urdu words people actually use (RES-04 §2).
- "I'm not safe" is always the last option and opens the safety pathway first (SAF-01, SR-03).
- Lock-screen notification text is neutral and never names a feeling (TH-06).
- Manual check-in: a "How am I?" button on Home opens the same flow.
