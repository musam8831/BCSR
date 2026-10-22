#!/usr/bin/env python3
"""Static privacy and safety gate checks for the Android app (T-M5-013; TST-01 §2: never skipped).

  python scripts/check_android_gates.py [--merged MANIFEST ...]

Checks the app manifest (the merged ones from the build when given, so library-added permissions count too),
data-extraction rules, the shipped strings, the notification code and the safety copy against the signed SAF-01.
Exit 1 lists every failure.
"""
import argparse
import hashlib
import pathlib
import re
import sys
import xml.etree.ElementTree as ET

ROOT = pathlib.Path(__file__).resolve().parents[1]
APP = ROOT / "app/android/app/src/main"
SAF01 = ROOT / "docs/06-security-privacy-safety/safety/safety-pathway.md"
ANDROID = "{http://schemas.android.com/apk/res/android}"

# AC-PR-01 / ADR-0002 (no content), AC-PR-04 (no location), NFR-01 / AGENTS.md §4 (nothing leaves the device yet).
FORBIDDEN_PERMISSIONS = {
    "android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_CALL_LOG",
    "android.permission.READ_CONTACTS", "android.permission.RECORD_AUDIO", "android.permission.CAMERA",
    "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION",
    "android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.QUERY_ALL_PACKAGES",
    "android.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION",
    # shortcut: no network at all until research upload (T-M5-012); then REPLACE this with a scoped check
    # (network only in the research module, behind PR-03 consent, AG-09 review) — never just delete it.
    "android.permission.INTERNET",
}
# Notification listener counts arrive with sensing (T-M5-003); until then any binding is a regression.
FORBIDDEN_BINDINGS = {"android.permission.BIND_ACCESSIBILITY_SERVICE", "android.permission.BIND_INPUT_METHOD",
                      "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE"}
# TH-06: lock-screen text must not name a feeling (English and Urdu).
FEELING_WORDS = re.compile(r"\b(stress\w*|anxi\w*|worr\w*|sad|depress\w*|lonel\w*|angry|anger|mood|crisis|upset)\b"
                           r"|پریشان|اداس|غصہ|تنہا|ڈپریشن|بحران", re.I)
# SAF-01 §4 "not allowed" (English and Urdu).
BANNED_SAFETY = re.compile(r"\b(you are depressed|pray more|weak iman|cure)\b|کمزور ایمان|زیادہ نماز|علاج", re.I)
SAFETY_KEYS = {"Heading": "safety_heading", "Body": "safety_body", "Emergency button": "call_emergency",
               "Safe option": "safe_for_now", "Disclaimer": "not_medical", "After safe": "offer_dua"}
EXPECTED_ELEMENTS = set(SAFETY_KEYS) | {"Helpline button"}
# SHA-256 of the SAF-01 §4 copy table as signed in SAF-01 §6 (v1.0, Project Lead, PR #18).
# If §4 changes, the safety copy needs a new signature (SAF-01 §5): re-sign, then update this value.
SIGNED_COPY_SHA256 = "e01c11c8cf2ab269504cf8d4c062154fa6604b579c92669d54053de43a1814c6"
# AC-SR-02: screens that must state "not a medical service". There is no About screen yet; Privacy stands in for it.
NOT_MEDICAL_SCREENS = ("Onboarding", "SafetyScreen", "Privacy")


def strings(path):
    """name → text from an Android strings.xml, with \\' unescaped."""
    return {e.get("name"): (e.text or "").replace("\\'", "'") for e in ET.parse(path).getroot().iter("string")}


def manifest_failures(manifest_path, extraction_rules=None):
    root = ET.parse(manifest_path).getroot()
    fails = []
    for tag in ("uses-permission", "uses-permission-sdk-23"):
        for p in root.iter(tag):
            if p.get(ANDROID + "name") in FORBIDDEN_PERMISSIONS:
                fails.append(f"forbidden permission {p.get(ANDROID + 'name')} ({tag})")
    for el in root.iter():
        if el.get(ANDROID + "permission") in FORBIDDEN_BINDINGS:
            fails.append(f"forbidden component binding {el.get(ANDROID + 'permission')} on {el.get(ANDROID + 'name')}")
    app = root.find("application")
    if app is None or app.get(ANDROID + "allowBackup") != "false":
        fails.append("application must set allowBackup=false (TH-01)")
    if app is None or not app.get(ANDROID + "dataExtractionRules"):
        fails.append("application must set dataExtractionRules (TH-01, Android 12+ device transfer)")
    if extraction_rules is not None:
        fails += extraction_failures(extraction_rules)
    return fails


def extraction_failures(rules_path):
    """TH-01: cloud backup and device transfer must both exclude every domain the app stores data in."""
    root = ET.parse(rules_path).getroot()
    fails = []
    for section in ("cloud-backup", "device-transfer"):
        el = root.find(section)
        excluded = {x.get("domain") for x in el.iter("exclude")} if el is not None else set()
        missing = {"root", "file", "database", "sharedpref"} - excluded
        if missing:
            fails.append(f"data_extraction_rules {section} does not exclude {sorted(missing)}")
    return fails


