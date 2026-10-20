# ADR-0001: Android-first MVP, native Kotlin

- Status: Proposed (confirm at G2 after T-M2-001)
- Date: 2026-10-15
- Related: D-002, FR-03..07, RES-01

## Context
The core value depends on cross-app usage metadata. Android exposes it through UsageStatsManager with user consent. iOS confines Screen Time data to sandboxed extensions. Pakistan's smartphone market is predominantly Android.

## Decision
Build the MVP as a native Android app in Kotlin with Jetpack Compose. Set min SDK 28 (needed for screen/keyguard usage events), to be confirmed by spike data on the target device distribution. Defer iOS.

## Options considered
| Option | Pros | Cons |
|---|---|---|
| Native Kotlin | Full API access, best background control, LiteRT first-class | Android only |
| Flutter / React Native | Cross-platform UI | Sensing needs native plugins anyway; iOS gives little data |
| iOS + Android parallel | Reach | Doubles cost for weak iOS sensing |

## Consequences
A single platform team. The UI layer is kept clean so that a Compose Multiplatform port is possible later.
