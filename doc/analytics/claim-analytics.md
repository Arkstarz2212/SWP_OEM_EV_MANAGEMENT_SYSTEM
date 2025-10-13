# GET /api/analytics/claims

## Mô tả

Lấy phân tích chi tiết về warranty claims bao gồm performance metrics, approval rates, và trends theo thời gian.

## Endpoint

```
GET /api/analytics/claims
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type   | Required | Description                                           |
| --------------- | ------ | -------- | ----------------------------------------------------- |
| period          | string | No       | Khoảng thời gian (7d, 30d, 90d, 1y, mặc định 30d)     |
| status          | string | No       | Filter theo status (PENDING_APPROVAL, APPROVED, etc.) |
| serviceCenterId | long   | No       | Filter theo service center ID                         |
| oemId           | long   | No       | Filter theo OEM ID (mặc định 1)                       |

## Response

### Success Response (200 OK)

```json
{
  "period": "30d",
  "status": "PENDING_APPROVAL",
  "serviceCenterId": 1,
  "oemId": 1,
  "oemName": "Tesla Motors",
  "overview": {
    "totalClaims": 245,
    "pendingClaims": 12,
    "approvedClaims": 45,
    "inProgressClaims": 38,
    "completedClaims": 140,
    "rejectedClaims": 8,
    "cancelledClaims": 2,
    "totalRepairCost": 125000.0,
    "averageRepairCost": 510.2
  },
  "performance": {
    "averageProcessingTime": 3.2,
    "averageApprovalTime": 1.5,
    "averageRepairTime": 8.7,
    "approvalRate": 94.7,
    "completionRate": 85.2,
    "customerSatisfaction": 4.5
  },
  "byStatus": {
    "PENDING_APPROVAL": 12,
    "APPROVED": 45,
    "IN_PROGRESS": 38,
    "ON_HOLD": 5,
    "COMPLETED": 140,
    "REJECTED": 8,
    "CANCELLED": 2
  },
  "byServiceCenter": [
    {
      "serviceCenterId": 1,
      "name": "Downtown Service Center",
      "totalClaims": 120,
      "completedClaims": 95,
      "completionRate": 79.2,
      "averageProcessingTime": 2.8
    },
    {
      "serviceCenterId": 2,
      "name": "Uptown Service Center",
      "totalClaims": 125,
      "completedClaims": 110,
      "completionRate": 88.0,
      "averageProcessingTime": 3.5
    }
  ],
  "byIssueType": {
    "Battery": 45,
    "Engine": 38,
    "Brake": 32,
    "Transmission": 28,
    "Electrical": 25,
    "Other": 77
  },
  "costAnalysis": {
    "byRange": {
      "0-500": 120,
      "500-1000": 80,
      "1000-2000": 35,
      "2000+": 10
    },
    "averageByIssueType": {
      "Battery": 1200.0,
      "Engine": 800.0,
      "Brake": 300.0,
      "Transmission": 1500.0,
      "Electrical": 600.0
    }
  },
  "trends": {
    "claimsCreated": [
      { "date": "2024-01-01", "count": 8 },
      { "date": "2024-01-02", "count": 12 },
      { "date": "2024-01-03", "count": 15 }
    ],
    "claimsCompleted": [
      { "date": "2024-01-01", "count": 5 },
      { "date": "2024-01-02", "count": 8 },
      { "date": "2024-01-03", "count": 12 }
    ],
    "approvalRate": [
      { "date": "2024-01-01", "rate": 90.0 },
      { "date": "2024-01-02", "rate": 95.0 },
      { "date": "2024-01-03", "rate": 92.0 }
    ]
  },
  "topIssues": [
    {
      "issueType": "Battery",
      "count": 45,
      "averageCost": 1200.0,
      "averageProcessingTime": 4.2
    },
    {
      "issueType": "Engine",
      "count": 38,
      "averageCost": 800.0,
      "averageProcessingTime": 3.8
    }
  ],
  "serviceCenterRanking": [
    {
      "serviceCenterId": 2,
      "name": "Uptown Service Center",
      "completionRate": 88.0,
      "averageProcessingTime": 3.5,
      "customerSatisfaction": 4.6
    },
    {
      "serviceCenterId": 1,
      "name": "Downtown Service Center",
      "completionRate": 79.2,
      "averageProcessingTime": 2.8,
      "customerSatisfaction": 4.3
    }
  ]
}
```

### Error Responses

#### 400 Bad Request - Period không hợp lệ

```json
{
  "error": "Invalid period. Valid periods are: 7d, 30d, 90d, 1y",
  "path": "/api/analytics/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Status không hợp lệ

```json
{
  "error": "Invalid status. Valid statuses are: PENDING_APPROVAL, APPROVED, IN_PROGRESS, ON_HOLD, COMPLETED, REJECTED, CANCELLED",
  "path": "/api/analytics/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve claim analytics: Internal server error",
  "path": "/api/analytics/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem claim analytics trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy claim analytics với period mặc định

**Request:**

```
GET /api/analytics/claims
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với claim analytics 30 ngày

### Test Case 2: Filter theo status PENDING_APPROVAL

**Request:**

```
GET /api/analytics/claims?status=PENDING_APPROVAL
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ PENDING_APPROVAL claims

### Test Case 3: Filter theo service center

**Request:**

```
GET /api/analytics/claims?serviceCenterId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với claims của service center 1

### Test Case 4: Status không hợp lệ

**Request:**

```
GET /api/analytics/claims?status=INVALID_STATUS
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 5: Period không hợp lệ

**Request:**

```
GET /api/analytics/claims?period=invalid
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/analytics/claims?period=30d&status=PENDING_APPROVAL&serviceCenterId=1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/analytics/claims?period=30d&status=PENDING_APPROVAL&serviceCenterId=1",
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

url = "http://localhost:8080/api/analytics/claims"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "period": "30d",
    "status": "PENDING_APPROVAL",
    "serviceCenterId": 1
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

### Bước 2: Lấy claim analytics

```bash
curl -X GET "http://localhost:8080/api/analytics/claims?period=30d" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "period": "30d",
  "oemId": 1,
  "oemName": "Tesla Motors",
  "overview": {
    "totalClaims": 245,
    "pendingClaims": 12,
    "approvedClaims": 45,
    "completedClaims": 140,
    "totalRepairCost": 125000.0,
    "averageRepairCost": 510.2
  },
  "performance": {
    "averageProcessingTime": 3.2,
    "approvalRate": 94.7,
    "completionRate": 85.2,
    "customerSatisfaction": 4.5
  }
}
```

## Response Fields Explanation

### Basic Information

- `period`: Khoảng thời gian được phân tích
- `status`: Status được filter
- `serviceCenterId`: ID service center được filter
- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Overview

- `totalClaims`: Tổng số claims
- `pendingClaims`: Số claims chờ phê duyệt
- `approvedClaims`: Số claims đã phê duyệt
- `inProgressClaims`: Số claims đang xử lý
- `completedClaims`: Số claims đã hoàn thành
- `rejectedClaims`: Số claims bị từ chối
- `cancelledClaims`: Số claims bị hủy
- `totalRepairCost`: Tổng chi phí sửa chữa
- `averageRepairCost`: Chi phí sửa chữa trung bình

### Performance Metrics

- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)
- `averageApprovalTime`: Thời gian phê duyệt trung bình (ngày)
- `averageRepairTime`: Thời gian sửa chữa trung bình (ngày)
- `approvalRate`: Tỷ lệ phê duyệt (%)
- `completionRate`: Tỷ lệ hoàn thành (%)
- `customerSatisfaction`: Đánh giá khách hàng (1-5)

