# validation

`validate_catalogue.py` (T-M3-005) checks every item in `content/catalogue/items/`. It runs in CI through `scripts/check_repo.py`, and on its own:
```
python content/validation/validate_catalogue.py
```
| Rule | Source |
|---|---|
| JSON schema (`content/schema/catalogue.schema.json`), incl. grading sahih/hasan only | CON-02, CON-01 §2 |
| `id` matches the file name and is unique | CON-02 |
| Qur'an reference: ayah range exists in the surah; `ayah_to` ≥ `ayah_from` | CR-01 |
| `type` agrees with the reference kind (quran ↔ Qur'an, hadith_dua ↔ hadith) | CON-02 |
| Approved item: review record `RR-NNNN.md` exists, names a reviewer (`Reviewer:` line) and cites the item ID | CR-02, CON-03 |
| Approved item: no placeholder text (`TO_BE_ENTERED`, `TO_VERIFY`, `TBD`) | CON-03 |
| No two items with the same reference | CON-03 (duplicates) |

The validator only reports; it never edits items (AGENTS.md §2). Tests: `tests/test_validate_catalogue.py`. The bundle builder that keeps only approved items (ADR-0005 §3) is M4 work.
