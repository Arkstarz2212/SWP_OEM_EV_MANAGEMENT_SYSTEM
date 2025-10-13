# GET /api/analytics/dashboard

## Mô tả

Lấy tổng quan dashboard với các metrics chính của hệ thống bao gồm số liệu campaigns, claims, vehicles và performance indicators.

## Endpoint

```
GET /api/analytics/dashboard
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter | Type   | Required | Description                                       |
| --------- | ------ | -------- | ------------------------------------------------- |
| period    | string | No       | Khoảng thời gian (7d, 30d, 90d, 1y, mặc định 30d) |
| oemId     | long   | No       | Filter theo OEM ID (mặc định 1)                   |

## Response

### Success Response (200 OK)

```json
{
  "period": "30d",
  "oemId": 1,
  "oemName": "Tesla Motors",
  "summary": {
    "totalCampaigns": 15,
    "activeCampaigns": 8,
    "totalClaims": 245,
    "pendingClaims": 12,
    "totalVehicles": 12500,
    "affectedVehicles": 850
  },
  "campaigns": {
    "byType": {
      "RECALL": 5,
      "SERVICE": 7,
      "PROMOTION": 3
    },
    "byStatus": {
      "DRAFT": 2,
      "ACTIVE": 8,
      "COMPLETED": 4,
      "EXPIRED": 1
    },
    "completionRate": 75.5
  },
  "claims": {
    "byStatus": {
      "PENDING_APPROVAL": 12,
      "APPROVED": 45,
      "IN_PROGRESS": 38,
      "COMPLETED": 140,
      "REJECTED": 8,
      "CANCELLED": 2
    },
    "averageProcessingTime": 3.2,
    "approvalRate": 94.7
  },
  "vehicles": {
    "byMake": {
      "Tesla": 8500,
      "BMW": 2500,
      "Mercedes": 1500
    },
    "byYear": {
      "2024": 3200,
      "2023": 4800,
      "2022": 4500
    },
    "warrantyCoverage": 68.5
  },
  "performance": {
    "serviceCenters": {
      "total": 25,
      "active": 23,
      "averageRating": 4.2
    },
    "technicians": {
      "total": 156,
      "active": 142,
      "averageWorkload": 3.8
    },
    "customerSatisfaction": 4.5
  },
  "trends": {
    "campaignsCreated": [
      { "date": "2024-01-01", "count": 2 },
      { "date": "2024-01-02", "count": 1 },
      { "date": "2024-01-03", "count": 3 }
    ],
    "claimsProcessed": [
      { "date": "2024-01-01", "count": 8 },
      { "date": "2024-01-02", "count": 12 },
      { "date": "2024-01-03", "count": 15 }
    ]
  },
  "alerts": [
    {
      "type": "HIGH_PRIORITY",
      "message": "3 recall campaigns require immediate attention",
      "count": 3
    },
    {
      "type": "OVERDUE",
      "message": "5 claims are overdue for approval",
      "count": 5
    }
  ]
}
```

### Error Responses

#### 400 Bad Request - Period không hợp lệ

```json
{
  "error": "Invalid period. Valid periods are: 7d, 30d, 90d, 1y",
  "path": "/api/analytics/dashboard",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - OEM ID không hợp lệ

```json
{
  "error": "Invalid OEM ID",
  "path": "/api/analytics/dashboard",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve dashboard data: Internal server error",
  "path": "/api/analytics/dashboard",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem dashboard trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy dashboard với period mặc định

**Request:**

```
GET /api/analytics/dashboard
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với dashboard data 30 ngày

### Test Case 2: Lấy dashboard với period 7 ngày

**Request:**

```
GET /api/analytics/dashboard?period=7d
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với dashboard data 7 ngày

### Test Case 3: Lấy dashboard với OEM cụ thể

**Request:**

```
GET /api/analytics/dashboard?oemId=2
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với dashboard data của OEM 2

### Test Case 4: Period không hợp lệ

**Request:**

```
GET /api/analytics/dashboard?period=invalid
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 5: OEM ID không tồn tại

**Request:**

```
GET /api/analytics/dashboard?oemId=999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/analytics/dashboard?period=30d&oemId=1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/analytics/dashboard?period=30d&oemId=1",
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

url = "http://localhost:8080/api/analytics/dashboard"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "period": "30d",
    "oemId": 1
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

### Bước 2: Lấy dashboard overview

```bash
curl -X GET "http://localhost:8080/api/analytics/dashboard?period=30d" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "period": "30d",
  "oemId": 1,
  "oemName": "Tesla Motors",
  "summary": {
    "totalCampaigns": 15,
    "activeCampaigns": 8,
    "totalClaims": 245,
    "pendingClaims": 12,
    "totalVehicles": 12500,
    "affectedVehicles": 850
  },
  "campaigns": {
    "byType": {
      "RECALL": 5,
      "SERVICE": 7,
      "PROMOTION": 3
    },
    "byStatus": {
      "DRAFT": 2,
      "ACTIVE": 8,
      "COMPLETED": 4,
      "EXPIRED": 1
    },
    "completionRate": 75.5
  }
}
```

## Response Fields Explanation

### Basic Information

- `period`: Khoảng thời gian được phân tích
- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Summary

- `totalCampaigns`: Tổng số campaigns
- `activeCampaigns`: Số campaigns đang hoạt động
- `totalClaims`: Tổng số claims
- `pendingClaims`: Số claims chờ xử lý
- `totalVehicles`: Tổng số xe
- `affectedVehicles`: Số xe bị ảnh hưởng

### Campaigns Analytics

- `byType`: Phân bố theo loại (RECALL, SERVICE, PROMOTION)
- `byStatus`: Phân bố theo trạng thái
- `completionRate`: Tỷ lệ hoàn thành (%)

### Claims Analytics

- `byStatus`: Phân bố theo trạng thái
- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)
- `approvalRate`: Tỷ lệ phê duyệt (%)

