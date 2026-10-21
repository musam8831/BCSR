# Device Matrix — TST-02 v1.0-rc

Task: T-M2-012 · Owner: AG-14 · Used by SPK-01, SPK-03, SPK-04, SPK-05 and system test.

## Selection rules
- **≥ 5 devices**, from the **five most-used manufacturers in Pakistan**. Confirm the current list from a market-share source when buying or borrowing, and record the source and date below (not verified here).
- Cover **Android 9 (API 28, min SDK)** to the latest release, and at least one **low-RAM / Android Go** device (notification-listener limits, SPK-03).
- One **reference device** (mid-range, current) for the NFR-02 latency and NFR-04 battery budgets.
- At least one device **without Google Play services** if available (FR-06 fallback).

## Matrix
| ID | Role | OEM | Model | Android | API | RAM | Play services | Used in | Last run |
|---|---|---|---|---|---|---|---|---|---|
| DEV-01 | Reference (mid-range) | | | | | | Yes | SPK-01..05, system test | |
| DEV-02 | OEM 2 | | | | | | Yes | SPK-01, 03, 04 | |
| DEV-03 | OEM 3 | | | | | | Yes | SPK-01, 03, 04 | |
| DEV-04 | OEM 4 | | | | | | Yes | SPK-01, 03, 04 | |
| DEV-05 | OEM 5 | | | | | | Yes | SPK-01, 03, 04 | |
| DEV-06 | Oldest supported (API 28) | | | 9 | 28 | | Yes | SPK-01, system test | |
| DEV-07 | Low-RAM / Go | | | | | ≤ 2 GB | | SPK-03 | |

Market-share source for OEM choice: ______ (date ______).
