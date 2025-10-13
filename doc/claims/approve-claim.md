# POST /api/claims/{id}/approve

## Mô tả

Phê duyệt warranty claim để cho phép bắt đầu sửa chữa. Endpoint này chỉ dành cho EVM Staff và Admin để xem xét và phê duyệt claims.

## Endpoint

```
POST /api/claims/{id}/approve
```

## Request

### Headers

```
Content-Type: application/json
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description                |
| --------- | ---- | -------- | -------------------------- |
| id        | long | Yes      | ID của claim cần phê duyệt |

### Body

```json
{
  "notes": "Approved for repair. Parts will be ordered immediately.",
  "approvedRepairCost": 1500.0
}
```

### Parameters

| Field              | Type   | Required | Description                     |
| ------------------ | ------ | -------- | ------------------------------- |
| notes              | string | No       | Ghi chú phê duyệt               |
| approvedRepairCost | number | No       | Chi phí sửa chữa được phê duyệt |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "message": "Claim approved successfully",
  "claimId": 1,
  "status": "APPROVED",
  "approvedAt": "2024-01-02T10:00:00Z",
  "approvedBy": 3,
  "approvedByName": "Jane EVM Staff",
  "notes": "Approved for repair. Parts will be ordered immediately.",
  "approvedRepairCost": 1500.0
}
```

### Error Responses

#### 400 Bad Request - Claim không ở trạng thái PENDING_APPROVAL

