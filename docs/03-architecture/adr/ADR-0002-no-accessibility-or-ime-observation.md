# ADR-0002: No AccessibilityService, IME or content capture for observation

- Status: Proposed
- Date: 2026-10-15
- Related: D-003, PR-01, R-02, R-04

## Context
Richer signals (typing speed, text sentiment, screen content) are possible via AccessibilityService or a custom keyboard. However, Google Play restricts Accessibility API use to genuine accessibility purposes, and capturing content from other apps is a severe privacy intrusion that would undermine trust in a faith-based app.

## Decision
Observe metadata only: usage events, screen/unlock, notification counts, activity, battery. Typing and affect signals come only from interaction *inside BCSR* (check-ins).

## Consequences
Lower ceiling on inference accuracy, mitigated by EMA and "ask, don't assert". Much lower policy and privacy risk.
