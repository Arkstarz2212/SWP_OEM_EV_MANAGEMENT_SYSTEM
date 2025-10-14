# PUT /api/inventory/stock

## Mô tả

Cập nhật số lượng tồn kho của phụ tùng. Endpoint này cho phép Admin, EVM_Staff và SC_Staff cập nhật số lượng tồn kho và tự động thực hiện các hành động liên quan.

## Endpoint

```
PUT /api/inventory/stock
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
  "partId": 1,
  "quantity": 50,
  "operation": "ADD",
  "reason": "Stock replenishment from supplier",
  "supplierId": 1,
  "purchaseOrder": "PO-2024-001",
  "cost": 150.0,
  "notes": "Regular stock replenishment"
}
```

### Parameters

| Field         | Type    | Required | Description                      |
| ------------- | ------- | -------- | -------------------------------- |
| partId        | long    | Yes      | ID của phụ tùng                  |
| quantity      | integer | Yes      | Số lượng cập nhật                |
| operation     | string  | Yes      | Loại thao tác (ADD, REMOVE, SET) |
| reason        | string  | No       | Lý do cập nhật                   |
| supplierId    | long    | No       | ID nhà cung cấp                  |
| purchaseOrder | string  | No       | Số đơn hàng                      |
| cost          | number  | No       | Chi phí                          |
| notes         | string  | No       | Ghi chú bổ sung                  |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
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
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com",
    "notes": "Regular stock replenishment"
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
      "field": "quantity",
      "message": "Quantity must be a positive number"
    },
    {
      "field": "operation",
      "message": "Invalid operation. Valid operations are: ADD, REMOVE, SET"
    }
  ],
  "path": "/api/inventory/stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Phụ tùng không tồn tại

