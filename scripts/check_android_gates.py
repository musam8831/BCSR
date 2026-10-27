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
               "Safe option": "safe_for_now", "Disclaimer": "not_medical", "After safe": "offer_dua",
               # v1.1 (T-M5-025): Locked screen
               "Locked title": "locked_title", "Locked body": "locked_body", "Locked retry": "locked_retry_body",
               "Try again": "try_again", "Start fresh": "start_fresh", "Start fresh confirm": "start_fresh_confirm"}
EXPECTED_ELEMENTS = set(SAFETY_KEYS) | {"Helpline button"}
# SHA-256 of the SAF-01 §4 copy table as signed in SAF-01 §6 (v1.1, Project Lead, 2026-10-23; v1.0 was PR #18).
# If §4 changes, the safety copy needs a new signature (SAF-01 §5): re-sign, then update this value.
SIGNED_COPY_SHA256 = "af453e97d4884e315e01aadeb2b84e7cb6a45d8f6c7a578e783074ce3c851aa6"
# AC-SR-02: screens that must state "not a medical service" (onboarding, safety, about).
NOT_MEDICAL_SCREENS = ("Onboarding", "SafetyScreen", "About", "Locked")  # Locked: store unreadable (T-M5-025)
# Tanzil notice lines (tanzil.net/docs/text_license, 2026-10-22) that must be shown verbatim (AC-CR-07, RES-03 §3.2).
# AC-SR-03: the only allowed ways into the safety screen ("I need help now", check-in "not safe").
# Each must be the button's own label; any other reference to Screen.Safety, in any app source file, fails.
SAFETY_ENTRY = re.compile(r"^\s*(?:Outlined)?Button\(\{ go\(Screen\.Safety\) \}, Modifier\.fillMaxWidth\(\)\) "
                          r"\{ Text\(stringResource\(R\.string\.(need_help|state_not_safe)\)\) \}\s*$")
SAFETY_DISPATCH = re.compile(r"^\s*Screen\.Safety -> SafetyScreen\(c, go\)\s*$")
SAFETY_DEF = re.compile(r"^private fun SafetyScreen\(")
MAIN_ACTIVITY = "main/java/org/pide/bcsr/MainActivity.kt"  # the only file allowed to hold the dispatch
SAFETY_INTENT =re.compile(r'putExtra\(\s*"[^"]*safety|EXTRA_\w*SAFETY', re.I)
SCREEN_CAPTURE = re.compile(r"MediaProjection|createScreenCaptureIntent")
TANZIL_NOTICE = ("Tanzil Quran Text", "Copyright (C) 2007-2021 Tanzil Project", "License: Creative Commons Attribution 3.0")


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
    src = re.sub(r"/\*.*?\*/", "", src, flags=re.S)  # Kotlin block comments (nesting not handled; none in this code)
    return re.sub(r"(?<!:)//.*", "", src)  # line comments, but not the // in a URL like https://


def source_failures(sources):
    """AC-SR-03 and AC-PR-01 over every app source file: path relative to app/src → text."""
    fails, entries, dispatch = [], [], 0
    for name, src in sources.items():
        for line in without_comments(src).splitlines():
            if SCREEN_CAPTURE.search(line):
                fails.append(f"{name}: screen-capture API (AC-PR-01): {line.strip()}")
            if SAFETY_INTENT.search(line):
                fails.append(f"{name}: intent extra opens the safety pathway (AC-SR-03): {line.strip()}")
            # any mention (call, spaced call, ::reference) other than its definition and the dispatch
            if re.search(r"\bSafetyScreen\b", line) and not SAFETY_DEF.match(line) and not SAFETY_DISPATCH.match(line):
                fails.append(f"{name}: SafetyScreen used outside the dispatch (AC-SR-03): {line.strip()}")
            if "Screen.Safety" not in line:
                continue
            if name == MAIN_ACTIVITY and SAFETY_DISPATCH.match(line):
                dispatch += 1
            elif m := SAFETY_ENTRY.match(line):
                entries.append(m.group(1))
            else:
                fails.append(f"{name}: safety pathway opened outside an explicit control (AC-SR-03): {line.strip()}")
    if sorted(entries) != ["need_help", "state_not_safe"] or dispatch != 1:
        fails.append(f"safety entries must be exactly need_help and state_not_safe plus one dispatch (AC-SR-03); found {sorted(entries)}, dispatch {dispatch}")
    return fails


