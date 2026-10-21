"""Pilot P3 (AG-14): independent tests of the AG-13 bundle builder — TC-FR-15 / ADR-0005 §3."""
import copy
import itertools
import json
import pathlib
import random
import sys
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
import build_catalogue_bundle as b  # noqa: E402

STATUSES = ["draft", "in_review", "approved", "rejected"]


def item(n, status):
    return {"id": f"DUA-{n:04d}", "verification": {"status": status}}


class BundleTest(unittest.TestCase):
    def test_only_approved_items_enter_the_bundle(self):
        # TC-FR-15-01: property over 2,000 random catalogues mixing all four statuses
        rng = random.Random(15)
        for _ in range(2000):
            items = [item(n, rng.choice(STATUSES)) for n in rng.sample(range(1, 500), rng.randint(0, 12))]
            bundle, report = b.build(items, "t", "k", created="2026-01-01T00:00:00Z")
            got = bundle["items"]
            self.assertTrue(all(i["verification"]["status"] == "approved" for i in got))
            self.assertEqual(len(got), sum(i["verification"]["status"] == "approved" for i in items))
            self.assertEqual(bundle["manifest"]["item_count"], len(got))
            self.assertEqual(len(report["included"]) + len(report["excluded_not_approved"]), len(items))

    def test_items_sorted_by_id(self):
        items = [item(n, "approved") for n in (7, 2, 5)]
        ids = [i["id"] for i in b.build(items, "t", "k")[0]["items"]]
        self.assertEqual(ids, ["DUA-0002", "DUA-0005", "DUA-0007"])

    def test_digest_is_order_independent_and_detects_change(self):
        # TC-FR-15-02: same approved set in any input order → same digest; any edit → different digest
        base = [item(n, "approved") for n in (1, 2, 3)]
        digests = {b.build(list(p), "t", "k")[0]["manifest"]["items_sha256"] for p in itertools.permutations(base)}
        self.assertEqual(len(digests), 1)
        edited = copy.deepcopy(base)
        edited[1]["title"] = {"en": "changed"}
        self.assertNotIn(b.build(edited, "t", "k")[0]["manifest"]["items_sha256"], digests)

    def test_digest_matches_api_contract_test_vector(self):
        # api-contracts §2: the Kotlin verifier must reproduce this exact value
        items = [{"id": "DUA-0001", "title": {"en": "Ease", "ur": "آسانی"},
                  "verification": {"status": "approved", "record_id": "RR-0001"}},
                 {"id": "DUA-0002", "repetitions": 3, "note": 'line\nbreak "q"'}]
        self.assertEqual(b.items_digest(items), "8fed260e4d68a9f7ff9263d63e9550a8aa0e2c8600db8fa16496cbf5ca036549")

    def test_manifest_fields(self):
        m = b.build([], "2026.1", "key-x", created="2026-01-01T00:00:00Z")[0]["manifest"]
        self.assertEqual(set(m), {"version", "created", "item_count", "items_sha256", "key_id"})
        self.assertEqual((m["version"], m["key_id"], m["item_count"]), ("2026.1", "key-x", 0))

    def test_invalid_catalogue_builds_nothing(self):
        # TC-FR-15-03: validator errors stop the build (exit 1, no file)
        out = ROOT / "build" / "test-bundle"
        orig = b.vc.validate
        b.vc.validate = lambda *a, **k: ["DUA-0001.json: forced error"]
        try:
            self.assertEqual(b.main(["--version", "x", "--out", str(out)]), 1)
            self.assertFalse((out / "catalogue-x.json").exists())
        finally:
            b.vc.validate = orig

    def test_real_catalogue_smoke(self):
        # Smoke test only: today's catalogue has no approved items, so this mainly checks the build runs end to end.
        out = ROOT / "build" / "test-bundle"
        self.assertEqual(b.main(["--version", "y", "--out", str(out)]), 0)
        bundle = json.loads((out / "catalogue-y.json").read_text(encoding="utf-8"))
        self.assertTrue(all(i["verification"]["status"] == "approved" for i in bundle["items"]))


if __name__ == "__main__":
    unittest.main()