### By Status Analytics

- Phân bố claims theo trạng thái
- PENDING_APPROVAL, APPROVED, IN_PROGRESS, ON_HOLD, COMPLETED, REJECTED, CANCELLED

### By Service Center Analytics

- `serviceCenterId`: ID service center
- `name`: Tên service center
- `totalClaims`: Tổng số claims
- `completedClaims`: Số claims đã hoàn thành
- `completionRate`: Tỷ lệ hoàn thành (%)
- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)

### By Issue Type Analytics

- Phân bố claims theo loại vấn đề
- Battery, Engine, Brake, Transmission, Electrical, Other

### Cost Analysis

- `byRange`: Phân bố theo khoảng chi phí
- `averageByIssueType`: Chi phí trung bình theo loại vấn đề

### Trends

- `claimsCreated`: Xu hướng tạo claims
- `claimsCompleted`: Xu hướng hoàn thành claims
- `approvalRate`: Xu hướng tỷ lệ phê duyệt

### Top Issues

- `issueType`: Loại vấn đề
- `count`: Số lượng
- `averageCost`: Chi phí trung bình
- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)

### Service Center Ranking

- `serviceCenterId`: ID service center
- `name`: Tên service center
- `completionRate`: Tỷ lệ hoàn thành (%)
- `averageProcessingTime`: Thời gian xử lý trung bình (ngày)
- `customerSatisfaction`: Đánh giá khách hàng (1-5)

