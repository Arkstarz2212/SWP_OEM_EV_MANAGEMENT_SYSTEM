# PUT /api/parts/{id}/status

## Mô tả

Cập nhật trạng thái phụ tùng theo ID. Endpoint này cho phép Admin và EVM_Staff thay đổi trạng thái phụ tùng và tự động thực hiện các hành động liên quan.

## Endpoint

```
PUT /api/parts/{id}/status
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter | Type | Required | Description                  |
| --------- | ---- | -------- | ---------------------------- |
| id        | long | Yes      | ID của phụ tùng cần cập nhật |

### Body

```json
{
  "status": "INACTIVE",
  "reason": "Part discontinued by manufacturer",
  "effectiveDate": "2024-02-01",
  "notes": "Part will be phased out over the next 3 months"
}
```

### Parameters

| Field         | Type   | Required | Description                   |
| ------------- | ------ | -------- | ----------------------------- |
| status        | string | Yes      | Trạng thái mới của phụ tùng   |
| reason        | string | No       | Lý do thay đổi trạng thái     |
| effectiveDate | string | No       | Ngày có hiệu lực (YYYY-MM-DD) |
| notes         | string | No       | Ghi chú bổ sung               |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "status": "INACTIVE",
    "reason": "Part discontinued by manufacturer",
    "effectiveDate": "2024-02-01",
    "notes": "Part will be phased out over the next 3 months",
    "statusHistory": [
      {
        "status": "INACTIVE",
        "changedAt": "2024-01-15T00:00:00Z",
        "changedBy": "evmstaff@example.com",
        "reason": "Part discontinued by manufacturer"
      },
      {
        "status": "ACTIVE",
        "changedAt": "2024-01-01T00:00:00Z",
        "changedBy": "admin@example.com",
        "reason": "Part created"
      }
    ],
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com"
  }
}
```

### Error Responses

#### 400 Bad Request - Validation Error

