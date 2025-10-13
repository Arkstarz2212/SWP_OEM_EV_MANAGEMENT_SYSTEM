# PUT /api/vehicles/{id}

## Mô tả

Cập nhật thông tin xe theo ID. Endpoint này cho phép Admin, EVM_Staff và SC_Staff cập nhật thông tin xe bao gồm thông tin cơ bản, chủ sở hữu, và thông tin bảo hành.

## Endpoint

```
PUT /api/vehicles/{id}
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
  "color": "Midnight Silver",
  "currentMileage": 20000,
  "ownerName": "John Doe Updated",
  "ownerEmail": "john.doe.updated@example.com",
  "ownerPhone": "+1-555-0124",
  "ownerAddress": "456 Updated Street, New York, NY 10002",
  "serviceCenterId": 2,
  "notes": "Vehicle information updated"
}
```

### Parameters

| Field           | Type    | Required | Description              |
| --------------- | ------- | -------- | ------------------------ |
| color           | string  | No       | Màu sắc xe               |
| currentMileage  | integer | No       | Số km hiện tại           |
| ownerName       | string  | No       | Tên chủ sở hữu           |
| ownerEmail      | string  | No       | Email chủ sở hữu         |
| ownerPhone      | string  | No       | Số điện thoại chủ sở hữu |
| ownerAddress    | string  | No       | Địa chỉ chủ sở hữu       |
| serviceCenterId | long    | No       | ID service center gán    |
| notes           | string  | No       | Ghi chú bổ sung          |

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
    "color": "Midnight Silver",
    "engineType": "Electric",
    "fuelType": "Electric",
    "transmission": "Automatic",
    "ownerName": "John Doe Updated",
    "ownerEmail": "john.doe.updated@example.com",
    "ownerPhone": "+1-555-0124",
    "ownerAddress": "456 Updated Street, New York, NY 10002",
    "purchaseDate": "2023-01-15",
    "purchasePrice": 45000.0,
    "warrantyStartDate": "2023-01-15",
    "warrantyEndDate": "2028-01-15",
    "warrantyMileage": 50000,
    "currentMileage": 20000,
    "warrantyStatus": "ACTIVE",
    "warrantyCoverage": 100.0,
    "remainingMileage": 30000,
    "remainingDays": 1825,
    "serviceCenterId": 2,
    "serviceCenterName": "Uptown Service Center",
    "lastUpdated": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com",
    "notes": "Vehicle information updated"
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
      "field": "ownerEmail",
      "message": "Invalid email format"
    },
    {
      "field": "currentMileage",
      "message": "Current mileage must be greater than or equal to previous mileage"
    }
  ],
  "path": "/api/vehicles/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Xe không tồn tại

