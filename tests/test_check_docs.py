"""T-M3-007: the docs check catches broken links and unknown or untraced requirement IDs."""
import pathlib
import sys
import tempfile
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
import check_docs as cd  # noqa: E402


class CheckDocsTest(unittest.TestCase):
    def test_broken_and_good_links(self):
        with tempfile.TemporaryDirectory() as d:
            root = pathlib.Path(d)
            (root / "ok.md").write_text("x", encoding="utf-8")
            page = root / "page.md"
            text = "[a](ok.md) [b](missing.md) [c](https://x.org) [d](#top) [e](ok.md#s) [f](mailto:a@b.c)"
            old = cd.ROOT
            cd.ROOT = root
            try:
                errs = cd.broken_links(page, text)
            finally:
                cd.ROOT = old
            self.assertEqual(len(errs), 1)
            self.assertIn("missing.md", errs[0])

    def test_cited_ids_expand_ranges(self):
        ids = cd.cited_ids("see FR-03..FR-05, SR-01..05 and NFR-09; not FR-3 or PR #1")
        self.assertEqual(ids, {"FR-03", "FR-04", "FR-05", "SR-01", "SR-02", "SR-03", "SR-04", "SR-05", "NFR-09"})

    def test_srs_ids_read_only_the_catalogue(self):
        srs = "| FR-99 | early mention |\n## 5. Requirements catalogue\n| FR-01 | m | text | M |\n## 5.6 Log\n| FR-50 | x |\n"
        self.assertEqual(cd.srs_ids(srs), {"FR-01"})

    def test_repository_passes(self):
        self.assertEqual(cd.main(), 0)


if __name__ == "__main__":
    unittest.main()
