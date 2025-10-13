# POST /api/notifications/send

## Mô tả

Gửi thông báo đến người dùng hoặc nhóm người dùng. Endpoint này cho phép Admin và EVM_Staff gửi thông báo qua nhiều kênh khác nhau.

## Endpoint

```
POST /api/notifications/send
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Body

```json
{
  "recipients": [1, 2, 3],
  "channels": ["EMAIL", "SMS", "PUSH"],
  "subject": "Campaign Update Notification",
  "message": "New campaign has been activated for your vehicles",
  "priority": "HIGH",
  "scheduledAt": "2024-01-15T10:00:00Z",
  "expiresAt": "2024-01-20T10:00:00Z",
  "metadata": {
    "campaignId": 1,
    "campaignName": "Battery Safety Campaign"
  }
}
```

### Parameters

| Field       | Type   | Required | Description                                  |
| ----------- | ------ | -------- | -------------------------------------------- |
| recipients  | array  | Yes      | Danh sách ID người nhận                      |
| channels    | array  | Yes      | Kênh gửi thông báo                           |
| subject     | string | Yes      | Tiêu đề thông báo                            |
| message     | string | Yes      | Nội dung thông báo                           |
| priority    | string | No       | Mức độ ưu tiên (LOW, MEDIUM, HIGH, CRITICAL) |
| scheduledAt | string | No       | Thời gian gửi (ISO 8601)                     |
| expiresAt   | string | No       | Thời gian hết hạn (ISO 8601)                 |
| metadata    | object | No       | Dữ liệu bổ sung                              |

## Response

### Success Response (201 Created)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "notificationId": "NOTIF-2024-001",
    "recipients": [
      {
        "userId": 1,
        "userName": "admin@example.com",
        "status": "PENDING"
      },
      {
        "userId": 2,
        "userName": "evmstaff@example.com",
        "status": "PENDING"
      },
      {
        "userId": 3,
        "userName": "scstaff@example.com",
        "status": "PENDING"
      }
    ],
    "channels": ["EMAIL", "SMS", "PUSH"],
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH",
    "status": "SCHEDULED",
    "scheduledAt": "2024-01-15T10:00:00Z",
    "expiresAt": "2024-01-20T10:00:00Z",
    "metadata": {
      "campaignId": 1,
      "campaignName": "Battery Safety Campaign"
    },
    "createdAt": "2024-01-15T09:00:00Z",
    "createdBy": "admin@example.com",
    "totalRecipients": 3,
    "estimatedDeliveryTime": "2024-01-15T10:05:00Z"
  }
}
```

### Error Responses

#### 400 Bad Request - Validation Error

