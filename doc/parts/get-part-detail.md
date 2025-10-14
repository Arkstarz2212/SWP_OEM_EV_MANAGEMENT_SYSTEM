# GET /api/parts/{id}

## Mô tả

Lấy thông tin chi tiết của phụ tùng theo ID. Endpoint này cho phép xem thông tin đầy đủ về phụ tùng bao gồm thông số kỹ thuật, giá cả, tồn kho, và lịch sử sử dụng.

## Endpoint

```
GET /api/parts/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description             |
| --------- | ---- | -------- | ----------------------- |
| id        | long | Yes      | ID của phụ tùng cần lấy |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "compatibleVehicles": ["Model 3", "Model Y"],
    "specifications": {
      "voltage": "12V",
      "current": "5A",
      "temperature": "-40°C to +85°C",
      "dimensions": "50x30x20mm",
      "weight": "0.1kg"
    },
    "pricing": {
      "cost": 150.0,
      "retailPrice": 200.0,
      "warrantyPrice": 180.0,
      "currency": "USD"
    },
    "supplier": {
      "name": "Tesla Parts Supplier",
      "contact": "supplier@tesla.com",
      "phone": "+1-555-0123",
      "address": "123 Supplier Street, CA 90210"
    },
    "warranty": {
      "period": 24,
      "unit": "MONTHS",
      "conditions": "Standard warranty terms apply"
    },
    "stock": {
      "minQuantity": 10,
      "maxQuantity": 100,
      "reorderPoint": 20,
      "currentQuantity": 45,
      "reservedQuantity": 5,
      "availableQuantity": 40
    },
    "usage": {
      "totalUsed": 25,
      "lastUsed": "2024-01-10",
      "averageUsage": 2.5,
      "usageTrend": "INCREASING"
    },
    "performance": {
      "reliability": 4.5,
      "customerSatisfaction": 4.2,
      "returnRate": 0.02,
      "defectRate": 0.01
    },
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "admin@example.com",
    "lastUpdated": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com"
  }
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid part ID",
  "path": "/api/parts/invalid",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Phụ tùng không tồn tại

```json
{
  "error": "Part not found",
  "path": "/api/parts/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền truy cập

```json
{
  "error": "Insufficient permissions to view part",
  "path": "/api/parts/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve part details: Internal server error",
  "path": "/api/parts/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem phụ tùng trong phạm vi quyền
- **Part:** Phụ tùng phải tồn tại và active

## Test Cases

### Test Case 1: Lấy thông tin phụ tùng thành công

**Request:**

```
GET /api/parts/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin phụ tùng

### Test Case 2: Phụ tùng không tồn tại

**Request:**

```
GET /api/parts/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found

### Test Case 3: ID không hợp lệ

**Request:**

```
GET /api/parts/invalid
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Không có quyền truy cập

**Request:**

```
GET /api/parts/1
Authorization: Bearer sess_unauthorized_token
```

**Expected Response:** 403 Forbidden

### Test Case 5: Phụ tùng bị inactive

**Request:**

```
GET /api/parts/2
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found (nếu không có quyền xem inactive)

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/parts/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/parts/1", {
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

url = "http://localhost:8080/api/parts/1"
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

### Bước 2: Lấy thông tin phụ tùng

```bash
curl -X GET "http://localhost:8080/api/parts/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "pricing": {
      "cost": 150.0,
      "retailPrice": 200.0,
      "warrantyPrice": 180.0,
      "currency": "USD"
    },
    "supplier": {
      "name": "Tesla Parts Supplier",
      "contact": "supplier@tesla.com",
      "phone": "+1-555-0123",
      "address": "123 Supplier Street, CA 90210"
    },
    "stock": {
      "minQuantity": 10,
      "maxQuantity": 100,
      "reorderPoint": 20,
      "currentQuantity": 45,
      "reservedQuantity": 5,
      "availableQuantity": 40
    },
    "status": "ACTIVE"
  }
}
```

## Response Fields Explanation

### Basic Part Information

- `id`: ID duy nhất của phụ tùng
- `partNumber`: Số phụ tùng (duy nhất)
- `name`: Tên phụ tùng
- `description`: Mô tả phụ tùng
- `category`: Danh mục phụ tùng
- `subcategory`: Danh mục con

### OEM Information

- `oemId`: ID của OEM
- `oemName`: Tên OEM

### Vehicle Compatibility

- `make`: Hãng xe tương thích
- `model`: Model xe tương thích
- `year`: Năm sản xuất tương thích
- `compatibleVehicles`: Danh sách xe tương thích

### Technical Specifications

- `specifications`: Thông số kỹ thuật
  - `voltage`: Điện áp
  - `current`: Dòng điện
  - `temperature`: Nhiệt độ hoạt động
  - `dimensions`: Kích thước
  - `weight`: Trọng lượng

### Pricing Information

- `pricing`: Thông tin giá cả
  - `cost`: Giá gốc
  - `retailPrice`: Giá bán lẻ
  - `warrantyPrice`: Giá bảo hành
  - `currency`: Đơn vị tiền tệ

### Supplier Information

- `supplier`: Thông tin nhà cung cấp
  - `name`: Tên nhà cung cấp
  - `contact`: Liên hệ
  - `phone`: Số điện thoại
  - `address`: Địa chỉ

### Warranty Information

- `warranty`: Thông tin bảo hành
  - `period`: Thời gian bảo hành
  - `unit`: Đơn vị thời gian
  - `conditions`: Điều kiện bảo hành

### Stock Information

- `stock`: Thông tin tồn kho
  - `minQuantity`: Số lượng tối thiểu
  - `maxQuantity`: Số lượng tối đa
  - `reorderPoint`: Điểm đặt hàng lại
  - `currentQuantity`: Số lượng hiện tại
  - `reservedQuantity`: Số lượng đã đặt
  - `availableQuantity`: Số lượng có sẵn

### Usage Information

- `usage`: Thông tin sử dụng
  - `totalUsed`: Tổng số đã sử dụng
  - `lastUsed`: Lần sử dụng cuối
  - `averageUsage`: Sử dụng trung bình
  - `usageTrend`: Xu hướng sử dụng

### Performance Information

- `performance`: Thông tin hiệu suất
  - `reliability`: Độ tin cậy (1-5)
  - `customerSatisfaction`: Đánh giá khách hàng (1-5)
  - `returnRate`: Tỷ lệ trả hàng
  - `defectRate`: Tỷ lệ lỗi

### Status Information

- `status`: Trạng thái phụ tùng
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo
- `lastUpdated`: Thời gian cập nhật cuối
- `updatedBy`: Người cập nhật cuối

## Part Statuses

### ACTIVE

- Phụ tùng đang hoạt động
- Có thể sử dụng
- Tất cả chức năng hoạt động

### INACTIVE

- Phụ tùng tạm dừng
- Không thể sử dụng
- Chỉ có thể xem thông tin

### DISCONTINUED

- Phụ tùng ngừng sản xuất
- Không thể sử dụng
- Chỉ có thể xem thông tin

### OUT_OF_STOCK

- Phụ tùng hết hàng
- Không thể sử dụng
- Cần đặt hàng

## Usage Trends

### INCREASING

- Xu hướng sử dụng tăng
- Cần tăng stock
- Cần theo dõi

### DECREASING

- Xu hướng sử dụng giảm
- Cần giảm stock
- Cần đánh giá

### STABLE

- Xu hướng sử dụng ổn định
- Stock hiện tại phù hợp
- Không cần thay đổi

## Performance Metrics

### Reliability

- Độ tin cậy của phụ tùng
- Đánh giá từ 1-5
- Dựa trên lịch sử sử dụng

### Customer Satisfaction

- Đánh giá khách hàng
- Đánh giá từ 1-5
- Dựa trên feedback

### Return Rate

- Tỷ lệ trả hàng
- Phần trăm
- Dựa trên lịch sử

### Defect Rate

- Tỷ lệ lỗi
- Phần trăm
- Dựa trên quality control

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả phụ tùng
- ✅ Có thể xem thông tin nhạy cảm
- ✅ Có thể xem performance metrics

### EVM_Staff

- ✅ Có thể xem phụ tùng của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem phụ tùng OEM khác

### SC_Staff

- ✅ Có thể xem phụ tùng của service center
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem phụ tùng service center khác

### SC_Technician

- ✅ Có thể xem phụ tùng được gán
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem phụ tùng không được gán

## Performance Considerations

### Database Optimization

- Sử dụng index trên part ID
- Join với bảng suppliers để lấy thông tin
- Join với bảng inventory để lấy stock

### Caching

- Cache part details trong 10 phút
- Invalidate cache khi có thay đổi
- Cache performance metrics riêng biệt

### Data Aggregation

- Tính toán performance metrics real-time
- Sử dụng background jobs cho heavy calculations
- Cache kết quả trong database

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Performance metrics được tính toán real-time
- Stock information được cập nhật real-time
- Usage information được tính từ usage records
- Performance metrics được tính từ feedback
- Supplier information được lấy từ supplier records
- Warranty information được lấy từ warranty records
- Audit information được lấy từ audit logs
- Performance metrics được cập nhật theo thời gian
- Stock information được cập nhật từ inventory
- Usage information được cập nhật từ usage records
- Performance metrics được cập nhật từ feedback
- Cache được sử dụng để tối ưu performance
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
