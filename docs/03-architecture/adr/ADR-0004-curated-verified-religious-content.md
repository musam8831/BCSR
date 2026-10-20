# ADR-0004: Religious content is curated and scholar-verified, never AI-generated

- Status: Accepted
- Date: 2026-10-15
- Related: D-005, CR-01..05, FR-15, R-01

## Decision
The recommender selects from a static, signed catalogue of items, each approved by a named qualified reviewer. No model (including open-weight LLMs) may produce, translate or alter Qur'anic or Hadith text. Machine assistance is limited to metadata drafting, schema validation and duplicate detection, all subject to human review.

## Consequences
The catalogue grows at the pace of scholarly review, so the MVP target is ~60–100 items covering CS-01..CS-10. The strongest guarantee of authenticity.
