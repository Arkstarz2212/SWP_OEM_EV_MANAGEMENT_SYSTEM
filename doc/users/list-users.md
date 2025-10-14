# GET /api/users

## Mô tả

Lấy danh sách người dùng với khả năng filter theo role, service center và hỗ trợ pagination. Endpoint này cho phép Admin và EVM_Staff quản lý danh sách người dùng trong hệ thống.

## Endpoint

```
GET /api/users
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type    | Required | Description                                                  |
| --------------- | ------- | -------- | ------------------------------------------------------------ |
| role            | string  | No       | Filter theo role (Admin, EVM_Staff, SC_Staff, SC_Technician) |
| serviceCenterId | long    | No       | Filter theo service center ID                                |
| limit           | integer | No       | Số lượng kết quả (1-100, mặc định 20)                        |
| offset          | integer | No       | Vị trí bắt đầu (mặc định 0)                                  |

## Response

### Success Response (200 OK)

```json
{
  "users": [
    {
      "id": 1,
      "email": "admin@example.com",
      "fullName": "System Administrator",
      "role": "Admin",
      "serviceCenterId": null,
      "serviceCenterName": null,
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00Z"
    },
    {
      "id": 2,
      "email": "scstaff@service.com",
      "fullName": "Service Center Staff",
      "role": "SC_Staff",
      "serviceCenterId": 1,
      "serviceCenterName": "Downtown Service Center",
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "total": 2,
  "offset": 0,
  "limit": 20
}
```

### Error Responses

#### 400 Bad Request - Limit không hợp lệ

```json
{
  "error": "Limit must be between 1 and 100",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Offset không hợp lệ

```json
{
  "error": "Offset must be 0 or greater",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Service center ID không hợp lệ

```json
{
  "error": "Invalid service center ID",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve users: Internal server error",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với quyền Admin hoặc EVM_Staff
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token hợp lệ
- **Permission:** Chỉ Admin và EVM_Staff mới có quyền xem danh sách người dùng

## Test Cases

### Test Case 1: Lấy tất cả người dùng

**Request:**

```
GET /api/users
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách tất cả người dùng

### Test Case 2: Filter theo role SC_Staff

**Request:**

```
GET /api/users?role=SC_Staff
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ SC_Staff

### Test Case 3: Filter theo service center

**Request:**

```
GET /api/users?serviceCenterId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với người dùng trong service center 1

### Test Case 4: Pagination với limit và offset

**Request:**

```
GET /api/users?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 người dùng từ vị trí 20

### Test Case 5: Limit vượt quá 100

**Request:**

```
GET /api/users?limit=150
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/users?role=SC_Staff&limit=10&offset=0" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/users?role=SC_Staff&limit=10&offset=0",
  {
    method: "GET",
    headers: {
      Authorization: "Bearer sess_abc123def456ghi789",
    },
  }
);

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/users"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "role": "SC_Staff",
    "limit": 10,
    "offset": 0
}

response = requests.get(url, headers=headers, params=params)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập với quyền Admin

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@example.com",
    "password": "admin123"
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

### Bước 2: Lấy danh sách tất cả người dùng

```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "users": [
    {
      "id": 1,
      "email": "admin@example.com",
      "fullName": "System Administrator",
      "role": "Admin",
      "serviceCenterId": null,
      "serviceCenterName": null,
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00Z"
    },
    {
      "id": 2,
      "email": "scstaff@service.com",
      "fullName": "Service Center Staff",
      "role": "SC_Staff",
      "serviceCenterId": 1,
      "serviceCenterName": "Downtown Service Center",
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "total": 2,
  "offset": 0,
  "limit": 20
}
```

### Bước 3: Filter theo role SC_Staff

```bash
curl -X GET "http://localhost:8080/api/users?role=SC_Staff" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "users": [
    {
      "id": 2,
      "email": "scstaff@service.com",
      "fullName": "Service Center Staff",
      "role": "SC_Staff",
      "serviceCenterId": 1,
      "serviceCenterName": "Downtown Service Center",
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "total": 1,
  "offset": 0,
  "limit": 20
}
```

## Filter Options

### Role Filter

- `Admin`: Chỉ lấy Admin users
- `EVM_Staff`: Chỉ lấy EVM Staff users
- `SC_Staff`: Chỉ lấy Service Center Staff users
- `SC_Technician`: Chỉ lấy Service Center Technician users

### Service Center Filter

- `serviceCenterId`: Lấy người dùng thuộc service center cụ thể
- Chỉ áp dụng cho SC_Staff và SC_Technician
- Admin và EVM_Staff không có serviceCenterId

### Pagination

- `limit`: Số lượng kết quả (1-100, mặc định 20)
- `offset`: Vị trí bắt đầu (0 hoặc lớn hơn, mặc định 0)
- Response bao gồm `total`, `offset`, `limit` để client biết thông tin pagination

## Response Fields Explanation

### User Object

- `id`: ID duy nhất của người dùng
- `email`: Email đăng nhập
- `fullName`: Họ tên đầy đủ
- `role`: Vai trò trong hệ thống
- `serviceCenterId`: ID service center (nếu có)
- `serviceCenterName`: Tên service center (nếu có)
- `isActive`: Tài khoản có active không
- `createdAt`: Thời gian tạo tài khoản

### Pagination Info

- `total`: Tổng số người dùng thỏa mãn filter
- `offset`: Vị trí bắt đầu hiện tại
- `limit`: Số lượng kết quả hiện tại

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả người dùng
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem tất cả người dùng
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### SC_Staff

- ❌ Không có quyền sử dụng endpoint này
- ❌ Chỉ có thể xem profile của chính mình

### SC_Technician

- ❌ Không có quyền sử dụng endpoint này
- ❌ Chỉ có thể xem profile của chính mình

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter (role, serviceCenterId)
- Pagination được implement ở database level
- Không load tất cả records vào memory

### Caching

- Có thể cache danh sách người dùng theo role
- Cache có thể invalidate khi có thay đổi user
- TTL cache thường là 5-10 phút

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Response được sắp xếp theo createdAt (mới nhất trước)
- Thông tin nhạy cảm như password, session không được trả về
- Service center name được join từ bảng service_centers
- Nếu không có filter, trả về tất cả người dùng với pagination
- Limit tối đa là 100 để tránh overload server
