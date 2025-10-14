# POST /api/vehicles

## Mô tả

Đăng ký xe mới vào hệ thống AOEM. Endpoint này cho phép Admin, EVM_Staff và SC_Staff đăng ký xe với thông tin chi tiết bao gồm VIN, thông tin chủ sở hữu, và thông tin bảo hành.

## Endpoint

```
POST /api/vehicles
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
  "serviceCenterId": 1,
  "notes": "Vehicle registered for warranty coverage"
}
```

### Parameters

| Field             | Type    | Required | Description                         |
| ----------------- | ------- | -------- | ----------------------------------- |
| vin               | string  | Yes      | VIN của xe (17 ký tự)               |
| make              | string  | Yes      | Hãng xe                             |
| model             | string  | Yes      | Model xe                            |
| year              | integer | Yes      | Năm sản xuất                        |
| color             | string  | Yes      | Màu sắc xe                          |
| engineType        | string  | Yes      | Loại động cơ                        |
| fuelType          | string  | Yes      | Loại nhiên liệu                     |
| transmission      | string  | Yes      | Loại hộp số                         |
| ownerName         | string  | Yes      | Tên chủ sở hữu                      |
| ownerEmail        | string  | Yes      | Email chủ sở hữu                    |
| ownerPhone        | string  | Yes      | Số điện thoại chủ sở hữu            |
| ownerAddress      | string  | Yes      | Địa chỉ chủ sở hữu                  |
| purchaseDate      | string  | Yes      | Ngày mua xe (YYYY-MM-DD)            |
| purchasePrice     | number  | Yes      | Giá mua xe                          |
| dealerName        | string  | No       | Tên đại lý                          |
| dealerAddress     | string  | No       | Địa chỉ đại lý                      |
| warrantyStartDate | string  | Yes      | Ngày bắt đầu bảo hành (YYYY-MM-DD)  |
| warrantyEndDate   | string  | Yes      | Ngày kết thúc bảo hành (YYYY-MM-DD) |
| warrantyMileage   | integer | Yes      | Số km bảo hành                      |
| currentMileage    | integer | Yes      | Số km hiện tại                      |
| serviceCenterId   | long    | No       | ID service center gán               |
| notes             | string  | No       | Ghi chú bổ sung                     |

## Response

### Success Response (201 Created)

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
    "serviceCenterId": 1,
    "serviceCenterName": "Downtown Service Center",
    "warrantyStatus": "ACTIVE",
    "warrantyCoverage": 100.0,
    "remainingMileage": 35000,
    "remainingDays": 1825,
    "registrationDate": "2024-01-01T00:00:00Z",
    "registeredBy": "admin@example.com",
    "notes": "Vehicle registered for warranty coverage"
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
      "field": "vin",
      "message": "VIN must be exactly 17 characters"
    },
    {
      "field": "ownerEmail",
      "message": "Invalid email format"
    }
  ],
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - VIN đã tồn tại

