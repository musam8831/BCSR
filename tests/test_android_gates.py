"""T-M5-013: each static gate fails on a bad input and passes on the real app (incl. AG-16 round-1 findings)."""
import pathlib
import sys
import tempfile
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
import check_android_gates as g  # noqa: E402

GOOD = """<manifest xmlns:android="http://schemas.android.com/apk/res/android">
  <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
  <application android:allowBackup="false" android:dataExtractionRules="@xml/data_extraction_rules"/>
</manifest>"""
SAF = g.SAF01.read_text(encoding="utf-8")
EN = g.strings(g.APP / "res/values/strings.xml")
UR = g.strings(g.APP / "res/values-ur/strings.xml")
SRC = (g.APP / "java/org/pide/bcsr/MainActivity.kt").read_text(encoding="utf-8")
CHK = (g.APP / "java/org/pide/bcsr/CheckIns.kt").read_text(encoding="utf-8")


class AndroidGatesTest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()

    def tearDown(self):
        self.tmp.cleanup()

    def xml(self, text):
        p = pathlib.Path(self.tmp.name) / f"m{len(list(pathlib.Path(self.tmp.name).iterdir()))}.xml"
        p.write_text(text, encoding="utf-8")
        return p

    def test_real_app_passes(self):
        self.assertEqual(g.main([]), 0)

    def test_good_manifest_passes(self):
        self.assertEqual(g.manifest_failures(self.xml(GOOD)), [])

    def test_forbidden_permissions_fail(self):
        for perm in ("android.permission.INTERNET", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_SMS"):
            bad = GOOD.replace("android.permission.POST_NOTIFICATIONS", perm)
            self.assertTrue(any(perm in f for f in g.manifest_failures(self.xml(bad))), perm)

    def test_sdk23_permission_tag_is_scanned(self):
        # AG-16 major 3
        bad = GOOD.replace("<uses-permission ", '<uses-permission-sdk-23 android:name="android.permission.INTERNET"/>\n  <uses-permission ')
        self.assertTrue(any("uses-permission-sdk-23" in f for f in g.manifest_failures(self.xml(bad))))

    def test_forbidden_bindings_fail(self):
        for binding in ("BIND_ACCESSIBILITY_SERVICE", "BIND_NOTIFICATION_LISTENER_SERVICE"):
            bad = GOOD.replace("/>\n</manifest>", f'><service android:name=".X" android:permission="android.permission.{binding}"/></application>\n</manifest>')
            self.assertTrue(any(binding in f for f in g.manifest_failures(self.xml(bad))), binding)

    def test_backup_and_extraction_rules(self):
        self.assertTrue(g.manifest_failures(self.xml(GOOD.replace('allowBackup="false"', 'allowBackup="true"'))))
        self.assertTrue(g.manifest_failures(self.xml(GOOD.replace(' android:dataExtractionRules="@xml/data_extraction_rules"', ""))))
        self.assertEqual(g.extraction_failures(g.APP / "res/xml/data_extraction_rules.xml"), [])
        weak = self.xml("<data-extraction-rules><cloud-backup><exclude domain='root'/></cloud-backup></data-extraction-rules>")
        self.assertEqual(len(g.extraction_failures(weak)), 2)

    def test_safety_copy_matches_signed_version(self):
        self.assertEqual(g.copy_failures(SAF, EN, UR), [])

    def test_string_drift_and_forbidden_wording_fail(self):
        fails = g.copy_failures(SAF, dict(EN, safety_heading="Just pray more."), UR)
        self.assertTrue(any("safety_heading" in f and "differs" in f for f in fails))
        self.assertTrue(any("forbids" in f for f in fails))
        self.assertTrue(any("ur:" in f and "forbids" in f for f in g.copy_failures(SAF, EN, dict(UR, safety_body="کمزور ایمان"))))

    def test_editing_saf01_without_resigning_fails(self):
        # AG-16 major 1: doc and strings changed together must still fail until re-signed
        edited = SAF.replace("I'm safe for now", "I am fine")
        fails = g.copy_failures(edited, dict(EN, safe_for_now="I am fine"), UR)
        self.assertTrue(any("re-sign" in f for f in fails))

    def test_table_shape_change_cannot_silently_pass(self):
        # AG-16 major 1: a reshaped table must not yield zero comparisons
        reshaped = SAF.replace("| Element | English | Urdu |", "| Element | English | Urdu | Note |")
        reshaped = "\n".join(l + " x |" if l.startswith("| ") and "Element" not in l and "---" not in l and "## 4" in reshaped.split(l)[0][-2000:] + "## 4" else l for l in reshaped.splitlines())
        fails = g.copy_failures(reshaped, EN, UR, signed_hash=g.copy_hash(reshaped))
        self.assertTrue(any("missing elements" in f for f in fails))

    def test_not_medical_checked_per_screen(self):
        # AG-16 major 2: removing it from the safety screen alone must fail, even if it stays elsewhere
        body = g.composable_body(SRC, "SafetyScreen")
        stripped = SRC.replace(body, body.replace("R.string.not_medical", "R.string.app_name"))
        fails = g.ui_failures(EN, UR, stripped, CHK)
        self.assertEqual(fails, ["screen SafetyScreen does not show not_medical (AC-SR-02)"])
        commented = SRC.replace(body, body.replace("Text(stringResource(R.string.not_medical)", "// Text(stringResource(R.string.not_medical)"))
        self.assertTrue(g.ui_failures(EN, UR, commented, CHK))
        block = SRC.replace(body, body.replace("Text(stringResource(R.string.not_medical)", "/* Text(stringResource(R.string.not_medical) */ Text(\"x\""))
        self.assertTrue(g.ui_failures(EN, UR, block, CHK))  # AG-16 round 2: block comments

    def test_tanzil_attribution_required(self):
        body = g.composable_body(SRC, "About")
        self.assertIsNotNone(body)
        self.assertTrue(g.ui_failures(EN, UR, SRC.replace(body, body.replace("R.string.credit_tanzil", "R.string.app_name")), CHK))
        self.assertTrue(g.ui_failures(dict(EN, credit_tanzil="Thanks"), UR, SRC, CHK))
        # AG-16: copyright notice and licence are required, not just the name and link
        self.assertTrue(any("copyright notice" in f for f in g.ui_failures(dict(EN, tanzil_notice="Tanzil Quran Text"), UR, SRC, CHK)))
        no_notice = SRC.replace(body, body.replace("R.string.tanzil_notice", "R.string.app_name"))
        self.assertTrue(any("copyright notice" in f for f in g.ui_failures(EN, UR, no_notice, CHK)))
        # the link must be the real call, not just a label
        no_link = SRC.replace(body, body.replace('Uri.parse("https://tanzil.net")', 'Uri.parse("https://example.org")'))
        self.assertTrue(any("attribution and link" in f for f in g.ui_failures(EN, UR, no_link, CHK)))
        # reachable from Home
        self.assertTrue(any("reachable" in f for f in g.ui_failures(EN, UR, SRC.replace("go(Screen.About)", "go(Screen.Home)"), CHK)))

    def test_safety_opens_only_from_explicit_controls(self):
        # AC-SR-03
        auto = SRC.replace("Screen.Safety -> SafetyScreen(c, go)", "Screen.Safety -> SafetyScreen(c, go)\n            else -> if (c.recommender == null) go(Screen.Safety)")
        self.assertTrue(any("AC-SR-03" in f for f in g.ui_failures(EN, UR, auto, CHK)))
        relabelled = SRC.replace("{ go(Screen.Safety) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.need_help))",
                                 "{ go(Screen.Safety) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.app_name))")
        self.assertTrue(any("AC-SR-03" in f for f in g.ui_failures(EN, UR, relabelled, CHK)))
        self.assertTrue(any("AC-SR-03" in f for f in g.ui_failures(EN, UR, SRC, CHK + "\nfun x(go: (Screen) -> Unit) = go(Screen.Safety)\n")))

    def test_notification_rules(self):
        self.assertEqual(g.ui_failures(EN, UR, SRC, CHK), [])
        self.assertTrue(g.ui_failures(dict(EN, notif_text="Feeling stressed?"), UR, SRC, CHK))
        self.assertTrue(g.ui_failures(EN, dict(UR, notif_text="کیا آپ پریشان ہیں؟"), SRC, CHK))
        self.assertTrue(g.ui_failures(EN, UR, SRC, CHK.replace("VISIBILITY_PRIVATE", "VISIBILITY_PUBLIC")))
        # AG-16 round 2: a comment mentioning it must not count once the real call is gone
        gone = CHK.replace(".setVisibility(NotificationCompat.VISIBILITY_PRIVATE)", "// .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)")
        self.assertTrue(g.ui_failures(EN, UR, SRC, gone))


if __name__ == "__main__":
    unittest.main()
