# GET /api/analytics/service-centers

## Mô tả

Lấy phân tích chi tiết về service centers bao gồm performance metrics, customer satisfaction, và capacity utilization.

## Endpoint

```
GET /api/analytics/service-centers
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
  "overview": {
    "totalServiceCenters": 25,
    "activeServiceCenters": 23,
    "inactiveServiceCenters": 2,
    "totalTechnicians": 156,
    "activeTechnicians": 142,
    "totalCapacity": 500,
    "utilizedCapacity": 380,
    "utilizationRate": 76.0
  },
  "performance": {
    "averageProcessingTime": 3.2,
    "averageCustomerSatisfaction": 4.5,
    "averageCompletionRate": 85.7,
    "averageResponseTime": 1.5,
    "totalServicesCompleted": 1250,
    "totalRevenue": 125000.0
  },
  "byStatus": {
    "ACTIVE": 23,
    "INACTIVE": 2,
    "MAINTENANCE": 0,
    "OVERLOADED": 3
  },
  "byRegion": [
    {
      "region": "North America",
      "count": 15,
      "averageRating": 4.6,
      "totalCapacity": 300,
      "utilizedCapacity": 230
    },
    {
      "region": "Europe",
      "count": 8,
      "averageRating": 4.3,
      "totalCapacity": 160,
      "utilizedCapacity": 120
    },
    {
      "region": "Asia",
      "count": 2,
      "averageRating": 4.1,
      "totalCapacity": 40,
      "utilizedCapacity": 30
    }
  ],
  "topPerformers": [
    {
      "serviceCenterId": 1,
      "name": "Downtown Service Center",
      "location": "New York, NY",
      "rating": 4.8,
      "completionRate": 92.5,
      "averageProcessingTime": 2.5,
      "customerSatisfaction": 4.7,
      "totalServices": 150,
      "revenue": 15000.0
    },
    {
      "serviceCenterId": 2,
      "name": "Uptown Service Center",
      "location": "Los Angeles, CA",
      "rating": 4.6,
      "completionRate": 88.0,
      "averageProcessingTime": 3.0,
      "customerSatisfaction": 4.5,
      "totalServices": 120,
      "revenue": 12000.0
    }
  ],
  "capacityAnalysis": {
    "byUtilization": {
      "0-50%": 5,
      "50-75%": 12,
      "75-90%": 6,
      "90-100%": 2,
      "100%+": 0
    },
    "averageUtilization": 76.0,
    "peakUtilization": 95.0,
    "lowUtilization": 45.0
  },
  "technicianPerformance": {
    "averageWorkload": 3.8,
    "averageRating": 4.3,
    "totalTechnicians": 156,
    "activeTechnicians": 142,
    "averageExperience": 5.2,
    "certificationRate": 85.0
  },
  "trends": {
    "serviceCenterActivity": [
      { "date": "2024-01-01", "count": 45 },
      { "date": "2024-01-02", "count": 52 },
      { "date": "2024-01-03", "count": 48 }
    ],
    "customerSatisfaction": [
      { "date": "2024-01-01", "rating": 4.5 },
      { "date": "2024-01-02", "rating": 4.6 },
      { "date": "2024-01-03", "rating": 4.4 }
    ],
    "capacityUtilization": [
      { "date": "2024-01-01", "rate": 75.0 },
      { "date": "2024-01-02", "rate": 78.0 },
      { "date": "2024-01-03", "rate": 72.0 }
    ]
  },
  "alerts": [
    {
      "type": "CAPACITY_WARNING",
      "message": "3 service centers are at 90%+ capacity",
      "count": 3
    },
    {
      "type": "PERFORMANCE_WARNING",
      "message": "2 service centers have low customer satisfaction",
      "count": 2
    }
  ]
}
```

### Error Responses

#### 400 Bad Request - Period không hợp lệ

