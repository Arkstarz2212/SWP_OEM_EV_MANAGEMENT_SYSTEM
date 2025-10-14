# GET /api/inventory/stock/history

## Mô tả

Lấy lịch sử thay đổi tồn kho của phụ tùng. Endpoint này cho phép xem lịch sử chi tiết về các thay đổi tồn kho bao gồm thời gian, số lượng, và lý do thay đổi.

## Endpoint

```
GET /api/inventory/stock/history
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter  | Type    | Required | Description                           |
| ---------- | ------- | -------- | ------------------------------------- |
| partId     | long    | No       | Filter theo ID phụ tùng               |
| partNumber | string  | No       | Filter theo số phụ tùng               |
| operation  | string  | No       | Filter theo loại thao tác             |
| supplierId | long    | No       | Filter theo ID nhà cung cấp           |
| startDate  | string  | No       | Filter từ ngày (YYYY-MM-DD)           |
| endDate    | string  | No       | Filter đến ngày (YYYY-MM-DD)          |
| userId     | long    | No       | Filter theo ID người thực hiện        |
| limit      | integer | No       | Số lượng kết quả (1-100, mặc định 20) |
| offset     | integer | No       | Vị trí bắt đầu (mặc định 0)           |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "history": [
      {
        "id": 1,
        "partId": 1,
        "partNumber": "TESLA-BMS-001",
        "partName": "Battery Management System Sensor",
        "operation": "ADD",
        "quantity": 50,
        "previousQuantity": 45,
        "newQuantity": 95,
        "reason": "Stock replenishment from supplier",
        "supplierId": 1,
        "supplierName": "Tesla Parts Supplier",
        "purchaseOrder": "PO-2024-001",
        "cost": 150.0,
        "totalCost": 7500.0,
        "stockStatus": "IN_STOCK",
        "reorderTriggered": false,
        "lowStockAlert": false,
        "timestamp": "2024-01-15T10:30:00Z",
        "userId": 3,
        "userName": "scstaff@example.com",
        "notes": "Regular stock replenishment"
      },
      {
        "id": 2,
        "partId": 1,
        "partNumber": "TESLA-BMS-001",
        "partName": "Battery Management System Sensor",
        "operation": "REMOVE",
        "quantity": 10,
        "previousQuantity": 95,
        "newQuantity": 85,
        "reason": "Used for warranty claim",
        "supplierId": null,
        "supplierName": null,
        "purchaseOrder": null,
        "cost": null,
        "totalCost": null,
        "stockStatus": "IN_STOCK",
        "reorderTriggered": false,
        "lowStockAlert": false,
        "timestamp": "2024-01-14T15:45:00Z",
        "userId": 3,
        "userName": "scstaff@example.com",
        "notes": "Part used in warranty repair"
      },
      {
        "id": 3,
        "partId": 1,
        "partNumber": "TESLA-BMS-001",
        "partName": "Battery Management System Sensor",
        "operation": "SET",
        "quantity": 100,
        "previousQuantity": 85,
        "newQuantity": 100,
        "reason": "Stock count correction",
        "supplierId": null,
        "supplierName": null,
        "purchaseOrder": null,
        "cost": null,
        "totalCost": null,
        "stockStatus": "IN_STOCK",
        "reorderTriggered": false,
        "lowStockAlert": false,
        "timestamp": "2024-01-13T09:15:00Z",
        "userId": 2,
        "userName": "evmstaff@example.com",
        "notes": "Physical count correction"
      }
    ],
    "pagination": {
      "total": 150,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalOperations": 150,
      "addOperations": 75,
      "removeOperations": 60,
      "setOperations": 15,
      "totalQuantityAdded": 3750,
      "totalQuantityRemoved": 1800,
      "averageQuantityPerOperation": 25.0
    },
    "filters": {
      "partId": null,
      "partNumber": null,
      "operation": null,
      "supplierId": null,
      "startDate": null,
      "endDate": null,
      "userId": null
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: limit must be between 1 and 100",
  "path": "/api/inventory/stock/history",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Date format không hợp lệ

```json
{
  "error": "Invalid date format. Use YYYY-MM-DD",
  "path": "/api/inventory/stock/history",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve stock history: Internal server error",
  "path": "/api/inventory/stock/history",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem lịch sử tồn kho trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy lịch sử tồn kho mặc định

**Request:**

```
GET /api/inventory/stock/history
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với lịch sử tồn kho

### Test Case 2: Filter theo phụ tùng

**Request:**

```
GET /api/inventory/stock/history?partId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với lịch sử của phụ tùng 1

### Test Case 3: Filter theo operation

**Request:**

```
GET /api/inventory/stock/history?operation=ADD
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ ADD operations

### Test Case 4: Filter theo date range

**Request:**

```
GET /api/inventory/stock/history?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với lịch sử trong tháng 1/2024

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/inventory/stock/history?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 records từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/inventory/stock/history?partId=1&operation=ADD&limit=20" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/inventory/stock/history?partId=1&operation=ADD&limit=20",
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

url = "http://localhost:8080/api/inventory/stock/history"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "partId": 1,
    "operation": "ADD",
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

### Bước 2: Lấy lịch sử tồn kho

```bash
curl -X GET "http://localhost:8080/api/inventory/stock/history?partId=1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "history": [
      {
        "id": 1,
        "partId": 1,
        "partNumber": "TESLA-BMS-001",
        "partName": "Battery Management System Sensor",
        "operation": "ADD",
        "quantity": 50,
        "previousQuantity": 45,
        "newQuantity": 95,
        "reason": "Stock replenishment from supplier",
        "supplierId": 1,
        "supplierName": "Tesla Parts Supplier",
        "purchaseOrder": "PO-2024-001",
        "cost": 150.0,
        "totalCost": 7500.0,
        "stockStatus": "IN_STOCK",
        "reorderTriggered": false,
        "lowStockAlert": false,
        "timestamp": "2024-01-15T10:30:00Z",
        "userId": 3,
        "userName": "scstaff@example.com",
        "notes": "Regular stock replenishment"
      }
    ],
    "pagination": {
      "total": 150,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalOperations": 150,
      "addOperations": 75,
      "removeOperations": 60,
      "setOperations": 15,
      "totalQuantityAdded": 3750,
      "totalQuantityRemoved": 1800,
      "averageQuantityPerOperation": 25.0
    }
  }
}
```

## Response Fields Explanation

### History Object

- `id`: ID của record lịch sử
- `partId`: ID của phụ tùng
- `partNumber`: Số phụ tùng
- `partName`: Tên phụ tùng
- `operation`: Loại thao tác
- `quantity`: Số lượng thay đổi
- `previousQuantity`: Số lượng trước đó
- `newQuantity`: Số lượng mới
- `reason`: Lý do thay đổi
- `supplierId`: ID nhà cung cấp
- `supplierName`: Tên nhà cung cấp
- `purchaseOrder`: Số đơn hàng
- `cost`: Chi phí đơn vị
- `totalCost`: Tổng chi phí
- `stockStatus`: Trạng thái tồn kho
- `reorderTriggered`: Có kích hoạt đặt hàng lại không
- `lowStockAlert`: Có cảnh báo tồn kho thấp không
- `timestamp`: Thời gian thay đổi
- `userId`: ID người thực hiện
- `userName`: Tên người thực hiện
- `notes`: Ghi chú bổ sung

### Pagination Information

- `total`: Tổng số records thỏa mãn filter
- `limit`: Số lượng kết quả hiện tại
- `offset`: Vị trí bắt đầu hiện tại
- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không

### Summary Information

- `totalOperations`: Tổng số thao tác
- `addOperations`: Số thao tác ADD
- `removeOperations`: Số thao tác REMOVE
- `setOperations`: Số thao tác SET
- `totalQuantityAdded`: Tổng số lượng đã thêm
- `totalQuantityRemoved`: Tổng số lượng đã giảm
- `averageQuantityPerOperation`: Số lượng trung bình mỗi thao tác

### Filter Information

- `partId`: ID phụ tùng được filter
- `partNumber`: Số phụ tùng được filter
- `operation`: Loại thao tác được filter
- `supplierId`: ID nhà cung cấp được filter
- `startDate`: Ngày bắt đầu được filter
- `endDate`: Ngày kết thúc được filter
- `userId`: ID người dùng được filter

## Operations

### ADD

- Thêm số lượng vào tồn kho
- Số lượng dương
- Tăng tổng tồn kho

### REMOVE

- Giảm số lượng từ tồn kho
- Số lượng dương
- Giảm tổng tồn kho

### SET

- Đặt số lượng tồn kho về giá trị cụ thể
- Số lượng không âm
- Thay thế tổng tồn kho

## Stock Status

### IN_STOCK

- Tồn kho đủ
- Không cần đặt hàng
- Hoạt động bình thường

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

### Part Filter

- `partId`: Filter theo ID phụ tùng
- `partNumber`: Filter theo số phụ tùng
- Chỉ áp dụng cho phụ tùng có quyền truy cập

### Operation Filter

- `ADD`: Chỉ thao tác thêm
- `REMOVE`: Chỉ thao tác giảm
- `SET`: Chỉ thao tác đặt

### Supplier Filter

- `supplierId`: Filter theo nhà cung cấp
- Chỉ áp dụng cho EVM_Staff và Admin

### Date Filter

- `startDate`: Từ ngày
- `endDate`: Đến ngày
- Format: YYYY-MM-DD

### User Filter

- `userId`: Filter theo người thực hiện
- Chỉ áp dụng cho Admin

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

- ✅ Có thể xem lịch sử tất cả tồn kho
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem lịch sử tồn kho của OEM
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem lịch sử tồn kho OEM khác

### SC_Staff

- ✅ Có thể xem lịch sử tồn kho của service center
- ❌ Không thể xem lịch sử tồn kho service center khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem lịch sử tồn kho được gán
- ❌ Không thể xem lịch sử tồn kho không được gán
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng parts để lấy thông tin
- Sử dụng pagination để giới hạn kết quả

### Caching

- Cache lịch sử tồn kho trong 5 phút
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
- History được sắp xếp theo timestamp giảm dần
- Filter information được trả về để debug
- Pagination information được trả về để navigation
- Summary information được trả về để overview
- Performance được tối ưu thông qua caching
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
