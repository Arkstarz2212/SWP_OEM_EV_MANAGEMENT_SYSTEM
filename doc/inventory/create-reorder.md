# POST /api/inventory/reorder

## Mô tả

Tạo đơn đặt hàng lại cho phụ tùng có tồn kho thấp. Endpoint này cho phép Admin, EVM_Staff và SC_Staff tạo đơn đặt hàng lại và tự động thực hiện các hành động liên quan.

## Endpoint

```
POST /api/inventory/reorder
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
  "supplierId": 1,
  "priority": "HIGH",
  "requestedDeliveryDate": "2024-01-25",
  "notes": "Urgent reorder for warranty claims",
  "autoApprove": false
}
```

### Parameters

| Field                 | Type    | Required | Description                                  |
| --------------------- | ------- | -------- | -------------------------------------------- |
| partId                | long    | Yes      | ID của phụ tùng                              |
| quantity              | integer | Yes      | Số lượng đặt hàng                            |
| supplierId            | long    | Yes      | ID nhà cung cấp                              |
| priority              | string  | No       | Mức độ ưu tiên (LOW, MEDIUM, HIGH, CRITICAL) |
| requestedDeliveryDate | string  | No       | Ngày giao hàng yêu cầu (YYYY-MM-DD)          |
| notes                 | string  | No       | Ghi chú bổ sung                              |
| autoApprove           | boolean | No       | Tự động phê duyệt (mặc định false)           |

## Response

### Success Response (201 Created)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "reorderNumber": "RO-2024-001",
    "partId": 1,
    "partNumber": "TESLA-BMS-001",
    "partName": "Battery Management System Sensor",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "quantity": 50,
    "supplierId": 1,
    "supplierName": "Tesla Parts Supplier",
    "supplierContact": "supplier@tesla.com",
    "supplierPhone": "+1-555-0123",
    "priority": "HIGH",
    "status": "PENDING",
    "requestedDeliveryDate": "2024-01-25",
    "estimatedCost": 7500.0,
    "currency": "USD",
    "currentStock": 5,
    "minQuantity": 10,
    "reorderPoint": 20,
    "notes": "Urgent reorder for warranty claims",
    "autoApprove": false,
    "createdAt": "2024-01-15T00:00:00Z",
    "createdBy": "scstaff@example.com",
    "approvedAt": null,
    "approvedBy": null,
    "deliveredAt": null,
    "deliveredBy": null
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
      "field": "requestedDeliveryDate",
      "message": "Requested delivery date cannot be in the past"
    }
  ],
  "path": "/api/inventory/reorder",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Phụ tùng không tồn tại

