# POST /api/auth/logout

## Mô tả

Đăng xuất người dùng khỏi hệ thống bằng cách vô hiệu hóa session token. Endpoint này yêu cầu cả userId và sessionId để đảm bảo bảo mật.

## Endpoint

```
POST /api/auth/logout
```

## Request

### Headers

```
Content-Type: application/json
```

### Query Parameters

| Parameter | Type   | Required | Description                     |
| --------- | ------ | -------- | ------------------------------- |
| userId    | long   | Yes      | ID của người dùng cần đăng xuất |
| sessionId | string | Yes      | Session ID cần vô hiệu hóa      |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

### Error Responses

#### 400 Bad Request - Thiếu tham số

```json
{
  "error": "userId and sessionId parameters are required",
  "path": "/api/auth/logout",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Session không tồn tại

```json
{
  "error": "Session not found or already expired",
  "path": "/api/auth/logout",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Logout failed: Internal server error",
  "path": "/api/auth/logout",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập trước đó để có session token
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Session token:** Phải có session token hợp lệ

## Test Cases

### Test Case 1: Đăng xuất thành công

**Request:**

```
POST /api/auth/logout?userId=1&sessionId=sess_abc123def456ghi789
```

**Expected Response:** 200 OK với message thành công

### Test Case 2: Thiếu userId

**Request:**

```
POST /api/auth/logout?sessionId=sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 3: Thiếu sessionId

**Request:**

```
POST /api/auth/logout?userId=1
```

**Expected Response:** 400 Bad Request

### Test Case 4: Session không tồn tại

**Request:**

```
POST /api/auth/logout?userId=1&sessionId=invalid_session
```

**Expected Response:** 404 Not Found

### Test Case 5: Session đã hết hạn

**Request:**

```
POST /api/auth/logout?userId=1&sessionId=expired_session
```

**Expected Response:** 404 Not Found

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/auth/logout?userId=1&sessionId=sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/auth/logout?userId=1&sessionId=sess_abc123def456ghi789",
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

url = "http://localhost:8080/api/auth/logout"
params = {
    "userId": 1,
    "sessionId": "sess_abc123def456ghi789"
}

response = requests.post(url, params=params)
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
    "sessionToken": "sess_abc123def456ghi789",
    "expiresAt": "2024-12-31T23:59:59Z"
  }
}
```

### Bước 2: Sử dụng session token cho các API khác

```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### Bước 3: Đăng xuất khi hoàn thành

```bash
curl -X POST "http://localhost:8080/api/auth/logout?userId=1&sessionId=sess_abc123def456ghi789"
```

## Notes

- Sau khi đăng xuất, session token sẽ không thể sử dụng được nữa
- Tất cả các request tiếp theo sẽ cần đăng nhập lại
- Nên đăng xuất khi người dùng hoàn thành phiên làm việc để bảo mật
- Có thể đăng xuất từ nhiều thiết bị khác nhau nếu có cùng sessionId