```json
{
  "error": "Part not found",
  "path": "/api/inventory/stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Số lượng không hợp lệ

```json
{
  "error": "Invalid quantity: Cannot remove more than available stock",
  "path": "/api/inventory/stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Supplier không tồn tại

```json
{
  "error": "Supplier not found",
  "path": "/api/inventory/stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update stock",
  "path": "/api/inventory/stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update stock: Internal server error",
  "path": "/api/inventory/stock",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin, EVM_Staff hoặc SC_Staff role
- **Part:** Phụ tùng phải tồn tại
- **Supplier:** Supplier phải tồn tại (nếu được cung cấp)

## Test Cases

### Test Case 1: Thêm tồn kho thành công

**Request:**

```json
{
  "partId": 1,
  "quantity": 50,
  "operation": "ADD",
  "reason": "Stock replenishment from supplier",
  "supplierId": 1,
  "purchaseOrder": "PO-2024-001",
  "cost": 150.0,
  "notes": "Regular stock replenishment"
}
```

**Expected Response:** 200 OK với thông tin tồn kho đã cập nhật

### Test Case 2: Giảm tồn kho

**Request:**

```json
{
  "partId": 1,
  "quantity": 10,
  "operation": "REMOVE",
  "reason": "Used for warranty claim",
  "notes": "Part used in warranty repair"
}
```

**Expected Response:** 200 OK với thông tin tồn kho đã cập nhật

### Test Case 3: Set tồn kho

**Request:**

```json
{
  "partId": 1,
  "quantity": 100,
  "operation": "SET",
  "reason": "Stock count correction",
  "notes": "Physical count correction"
}
```

**Expected Response:** 200 OK với thông tin tồn kho đã cập nhật

### Test Case 4: Phụ tùng không tồn tại

**Request:**

```json
{
  "partId": 999,
  "quantity": 50,
  "operation": "ADD",
  "reason": "Test stock update"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Số lượng không hợp lệ

**Request:**

```json
{
  "partId": 1,
  "quantity": -10,
  "operation": "ADD",
  "reason": "Invalid quantity test"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/inventory/stock" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "partId": 1,
    "quantity": 50,
    "operation": "ADD",
    "reason": "Stock replenishment from supplier",
    "supplierId": 1,
    "purchaseOrder": "PO-2024-001",
    "cost": 150.0,
    "notes": "Regular stock replenishment"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/inventory/stock", {
  method: "PUT",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    partId: 1,
    quantity: 50,
    operation: "ADD",
    reason: "Stock replenishment from supplier",
    supplierId: 1,
    purchaseOrder: "PO-2024-001",
    cost: 150.0,
    notes: "Regular stock replenishment",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/inventory/stock"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "partId": 1,
    "quantity": 50,
    "operation": "ADD",
    "reason": "Stock replenishment from supplier",
    "supplierId": 1,
    "purchaseOrder": "PO-2024-001",
    "cost": 150.0,
    "notes": "Regular stock replenishment"
}

response = requests.put(url, headers=headers, json=data)
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

### Bước 2: Cập nhật tồn kho

```bash
curl -X PUT "http://localhost:8080/api/inventory/stock" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "partId": 1,
    "quantity": 50,
    "operation": "ADD",
    "reason": "Stock replenishment from supplier",
    "supplierId": 1,
    "purchaseOrder": "PO-2024-001",
    "cost": 150.0,
    "notes": "Regular stock replenishment"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
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
    "updatedAt": "2024-01-15T00:00:00Z",
    "updatedBy": "scstaff@example.com",
    "notes": "Regular stock replenishment"
  }
}
```

## Response Fields Explanation

### Basic Information

- `partId`: ID của phụ tùng
- `partNumber`: Số phụ tùng
- `partName`: Tên phụ tùng

### Operation Information

- `operation`: Loại thao tác
- `quantity`: Số lượng cập nhật
- `previousQuantity`: Số lượng trước đó
- `newQuantity`: Số lượng mới

### Reason Information

- `reason`: Lý do cập nhật
- `supplierId`: ID nhà cung cấp
- `supplierName`: Tên nhà cung cấp
- `purchaseOrder`: Số đơn hàng

### Cost Information

- `cost`: Chi phí đơn vị
- `totalCost`: Tổng chi phí

### Status Information

- `stockStatus`: Trạng thái tồn kho
- `reorderTriggered`: Có kích hoạt đặt hàng lại không
- `lowStockAlert`: Có cảnh báo tồn kho thấp không

### Audit Information

- `updatedAt`: Thời gian cập nhật
- `updatedBy`: Người cập nhật
- `notes`: Ghi chú bổ sung

## Operations

### ADD

- Thêm số lượng vào tồn kho
- Số lượng phải dương
- Tăng tổng tồn kho

### REMOVE

- Giảm số lượng từ tồn kho
- Số lượng phải dương
- Không được vượt quá tồn kho hiện tại
- Giảm tổng tồn kho

### SET

- Đặt số lượng tồn kho về giá trị cụ thể
- Số lượng phải không âm
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

## Validation Rules

### Part ID

- Phải tồn tại trong hệ thống
- Phải active
- Phải có quyền truy cập

### Quantity

- Phải là số dương
- Phải hợp lệ với operation
- Không được vượt quá giới hạn

### Operation

- Phải là operation hợp lệ
- Phải phù hợp với quantity
- Không được trống

### Reason

- Tối đa 200 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Supplier

- Phải tồn tại trong hệ thống nếu được cung cấp
- Phải active
- Phải có quyền truy cập

### Purchase Order

- Tối đa 50 ký tự
- Phải có format hợp lệ
- Không được trống nếu được cung cấp

### Cost

- Phải là số dương nếu được cung cấp
- Phải hợp lệ với currency
- Không được vượt quá giới hạn

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

## Permission Matrix

### Admin

- ✅ Có thể cập nhật tồn kho của tất cả phụ tùng
- ✅ Có thể sử dụng bất kỳ operation nào
- ✅ Có thể cập nhật với bất kỳ supplier nào

### EVM_Staff

- ✅ Có thể cập nhật tồn kho của phụ tùng trong OEM
- ✅ Có thể sử dụng bất kỳ operation nào
- ❌ Không thể cập nhật phụ tùng OEM khác

### SC_Staff

- ✅ Có thể cập nhật tồn kho của phụ tùng trong service center
- ✅ Có thể sử dụng bất kỳ operation nào
- ❌ Không thể cập nhật phụ tùng service center khác

### SC_Technician

- ❌ Không có quyền cập nhật tồn kho
- ❌ Chỉ có thể xem thông tin tồn kho

## Automatic Actions

### Stock Update

- Cập nhật số lượng trong database
- Cập nhật updatedAt timestamp
- Tạo audit log

### Reorder Check

- Kiểm tra reorder point
- Kích hoạt reorder nếu cần
- Tạo reorder record

### Alert Check

- Kiểm tra low stock alert
- Tạo alert nếu cần
- Gửi notification

### Cost Update

- Cập nhật cost nếu được cung cấp
- Tính toán total cost
- Cập nhật pricing history

### Notification

- Gửi email đến supplier về stock update
- Gửi thông báo đến EVM_Staff
- Tạo task cho inventory management

## Manual Actions Required

### Supplier Notification

- Supplier cần xác nhận stock update
- Cập nhật thông tin part
- Thiết lập pricing mới

### Inventory Management

- Cập nhật inventory system
- Thiết lập reorder points mới
- Cấu hình stock levels

## Notes

- Endpoint này dành cho Admin, EVM_Staff và SC_Staff
- SC_Technician không có quyền sử dụng
- Operation phải hợp lệ với quantity
- Supplier phải tồn tại và active
- Cost information là optional
- Purchase order là optional
- Notes là optional
- Stock status được cập nhật tự động
- Reorder được kích hoạt tự động
- Low stock alert được tạo tự động
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Supplier notification được gửi về stock update
- Inventory notification được gửi về stock change
- EVM_Staff notification được gửi về stock update
- Task được tạo cho inventory management
- Email được gửi đến supplier
- SMS được gửi nếu cần thiết
- Push notification được gửi đến mobile app
- Webhook được gửi đến external systems
