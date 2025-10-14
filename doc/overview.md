### Actors (4 vai trò)

- Admin
- EVM Staff (nhân viên hãng)
- SC Staff (nhân viên trung tâm dịch vụ)
- SC Technician (kỹ thuật viên)

RBAC tổng quan

- Reads: yêu cầu Authorization: Bearer <token> cho cả 4 vai trò ở các controller chính (claims, vehicles, service-records, parts, policies).
- Writes: hạn chế theo nghiệp vụ (ví dụ: approve/reject chỉ Admin/EVM; tạo part/policy chỉ Admin/EVM; technician chỉ thao tác bản ghi của chính mình).

### Functional Requirements (cần giữ)

Trung tâm dịch vụ

1. Quản lý hồ sơ xe (VIN, model, khách hàng)
   - Endpoints: `GET /api/vehicles/{vin}`, `POST /api/vehicles` (đăng ký)
2. Quản lý lịch sử bảo hành (service history, claim history)
   - Endpoints: `GET /api/vehicles/{vin}/service-history`, `GET /api/vehicles/{vin}/claim-history`
3. Tạo yêu cầu bảo hành (warranty claim) gửi lên hãng
   - Endpoints: `POST /api/claims` (draft), `POST /api/claims/{id}/submit`
4. Theo dõi trạng thái claim (đã gửi → chờ duyệt → chấp nhận → xử lý)
   - Endpoints: `GET /api/claims/{id}`, `GET /api/claims?status=...`, `PUT /api/claims/{id}` (transition)
5. Cập nhật kết quả bảo hành & bàn giao xe
   - Endpoints: `POST /api/service-records/{id}/handover?time=...&completeClaim=true|false`
6. Phân công kỹ thuật viên cho từng case
   - Endpoints: `POST /api/claims/{id}/assign`

Hãng sản xuất

1. Quản lý sản phẩm & phụ tùng cơ bản (parts catalog)
   - Endpoints: `POST /api/parts`, `GET /api/parts/**`, `POST /api/parts/{id}/(activate|deactivate)`
   - Repo: JDBC → `aoem.parts_catalog`
2. Quản lý chính sách bảo hành (thời hạn, phạm vi, điều kiện)
   - Read-only từ `vehicle.warranty_info`:
     - `GET /api/warranty-policies/by-vin/{vin}` (underWarranty, expiryDate)
     - `GET /api/warranty-policies/by-model/{model}` (policyCount)
   - Admin policy APIs ở chế độ in-memory (không đổi schema)
3. Tiếp nhận & phê duyệt claims
   - Endpoints: `POST /api/claims/{id}/approve`, `POST /api/claims/{id}/reject`
4. Theo dõi trạng thái claim đến khi hoàn tất
   - Endpoints: `GET /api/claims?status=...`, kết hợp handover để hoàn tất quy trình

### Những chức năng có thể bỏ (đã loại khỏi scope)

Về phụ tùng

- Gắn số seri phụ tùng lắp trên xe.
- Gắn số seri phụ tùng với VIN.

Về chiến dịch (campaigns)

- Nhận danh sách xe thuộc diện recall/service campaigns.
- Gửi thông báo cho khách hàng.
- Quản lý lịch hẹn campaign.
- Báo cáo kết quả campaign.

Về chuỗi cung ứng phụ tùng

- Quản lý tồn kho phụ tùng.
- Phân bổ phụ tùng cho trung tâm dịch vụ.
- Cảnh báo thiếu hụt phụ tùng.

Về claim

- Đính kèm báo cáo kiểm tra, hình ảnh, chẩn đoán.

Về chi phí & lịch hẹn

- Quản lý chi phí chi tiết (part_cost, labor_cost…).
- Đặt lịch hẹn bảo hành/bảo trì cho khách hàng.
- Gửi thông báo tự động cho khách hàng.

### Tài liệu chi tiết liên quan

- Service records: `doc/service-records.md`
- Warranty policies (read-only): `doc/warranty-policies/by-vin.md`, `doc/warranty-policies/by-model.md`
- Parts RBAC & reads: `doc/parts/readme.md`
- Vehicles history: `doc/vehicles/history.md`