```json
{
  "error": "Claim is not in PENDING_APPROVAL status",
  "path": "/api/claims/1/approve",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Claim không tồn tại

```json
{
  "error": "Claim not found",
  "path": "/api/claims/999/approve",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền phê duyệt

```json
{
  "error": "Insufficient permissions to approve claim",
  "path": "/api/claims/1/approve",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to approve claim: Internal server error",
  "path": "/api/claims/1/approve",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với quyền EVM_Staff hoặc Admin
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token hợp lệ
- **Permission:** Chỉ EVM_Staff và Admin mới có quyền phê duyệt claim
- **Claim Status:** Claim phải ở trạng thái PENDING_APPROVAL

## Test Cases

### Test Case 1: Phê duyệt claim thành công

**Request:**

```json
{
  "notes": "Approved for repair. Parts will be ordered immediately.",
  "approvedRepairCost": 1500.0
}
```

**Expected Response:** 200 OK với message thành công

### Test Case 2: Phê duyệt claim với ghi chú

**Request:**

```json
{
  "notes": "Approved with conditions. Monitor repair progress closely."
}
```

**Expected Response:** 200 OK với ghi chú

### Test Case 3: Claim đã được phê duyệt

**Request:**

```json
{
  "notes": "Approved for repair"
}
```

**Expected Response:** 400 Bad Request - Claim không ở trạng thái PENDING_APPROVAL

### Test Case 4: Claim không tồn tại

**Request:**

```json
{
  "notes": "Approved for repair"
}
```

**Expected Response:** 404 Not Found

### Test Case 5: Không có quyền phê duyệt

**Request:**

```json
{
  "notes": "Approved for repair"
}
```

**Expected Response:** 403 Forbidden

## Usage Example

### cURL

```bash
curl -X POST http://localhost:8080/api/claims/1/approve \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "notes": "Approved for repair. Parts will be ordered immediately.",
    "approvedRepairCost": 1500.00
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/claims/1/approve", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    Authorization: "Bearer sess_abc123def456ghi789",
  },
  body: JSON.stringify({
    notes: "Approved for repair. Parts will be ordered immediately.",
    approvedRepairCost: 1500.0,
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/claims/1/approve"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer sess_abc123def456ghi789"
}
data = {
    "notes": "Approved for repair. Parts will be ordered immediately.",
    "approvedRepairCost": 1500.00
}

response = requests.post(url, json=data, headers=headers)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập với quyền EVM_Staff

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "evmstaff@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 3,
    "role": "EVM_Staff",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Kiểm tra trạng thái claim

```bash
curl -X GET http://localhost:8080/api/claims/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 1,
  "claimNumber": "WC-2024-001",
  "status": "PENDING_APPROVAL",
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure",
  "estimatedRepairCost": 1500.0
}
```

### Bước 3: Phê duyệt claim

```bash
curl -X POST http://localhost:8080/api/claims/1/approve \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "notes": "Approved for repair. Parts will be ordered immediately.",
    "approvedRepairCost": 1500.00
  }'
```

**Response:**

```json
{
  "success": true,
  "message": "Claim approved successfully",
  "claimId": 1,
  "status": "APPROVED",
  "approvedAt": "2024-01-02T10:00:00Z",
  "approvedBy": 3,
  "approvedByName": "Jane EVM Staff",
  "notes": "Approved for repair. Parts will be ordered immediately.",
  "approvedRepairCost": 1500.0
}
```

### Bước 4: Xác nhận claim đã được phê duyệt

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
  "approvedAt": "2024-01-02T10:00:00Z",
  "approvedBy": 3,
  "approvedByName": "Jane EVM Staff"
}
```

## Claim Status Transitions

### PENDING_APPROVAL → APPROVED

- Claim được phê duyệt
- Có thể bắt đầu sửa chữa
- Phụ tùng có thể được order
- Gửi thông báo đến SC Staff

### PENDING_APPROVAL → REJECTED

- Claim bị từ chối
- Không được sửa chữa
- Cần tạo claim mới
- Gửi thông báo từ chối

## Permission Matrix

### EVM_Staff

- ✅ Có thể phê duyệt claims của OEM
- ✅ Có thể thêm ghi chú phê duyệt
- ✅ Có thể điều chỉnh chi phí sửa chữa
- ❌ Không thể phê duyệt claims của OEM khác

### Admin

- ✅ Có thể phê duyệt tất cả claims
- ✅ Có thể thêm ghi chú phê duyệt
- ✅ Có thể điều chỉnh chi phí sửa chữa
- ✅ Có thể phê duyệt claims của bất kỳ OEM nào

### SC_Staff

- ❌ Không có quyền phê duyệt claim
- ❌ Chỉ có thể tạo và cập nhật claim

### SC_Technician

- ❌ Không có quyền phê duyệt claim
- ❌ Chỉ có thể thực hiện claim được gán

## What Happens After Approval

### Automatic Actions

- Chuyển status từ PENDING_APPROVAL sang APPROVED
- Cập nhật approvedAt timestamp
- Cập nhật approvedBy và approvedByName
- Ghi log trong status history
- Gửi thông báo đến SC Staff

### Manual Actions Required

- SC Staff bắt đầu sửa chữa
- Order phụ tùng cần thiết
- Cập nhật tiến độ sửa chữa
- Upload ảnh chụp quá trình sửa chữa

### Notifications

- Gửi email đến SC Staff về việc phê duyệt
- Gửi SMS đến SC Staff nếu cần
- Tạo notification records trong database
- Cập nhật dashboard của SC Staff

## Validation Rules

### Claim Status

- Phải ở trạng thái PENDING_APPROVAL
- Không được là APPROVED, IN_PROGRESS, COMPLETED, REJECTED, CANCELLED

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Approved Repair Cost

- Phải là số dương
- Không được vượt quá estimatedRepairCost \* 1.5
- Phải hợp lý với loại sửa chữa

### Permission

- Chỉ EVM_Staff và Admin mới có quyền
- SC_Staff và SC_Technician không có quyền
- EVM_Staff chỉ phê duyệt claims của OEM

## Notes

- Endpoint này chỉ dành cho EVM_Staff và Admin
- SC_Staff và SC_Technician không có quyền sử dụng
- Claim phải ở trạng thái PENDING_APPROVAL để phê duyệt
- Sau khi phê duyệt, claim không thể bị từ chối
- Tất cả thay đổi được ghi log để audit
- Phê duyệt claim sẽ trigger các workflow khác
- SC Staff sẽ nhận thông báo về việc phê duyệt
- Phụ tùng có thể được order ngay sau khi phê duyệt
- Claim sẽ chuyển sang trạng thái có thể sửa chữa
- Nếu có ghi chú, sẽ được lưu trong status history
- Chi phí sửa chữa được phê duyệt có thể khác với ước tính
- EVM_Staff chỉ có thể phê duyệt claims của OEM
- Admin có thể phê duyệt claims của bất kỳ OEM nào
