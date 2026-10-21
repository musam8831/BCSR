"""T-M4-006: Ed25519 sign/verify round trip, tamper and wrong-key detection, private key never inside the repo."""
import json
import pathlib
import sys
import tempfile
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
import build_catalogue_bundle as b  # noqa: E402
import sign_bundle as s  # noqa: E402


class SignBundleTest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.d = pathlib.Path(self.tmp.name)  # outside the repo
        self.priv, self.pub = self.d / "k.pem", self.d / "k.pub"
        s.keygen("test-key", self.priv, self.pub)
        items = [{"id": "DUA-0001", "verification": {"status": "approved"}}]
        bundle, _ = b.build(items, "t", "test-key", created="2026-01-01T00:00:00Z")
        self.bundle = self.d / "catalogue-t.json"
        self.bundle.write_text(json.dumps(bundle, ensure_ascii=False, indent=1), encoding="utf-8")

    def tearDown(self):
        self.tmp.cleanup()

    def test_round_trip(self):
        s.sign(self.bundle, self.priv)
        self.assertEqual(s.verify(self.bundle, self.pub), [])

    def test_any_byte_change_breaks_the_signature(self):
        s.sign(self.bundle, self.priv)
        self.bundle.write_text(self.bundle.read_text(encoding="utf-8").replace("approved", "approvee"), encoding="utf-8")
        self.assertIn("signature does not match the bundle bytes", s.verify(self.bundle, self.pub))

    def test_wrong_key_is_rejected(self):
        s.sign(self.bundle, self.priv)
        other_priv, other_pub = self.d / "o.pem", self.d / "o.pub"
        s.keygen("test-key", other_priv, other_pub)
        self.assertIn("signature does not match the bundle bytes", s.verify(self.bundle, other_pub))

    def test_missing_signature(self):
        self.assertEqual(s.verify(self.bundle, self.pub), ["signature file missing"])

    def test_signed_but_inconsistent_manifest_is_rejected(self):
        # A correctly signed bundle whose manifest disagrees with its items still fails (ADR-0005 §5).
        data = json.loads(self.bundle.read_text(encoding="utf-8"))
        data["items"].append({"id": "DUA-0002", "verification": {"status": "draft"}})
        self.bundle.write_text(json.dumps(data), encoding="utf-8")
        s.sign(self.bundle, self.priv)
        errs = s.verify(self.bundle, self.pub)
        self.assertTrue(any("item_count" in e for e in errs))
        self.assertTrue(any("non-approved" in e for e in errs))

    def test_key_id_must_match(self):
        s.sign(self.bundle, self.priv)
        k2 = self.d / "k2.pub"  # same key bytes, different key_id
        k2.write_text("other-id " + self.pub.read_text(encoding="utf-8").split()[1], encoding="utf-8")
        self.assertTrue(any("key_id" in e for e in s.verify(self.bundle, k2)))

    def test_private_key_inside_repo_is_refused(self):
        with self.assertRaises(SystemExit):
            s.keygen("x", ROOT / "build" / "never.pem", self.d / "x.pub")
        self.assertFalse((ROOT / "build" / "never.pem").exists())
        with self.assertRaises(SystemExit):
            s.sign(self.bundle, ROOT / "build" / "never.pem")


if __name__ == "__main__":
    unittest.main()