## Filter Options

### Period Filter

- `7d`: 7 ngày gần nhất
- `30d`: 30 ngày gần nhất (mặc định)
- `90d`: 90 ngày gần nhất
- `1y`: 1 năm gần nhất

### Status Filter

- `PENDING_APPROVAL`: Chỉ claims chờ phê duyệt
- `APPROVED`: Chỉ claims đã phê duyệt
- `IN_PROGRESS`: Chỉ claims đang xử lý
- `ON_HOLD`: Chỉ claims tạm dừng
- `COMPLETED`: Chỉ claims đã hoàn thành
- `REJECTED`: Chỉ claims bị từ chối
- `CANCELLED`: Chỉ claims bị hủy

### Service Center Filter

- `serviceCenterId`: Filter theo service center cụ thể
- Chỉ áp dụng cho SC_Staff và SC_Technician

### OEM Filter

- `oemId`: Filter theo OEM cụ thể
- Mặc định là 1 nếu không cung cấp

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả claim analytics
- ✅ Có thể filter theo bất kỳ service center nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem claim analytics của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu OEM khác

### SC_Staff

- ✅ Có thể xem claim analytics của service center
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu service center khác

### SC_Technician

- ❌ Không có quyền xem claim analytics
- ❌ Chỉ có thể xem tasks được gán

## Performance Considerations

### Database Optimization

- Sử dụng materialized views cho claim analytics
- Cache kết quả theo period, status, service center, và OEM
- Pre-calculate metrics hàng ngày

### Caching Strategy

- Cache claim analytics trong 10 phút
- Invalidate cache khi có thay đổi claim
- Cache trends data riêng biệt

### Data Aggregation

- Tính toán metrics offline
- Sử dụng background jobs
- Lưu kết quả vào cache

## Notes

- Endpoint này yêu cầu quyền phù hợp để sử dụng
- Response sẽ khác nhau tùy theo quyền của người dùng
- Dữ liệu được tính toán từ claim data thực tế
- Trends data có thể được lazy load
- Performance metrics được tính toán từ claim timestamps
- Service center performance được tính từ assignments
- Approval rate được tính từ claim approvals
- Completion rate được tính từ claim completions
- Average processing time được tính từ claim timestamps
- Customer satisfaction được lấy từ feedback system
- Cost analysis được tính từ repair costs
- Issue type được phân loại từ diagnosis
- Service center ranking được sắp xếp theo performance
- Top issues được sắp xếp theo số lượng
- Trends được tính toán theo ngày
- Cost ranges được định nghĩa theo business rules
