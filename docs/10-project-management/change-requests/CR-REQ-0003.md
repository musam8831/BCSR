# CR-REQ-0003 — AC-SR-02 "about screen" does not exist

| Field | Value |
|---|---|
| raised_by | AG-14 (T-M5-013), on an AG-16 finding |
| task_id | T-M5-013 |
| routed | AG-00 → AG-02 → Project Lead |
| status | Closed 2026-10-22: option (a) — About screen built (T-M5-024); AC-SR-02 unchanged. Closure accepted by AG-00 under the Project Lead's standing delegation to follow the expert recommendation (no requirement changed, so AG-02 had nothing to amend); gate: AG-16 on PR #26 |

**Gap.** AC-SR-02 and SAF-01 §1 say the not-medical statement appears in onboarding, the safety screen and the **about** screen. The app has no About screen. The gate check (`scripts/check_android_gates.py`) uses the Privacy Center as a stand-in.

**Impact.** AC-SR-02 cannot be met as written. Scope: small; schedule: none; risk: low.

**Proposed AC text.** "…onboarding, the safety screen, and the Privacy Center (which also shows the app's about information: version, licences and attributions, CR-07)…"

**Alternatives.** (a) Build a separate About screen (version, licences, Tanzil attribution per CR-07) and keep the AC. (b) Accept the proposed wording.
