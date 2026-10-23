"""T-M5-013: every privacy, safety and content AC in REQ-03 has a gate case (TST-01 §2)."""
import pathlib
import re
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
AC = re.compile(r"AC-(?:PR|SR|CR)-\d+")


class GateCasesTest(unittest.TestCase):
    def test_every_gate_ac_has_a_case(self):
        acs = set(AC.findall((ROOT / "docs/01-requirements/acceptance-criteria.md").read_text(encoding="utf-8")))
        rows = [l for l in (ROOT / "docs/08-testing/cases/TC-PR-SR-CR.md").read_text(encoding="utf-8").splitlines()
                if l.startswith("| TC-")]
        covered = {cells[2].strip() for cells in (r.split("|") for r in rows)}
        self.assertGreaterEqual(len(acs), 19)
        self.assertEqual(sorted(acs - covered), [])


if __name__ == "__main__":
    unittest.main()