def ui_failures(en, ur, main_activity_src, checkins_src):
    fails = []
    for lang, table in (("en", en), ("ur", ur)):
        if FEELING_WORDS.search(table.get("notif_text", "")):
            fails.append(f"{lang}: notif_text names a feeling (TH-06)")
    if not re.search(r"\.setVisibility\(\s*NotificationCompat\.VISIBILITY_PRIVATE\s*\)", without_comments(checkins_src)):
        fails.append("check-in notification must be VISIBILITY_PRIVATE (TH-06)")
    for screen in NOT_MEDICAL_SCREENS:
        body = composable_body(main_activity_src, screen)
        if body is None:
            fails.append(f"screen {screen} not found (AC-SR-02)")
        elif "R.string.not_medical" not in without_comments(body):
            fails.append(f"screen {screen} does not show not_medical (AC-SR-02)")
    # AC-SR-01 / T-M5-025: helplines on the safety screen and on the locked screen shown when the store cannot be opened.
    for screen in ("SafetyScreen", "Locked"):
        if "HelplineButtons()" not in without_comments(composable_body(main_activity_src, screen) or ""):
            fails.append(f"screen {screen} must show HelplineButtons() (AC-SR-01)")
    # SAF-01 §1.1 / §3 (AG-10 R-13): help comes before "I'm safe for now" and before any du'a on the safety screen
    safety = without_comments(composable_body(main_activity_src, "SafetyScreen") or "")
    h = safety.find("HelplineButtons()")
    if h < 0 or any(0 <= safety.find(x) < h for x in ("R.string.safe_for_now", "R.string.offer_dua", "ItemCard(")):
        fails.append("SafetyScreen must show HelplineButtons() before safe_for_now and any du'a (SAF-01 §3)")
    buttons = without_comments(composable_body(main_activity_src, "HelplineButtons") or "")
    if not all(x in buttons for x in ("shownHelplines(", "Intent.ACTION_DIAL", "R.string.call_emergency", "R.string.talk_to")):
        fails.append("HelplineButtons must dial the emergency and helpline numbers from shownHelplines (AC-SR-01)")
    # AC-CR-07: the About screen names and links the Tanzil Project (RES-03 §3.2).
    about = composable_body(main_activity_src, "About")
    code = without_comments(about or "")
    notice = en.get("tanzil_notice", "").replace("\\n", "\n")
    if about is None or "Tanzil" not in en.get("credit_tanzil", "") or "R.string.credit_tanzil" not in code \
            or 'Uri.parse("https://tanzil.net")' not in code:
        fails.append("About screen must show the Tanzil attribution and link (AC-CR-07)")
    if not all(line in notice.splitlines() for line in TANZIL_NOTICE) or "R.string.tanzil_notice" not in code:
        fails.append("About screen must show the Tanzil copyright notice and CC BY 3.0 licence (RES-03 §3.2)")
    if "go(Screen.About)" not in without_comments(composable_body(main_activity_src, "Home") or ""):
        fails.append("About screen must be reachable from Home (AC-SR-02, AC-CR-07)")
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
    # every source set (main, debug, release; java/ and kotlin/), keyed by path so same-named files both count
    kt = {f.relative_to(APP.parent).as_posix(): f.read_text(encoding="utf-8") for f in sorted(APP.parent.rglob("*.kt"))
          if f.relative_to(APP.parent).parts[0] not in ("test", "androidTest")}
    fails += extraction_failures(rules) + copy_failures(saf, en, ur) + ui_failures(en, ur, src, chk) + source_failures(kt)
    if fails:
        print("Android gate checks FAILED:\n" + "\n".join(fails))
        return 1
    print(f"OK: Android gates pass ({len(manifests)} manifest(s))")
    return 0


if __name__ == "__main__":
    sys.exit(main())
