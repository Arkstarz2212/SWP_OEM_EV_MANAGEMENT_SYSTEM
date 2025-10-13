# POST /api/auth/password/reset/confirm

## Mô tả

Xác nhận reset mật khẩu bằng token nhận được qua email/SMS. Endpoint này cho phép người dùng đặt mật khẩu mới sau khi đã xác thực token reset.

## Endpoint

```
POST /api/auth/password/reset/confirm
```

## Request

### Headers

```
Content-Type: application/json
```

### Query Parameters

| Parameter   | Type   | Required | Description                                  |
| ----------- | ------ | -------- | -------------------------------------------- |
| token       | string | Yes      | Token reset password nhận được qua email/SMS |
| newPassword | string | Yes      | Mật khẩu mới (tối thiểu 6 ký tự)             |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "message": "Password reset successfully"
}
```

### Error Responses

#### 400 Bad Request - Thiếu tham số

```json
{
  "error": "Token parameter is required",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Mật khẩu không đủ dài

```json
{
  "error": "Password must be at least 6 characters long",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Token không hợp lệ

```json
{
  "error": "Invalid or expired token",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Password reset failed: Internal server error",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải có token reset hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/password/reset/request`
- **Token phải còn hiệu lực** (chưa hết hạn và chưa được sử dụng)

## Test Cases

### Test Case 1: Reset mật khẩu thành công

**Request:**

```
POST /api/auth/password/reset/confirm?token=abc123def456ghi789&newPassword=newpassword123
```

**Expected Response:** 200 OK với message thành công

### Test Case 2: Thiếu token

**Request:**

```
POST /api/auth/password/reset/confirm?newPassword=newpassword123
```

**Expected Response:** 400 Bad Request

### Test Case 3: Thiếu mật khẩu mới

**Request:**

```
POST /api/auth/password/reset/confirm?token=abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Mật khẩu quá ngắn

**Request:**

```
POST /api/auth/password/reset/confirm?token=abc123def456ghi789&newPassword=123
```

**Expected Response:** 400 Bad Request

### Test Case 5: Token không hợp lệ hoặc đã hết hạn

**Request:**

```
POST /api/auth/password/reset/confirm?token=invalid_token&newPassword=newpassword123
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/auth/password/reset/confirm?token=abc123def456ghi789&newPassword=newpassword123"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/auth/password/reset/confirm?token=abc123def456ghi789&newPassword=newpassword123",
  {
    method: "POST",
  }
);

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/auth/password/reset/confirm"
params = {
    "token": "abc123def456ghi789",
    "newPassword": "newpassword123"
}

response = requests.post(url, params=params)
print(response.json())
```

## Workflow Example

### Bước 1: Yêu cầu reset mật khẩu

```bash
curl -X POST http://localhost:8080/api/auth/password/reset/request \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "user@example.com"
  }'
```

### Bước 2: Nhận token qua email/SMS

```
Email: "Your reset token: abc123def456ghi789"
SMS: "Reset code: abc123def456ghi789"
```

### Bước 3: Xác nhận reset với token và mật khẩu mới

```bash
curl -X POST "http://localhost:8080/api/auth/password/reset/confirm?token=abc123def456ghi789&newPassword=newpassword123"
```

**Response:**

```json
{
  "success": true,
  "message": "Password reset successfully"
}
```

### Bước 4: Đăng nhập với mật khẩu mới

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "newpassword123"
  }'
```

## Security Considerations

### Token Security

- Token có thời hạn ngắn (15-30 phút)
- Token chỉ có thể sử dụng một lần
- Token được mã hóa và không thể đoán được
- Sau khi sử dụng, token sẽ bị vô hiệu hóa

### Password Requirements

- Tối thiểu 6 ký tự
- Nên bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt
- Không được trùng với mật khẩu cũ
- Không được là các mật khẩu phổ biến

### Rate Limiting

- Chỉ cho phép 3 lần thử reset trong 1 giờ
- Chỉ cho phép 5 lần thử token trong 1 giờ
- Sau khi vượt quá giới hạn, tài khoản bị khóa tạm thời

## Error Handling

### Token Expired

```json
{
  "error": "Token has expired. Please request a new password reset.",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### Token Already Used

```json
{
  "error": "Token has already been used. Please request a new password reset.",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### Invalid Token Format

```json
{
  "error": "Invalid token format",
  "path": "/api/auth/password/reset/confirm",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Notes

- Sau khi reset thành công, tất cả session cũ sẽ bị vô hiệu hóa
- Người dùng cần đăng nhập lại với mật khẩu mới
- Hệ thống sẽ gửi email thông báo về việc thay đổi mật khẩu
- Nếu có nhiều thiết bị đăng nhập, tất cả sẽ bị đăng xuất
- Token reset chỉ có hiệu lực trong thời gian ngắn để bảo mật
