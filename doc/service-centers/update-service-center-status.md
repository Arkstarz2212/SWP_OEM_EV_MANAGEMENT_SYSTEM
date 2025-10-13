# PUT /api/service-centers/{id}/status

## Mô tả

Cập nhật trạng thái service center theo ID. Endpoint này cho phép Admin và EVM_Staff thay đổi trạng thái service center (ACTIVE, INACTIVE, MAINTENANCE, OVERLOADED).

## Endpoint

```
PUT /api/service-centers/{id}/status
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter | Type | Required | Description                        |
| --------- | ---- | -------- | ---------------------------------- |
| id        | long | Yes      | ID của service center cần cập nhật |

### Body

```json
{
  "status": "MAINTENANCE",
  "reason": "Scheduled maintenance for equipment upgrade",
  "estimatedDuration": "2 days",
  "notes": "All appointments will be rescheduled"
}
```

### Parameters

| Field             | Type   | Required | Description                              |
| ----------------- | ------ | -------- | ---------------------------------------- |
| status            | string | Yes      | Trạng thái mới của service center        |
| reason            | string | No       | Lý do thay đổi trạng thái                |
| estimatedDuration | string | No       | Thời gian ước tính (chỉ cho MAINTENANCE) |
| notes             | string | No       | Ghi chú bổ sung                          |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Downtown Service Center",
    "status": "MAINTENANCE",
    "previousStatus": "ACTIVE",
    "reason": "Scheduled maintenance for equipment upgrade",
    "estimatedDuration": "2 days",
    "notes": "All appointments will be rescheduled",
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com",
    "statusHistory": [
      {
        "status": "ACTIVE",
        "changedAt": "2024-01-01T00:00:00Z",
        "changedBy": "admin@example.com",
        "reason": "Service center activated"
      },
      {
        "status": "MAINTENANCE",
        "changedAt": "2024-01-15T00:00:00Z",
        "changedBy": "evmstaff@example.com",
        "reason": "Scheduled maintenance for equipment upgrade"
      }
    ]
  }
}
```

### Error Responses

#### 400 Bad Request - Status không hợp lệ

