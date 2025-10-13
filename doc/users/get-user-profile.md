# GET /api/users/{id}

## Mô tả

Lấy thông tin chi tiết profile của một người dùng cụ thể theo ID. Endpoint này cho phép xem thông tin cá nhân, role, và các thông tin liên quan khác của người dùng.

## Endpoint

```
GET /api/users/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description                         |
| --------- | ---- | -------- | ----------------------------------- |
| id        | long | Yes      | ID của người dùng cần lấy thông tin |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "serviceCenterName": "Downtown Service Center",
  "phone": "+1234567890",
  "address": "123 Main St",
  "mfaEnabled": false,
  "isActive": true,
  "lastLoginAt": "2024-01-01T10:30:00Z",
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z"
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid user ID",
  "path": "/api/users/0",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Người dùng không tồn tại

```json
{
  "error": "User not found",
  "path": "/api/users/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve user profile: Internal server error",
  "path": "/api/users/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem profile của chính mình hoặc có quyền quản lý người dùng

## Test Cases

### Test Case 1: Lấy profile thành công

**Request:**

```
GET /api/users/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin đầy đủ

### Test Case 2: Lấy profile của chính mình

**Request:**

```
GET /api/users/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin cá nhân

### Test Case 3: ID không hợp lệ (0 hoặc âm)

**Request:**

```
GET /api/users/0
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Người dùng không tồn tại

**Request:**

```
GET /api/users/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found

### Test Case 5: Không có authorization

**Request:**

```
GET /api/users/1
```

**Expected Response:** 401 Unauthorized

## Usage Example

### cURL

```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/users/1", {
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

url = "http://localhost:8080/api/users/1"
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
    "username": "user@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Lấy thông tin profile

```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "serviceCenterName": "Downtown Service Center",
  "phone": "+1234567890",
  "address": "123 Main St",
  "mfaEnabled": false,
  "isActive": true,
  "lastLoginAt": "2024-01-01T10:30:00Z",
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z"
}
```

## Permission Matrix

### Admin

- ✅ Có thể xem profile của tất cả người dùng
- ✅ Có thể xem thông tin nhạy cảm (lastLoginAt, createdAt)

### EVM_Staff

- ✅ Có thể xem profile của tất cả người dùng
- ✅ Có thể xem thông tin nhạy cảm

### SC_Staff

- ✅ Có thể xem profile của chính mình
- ✅ Có thể xem profile của SC_Staff/SC_Technician trong cùng service center
- ❌ Không thể xem profile của Admin/EVM_Staff

### SC_Technician

- ✅ Có thể xem profile của chính mình
- ❌ Không thể xem profile của người dùng khác

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của người dùng
- `email`: Email đăng nhập
- `fullName`: Họ tên đầy đủ
- `role`: Vai trò trong hệ thống

### Service Center Information

- `serviceCenterId`: ID service center (nếu có)
- `serviceCenterName`: Tên service center (nếu có)

### Contact Information

- `phone`: Số điện thoại
- `address`: Địa chỉ

### Security Information

- `mfaEnabled`: Có bật 2FA không
- `isActive`: Tài khoản có active không
- `lastLoginAt`: Lần đăng nhập cuối
- `createdAt`: Thời gian tạo tài khoản
- `updatedAt`: Thời gian cập nhật cuối

## Security Considerations

### Data Privacy

- Mật khẩu không bao giờ được trả về trong response
- Session token không được trả về
- Chỉ trả về thông tin cần thiết dựa trên quyền của người dùng

### Access Control

- Người dùng chỉ có thể xem profile của chính mình hoặc người dùng trong phạm vi quản lý
- Admin và EVM_Staff có quyền xem tất cả
- SC_Staff chỉ xem được trong service center của mình

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng đang xem
- Thông tin nhạy cảm chỉ hiển thị cho Admin và EVM_Staff
- Service center name sẽ được join từ bảng service_centers
- Nếu người dùng không có service center, serviceCenterId và serviceCenterName sẽ là null