def copy_section(md_text):
    return md_text.split("## 4. Copy", 1)[1].split("\n## ", 1)[0]


def copy_hash(md_text):
    """SHA-256 of the §4 table rows, whitespace-normalised, so only wording changes alter it."""
    rows = [" ".join(l.split()) for l in copy_section(md_text).splitlines() if l.strip().startswith("|")]
    return hashlib.sha256("\n".join(rows).encode("utf-8")).hexdigest()


def saf01_copy(md_text):
    """Element → (English, Urdu) from the SAF-01 §4 copy table."""
    rows = {}
    for line in copy_section(md_text).splitlines():
        cells = [c.strip() for c in line.strip().strip("|").split("|")]
        if len(cells) == 3 and cells[0] not in ("Element", "---"):
            rows[cells[0]] = (cells[1], cells[2])
    return rows


def copy_failures(saf_md, en, ur, signed_hash=None):
    """AC-SR-04: shipped safety strings equal the signed SAF-01 copy (placeholder %1$s stands for 1122)."""
    fails = []
    signed_hash = SIGNED_COPY_SHA256 if signed_hash is None else signed_hash
    if copy_hash(saf_md) != signed_hash:
        fails.append("SAF-01 §4 copy differs from the signed version: re-sign (SAF-01 §5/§6) and update SIGNED_COPY_SHA256")
    rows = saf01_copy(saf_md)
    missing = EXPECTED_ELEMENTS - set(rows)
    if missing:
        fails.append(f"SAF-01 §4 table missing elements {sorted(missing)} (table shape changed?)")
    for element, key in SAFETY_KEYS.items():
        if element not in rows:
            continue
        want_en, want_ur = rows[element]
        for lang, table, want in (("en", en, want_en), ("ur", ur, want_ur)):
            got = table.get(key)
            if got is None:
                fails.append(f"{lang}: missing safety string {key}")
            elif got.replace("%1$s", "1122") != want:
                fails.append(f"{lang}: {key} = {got!r} differs from SAF-01 {want!r}")
    for lang, table in (("en", en), ("ur", ur)):
        for key in SAFETY_KEYS.values():
            if key in table and BANNED_SAFETY.search(table[key]):
                fails.append(f"{lang}: {key} uses wording SAF-01 forbids")
    return fails


def composable_body(src, name):
    """Source of `private fun <name>(` up to the next top-level declaration (raw text)."""
    m = re.search(rf"^private fun {name}\(.*?(?=^(?:@Composable|private |fun |class |object |sealed |// -)|\Z)", src, re.M | re.S)
    if not m:
        return None
    return m.group(0)


def without_comments(src):
    return "\n".join(l.split("//", 1)[0] for l in src.splitlines())


def ui_failures(en, ur, main_activity_src, checkins_src):
    fails = []
    for lang, table in (("en", en), ("ur", ur)):
        if FEELING_WORDS.search(table.get("notif_text", "")):
            fails.append(f"{lang}: notif_text names a feeling (TH-06)")
    if "VISIBILITY_PRIVATE" not in checkins_src:
        fails.append("check-in notification must be VISIBILITY_PRIVATE (TH-06)")
    for screen in NOT_MEDICAL_SCREENS:
        body = composable_body(main_activity_src, screen)
        if body is None:
            fails.append(f"screen {screen} not found (AC-SR-02)")
        elif "R.string.not_medical" not in without_comments(body):
            fails.append(f"screen {screen} does not show not_medical (AC-SR-02)")
    return fails


def main(argv=None):
    ap = argparse.ArgumentParser()
    ap.add_argument("--merged", nargs="*", default=[], help="merged AndroidManifest.xml files from the build")
    a = ap.parse_args(argv)
    manifests = [pathlib.Path(m) for m in a.merged] or [APP / "AndroidManifest.xml"]
    rules = APP / "res/xml/data_extraction_rules.xml"
    en = strings(APP / "res/values/strings.xml")
    ur = strings(APP / "res/values-ur/strings.xml")
    saf = SAF01.read_text(encoding="utf-8")
    src = (APP / "java/org/pide/bcsr/MainActivity.kt").read_text(encoding="utf-8")
    chk = (APP / "java/org/pide/bcsr/CheckIns.kt").read_text(encoding="utf-8")
    fails = [f"{m.name}: {f}" for m in manifests for f in manifest_failures(m)]
    fails += extraction_failures(rules) + copy_failures(saf, en, ur) + ui_failures(en, ur, src, chk)
    if fails:
        print("Android gate checks FAILED:\n" + "\n".join(fails))
        return 1
    print(f"OK: Android gates pass ({len(manifests)} manifest(s))")
    return 0


if __name__ == "__main__":
    sys.exit(main())