```json
{
  "error": "Invalid status. Valid statuses are: ACTIVE, INACTIVE, MAINTENANCE, OVERLOADED",
  "path": "/api/service-centers/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Service center không tồn tại

```json
{
  "error": "Service center not found",
  "path": "/api/service-centers/999/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Status transition không hợp lệ

```json
{
  "error": "Invalid status transition from ACTIVE to INACTIVE",
  "path": "/api/service-centers/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update service center status",
  "path": "/api/service-centers/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update service center status: Internal server error",
  "path": "/api/service-centers/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **Service Center:** Service center phải tồn tại
- **Status Transition:** Status transition phải hợp lệ

## Test Cases

### Test Case 1: Chuyển từ ACTIVE sang MAINTENANCE

**Request:**

```json
{
  "status": "MAINTENANCE",
  "reason": "Scheduled maintenance for equipment upgrade",
  "estimatedDuration": "2 days",
  "notes": "All appointments will be rescheduled"
}
```

**Expected Response:** 200 OK với status mới

### Test Case 2: Chuyển từ MAINTENANCE sang ACTIVE

**Request:**

```json
{
  "status": "ACTIVE",
  "reason": "Maintenance completed successfully",
  "notes": "Service center is now fully operational"
}
```

**Expected Response:** 200 OK với status ACTIVE

### Test Case 3: Chuyển sang OVERLOADED

**Request:**

```json
{
  "status": "OVERLOADED",
  "reason": "High demand period",
  "notes": "Limited capacity available"
}
```

**Expected Response:** 200 OK với status OVERLOADED

### Test Case 4: Status không hợp lệ

**Request:**

```json
{
  "status": "INVALID_STATUS",
  "reason": "Test status change"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Status transition không hợp lệ

**Request:**

```json
{
  "status": "INACTIVE",
  "reason": "Invalid transition"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/service-centers/1/status" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "MAINTENANCE",
    "reason": "Scheduled maintenance for equipment upgrade",
    "estimatedDuration": "2 days",
    "notes": "All appointments will be rescheduled"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/service-centers/1/status",
  {
    method: "PUT",
    headers: {
      Authorization: "Bearer sess_abc123def456ghi789",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      status: "MAINTENANCE",
      reason: "Scheduled maintenance for equipment upgrade",
      estimatedDuration: "2 days",
      notes: "All appointments will be rescheduled",
    }),
  }
);

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/service-centers/1/status"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "status": "MAINTENANCE",
    "reason": "Scheduled maintenance for equipment upgrade",
    "estimatedDuration": "2 days",
    "notes": "All appointments will be rescheduled"
}

response = requests.put(url, headers=headers, json=data)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập để lấy session token

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
    "userId": 2,
    "role": "EVM_Staff",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Cập nhật status service center

```bash
curl -X PUT "http://localhost:8080/api/service-centers/1/status" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "MAINTENANCE",
    "reason": "Scheduled maintenance for equipment upgrade",
    "estimatedDuration": "2 days",
    "notes": "All appointments will be rescheduled"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Downtown Service Center",
    "status": "MAINTENANCE",
    "previousStatus": "ACTIVE",
    "reason": "Scheduled maintenance for equipment upgrade",
    "estimatedDuration": "2 days",
    "notes": "All appointments will be rescheduled",
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com"
  }
}
```

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của service center
- `name`: Tên service center
- `status`: Trạng thái mới
- `previousStatus`: Trạng thái trước đó

### Status Change Information

- `reason`: Lý do thay đổi trạng thái
- `estimatedDuration`: Thời gian ước tính (nếu có)
- `notes`: Ghi chú bổ sung

### Audit Information

- `updatedAt`: Thời gian cập nhật
- `updatedBy`: Người cập nhật

### Status History

- `status`: Trạng thái cũ
- `changedAt`: Thời gian thay đổi
- `changedBy`: Người thay đổi
- `reason`: Lý do thay đổi

## Valid Status Transitions

### From ACTIVE

- ✅ **MAINTENANCE** (by EVM_Staff/Admin)
- ✅ **OVERLOADED** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang INACTIVE trực tiếp

### From MAINTENANCE

- ✅ **ACTIVE** (by EVM_Staff/Admin)
- ✅ **INACTIVE** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang OVERLOADED

### From OVERLOADED

- ✅ **ACTIVE** (by EVM_Staff/Admin)
- ✅ **MAINTENANCE** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang INACTIVE trực tiếp

### From INACTIVE

- ✅ **ACTIVE** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang MAINTENANCE, OVERLOADED

## Status Descriptions

### ACTIVE

- Service center đang hoạt động bình thường
- Có thể nhận dịch vụ mới
- Tất cả chức năng hoạt động

### INACTIVE

- Service center tạm dừng hoạt động
- Không nhận dịch vụ mới
- Chỉ xử lý dịch vụ đang có

### MAINTENANCE

- Service center đang bảo trì
- Không nhận dịch vụ mới
- Chỉ xử lý dịch vụ khẩn cấp

### OVERLOADED

- Service center quá tải
- Hạn chế nhận dịch vụ mới
- Ưu tiên dịch vụ quan trọng

## Validation Rules

### Status

- Phải là status hợp lệ
- Phải có status transition hợp lệ
- Không được trống

### Reason

- Tối đa 200 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Estimated Duration

- Chỉ áp dụng cho MAINTENANCE status
- Tối đa 50 ký tự
- Format: "X days", "X hours", "X weeks"

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

## Permission Matrix

### Admin

- ✅ Có thể thay đổi status của tất cả service centers
- ✅ Có thể thực hiện bất kỳ status transition nào
- ✅ Có thể thêm reason và notes

### EVM_Staff

- ✅ Có thể thay đổi status của service centers trong OEM
- ✅ Có thể thực hiện status transition hợp lệ
- ❌ Không thể thay đổi status của service centers OEM khác

### SC_Staff

- ❌ Không có quyền thay đổi status
- ❌ Chỉ có thể xem thông tin service center

### SC_Technician

- ❌ Không có quyền thay đổi status
- ❌ Chỉ có thể xem thông tin service center

## Automatic Actions

### Status Update

- Cập nhật status trong database
- Cập nhật updatedAt timestamp
- Tạo audit log

### Appointment Management

- Reschedule appointments nếu cần
- Notify customers về thay đổi
- Update appointment status

### Notification

- Gửi email đến manager về thay đổi status
- Gửi thông báo đến customers
- Tạo task cho manager

## Manual Actions Required

### Manager Notification

- Manager cần xác nhận thay đổi
- Cập nhật thông tin cho customers
- Thiết lập lại quy trình

### Service Center Reconfiguration

- Cấu hình lại thiết bị
- Thiết lập lại quy trình
- Đào tạo lại nhân viên

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Status transition phải tuân theo business rules
- Reason và notes là optional nhưng khuyến khích
- Estimated duration chỉ áp dụng cho MAINTENANCE
- Status history được lưu trữ đầy đủ
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Appointments được reschedule tự động
- Customers được notify về thay đổi
- Cache được invalidate khi có thay đổi
- Status transition được validate nghiêm ngặt
- Business rules được áp dụng cho từng transition
- Manager được notify về mọi thay đổi
- Customers được notify về ảnh hưởng
- Appointments được xử lý theo status mới
- Service center capacity được cập nhật
- Utilization rate được tính lại
- Performance metrics được cập nhật
