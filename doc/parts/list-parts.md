# GET /api/parts

## Mô tả

Lấy danh sách phụ tùng với khả năng filter theo nhiều tiêu chí. Endpoint này cho phép xem danh sách phụ tùng với thông tin tóm tắt và phân trang.

## Endpoint

```
GET /api/parts
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter   | Type    | Required | Description                           |
| ----------- | ------- | -------- | ------------------------------------- |
| category    | string  | No       | Filter theo danh mục phụ tùng         |
| subcategory | string  | No       | Filter theo danh mục con              |
| oemId       | long    | No       | Filter theo OEM ID                    |
| make        | string  | No       | Filter theo hãng xe                   |
| model       | string  | No       | Filter theo model xe                  |
| year        | integer | No       | Filter theo năm sản xuất              |
| status      | string  | No       | Filter theo trạng thái                |
| supplier    | string  | No       | Filter theo nhà cung cấp              |
| minPrice    | number  | No       | Filter theo giá tối thiểu             |
| maxPrice    | number  | No       | Filter theo giá tối đa                |
| inStock     | boolean | No       | Filter theo tình trạng tồn kho        |
| limit       | integer | No       | Số lượng kết quả (1-100, mặc định 20) |
| offset      | integer | No       | Vị trí bắt đầu (mặc định 0)           |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "parts": [
      {
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
          "contact": "supplier@tesla.com"
        },
        "stock": {
          "currentQuantity": 45,
          "reservedQuantity": 5,
          "availableQuantity": 40,
          "reorderPoint": 20
        },
        "status": "ACTIVE",
        "createdAt": "2024-01-01T00:00:00Z"
      },
      {
        "id": 2,
        "partNumber": "TESLA-BMS-002",
        "name": "Battery Management System Controller",
        "description": "Advanced controller for battery management system",
        "category": "ELECTRICAL",
        "subcategory": "BATTERY_SYSTEM",
        "oemId": 1,
        "oemName": "Tesla Motors",
        "make": "Tesla",
        "model": "Model S",
        "year": 2023,
        "pricing": {
          "cost": 300.0,
          "retailPrice": 400.0,
          "warrantyPrice": 360.0,
          "currency": "USD"
        },
        "supplier": {
          "name": "Tesla Parts Supplier",
          "contact": "supplier@tesla.com"
        },
        "stock": {
          "currentQuantity": 25,
          "reservedQuantity": 0,
          "availableQuantity": 25,
          "reorderPoint": 10
        },
        "status": "ACTIVE",
        "createdAt": "2024-01-02T00:00:00Z"
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
      "totalParts": 1250,
      "activeParts": 1100,
      "inactiveParts": 150,
      "outOfStockParts": 50,
      "averagePrice": 250.0,
      "totalValue": 312500.0
    },
    "filters": {
      "category": null,
      "subcategory": null,
      "oemId": null,
      "make": null,
      "model": null,
      "year": null,
      "status": null,
      "supplier": null,
      "minPrice": null,
      "maxPrice": null,
      "inStock": null
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: limit must be between 1 and 100",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Category không hợp lệ

```json
{
  "error": "Invalid category. Valid categories are: ELECTRICAL, MECHANICAL, BODY, ENGINE, TRANSMISSION",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve parts: Internal server error",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem phụ tùng trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy danh sách phụ tùng mặc định

**Request:**

```
GET /api/parts
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách phụ tùng

### Test Case 2: Filter theo category ELECTRICAL

**Request:**

```
GET /api/parts?category=ELECTRICAL
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ ELECTRICAL parts

### Test Case 3: Filter theo OEM

**Request:**

```
GET /api/parts?oemId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với parts của OEM 1

### Test Case 4: Filter theo tình trạng tồn kho

**Request:**

```
GET /api/parts?inStock=true
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với parts còn hàng

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/parts?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 parts từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/parts?category=ELECTRICAL&oemId=1&limit=20" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/parts?category=ELECTRICAL&oemId=1&limit=20",
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

url = "http://localhost:8080/api/parts"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "category": "ELECTRICAL",
    "oemId": 1,
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

### Bước 2: Lấy danh sách phụ tùng

```bash
curl -X GET "http://localhost:8080/api/parts?category=ELECTRICAL" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "parts": [
      {
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
          "contact": "supplier@tesla.com"
        },
        "stock": {
          "currentQuantity": 45,
          "reservedQuantity": 5,
          "availableQuantity": 40,
          "reorderPoint": 20
        },
        "status": "ACTIVE",
        "createdAt": "2024-01-01T00:00:00Z"
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
      "totalParts": 1250,
      "activeParts": 1100,
      "inactiveParts": 150,
      "outOfStockParts": 50,
      "averagePrice": 250.0,
      "totalValue": 312500.0
    }
  }
}
```

## Response Fields Explanation

### Part Object

- `id`: ID duy nhất của phụ tùng
- `partNumber`: Số phụ tùng (duy nhất)
- `name`: Tên phụ tùng
- `description`: Mô tả phụ tùng
- `category`: Danh mục phụ tùng
- `subcategory`: Danh mục con
- `oemId`: ID của OEM
- `oemName`: Tên OEM
- `make`: Hãng xe tương thích
- `model`: Model xe tương thích
- `year`: Năm sản xuất tương thích
- `pricing`: Thông tin giá cả
- `supplier`: Thông tin nhà cung cấp
- `stock`: Thông tin tồn kho
- `status`: Trạng thái phụ tùng
- `createdAt`: Thời gian tạo

### Pagination Information

- `total`: Tổng số phụ tùng thỏa mãn filter
- `limit`: Số lượng kết quả hiện tại
- `offset`: Vị trí bắt đầu hiện tại
- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không

### Summary Information

- `totalParts`: Tổng số phụ tùng
- `activeParts`: Số phụ tùng đang hoạt động
- `inactiveParts`: Số phụ tùng không hoạt động
- `outOfStockParts`: Số phụ tùng hết hàng
- `averagePrice`: Giá trung bình
- `totalValue`: Tổng giá trị

### Filter Information

- `category`: Danh mục được filter
- `subcategory`: Danh mục con được filter
- `oemId`: OEM được filter
- `make`: Hãng xe được filter
- `model`: Model được filter
- `year`: Năm được filter
- `status`: Trạng thái được filter
- `supplier`: Nhà cung cấp được filter
- `minPrice`: Giá tối thiểu được filter
- `maxPrice`: Giá tối đa được filter
- `inStock`: Tình trạng tồn kho được filter

## Filter Options

### Category Filter

- `ELECTRICAL`: Chỉ phụ tùng điện
- `MECHANICAL`: Chỉ phụ tùng cơ khí
- `BODY`: Chỉ phụ tùng thân xe
- `ENGINE`: Chỉ phụ tùng động cơ
- `TRANSMISSION`: Chỉ phụ tùng hộp số

### Subcategory Filter

- `BATTERY_SYSTEM`: Hệ thống pin
- `SENSOR`: Cảm biến
- `CONTROLLER`: Điều khiển
- `WIRING`: Dây điện
- `FUSE`: Cầu chì

### OEM Filter

- `oemId`: Filter theo OEM cụ thể
- Chỉ áp dụng cho EVM_Staff và Admin

### Vehicle Filter

- `make`: Filter theo hãng xe
- `model`: Filter theo model xe
- `year`: Filter theo năm sản xuất

### Status Filter

- `ACTIVE`: Chỉ phụ tùng đang hoạt động
- `INACTIVE`: Chỉ phụ tùng không hoạt động
- `DISCONTINUED`: Chỉ phụ tùng ngừng sản xuất
- `OUT_OF_STOCK`: Chỉ phụ tùng hết hàng

### Supplier Filter

- `supplier`: Filter theo nhà cung cấp
- Chỉ áp dụng cho EVM_Staff và Admin

### Price Filter

- `minPrice`: Giá tối thiểu
- `maxPrice`: Giá tối đa
- Phải là số dương

### Stock Filter

- `inStock`: Tình trạng tồn kho
- `true`: Chỉ phụ tùng còn hàng
- `false`: Chỉ phụ tùng hết hàng

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

- ✅ Có thể xem tất cả phụ tùng
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem phụ tùng của OEM
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem phụ tùng OEM khác

### SC_Staff

- ✅ Có thể xem phụ tùng của service center
- ❌ Không thể xem phụ tùng service center khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem phụ tùng được gán
- ❌ Không thể xem phụ tùng không được gán
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng suppliers để lấy thông tin
- Sử dụng pagination để giới hạn kết quả

### Caching

- Cache danh sách phụ tùng trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thay đổi phụ tùng

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
- Part object được tối ưu cho danh sách
- Performance metrics được tính toán từ usage records
- Stock information được lấy từ inventory records
- Supplier information được lấy từ supplier records
- Pricing information được lấy từ pricing records
- Filter information được trả về để debug
- Pagination information được trả về để navigation
- Summary information được trả về để overview
