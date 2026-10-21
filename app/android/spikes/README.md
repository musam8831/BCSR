# BCSR spike app (M2 feasibility, not the product)

Throwaway app for spikes SPK-01, SPK-03, SPK-04 and SPK-05 (`docs/02-research/spikes/`). It is **not** the product app and is never published.

## What it records (all on the device, in the app's external files folder)
| File | Spike | Content |
|---|---|---|
| `usage_events.csv` | SPK-01 | Time, event type (app resumed/paused, screen on/off, keyguard shown, unlock), app **category** (never the package name) |
| `notifications.csv` | SPK-03 | Time and app category of each notification. Title, text and extras are never read |
| `activity.csv` | SPK-03 | Activity transitions (still, walking, running, bicycle, vehicle; enter/exit) |
| `heartbeat.csv` | SPK-04, SPK-05 | Every 15 min (WorkManager): time, battery %, charging, power-save, device idle, app standby bucket |
| `errors.csv` | all | Permission errors |

## Build
Requires Android Studio's JDK 17+ and the Android SDK (platform 36).
```
cd app/android/spikes
./gradlew :app:testDebugUnitTest :app:assembleDebug
```
APK: `app/build/outputs/apk/debug/app-debug.apk`. Built and unit-tested on 2026-10-21 (4/4 tests pass); not yet run on a phone.

## Run on a test phone
1. `adb install -r app/build/outputs/apk/debug/app-debug.apk`
2. Open "BCSR Spike" and tap buttons 1–4 in order, granting each permission.
3. Use the phone normally for the period the protocol says.
4. Tap 5 (collect usage events) and 6 (summary); copy the summary into the protocol's result table.
5. Pull the files: `adb pull /sdcard/Android/data/org.pide.bcsr.spike/files/ ./spike-results/<device-id>/`
6. Uninstall when done: `adb uninstall org.pide.bcsr.spike`

Use only phones whose owner has agreed to the test; the files show that person's phone habits. Keep pulled files out of Git.
