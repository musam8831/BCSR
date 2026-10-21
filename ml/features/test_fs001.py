"""Pilot P4 (AG-06): fixtures for the FS-001 slice (SK-06 template step 5)."""
import math
import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parent))
import fs001 as f  # noqa: E402

MIN = 60_000


def ev(minute, typ, sec=0):
    return {"ts": minute * MIN + sec * 1000, "type": typ}


class FS001Test(unittest.TestCase):
    def test_unlock_count_respects_window_edges(self):
        events = [ev(0, "UNLOCK"), ev(14, "UNLOCK"), ev(15, "UNLOCK"), ev(5, "SCREEN_ON")]
        self.assertEqual(f.unlock_count(events, 0, 15 * MIN), 2)  # end is exclusive
        self.assertEqual(f.unlock_count(events, 15 * MIN, 15 * MIN), 1)

    def test_checking_sessions(self):
        events = [
            ev(1, "SCREEN_ON"), ev(1, "SCREEN_OFF", 20),   # 20 s → checking
            ev(2, "SCREEN_ON"), ev(3, "SCREEN_OFF"),       # 60 s → not
            ev(4, "SCREEN_ON"), ev(4, "SCREEN_OFF", 30),   # exactly 30 s → not (< 30 s)
            ev(5, "SCREEN_ON"), ev(6, "SCREEN_ON"),        # missing OFF → not counted
            ev(6, "SCREEN_OFF", 5),                        # 5 s after the second ON → checking
        ]
        self.assertEqual(f.checking_sessions(events, 0, 60 * MIN), 2)

    def test_entropy(self):
        self.assertEqual(f.app_switch_entropy([]), 0.0)
        self.assertEqual(f.app_switch_entropy(["SOCIAL"] * 5), 0.0)
        self.assertAlmostEqual(f.app_switch_entropy(["SOCIAL", "VIDEO"]), 1.0)
        self.assertAlmostEqual(f.app_switch_entropy(["A", "B", "C", "D"]), 2.0)

    def test_robust_z_needs_seven_days_and_spread(self):
        self.assertIsNone(f.robust_z(5, [1, 2, 3, 4, 5, 6]))
        self.assertIsNone(f.robust_z(5, [3] * 10))
        hist = [10, 12, 11, 13, 9, 10, 12, 11]   # median 11, MAD 1
        self.assertAlmostEqual(f.robust_z(14, hist), 3.0)

    def test_robust_z_uses_only_trailing_14_days(self):
        old = [1000] * 20
        recent = [10, 12, 11, 13, 9, 10, 12, 11, 10, 12, 11, 13, 9, 10]
        self.assertAlmostEqual(f.robust_z(11, old + recent), f.robust_z(11, recent))
        self.assertFalse(math.isnan(f.robust_z(11, recent)))


if __name__ == "__main__":
    unittest.main()