```json
{
  "error": "Vehicle not found",
  "path": "/api/vehicles/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Service center không tồn tại

```json
{
  "error": "Service center not found",
  "path": "/api/vehicles/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update vehicle",
  "path": "/api/vehicles/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update vehicle: Internal server error",
  "path": "/api/vehicles/1",
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

### Test Case 1: Cập nhật thông tin cơ bản

**Request:**

```json
{
  "color": "Midnight Silver",
  "currentMileage": 20000,
  "notes": "Vehicle color updated"
}
```

**Expected Response:** 200 OK với thông tin đã cập nhật

### Test Case 2: Cập nhật thông tin chủ sở hữu

**Request:**

```json
{
  "ownerName": "John Doe Updated",
  "ownerEmail": "john.doe.updated@example.com",
  "ownerPhone": "+1-555-0124",
  "ownerAddress": "456 Updated Street, New York, NY 10002"
}
```

**Expected Response:** 200 OK với thông tin chủ sở hữu đã cập nhật

### Test Case 3: Cập nhật service center

**Request:**

```json
{
  "serviceCenterId": 2,
  "notes": "Service center reassigned"
}
```

**Expected Response:** 200 OK với service center mới

### Test Case 4: Xe không tồn tại

**Request:**

```json
{
  "color": "Midnight Silver"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Service center không tồn tại

**Request:**

```json
{
  "serviceCenterId": 999,
  "notes": "Invalid service center"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/vehicles/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "color": "Midnight Silver",
    "currentMileage": 20000,
    "ownerName": "John Doe Updated",
    "ownerEmail": "john.doe.updated@example.com",
    "ownerPhone": "+1-555-0124",
    "ownerAddress": "456 Updated Street, New York, NY 10002",
    "serviceCenterId": 2,
    "notes": "Vehicle information updated"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/vehicles/1", {
  method: "PUT",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    color: "Midnight Silver",
    currentMileage: 20000,
    ownerName: "John Doe Updated",
    ownerEmail: "john.doe.updated@example.com",
    ownerPhone: "+1-555-0124",
    ownerAddress: "456 Updated Street, New York, NY 10002",
    serviceCenterId: 2,
    notes: "Vehicle information updated",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/vehicles/1"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "color": "Midnight Silver",
    "currentMileage": 20000,
    "ownerName": "John Doe Updated",
    "ownerEmail": "john.doe.updated@example.com",
    "ownerPhone": "+1-555-0124",
    "ownerAddress": "456 Updated Street, New York, NY 10002",
    "serviceCenterId": 2,
    "notes": "Vehicle information updated"
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

### Bước 2: Cập nhật thông tin xe

```bash
curl -X PUT "http://localhost:8080/api/vehicles/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "color": "Midnight Silver",
    "currentMileage": 20000,
    "ownerName": "John Doe Updated",
    "ownerEmail": "john.doe.updated@example.com",
    "ownerPhone": "+1-555-0124",
    "ownerAddress": "456 Updated Street, New York, NY 10002",
    "serviceCenterId": 2,
    "notes": "Vehicle information updated"
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
    "color": "Midnight Silver",
    "engineType": "Electric",
    "fuelType": "Electric",
    "transmission": "Automatic",
    "ownerName": "John Doe Updated",
    "ownerEmail": "john.doe.updated@example.com",
    "ownerPhone": "+1-555-0124",
    "ownerAddress": "456 Updated Street, New York, NY 10002",
    "purchaseDate": "2023-01-15",
    "purchasePrice": 45000.0,
    "warrantyStartDate": "2023-01-15",
    "warrantyEndDate": "2028-01-15",
    "warrantyMileage": 50000,
    "currentMileage": 20000,
    "warrantyStatus": "ACTIVE",
    "warrantyCoverage": 100.0,
    "remainingMileage": 30000,
    "remainingDays": 1825,
    "serviceCenterId": 2,
    "serviceCenterName": "Uptown Service Center",
    "lastUpdated": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com",
    "notes": "Vehicle information updated"
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
- `color`: Màu sắc xe (đã cập nhật)
- `engineType`: Loại động cơ
- `fuelType`: Loại nhiên liệu
- `transmission`: Loại hộp số

### Owner Information

- `ownerName`: Tên chủ sở hữu (đã cập nhật)
- `ownerEmail`: Email chủ sở hữu (đã cập nhật)
- `ownerPhone`: Số điện thoại chủ sở hữu (đã cập nhật)
- `ownerAddress`: Địa chỉ chủ sở hữu (đã cập nhật)

### Purchase Information

- `purchaseDate`: Ngày mua xe
- `purchasePrice`: Giá mua xe

### Warranty Information

- `warrantyStartDate`: Ngày bắt đầu bảo hành
- `warrantyEndDate`: Ngày kết thúc bảo hành
- `warrantyMileage`: Số km bảo hành
- `currentMileage`: Số km hiện tại (đã cập nhật)
- `warrantyStatus`: Trạng thái bảo hành
- `warrantyCoverage`: Tỷ lệ bảo hành (%)
- `remainingMileage`: Số km còn lại (đã cập nhật)
- `remainingDays`: Số ngày còn lại

### Service Center Information

- `serviceCenterId`: ID service center (đã cập nhật)
- `serviceCenterName`: Tên service center (đã cập nhật)

### Audit Information

- `lastUpdated`: Thời gian cập nhật
- `updatedBy`: Người cập nhật
- `notes`: Ghi chú bổ sung (đã cập nhật)

## Updatable Fields

### Basic Information

- `color`: Màu sắc xe
- `currentMileage`: Số km hiện tại

### Owner Information

- `ownerName`: Tên chủ sở hữu
- `ownerEmail`: Email chủ sở hữu
- `ownerPhone`: Số điện thoại chủ sở hữu
- `ownerAddress`: Địa chỉ chủ sở hữu

### Service Center Information

- `serviceCenterId`: ID service center
- `notes`: Ghi chú bổ sung

## Validation Rules

### Color

- Không được trống nếu được cung cấp
- Tối đa 30 ký tự
- Phải là màu hợp lệ

### Current Mileage

- Phải là số không âm nếu được cung cấp
- Phải lớn hơn hoặc bằng mileage trước đó
- Tối đa 1,000,000 km

### Owner Information

- `ownerName`: Không được trống nếu được cung cấp, tối đa 100 ký tự
- `ownerEmail`: Phải có format email hợp lệ nếu được cung cấp
- `ownerPhone`: Phải có format số điện thoại hợp lệ nếu được cung cấp
- `ownerAddress`: Không được trống nếu được cung cấp, tối đa 200 ký tự

### Service Center

- `serviceCenterId`: Phải tồn tại trong hệ thống nếu được cung cấp
- Phải active
- Phải có quyền xử lý loại xe này

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

## Permission Matrix

### Admin

- ✅ Có thể cập nhật tất cả xe
- ✅ Có thể thay đổi service center bất kỳ
- ✅ Có thể cập nhật tất cả thông tin

### EVM_Staff

- ✅ Có thể cập nhật xe của OEM
- ✅ Có thể thay đổi service center trong OEM
- ❌ Không thể cập nhật xe OEM khác

### SC_Staff

- ✅ Có thể cập nhật xe của service center
- ✅ Có thể cập nhật thông tin cơ bản
- ❌ Không thể thay đổi service center khác

### SC_Technician

- ❌ Không có quyền cập nhật xe
- ❌ Chỉ có thể xem thông tin xe

## Automatic Actions

### Vehicle Update

- Cập nhật thông tin trong database
- Cập nhật lastUpdated timestamp
- Tạo audit log

### Service Center Change

- Cập nhật service center nếu được cung cấp
- Cập nhật service center capacity
- Tạo assignment record

### Warranty Recalculation

- Tính lại warranty coverage
- Tính lại remaining mileage
- Cập nhật warranty status

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
- Chỉ cập nhật các field được cung cấp
- Các field không được cung cấp sẽ giữ nguyên
- Service center phải tồn tại và active
- Current mileage phải hợp lệ
- Owner information phải đầy đủ và chính xác
- Warranty coverage được tính lại tự động
- Remaining mileage và days được tính lại tự động
- Service center assignment được validate
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
