# PUT /api/claims/{id}/status

## Mô tả

Cập nhật trạng thái của warranty claim để theo dõi tiến độ sửa chữa. Endpoint này cho phép SC Staff và EVM Staff cập nhật status của claim.

## Endpoint

```
PUT /api/claims/{id}/status
```

## Request

### Headers

```
Content-Type: application/json
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description               |
| --------- | ---- | -------- | ------------------------- |
| id        | long | Yes      | ID của claim cần cập nhật |

### Body

```json
{
  "status": "IN_PROGRESS",
  "notes": "Repair started. Parts ordered and technician assigned.",
  "actualRepairCost": 1200.0
}
```

### Parameters

| Field            | Type   | Required | Description                    |
| ---------------- | ------ | -------- | ------------------------------ |
| status           | string | Yes      | Trạng thái mới của claim       |
| notes            | string | No       | Ghi chú về thay đổi trạng thái |
| actualRepairCost | number | No       | Chi phí sửa chữa thực tế       |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "message": "Claim status updated successfully",
  "claimId": 1,
  "status": "IN_PROGRESS",
  "updatedAt": "2024-01-02T10:00:00Z",
  "updatedBy": 2,
  "updatedByName": "John SC Staff",
  "notes": "Repair started. Parts ordered and technician assigned.",
  "actualRepairCost": 1200.0
}
```

### Error Responses

#### 400 Bad Request - Status không hợp lệ

