# CR-REQ-0001 — AC-FR-21: drop vs defer

| Field | Value |
|---|---|
| raised_by | AG-14 (pilot P3) |
| task_id | T-M3-006 |
| routed | AG-00 → AG-02 → Project Lead |
| status | Open |

**Gap.** AC-FR-21 says a notification inside quiet hours or a Salah window is "deferred or dropped" but not when each applies.

**Impact.** Implementation choice in `:core:policy` (P2): drop only when the daily cap is reached, defer otherwise. Without a rule, tests cannot tell a correct drop from a bug. Scope: none; schedule: none; risk: low.

**Proposed AC text.** "…then it is deferred to the first free minute after the window, unless the daily cap is reached or no free minute exists in the next 48 h, in which case it is dropped; the daily notification cap is never exceeded."

**Alternatives.** (a) Always drop check-ins that fall in a window (simpler; loses prompts). (b) Defer only check-ins, drop reminders.
