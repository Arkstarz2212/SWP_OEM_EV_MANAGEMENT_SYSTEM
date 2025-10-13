# GET /api/notifications/{id}

## Mô tả

Lấy chi tiết thông báo theo ID. Endpoint này cho phép xem thông tin chi tiết của một thông báo cụ thể.

## Endpoint

```
GET /api/notifications/{id}
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
    },
    "deliveryDetails": [
      {
        "channel": "EMAIL",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:00Z",
        "error": null
      },
      {
        "channel": "SMS",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:30Z",
        "error": null
      }
    ],
    "recipients": [
      {
        "userId": 1,
        "userName": "admin@example.com",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:00Z",
        "readAt": "2024-01-15T10:30:00Z"
      },
      {
        "userId": 2,
        "userName": "evmstaff@example.com",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:00Z",
        "readAt": null
      }
    ]
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

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to view notification",
  "path": "/api/notifications/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve notification: Internal server error",
  "path": "/api/notifications/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem thông báo của mình
- **Notification:** Thông báo phải tồn tại và thuộc về người dùng

## Test Cases

### Test Case 1: Lấy chi tiết thông báo thành công

**Request:**

```
GET /api/notifications/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chi tiết thông báo

### Test Case 2: Thông báo không tồn tại

**Request:**

```
GET /api/notifications/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 3: Không có quyền truy cập

**Request:**

```
GET /api/notifications/1
Authorization: Bearer sess_invalid_token
```

**Expected Response:** 403 Forbidden

### Test Case 4: Thông báo không thuộc về người dùng

**Request:**

```
GET /api/notifications/1
Authorization: Bearer sess_other_user_token
```

**Expected Response:** 403 Forbidden

### Test Case 5: Thông báo đã hết hạn

**Request:**

```
GET /api/notifications/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông báo hết hạn

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/notifications/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/notifications/1", {
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

url = "http://localhost:8080/api/notifications/1"
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

### Bước 2: Lấy chi tiết thông báo

```bash
curl -X GET "http://localhost:8080/api/notifications/1" \
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
    },
    "deliveryDetails": [
      {
        "channel": "EMAIL",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:00Z",
        "error": null
      },
      {
        "channel": "SMS",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:30Z",
        "error": null
      }
    ],
    "recipients": [
      {
        "userId": 1,
        "userName": "admin@example.com",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:00Z",
        "readAt": "2024-01-15T10:30:00Z"
      },
      {
        "userId": 2,
        "userName": "evmstaff@example.com",
        "status": "DELIVERED",
        "deliveredAt": "2024-01-15T10:05:00Z",
        "readAt": null
      }
    ]
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

- `read`: Đã đọc chưa
- `readAt`: Thời gian đọc
- `deliveredAt`: Thời gian giao hàng
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo

### Additional Information

- `metadata`: Dữ liệu bổ sung

### Delivery Details

- `deliveryDetails`: Chi tiết giao hàng theo kênh
  - `channel`: Kênh gửi
  - `status`: Trạng thái giao hàng
  - `deliveredAt`: Thời gian giao hàng
  - `error`: Lỗi nếu có

### Recipients Information

- `recipients`: Danh sách người nhận
  - `userId`: ID người dùng
  - `userName`: Tên người dùng
  - `status`: Trạng thái giao hàng
  - `deliveredAt`: Thời gian giao hàng
  - `readAt`: Thời gian đọc

## Statuses

### PENDING

- Chờ gửi
- Chưa được xử lý
- Có thể bị hủy

### SENT

- Đã gửi
- Đang chờ delivery
- Không thể hủy

### DELIVERED

- Đã giao hàng
- Người nhận đã nhận
- Hoàn thành

### FAILED

- Gửi thất bại
- Cần gửi lại
- Có thể có lỗi

### EXPIRED

- Đã hết hạn
- Không thể gửi
- Tự động hủy

## Priorities

### CRITICAL

- Ưu tiên cao nhất
- Gửi ngay lập tức
- Gửi qua tất cả kênh

### HIGH

- Ưu tiên cao
- Gửi trong 5 phút
- Gửi qua email và push

### MEDIUM

- Ưu tiên trung bình
- Gửi trong 30 phút
- Gửi qua email

### LOW

- Ưu tiên thấp
- Gửi trong 2 giờ
- Gửi qua email

## Channels

### EMAIL

- Gửi qua email
- Hỗ trợ HTML content
- Có thể đính kèm file

### SMS

- Gửi qua tin nhắn
- Giới hạn 160 ký tự
- Hỗ trợ Unicode

### PUSH

- Gửi qua push notification
- Hiển thị trên mobile app
- Có thể có action buttons

### IN_APP

- Hiển thị trong ứng dụng
- Có thể có link đến trang cụ thể
- Hỗ trợ rich content

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả thông báo
- ✅ Có thể xem thông tin nhạy cảm
- ✅ Có thể xem delivery details

### EVM_Staff

- ✅ Có thể xem thông báo của mình
- ❌ Không thể xem thông báo của người khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Staff

- ✅ Có thể xem thông báo của mình
- ❌ Không thể xem thông báo của người khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem thông báo của mình
- ❌ Không thể xem thông báo của người khác
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên notification ID
- Join với bảng users để lấy thông tin
- Join với bảng delivery để lấy chi tiết

### Caching

- Cache notification details trong 10 phút
- Invalidate cache khi có thay đổi
- Cache delivery details riêng biệt

### Data Aggregation

- Tính toán delivery metrics real-time
- Sử dụng background jobs cho heavy calculations
- Cache kết quả trong database

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Chỉ có thể xem thông báo của mình
- Delivery details chỉ hiển thị cho Admin
- Recipients information chỉ hiển thị cho Admin
- Performance được tối ưu thông qua caching
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
