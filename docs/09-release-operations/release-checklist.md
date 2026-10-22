# Release checklist (draft, REL-02)

Task: T-M5-016 · Owner: AG-15 · Gates: AG-09, Project Lead, Safety reviewer (Project Lead, D-011) · **Status: partly done.** Creating the Play Console app, the internal-testing track and the upload key needs the Project Lead's Play developer account; no step below may put a key in the repository.

## One-time setup (Project Lead)
- [ ] Play Console app created (`org.pide.bcsr`), Play App Signing enabled.
- [ ] Upload key created **outside the repo**; path and passwords in `local.properties` / environment on the release machine only (both ignored by Git).
- [ ] Internal-testing track with named testers.
- [ ] Health apps declaration and Data safety form filed (`docs/06-security-privacy-safety/play-data-safety.md`).

## Every release
- [ ] CI green on the release commit (`checks`, `kotlin-tests`), SBOM artifact kept.
- [ ] Version code/name bumped in `app/android/app/build.gradle.kts`; CHANGELOG entry.
- [ ] Catalogue bundle signed offline and verified (REL-01); only approved items.
- [ ] `releaseBlockers()` (`:core:safety`) returns nothing for today's date; SAF-02 reviewed in the last 3 months.
- [ ] Safety copy matches SAF-01 (AC-SR-04); Project Lead safety sign-off recorded.
- [ ] Privacy and safety gate tests pass on ≥ 5 matrix devices (TST-01, T-M5-013/014).
- [ ] `./gradlew :app:bundleRelease` on the release machine; upload to the internal track.
- [ ] Full open-source licence notices in the app (MIT and BSD-3 notices, Apache-2.0 licence + NOTICE), generated from the SBOM, including transitive dependencies (AG-16 finding on PR #26)
- [ ] Tanzil notice on the About screen matches the header of the imported Tanzil file (T-M4-002)
- [ ] G-Release decision recorded (AGENTS.md §7) before any production track.
