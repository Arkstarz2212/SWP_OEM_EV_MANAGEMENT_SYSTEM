# PUT /api/vehicles/{id}/mileage

## Mô tả

Cập nhật số km hiện tại của xe theo ID. Endpoint này cho phép Admin, EVM_Staff và SC_Staff cập nhật số km hiện tại của xe và tự động tính toán lại warranty coverage.

## Endpoint

```
PUT /api/vehicles/{id}/mileage
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter | Type | Required | Description            |
| --------- | ---- | -------- | ---------------------- |
| id        | long | Yes      | ID của xe cần cập nhật |

### Body

```json
{
  "currentMileage": 25000,
  "updateReason": "Regular service update",
  "serviceDate": "2024-01-15",
  "serviceCenterId": 1,
  "notes": "Mileage updated during routine maintenance"
}
```

### Parameters

| Field           | Type    | Required | Description                          |
| --------------- | ------- | -------- | ------------------------------------ |
| currentMileage  | integer | Yes      | Số km hiện tại                       |
| updateReason    | string  | No       | Lý do cập nhật                       |
| serviceDate     | string  | No       | Ngày dịch vụ (YYYY-MM-DD)            |
| serviceCenterId | long    | No       | ID service center thực hiện cập nhật |
| notes           | string  | No       | Ghi chú bổ sung                      |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "vin": "1HGBH41JXMN109186",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "color": "Pearl White",
    "currentMileage": 25000,
    "previousMileage": 20000,
    "mileageDifference": 5000,
    "warrantyMileage": 50000,
    "remainingMileage": 25000,
    "warrantyCoverage": 100.0,
    "warrantyStatus": "ACTIVE",
    "updateReason": "Regular service update",
    "serviceDate": "2024-01-15",
    "serviceCenterId": 1,
    "serviceCenterName": "Downtown Service Center",
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com",
    "notes": "Mileage updated during routine maintenance",
    "mileageHistory": [
      {
        "mileage": 25000,
        "updatedAt": "2024-01-15T00:00:00Z",
        "updatedBy": "scstaff@example.com",
        "reason": "Regular service update"
      },
      {
        "mileage": 20000,
        "updatedAt": "2023-12-01T00:00:00Z",
        "updatedBy": "scstaff@example.com",
        "reason": "Oil change service"
      }
    ]
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
      "field": "currentMileage",
      "message": "Current mileage must be greater than or equal to previous mileage"
    },
    {
      "field": "serviceDate",
      "message": "Service date cannot be in the future"
    }
  ],
  "path": "/api/vehicles/1/mileage",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Xe không tồn tại

```json
{
  "error": "Vehicle not found",
  "path": "/api/vehicles/999/mileage",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Service center không tồn tại

```json
{
  "error": "Service center not found",
  "path": "/api/vehicles/1/mileage",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update vehicle mileage",
  "path": "/api/vehicles/1/mileage",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update vehicle mileage: Internal server error",
  "path": "/api/vehicles/1/mileage",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin, EVM_Staff hoặc SC_Staff role
- **Vehicle:** Xe phải tồn tại
- **Service Center:** Service center phải tồn tại (nếu được cung cấp)

## Test Cases

### Test Case 1: Cập nhật số km thành công

**Request:**

```json
{
  "currentMileage": 25000,
  "updateReason": "Regular service update",
  "serviceDate": "2024-01-15",
  "serviceCenterId": 1,
  "notes": "Mileage updated during routine maintenance"
}
```

**Expected Response:** 200 OK với thông tin đã cập nhật

### Test Case 2: Cập nhật số km với lý do

**Request:**

```json
{
  "currentMileage": 30000,
  "updateReason": "Oil change service",
  "serviceDate": "2024-01-20",
  "notes": "Mileage updated during oil change"
}
```

**Expected Response:** 200 OK với thông tin đã cập nhật

### Test Case 3: Số km không hợp lệ

**Request:**

```json
{
  "currentMileage": 15000,
  "updateReason": "Invalid mileage update"
}
```

**Expected Response:** 400 Bad Request

### Test Case 4: Xe không tồn tại

**Request:**

```json
{
  "currentMileage": 25000,
  "updateReason": "Test update"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Service center không tồn tại

**Request:**

```json
{
  "currentMileage": 25000,
  "updateReason": "Test update",
  "serviceCenterId": 999
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/vehicles/1/mileage" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "currentMileage": 25000,
    "updateReason": "Regular service update",
    "serviceDate": "2024-01-15",
    "serviceCenterId": 1,
    "notes": "Mileage updated during routine maintenance"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/vehicles/1/mileage", {
  method: "PUT",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    currentMileage: 25000,
    updateReason: "Regular service update",
    serviceDate: "2024-01-15",
    serviceCenterId: 1,
    notes: "Mileage updated during routine maintenance",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/vehicles/1/mileage"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "currentMileage": 25000,
    "updateReason": "Regular service update",
    "serviceDate": "2024-01-15",
    "serviceCenterId": 1,
    "notes": "Mileage updated during routine maintenance"
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
    "username": "scstaff@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 3,
    "role": "SC_Staff",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Cập nhật số km

```bash
curl -X PUT "http://localhost:8080/api/vehicles/1/mileage" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "currentMileage": 25000,
    "updateReason": "Regular service update",
    "serviceDate": "2024-01-15",
    "serviceCenterId": 1,
    "notes": "Mileage updated during routine maintenance"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "vin": "1HGBH41JXMN109186",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "color": "Pearl White",
    "currentMileage": 25000,
    "previousMileage": 20000,
    "mileageDifference": 5000,
    "warrantyMileage": 50000,
    "remainingMileage": 25000,
    "warrantyCoverage": 100.0,
    "warrantyStatus": "ACTIVE",
    "updateReason": "Regular service update",
    "serviceDate": "2024-01-15",
    "serviceCenterId": 1,
    "serviceCenterName": "Downtown Service Center",
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com",
    "notes": "Mileage updated during routine maintenance"
  }
}
```

## Response Fields Explanation

### Basic Vehicle Information

- `id`: ID duy nhất của xe
- `vin`: VIN của xe (17 ký tự)
- `make`: Hãng xe
- `model`: Model xe
- `year`: Năm sản xuất
- `color`: Màu sắc xe

### Mileage Information

- `currentMileage`: Số km hiện tại (đã cập nhật)
- `previousMileage`: Số km trước đó
- `mileageDifference`: Chênh lệch số km
- `warrantyMileage`: Số km bảo hành
- `remainingMileage`: Số km còn lại (đã cập nhật)

### Warranty Information

- `warrantyCoverage`: Tỷ lệ bảo hành (%)
- `warrantyStatus`: Trạng thái bảo hành

### Update Information

- `updateReason`: Lý do cập nhật
- `serviceDate`: Ngày dịch vụ
- `serviceCenterId`: ID service center
- `serviceCenterName`: Tên service center

### Audit Information

- `updatedAt`: Thời gian cập nhật
- `updatedBy`: Người cập nhật
- `notes`: Ghi chú bổ sung

### Mileage History

- `mileage`: Số km tại thời điểm
- `updatedAt`: Thời gian cập nhật
- `updatedBy`: Người cập nhật
- `reason`: Lý do cập nhật

## Validation Rules

### Current Mileage

- Phải là số không âm
- Phải lớn hơn hoặc bằng mileage trước đó
- Tối đa 1,000,000 km
- Phải hợp lý với loại xe

### Update Reason

- Tối đa 200 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Service Date

- Phải có format YYYY-MM-DD
- Không được trong tương lai
- Không được trống nếu được cung cấp

### Service Center

- Phải tồn tại trong hệ thống nếu được cung cấp
- Phải active
- Phải có quyền xử lý loại xe này

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

## Permission Matrix

### Admin

- ✅ Có thể cập nhật số km của tất cả xe
- ✅ Có thể cập nhật với bất kỳ service center nào
- ✅ Có thể cập nhật với đầy đủ thông tin

### EVM_Staff

- ✅ Có thể cập nhật số km của xe trong OEM
- ✅ Có thể cập nhật với service center trong OEM
- ❌ Không thể cập nhật xe OEM khác

### SC_Staff

- ✅ Có thể cập nhật số km của xe trong service center
- ✅ Có thể cập nhật với service center của mình
- ❌ Không thể cập nhật xe service center khác

### SC_Technician

- ❌ Không có quyền cập nhật số km
- ❌ Chỉ có thể xem thông tin xe

## Automatic Actions

### Mileage Update

- Cập nhật số km trong database
- Cập nhật updatedAt timestamp
- Tạo audit log

### Warranty Recalculation

- Tính lại warranty coverage
- Tính lại remaining mileage
- Cập nhật warranty status

### Service Center Assignment

- Cập nhật service center nếu được cung cấp
- Cập nhật service center capacity
- Tạo assignment record

### Notification

- Gửi email đến owner về thay đổi
- Gửi thông báo đến service center
- Tạo task cho service center

## Manual Actions Required

### Owner Notification

- Owner cần xác nhận thay đổi
- Cập nhật thông tin cá nhân
- Thiết lập tài khoản

### Service Center Setup

- Service center cần xác nhận assignment
- Cập nhật thông tin xe
- Thiết lập quy trình

## Notes

- Endpoint này dành cho Admin, EVM_Staff và SC_Staff
- SC_Technician không có quyền sử dụng
- Current mileage phải hợp lệ
- Service center phải tồn tại và active
- Warranty coverage được tính lại tự động
- Remaining mileage được tính lại tự động
- Mileage history được lưu trữ đầy đủ
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Owner notification được gửi về mọi thay đổi
- Service center notification được gửi về assignment
- Warranty recalculation được thực hiện tự động
- Service center capacity được cập nhật
- Assignment record được tạo
- Task được tạo cho service center
- Email được gửi đến owner
- SMS được gửi nếu cần thiết
- Push notification được gửi đến mobile app
- Webhook được gửi đến external systems