```json
{
  "error": "Validation failed",
  "details": [
    {
      "field": "status",
      "message": "Invalid status. Valid statuses are: ACTIVE, INACTIVE, DISCONTINUED, OUT_OF_STOCK"
    },
    {
      "field": "effectiveDate",
      "message": "Effective date cannot be in the past"
    }
  ],
  "path": "/api/parts/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Phụ tùng không tồn tại

```json
{
  "error": "Part not found",
  "path": "/api/parts/999/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Status transition không hợp lệ

```json
{
  "error": "Invalid status transition from ACTIVE to DISCONTINUED",
  "path": "/api/parts/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update part status",
  "path": "/api/parts/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update part status: Internal server error",
  "path": "/api/parts/1/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **Part:** Phụ tùng phải tồn tại
- **Status:** Status phải hợp lệ và có thể transition

## Test Cases

### Test Case 1: Cập nhật trạng thái thành công

**Request:**

```json
{
  "status": "INACTIVE",
  "reason": "Part discontinued by manufacturer",
  "effectiveDate": "2024-02-01",
  "notes": "Part will be phased out over the next 3 months"
}
```

**Expected Response:** 200 OK với trạng thái đã cập nhật

### Test Case 2: Cập nhật trạng thái với lý do

**Request:**

```json
{
  "status": "OUT_OF_STOCK",
  "reason": "Temporary stock shortage",
  "effectiveDate": "2024-01-20",
  "notes": "Stock will be replenished in 2 weeks"
}
```

**Expected Response:** 200 OK với trạng thái đã cập nhật

### Test Case 3: Status không hợp lệ

**Request:**

```json
{
  "status": "INVALID_STATUS",
  "reason": "Test invalid status"
}
```

**Expected Response:** 400 Bad Request

### Test Case 4: Phụ tùng không tồn tại

**Request:**

```json
{
  "status": "INACTIVE",
  "reason": "Test status update"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Status transition không hợp lệ

**Request:**

```json
{
  "status": "DISCONTINUED",
  "reason": "Invalid transition from ACTIVE to DISCONTINUED"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/parts/1/status" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INACTIVE",
    "reason": "Part discontinued by manufacturer",
    "effectiveDate": "2024-02-01",
    "notes": "Part will be phased out over the next 3 months"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/parts/1/status", {
  method: "PUT",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    status: "INACTIVE",
    reason: "Part discontinued by manufacturer",
    effectiveDate: "2024-02-01",
    notes: "Part will be phased out over the next 3 months",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/parts/1/status"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "status": "INACTIVE",
    "reason": "Part discontinued by manufacturer",
    "effectiveDate": "2024-02-01",
    "notes": "Part will be phased out over the next 3 months"
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

### Bước 2: Cập nhật trạng thái phụ tùng

```bash
curl -X PUT "http://localhost:8080/api/parts/1/status" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INACTIVE",
    "reason": "Part discontinued by manufacturer",
    "effectiveDate": "2024-02-01",
    "notes": "Part will be phased out over the next 3 months"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "status": "INACTIVE",
    "reason": "Part discontinued by manufacturer",
    "effectiveDate": "2024-02-01",
    "notes": "Part will be phased out over the next 3 months",
    "statusHistory": [
      {
        "status": "INACTIVE",
        "changedAt": "2024-01-15T00:00:00Z",
        "changedBy": "evmstaff@example.com",
        "reason": "Part discontinued by manufacturer"
      },
      {
        "status": "ACTIVE",
        "changedAt": "2024-01-01T00:00:00Z",
        "changedBy": "admin@example.com",
        "reason": "Part created"
      }
    ],
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com"
  }
}
```

## Response Fields Explanation

### Basic Part Information

- `id`: ID duy nhất của phụ tùng
- `partNumber`: Số phụ tùng (duy nhất)
- `name`: Tên phụ tùng
- `description`: Mô tả phụ tùng
- `category`: Danh mục phụ tùng
- `subcategory`: Danh mục con

### OEM Information

- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Status Information

- `status`: Trạng thái phụ tùng (đã cập nhật)
- `reason`: Lý do thay đổi trạng thái
- `effectiveDate`: Ngày có hiệu lực
- `notes`: Ghi chú bổ sung

### Status History

- `status`: Trạng thái tại thời điểm
- `changedAt`: Thời gian thay đổi
- `changedBy`: Người thay đổi
- `reason`: Lý do thay đổi

### Audit Information

- `updatedAt`: Thời gian cập nhật
- `updatedBy`: Người cập nhật

## Status Transitions

### From ACTIVE

- ✅ **INACTIVE** (by EVM_Staff/Admin)
- ✅ **OUT_OF_STOCK** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang DISCONTINUED trực tiếp

### From INACTIVE

- ✅ **ACTIVE** (by EVM_Staff/Admin)
- ✅ **DISCONTINUED** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang OUT_OF_STOCK

### From OUT_OF_STOCK

- ✅ **ACTIVE** (by EVM_Staff/Admin)
- ✅ **INACTIVE** (by EVM_Staff/Admin)
- ❌ Không thể chuyển sang DISCONTINUED trực tiếp

### From DISCONTINUED

- ❌ Không thể chuyển sang bất kỳ trạng thái nào
- ❌ Trạng thái cuối cùng

## Part Statuses

### ACTIVE

- Phụ tùng đang hoạt động
- Có thể sử dụng
- Tất cả chức năng hoạt động

### INACTIVE

- Phụ tùng tạm dừng
- Không thể sử dụng
- Chỉ có thể xem thông tin

### DISCONTINUED

- Phụ tùng ngừng sản xuất
- Không thể sử dụng
- Chỉ có thể xem thông tin

### OUT_OF_STOCK

- Phụ tùng hết hàng
- Không thể sử dụng
- Cần đặt hàng

## Validation Rules

### Status

- Phải là status hợp lệ
- Phải có status transition hợp lệ
- Không được trống

### Reason

- Tối đa 200 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Effective Date

- Phải có format YYYY-MM-DD
- Không được trong quá khứ
- Không được trống nếu được cung cấp

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

## Permission Matrix

### Admin

- ✅ Có thể thay đổi status của tất cả phụ tùng
- ✅ Có thể thực hiện bất kỳ status transition nào
- ✅ Có thể thêm reason và notes

### EVM_Staff

- ✅ Có thể thay đổi status của phụ tùng trong OEM
- ✅ Có thể thực hiện status transition hợp lệ
- ❌ Không thể thay đổi status của phụ tùng OEM khác

### SC_Staff

- ❌ Không có quyền thay đổi status
- ❌ Chỉ có thể xem thông tin phụ tùng

### SC_Technician

- ❌ Không có quyền thay đổi status
- ❌ Chỉ có thể xem thông tin phụ tùng

## Automatic Actions

### Status Update

- Cập nhật status trong database
- Cập nhật updatedAt timestamp
- Tạo audit log

### Status History

- Lưu trữ status history
- Tạo status record
- Cập nhật status timeline

### Inventory Management

- Cập nhật inventory status
- Cập nhật stock availability
- Tạo inventory record

### Notification

- Gửi email đến supplier về thay đổi status
- Gửi thông báo đến EVM_Staff
- Tạo task cho inventory management

## Manual Actions Required

### Supplier Notification

- Supplier cần xác nhận thay đổi
- Cập nhật thông tin part
- Thiết lập pricing mới

### Inventory Update

- Cập nhật inventory system
- Thiết lập stock levels mới
- Cấu hình reorder points

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Status transition phải hợp lệ
- Effective date phải hợp lệ
- Reason và notes là optional
- Status history được lưu trữ đầy đủ
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Supplier notification được gửi về thay đổi status
- Inventory notification được gửi về stock update
- EVM_Staff notification được gửi về status change
- Task được tạo cho inventory management
- Email được gửi đến supplier
- SMS được gửi nếu cần thiết
- Push notification được gửi đến mobile app
- Webhook được gửi đến external systems
