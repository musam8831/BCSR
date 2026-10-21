# SPK-03 — Notification counts and activity recognition

Task: T-M2-003 · Owner: AG-05 · Reviewer: AG-03 · Acceptance: verdict per signal · Status: **protocol and app ready; not yet run**

## Questions
1. Can a NotificationListenerService count notifications per app category without reading content (FR-05, PR-01)?
2. Does the Activity Recognition Transition API report still / walking / vehicle reliably enough for FR-06 and for "don't prompt while driving"?

## Method
Same phones as SPK-01, 24–48 h.
1. Enable the notification counter (button 2) and grant activity permission (button 3); start collection (button 4).
2. Manual log: send yourself 5 notifications from known apps (times noted); walk for 10 min; travel by car/bus for 10 min; sit still for 30 min.
3. Pull files; compare with the log.

## Results
| Device | Notification listener enabled | Notifications logged / sent (5) | Content fields present in CSV? (must be "no") | Walking detected | Vehicle detected | Still detected | Delay of transitions (approx.) | Notes |
|---|---|---|---|---|---|---|---|---|
| | | | | | | | | |

## Verdict
| Signal | Go / Conditional / No-go | Reason |
|---|---|---|
| Notification counts (FR-05) | | |
| Activity state (FR-06) | | |

## Known risks
- Notification access is a high-trust permission; Play policy treatment is in SPK-02 and must be confirmed before shipping FR-05.
- Notification listeners cannot be enabled on some low-RAM devices running Android 10 and below (Android reference documentation).
- Activity recognition depends on Google Play services; phones without it get no FR-06.