```json
{
  "error": "VIN already exists in the system",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Service center không tồn tại

```json
{
  "error": "Service center not found",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to register vehicle",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to register vehicle: Internal server error",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin, EVM_Staff hoặc SC_Staff role
- **VIN:** VIN phải duy nhất trong hệ thống
- **Service Center:** Service center phải tồn tại (nếu được cung cấp)

## Test Cases

### Test Case 1: Đăng ký xe thành công

**Request:**

```json
{
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
  "currentMileage": 15000
}
```

**Expected Response:** 201 Created với thông tin xe mới

### Test Case 2: Đăng ký xe với service center

**Request:**

```json
{
  "vin": "1HGBH41JXMN109187",
  "make": "Tesla",
  "model": "Model S",
  "year": 2023,
  "color": "Midnight Silver",
  "engineType": "Electric",
  "fuelType": "Electric",
  "transmission": "Automatic",
  "ownerName": "Jane Smith",
  "ownerEmail": "jane.smith@example.com",
  "ownerPhone": "+1-555-0456",
  "ownerAddress": "456 Oak Avenue, Los Angeles, CA 90210",
  "purchaseDate": "2023-02-01",
  "purchasePrice": 80000.0,
  "warrantyStartDate": "2023-02-01",
  "warrantyEndDate": "2028-02-01",
  "warrantyMileage": 100000,
  "currentMileage": 25000,
  "serviceCenterId": 1
}
```

**Expected Response:** 201 Created với service center được gán

### Test Case 3: VIN đã tồn tại

**Request:**

```json
{
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
  "currentMileage": 15000
}
```

**Expected Response:** 400 Bad Request

### Test Case 4: VIN không hợp lệ

**Request:**

```json
{
  "vin": "INVALID_VIN",
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
  "currentMileage": 15000
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Service center không tồn tại

**Request:**

```json
{
  "vin": "1HGBH41JXMN109188",
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
  "serviceCenterId": 999
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/vehicles" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
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
    "currentMileage": 15000
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/vehicles", {
  method: "POST",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    vin: "1HGBH41JXMN109186",
    make: "Tesla",
    model: "Model 3",
    year: 2023,
    color: "Pearl White",
    engineType: "Electric",
    fuelType: "Electric",
    transmission: "Automatic",
    ownerName: "John Doe",
    ownerEmail: "john.doe@example.com",
    ownerPhone: "+1-555-0123",
    ownerAddress: "123 Main Street, New York, NY 10001",
    purchaseDate: "2023-01-15",
    purchasePrice: 45000.0,
    warrantyStartDate: "2023-01-15",
    warrantyEndDate: "2028-01-15",
    warrantyMileage: 50000,
    currentMileage: 15000,
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/vehicles"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
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
    "currentMileage": 15000
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

### Bước 2: Đăng ký xe mới

```bash
curl -X POST "http://localhost:8080/api/vehicles" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
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
    "currentMileage": 15000
  }'
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
    "remainingDays": 1825,
    "registrationDate": "2024-01-01T00:00:00Z",
    "registeredBy": "scstaff@example.com"
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

### Registration Information

- `registrationDate`: Ngày đăng ký
- `registeredBy`: Người đăng ký
- `notes`: Ghi chú bổ sung

## VIN Validation

### Format Requirements

- Phải có đúng 17 ký tự
- Chỉ chứa chữ cái và số
- Không chứa ký tự đặc biệt
- Không chứa khoảng trắng

### Character Restrictions

- Không được chứa I, O, Q
- Chỉ chứa A-Z và 0-9
- Phải có format chuẩn

### Uniqueness

- VIN phải duy nhất trong hệ thống
- Không được trùng với VIN đã tồn tại
- Phải được validate trước khi lưu

## Warranty Status

### ACTIVE

- Bảo hành đang có hiệu lực
- Có thể sử dụng dịch vụ bảo hành
- Tất cả quyền lợi bảo hành

### EXPIRED

- Bảo hành đã hết hạn
- Không thể sử dụng dịch vụ bảo hành
- Chỉ có thể sử dụng dịch vụ trả phí

### SUSPENDED

- Bảo hành bị tạm dừng
- Không thể sử dụng dịch vụ bảo hành
- Cần liên hệ để khôi phục

## Validation Rules

### VIN

- Phải có đúng 17 ký tự
- Phải duy nhất trong hệ thống
- Phải có format hợp lệ

### Owner Information

- `ownerName`: Không được trống, tối đa 100 ký tự
- `ownerEmail`: Phải có format email hợp lệ
- `ownerPhone`: Phải có format số điện thoại hợp lệ
- `ownerAddress`: Không được trống, tối đa 200 ký tự

### Vehicle Information

- `make`: Không được trống, tối đa 50 ký tự
- `model`: Không được trống, tối đa 50 ký tự
- `year`: Phải từ 1900 đến năm hiện tại + 1
- `color`: Không được trống, tối đa 30 ký tự

### Warranty Information

- `warrantyStartDate`: Phải trước hoặc bằng warrantyEndDate
- `warrantyEndDate`: Phải sau hoặc bằng warrantyStartDate
- `warrantyMileage`: Phải là số dương
- `currentMileage`: Phải là số không âm

### Service Center

- `serviceCenterId`: Phải tồn tại trong hệ thống
- Phải active
- Phải có quyền xử lý loại xe này

## Permission Matrix

### Admin

- ✅ Có thể đăng ký xe cho bất kỳ service center nào
- ✅ Có thể đăng ký xe với đầy đủ thông tin
- ✅ Có thể gán service center bất kỳ

### EVM_Staff

- ✅ Có thể đăng ký xe cho service centers trong OEM
- ✅ Có thể đăng ký xe với đầy đủ thông tin
- ❌ Không thể gán service center ngoài OEM

### SC_Staff

- ✅ Có thể đăng ký xe cho service center của mình
- ✅ Có thể đăng ký xe với thông tin cơ bản
- ❌ Không thể gán service center khác

### SC_Technician

- ❌ Không có quyền đăng ký xe
- ❌ Chỉ có thể xem thông tin xe

## Automatic Actions

### Vehicle Registration

- Tạo vehicle record trong database
- Gán ID duy nhất
- Tạo audit log

### Warranty Setup

- Thiết lập warranty policy
- Tính toán warranty coverage
- Tạo warranty record

### Service Center Assignment

- Gán service center nếu được cung cấp
- Cập nhật service center capacity
- Tạo assignment record

### Notification

- Gửi email đến owner về đăng ký
- Gửi thông báo đến service center
- Tạo task cho service center

## Manual Actions Required

### Owner Notification

- Owner cần xác nhận thông tin
- Cập nhật thông tin cá nhân
- Thiết lập tài khoản

### Service Center Setup

- Service center cần xác nhận assignment
- Cập nhật thông tin xe
- Thiết lập quy trình

## Notes

- Endpoint này dành cho Admin, EVM_Staff và SC_Staff
- SC_Technician không có quyền sử dụng
- VIN phải duy nhất trong hệ thống
- Service center phải tồn tại và active
- Warranty information phải hợp lệ
- Owner information phải đầy đủ và chính xác
- Vehicle information phải đầy đủ
- Warranty coverage được tính tự động
- Remaining mileage và days được tính tự động
- Registration date được set tự động
- Registered by được set từ session
- Notes là optional
- Dealer information là optional
- Service center assignment là optional
- Warranty status được set tự động
- Warranty coverage được tính từ warranty policy
- Remaining mileage được tính từ warranty mileage - current mileage
- Remaining days được tính từ warranty end date - current date
- VIN validation được thực hiện nghiêm ngặt
- Owner information được validate đầy đủ
- Vehicle information được validate đầy đủ
- Warranty information được validate đầy đủ
- Service center assignment được validate
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
