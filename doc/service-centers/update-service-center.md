# PUT /api/service-centers/{id}

## Mô tả

Cập nhật thông tin service center theo ID. Endpoint này cho phép Admin và EVM_Staff cập nhật thông tin service center bao gồm thông tin cơ bản, capacity, services, và cấu hình.

## Endpoint

```
PUT /api/service-centers/{id}
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
  "name": "Downtown Service Center Updated",
  "address": "123 Main Street, New York, NY 10001",
  "phone": "+1-555-0123",
  "email": "downtown@servicecenter.com",
  "capacity": 60,
  "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
  "workingHours": {
    "monday": "08:00-18:00",
    "tuesday": "08:00-18:00",
    "wednesday": "08:00-18:00",
    "thursday": "08:00-18:00",
    "friday": "08:00-18:00",
    "saturday": "09:00-15:00",
    "sunday": "closed"
  },
  "coordinates": {
    "latitude": 40.7128,
    "longitude": -74.006
  },
  "managerId": 6,
  "description": "Updated full-service center with expanded facilities"
}
```

### Parameters

| Field        | Type    | Required | Description                |
| ------------ | ------- | -------- | -------------------------- |
| name         | string  | No       | Tên service center         |
| address      | string  | No       | Địa chỉ service center     |
| phone        | string  | No       | Số điện thoại liên hệ      |
| email        | string  | No       | Email liên hệ              |
| capacity     | integer | No       | Sức chứa tối đa            |
| services     | array   | No       | Danh sách dịch vụ cung cấp |
| workingHours | object  | No       | Giờ làm việc theo ngày     |
| coordinates  | object  | No       | Tọa độ địa lý              |
| managerId    | long    | No       | ID của manager             |
| description  | string  | No       | Mô tả service center       |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Downtown Service Center Updated",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "capacity": 60,
    "currentUtilization": 38,
    "utilizationRate": 63.3,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
    "workingHours": {
      "monday": "08:00-18:00",
      "tuesday": "08:00-18:00",
      "wednesday": "08:00-18:00",
      "thursday": "08:00-18:00",
      "friday": "08:00-18:00",
      "saturday": "09:00-15:00",
      "sunday": "closed"
    },
    "coordinates": {
      "latitude": 40.7128,
      "longitude": -74.006
    },
    "managerId": 6,
    "managerName": "Jane Manager",
    "managerEmail": "jane.manager@example.com",
    "managerPhone": "+1-555-0125",
    "status": "ACTIVE",
    "description": "Updated full-service center with expanded facilities",
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
      "field": "name",
      "message": "Name is required"
    },
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ],
  "path": "/api/service-centers/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Service center không tồn tại

