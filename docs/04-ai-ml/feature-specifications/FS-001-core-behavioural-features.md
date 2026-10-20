# FS-001 Core Behavioural Features v0.1

Windows: W15 (15 min), W60 (1 h), D (day). Every feature also gets `z_<name>` = (value − personal median) / personal MAD over the trailing 14 days (computed after ≥ 7 days of data).

| Feature | Window | Definition | Hypothesised states |
|---|---|---|---|
| unlock_count | W15, W60 | Count of UNLOCK events | CS-02 |
| checking_sessions | W60 | Sessions < 30 s | CS-02 |
| app_switch_rate | W15 | Foreground changes per active minute | CS-02, CS-05 |
| app_switch_entropy | W60 | Shannon entropy of the category distribution | CS-02, CS-03 |
| longest_session_min | W60 | Longest continuous session in one category | CS-03 |
| cat_share_{social,video,messaging,games,productivity,religious} | W60, D | Share of screen time | CS-03, CS-07, CS-08 |
| screen_time_min | W60, D | Total screen-on time | CS-03 |
| night_screen_min | D | Screen time within the habitual sleep window (learned from charging + inactivity) | CS-06 |
| first_use_ts, last_use_ts | D | Circadian anchors | CS-06 |
| notif_count_{category} | W60 | Notification arrivals | CS-02, CS-08 |
| messaging_share_delta | D | Change vs baseline in messaging/social share | CS-07 |
| mobility_still_ratio | D | Share of time still (activity recognition) | CS-04 |
| hour_sin, hour_cos, is_weekend | — | Time encoding | all |

Quality rules: drop windows with < 50% sensing coverage; flag OEM-killed gaps.
