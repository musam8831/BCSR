#!/usr/bin/env python3
"""Repository governance checks (run in CI).

1. Validate every catalogue item against the JSON schema; only allow `approved`
   status when a matching review record exists.
2. Ensure the task register has unique IDs and no dangling dependencies.
3. Ensure every skill folder has SKILL.md with name + description frontmatter.
"""
import csv, json, pathlib, re, sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
errors = []

# 1. Content catalogue
try:
    import jsonschema
    schema = json.loads((ROOT / "content/schema/catalogue.schema.json").read_text())
    for item_path in sorted((ROOT / "content/catalogue").rglob("*.json")):
        item = json.loads(item_path.read_text())
        for e in jsonschema.Draft202012Validator(schema).iter_errors(item):
            errors.append(f"{item_path.name}: {e.message}")
        v = item.get("verification", {})
        if v.get("status") == "approved":
            rr = ROOT / "docs/05-spiritual-content/review-records" / f"{v.get('record_id')}.md"
            if not rr.exists():
                errors.append(f"{item_path.name}: approved without review record {rr.name}")
except ImportError:
    errors.append("jsonschema not installed (pip install jsonschema)")

# 2. Task register
reg = ROOT / "docs/10-project-management/task-register.csv"
rows = list(csv.DictReader(reg.open()))
ids = [r["ID"] for r in rows]
if len(ids) != len(set(ids)):
    errors.append("task-register: duplicate task IDs")
for r in rows:
    for d in filter(None, r["Depends on"].split(";")):
        if d not in ids:
            errors.append(f"task-register: {r['ID']} depends on unknown {d}")

# 3. Skills
for sk in sorted((ROOT / "docs/11-agent-system/skills").glob("SK-*")):
    f = sk / "SKILL.md"
    if not f.exists():
        errors.append(f"{sk.name}: missing SKILL.md"); continue
    head = f.read_text().split("---")[1] if f.read_text().startswith("---") else ""
    if not re.search(r"^name:", head, re.M) or not re.search(r"^description:", head, re.M):
        errors.append(f"{sk.name}: SKILL.md frontmatter needs name and description")

if errors:
    print("\n".join(errors)); sys.exit(1)
print(f"OK: {len(rows)} tasks, skills and catalogue valid")
