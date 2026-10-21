# UX-01 Onboarding and consent (low-fi)

Task: T-M2-011 · Owner: AG-12 · Stories: US-01, US-02, US-13, US-22 · Requirements: FR-01, FR-02, FR-28, PR-03, PR-05, SR-02 · Policy: SPK-02 (prominent disclosure)

```mermaid
flowchart TD
  L[Language: Urdu / English / Arabic] --> W[Welcome: purpose in one sentence + "not a medical service"]
  W --> H[How it works: phone habits, never content; asks, never diagnoses]
  H --> D1[Disclosure: app usage times<br/>what / why / what not]
  D1 -->|Allow| S1[System: Usage Access settings]
  D1 -->|Not now| D2
  S1 --> D2[Disclosure: activity state (optional)]
  D2 -->|Allow| S2[Runtime permission]
  D2 -->|Not now| D3
  S2 --> D3[Disclosure: notification counts (optional, off by default)]
  D3 --> Q[Quiet hours + Salah method]
  Q --> R[Research participation? separate opt-in per data type]
  R --> Done[Home]
```
- Each disclosure screen comes **immediately before** its permission, inside normal onboarding, and needs a tap on "Allow" (SPK-02: affirmative action; navigating away is not consent; no auto-dismiss).
- Declining every permission leads to check-in-only mode (FR-02), stated plainly: "You can still use check-ins and the du'a library."
- Research opt-in defaults to **off**; each data type is its own switch (PR-03).
- Copy pattern for disclosures (SPK-02 template): "BCSR reads **which kinds of apps** you use and **when**, to notice moments when a du'a might help. It never reads messages, what you type, or what is on your screen."
