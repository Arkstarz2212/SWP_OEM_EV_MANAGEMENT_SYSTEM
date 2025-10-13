# GET /api/notifications

## Mô tả

Lấy danh sách thông báo của người dùng hiện tại. Endpoint này cho phép xem lịch sử thông báo với các filter và pagination.

## Endpoint

```
GET /api/notifications
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter | Type    | Required | Description                           |
| --------- | ------- | -------- | ------------------------------------- |
| status    | string  | No       | Filter theo trạng thái                |
| priority  | string  | No       | Filter theo mức độ ưu tiên            |
| channel   | string  | No       | Filter theo kênh gửi                  |
| read      | boolean | No       | Filter theo trạng thái đọc            |
| startDate | string  | No       | Filter từ ngày (YYYY-MM-DD)           |
| endDate   | string  | No       | Filter đến ngày (YYYY-MM-DD)          |
| limit     | integer | No       | Số lượng kết quả (1-100, mặc định 20) |
| offset    | integer | No       | Vị trí bắt đầu (mặc định 0)           |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "notifications": [
      {
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
      },
      {
        "id": 2,
        "notificationId": "NOTIF-2024-002",
        "subject": "Warranty Claim Update",
        "message": "Your warranty claim has been approved",
        "priority": "MEDIUM",
        "channels": ["PUSH"],
        "status": "DELIVERED",
        "read": false,
        "readAt": null,
        "deliveredAt": "2024-01-14T15:20:00Z",
        "createdAt": "2024-01-14T15:00:00Z",
        "createdBy": "evmstaff@example.com",
        "metadata": {
          "claimId": 1,
          "claimNumber": "WC-2024-001"
        }
      }
    ],
    "pagination": {
      "total": 25,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalNotifications": 25,
      "unreadNotifications": 5,
      "highPriorityNotifications": 3,
      "criticalNotifications": 1,
      "recentNotifications": 10
    },
    "filters": {
      "status": null,
      "priority": null,
      "channel": null,
      "read": null,
      "startDate": null,
      "endDate": null
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: limit must be between 1 and 100",
  "path": "/api/notifications",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Date format không hợp lệ

```json
{
  "error": "Invalid date format. Use YYYY-MM-DD",
  "path": "/api/notifications",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve notifications: Internal server error",
  "path": "/api/notifications",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem thông báo của mình

## Test Cases

### Test Case 1: Lấy danh sách thông báo mặc định

**Request:**

```
GET /api/notifications
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách thông báo

### Test Case 2: Filter theo trạng thái

**Request:**

```
GET /api/notifications?status=DELIVERED
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ thông báo đã gửi

### Test Case 3: Filter theo priority

**Request:**

```
GET /api/notifications?priority=HIGH
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ thông báo HIGH priority

### Test Case 4: Filter theo chưa đọc

**Request:**

```
GET /api/notifications?read=false
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ thông báo chưa đọc

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/notifications?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 thông báo từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/notifications?status=DELIVERED&priority=HIGH&limit=20" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/notifications?status=DELIVERED&priority=HIGH&limit=20",
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

url = "http://localhost:8080/api/notifications"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "status": "DELIVERED",
    "priority": "HIGH",
    "limit": 20
}

response = requests.get(url, headers=headers, params=params)
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

### Bước 2: Lấy danh sách thông báo

```bash
curl -X GET "http://localhost:8080/api/notifications?read=false" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "notifications": [
      {
        "id": 2,
        "notificationId": "NOTIF-2024-002",
        "subject": "Warranty Claim Update",
        "message": "Your warranty claim has been approved",
        "priority": "MEDIUM",
        "channels": ["PUSH"],
        "status": "DELIVERED",
        "read": false,
        "readAt": null,
        "deliveredAt": "2024-01-14T15:20:00Z",
        "createdAt": "2024-01-14T15:00:00Z",
        "createdBy": "evmstaff@example.com",
        "metadata": {
          "claimId": 1,
          "claimNumber": "WC-2024-001"
        }
      }
    ],
    "pagination": {
      "total": 25,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalNotifications": 25,
      "unreadNotifications": 5,
      "highPriorityNotifications": 3,
      "criticalNotifications": 1,
      "recentNotifications": 10
    }
  }
}
```

