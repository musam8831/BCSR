# CI workflow templates

## Task ID check
- Fail PR if description/commits lack `T-Mx-NNN`

## Docs link check
- Fail on broken relative links under `docs/`

## Content schema
- Validate `content/catalogue/**` against schema
- Fail if `verification.status` not allowed for packaged set

See existing `.github/workflows/governance.yml` as baseline.
