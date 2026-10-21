"""T-M3-005 acceptance: the validator rejects invalid and unapproved-but-claimed items."""
import copy
import json
import pathlib
import sys
import tempfile
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "content/validation"))
from validate_catalogue import validate, AYAH_COUNTS  # noqa: E402

SCHEMA = json.loads((ROOT / "content/schema/catalogue.schema.json").read_text(encoding="utf-8"))

DRAFT = {
    "id": "DUA-0101", "type": "hadith_dua",
    "title": {"en": "t", "ur": "t"}, "arabic": "TO_BE_ENTERED_FROM_SOURCE",
    "transliteration": {"en": "x"}, "translation": {"en": "x", "ur": "x", "source": "TBD"},
    "reference": {"kind": "hadith", "collection": "Sahih al-Bukhari", "number": "1", "numbering_system": "x",
                  "grading": "sahih", "grader": "x"},
    "context_note": {"en": "x", "ur": "x"}, "states": ["CS-02"], "verification": {"status": "draft"},
}


def approved(**over):
    item = copy.deepcopy(DRAFT)
    item.update(arabic="[test text]", translation={"en": "e", "ur": "u", "source": "Edition X"})
    item["verification"] = {"status": "approved", "reviewer": "R", "date": "2026-11-20", "record_id": "RR-0101"}
    item.update(over)
    return item


class ValidatorTest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.rr = pathlib.Path(self.tmp.name)

    def tearDown(self):
        self.tmp.cleanup()

    def run_v(self, *items):
        return validate([(f"{i['id']}.json", i) for i in items], self.rr, SCHEMA)

    def record(self, text):
        (self.rr / "RR-0101.md").write_text(text, encoding="utf-8")

    def test_draft_with_placeholders_is_valid(self):
        self.assertEqual(self.run_v(DRAFT), [])

    def test_approved_with_good_record_is_valid(self):
        self.record("Item: DUA-0101\nReviewer: Named Scholar (qualification)\n")
        self.assertEqual(self.run_v(approved()), [])

    def test_approved_without_record_is_rejected(self):
        self.assertTrue(any("without review record" in e for e in self.run_v(approved())))

    def test_record_without_reviewer_or_item_is_rejected(self):
        self.record("Notes only\n")
        errs = self.run_v(approved())
        self.assertTrue(any("names no reviewer" in e for e in errs))
        self.assertTrue(any("does not cite DUA-0101" in e for e in errs))

    def test_approved_with_placeholder_is_rejected(self):
        self.record("Item: DUA-0101\nReviewer: R\n")
        self.assertTrue(any("placeholder" in e for e in self.run_v(approved(arabic="TO_BE_ENTERED_FROM_SOURCE"))))

    def test_approved_missing_reviewer_field_fails_schema(self):
        item = approved()
        del item["verification"]["reviewer"]
        self.assertTrue(any(e.startswith("DUA-0101.json: schema") for e in self.run_v(item)))

    def test_daif_grading_fails_schema(self):
        item = copy.deepcopy(DRAFT)
        item["reference"]["grading"] = "daif"
        self.assertTrue(any("schema" in e for e in self.run_v(item)))

    def test_quran_range_is_checked(self):
        item = copy.deepcopy(DRAFT)
        item.update(type="quran", reference={"kind": "quran", "surah": 94, "ayah_from": 5, "ayah_to": 9})
        self.assertTrue(any("surah 94 has 8 ayat" in e for e in self.run_v(item)))
        item["reference"].update(ayah_from=6, ayah_to=5)
        self.assertTrue(any("before ayah_from" in e for e in self.run_v(item)))

    def test_type_and_reference_kind_must_agree(self):
        item = copy.deepcopy(DRAFT)
        item["type"] = "quran"
        self.assertTrue(any("needs a Qur'an reference" in e for e in self.run_v(item)))

    def test_duplicates_are_rejected(self):
        b = copy.deepcopy(DRAFT)
        b["id"] = "DUA-0102"
        self.assertTrue(any("duplicate reference" in e for e in self.run_v(DRAFT, b)))
        self.assertTrue(any("duplicate id" in e for e in self.run_v(DRAFT, copy.deepcopy(DRAFT))))

    def test_id_must_match_file_name(self):
        errs = validate([("DUA-9999.json", DRAFT)], self.rr, SCHEMA)
        self.assertTrue(any("does not match the file name" in e for e in errs))

    def test_ayah_table_is_consistent(self):
        self.assertEqual((len(AYAH_COUNTS), sum(AYAH_COUNTS)), (114, 6236))


if __name__ == "__main__":
    unittest.main()
