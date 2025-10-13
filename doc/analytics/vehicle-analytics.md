# GET /api/analytics/vehicles

## Mô tả

Lấy phân tích chi tiết về vehicles bao gồm distribution, warranty coverage, và performance metrics theo thời gian.

## Endpoint

```
GET /api/analytics/vehicles
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter | Type    | Required | Description                                       |
| --------- | ------- | -------- | ------------------------------------------------- |
| period    | string  | No       | Khoảng thời gian (7d, 30d, 90d, 1y, mặc định 30d) |
| make      | string  | No       | Filter theo hãng xe (Tesla, BMW, Mercedes)        |
| year      | integer | No       | Filter theo năm sản xuất                          |
| oemId     | long    | No       | Filter theo OEM ID (mặc định 1)                   |

## Response

### Success Response (200 OK)

```json
{
  "period": "30d",
  "make": "Tesla",
  "year": 2023,
  "oemId": 1,
  "oemName": "Tesla Motors",
  "overview": {
    "totalVehicles": 12500,
    "newVehicles": 250,
    "affectedVehicles": 850,
    "warrantyCoverage": 68.5,
    "averageAge": 2.3,
    "totalMileage": 12500000
  },
  "distribution": {
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
    "byModel": {
      "Model 3": 4500,
      "Model S": 2000,
      "Model X": 1500,
      "Model Y": 500
    },
    "byColor": {
      "Pearl White": 3200,
      "Midnight Silver": 2800,
      "Deep Blue": 2500,
      "Red": 2000,
      "Black": 2000
    }
  },
  "warranty": {
    "coverage": {
      "underWarranty": 8562,
      "expiredWarranty": 3938,
      "coverageRate": 68.5
    },
    "byType": {
      "Basic": 5000,
      "Extended": 3562,
      "Premium": 0
    },
    "expirationTrends": [
      { "date": "2024-01-01", "count": 50 },
      { "date": "2024-01-02", "count": 45 },
      { "date": "2024-01-03", "count": 60 }
    ]
  },
  "performance": {
    "reliability": {
      "averageIssuesPerVehicle": 0.8,
      "criticalIssues": 45,
      "minorIssues": 125,
      "reliabilityScore": 4.2
    },
    "maintenance": {
      "averageServiceInterval": 120.5,
      "totalServices": 1250,
      "averageServiceCost": 350.0
    },
    "usage": {
      "averageMileage": 1000.0,
      "highMileageVehicles": 250,
      "lowMileageVehicles": 500
    }
  },
  "trends": {
    "vehicleRegistrations": [
      { "date": "2024-01-01", "count": 15 },
      { "date": "2024-01-02", "count": 20 },
      { "date": "2024-01-03", "count": 25 }
    ],
    "warrantyExpirations": [
      { "date": "2024-01-01", "count": 50 },
      { "date": "2024-01-02", "count": 45 },
      { "date": "2024-01-03", "count": 60 }
    ],
    "issueReports": [
      { "date": "2024-01-01", "count": 8 },
      { "date": "2024-01-02", "count": 12 },
      { "date": "2024-01-03", "count": 15 }
    ]
  },
  "topModels": [
    {
      "model": "Model 3",
      "count": 4500,
      "warrantyCoverage": 75.0,
      "averageIssues": 0.6,
      "reliabilityScore": 4.5
    },
    {
      "model": "Model S",
      "count": 2000,
      "warrantyCoverage": 80.0,
      "averageIssues": 0.8,
      "reliabilityScore": 4.3
    }
  ],
  "geographicDistribution": [
    {
      "region": "North America",
      "count": 7500,
      "percentage": 60.0
    },
    {
      "region": "Europe",
      "count": 3750,
      "percentage": 30.0
    },
    {
      "region": "Asia",
      "count": 1250,
      "percentage": 10.0
    }
  ]
}
```

### Error Responses

#### 400 Bad Request - Period không hợp lệ

```json
{
  "error": "Invalid period. Valid periods are: 7d, 30d, 90d, 1y",
  "path": "/api/analytics/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Make không hợp lệ

```json
{
  "error": "Invalid make. Valid makes are: Tesla, BMW, Mercedes, Audi, Lexus",
  "path": "/api/analytics/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve vehicle analytics: Internal server error",
  "path": "/api/analytics/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem vehicle analytics trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy vehicle analytics với period mặc định

**Request:**

```
GET /api/analytics/vehicles
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với vehicle analytics 30 ngày

### Test Case 2: Filter theo make Tesla

**Request:**

```
GET /api/analytics/vehicles?make=Tesla
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ Tesla vehicles

### Test Case 3: Filter theo year 2023

**Request:**

```
GET /api/analytics/vehicles?year=2023
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với vehicles năm 2023

### Test Case 4: Make không hợp lệ

**Request:**

```
GET /api/analytics/vehicles?make=InvalidMake
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 5: Year không hợp lệ

**Request:**

```
GET /api/analytics/vehicles?year=1900
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/analytics/vehicles?period=30d&make=Tesla&year=2023" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/analytics/vehicles?period=30d&make=Tesla&year=2023",
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

url = "http://localhost:8080/api/analytics/vehicles"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "period": "30d",
    "make": "Tesla",
    "year": 2023
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

### Bước 2: Lấy vehicle analytics

```bash
curl -X GET "http://localhost:8080/api/analytics/vehicles?period=30d" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "period": "30d",
  "oemId": 1,
  "oemName": "Tesla Motors",
  "overview": {
    "totalVehicles": 12500,
    "newVehicles": 250,
    "affectedVehicles": 850,
    "warrantyCoverage": 68.5,
    "averageAge": 2.3,
    "totalMileage": 12500000
  },
  "distribution": {
    "byMake": {
      "Tesla": 8500,
      "BMW": 2500,
      "Mercedes": 1500
    },
    "byYear": {
      "2024": 3200,
      "2023": 4800,
      "2022": 4500
    }
  }
}
```

## Response Fields Explanation

### Basic Information

- `period`: Khoảng thời gian được phân tích
- `make`: Hãng xe được filter
- `year`: Năm sản xuất được filter
- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Overview

- `totalVehicles`: Tổng số xe
- `newVehicles`: Số xe mới đăng ký
- `affectedVehicles`: Số xe bị ảnh hưởng bởi campaigns
- `warrantyCoverage`: Tỷ lệ bảo hành (%)
- `averageAge`: Tuổi trung bình (năm)
- `totalMileage`: Tổng số km

### Distribution

- `byMake`: Phân bố theo hãng xe
- `byYear`: Phân bố theo năm sản xuất
- `byModel`: Phân bố theo model
- `byColor`: Phân bố theo màu sắc

### Warranty Analytics

- `coverage`: Thống kê bảo hành
  - `underWarranty`: Số xe còn bảo hành
  - `expiredWarranty`: Số xe hết bảo hành
  - `coverageRate`: Tỷ lệ bảo hành (%)
- `byType`: Phân bố theo loại bảo hành
- `expirationTrends`: Xu hướng hết hạn bảo hành

### Performance Metrics

- `reliability`: Độ tin cậy
  - `averageIssuesPerVehicle`: Số vấn đề trung bình mỗi xe
  - `criticalIssues`: Số vấn đề nghiêm trọng
  - `minorIssues`: Số vấn đề nhỏ
  - `reliabilityScore`: Điểm tin cậy (1-5)
- `maintenance`: Bảo trì
  - `averageServiceInterval`: Khoảng cách bảo trì trung bình (ngày)
  - `totalServices`: Tổng số dịch vụ
  - `averageServiceCost`: Chi phí dịch vụ trung bình
- `usage`: Sử dụng
  - `averageMileage`: Số km trung bình
  - `highMileageVehicles`: Số xe có km cao
  - `lowMileageVehicles`: Số xe có km thấp

### Trends

- `vehicleRegistrations`: Xu hướng đăng ký xe
- `warrantyExpirations`: Xu hướng hết hạn bảo hành
- `issueReports`: Xu hướng báo cáo vấn đề

### Top Models

- `model`: Model xe
- `count`: Số lượng
- `warrantyCoverage`: Tỷ lệ bảo hành (%)
- `averageIssues`: Số vấn đề trung bình
- `reliabilityScore`: Điểm tin cậy (1-5)

### Geographic Distribution

- `region`: Khu vực
- `count`: Số lượng
- `percentage`: Phần trăm

## Filter Options

### Period Filter

- `7d`: 7 ngày gần nhất
- `30d`: 30 ngày gần nhất (mặc định)
- `90d`: 90 ngày gần nhất
- `1y`: 1 năm gần nhất

### Make Filter

- `Tesla`: Chỉ Tesla vehicles
- `BMW`: Chỉ BMW vehicles
- `Mercedes`: Chỉ Mercedes vehicles
- `Audi`: Chỉ Audi vehicles
- `Lexus`: Chỉ Lexus vehicles

### Year Filter

- `2024`: Chỉ vehicles năm 2024
- `2023`: Chỉ vehicles năm 2023
- `2022`: Chỉ vehicles năm 2022
- `2021`: Chỉ vehicles năm 2021
- `2020`: Chỉ vehicles năm 2020

### OEM Filter

- `oemId`: Filter theo OEM cụ thể
- Mặc định là 1 nếu không cung cấp

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả vehicle analytics
- ✅ Có thể filter theo bất kỳ OEM nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem vehicle analytics của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu OEM khác

### SC_Staff

- ✅ Có thể xem vehicle analytics của service center
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem dữ liệu service center khác

### SC_Technician

- ❌ Không có quyền xem vehicle analytics
- ❌ Chỉ có thể xem tasks được gán

## Performance Considerations

### Database Optimization

- Sử dụng materialized views cho vehicle analytics
- Cache kết quả theo period, make, year, và OEM
- Pre-calculate metrics hàng ngày

### Caching Strategy

- Cache vehicle analytics trong 15 phút
- Invalidate cache khi có thay đổi vehicle
- Cache trends data riêng biệt

### Data Aggregation

- Tính toán metrics offline
- Sử dụng background jobs
- Lưu kết quả vào cache

## Notes

- Endpoint này yêu cầu quyền phù hợp để sử dụng
- Response sẽ khác nhau tùy theo quyền của người dùng
- Dữ liệu được tính toán từ vehicle data thực tế
- Trends data có thể được lazy load
- Performance metrics được tính toán từ vehicle data
- Warranty coverage được tính từ warranty policies
- Reliability score được tính từ issue reports
- Maintenance data được tính từ service records
- Usage data được tính từ mileage records
- Geographic distribution được tính từ registration data
- Top models được sắp xếp theo số lượng
- Trends được tính toán theo ngày
- Warranty expiration trends được tính từ warranty end dates
- Issue reports được tính từ claim data
- Vehicle registrations được tính từ registration data
