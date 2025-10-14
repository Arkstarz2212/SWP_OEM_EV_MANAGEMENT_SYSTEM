# AOEM EV Warranty Management System API Guide

## Giới thiệu

AOEM là hệ thống quản lý bảo hành xe điện (EV) phục vụ Hãng sản xuất (EVM) và Trung tâm dịch vụ (SC). Tài liệu này mô tả các API chính và hướng dẫn thực hiện các use case từ UC1..UC38.

## Base URL

- Dev (mặc định khi chạy `./gradlew bootRun`): `http://localhost:8080`
- Tất cả endpoint đều trả về JSON theo dạng `ApiResponse<T>`.

## Xác thực & Phiên (Auth & Session)

- Đăng nhập lấy token: `POST /api/auth/login`
- Token phiên (ví dụ `sessionToken`) truyền qua header `Authorization: Bearer <token>`.
- Đăng xuất: `POST /api/auth/logout`
- Quản lý phiên: nhóm `/api/sessions` (tạo/kết thúc/tra cứu phiên).

## Headers chung

- `Content-Type: application/json`
- `Accept: application/json`
- `Authorization: Bearer <sessionToken>` (nếu endpoint yêu cầu)

---

## Danh mục API theo nhóm

Sử dụng các section bên dưới để tra cứu chi tiết:

- Auth: `/api/auth`
- Sessions: `/api/sessions`
- Users: `/api/users`
- Vehicles: `/api/vehicles`
- Warranty Claims: `/api/claims`
- Technician Assignments: `/api/assignments`
- Parts Catalog: `/api/parts`
- Vehicle Components: `/api/vehicle-components`
- Service Centers: `/api/service-centers`
- Roles: role-only qua `users.role`; endpoint: `PUT /api/users/{userId}/role?role=...`
- Search (quick/basic): `/api/search`
- Campaigns: `/api/campaigns`
- Campaign Notifications: `/api/campaign-notifications`
- Campaign Targets: `/api/campaign-targets`
- Claim Attachments: `/api/claim-attachments`
- Claim Status History: `/api/claim-status-history`
- Warranty Policies: `/api/warranty-policies`

---

### Auth `/api/auth`

- `POST /login` (body: `LoginRequest`) → `LoginResponse`
- `POST /logout?userId&sessionId` → `Boolean`
- `POST /password/reset/request` (body: `PasswordResetRequest`) → `Boolean`
- `POST /password/reset/confirm?token&newPassword` → `Boolean`

### Sessions `/api/sessions`

- `POST /create?userId&token[&ip]` → tạo session
- `GET /by-token?token` → thông tin session
- `POST /terminate?token` → kết thúc 1 session
- `POST /terminate-all?userId` → kết thúc toàn bộ session của user
- `POST /terminate-expired` → kết thúc session hết hạn
- `GET /by-user?userId` → danh sách session theo user
- `GET /active` → danh sách session active

### Users `/api/users`

- `POST /` (body: `UserCreateRequest`) → tạo user
- `PUT /{userId}` (body: `UserCreateRequest`) → cập nhật user
- `POST /password/reset` (body: `PasswordResetRequest`) → reset mật khẩu (admin/self)
- `POST /{userId}/password/change` (body: `PasswordChangeRequest`) → đổi mật khẩu
- `GET /{userId}` → lấy hồ sơ user
- `PUT /{userId}/profile` (body: `ProfileUpdateRequest`) → cập nhật hồ sơ
- `GET /?role&serviceCenterId` → liệt kê users

### Vehicles `/api/vehicles`

- `POST /` (body: `VehicleRegisterRequest`) → đăng ký xe theo VIN
- `POST /components` (body: `VehicleComponentRegisterRequest`) → gắn số seri phụ tùng quan trọng
- `GET /{vin}` → chi tiết xe theo VIN
- `POST /search` (body: `VehicleSearchRequest`) → tìm kiếm xe
- `PUT /{vehicleId}/odometer?km` → cập nhật số km

### Warranty Claims `/api/claims`

- `POST /` (body: `WarrantyClaimCreateRequest`) → tạo claim (draft)
- `POST /{claimId}/attachments` (body: `ClaimAttachmentRequest`) → đính kèm
- `POST /{claimId}/submit` → gửi claim
- `POST /{claimId}/assign` (body: `TechnicianAssignmentRequest`) → phân công kỹ thuật viên
- `PUT /{claimId}` (body: `WarrantyClaimUpdateRequest`) → cập nhật trạng thái/chi tiết
- `POST /{claimId}/complete?completedAt` → đóng claim
- `GET /{claimId}` → chi tiết claim
- `POST /search` (body: `WarrantyClaimSearchRequest`) → tìm kiếm claim
- `GET /dashboard?serviceCenterId` → dashboard SC

