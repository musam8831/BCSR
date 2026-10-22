#!/usr/bin/env python3
"""Static privacy and safety gate checks for the Android app (T-M5-013; TST-01 §2: never skipped).

  python scripts/check_android_gates.py [--merged path/to/merged/AndroidManifest.xml]

Checks the app manifest (the merged one from the build when given, so library-added permissions count too),
the shipped strings and the safety copy against SAF-01. Exit 1 lists every failure.
"""
import argparse
import pathlib
import re
import sys
import xml.etree.ElementTree as ET

ROOT = pathlib.Path(__file__).resolve().parents[1]
APP = ROOT / "app/android/app/src/main"
ANDROID = "{http://schemas.android.com/apk/res/android}"

# AC-PR-01 / ADR-0002 (no content), AC-PR-04 (no location), NFR-01 / AGENTS.md §4 (nothing leaves the device yet).
FORBIDDEN_PERMISSIONS = {
    "android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_CALL_LOG",
    "android.permission.READ_CONTACTS", "android.permission.RECORD_AUDIO", "android.permission.CAMERA",
    "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION",
    "android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.QUERY_ALL_PACKAGES",
    "android.permission.INTERNET",  # shortcut: allowed only once research upload (T-M5-012) ships; remove from this set then
}
FORBIDDEN_BINDINGS = {"android.permission.BIND_ACCESSIBILITY_SERVICE", "android.permission.BIND_INPUT_METHOD"}
# TH-06: lock-screen text must not name a feeling.
FEELING_WORDS = re.compile(r"\b(stress\w*|anxi\w*|worr\w*|sad|depress\w*|lonel\w*|angry|anger|low mood|crisis|upset)\b", re.I)
# SAF-01 §4 "not allowed".
BANNED_SAFETY = re.compile(r"\b(you are depressed|pray more|weak iman|cure)\b", re.I)
SAFETY_KEYS = {"Heading": "safety_heading", "Body": "safety_body", "Emergency button": "call_emergency",
               "Safe option": "safe_for_now", "Disclaimer": "not_medical", "After safe": "offer_dua"}


def strings(path):
    """name → text from an Android strings.xml, with \\' unescaped."""
    return {e.get("name"): (e.text or "").replace("\\'", "'") for e in ET.parse(path).getroot().iter("string")}


def manifest_failures(manifest_path):
    root = ET.parse(manifest_path).getroot()
    fails = []
    for p in root.iter("uses-permission"):
        name = p.get(ANDROID + "name")
        if name in FORBIDDEN_PERMISSIONS:
            fails.append(f"forbidden permission {name}")
    for el in root.iter():
        if el.get(ANDROID + "permission") in FORBIDDEN_BINDINGS:
            fails.append(f"forbidden component binding {el.get(ANDROID + 'permission')} on {el.get(ANDROID + 'name')}")
    app = root.find("application")
    if app is None or app.get(ANDROID + "allowBackup") != "false":
        fails.append("application must set allowBackup=false (TH-01)")
    return fails


def saf01_copy(md_text):
    """Element → (English, Urdu) from the SAF-01 §4 copy table."""
    section = md_text.split("## 4. Copy", 1)[1].split("\n## ", 1)[0]
    rows = {}
    for line in section.splitlines():
        cells = [c.strip() for c in line.strip().strip("|").split("|")]
        if len(cells) == 3 and cells[0] not in ("Element", "---"):
            rows[cells[0]] = (cells[1], cells[2])
    return rows


def copy_failures(saf_md, en, ur):
    """AC-SR-04: shipped safety strings equal the signed SAF-01 copy (placeholder %1$s stands for 1122)."""
    fails = []
    for element, (want_en, want_ur) in saf01_copy(saf_md).items():
        key = SAFETY_KEYS.get(element)
        if key is None:
            continue  # helpline button: name comes from the directory at runtime
        for lang, table, want in (("en", en, want_en), ("ur", ur, want_ur)):
            got = table.get(key)
            if got is None:
                fails.append(f"{lang}: missing safety string {key}")
            elif got.replace("%1$s", "1122") != want:
                fails.append(f"{lang}: {key} = {got!r} differs from SAF-01 {want!r}")
    for lang, table in (("en", en), ("ur", ur)):
        for key in list(SAFETY_KEYS.values()):
            if key in table and BANNED_SAFETY.search(table[key]):
                fails.append(f"{lang}: {key} uses wording SAF-01 forbids")
    return fails


def ui_failures(en, main_activity_src):
    fails = []
    if FEELING_WORDS.search(en.get("notif_text", "")):
        fails.append("notif_text names a feeling (TH-06)")
    # AC-SR-02: the not-medical statement appears in onboarding, the safety screen and at least one more screen.
    if main_activity_src.count("R.string.not_medical") < 3:
        fails.append("not_medical must be shown on at least three screens (AC-SR-02)")
    return fails


def main(argv=None):
    ap = argparse.ArgumentParser()
    ap.add_argument("--merged", help="merged AndroidManifest.xml from the build")
    a = ap.parse_args(argv)
    manifest = pathlib.Path(a.merged) if a.merged else APP / "AndroidManifest.xml"
    en = strings(APP / "res/values/strings.xml")
    ur = strings(APP / "res/values-ur/strings.xml")
    saf = (ROOT / "docs/06-security-privacy-safety/safety/safety-pathway.md").read_text(encoding="utf-8")
    src = (APP / "java/org/pide/bcsr/MainActivity.kt").read_text(encoding="utf-8")
    fails = manifest_failures(manifest) + copy_failures(saf, en, ur) + ui_failures(en, src)
    if fails:
        print("Android gate checks FAILED:\n" + "\n".join(fails))
        return 1
    print(f"OK: Android gates pass ({manifest.relative_to(ROOT) if manifest.is_relative_to(ROOT) else manifest})")
    return 0


if __name__ == "__main__":
    sys.exit(main())
