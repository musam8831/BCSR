#!/usr/bin/env python3
"""Catalogue validator (T-M3-005; CON-01, CON-03, ADR-0005).

Checks every item in content/catalogue/items/ and returns a list of error strings:
  1. JSON schema (content/schema/catalogue.schema.json)
  2. id matches the file name and is unique
  3. reference sanity: Qur'an surah/ayah range exists; type and reference kind agree
  4. approved items: review record exists, names a reviewer, cites the item id; no placeholder text
  5. duplicate references across items
It never edits items: agents may not change scripture fields (AGENTS.md §2).
"""
import json
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parents[2]
SCHEMA = ROOT / "content/schema/catalogue.schema.json"
ITEMS = ROOT / "content/catalogue/items"
RECORDS = ROOT / "docs/05-spiritual-content/review-records"

# Ayah count per surah (Hafs, Kufan count; total 6236), used only to check reference ranges.
# shortcut: hard-coded counts, upgrade to the Tanzil metadata file once the Qur'an source is imported (T-M1-010).
AYAH_COUNTS = [
    7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135,
    112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85,
    54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13,
    14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42,
    29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
    11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6,
]
assert len(AYAH_COUNTS) == 114 and sum(AYAH_COUNTS) == 6236

PLACEHOLDER = re.compile(r"TO_BE_ENTERED|TO_VERIFY|\bTBD\b")


def _strings(value):
    if isinstance(value, str):
        yield value
    elif isinstance(value, dict):
        for v in value.values():
            yield from _strings(v)
    elif isinstance(value, list):
        for v in value:
            yield from _strings(v)


def _ref_key(ref):
    if ref.get("kind") == "quran":
        return ("quran", ref.get("surah"), ref.get("ayah_from"), ref.get("ayah_to"))
    return ("hadith", str(ref.get("collection", "")).strip().lower(), str(ref.get("number", "")).strip(),
            str(ref.get("numbering_system", "")).strip().lower())


def validate(items, records_dir, schema):
    """items: list of (file_name, parsed_json). Returns a list of error strings (empty = valid)."""
    import jsonschema

    errors = []
    validator = jsonschema.Draft202012Validator(schema)
    seen_ids, seen_refs = {}, {}
    for name, item in items:
        for e in validator.iter_errors(item):
            errors.append(f"{name}: schema: {e.message}")
        if not isinstance(item, dict):
            continue
        iid = item.get("id")
        if iid and pathlib.Path(name).stem != iid:
            errors.append(f"{name}: id {iid} does not match the file name")
        if iid in seen_ids:
            errors.append(f"{name}: duplicate id {iid} (also in {seen_ids[iid]})")
        seen_ids.setdefault(iid, name)

        ref = item.get("reference") or {}
        kind, typ = ref.get("kind"), item.get("type")
        if typ == "quran" and kind != "quran":
            errors.append(f"{name}: type quran needs a Qur'an reference")
        if typ == "hadith_dua" and kind != "hadith":
            errors.append(f"{name}: type hadith_dua needs a hadith reference")
        if kind == "quran":
            s, a, b = ref.get("surah"), ref.get("ayah_from"), ref.get("ayah_to")
            if isinstance(s, int) and 1 <= s <= 114 and isinstance(a, int) and isinstance(b, int):
                if b < a:
                    errors.append(f"{name}: ayah_to {b} is before ayah_from {a}")
                if b > AYAH_COUNTS[s - 1]:
                    errors.append(f"{name}: surah {s} has {AYAH_COUNTS[s - 1]} ayat, reference goes to {b}")
        key = _ref_key(ref)
        if key in seen_refs:
            errors.append(f"{name}: duplicate reference, same as {seen_refs[key]}")
        seen_refs.setdefault(key, name)

        v = item.get("verification") or {}
        if v.get("status") == "approved":
            if any(PLACEHOLDER.search(t) for t in _strings(item)):
                errors.append(f"{name}: approved item still contains placeholder text")
            rid = v.get("record_id")
            rr = pathlib.Path(records_dir) / f"{rid}.md"
            if not rid or not rr.exists():
                errors.append(f"{name}: approved without review record {rid}.md")
            else:
                text = rr.read_text(encoding="utf-8")
                if iid not in text:
                    errors.append(f"{name}: review record {rid} does not cite {iid}")
                m = re.search(r"^Reviewer:\s*(.+)$", text, re.M)
                if not m or not m.group(1).strip():
                    errors.append(f"{name}: review record {rid} names no reviewer")
    return errors


def load_items(items_dir=ITEMS):
    out = []
    for p in sorted(pathlib.Path(items_dir).glob("*.json")):
        try:
            out.append((p.name, json.loads(p.read_text(encoding="utf-8"))))
        except json.JSONDecodeError as e:
            out.append((p.name, {"_parse_error": str(e)}))
    return out


def main():
    schema = json.loads(SCHEMA.read_text(encoding="utf-8"))
    items = load_items()
    errors = validate(items, RECORDS, schema)
    if errors:
        print("\n".join(errors))
        return 1
    print(f"OK: {len(items)} catalogue items valid")
    return 0


if __name__ == "__main__":
    sys.exit(main())