```json
{
  "error": "Invalid period. Valid periods are: 7d, 30d, 90d, 1y",
  "path": "/api/analytics/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - OEM ID không hợp lệ

```json
{
  "error": "Invalid OEM ID",
  "path": "/api/analytics/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve service center analytics: Internal server error",
  "path": "/api/analytics/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem service center analytics trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy service center analytics với period mặc định

**Request:**

```
GET /api/analytics/service-centers
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với service center analytics 30 ngày

### Test Case 2: Filter theo period 90 ngày

**Request:**

```
GET /api/analytics/service-centers?period=90d
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với service center analytics 90 ngày

### Test Case 3: Filter theo OEM cụ thể

**Request:**

```
GET /api/analytics/service-centers?oemId=2
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với service center analytics của OEM 2

### Test Case 4: Period không hợp lệ

**Request:**

```
GET /api/analytics/service-centers?period=invalid
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 5: OEM ID không tồn tại

**Request:**

```
GET /api/analytics/service-centers?oemId=999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/analytics/service-centers?period=30d&oemId=1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/analytics/service-centers?period=30d&oemId=1",
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

url = "http://localhost:8080/api/analytics/service-centers"
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
    "username": "evmstaff@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 2,
    "role": "EVM_Staff",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Lấy service center analytics

```bash
curl -X GET "http://localhost:8080/api/analytics/service-centers?period=30d" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "period": "30d",
  "oemId": 1,
  "oemName": "Tesla Motors",
  "overview": {
    "totalServiceCenters": 25,
    "activeServiceCenters": 23,
    "totalTechnicians": 156,
    "totalCapacity": 500,
    "utilizedCapacity": 380,
    "utilizationRate": 76.0
  },
  "performance": {
    "averageProcessingTime": 3.2,
    "averageCustomerSatisfaction": 4.5,
    "averageCompletionRate": 85.7,
    "totalServicesCompleted": 1250,
    "totalRevenue": 125000.0
  }
}
```

## Response Fields Explanation

### Basic Information

- `period`: Khoảng thời gian được phân tích
- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Overview

- `totalServiceCenters`: Tổng số service centers
- `activeServiceCenters`: Số service centers đang hoạt động
- `inactiveServiceCenters`: Số service centers không hoạt động
- `totalTechnicians`: Tổng số technicians
- `activeTechnicians`: Số technicians đang hoạt động
- `totalCapacity`: Tổng công suất
- `utilizedCapacity`: Công suất đã sử dụng
- `utilizationRate`: Tỷ lệ sử dụng (%)

### Performance Metrics

- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)
- `averageCustomerSatisfaction`: Đánh giá khách hàng trung bình (1-5)
- `averageCompletionRate`: Tỷ lệ hoàn thành trung bình (%)
- `averageResponseTime`: Thời gian phản hồi trung bình (ngày)
- `totalServicesCompleted`: Tổng số dịch vụ đã hoàn thành
- `totalRevenue`: Tổng doanh thu

### By Status Analytics

- `ACTIVE`: Service centers đang hoạt động
- `INACTIVE`: Service centers không hoạt động
- `MAINTENANCE`: Service centers đang bảo trì
- `OVERLOADED`: Service centers quá tải

### By Region Analytics

- `region`: Khu vực
- `count`: Số lượng service centers
- `averageRating`: Đánh giá trung bình (1-5)
- `totalCapacity`: Tổng công suất
- `utilizedCapacity`: Công suất đã sử dụng

### Top Performers

- `serviceCenterId`: ID service center
- `name`: Tên service center
- `location`: Địa điểm
- `rating`: Đánh giá (1-5)
- `completionRate`: Tỷ lệ hoàn thành (%)
- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)
- `customerSatisfaction`: Đánh giá khách hàng (1-5)
- `totalServices`: Tổng số dịch vụ
- `revenue`: Doanh thu

### Capacity Analysis

- `byUtilization`: Phân bố theo tỷ lệ sử dụng
- `averageUtilization`: Tỷ lệ sử dụng trung bình (%)
- `peakUtilization`: Tỷ lệ sử dụng cao nhất (%)
- `lowUtilization`: Tỷ lệ sử dụng thấp nhất (%)

### Technician Performance

- `averageWorkload`: Khối lượng công việc trung bình
- `averageRating`: Đánh giá trung bình (1-5)
- `totalTechnicians`: Tổng số technicians
- `activeTechnicians`: Số technicians đang hoạt động
- `averageExperience`: Kinh nghiệm trung bình (năm)
- `certificationRate`: Tỷ lệ chứng chỉ (%)

### Trends

- `serviceCenterActivity`: Xu hướng hoạt động service center
- `customerSatisfaction`: Xu hướng đánh giá khách hàng
- `capacityUtilization`: Xu hướng sử dụng công suất

### Alerts

- `type`: Loại cảnh báo
- `message`: Nội dung cảnh báo
- `count`: Số lượng

## Filter Options

### Period Filter

- `7d`: 7 ngày gần nhất
- `30d`: 30 ngày gần nhất (mặc định)
- `90d`: 90 ngày gần nhất
- `1y`: 1 năm gần nhất

### OEM Filter

- `oemId`: Filter theo OEM cụ thể
- Mặc định là 1 nếu không cung cấp

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả service center analytics
- ✅ Có thể filter theo bất kỳ OEM nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem service center analytics của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu OEM khác

### SC_Staff

- ✅ Có thể xem service center analytics của service center
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu service center khác

### SC_Technician

- ❌ Không có quyền xem service center analytics
- ❌ Chỉ có thể xem tasks được gán

## Performance Considerations

### Database Optimization

- Sử dụng materialized views cho service center analytics
- Cache kết quả theo period và OEM
- Pre-calculate metrics hàng ngày

### Caching Strategy

- Cache service center analytics trong 15 phút
- Invalidate cache khi có thay đổi service center
- Cache trends data riêng biệt

### Data Aggregation

- Tính toán metrics offline
- Sử dụng background jobs
- Lưu kết quả vào cache

## Notes

- Endpoint này yêu cầu quyền phù hợp để sử dụng
- Response sẽ khác nhau tùy theo quyền của người dùng
- Dữ liệu được tính toán từ service center data thực tế
- Trends data có thể được lazy load
- Performance metrics được tính toán từ service records
- Customer satisfaction được lấy từ feedback system
- Capacity utilization được tính từ appointments
- Technician performance được tính từ assignments
- Revenue được tính từ service costs
- Top performers được sắp xếp theo performance
- Alerts được cập nhật real-time
- Trends được tính toán theo ngày
- Service center activity được tính từ appointments
- Customer satisfaction trends được tính từ feedback
- Capacity utilization trends được tính từ bookings
