# GET /api/inventory/alerts/low-stock

## Mô tả

Lấy danh sách cảnh báo tồn kho thấp. Endpoint này cho phép xem các phụ tùng có tồn kho thấp và cần đặt hàng lại.

## Endpoint

```
GET /api/inventory/alerts/low-stock
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type    | Required | Description                           |
| --------------- | ------- | -------- | ------------------------------------- |
| serviceCenterId | long    | No       | Filter theo service center ID         |
| oemId           | long    | No       | Filter theo OEM ID                    |
| category        | string  | No       | Filter theo danh mục phụ tùng         |
| severity        | string  | No       | Filter theo mức độ nghiêm trọng       |
| limit           | integer | No       | Số lượng kết quả (1-100, mặc định 20) |
| offset          | integer | No       | Vị trí bắt đầu (mặc định 0)           |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "alerts": [
      {
        "id": 1,
        "partId": 1,
        "partNumber": "TESLA-BMS-001",
        "partName": "Battery Management System Sensor",
        "category": "ELECTRICAL",
        "subcategory": "BATTERY_SYSTEM",
        "currentQuantity": 5,
        "minQuantity": 10,
        "reorderPoint": 20,
        "maxQuantity": 100,
        "stockStatus": "LOW_STOCK",
        "severity": "HIGH",
        "daysUntilOutOfStock": 3,
        "supplierId": 1,
        "supplierName": "Tesla Parts Supplier",
        "supplierContact": "supplier@tesla.com",
        "supplierPhone": "+1-555-0123",
        "lastRestocked": "2024-01-10T00:00:00Z",
        "averageUsage": 2.5,
        "recommendedQuantity": 50,
        "estimatedCost": 7500.0,
        "currency": "USD",
        "alertCreatedAt": "2024-01-15T00:00:00Z",
        "alertUpdatedAt": "2024-01-15T00:00:00Z"
      },
      {
        "id": 2,
        "partId": 2,
        "partNumber": "TESLA-BMS-002",
        "partName": "Battery Management System Controller",
        "category": "ELECTRICAL",
        "subcategory": "BATTERY_SYSTEM",
        "currentQuantity": 2,
        "minQuantity": 5,
        "reorderPoint": 10,
        "maxQuantity": 50,
        "stockStatus": "LOW_STOCK",
        "severity": "CRITICAL",
        "daysUntilOutOfStock": 1,
        "supplierId": 1,
        "supplierName": "Tesla Parts Supplier",
        "supplierContact": "supplier@tesla.com",
        "supplierPhone": "+1-555-0123",
        "lastRestocked": "2024-01-12T00:00:00Z",
        "averageUsage": 3.0,
        "recommendedQuantity": 30,
        "estimatedCost": 9000.0,
        "currency": "USD",
        "alertCreatedAt": "2024-01-15T00:00:00Z",
        "alertUpdatedAt": "2024-01-15T00:00:00Z"
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
      "totalAlerts": 25,
      "criticalAlerts": 5,
      "highAlerts": 15,
      "mediumAlerts": 5,
      "totalEstimatedCost": 125000.0,
      "averageDaysUntilOutOfStock": 2.5,
      "totalRecommendedQuantity": 1250
    },
    "filters": {
      "serviceCenterId": null,
      "oemId": null,
      "category": null,
      "severity": null
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: limit must be between 1 and 100",
  "path": "/api/inventory/alerts/low-stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Severity không hợp lệ

```json
{
  "error": "Invalid severity. Valid severities are: CRITICAL, HIGH, MEDIUM, LOW",
  "path": "/api/inventory/alerts/low-stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve low stock alerts: Internal server error",
  "path": "/api/inventory/alerts/low-stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem cảnh báo tồn kho trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy cảnh báo tồn kho thấp mặc định

**Request:**

```
GET /api/inventory/alerts/low-stock
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách cảnh báo

### Test Case 2: Filter theo service center

**Request:**

```
GET /api/inventory/alerts/low-stock?serviceCenterId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với cảnh báo của service center 1

### Test Case 3: Filter theo severity

**Request:**

```
GET /api/inventory/alerts/low-stock?severity=CRITICAL
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ CRITICAL alerts

### Test Case 4: Filter theo category

**Request:**

```
GET /api/inventory/alerts/low-stock?category=ELECTRICAL
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ ELECTRICAL alerts

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/inventory/alerts/low-stock?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 alerts từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/inventory/alerts/low-stock?severity=CRITICAL&limit=20" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/inventory/alerts/low-stock?severity=CRITICAL&limit=20",
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

url = "http://localhost:8080/api/inventory/alerts/low-stock"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "severity": "CRITICAL",
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

### Bước 2: Lấy cảnh báo tồn kho thấp

```bash
curl -X GET "http://localhost:8080/api/inventory/alerts/low-stock?severity=CRITICAL" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "alerts": [
      {
        "id": 2,
        "partId": 2,
        "partNumber": "TESLA-BMS-002",
        "partName": "Battery Management System Controller",
        "category": "ELECTRICAL",
        "subcategory": "BATTERY_SYSTEM",
        "currentQuantity": 2,
        "minQuantity": 5,
        "reorderPoint": 10,
        "maxQuantity": 50,
        "stockStatus": "LOW_STOCK",
        "severity": "CRITICAL",
        "daysUntilOutOfStock": 1,
        "supplierId": 1,
        "supplierName": "Tesla Parts Supplier",
        "supplierContact": "supplier@tesla.com",
        "supplierPhone": "+1-555-0123",
        "lastRestocked": "2024-01-12T00:00:00Z",
        "averageUsage": 3.0,
        "recommendedQuantity": 30,
        "estimatedCost": 9000.0,
        "currency": "USD",
        "alertCreatedAt": "2024-01-15T00:00:00Z",
        "alertUpdatedAt": "2024-01-15T00:00:00Z"
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
      "totalAlerts": 25,
      "criticalAlerts": 5,
      "highAlerts": 15,
      "mediumAlerts": 5,
      "totalEstimatedCost": 125000.0,
      "averageDaysUntilOutOfStock": 2.5,
      "totalRecommendedQuantity": 1250
    }
  }
}
```

## Response Fields Explanation

### Alert Object

- `id`: ID của alert
- `partId`: ID của phụ tùng
- `partNumber`: Số phụ tùng
- `partName`: Tên phụ tùng
- `category`: Danh mục phụ tùng
- `subcategory`: Danh mục con
- `currentQuantity`: Số lượng hiện tại
- `minQuantity`: Số lượng tối thiểu
- `reorderPoint`: Điểm đặt hàng lại
- `maxQuantity`: Số lượng tối đa
- `stockStatus`: Trạng thái tồn kho
- `severity`: Mức độ nghiêm trọng
- `daysUntilOutOfStock`: Số ngày đến khi hết hàng
- `supplierId`: ID nhà cung cấp
- `supplierName`: Tên nhà cung cấp
- `supplierContact`: Liên hệ nhà cung cấp
- `supplierPhone`: Số điện thoại nhà cung cấp
- `lastRestocked`: Lần cuối nhập hàng
- `averageUsage`: Sử dụng trung bình
- `recommendedQuantity`: Số lượng khuyến nghị
- `estimatedCost`: Chi phí ước tính
- `currency`: Đơn vị tiền tệ
- `alertCreatedAt`: Thời gian tạo alert
- `alertUpdatedAt`: Thời gian cập nhật alert

### Pagination Information

- `total`: Tổng số alerts thỏa mãn filter
- `limit`: Số lượng kết quả hiện tại
- `offset`: Vị trí bắt đầu hiện tại
- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không

### Summary Information

- `totalAlerts`: Tổng số alerts
- `criticalAlerts`: Số alerts CRITICAL
- `highAlerts`: Số alerts HIGH
- `mediumAlerts`: Số alerts MEDIUM
- `totalEstimatedCost`: Tổng chi phí ước tính
- `averageDaysUntilOutOfStock`: Số ngày trung bình đến khi hết hàng
- `totalRecommendedQuantity`: Tổng số lượng khuyến nghị

### Filter Information

- `serviceCenterId`: Service center được filter
- `oemId`: OEM được filter
- `category`: Danh mục được filter
- `severity`: Mức độ nghiêm trọng được filter

## Severity Levels

### CRITICAL

- Cần hành động ngay lập tức
- Sẽ hết hàng trong 1-2 ngày
- Ưu tiên cao nhất

### HIGH

- Cần hành động sớm
- Sẽ hết hàng trong 3-5 ngày
- Ưu tiên cao

### MEDIUM

- Cần hành động trong thời gian hợp lý
- Sẽ hết hàng trong 1-2 tuần
- Ưu tiên trung bình

### LOW

- Cần hành động khi có thời gian
- Sẽ hết hàng trong 2-4 tuần
- Ưu tiên thấp

## Stock Status

### LOW_STOCK

- Tồn kho thấp
- Cần đặt hàng sớm
- Cảnh báo tồn kho

### OUT_OF_STOCK

- Hết tồn kho
- Cần đặt hàng ngay
- Không thể sử dụng

### OVERSTOCK

- Tồn kho quá nhiều
- Cần giảm tồn kho
- Cảnh báo tồn kho

## Filter Options

### Service Center Filter

- `serviceCenterId`: Filter theo service center
- Chỉ áp dụng cho SC_Staff và SC_Technician

### OEM Filter

- `oemId`: Filter theo OEM
- Chỉ áp dụng cho EVM_Staff và Admin

### Category Filter

- `ELECTRICAL`: Chỉ phụ tùng điện
- `MECHANICAL`: Chỉ phụ tùng cơ khí
- `BODY`: Chỉ phụ tùng thân xe
- `ENGINE`: Chỉ phụ tùng động cơ
- `TRANSMISSION`: Chỉ phụ tùng hộp số

### Severity Filter

- `CRITICAL`: Chỉ alerts CRITICAL
- `HIGH`: Chỉ alerts HIGH
- `MEDIUM`: Chỉ alerts MEDIUM
- `LOW`: Chỉ alerts LOW

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

- ✅ Có thể xem tất cả cảnh báo
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem cảnh báo của OEM
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem cảnh báo OEM khác

### SC_Staff

- ✅ Có thể xem cảnh báo của service center
- ❌ Không thể xem cảnh báo service center khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem cảnh báo được gán
- ❌ Không thể xem cảnh báo không được gán
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng parts để lấy thông tin
- Sử dụng pagination để giới hạn kết quả

### Caching

- Cache alerts trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thay đổi tồn kho

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
- Alerts được sắp xếp theo severity và daysUntilOutOfStock
- Filter information được trả về để debug
- Pagination information được trả về để navigation
- Summary information được trả về để overview
- Performance được tối ưu thông qua caching
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