```json
{
  "error": "Validation failed",
  "details": [
    {
      "field": "recipients",
      "message": "Recipients list cannot be empty"
    },
    {
      "field": "channels",
      "message": "At least one channel must be specified"
    }
  ],
  "path": "/api/notifications/send",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Người nhận không tồn tại

```json
{
  "error": "Some recipients not found",
  "details": [999, 1000],
  "path": "/api/notifications/send",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Kênh không hợp lệ

```json
{
  "error": "Invalid channel. Valid channels are: EMAIL, SMS, PUSH, IN_APP",
  "path": "/api/notifications/send",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to send notifications",
  "path": "/api/notifications/send",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to send notification: Internal server error",
  "path": "/api/notifications/send",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **Recipients:** Người nhận phải tồn tại và active
- **Channels:** Kênh gửi phải được hỗ trợ

## Test Cases

### Test Case 1: Gửi thông báo thành công

**Request:**

```json
{
  "recipients": [1, 2, 3],
  "channels": ["EMAIL", "SMS"],
  "subject": "Campaign Update Notification",
  "message": "New campaign has been activated for your vehicles",
  "priority": "HIGH"
}
```

**Expected Response:** 201 Created với thông tin notification

### Test Case 2: Gửi thông báo với lịch trình

**Request:**

```json
{
  "recipients": [1, 2],
  "channels": ["EMAIL"],
  "subject": "Scheduled Maintenance",
  "message": "Your vehicle maintenance is scheduled for tomorrow",
  "priority": "MEDIUM",
  "scheduledAt": "2024-01-16T09:00:00Z"
}
```

**Expected Response:** 201 Created với status SCHEDULED

### Test Case 3: Gửi thông báo với metadata

**Request:**

```json
{
  "recipients": [3],
  "channels": ["PUSH"],
  "subject": "Warranty Claim Update",
  "message": "Your warranty claim has been approved",
  "priority": "HIGH",
  "metadata": {
    "claimId": 1,
    "claimNumber": "WC-2024-001"
  }
}
```

**Expected Response:** 201 Created với metadata

### Test Case 4: Người nhận không tồn tại

**Request:**

```json
{
  "recipients": [999, 1000],
  "channels": ["EMAIL"],
  "subject": "Test Notification",
  "message": "This is a test"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Kênh không hợp lệ

**Request:**

```json
{
  "recipients": [1],
  "channels": ["INVALID_CHANNEL"],
  "subject": "Test Notification",
  "message": "This is a test"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/notifications/send" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "recipients": [1, 2, 3],
    "channels": ["EMAIL", "SMS", "PUSH"],
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH",
    "scheduledAt": "2024-01-15T10:00:00Z",
    "expiresAt": "2024-01-20T10:00:00Z",
    "metadata": {
      "campaignId": 1,
      "campaignName": "Battery Safety Campaign"
    }
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/notifications/send", {
  method: "POST",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    recipients: [1, 2, 3],
    channels: ["EMAIL", "SMS", "PUSH"],
    subject: "Campaign Update Notification",
    message: "New campaign has been activated for your vehicles",
    priority: "HIGH",
    scheduledAt: "2024-01-15T10:00:00Z",
    expiresAt: "2024-01-20T10:00:00Z",
    metadata: {
      campaignId: 1,
      campaignName: "Battery Safety Campaign",
    },
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/notifications/send"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "recipients": [1, 2, 3],
    "channels": ["EMAIL", "SMS", "PUSH"],
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH",
    "scheduledAt": "2024-01-15T10:00:00Z",
    "expiresAt": "2024-01-20T10:00:00Z",
    "metadata": {
        "campaignId": 1,
        "campaignName": "Battery Safety Campaign"
    }
}

response = requests.post(url, headers=headers, json=data)
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
    "role": "Admin",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Gửi thông báo

```bash
curl -X POST "http://localhost:8080/api/notifications/send" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "recipients": [1, 2, 3],
    "channels": ["EMAIL", "SMS"],
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "notificationId": "NOTIF-2024-001",
    "recipients": [
      {
        "userId": 1,
        "userName": "admin@example.com",
        "status": "PENDING"
      },
      {
        "userId": 2,
        "userName": "evmstaff@example.com",
        "status": "PENDING"
      },
      {
        "userId": 3,
        "userName": "scstaff@example.com",
        "status": "PENDING"
      }
    ],
    "channels": ["EMAIL", "SMS"],
    "subject": "Campaign Update Notification",
    "message": "New campaign has been activated for your vehicles",
    "priority": "HIGH",
    "status": "SENT",
    "createdAt": "2024-01-15T09:00:00Z",
    "createdBy": "admin@example.com",
    "totalRecipients": 3,
    "estimatedDeliveryTime": "2024-01-15T09:05:00Z"
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
- `status`: Trạng thái notification

### Recipients Information

- `recipients`: Danh sách người nhận
  - `userId`: ID người dùng
  - `userName`: Tên người dùng
  - `status`: Trạng thái gửi

### Channels Information

- `channels`: Kênh gửi thông báo
- `totalRecipients`: Tổng số người nhận

### Timing Information

- `scheduledAt`: Thời gian gửi
- `expiresAt`: Thời gian hết hạn
- `createdAt`: Thời gian tạo
- `estimatedDeliveryTime`: Thời gian giao hàng ước tính

### Additional Information

- `metadata`: Dữ liệu bổ sung
- `createdBy`: Người tạo

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

## Validation Rules

### Recipients

- Phải là array không rỗng
- Tất cả ID phải tồn tại
- Tất cả user phải active

### Channels

- Phải là array không rỗng
- Tất cả channel phải hợp lệ
- Phải có ít nhất 1 channel

### Subject

- Không được trống
- Tối đa 200 ký tự
- Phải mô tả rõ ràng

### Message

- Không được trống
- Tối đa 5000 ký tự
- Có thể chứa HTML tags

### Priority

- Phải là priority hợp lệ
- Chỉ được chọn từ danh sách có sẵn
- Mặc định là MEDIUM

### Scheduled At

- Phải có format ISO 8601
- Không được trong quá khứ
- Phải hợp lý với expiresAt

### Expires At

- Phải có format ISO 8601
- Phải sau scheduledAt
- Không được trong quá khứ

### Metadata

- Phải là object hợp lệ
- Tối đa 10 fields
- Mỗi field tối đa 1000 ký tự

## Permission Matrix

### Admin

- ✅ Có thể gửi thông báo đến tất cả người dùng
- ✅ Có thể sử dụng tất cả kênh
- ✅ Có thể sử dụng tất cả priority

### EVM_Staff

- ✅ Có thể gửi thông báo đến người dùng trong OEM
- ✅ Có thể sử dụng tất cả kênh
- ❌ Không thể gửi đến người dùng OEM khác

### SC_Staff

- ❌ Không có quyền gửi thông báo
- ❌ Chỉ có thể nhận thông báo

### SC_Technician

- ❌ Không có quyền gửi thông báo
- ❌ Chỉ có thể nhận thông báo

## Automatic Actions

### Notification Creation

- Tạo notification record trong database
- Gán notification ID duy nhất
- Tạo audit log

### Channel Processing

- Xử lý từng kênh riêng biệt
- Tạo delivery record cho mỗi kênh
- Cập nhật status theo kênh

### Scheduling

- Lên lịch gửi nếu có scheduledAt
- Tạo scheduled job
- Cập nhật status thành SCHEDULED

### Delivery Tracking

- Theo dõi delivery status
- Cập nhật status khi gửi
- Tạo delivery log

### Expiration Handling

- Kiểm tra expiration
- Hủy notification hết hạn
- Cập nhật status thành EXPIRED

## Manual Actions Required

### Content Review

- Admin cần review nội dung
- Kiểm tra compliance
- Phê duyệt trước khi gửi

### Channel Configuration

- Cấu hình kênh gửi
- Thiết lập templates
- Test delivery

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Recipients phải tồn tại và active
- Channels phải được hỗ trợ
- Subject và message là bắt buộc
- Priority là optional, mặc định MEDIUM
- ScheduledAt và expiresAt là optional
- Metadata là optional
- Notification ID được tạo tự động
- Status được set thành PENDING ban đầu
- Delivery được xử lý bất đồng bộ
- Audit log được tạo cho mọi thay đổi
- Performance được tối ưu thông qua queue
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Email được gửi qua SMTP service
- SMS được gửi qua SMS gateway
- Push notification được gửi qua FCM/APNS
- In-app notification được lưu trong database
- Delivery status được cập nhật real-time
- Error handling được thực hiện cho từng kênh
- Retry mechanism được áp dụng cho failed delivery
- Rate limiting được áp dụng cho từng kênh
- Content filtering được áp dụng cho spam prevention
- Template system được hỗ trợ cho consistent formatting
- Localization được hỗ trợ cho multi-language
- Analytics được thu thập cho delivery metrics
- Compliance được đảm bảo cho data protection
