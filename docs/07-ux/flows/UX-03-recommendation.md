# UX-03 Recommendation card (low-fi)

Task: T-M2-011 · Owner: AG-12 · Stories: US-05, US-06, US-07, US-16, US-17, US-23, US-25 · Requirements: FR-15..FR-20, FR-22, FR-29, NFR-08, CR-01, CR-07

```mermaid
flowchart TD
  C[Card: situational title] --> A[Arabic, RTL, naskh font]
  A --> TR[Transliteration (labelled as an aid)]
  TR --> TL[Translation in UI language]
  TL --> REF[Reference: Surah:Ayah / collection + number + grading]
  REF --> ACT[Actions: ▶ audio if licensed · counter if repeated · ☆ save]
  ACT --> FB[Helpful · Not now · Not relevant]
  FB --> WHY[Why am I seeing this?]
```
- Order and presentation follow the source policy (CON-01 §4): Arabic first; honorific ﷺ; never truncate a verse; no ads.
- Mock-ups use a **labelled placeholder** ("[Arabic text from catalogue]"), never real or invented scripture (SK-12 step 2).
- "Why am I seeing this?" gives a plain reason from the inputs: "You said you feel restless, and it is evening" (NFR-08); no diagnostic words.
- Feedback buttons are one tap and optional (FR-29).
- Counter (FR-22) appears only when the item has `repetitions`.