### Technician Assignments `/api/assignments`

- `POST /` (body: `TechnicianAssignmentRequest`) → tạo phân công
- `GET /{id}` → chi tiết phân công
- `PUT /{id}` (body: `TechnicianAssignmentRequest`) → cập nhật phân công
- `POST /{id}/soft-delete` | `POST /{id}/hard-delete`
- `GET /?technicianId&claimId&status` → tìm kiếm phân công
- `GET /all?limit&offset`
- `GET /by-technician/{technicianId}` | `GET /by-claim/{claimId}`
- `POST /{id}/start` | `POST /{id}/complete`
- `GET /performance/{technicianId}[?fromDate&toDate]`

### Parts Catalog `/api/parts`

- `POST /` (body: `PartCatalogCreateRequest`) → tạo phụ tùng
- `GET /{partId}` | `PUT /{partId}` (body: create request)
- `POST /{partId}/soft-delete` | `POST /{partId}/hard-delete`
- `GET /?keyword&category&vehicleModel&active&limit&offset` → tìm kiếm
- `GET /all?limit&offset`
- `GET /number/{partNumber}`
- `GET /category/{category}`
- `GET /vehicle-model/{vehicleModel}`
- `POST /{partId}/activate` | `POST /{partId}/deactivate`

### Vehicle Components `/api/vehicle-components`

- `POST /` (body: register) | `GET /{id}` | `PUT /{id}`
- `POST /{id}/soft-delete` | `POST /{id}/hard-delete`
- `GET /all?limit&offset`
- `GET /by-vehicle/{vehicleId}` | `GET /by-part/{partCatalogId}`
- `GET /by-serial/{serialNumber}`
- `POST /{id}/replace` | `POST /{id}/activate` | `POST /{id}/deactivate`

### Service Centers `/api/service-centers`

- `POST /` (body: `ServiceCenterCreateRequest`) | `GET /{id}` | `PUT /{id}`
- `POST /{id}/soft-delete` | `POST /{id}/hard-delete`
- `GET /?keyword&region&active&limit&offset`
- `GET /all?limit&offset`
- `GET /code/{code}` | `GET /region/{region}`
- `POST /{id}/activate` | `POST /{id}/deactivate`

### Roles (role-only)

- `PUT /api/users/{userId}/role?role=SC_Staff|SC_Technician|EVM_Staff|Admin` → cập nhật role của user

### Search `/api/search`

- Quick: `GET /quick/claims|vehicles|parts|customers?q&limit`
- Basic: `GET /claims|vehicles|parts|customers` với các query tương ứng

### Campaigns `/api/campaigns`

- `POST /` (body: `CampaignCreateRequest`) | `PUT /{campaignId}`
- `POST /{campaignId}/affected-vins` (body: `List<String>`)
- `POST /{campaignId}/notify` (body: noti request) → gửi thông báo đến SC
- `POST /appointments` (body: `CampaignAppointmentRequest`)
- `POST /results` (body: `CampaignResultRequest`)
- `GET /{campaignId}` | `GET /` → danh sách campaign

### Campaign Notifications `/api/campaign-notifications`

- CRUD cơ bản, search/filter, gửi/đánh dấu sent/failed

### Campaign Targets `/api/campaign-targets`

- `GET /{id}` | `POST /{id}/update` | delete (soft/hard)
- `GET /all` | `GET /by-campaign/{campaignId}` | `GET /by-vin/{vin}` | `GET /by-vehicle/{vehicleId}`
- `POST /{id}/mark-completed` | `POST /{id}/mark-notified`
- `GET /progress/{campaignId}`

### Claim Attachments `/api/claim-attachments`

- `POST /` | `GET /{id}` | `PUT /{id}` | delete (soft/hard)
- `GET /all` | `GET /by-claim/{claimId}` | `GET /by-type/{type}` | `GET /by-user/{userId}`
- `POST /{id}/download`

### Claim Status History `/api/claim-status-history`

- `GET /{id}` | delete (soft/hard)
- `GET /all` | `GET /by-claim/{claimId}` | `GET /by-user/{userId}` | `GET /by-status/{status}` | `GET /latest/{claimId}`

### Warranty Policies `/api/warranty-policies`

