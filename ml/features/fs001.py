"""FS-001 core behavioural features — first slice (pilot P4, AG-06, SK-06 feature-to-code template).

Reference implementation for research (D0/D1/D2 analysis) and the spec the Kotlin `:core:features`
module must match (FS-001 says on-device code computes the same values).

| FS-001 feature | Function | Inputs (on-device signals only) | Window |
|---|---|---|---|
| unlock_count | unlock_count | device events (type, ts) | W15, W60 |
| checking_sessions | checking_sessions | device events | W60 |
| app_switch_entropy | app_switch_entropy | app-usage categories | W60 |
| z_<name> | robust_z | daily values of a feature, trailing 14 days | — |

Events are dicts: {"ts": epoch_ms, "type": "UNLOCK" | "SCREEN_ON" | "SCREEN_OFF"} (data dictionary ARC-04).
No raw export: functions return numbers only. Model card impact: these features feed the population model (EP-001 §3).
"""
from collections import Counter
import math
import statistics

CHECKING_SESSION_MAX_S = 30  # FS-001: a "checking" session is shorter than 30 s
BASELINE_DAYS = 14           # FS-001: z-scores over the trailing 14 days
BASELINE_MIN_DAYS = 7        # FS-001 / FR-09: baseline only after >= 7 days


def in_window(events, start_ms, length_ms):
    end = start_ms + length_ms
    return [e for e in events if start_ms <= e["ts"] < end]


def unlock_count(events, start_ms, length_ms):
    """Count UNLOCK events in [start, start + length)."""
    return sum(1 for e in in_window(events, start_ms, length_ms) if e["type"] == "UNLOCK")


def checking_sessions(events, start_ms, length_ms):
    """Screen sessions (SCREEN_ON → next SCREEN_OFF) that start in the window and last < 30 s.
    A session with no SCREEN_OFF before the next SCREEN_ON is not counted (missing data, not a short session)."""
    evs = sorted((e for e in events if e["type"] in ("SCREEN_ON", "SCREEN_OFF")), key=lambda e: e["ts"])
    count, on_ts = 0, None
    for e in evs:
        if e["type"] == "SCREEN_ON":
            on_ts = e["ts"]
        elif on_ts is not None:
            if start_ms <= on_ts < start_ms + length_ms and (e["ts"] - on_ts) < CHECKING_SESSION_MAX_S * 1000:
                count += 1
            on_ts = None
    return count


def app_switch_entropy(categories):
    """Shannon entropy (bits) of the category distribution of foreground app sessions in the window; 0 if none."""
    counts = Counter(categories)
    n = sum(counts.values())
    if n == 0:
        return 0.0
    return -sum((c / n) * math.log2(c / n) for c in counts.values())


def robust_z(value, history):
    """(value − median) / MAD over the trailing 14 daily values (FS-001, unscaled MAD); None before 7 days or when MAD is 0."""
    hist = list(history)[-BASELINE_DAYS:]
    if len(hist) < BASELINE_MIN_DAYS:
        return None
    med = statistics.median(hist)
    mad = statistics.median(abs(x - med) for x in hist)
    if mad == 0:
        return None
    return (value - med) / mad