```json
{
  "error": "Part not found",
  "path": "/api/inventory/reorder",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Supplier không tồn tại

```json
{
  "error": "Supplier not found",
  "path": "/api/inventory/reorder",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Đã có reorder đang chờ

```json
{
  "error": "Part already has a pending reorder",
  "path": "/api/inventory/reorder",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to create reorder",
  "path": "/api/inventory/reorder",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to create reorder: Internal server error",
  "path": "/api/inventory/reorder",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin, EVM_Staff hoặc SC_Staff role
- **Part:** Phụ tùng phải tồn tại
- **Supplier:** Supplier phải tồn tại và active

## Test Cases

### Test Case 1: Tạo reorder thành công

**Request:**

```json
{
  "partId": 1,
  "quantity": 50,
  "supplierId": 1,
  "priority": "HIGH",
  "requestedDeliveryDate": "2024-01-25",
  "notes": "Urgent reorder for warranty claims",
  "autoApprove": false
}
```

**Expected Response:** 201 Created với thông tin reorder

### Test Case 2: Tạo reorder với auto approve

**Request:**

```json
{
  "partId": 1,
  "quantity": 30,
  "supplierId": 1,
  "priority": "MEDIUM",
  "requestedDeliveryDate": "2024-01-30",
  "notes": "Standard reorder",
  "autoApprove": true
}
```

**Expected Response:** 201 Created với status APPROVED

### Test Case 3: Phụ tùng không tồn tại

**Request:**

```json
{
  "partId": 999,
  "quantity": 50,
  "supplierId": 1,
  "priority": "HIGH"
}
```

**Expected Response:** 400 Bad Request

### Test Case 4: Supplier không tồn tại

**Request:**

```json
{
  "partId": 1,
  "quantity": 50,
  "supplierId": 999,
  "priority": "HIGH"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Đã có reorder đang chờ

**Request:**

```json
{
  "partId": 1,
  "quantity": 50,
  "supplierId": 1,
  "priority": "HIGH"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/inventory/reorder" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "partId": 1,
    "quantity": 50,
    "supplierId": 1,
    "priority": "HIGH",
    "requestedDeliveryDate": "2024-01-25",
    "notes": "Urgent reorder for warranty claims",
    "autoApprove": false
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/inventory/reorder", {
  method: "POST",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    partId: 1,
    quantity: 50,
    supplierId: 1,
    priority: "HIGH",
    requestedDeliveryDate: "2024-01-25",
    notes: "Urgent reorder for warranty claims",
    autoApprove: false,
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/inventory/reorder"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "partId": 1,
    "quantity": 50,
    "supplierId": 1,
    "priority": "HIGH",
    "requestedDeliveryDate": "2024-01-25",
    "notes": "Urgent reorder for warranty claims",
    "autoApprove": False
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

### Bước 2: Tạo reorder

```bash
curl -X POST "http://localhost:8080/api/inventory/reorder" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "partId": 1,
    "quantity": 50,
    "supplierId": 1,
    "priority": "HIGH",
    "requestedDeliveryDate": "2024-01-25",
    "notes": "Urgent reorder for warranty claims",
    "autoApprove": false
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "reorderNumber": "RO-2024-001",
    "partId": 1,
    "partNumber": "TESLA-BMS-001",
    "partName": "Battery Management System Sensor",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "quantity": 50,
    "supplierId": 1,
    "supplierName": "Tesla Parts Supplier",
    "supplierContact": "supplier@tesla.com",
    "supplierPhone": "+1-555-0123",
    "priority": "HIGH",
    "status": "PENDING",
    "requestedDeliveryDate": "2024-01-25",
    "estimatedCost": 7500.0,
    "currency": "USD",
    "currentStock": 5,
    "minQuantity": 10,
    "reorderPoint": 20,
    "notes": "Urgent reorder for warranty claims",
    "autoApprove": false,
    "createdAt": "2024-01-15T00:00:00Z",
    "createdBy": "scstaff@example.com"
  }
}
```

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của reorder
- `reorderNumber`: Số reorder (duy nhất)
- `partId`: ID của phụ tùng
- `partNumber`: Số phụ tùng
- `partName`: Tên phụ tùng
- `category`: Danh mục phụ tùng
- `subcategory`: Danh mục con

### Order Information

- `quantity`: Số lượng đặt hàng
- `supplierId`: ID nhà cung cấp
- `supplierName`: Tên nhà cung cấp
- `supplierContact`: Liên hệ nhà cung cấp
- `supplierPhone`: Số điện thoại nhà cung cấp
- `priority`: Mức độ ưu tiên
- `status`: Trạng thái reorder

### Delivery Information

- `requestedDeliveryDate`: Ngày giao hàng yêu cầu
- `estimatedCost`: Chi phí ước tính
- `currency`: Đơn vị tiền tệ

### Stock Information

- `currentStock`: Tồn kho hiện tại
- `minQuantity`: Số lượng tối thiểu
- `reorderPoint`: Điểm đặt hàng lại

### Additional Information

- `notes`: Ghi chú bổ sung
- `autoApprove`: Tự động phê duyệt

### Audit Information

- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo
- `approvedAt`: Thời gian phê duyệt
- `approvedBy`: Người phê duyệt
- `deliveredAt`: Thời gian giao hàng
- `deliveredBy`: Người giao hàng

## Priorities

### CRITICAL

- Ưu tiên cao nhất
- Cần xử lý ngay lập tức
- Giao hàng trong 1-2 ngày

### HIGH

- Ưu tiên cao
- Cần xử lý sớm
- Giao hàng trong 3-5 ngày

### MEDIUM

- Ưu tiên trung bình
- Cần xử lý trong thời gian hợp lý
- Giao hàng trong 1-2 tuần

### LOW

- Ưu tiên thấp
- Cần xử lý khi có thời gian
- Giao hàng trong 2-4 tuần

## Statuses

### PENDING

- Chờ phê duyệt
- Chưa được xử lý
- Cần approval

### APPROVED

- Đã được phê duyệt
- Đang xử lý
- Chờ giao hàng

### ORDERED

- Đã đặt hàng
- Đang chờ supplier
- Chờ giao hàng

### DELIVERED

- Đã giao hàng
- Hoàn thành
- Cập nhật tồn kho

### CANCELLED

- Đã hủy
- Không xử lý
- Không cập nhật tồn kho

## Validation Rules

### Part ID

- Phải tồn tại trong hệ thống
- Phải active
- Phải có quyền truy cập

### Quantity

- Phải là số dương
- Phải hợp lý với part
- Không được vượt quá giới hạn

### Supplier

- Phải tồn tại trong hệ thống
- Phải active
- Phải có quyền truy cập

### Priority

- Phải là priority hợp lệ
- Chỉ được chọn từ danh sách có sẵn
- Không được trống

### Requested Delivery Date

- Phải có format YYYY-MM-DD
- Không được trong quá khứ
- Phải hợp lý với priority

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

### Auto Approve

- Phải là boolean
- Mặc định là false
- Chỉ Admin và EVM_Staff có thể sử dụng

## Permission Matrix

### Admin

- ✅ Có thể tạo reorder cho tất cả phụ tùng
- ✅ Có thể sử dụng autoApprove
- ✅ Có thể tạo reorder với bất kỳ supplier nào

### EVM_Staff

- ✅ Có thể tạo reorder cho phụ tùng trong OEM
- ✅ Có thể sử dụng autoApprove
- ❌ Không thể tạo reorder cho phụ tùng OEM khác

### SC_Staff

- ✅ Có thể tạo reorder cho phụ tùng trong service center
- ❌ Không thể sử dụng autoApprove
- ❌ Không thể tạo reorder cho phụ tùng service center khác

### SC_Technician

- ❌ Không có quyền tạo reorder
- ❌ Chỉ có thể xem thông tin reorder

## Automatic Actions

### Reorder Creation

- Tạo reorder record trong database
- Gán reorder number duy nhất
- Tạo audit log

### Auto Approval

- Tự động phê duyệt nếu autoApprove = true
- Cập nhật status thành APPROVED
- Tạo approval record

### Supplier Notification

- Gửi email đến supplier về reorder
- Tạo supplier task
- Cập nhật supplier dashboard

### Inventory Update

- Cập nhật reorder status
- Tạo inventory record
- Cập nhật stock alerts

### Notification

- Gửi email đến EVM_Staff về reorder
- Gửi thông báo đến Admin
- Tạo task cho approval

## Manual Actions Required

### Supplier Processing

- Supplier cần xác nhận reorder
- Cập nhật thông tin delivery
- Thiết lập pricing

### Approval Process

- EVM_Staff cần phê duyệt reorder
- Cập nhật thông tin delivery
- Thiết lập timeline

## Notes

- Endpoint này dành cho Admin, EVM_Staff và SC_Staff
- SC_Technician không có quyền sử dụng
- Part phải tồn tại và active
- Supplier phải tồn tại và active
- Priority là optional, mặc định MEDIUM
- Requested delivery date là optional
- Notes là optional
- Auto approve chỉ dành cho Admin và EVM_Staff
- Reorder number được tạo tự động
- Status được set thành PENDING ban đầu
- Estimated cost được tính từ pricing
- Current stock được lấy từ inventory
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Supplier notification được gửi về reorder
- EVM_Staff notification được gửi về approval
- Admin notification được gửi về reorder
- Task được tạo cho approval process
- Email được gửi đến supplier
- SMS được gửi nếu cần thiết
- Push notification được gửi đến mobile app
- Webhook được gửi đến external systems
