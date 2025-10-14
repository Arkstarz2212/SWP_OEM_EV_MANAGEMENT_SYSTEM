### Claim Status Logs API

- Base: `/api/claim-status-logs`

- POST `/` create log

  - Body: `claimId, fromStatus?, toStatus, changedByUserId, note?, changedAt?`
  - 200: created log

- GET `/` list
  - Query: `claimId?` or `start&end` (ISO-8601)
  - 200: array
