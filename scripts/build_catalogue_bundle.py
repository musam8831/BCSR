#!/usr/bin/env python3
"""Build the app's catalogue bundle (ADR-0005 §3; pilot P1, AG-13).

Steps (SK-13 pipeline template): validate → keep approved only → manifest with SHA-256 → report.
Signing is a separate offline step by the Project Lead (ADR-0005 §4); this script never touches keys.

Usage: python scripts/build_catalogue_bundle.py --version 2026.1 --out build/catalogue
Exit 1 if any item fails validation: a bundle is never built from an invalid catalogue.
"""
import argparse
import datetime
import hashlib
import json
import pathlib
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "content/validation"))
import validate_catalogue as vc  # noqa: E402


def items_digest(items):
    """SHA-256 over the canonical JSON of the items array (sorted keys, no spaces, UTF-8)."""
    canon = json.dumps(items, ensure_ascii=False, sort_keys=True, separators=(",", ":"))
    return hashlib.sha256(canon.encode("utf-8")).hexdigest()


def build(items, version, key_id, created=None):
    """items: parsed catalogue items. Returns (bundle, report). Only `approved` items enter the bundle."""
    approved = sorted((i for i in items if (i.get("verification") or {}).get("status") == "approved"),
                      key=lambda i: i["id"])
    excluded = sorted(i.get("id", "?") for i in items if i not in approved)
    created = created or datetime.datetime.now(datetime.timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")
    bundle = {
        "manifest": {
            "version": version,
            "created": created,
            "item_count": len(approved),
            "items_sha256": items_digest(approved),
            "key_id": key_id,
        },
        "items": approved,
    }
    report = {"included": [i["id"] for i in approved], "excluded_not_approved": excluded}
    return bundle, report


def main(argv=None):
    ap = argparse.ArgumentParser()
    ap.add_argument("--version", required=True)
    ap.add_argument("--key-id", default="bcsr-catalogue-2026")
    ap.add_argument("--out", default="build/catalogue")
    args = ap.parse_args(argv)

    loaded = vc.load_items()
    errors = vc.validate(loaded, vc.RECORDS, json.loads(vc.SCHEMA.read_text(encoding="utf-8")))
    if errors:
        print("Catalogue invalid; no bundle built:\n" + "\n".join(errors))
        return 1
    bundle, report = build([item for _, item in loaded], args.version, args.key_id)
    out = pathlib.Path(args.out)
    out.mkdir(parents=True, exist_ok=True)
    path = out / f"catalogue-{args.version}.json"
    path.write_text(json.dumps(bundle, ensure_ascii=False, indent=1), encoding="utf-8")
    print(f"Wrote {path}: {len(report['included'])} approved item(s); "
          f"excluded (not approved): {', '.join(report['excluded_not_approved']) or 'none'}")
    print("Next: the Project Lead signs this file offline (ADR-0005, section 4).")
    return 0


if __name__ == "__main__":
    sys.exit(main())
