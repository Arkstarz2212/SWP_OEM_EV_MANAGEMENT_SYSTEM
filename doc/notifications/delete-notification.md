# DELETE /api/notifications/{id}

## Mô tả

Xóa thông báo khỏi danh sách của người dùng. Endpoint này cho phép người dùng xóa thông báo không cần thiết.

## Endpoint

```
DELETE /api/notifications/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description      |
| --------- | ---- | -------- | ---------------- |
| id        | long | Yes      | ID của thông báo |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "notificationId": "NOTIF-2024-001",
    "deleted": true,
    "deletedAt": "2024-01-15T11:00:00Z",
    "deletedBy": "scstaff@example.com"
  }
}
```

### Error Responses

#### 400 Bad Request - Thông báo không tồn tại

```json
{
  "error": "Notification not found",
  "path": "/api/notifications/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Thông báo đã được xóa

```json
{
  "error": "Notification already deleted",
  "path": "/api/notifications/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to delete notification",
  "path": "/api/notifications/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to delete notification: Internal server error",
  "path": "/api/notifications/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xóa thông báo của mình
- **Notification:** Thông báo phải tồn tại và thuộc về người dùng

## Test Cases

### Test Case 1: Xóa thông báo thành công

**Request:**

```
DELETE /api/notifications/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông báo đã xóa

### Test Case 2: Thông báo không tồn tại

**Request:**

```
DELETE /api/notifications/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 3: Thông báo đã được xóa

**Request:**

```
DELETE /api/notifications/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Không có quyền truy cập

**Request:**

```
DELETE /api/notifications/1
Authorization: Bearer sess_invalid_token
```

**Expected Response:** 403 Forbidden

### Test Case 5: Thông báo không thuộc về người dùng

**Request:**

```
DELETE /api/notifications/1
Authorization: Bearer sess_other_user_token
```

**Expected Response:** 403 Forbidden

## Usage Example

### cURL

```bash
curl -X DELETE "http://localhost:8080/api/notifications/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/notifications/1", {
  method: "DELETE",
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

url = "http://localhost:8080/api/notifications/1"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}

response = requests.delete(url, headers=headers)
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

### Bước 2: Xóa thông báo

```bash
curl -X DELETE "http://localhost:8080/api/notifications/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "notificationId": "NOTIF-2024-001",
    "deleted": true,
    "deletedAt": "2024-01-15T11:00:00Z",
    "deletedBy": "scstaff@example.com"
  }
}
```

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của notification
- `notificationId`: Số notification (duy nhất)
- `deleted`: Đã xóa thành công
- `deletedAt`: Thời gian xóa
- `deletedBy`: Người xóa

## Validation Rules

### Notification ID

- Phải tồn tại trong hệ thống
- Phải thuộc về người dùng hiện tại
- Phải có quyền truy cập

### Delete Status

- Phải chưa được xóa
- Không thể xóa lại
- Chỉ có thể xóa một lần

### Permission

- Chỉ có thể xóa thông báo của mình
- Không thể xóa thông báo của người khác
- Phải có quyền truy cập

## Permission Matrix

### Admin

- ✅ Có thể xóa tất cả thông báo
- ✅ Có thể xóa thông báo của mình
- ✅ Có thể xóa thông báo của người khác

### EVM_Staff

- ✅ Có thể xóa thông báo của mình
- ❌ Không thể xóa thông báo của người khác

### SC_Staff

- ✅ Có thể xóa thông báo của mình
- ❌ Không thể xóa thông báo của người khác

### SC_Technician

- ✅ Có thể xóa thông báo của mình
- ❌ Không thể xóa thông báo của người khác

## Automatic Actions

### Delete Status Update

- Cập nhật deleted status thành true
- Cập nhật deletedAt timestamp
- Tạo audit log

### Notification Update

- Cập nhật notification record
- Cập nhật lastUpdated timestamp
- Tạo delete record

### Analytics Update

- Cập nhật delete metrics
- Cập nhật user engagement
- Tạo analytics record

## Manual Actions Required

### User Action

- Người dùng cần xác nhận xóa
- Cập nhật trạng thái xóa
- Xác nhận không cần thông báo

## Notes

- Endpoint này dành cho tất cả người dùng đã đăng nhập
- Chỉ có thể xóa thông báo của mình
- Không thể xóa lại thông báo đã xóa
- DeletedAt được set thành thời gian hiện tại
- Audit log được tạo cho mọi thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