```json
{
  "error": "Invalid status. Valid statuses are: APPROVED, IN_PROGRESS, ON_HOLD, COMPLETED, REJECTED, CANCELLED",
  "path": "/api/claims/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Status transition không hợp lệ

```json
{
  "error": "Invalid status transition from PENDING_APPROVAL to COMPLETED",
  "path": "/api/claims/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Claim không tồn tại

```json
{
  "error": "Claim not found",
  "path": "/api/claims/999/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update claim status: Internal server error",
  "path": "/api/claims/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể cập nhật status trong phạm vi quyền
- **Claim Status:** Phải có status transition hợp lệ

## Test Cases

### Test Case 1: Cập nhật status thành công

**Request:**

```json
{
  "status": "IN_PROGRESS",
  "notes": "Repair started. Parts ordered and technician assigned.",
  "actualRepairCost": 1200.0
}
```

**Expected Response:** 200 OK với message thành công

### Test Case 2: Cập nhật status với ghi chú

**Request:**

```json
{
  "status": "ON_HOLD",
  "notes": "Waiting for additional parts. Expected delivery in 3 days."
}
```

**Expected Response:** 200 OK với ghi chú

### Test Case 3: Status không hợp lệ

**Request:**

```json
{
  "status": "INVALID_STATUS"
}
```

**Expected Response:** 400 Bad Request

### Test Case 4: Status transition không hợp lệ

**Request:**

```json
{
  "status": "COMPLETED"
}
```

**Expected Response:** 400 Bad Request - Status transition không hợp lệ

### Test Case 5: Claim không tồn tại

**Request:**

```json
{
  "status": "IN_PROGRESS"
}
```

**Expected Response:** 404 Not Found

## Usage Example

### cURL

```bash
curl -X PUT http://localhost:8080/api/claims/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "status": "IN_PROGRESS",
    "notes": "Repair started. Parts ordered and technician assigned.",
    "actualRepairCost": 1200.00
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/claims/1/status", {
  method: "PUT",
  headers: {
    "Content-Type": "application/json",
    Authorization: "Bearer sess_abc123def456ghi789",
  },
  body: JSON.stringify({
    status: "IN_PROGRESS",
    notes: "Repair started. Parts ordered and technician assigned.",
    actualRepairCost: 1200.0,
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/claims/1/status"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer sess_abc123def456ghi789"
}
data = {
    "status": "IN_PROGRESS",
    "notes": "Repair started. Parts ordered and technician assigned.",
    "actualRepairCost": 1200.00
}

response = requests.put(url, json=data, headers=headers)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập với quyền SC_Staff

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "scstaff@service.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 2,
    "role": "SC_Staff",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Kiểm tra trạng thái claim hiện tại

```bash
curl -X GET http://localhost:8080/api/claims/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 1,
  "claimNumber": "WC-2024-001",
  "status": "APPROVED",
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure"
}
```

### Bước 3: Cập nhật status sang IN_PROGRESS

```bash
curl -X PUT http://localhost:8080/api/claims/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "status": "IN_PROGRESS",
    "notes": "Repair started. Parts ordered and technician assigned.",
    "actualRepairCost": 1200.00
  }'
```

**Response:**

```json
{
  "success": true,
  "message": "Claim status updated successfully",
  "claimId": 1,
  "status": "IN_PROGRESS",
  "updatedAt": "2024-01-02T10:00:00Z",
  "updatedBy": 2,
  "updatedByName": "John SC Staff",
  "notes": "Repair started. Parts ordered and technician assigned.",
  "actualRepairCost": 1200.0
}
```

### Bước 4: Xác nhận status đã được cập nhật

```bash
curl -X GET http://localhost:8080/api/claims/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

## Valid Status Transitions

### From PENDING_APPROVAL

- ✅ **APPROVED** (by EVM_Staff/Admin)
- ✅ **REJECTED** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED

### From APPROVED

- ✅ **IN_PROGRESS** (by SC_Staff/SC_Technician)
- ✅ **CANCELLED** (by SC_Staff/SC_Technician)
- ❌ Không thể chuyển sang PENDING_APPROVAL, ON_HOLD, COMPLETED, REJECTED

### From IN_PROGRESS

- ✅ **ON_HOLD** (by SC_Staff/SC_Technician)
- ✅ **COMPLETED** (by SC_Staff/SC_Technician)
- ❌ Không thể chuyển sang PENDING_APPROVAL, APPROVED, REJECTED, CANCELLED

### From ON_HOLD

- ✅ **IN_PROGRESS** (by SC_Staff/SC_Technician)
- ✅ **COMPLETED** (by SC_Staff/SC_Technician)
- ❌ Không thể chuyển sang PENDING_APPROVAL, APPROVED, REJECTED, CANCELLED

### From COMPLETED

- ❌ Không thể chuyển sang bất kỳ status nào
- ❌ Status cuối cùng, không thể thay đổi

### From REJECTED

- ❌ Không thể chuyển sang bất kỳ status nào
- ❌ Status cuối cùng, không thể thay đổi

### From CANCELLED

- ❌ Không thể chuyển sang bất kỳ status nào
- ❌ Status cuối cùng, không thể thay đổi

## Permission Matrix

### SC_Staff

- ✅ Có thể cập nhật status của claims trong service center
- ✅ Có thể chuyển từ APPROVED sang IN_PROGRESS
- ✅ Có thể chuyển từ IN_PROGRESS sang ON_HOLD/COMPLETED
- ❌ Không thể chuyển sang PENDING_APPROVAL, APPROVED, REJECTED

### SC_Technician

- ✅ Có thể cập nhật status của claims được gán
- ✅ Có thể chuyển từ APPROVED sang IN_PROGRESS
- ✅ Có thể chuyển từ IN_PROGRESS sang ON_HOLD/COMPLETED
- ❌ Không thể chuyển sang PENDING_APPROVAL, APPROVED, REJECTED

### EVM_Staff

- ✅ Có thể cập nhật status của tất cả claims
- ✅ Có thể chuyển sang bất kỳ status nào (trừ COMPLETED, REJECTED, CANCELLED)
- ❌ Không thể chuyển sang status cuối cùng

### Admin

- ✅ Có thể cập nhật status của tất cả claims
- ✅ Có thể chuyển sang bất kỳ status nào
- ✅ Có thể chuyển sang status cuối cùng

## What Happens After Status Update

### Automatic Actions

- Cập nhật status trong database
- Cập nhật updatedAt timestamp
- Cập nhật updatedBy và updatedByName
- Ghi log trong status history
- Gửi thông báo đến stakeholders

### Manual Actions Required

- Cập nhật tiến độ sửa chữa
- Upload ảnh chụp quá trình sửa chữa
- Cập nhật thông tin phụ tùng
- Cập nhật chi phí sửa chữa

### Notifications

- Gửi email đến stakeholders về thay đổi status
- Gửi SMS nếu cần thiết
- Tạo notification records trong database
- Cập nhật dashboard của users

## Validation Rules

### Status

- Phải là status hợp lệ
- Phải có status transition hợp lệ
- Không thể chuyển sang status cuối cùng (trừ Admin)

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Actual Repair Cost

- Phải là số dương
- Không được vượt quá approvedRepairCost \* 1.2
- Phải hợp lý với loại sửa chữa

### Permission

- Chỉ users có quyền mới có thể cập nhật
- SC_Staff chỉ cập nhật claims của service center
- SC_Technician chỉ cập nhật claims được gán
- EVM_Staff và Admin có thể cập nhật tất cả

## Notes

- Endpoint này yêu cầu quyền phù hợp để sử dụng
- Response sẽ khác nhau tùy theo quyền của người dùng
- Status transition phải tuân theo quy tắc business
- Tất cả thay đổi được ghi log để audit
- Cập nhật status sẽ trigger các workflow khác
- Stakeholders sẽ nhận thông báo về thay đổi status
- Status cuối cùng không thể thay đổi
- Nếu có ghi chú, sẽ được lưu trong status history
- Chi phí sửa chữa thực tế có thể khác với phê duyệt
- SC_Staff chỉ có thể cập nhật claims của service center
- SC_Technician chỉ có thể cập nhật claims được gán
- EVM_Staff và Admin có thể cập nhật tất cả claims
