# PUT /api/notifications/{id}/read

## Mô tả

Đánh dấu thông báo là đã đọc. Endpoint này cho phép người dùng đánh dấu thông báo đã đọc và cập nhật trạng thái.

## Endpoint

```
PUT /api/notifications/{id}/read
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
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH",
    "channels": ["EMAIL", "SMS"],
    "status": "DELIVERED",
    "read": true,
    "readAt": "2024-01-15T10:30:00Z",
    "deliveredAt": "2024-01-15T10:05:00Z",
    "createdAt": "2024-01-15T09:00:00Z",
    "createdBy": "admin@example.com",
    "metadata": {
      "campaignId": 1,
      "campaignName": "Battery Safety Campaign"
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Thông báo không tồn tại

```json
{
  "error": "Notification not found",
  "path": "/api/notifications/999/read",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Thông báo đã được đọc

```json
{
  "error": "Notification already read",
  "path": "/api/notifications/1/read",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to mark notification as read",
  "path": "/api/notifications/1/read",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to mark notification as read: Internal server error",
  "path": "/api/notifications/1/read",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể đánh dấu thông báo của mình
- **Notification:** Thông báo phải tồn tại và thuộc về người dùng

## Test Cases

### Test Case 1: Đánh dấu đã đọc thành công

**Request:**

```
PUT /api/notifications/1/read
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông báo đã được đánh dấu đọc

### Test Case 2: Thông báo không tồn tại

**Request:**

```
PUT /api/notifications/999/read
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 3: Thông báo đã được đọc

**Request:**

```
PUT /api/notifications/1/read
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Không có quyền truy cập

**Request:**

```
PUT /api/notifications/1/read
Authorization: Bearer sess_invalid_token
```

**Expected Response:** 403 Forbidden

### Test Case 5: Thông báo không thuộc về người dùng

**Request:**

```
PUT /api/notifications/1/read
Authorization: Bearer sess_other_user_token
```

**Expected Response:** 403 Forbidden

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/notifications/1/read" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/notifications/1/read", {
  method: "PUT",
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

url = "http://localhost:8080/api/notifications/1/read"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}

response = requests.put(url, headers=headers)
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

### Bước 2: Đánh dấu thông báo đã đọc

```bash
curl -X PUT "http://localhost:8080/api/notifications/1/read" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "notificationId": "NOTIF-2024-001",
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH",
    "channels": ["EMAIL", "SMS"],
    "status": "DELIVERED",
    "read": true,
    "readAt": "2024-01-15T10:30:00Z",
    "deliveredAt": "2024-01-15T10:05:00Z",
    "createdAt": "2024-01-15T09:00:00Z",
    "createdBy": "admin@example.com",
    "metadata": {
      "campaignId": 1,
      "campaignName": "Battery Safety Campaign"
    }
  }
}
```

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của notification
- `notificationId`: Số notification (duy nhất)
- `subject`: Tiêu đề thông báo
- `message`: Nội dung thông báo
- `priority`: Mức độ ưu tiên
- `channels`: Kênh gửi thông báo
- `status`: Trạng thái notification

### Read Information

- `read`: Đã đọc chưa (true)
- `readAt`: Thời gian đọc
- `deliveredAt`: Thời gian giao hàng
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo

### Additional Information

- `metadata`: Dữ liệu bổ sung

## Validation Rules

### Notification ID

- Phải tồn tại trong hệ thống
- Phải thuộc về người dùng hiện tại
- Phải có quyền truy cập

### Read Status

- Phải chưa được đọc
- Không thể đánh dấu lại
- Chỉ có thể đánh dấu một lần

### Permission

- Chỉ có thể đánh dấu thông báo của mình
- Không thể đánh dấu thông báo của người khác
- Phải có quyền truy cập

## Permission Matrix

### Admin

- ✅ Có thể đánh dấu tất cả thông báo
- ✅ Có thể đánh dấu thông báo của mình
- ✅ Có thể đánh dấu thông báo của người khác

### EVM_Staff

- ✅ Có thể đánh dấu thông báo của mình
- ❌ Không thể đánh dấu thông báo của người khác

### SC_Staff

- ✅ Có thể đánh dấu thông báo của mình
- ❌ Không thể đánh dấu thông báo của người khác

### SC_Technician

- ✅ Có thể đánh dấu thông báo của mình
- ❌ Không thể đánh dấu thông báo của người khác

## Automatic Actions

### Read Status Update

- Cập nhật read status thành true
- Cập nhật readAt timestamp
- Tạo audit log

### Notification Update

- Cập nhật notification record
- Cập nhật lastUpdated timestamp
- Tạo update record

### Analytics Update

- Cập nhật read metrics
- Cập nhật user engagement
- Tạo analytics record

## Manual Actions Required

### User Action

- Người dùng cần đánh dấu thông báo
- Cập nhật trạng thái đọc
- Xác nhận đã nhận thông báo

## Notes

- Endpoint này dành cho tất cả người dùng đã đăng nhập
- Chỉ có thể đánh dấu thông báo của mình
- Không thể đánh dấu lại thông báo đã đọc
- ReadAt được set thành thời gian hiện tại
- Audit log được tạo cho mọi thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
