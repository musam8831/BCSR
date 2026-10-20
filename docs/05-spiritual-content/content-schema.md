# Content Schema — CON-02 v0.1

Machine schema: `content/schema/catalogue.schema.json`. One file per item in `content/catalogue/items/`.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | string `DUA-NNNN` | ✔ | Stable |
| type | enum: quran, hadith_dua, dhikr | ✔ | |
| title | {en, ur} | ✔ | Short situational title |
| arabic | string | ✔ | Copied verbatim from the licensed source; the reviewer confirms |
| transliteration | {en} | ✔ | |
| translation | {en, ur} + translation_source | ✔ | |
| reference | object | ✔ | quran: {surah, ayah_from, ayah_to}; hadith: {collection, number, numbering_system, grading, grader} |
| context_note | {en, ur} | ✔ | When/why it was said, from the source |
| states | list of CS-xx | ✔ | Approved mapping |
| times | list: morning, evening, sleep, any | | |
| repetitions | int | | e.g., 3, 7, 33 |
| audio | {file, reciter, licence} | | Optional |
| verification | {status: draft/in_review/approved/rejected, reviewer, date, record_id} | ✔ | Only `approved` ships |

## Seed candidates (metadata only — Arabic/translation to be entered from sources and verified by the scholar)
| Draft ID | Situation | Reference to verify | States |
|---|---|---|---|
| DUA-0001 | Worry & grief (hamm, hazan) | Sahih al-Bukhari 6369 | CS-02, CS-04 |
| DUA-0002 | Distress (karb) | Sahih al-Bukhari 6346; Sahih Muslim 2730 | CS-04, CS-10 |
| DUA-0003 | Du'a of Yunus (AS) | Qur'an 21:87; Tirmidhi 3505 (grading to verify) | CS-02, CS-04 |
| DUA-0004 | Hearts find rest | Qur'an 13:28 | CS-01, CS-03 |
| DUA-0005 | Anger: seeking refuge | Sahih al-Bukhari 3282 | CS-05 |
| DUA-0006 | Before sleep | Sahih al-Bukhari 6320 / 6324 (verify) | CS-06 |
| DUA-0007 | Ease in difficulty | Qur'an 94:5–6 | CS-04, CS-08 |
| DUA-0008 | Hasbunallah | Qur'an 3:173 | CS-02 |

*All references above are drafts for scholar verification. Numbering differs between editions, so the reviewer must confirm the edition.*