```json
{
  "error": "Service center not found",
  "path": "/api/service-centers/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Manager không tồn tại

```json
{
  "error": "Manager not found",
  "path": "/api/service-centers/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update service center",
  "path": "/api/service-centers/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update service center: Internal server error",
  "path": "/api/service-centers/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **Service Center:** Service center phải tồn tại
- **Manager:** Manager phải tồn tại và có role phù hợp (nếu được cung cấp)

## Test Cases

### Test Case 1: Cập nhật thông tin cơ bản

**Request:**

```json
{
  "name": "Downtown Service Center Updated",
  "capacity": 60,
  "description": "Updated full-service center with expanded facilities"
}
```

**Expected Response:** 200 OK với thông tin đã cập nhật

### Test Case 2: Cập nhật services

**Request:**

```json
{
  "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
  "workingHours": {
    "monday": "08:00-18:00",
    "tuesday": "08:00-18:00",
    "wednesday": "08:00-18:00",
    "thursday": "08:00-18:00",
    "friday": "08:00-18:00",
    "saturday": "09:00-15:00",
    "sunday": "closed"
  }
}
```

**Expected Response:** 200 OK với services và working hours đã cập nhật

### Test Case 3: Cập nhật manager

**Request:**

```json
{
  "managerId": 6,
  "description": "Service center with new manager"
}
```

**Expected Response:** 200 OK với manager mới

### Test Case 4: Service center không tồn tại

**Request:**

```json
{
  "name": "Updated Service Center"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Manager không tồn tại

**Request:**

```json
{
  "managerId": 999,
  "description": "Service center with invalid manager"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/service-centers/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Downtown Service Center Updated",
    "capacity": 60,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
    "description": "Updated full-service center with expanded facilities"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/service-centers/1", {
  method: "PUT",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    name: "Downtown Service Center Updated",
    capacity: 60,
    services: ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
    description: "Updated full-service center with expanded facilities",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/service-centers/1"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "name": "Downtown Service Center Updated",
    "capacity": 60,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
    "description": "Updated full-service center with expanded facilities"
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

### Bước 2: Cập nhật service center

```bash
curl -X PUT "http://localhost:8080/api/service-centers/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Downtown Service Center Updated",
    "capacity": 60,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
    "description": "Updated full-service center with expanded facilities"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Downtown Service Center Updated",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "capacity": 60,
    "currentUtilization": 38,
    "utilizationRate": 63.3,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION", "DIAGNOSTICS"],
    "status": "ACTIVE",
    "description": "Updated full-service center with expanded facilities",
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com"
  }
}
```

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của service center
- `name`: Tên service center (đã cập nhật)
- `address`: Địa chỉ service center
- `phone`: Số điện thoại liên hệ
- `email`: Email liên hệ

### OEM Information

- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Capacity Information

- `capacity`: Sức chứa tối đa (đã cập nhật)
- `currentUtilization`: Số lượng hiện tại đang sử dụng
- `utilizationRate`: Tỷ lệ sử dụng (%)

### Services & Hours

- `services`: Danh sách dịch vụ cung cấp (đã cập nhật)
- `workingHours`: Giờ làm việc theo ngày (đã cập nhật)

### Location Information

- `coordinates`: Tọa độ địa lý (đã cập nhật)
  - `latitude`: Vĩ độ
  - `longitude`: Kinh độ

### Management Information

- `managerId`: ID của manager (đã cập nhật)
- `managerName`: Tên manager (đã cập nhật)
- `managerEmail`: Email manager (đã cập nhật)
- `managerPhone`: Số điện thoại manager (đã cập nhật)

### Status Information

- `status`: Trạng thái service center
- `description`: Mô tả service center (đã cập nhật)

### Audit Information

- `updatedAt`: Thời gian cập nhật
- `updatedBy`: Người cập nhật

## Updatable Fields

### Basic Information

- `name`: Tên service center
- `address`: Địa chỉ service center
- `phone`: Số điện thoại liên hệ
- `email`: Email liên hệ

### Capacity & Services

- `capacity`: Sức chứa tối đa
- `services`: Danh sách dịch vụ cung cấp
- `workingHours`: Giờ làm việc theo ngày

### Location Information

- `coordinates`: Tọa độ địa lý
  - `latitude`: Vĩ độ
  - `longitude`: Kinh độ

### Management Information

- `managerId`: ID của manager
- `description`: Mô tả service center

## Validation Rules

### Name

- Không được trống nếu được cung cấp
- Tối đa 100 ký tự
- Phải duy nhất trong OEM

### Address

- Không được trống nếu được cung cấp
- Tối đa 200 ký tự
- Phải có địa chỉ hợp lệ

### Phone

- Không được trống nếu được cung cấp
- Phải có format hợp lệ
- Tối đa 20 ký tự

### Email

- Không được trống nếu được cung cấp
- Phải có format email hợp lệ
- Phải duy nhất

### Capacity

- Phải là số dương nếu được cung cấp
- Tối đa 1000
- Phải hợp lý

### Services

- Phải có ít nhất 1 service nếu được cung cấp
- Chỉ được chọn từ danh sách có sẵn
- Không được trống

### Working Hours

- Phải có đầy đủ 7 ngày nếu được cung cấp
- Format phải hợp lệ
- Không được trống

### Coordinates

- Latitude: -90 đến 90 nếu được cung cấp
- Longitude: -180 đến 180 nếu được cung cấp
- Phải là số thực

### Manager

- Phải tồn tại trong hệ thống nếu được cung cấp
- Phải có role phù hợp
- Phải active

## Permission Matrix

### Admin

- ✅ Có thể cập nhật tất cả service centers
- ✅ Có thể thay đổi manager bất kỳ
- ✅ Có thể cập nhật tất cả thông tin

### EVM_Staff

- ✅ Có thể cập nhật service centers của OEM
- ✅ Có thể thay đổi manager trong OEM
- ❌ Không thể cập nhật service centers OEM khác

### SC_Staff

- ❌ Không có quyền cập nhật service center
- ❌ Chỉ có thể xem thông tin service center

### SC_Technician

- ❌ Không có quyền cập nhật service center
- ❌ Chỉ có thể xem thông tin service center

## Automatic Actions

### Service Center Update

- Cập nhật thông tin trong database
- Cập nhật updatedAt timestamp
- Tạo audit log

### Manager Change

- Cập nhật manager nếu được cung cấp
- Cập nhật quyền của manager cũ và mới
- Gửi thông báo đến manager mới và cũ

### Notification

- Gửi email đến manager về thay đổi
- Gửi thông báo đến EVM_Staff
- Tạo task cho manager mới

## Manual Actions Required

### Manager Notification

- Manager mới cần đăng nhập và xác nhận
- Cập nhật thông tin cá nhân
- Thiết lập mật khẩu

### Service Center Reconfiguration

- Cấu hình lại thiết bị
- Thiết lập lại quy trình
- Đào tạo lại nhân viên

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Chỉ cập nhật các field được cung cấp
- Các field không được cung cấp sẽ giữ nguyên
- Manager phải có role phù hợp
- Service center phải tồn tại và active
- Services phải được chọn từ danh sách có sẵn
- Working hours phải đầy đủ và hợp lệ
- Coordinates là optional
- Description là optional
- Service center name phải duy nhất trong OEM
- Email phải duy nhất trong hệ thống
- Phone phải có format hợp lệ
- Capacity phải hợp lý với loại service center
- Manager phải có quyền quản lý service center
- Cập nhật sẽ ảnh hưởng đến utilization rate
- Thay đổi services sẽ ảnh hưởng đến appointments
- Thay đổi working hours sẽ ảnh hưởng đến scheduling
- Thay đổi manager sẽ ảnh hưởng đến permissions
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
