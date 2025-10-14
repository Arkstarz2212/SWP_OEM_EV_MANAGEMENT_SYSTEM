# GET /api/vehicles/{id}

## Mô tả

Lấy thông tin chi tiết của xe theo ID. Endpoint này cho phép xem thông tin đầy đủ về xe bao gồm thông tin cơ bản, chủ sở hữu, bảo hành, và lịch sử dịch vụ.

## Endpoint

```
GET /api/vehicles/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description       |
| --------- | ---- | -------- | ----------------- |
| id        | long | Yes      | ID của xe cần lấy |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "vin": "1HGBH41JXMN109186",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "color": "Pearl White",
    "engineType": "Electric",
    "fuelType": "Electric",
    "transmission": "Automatic",
    "ownerName": "John Doe",
    "ownerEmail": "john.doe@example.com",
    "ownerPhone": "+1-555-0123",
    "ownerAddress": "123 Main Street, New York, NY 10001",
    "purchaseDate": "2023-01-15",
    "purchasePrice": 45000.0,
    "dealerName": "Tesla Store NYC",
    "dealerAddress": "456 Broadway, New York, NY 10013",
    "warrantyStartDate": "2023-01-15",
    "warrantyEndDate": "2028-01-15",
    "warrantyMileage": 50000,
    "currentMileage": 15000,
    "warrantyStatus": "ACTIVE",
    "warrantyCoverage": 100.0,
    "remainingMileage": 35000,
    "remainingDays": 1825,
    "serviceCenterId": 1,
    "serviceCenterName": "Downtown Service Center",
    "serviceHistory": [
      {
        "id": 1,
        "serviceDate": "2023-06-15",
        "serviceType": "MAINTENANCE",
        "description": "Regular maintenance service",
        "mileage": 10000,
        "cost": 150.0,
        "serviceCenterName": "Downtown Service Center"
      }
    ],
    "warrantyClaims": [
      {
        "id": 1,
        "claimNumber": "WC-2024-001",
        "issueDescription": "Battery management system error",
        "status": "COMPLETED",
        "createdAt": "2024-01-01T00:00:00Z",
        "completedAt": "2024-01-05T00:00:00Z"
      }
    ],
    "registrationDate": "2024-01-01T00:00:00Z",
    "registeredBy": "admin@example.com",
    "lastUpdated": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com"
  }
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid vehicle ID",
  "path": "/api/vehicles/invalid",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Xe không tồn tại

```json
{
  "error": "Vehicle not found",
  "path": "/api/vehicles/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền truy cập

```json
{
  "error": "Insufficient permissions to view vehicle",
  "path": "/api/vehicles/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve vehicle details: Internal server error",
  "path": "/api/vehicles/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem xe trong phạm vi quyền
- **Vehicle:** Xe phải tồn tại và active

## Test Cases

### Test Case 1: Lấy thông tin xe thành công

**Request:**

```
GET /api/vehicles/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin xe

### Test Case 2: Xe không tồn tại

**Request:**

```
GET /api/vehicles/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found

### Test Case 3: ID không hợp lệ

**Request:**

```
GET /api/vehicles/invalid
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Không có quyền truy cập

**Request:**

```
GET /api/vehicles/1
Authorization: Bearer sess_unauthorized_token
```

**Expected Response:** 403 Forbidden

### Test Case 5: Xe bị inactive

**Request:**

```
GET /api/vehicles/2
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found (nếu không có quyền xem inactive)

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/vehicles/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/vehicles/1", {
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

url = "http://localhost:8080/api/vehicles/1"
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

### Bước 2: Lấy thông tin xe

```bash
curl -X GET "http://localhost:8080/api/vehicles/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "vin": "1HGBH41JXMN109186",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "color": "Pearl White",
    "engineType": "Electric",
    "fuelType": "Electric",
    "transmission": "Automatic",
    "ownerName": "John Doe",
    "ownerEmail": "john.doe@example.com",
    "ownerPhone": "+1-555-0123",
    "ownerAddress": "123 Main Street, New York, NY 10001",
    "purchaseDate": "2023-01-15",
    "purchasePrice": 45000.0,
    "warrantyStartDate": "2023-01-15",
    "warrantyEndDate": "2028-01-15",
    "warrantyMileage": 50000,
    "currentMileage": 15000,
    "warrantyStatus": "ACTIVE",
    "warrantyCoverage": 100.0,
    "remainingMileage": 35000,
    "remainingDays": 1825
  }
}
```

## Response Fields Explanation

### Basic Vehicle Information

- `id`: ID duy nhất của xe
- `vin`: VIN của xe (17 ký tự)
- `make`: Hãng xe
- `model`: Model xe
- `year`: Năm sản xuất
- `color`: Màu sắc xe
- `engineType`: Loại động cơ
- `fuelType`: Loại nhiên liệu
- `transmission`: Loại hộp số

### Owner Information

- `ownerName`: Tên chủ sở hữu
- `ownerEmail`: Email chủ sở hữu
- `ownerPhone`: Số điện thoại chủ sở hữu
- `ownerAddress`: Địa chỉ chủ sở hữu

### Purchase Information

- `purchaseDate`: Ngày mua xe
- `purchasePrice`: Giá mua xe
- `dealerName`: Tên đại lý (nếu có)
- `dealerAddress`: Địa chỉ đại lý (nếu có)

### Warranty Information

- `warrantyStartDate`: Ngày bắt đầu bảo hành
- `warrantyEndDate`: Ngày kết thúc bảo hành
- `warrantyMileage`: Số km bảo hành
- `currentMileage`: Số km hiện tại
- `warrantyStatus`: Trạng thái bảo hành
- `warrantyCoverage`: Tỷ lệ bảo hành (%)
- `remainingMileage`: Số km còn lại
- `remainingDays`: Số ngày còn lại

### Service Center Information

- `serviceCenterId`: ID service center (nếu có)
- `serviceCenterName`: Tên service center (nếu có)

### Service History

- `id`: ID dịch vụ
- `serviceDate`: Ngày dịch vụ
- `serviceType`: Loại dịch vụ
- `description`: Mô tả dịch vụ
- `mileage`: Số km tại thời điểm dịch vụ
- `cost`: Chi phí dịch vụ
- `serviceCenterName`: Tên service center

### Warranty Claims

- `id`: ID claim
- `claimNumber`: Số claim
- `issueDescription`: Mô tả vấn đề
- `status`: Trạng thái claim
- `createdAt`: Thời gian tạo
- `completedAt`: Thời gian hoàn thành

### Audit Information

- `registrationDate`: Ngày đăng ký
- `registeredBy`: Người đăng ký
- `lastUpdated`: Thời gian cập nhật cuối
- `updatedBy`: Người cập nhật cuối

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả xe
- ✅ Có thể xem thông tin nhạy cảm
- ✅ Có thể xem service history và claims

### EVM_Staff

- ✅ Có thể xem xe của OEM
- ✅ Có thể xem thông tin nhạy cảm
- ❌ Không thể xem xe OEM khác

### SC_Staff

- ✅ Có thể xem xe của service center
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem xe service center khác

### SC_Technician

- ✅ Có thể xem xe được gán
- ❌ Không thể xem thông tin nhạy cảm
- ❌ Không thể xem xe không được gán

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Service history được lấy từ service records
- Warranty claims được lấy từ claim records
- Warranty status được tính toán real-time
- Warranty coverage được tính từ warranty policy
- Remaining mileage và days được tính tự động
- Service center information được lấy từ service center records
- Owner information được lấy từ owner records
- Purchase information được lấy từ purchase records
- Dealer information được lấy từ dealer records
- Audit information được lấy từ audit logs
- Service history được sắp xếp theo ngày
- Warranty claims được sắp xếp theo trạng thái
- Performance metrics được tính toán real-time
- Cache được sử dụng để tối ưu performance
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
