# Catalogue signing procedure (REL-01)

Task: T-M4-006 · Owners: AG-13 (tool) with AG-15 (release ops) · Gate: AG-09 · Design: ADR-0005 §4–§5, §8 · Tool: `scripts/sign_bundle.py`

The Project Lead holds the signing key **offline**. No private key is ever placed in the repository, CI, a cloud drive or a chat. The tool refuses a private-key path inside the repository.

## 1. One-time key creation
1. On a trusted offline computer (or one disconnected from the network for the ceremony), with this repository checked out:
   ```
   python scripts/sign_bundle.py keygen --key-id bcsr-catalogue-2026 --private E:/keys/bcsr-catalogue-2026.pem --public bcsr-catalogue-2026.pub
   ```
   `E:` is a removable drive kept only for this key.
2. Make **two** offline backups of the `.pem` file on separate removable drives, stored in different locations. Record where in a sealed note (not in the repo).
3. The `.pub` file is public. It is embedded in the app by T-M5-006 (pinned public key) and may be committed there.

## 2. Each release of the catalogue
1. A PR changes items; CI validates them and builds the bundle (T-M4-007). Merge only with a green CI and the scholar's review records.
2. On `main`, build the release bundle:
   ```
   python scripts/build_catalogue_bundle.py --version 2026.1 --out build/catalogue
   ```
3. Review the printed list of included items against the scholar's approvals.
4. Connect the key drive and sign:
   ```
   python scripts/sign_bundle.py sign --bundle build/catalogue/catalogue-2026.1.json --private E:/keys/bcsr-catalogue-2026.pem
   ```
   Disconnect the key drive.
5. Verify, as the app will:
   ```
   python scripts/sign_bundle.py verify --bundle build/catalogue/catalogue-2026.1.json --public bcsr-catalogue-2026.pub
   ```
6. Hand the `.json` and `.sig` to the app release (T-M5-006 loads them); record the version, date and item count in the release notes.

## 3. Rotation (ADR-0005 §8) and loss
- **Planned rotation:** create a new key (new `key_id`); ship an app release that pins **both** public keys; sign the next bundle with the new key; in the following release remove the old public key.
- **Key lost:** restore from a backup drive. If every copy is lost, create a new key and rotate as above; bundles signed with the old key keep working until the app drops that key.
- **Key exposed:** treat as compromised. Rotate immediately, and ship an app release that removes the old public key before any further bundle is published (TH-14).