### Vehicles Analytics

- `byMake`: Phân bố theo hãng xe
- `byYear`: Phân bố theo năm sản xuất
- `warrantyCoverage`: Tỷ lệ bảo hành (%)

### Performance Metrics

- `serviceCenters`: Thống kê service centers
- `technicians`: Thống kê technicians
- `customerSatisfaction`: Đánh giá khách hàng

### Trends

- `campaignsCreated`: Xu hướng tạo campaigns
- `claimsProcessed`: Xu hướng xử lý claims

### Alerts

- `type`: Loại cảnh báo
- `message`: Nội dung cảnh báo
- `count`: Số lượng

## Period Options

### 7d (7 ngày)

- Dữ liệu trong 7 ngày gần nhất
- Phù hợp cho monitoring ngắn hạn
- Cập nhật real-time

### 30d (30 ngày)

- Dữ liệu trong 30 ngày gần nhất
- Phù hợp cho báo cáo hàng tháng
- Mặc định

### 90d (90 ngày)

- Dữ liệu trong 90 ngày gần nhất
- Phù hợp cho phân tích quý
- Xu hướng dài hạn

### 1y (1 năm)

- Dữ liệu trong 1 năm gần nhất
- Phù hợp cho báo cáo hàng năm
- Phân tích xu hướng

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả dữ liệu
- ✅ Có thể filter theo bất kỳ OEM nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem dữ liệu của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu OEM khác

### SC_Staff

- ✅ Có thể xem dữ liệu của service center
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu service center khác

### SC_Technician

- ❌ Không có quyền xem dashboard
- ❌ Chỉ có thể xem tasks được gán

## Performance Considerations

### Database Optimization

- Sử dụng materialized views cho analytics
- Cache kết quả trong 5 phút
- Pre-calculate metrics hàng ngày

### Caching Strategy

- Cache dashboard data theo period và OEM
- Invalidate cache khi có thay đổi dữ liệu
- Cache trends data riêng biệt

### Data Aggregation

- Tính toán metrics offline
- Sử dụng background jobs
- Lưu kết quả vào cache

## Notes

- Endpoint này yêu cầu quyền phù hợp để sử dụng
- Response sẽ khác nhau tùy theo quyền của người dùng
- Dữ liệu được tính toán real-time hoặc từ cache
- Trends data có thể được lazy load
- Alerts được cập nhật real-time
- Performance metrics được tính toán từ dữ liệu thực tế
- Customer satisfaction được lấy từ feedback system
- Service center rating được tính từ reviews
- Technician workload được tính từ assignments
- Warranty coverage được tính từ warranty policies
- Completion rate được tính từ campaign progress
- Approval rate được tính từ claim approvals
- Average processing time được tính từ claim timestamps
