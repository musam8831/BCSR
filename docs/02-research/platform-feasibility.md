# Platform Feasibility — RES-01 v0.1

Owner: AG-05 Platform Feasibility Engineer. Each claim below must be confirmed by a spike (T-M2-001..005) on real devices before Gate G2. Android API behaviour changes between versions and Play policies change often, so re-verify against current official documentation at spike time.

## 1. Android signal sources
| Signal | API / permission | Access model | Feasibility | Notes / risks |
|---|---|---|---|---|
| App foreground/background events | `UsageStatsManager.queryEvents` · `PACKAGE_USAGE_STATS` | Special app access; the user enables it in Settings | High | Core signal. Needs prominent disclosure. Events are retained by the OS for a limited period, so poll regularly. |
| Screen interactive / keyguard events | Usage events (`SCREEN_INTERACTIVE`, `KEYGUARD_HIDDEN`, API 28+) or `ACTION_SCREEN_ON/OFF`, `ACTION_USER_PRESENT` broadcasts | Same as above / runtime | High | Unlock frequency and checking behaviour |
| App category | `ApplicationInfo.category` (API 26+) plus a bundled mapping table | None | Medium | Many apps leave the category undefined, so curate a mapping for top apps in Pakistan |
| Notification counts | `NotificationListenerService` | User-enabled notification access | Medium | Must discard content immediately; increases trust burden. Mark Should, not Must. |
| Physical activity | Activity Recognition Transition API · `ACTIVITY_RECOGNITION` (API 29+) | Runtime | Medium | Uses Google Play services; check the licence terms and offline behaviour |
| Coarse place context | Fused location coarse · `ACCESS_COARSE_LOCATION` | Runtime | Low priority | Derive home/away only; consider skipping in the MVP |
| Battery / charging | `BatteryManager` broadcasts | None | High | Sleep-proxy context |
| In-app interaction | Own UI telemetry | None | High | Check-in latency, recommendation dwell time |
| Typing dynamics in other apps | Would need an IME or AccessibilityService | — | **Rejected** | ADR-0002: privacy and Play policy |
| Message/call content | SMS/call-log permissions | — | **Rejected** | Out of scope; Play restricted |

## 2. Background execution
- Use `WorkManager` periodic work (≥ 15 min) to pull usage events in batches. Avoid long-running foreground services unless a spike proves it is needed. Foreground-service types are restricted on Android 14+.
- Doze and OEM battery killers (common on Xiaomi, Oppo, Vivo, Infinix and Tecno in Pakistan) can delay work. Spike T-M2-004 must test the top 5 OEMs by local market share.

## 3. Google Play policy checkpoints (T-M2-002)
- Prominent disclosure and consent before collecting usage data.
- A Data Safety form that declares the collection accurately.
- Usage Access is a special permission. Confirm it is acceptable for a "digital wellbeing" core purpose and document the justification.
- Health-app and sensitive-information policies: the app must not make medical claims.
- AccessibilityService use is not permitted for this purpose (rejected).

## 4. iOS (deferred)
Apple's Screen Time APIs (FamilyControls, DeviceActivity) expose usage only inside sandboxed extensions and do not let a third-party app export per-app usage streams for its own modelling. An iOS version would need to be EMA-led with limited passive context. Revisit after the MVP.

## 5. On-device ML runtime options
| Option | Licence | Fit |
|---|---|---|
| LiteRT (TensorFlow Lite) | Apache-2.0 | Primary candidate for small NN models |
| ONNX Runtime Mobile | MIT | Candidate if training in PyTorch/scikit-learn (via skl2onnx) |
| Pure Kotlin gradient-boosted tree inference (exported) | Own code | Very small, fast, explainable |

## 6. Spike exit criteria (Gate G2 inputs)
Each spike reports: the device matrix, data captured (sample schema), battery cost over 24 h, failure modes, and a go / no-go / go-with-conditions verdict.
