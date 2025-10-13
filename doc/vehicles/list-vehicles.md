# GET /api/vehicles

## Mô tả

Lấy danh sách xe với khả năng filter theo nhiều tiêu chí. Endpoint này cho phép xem danh sách xe với thông tin tóm tắt và phân trang.

## Endpoint

```
GET /api/vehicles
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type    | Required | Description                           |
| --------------- | ------- | -------- | ------------------------------------- |
| make            | string  | No       | Filter theo hãng xe                   |
| model           | string  | No       | Filter theo model xe                  |
| year            | integer | No       | Filter theo năm sản xuất              |
| color           | string  | No       | Filter theo màu sắc                   |
| warrantyStatus  | string  | No       | Filter theo trạng thái bảo hành       |
| serviceCenterId | long    | No       | Filter theo service center ID         |
| ownerEmail      | string  | No       | Filter theo email chủ sở hữu          |
| limit           | integer | No       | Số lượng kết quả (1-100, mặc định 20) |
| offset          | integer | No       | Vị trí bắt đầu (mặc định 0)           |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "vehicles": [
      {
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
        "currentMileage": 15000,
        "warrantyStatus": "ACTIVE",
        "warrantyCoverage": 100.0,
        "remainingMileage": 35000,
        "remainingDays": 1825,
        "serviceCenterId": 1,
        "serviceCenterName": "Downtown Service Center",
        "lastServiceDate": "2023-06-15",
        "totalServices": 3,
        "totalClaims": 1
      },
      {
        "id": 2,
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
        "currentMileage": 25000,
        "warrantyStatus": "ACTIVE",
        "warrantyCoverage": 100.0,
        "remainingMileage": 75000,
        "remainingDays": 1825,
        "serviceCenterId": 2,
        "serviceCenterName": "Uptown Service Center",
        "lastServiceDate": "2023-08-20",
        "totalServices": 5,
        "totalClaims": 0
      }
    ],
    "pagination": {
      "total": 1250,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalVehicles": 1250,
      "activeWarranty": 1100,
      "expiredWarranty": 150,
      "averageMileage": 18500,
      "averageAge": 2.3
    },
    "filters": {
      "make": null,
      "model": null,
      "year": null,
      "color": null,
      "warrantyStatus": null,
      "serviceCenterId": null,
      "ownerEmail": null
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: limit must be between 1 and 100",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Warranty status không hợp lệ

```json
{
  "error": "Invalid warranty status. Valid statuses are: ACTIVE, EXPIRED, SUSPENDED",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve vehicles: Internal server error",
  "path": "/api/vehicles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem xe trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy danh sách xe mặc định

**Request:**

```
GET /api/vehicles
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách xe

### Test Case 2: Filter theo make Tesla

**Request:**

```
GET /api/vehicles?make=Tesla
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ Tesla vehicles

### Test Case 3: Filter theo warranty status ACTIVE

**Request:**

```
GET /api/vehicles?warrantyStatus=ACTIVE
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ ACTIVE warranty vehicles

### Test Case 4: Filter theo service center

**Request:**

```
GET /api/vehicles?serviceCenterId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với vehicles của service center 1

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/vehicles?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 vehicles từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/vehicles?make=Tesla&warrantyStatus=ACTIVE&limit=20" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/vehicles?make=Tesla&warrantyStatus=ACTIVE&limit=20",
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

url = "http://localhost:8080/api/vehicles"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "make": "Tesla",
    "warrantyStatus": "ACTIVE",
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

### Bước 2: Lấy danh sách xe

```bash
curl -X GET "http://localhost:8080/api/vehicles?serviceCenterId=1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "vehicles": [
      {
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
        "currentMileage": 15000,
        "warrantyStatus": "ACTIVE",
        "warrantyCoverage": 100.0,
        "remainingMileage": 35000,
        "remainingDays": 1825,
        "serviceCenterId": 1,
        "serviceCenterName": "Downtown Service Center",
        "lastServiceDate": "2023-06-15",
        "totalServices": 3,
        "totalClaims": 1
      }
    ],
    "pagination": {
      "total": 1250,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalVehicles": 1250,
      "activeWarranty": 1100,
      "expiredWarranty": 150,
      "averageMileage": 18500,
      "averageAge": 2.3
    }
  }
}
```

## Response Fields Explanation

### Vehicle Object

- `id`: ID duy nhất của xe
- `vin`: VIN của xe (17 ký tự)
- `make`: Hãng xe
- `model`: Model xe
- `year`: Năm sản xuất
- `color`: Màu sắc xe
- `engineType`: Loại động cơ
- `fuelType`: Loại nhiên liệu
- `transmission`: Loại hộp số
- `ownerName`: Tên chủ sở hữu
- `ownerEmail`: Email chủ sở hữu
- `ownerPhone`: Số điện thoại chủ sở hữu
- `currentMileage`: Số km hiện tại
- `warrantyStatus`: Trạng thái bảo hành
- `warrantyCoverage`: Tỷ lệ bảo hành (%)
- `remainingMileage`: Số km còn lại
- `remainingDays`: Số ngày còn lại
- `serviceCenterId`: ID service center
- `serviceCenterName`: Tên service center
- `lastServiceDate`: Ngày dịch vụ cuối
- `totalServices`: Tổng số dịch vụ
- `totalClaims`: Tổng số claims

### Pagination Information

- `total`: Tổng số xe thỏa mãn filter
- `limit`: Số lượng kết quả hiện tại
- `offset`: Vị trí bắt đầu hiện tại
- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không

### Summary Information

- `totalVehicles`: Tổng số xe
- `activeWarranty`: Số xe có bảo hành active
- `expiredWarranty`: Số xe hết bảo hành
- `averageMileage`: Số km trung bình
- `averageAge`: Tuổi trung bình (năm)

### Filter Information

- `make`: Hãng xe được filter
- `model`: Model được filter
- `year`: Năm được filter
- `color`: Màu sắc được filter
- `warrantyStatus`: Trạng thái bảo hành được filter
- `serviceCenterId`: Service center được filter
- `ownerEmail`: Email chủ sở hữu được filter

## Filter Options

### Make Filter

- `Tesla`: Chỉ Tesla vehicles
- `BMW`: Chỉ BMW vehicles
- `Mercedes`: Chỉ Mercedes vehicles
- `Audi`: Chỉ Audi vehicles
- `Lexus`: Chỉ Lexus vehicles

### Model Filter

- `Model 3`: Chỉ Model 3
- `Model S`: Chỉ Model S
- `Model X`: Chỉ Model X
- `Model Y`: Chỉ Model Y

### Year Filter

- `2024`: Chỉ vehicles năm 2024
- `2023`: Chỉ vehicles năm 2023
- `2022`: Chỉ vehicles năm 2022
- `2021`: Chỉ vehicles năm 2021
- `2020`: Chỉ vehicles năm 2020

### Color Filter

- `Pearl White`: Chỉ xe màu trắng
- `Midnight Silver`: Chỉ xe màu bạc
- `Deep Blue`: Chỉ xe màu xanh
- `Red`: Chỉ xe màu đỏ
- `Black`: Chỉ xe màu đen

### Warranty Status Filter

- `ACTIVE`: Chỉ xe có bảo hành active
- `EXPIRED`: Chỉ xe hết bảo hành
- `SUSPENDED`: Chỉ xe bảo hành bị tạm dừng

### Service Center Filter

- `serviceCenterId`: Filter theo service center cụ thể
- Chỉ áp dụng cho SC_Staff và SC_Technician

### Owner Email Filter

- `ownerEmail`: Filter theo email chủ sở hữu
- Chỉ áp dụng cho Admin và EVM_Staff

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

- ✅ Có thể xem tất cả xe
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem xe của OEM
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem xe OEM khác

### SC_Staff

- ✅ Có thể xem xe của service center
- ❌ Không thể xem xe service center khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem xe được gán
- ❌ Không thể xem xe không được gán
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng service centers để lấy thông tin
- Sử dụng pagination để giới hạn kết quả

### Caching

- Cache danh sách xe trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thay đổi xe

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
- Vehicle object được tối ưu cho danh sách
- Performance metrics được tính toán từ service records
- Warranty information được lấy từ warranty records
- Service center information được lấy từ service center records
- Owner information được lấy từ owner records
- Filter information được trả về để debug
- Pagination information được trả về để navigation
- Summary information được trả về để overview