- `POST /` | `GET /{id}` | `PUT /` | `POST /{id}/delete`
- `GET /by-model/{model}` | `GET /by-category/{category}`
- `GET /by-model-and-category?model&category`
- `GET /by-months/{months}` | `GET /by-km/{km}` | `GET /by-range?minMonths&maxMonths`
- `GET /validate?model&category&months&km` → kiểm tra hợp lệ bảo hành
- Stats: `GET /stats/model/{model}` | `GET /stats/category/{category}`

---

## Mapping Use Cases → Chuỗi API đề xuất

Dưới đây là luồng tối thiểu (mỗi UC có thể tinh chỉnh theo nghiệp vụ thực tế):

Lưu ý chung cho tất cả UC (Bước 0 – Đăng nhập theo vai trò phù hợp)

- Mỗi luồng đều bắt đầu bằng đăng nhập để lấy `sessionToken`.
- Gửi token qua header `Authorization: Bearer <sessionToken>` cho tất cả API tiếp theo.
- Vai trò mặc định theo UC:
  - SC_Staff: UC1–UC9
  - SC_Technician: UC10–UC14
  - EVM_Staff: UC15–UC26
  - Admin: UC27–UC30, UC37–UC38
  - Cross-cutting (ai cũng dùng theo quyền): UC31–UC36

Mẫu khởi đầu cho mọi UC

- Auth: `POST /api/auth/login` với tài khoản có vai trò yêu cầu (ví dụ SC_Staff/EVM_Staff/Admin/SC_Technician)
- Nhận `sessionToken` → gắn vào `Authorization: Bearer <sessionToken>` cho các bước sau

