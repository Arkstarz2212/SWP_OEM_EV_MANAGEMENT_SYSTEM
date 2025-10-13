# GET /api/service-centers/{id}

## Mô tả

Lấy thông tin chi tiết của service center theo ID. Endpoint này cho phép xem thông tin đầy đủ về service center bao gồm thông tin cơ bản, capacity, services, và performance metrics.

## Endpoint

```
GET /api/service-centers/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| id        | long | Yes      | ID của service center cần lấy |

## Response

### Success Response (200 OK)

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
    "currentUtilization": 38,
    "utilizationRate": 76.0,
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
    "managerEmail": "john.manager@example.com",
    "managerPhone": "+1-555-0124",
    "status": "ACTIVE",
    "description": "Full-service center providing warranty repairs and maintenance",
    "performance": {
      "averageRating": 4.5,
      "totalServices": 1250,
      "completedServices": 1180,
      "completionRate": 94.4,
      "averageProcessingTime": 2.8,
      "customerSatisfaction": 4.5
    },
    "technicians": {
      "total": 15,
      "active": 12,
      "averageExperience": 5.2,
      "certificationRate": 85.0
    },
    "equipment": [
      {
        "id": 1,
        "name": "Diagnostic Scanner",
        "type": "DIAGNOSTIC",
        "status": "ACTIVE",
        "lastMaintenance": "2024-01-01T00:00:00Z"
      },
      {
        "id": 2,
        "name": "Lift System",
        "type": "LIFT",
        "status": "ACTIVE",
        "lastMaintenance": "2024-01-01T00:00:00Z"
      }
    ],
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "admin@example.com",
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com"
  }
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid service center ID",
  "path": "/api/service-centers/invalid",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Service center không tồn tại

```json
{
  "error": "Service center not found",
  "path": "/api/service-centers/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền truy cập

```json
{
  "error": "Insufficient permissions to view service center",
  "path": "/api/service-centers/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve service center details: Internal server error",
  "path": "/api/service-centers/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem service center trong phạm vi quyền
- **Service Center:** Service center phải tồn tại và active

## Test Cases

### Test Case 1: Lấy thông tin service center thành công

**Request:**

```
GET /api/service-centers/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin service center

### Test Case 2: Service center không tồn tại

**Request:**

```
GET /api/service-centers/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found

### Test Case 3: ID không hợp lệ

**Request:**

```
GET /api/service-centers/invalid
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Không có quyền truy cập

**Request:**

```
GET /api/service-centers/1
Authorization: Bearer sess_unauthorized_token
```

**Expected Response:** 403 Forbidden

### Test Case 5: Service center bị inactive

**Request:**

```
GET /api/service-centers/2
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found (nếu không có quyền xem inactive)

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/service-centers/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/service-centers/1", {
  method: "GET",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
  },
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/service-centers/1"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}

response = requests.get(url, headers=headers)
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

### Bước 2: Lấy thông tin service center

```bash
curl -X GET "http://localhost:8080/api/service-centers/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
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
    "currentUtilization": 38,
    "utilizationRate": 76.0,
    "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION"],
    "status": "ACTIVE"
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

### Capacity Information

- `capacity`: Sức chứa tối đa
- `currentUtilization`: Số lượng hiện tại đang sử dụng
- `utilizationRate`: Tỷ lệ sử dụng (%)

### Services & Hours

- `services`: Danh sách dịch vụ cung cấp
- `workingHours`: Giờ làm việc theo ngày

### Location Information

- `coordinates`: Tọa độ địa lý
  - `latitude`: Vĩ độ
  - `longitude`: Kinh độ

### Management Information

- `managerId`: ID của manager
- `managerName`: Tên manager
- `managerEmail`: Email manager
- `managerPhone`: Số điện thoại manager

### Status Information

- `status`: Trạng thái service center
- `description`: Mô tả service center

### Performance Metrics

- `averageRating`: Đánh giá trung bình (1-5)
- `totalServices`: Tổng số dịch vụ
- `completedServices`: Số dịch vụ đã hoàn thành
- `completionRate`: Tỷ lệ hoàn thành (%)
- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)
- `customerSatisfaction`: Đánh giá khách hàng (1-5)

### Technician Information

- `total`: Tổng số technicians
- `active`: Số technicians đang hoạt động
- `averageExperience`: Kinh nghiệm trung bình (năm)
- `certificationRate`: Tỷ lệ chứng chỉ (%)

### Equipment Information

- `id`: ID thiết bị
- `name`: Tên thiết bị
- `type`: Loại thiết bị
- `status`: Trạng thái thiết bị
- `lastMaintenance`: Lần bảo trì cuối

### Audit Information

- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo
- `updatedAt`: Thời gian cập nhật cuối
- `updatedBy`: Người cập nhật cuối

## Service Center Statuses

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

## Equipment Types

### DIAGNOSTIC

- Thiết bị chẩn đoán
- Scanner, tester
- Phân tích dữ liệu

### LIFT

- Hệ thống nâng
- Cầu nâng, kích
- Nâng hạ xe

### TOOL

- Dụng cụ sửa chữa
- Bộ tool, máy móc
- Thiết bị chuyên dụng

### SAFETY

- Thiết bị an toàn
- Bảo hộ, cứu hộ
- Thiết bị khẩn cấp

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả service centers
- ✅ Có thể xem thông tin nhạy cảm
- ✅ Có thể xem performance metrics

### EVM_Staff

- ✅ Có thể xem service centers của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem service centers OEM khác

### SC_Staff

- ✅ Có thể xem service center được gán
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem service centers khác

### SC_Technician

- ✅ Có thể xem service center được gán
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem service centers khác

## Performance Considerations

### Database Optimization

- Sử dụng index trên service center ID
- Join với bảng users để lấy thông tin manager
- Join với bảng equipment để lấy thiết bị

### Caching

- Cache service center details trong 10 phút
- Invalidate cache khi có thay đổi
- Cache performance metrics riêng biệt

### Data Aggregation

- Tính toán performance metrics real-time
- Sử dụng background jobs cho heavy calculations
- Cache kết quả trong database

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Performance metrics được tính toán real-time
- Equipment information được lấy từ equipment management system
- Technician information được lấy từ user management system
- Manager information được lấy từ user profile
- Utilization rate được tính từ current appointments
- Completion rate được tính từ service history
- Average rating được tính từ customer feedback
- Customer satisfaction được lấy từ feedback system
- Equipment status được cập nhật real-time
- Last maintenance được lấy từ maintenance records
- Audit information được lấy từ audit logs
- Service center phải tồn tại và active để xem được
- Inactive service centers chỉ Admin và EVM_Staff mới xem được
- Performance metrics được cập nhật theo thời gian thực
- Equipment information được cập nhật từ IoT devices
- Technician information được cập nhật từ HR system