## Response Fields Explanation

### Notification Object

- `id`: ID duy nhất của notification
- `notificationId`: Số notification (duy nhất)
- `subject`: Tiêu đề thông báo
- `message`: Nội dung thông báo
- `priority`: Mức độ ưu tiên
- `channels`: Kênh gửi thông báo
- `status`: Trạng thái notification
- `read`: Đã đọc chưa
- `readAt`: Thời gian đọc
- `deliveredAt`: Thời gian giao hàng
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo
- `metadata`: Dữ liệu bổ sung

### Pagination Information

- `total`: Tổng số notifications thỏa mãn filter
- `limit`: Số lượng kết quả hiện tại
- `offset`: Vị trí bắt đầu hiện tại
- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không

### Summary Information

- `totalNotifications`: Tổng số thông báo
- `unreadNotifications`: Số thông báo chưa đọc
- `highPriorityNotifications`: Số thông báo ưu tiên cao
- `criticalNotifications`: Số thông báo ưu tiên cao nhất
- `recentNotifications`: Số thông báo gần đây

### Filter Information

- `status`: Trạng thái được filter
- `priority`: Mức độ ưu tiên được filter
- `channel`: Kênh được filter
- `read`: Trạng thái đọc được filter
- `startDate`: Ngày bắt đầu được filter
- `endDate`: Ngày kết thúc được filter

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

## Filter Options

### Status Filter

- `PENDING`: Chỉ thông báo chờ gửi
- `SENT`: Chỉ thông báo đã gửi
- `DELIVERED`: Chỉ thông báo đã giao hàng
- `FAILED`: Chỉ thông báo gửi thất bại
- `EXPIRED`: Chỉ thông báo hết hạn

### Priority Filter

- `CRITICAL`: Chỉ thông báo ưu tiên cao nhất
- `HIGH`: Chỉ thông báo ưu tiên cao
- `MEDIUM`: Chỉ thông báo ưu tiên trung bình
- `LOW`: Chỉ thông báo ưu tiên thấp

### Channel Filter

- `EMAIL`: Chỉ thông báo qua email
- `SMS`: Chỉ thông báo qua SMS
- `PUSH`: Chỉ thông báo qua push
- `IN_APP`: Chỉ thông báo trong app

### Read Filter

- `true`: Chỉ thông báo đã đọc
- `false`: Chỉ thông báo chưa đọc

### Date Filter

- `startDate`: Từ ngày
- `endDate`: Đến ngày
- Format: YYYY-MM-DD

## Pagination

### Limit

- Tối thiểu: 1
- Tối đa: 100
- Mặc định: 20

### Offset

- Tối thiểu: 0
- Mặc định: 0
- Phải là bội số của limit

### Navigation

- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không
- `total`: Tổng số kết quả

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả thông báo
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem thông báo của mình
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem thông báo của người khác

### SC_Staff

- ✅ Có thể xem thông báo của mình
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem thông báo của người khác

### SC_Technician

- ✅ Có thể xem thông báo của mình
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem thông báo của người khác

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng users để lấy thông tin
- Sử dụng pagination để giới hạn kết quả

### Caching

- Cache notifications trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thông báo mới

### Data Aggregation

- Tính toán summary metrics real-time
- Sử dụng background jobs cho heavy calculations
- Cache kết quả trong database

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Filter được áp dụng theo thứ tự ưu tiên
- Pagination được tính toán dựa trên filter
- Summary metrics được tính toán real-time
- Notifications được sắp xếp theo createdAt giảm dần
- Filter information được trả về để debug
- Pagination information được trả về để navigation
- Summary information được trả về để overview
- Performance được tối ưu thông qua caching
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