1. UC1 Đăng ký xe theo VIN (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `POST /api/vehicles` → đăng ký xe
- `GET /api/vehicles/{vin}` → xác minh

2. UC2 Gắn số seri phụ tùng (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `POST /api/vehicles/components` → gắn phụ tùng quan trọng
- `GET /api/vehicle-components/by-vehicle/{vehicleId}` → kiểm tra

3. UC3 Tra cứu xe & khách hàng (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `POST /api/vehicles/search` hoặc `GET /api/search/vehicles`

4. UC4 Xem lịch sử bảo hành/dịch vụ (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `GET /api/claims?` qua `POST /api/claims/search` hoặc `GET /api/search/claims`
- `GET /api/claim-status-history/by-claim/{claimId}`

5. UC5 Tạo claim bảo hành (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `POST /api/claims` → draft

6. UC6 Đính kèm báo cáo/ảnh/log (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `POST /api/claims/{claimId}/attachments`

7. UC7 Gửi claim đến hãng (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `POST /api/claims/{claimId}/submit`

8. UC8 Theo dõi trạng thái claim (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `GET /api/claims/{claimId}` | `GET /api/claim-status-history/by-claim/{claimId}`

9. UC9 Nhận & gửi thông báo recall/campaign (SC_Staff)
   - Role: SC_Staff | Bước 0: Đăng nhập SC_Staff

- `GET /api/campaigns` | `GET /api/campaign-targets/by-vin/{vin}`
- `POST /api/campaign-notifications/{id}/send`

10. UC10 Nhận claim đã duyệt (Technician)
    - Role: SC_Technician | Bước 0: Đăng nhập SC_Technician

- `GET /api/claims/{claimId}` | `GET /api/assignments/by-claim/{claimId}`

11. UC11 Nhận phụ tùng từ hãng (Technician)
    - Role: SC_Technician | Bước 0: Đăng nhập SC_Technician

- `GET /api/parts?` hoặc `GET /api/parts/number/{partNumber}` (tham chiếu)

12. UC12 Sửa chữa & cập nhật kết quả (Technician)
    - Role: SC_Technician | Bước 0: Đăng nhập SC_Technician

- `POST /api/assignments/{id}/start`
- `PUT /api/claims/{claimId}` (cập nhật tiến độ, part used)
- `POST /api/assignments/{id}/complete`

13. UC13 Đóng hồ sơ và bàn giao (Technician)
    - Role: SC_Technician | Bước 0: Đăng nhập SC_Technician

- `POST /api/claims/{claimId}/complete?completedAt=...`

14. UC14 Thực hiện recall/campaign (Technician)
    - Role: SC_Technician | Bước 0: Đăng nhập SC_Technician

- `GET /api/campaign-targets/by-vehicle/{vehicleId}`
- `POST /api/campaign-targets/{id}/mark-completed`
- `POST /api/campaigns/results` (ghi nhận kết quả nếu cần)

15. UC15 Quản lý DB phụ tùng & VIN mapping (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- Parts CRUD: `/api/parts` | Vehicle Components: `/api/vehicle-components`

16. UC16 Thiết lập chính sách bảo hành (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `/api/warranty-policies` CRUD, validate

17. UC17 Quản lý ngoại lệ bảo hành (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- Quy ước qua `WarrantyClaimUpdateRequest` và `WarrantyPolicy` liên quan

18. UC18 Tiếp nhận claim từ SC (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `POST /api/claims/search` | `GET /api/claims/{claimId}`

19. UC19 Phân loại claim (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `PUT /api/claims/{claimId}` (status=Validating/NeedInfo/OutOfScope)

20. UC20 Thẩm định hồ sơ (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `GET /api/claim-attachments/by-claim/{claimId}` | `GET /api/claims/{claimId}`

21. UC21 Phê duyệt / từ chối claim (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `PUT /api/claims/{claimId}` (status=Approved/Rejected)

22. UC22 Theo dõi trạng thái claim (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `POST /api/claims/search` | `GET /api/claims/{claimId}`

23. UC23 Khởi tạo campaign recall (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `POST /api/campaigns`

24. UC24 Xác định VIN bị ảnh hưởng (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `POST /api/campaigns/{campaignId}/affected-vins`

25. UC25 Gửi thông báo recall đến SC (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `POST /api/campaigns/{campaignId}/notify`

26. UC26 Theo dõi tiến độ recall cơ bản (EVM)
    - Role: EVM_Staff | Bước 0: Đăng nhập EVM_Staff

- `GET /api/campaigns/{campaignId}` | `GET /api/campaign-targets/progress/{campaignId}`

27. UC27 Quản lý người dùng (Admin)
    - Role: Admin | Bước 0: Đăng nhập Admin

- Users CRUD `/api/users` + roles `/api/roles`

28. UC28 Phân quyền truy cập (Admin)
    - Role: Admin | Bước 0: Đăng nhập Admin

- `POST /api/roles/assign` | `POST /api/roles/remove` | `GET /api/roles/check`

29. UC29 Quản lý tham số hệ thống (Admin)
    - Role: Admin | Bước 0: Đăng nhập Admin

- Tuỳ chỉnh qua cấu hình ứng dụng; chưa có endpoint riêng

30. UC30 Theo dõi audit log (Admin)
    - Role: Admin | Bước 0: Đăng nhập Admin

- Sử dụng dữ liệu từ claim status/history, assignments, sessions

31. UC31 Đăng nhập
    - Role: All users (tài khoản hợp lệ)

- `POST /api/auth/login`

32. UC32 Đăng xuất
    - Role: All users (đã đăng nhập)

- `POST /api/auth/logout`

33. UC33 Đổi mật khẩu / reset mật khẩu
    - Role: All users (đổi); Admin hoặc flow token (reset)

- `POST /api/users/{userId}/password/change` | `POST /api/users/password/reset`
- hoặc flow token: `/api/auth/password/reset/request` + `/api/auth/password/reset/confirm`

34. UC34 Quản lý hồ sơ cá nhân
    - Role: All users

- `GET /api/users/{userId}` | `PUT /api/users/{userId}/profile`

35. UC35 Nhận thông báo (push/email)
    - Role: SC_Staff hoặc người dùng liên quan chiến dịch

- `POST /api/campaign-notifications/{id}/send` | query theo kênh

36. UC36 Tìm kiếm nhanh (claim, xe)
    - Role: Tùy quyền đã cấp (Search)

- `GET /api/search/quick/claims|vehicles|parts|customers`

37. UC37 Quản lý role & permission
    - Role: Admin

- Nhóm `/api/roles` đầy đủ

38. UC38 Quản lý session đăng nhập
    - Role: Admin (toàn bộ), User (một phần tự quản)

- Nhóm `/api/sessions` đầy đủ

---

## Ví dụ curl tối giản

Ví dụ đăng nhập và gọi API có bảo vệ:

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"sc.staff","password":"Passw0rd!"}'
```

```bash
# Giả sử nhận được token T
curl -s http://localhost:8080/api/vehicles/VIN123456789 \
  -H "Authorization: Bearer T" -H 'Accept: application/json'
```

---

## Ghi chú

- Phân quyền đơn giản theo `users.role`; không còn interceptor/permission động.
- Kiểu request/response chi tiết xem trong gói `models.dto.request|response` tương ứng.
