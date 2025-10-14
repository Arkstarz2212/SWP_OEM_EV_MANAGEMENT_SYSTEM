### Service Records API

- Base: `/api/service-records`

- POST `/` create

  - Body: ServiceRecord object fields: `vehicleId, serviceCenterId, claimId?, technicianId?, serviceType, description, performedAt?, result?, handoverAt?`
  - 200: created record

- PUT `/{id}` update

  - Body: same as create
  - 200: updated record

- GET `/` list

  - Query: `vehicleId? | claimId? | serviceCenterId?` (one filter at a time)
  - 200: array

- GET `/range?start=...&end=...`

  - ISO-8601 OffsetDateTime
  - 200: array

- DELETE `/{id}`

  - 200: void

- POST `/{id}/handover?time=2025-01-15T10:00:00Z&completeClaim=true`
  - Đặt `handover_at` cho bản ghi dịch vụ; nếu `completeClaim=true` và có `claimId`, hệ thống sẽ tự động chuyển claim sang `COMPLETED`.
  - 200: `{ "success": true, "handover_at": "2025-01-15T10:00:00Z", "claim_completed": true }`

RBAC

- Create/Update: Admin, EVM_Staff, SC_Staff; SC_Technician chỉ cho bản ghi của chính mình
- Delete: Admin
- Read/Handover: Tất cả 4 vai trò cần `Authorization: Bearer <token>`
