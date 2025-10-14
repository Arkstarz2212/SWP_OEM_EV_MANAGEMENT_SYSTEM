# POST /api/service-centers

## Mô tả

Tạo service center mới trong hệ thống AOEM. Endpoint này cho phép Admin và EVM_Staff tạo service center với thông tin chi tiết bao gồm địa chỉ, liên hệ, và cấu hình.

## Endpoint

```
POST /api/service-centers
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Body

```json
{
  "name": "Downtown Service Center",
  "address": "123 Main Street, New York, NY 10001",
  "phone": "+1-555-0123",
  "email": "downtown@servicecenter.com",
  "oemId": 1,
  "capacity": 50,
  "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION"],
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
  "managerId": 5,
  "description": "Full-service center providing warranty repairs and maintenance"
}
```

### Parameters

| Field        | Type    | Required | Description                |
| ------------ | ------- | -------- | -------------------------- |
| name         | string  | Yes      | Tên service center         |
| address      | string  | Yes      | Địa chỉ service center     |
| phone        | string  | Yes      | Số điện thoại liên hệ      |
| email        | string  | Yes      | Email liên hệ              |
| oemId        | long    | Yes      | ID của OEM                 |
| capacity     | integer | Yes      | Sức chứa tối đa            |
| services     | array   | Yes      | Danh sách dịch vụ cung cấp |
| workingHours | object  | Yes      | Giờ làm việc theo ngày     |
| coordinates  | object  | No       | Tọa độ địa lý              |
| managerId    | long    | No       | ID của manager             |
| description  | string  | No       | Mô tả service center       |

## Response

### Success Response (201 Created)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Downtown Service Center",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "capacity": 50,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION"],
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
    "managerId": 5,
    "managerName": "John Manager",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "admin@example.com"
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
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - OEM không tồn tại

```json
{
  "error": "OEM not found",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Manager không tồn tại

```json
{
  "error": "Manager not found",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to create service center",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to create service center: Internal server error",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **OEM:** OEM phải tồn tại trong hệ thống
- **Manager:** Manager phải tồn tại và có role phù hợp

## Test Cases

### Test Case 1: Tạo service center thành công

**Request:**

```json
{
  "name": "Downtown Service Center",
  "address": "123 Main Street, New York, NY 10001",
  "phone": "+1-555-0123",
  "email": "downtown@servicecenter.com",
  "oemId": 1,
  "capacity": 50,
  "services": ["WARRANTY_REPAIR", "MAINTENANCE"],
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

**Expected Response:** 201 Created với service center mới

### Test Case 2: Tạo service center với manager

**Request:**

```json
{
  "name": "Uptown Service Center",
  "address": "456 Oak Avenue, Los Angeles, CA 90210",
  "phone": "+1-555-0456",
  "email": "uptown@servicecenter.com",
  "oemId": 1,
  "capacity": 75,
  "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION"],
  "workingHours": {
    "monday": "08:00-18:00",
    "tuesday": "08:00-18:00",
    "wednesday": "08:00-18:00",
    "thursday": "08:00-18:00",
    "friday": "08:00-18:00",
    "saturday": "09:00-15:00",
    "sunday": "closed"
  },
  "managerId": 5,
  "description": "Premium service center with full facilities"
}
```

**Expected Response:** 201 Created với service center và manager

### Test Case 3: Tạo service center với tọa độ

**Request:**

```json
{
  "name": "Central Service Center",
  "address": "789 Pine Street, Chicago, IL 60601",
  "phone": "+1-555-0789",
  "email": "central@servicecenter.com",
  "oemId": 1,
  "capacity": 100,
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
    "latitude": 41.8781,
    "longitude": -87.6298
  }
}
```

**Expected Response:** 201 Created với service center và coordinates

### Test Case 4: Validation error - thiếu name

**Request:**

```json
{
  "address": "123 Main Street, New York, NY 10001",
  "phone": "+1-555-0123",
  "email": "downtown@servicecenter.com",
  "oemId": 1,
  "capacity": 50,
  "services": ["WARRANTY_REPAIR", "MAINTENANCE"]
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: OEM không tồn tại

**Request:**

```json
{
  "name": "Test Service Center",
  "address": "123 Main Street, New York, NY 10001",
  "phone": "+1-555-0123",
  "email": "test@servicecenter.com",
  "oemId": 999,
  "capacity": 50,
  "services": ["WARRANTY_REPAIR", "MAINTENANCE"]
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/service-centers" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Downtown Service Center",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "capacity": 50,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE"],
    "workingHours": {
      "monday": "08:00-18:00",
      "tuesday": "08:00-18:00",
      "wednesday": "08:00-18:00",
      "thursday": "08:00-18:00",
      "friday": "08:00-18:00",
      "saturday": "09:00-15:00",
      "sunday": "closed"
    }
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/service-centers", {
  method: "POST",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    name: "Downtown Service Center",
    address: "123 Main Street, New York, NY 10001",
    phone: "+1-555-0123",
    email: "downtown@servicecenter.com",
    oemId: 1,
    capacity: 50,
    services: ["WARRANTY_REPAIR", "MAINTENANCE"],
    workingHours: {
      monday: "08:00-18:00",
      tuesday: "08:00-18:00",
      wednesday: "08:00-18:00",
      thursday: "08:00-18:00",
      friday: "08:00-18:00",
      saturday: "09:00-15:00",
      sunday: "closed",
    },
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/service-centers"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "name": "Downtown Service Center",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "capacity": 50,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE"],
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

response = requests.post(url, headers=headers, json=data)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập để lấy session token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "role": "Admin",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Tạo service center

```bash
curl -X POST "http://localhost:8080/api/service-centers" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Downtown Service Center",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "capacity": 50,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE"],
    "workingHours": {
      "monday": "08:00-18:00",
      "tuesday": "08:00-18:00",
      "wednesday": "08:00-18:00",
      "thursday": "08:00-18:00",
      "friday": "08:00-18:00",
      "saturday": "09:00-15:00",
      "sunday": "closed"
    }
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Downtown Service Center",
    "address": "123 Main Street, New York, NY 10001",
    "phone": "+1-555-0123",
    "email": "downtown@servicecenter.com",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "capacity": 50,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE"],
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "admin@example.com"
  }
}
```

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của service center
- `name`: Tên service center
- `address`: Địa chỉ service center
- `phone`: Số điện thoại liên hệ
- `email`: Email liên hệ

### OEM Information

- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Capacity & Services

- `capacity`: Sức chứa tối đa
- `services`: Danh sách dịch vụ cung cấp
- `workingHours`: Giờ làm việc theo ngày

### Location Information

- `coordinates`: Tọa độ địa lý (nếu có)
  - `latitude`: Vĩ độ
  - `longitude`: Kinh độ

### Management Information

- `managerId`: ID của manager (nếu có)
- `managerName`: Tên manager (nếu có)

### Status Information

- `status`: Trạng thái service center
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo

## Services Available

### WARRANTY_REPAIR

- Sửa chữa bảo hành
- Thay thế phụ tùng
- Chẩn đoán lỗi

### MAINTENANCE

- Bảo trì định kỳ
- Thay dầu nhớt
- Kiểm tra tổng thể

### INSPECTION

- Kiểm tra an toàn
- Kiểm tra khí thải
- Kiểm tra kỹ thuật

### DIAGNOSTICS

- Chẩn đoán điện tử
- Kiểm tra hệ thống
- Phân tích dữ liệu

## Working Hours Format

### Standard Format

- `"08:00-18:00"`: Từ 8:00 đến 18:00
- `"09:00-15:00"`: Từ 9:00 đến 15:00
- `"closed"`: Đóng cửa

### Days of Week

- `monday`: Thứ 2
- `tuesday`: Thứ 3
- `wednesday`: Thứ 4
- `thursday`: Thứ 5
- `friday`: Thứ 6
- `saturday`: Thứ 7
- `sunday`: Chủ nhật

## Validation Rules

### Name

- Không được trống
- Tối đa 100 ký tự
- Phải duy nhất trong OEM

### Address

- Không được trống
- Tối đa 200 ký tự
- Phải có địa chỉ hợp lệ

### Phone

- Không được trống
- Phải có format hợp lệ
- Tối đa 20 ký tự

### Email

- Không được trống
- Phải có format email hợp lệ
- Phải duy nhất

### OEM

- Phải tồn tại trong hệ thống
- Phải active

### Capacity

- Phải là số dương
- Tối đa 1000
- Phải hợp lý

### Services

- Phải có ít nhất 1 service
- Chỉ được chọn từ danh sách có sẵn
- Không được trống

### Working Hours

- Phải có đầy đủ 7 ngày
- Format phải hợp lệ
- Không được trống

### Coordinates

- Latitude: -90 đến 90
- Longitude: -180 đến 180
- Phải là số thực

### Manager

- Phải tồn tại trong hệ thống
- Phải có role phù hợp
- Phải active

## Permission Matrix

### Admin

- ✅ Có thể tạo service center cho bất kỳ OEM nào
- ✅ Có thể gán manager bất kỳ
- ✅ Có thể tạo service center với đầy đủ thông tin

### EVM_Staff

- ✅ Có thể tạo service center cho OEM của mình
- ✅ Có thể gán manager trong OEM
- ❌ Không thể tạo service center cho OEM khác

### SC_Staff

- ❌ Không có quyền tạo service center
- ❌ Chỉ có thể quản lý service center được gán

### SC_Technician

- ❌ Không có quyền tạo service center
- ❌ Chỉ có thể xem thông tin service center

## Automatic Actions

### Service Center Creation

- Tạo service center với status ACTIVE
- Gán ID duy nhất
- Lưu thông tin vào database
- Tạo audit log

### Manager Assignment

- Gán manager nếu được cung cấp
- Cập nhật quyền của manager
- Gửi thông báo đến manager

### Notification

- Gửi email đến manager về service center mới
- Gửi thông báo đến EVM_Staff
- Tạo task cho manager

## Manual Actions Required

### Manager Setup

- Manager cần đăng nhập và xác nhận
- Cập nhật thông tin cá nhân
- Thiết lập mật khẩu

### Service Center Configuration

- Cấu hình thiết bị
- Thiết lập quy trình
- Đào tạo nhân viên

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Service center được tạo với status ACTIVE
- Manager phải có role phù hợp
- OEM phải tồn tại và active
- Services phải được chọn từ danh sách có sẵn
- Working hours phải đầy đủ và hợp lệ
- Coordinates là optional nhưng khuyến khích
- Description là optional
- Service center name phải duy nhất trong OEM
- Email phải duy nhất trong hệ thống
- Phone phải có format hợp lệ
- Capacity phải hợp lý với loại service center
- Manager phải có quyền quản lý service center
