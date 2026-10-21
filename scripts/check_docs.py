#!/usr/bin/env python3
"""Documentation checks for CI (T-M3-007).

1. Relative Markdown links in docs/ and the repo-root .md files resolve to existing files.
2. Every requirement ID cited in docs/ (FR-, NFR-, PR-, SR-, CR- with two digits) exists in SRS §5.
3. Every SRS §5 requirement has a row in the traceability matrix (REQ-05).
Exit code 1 lists every problem.
"""
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
LINK = re.compile(r"\[[^\]]*\]\(([^)\s]+)\)")
REQ = re.compile(r"\b((?:FR|NFR|PR|SR|CR)-\d{2})\b")
RANGE = re.compile(r"\b((?:FR|NFR|PR|SR|CR))-(\d{2})\.\.(?:(?:FR|NFR|PR|SR|CR)-)?(\d{2})\b")


def md_files(root=ROOT):
    yield from sorted((root / "docs").rglob("*.md"))
    yield from sorted(root.glob("*.md"))


def broken_links(path, text):
    out = []
    for target in LINK.findall(text):
        if re.match(r"^[a-z][a-z0-9+.-]*:", target) or target.startswith("#"):
            continue  # external URL, mailto, or in-page anchor
        rel = target.split("#", 1)[0]
        if rel and not (path.parent / rel).resolve().exists():
            out.append(f"{path.relative_to(ROOT)}: broken link -> {target}")
    return out


def cited_ids(text):
    ids = set(REQ.findall(text))
    for pre, a, b in RANGE.findall(text):
        ids |= {f"{pre}-{i:02d}" for i in range(int(a), int(b) + 1)}
    return ids


def srs_ids(srs_text):
    catalogue = srs_text.split("## 5. Requirements catalogue", 1)[-1].split("## 5.6", 1)[0]
    return set(re.findall(r"^\| ((?:FR|NFR|PR|SR|CR)-\d{2}) \|", catalogue, re.M))


def main():
    errors = []
    srs = (ROOT / "docs/01-requirements/SRS.md").read_text(encoding="utf-8")
    defined = srs_ids(srs)
    for p in md_files():
        text = p.read_text(encoding="utf-8")
        errors += broken_links(p, text)
        if p.is_relative_to(ROOT / "docs"):
            for rid in sorted(cited_ids(text) - defined):
                errors.append(f"{p.relative_to(ROOT)}: cites unknown requirement {rid}")
    matrix = (ROOT / "docs/01-requirements/traceability-matrix.md").read_text(encoding="utf-8")
    rows = set(re.findall(r"^\| ((?:FR|NFR|PR|SR|CR)-\d{2}) \|", matrix, re.M))
    for rid in sorted(defined - rows):
        errors.append(f"traceability-matrix.md: no row for {rid}")
    if errors:
        print("\n".join(errors))
        return 1
    print(f"OK: links resolve; {len(defined)} requirements, all cited IDs defined and traced")
    return 0


if __name__ == "__main__":
    sys.exit(main())
