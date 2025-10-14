# POST /api/auth/password/reset/request

## Mô tả

Yêu cầu reset mật khẩu bằng cách gửi link reset đến email hoặc SMS của người dùng. Hệ thống sẽ gửi token reset password qua kênh liên lạc đã đăng ký.

## Endpoint

```
POST /api/auth/password/reset/request
```

## Request

### Headers

```
Content-Type: application/json
```

### Body

```json
{
  "usernameOrEmail": "user@example.com"
}
```

### Parameters

| Field           | Type   | Required | Description                                       |
| --------------- | ------ | -------- | ------------------------------------------------- |
| usernameOrEmail | string | Yes      | Username, email hoặc số điện thoại của người dùng |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "message": "Password reset link sent"
}
```

### Error Responses

#### 400 Bad Request - Thiếu thông tin

```json
{
  "error": "usernameOrEmail is required",
  "path": "/api/auth/password/reset/request",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Không tìm thấy người dùng

```json
{
  "error": "User not found with provided credentials",
  "path": "/api/auth/password/reset/request",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to send password reset: Internal server error",
  "path": "/api/auth/password/reset/request",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Không cần đăng nhập** - Endpoint này dành cho người dùng quên mật khẩu
- **Không cần endpoint nào trước**
- **Người dùng phải tồn tại** trong hệ thống với email/username/phone đã đăng ký

## Test Cases

### Test Case 1: Yêu cầu reset thành công với email

**Request:**

```json
{
  "usernameOrEmail": "user@example.com"
}
```

**Expected Response:** 200 OK với message thành công

### Test Case 2: Yêu cầu reset thành công với username

**Request:**

```json
{
  "usernameOrEmail": "john_doe"
}
```

**Expected Response:** 200 OK với message thành công

### Test Case 3: Yêu cầu reset thành công với số điện thoại

**Request:**

```json
{
  "usernameOrEmail": "+1234567890"
}
```

**Expected Response:** 200 OK với message thành công

### Test Case 4: Thiếu thông tin

**Request:**

```json
{}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Người dùng không tồn tại

**Request:**

```json
{
  "usernameOrEmail": "nonexistent@example.com"
}
```

**Expected Response:** 404 Not Found

## Usage Example

### cURL

```bash
curl -X POST http://localhost:8080/api/auth/password/reset/request \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "user@example.com"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/auth/password/reset/request",
  {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      usernameOrEmail: "user@example.com",
    }),
  }
);

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/auth/password/reset/request"
data = {
    "usernameOrEmail": "user@example.com"
}

response = requests.post(url, json=data)
print(response.json())
```

## Workflow Example

### Bước 1: Người dùng quên mật khẩu, yêu cầu reset

```bash
curl -X POST http://localhost:8080/api/auth/password/reset/request \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "user@example.com"
  }'
```

**Response:**

```json
{
  "success": true,
  "message": "Password reset link sent"
}
```

### Bước 2: Người dùng nhận email/SMS với token reset

```
Email: "Click here to reset your password: https://app.aoem.com/reset?token=abc123def456ghi789"
SMS: "Your password reset code: abc123def456ghi789"
```

### Bước 3: Người dùng sử dụng token để reset mật khẩu

```bash
curl -X POST "http://localhost:8080/api/auth/password/reset/confirm?token=abc123def456ghi789&newPassword=newpassword123"
```

## Security Notes

- Token reset có thời hạn (thường 15-30 phút)
- Mỗi token chỉ có thể sử dụng một lần
- Hệ thống sẽ ghi log tất cả yêu cầu reset mật khẩu
- Nếu có nhiều yêu cầu reset liên tiếp, hệ thống có thể rate limit

## Email Template Example

```
Subject: Password Reset Request - AOEM System

Dear User,

You have requested to reset your password for your AOEM account.

Click the link below to reset your password:
https://app.aoem.com/reset?token=abc123def456ghi789

This link will expire in 30 minutes.

If you did not request this password reset, please ignore this email.

Best regards,
AOEM Support Team
```

## SMS Template Example

```
Your AOEM password reset code: abc123def456ghi789
Valid for 30 minutes. Do not share this code.
```

## Notes

- Hệ thống tự động phát hiện email vs phone number dựa trên format
- Email: chứa ký tự '@'
- Phone: chỉ chứa số và ký tự '+'
- Token được mã hóa và có thời hạn ngắn để bảo mật
- Người dùng có thể yêu cầu reset nhiều lần nhưng chỉ token mới nhất có hiệu lực
