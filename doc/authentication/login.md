# POST /api/auth/login

## Mô tả

Đăng nhập người dùng vào hệ thống AOEM. Endpoint này xác thực thông tin đăng nhập và trả về session token cùng thông tin người dùng.

## Endpoint

```
POST /api/auth/login
```

## Request

### Headers

```
Content-Type: application/json
```

### Body

```json
{
  "username": "admin@example.com",
  "password": "password123"
}
```

### Parameters

| Field    | Type   | Required | Description                        |
| -------- | ------ | -------- | ---------------------------------- |
| username | string | Yes      | Email hoặc username của người dùng |
| password | string | Yes      | Mật khẩu của người dùng            |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "email": "admin@example.com",
    "fullName": "John Doe",
    "role": "Admin",
    "serviceCenterId": null,
    "sessionToken": "sess_abc123def456ghi789",
    "expiresAt": "2024-12-31T23:59:59Z"
  }
}
```

### Error Responses

#### 400 Bad Request - Thiếu thông tin

```json
{
  "error": "Username is required",
  "path": "/api/auth/login",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 401 Unauthorized - Sai thông tin đăng nhập

```json
{
  "error": "Invalid username or password",
  "path": "/api/auth/login",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 401 Unauthorized - Tài khoản bị khóa

```json
{
  "error": "Account is locked",
  "path": "/api/auth/login",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Login failed: Internal server error",
  "path": "/api/auth/login",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Không cần endpoint nào trước
- Người dùng phải đã được tạo trong hệ thống
- Tài khoản phải ở trạng thái active

## Test Cases

### Test Case 1: Đăng nhập thành công với Admin

**Request:**

```json
{
  "username": "admin@example.com",
  "password": "admin123"
}
```

**Expected Response:** 200 OK với session token và thông tin admin

### Test Case 2: Đăng nhập thành công với SC Staff

**Request:**

```json
{
  "username": "scstaff@service.com",
  "password": "scstaff123"
}
```

**Expected Response:** 200 OK với role SC_Staff và serviceCenterId

### Test Case 3: Sai mật khẩu

**Request:**

```json
{
  "username": "admin@example.com",
  "password": "wrongpassword"
}
```

**Expected Response:** 401 Unauthorized

### Test Case 4: Thiếu username

**Request:**

```json
{
  "password": "password123"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Tài khoản không tồn tại

**Request:**

```json
{
  "username": "nonexistent@example.com",
  "password": "password123"
}
```

**Expected Response:** 401 Unauthorized

## Usage Example

### cURL

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@example.com",
    "password": "password123"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/auth/login", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    username: "admin@example.com",
    password: "password123",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/auth/login"
data = {
    "username": "admin@example.com",
    "password": "password123"
}

response = requests.post(url, json=data)
print(response.json())
```

## Notes

- Session token có thời hạn và cần được sử dụng cho các API khác
- Sau khi đăng nhập thành công, lưu session token để sử dụng cho các request tiếp theo
- Nếu đăng nhập thất bại nhiều lần, tài khoản có thể bị khóa tạm thời
