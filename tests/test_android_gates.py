"""T-M5-013: each static gate fails on a bad input and passes on the real app."""
import pathlib
import sys
import tempfile
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
import check_android_gates as g  # noqa: E402

GOOD = """<manifest xmlns:android="http://schemas.android.com/apk/res/android">
  <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
  <application android:allowBackup="false"/>
</manifest>"""


def manifest(text):
    f = tempfile.NamedTemporaryFile("w", suffix=".xml", delete=False, encoding="utf-8")
    f.write(text)
    f.close()
    return f.name


class AndroidGatesTest(unittest.TestCase):
    def test_real_app_passes(self):
        self.assertEqual(g.main([]), 0)

    def test_good_manifest_passes(self):
        self.assertEqual(g.manifest_failures(manifest(GOOD)), [])

    def test_forbidden_permissions_fail(self):
        for perm in ("android.permission.INTERNET", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_SMS"):
            bad = GOOD.replace("POST_NOTIFICATIONS", perm.split(".")[-1])
            self.assertTrue(any(perm in f for f in g.manifest_failures(manifest(bad))), perm)

    def test_accessibility_service_fails(self):
        bad = GOOD.replace('<application android:allowBackup="false"/>',
                           '<application android:allowBackup="false"><service android:name=".Spy" '
                           'android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"/></application>')
        self.assertTrue(any("BIND_ACCESSIBILITY_SERVICE" in f for f in g.manifest_failures(manifest(bad))))

    def test_backup_must_be_off(self):
        self.assertTrue(g.manifest_failures(manifest(GOOD.replace('"false"', '"true"'))))

    def test_safety_copy_drift_fails(self):
        saf = (ROOT / "docs/06-security-privacy-safety/safety/safety-pathway.md").read_text(encoding="utf-8")
        en = g.strings(g.APP / "res/values/strings.xml")
        ur = g.strings(g.APP / "res/values-ur/strings.xml")
        self.assertEqual(g.copy_failures(saf, en, ur), [])
        en2 = dict(en, safety_heading="Just pray more.")
        fails = g.copy_failures(saf, en2, ur)
        self.assertTrue(any("safety_heading" in f and "differs" in f for f in fails))
        self.assertTrue(any("forbids" in f for f in fails))

    def test_notification_naming_a_feeling_fails(self):
        self.assertTrue(g.ui_failures({"notif_text": "Feeling stressed?"}, "R.string.not_medical" * 3))
        self.assertEqual(g.ui_failures({"notif_text": "A moment for yourself? How are you?"}, "R.string.not_medical" * 3), [])

    def test_not_medical_on_three_screens(self):
        self.assertTrue(g.ui_failures({"notif_text": "ok"}, "R.string.not_medical"))


if __name__ == "__main__":
    unittest.main()
