# PUT /api/parts/{id}

## Mô tả

Cập nhật thông tin phụ tùng theo ID. Endpoint này cho phép Admin và EVM_Staff cập nhật thông tin phụ tùng bao gồm thông số kỹ thuật, giá cả, và thông tin nhà cung cấp.

## Endpoint

```
PUT /api/parts/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter | Type | Required | Description                  |
| --------- | ---- | -------- | ---------------------------- |
| id        | long | Yes      | ID của phụ tùng cần cập nhật |

### Body

```json
{
  "name": "Battery Management System Sensor Pro",
  "description": "Enhanced high-precision sensor for battery management system",
  "specifications": {
    "voltage": "12V",
    "current": "5A",
    "temperature": "-40°C to +85°C",
    "dimensions": "50x30x20mm",
    "weight": "0.1kg",
    "accuracy": "±0.1%"
  },
  "pricing": {
    "cost": 160.0,
    "retailPrice": 220.0,
    "warrantyPrice": 200.0,
    "currency": "USD"
  },
  "supplier": {
    "name": "Tesla Parts Supplier Pro",
    "contact": "supplier.pro@tesla.com",
    "phone": "+1-555-0124",
    "address": "456 Enhanced Street, CA 90210"
  },
  "warranty": {
    "period": 36,
    "unit": "MONTHS",
    "conditions": "Enhanced warranty terms apply"
  },
  "stock": {
    "minQuantity": 15,
    "maxQuantity": 120,
    "reorderPoint": 25
  },
  "notes": "Updated with enhanced specifications"
}
```

### Parameters

| Field          | Type   | Required | Description            |
| -------------- | ------ | -------- | ---------------------- |
| name           | string | No       | Tên phụ tùng           |
| description    | string | No       | Mô tả phụ tùng         |
| specifications | object | No       | Thông số kỹ thuật      |
| pricing        | object | No       | Thông tin giá cả       |
| supplier       | object | No       | Thông tin nhà cung cấp |
| warranty       | object | No       | Thông tin bảo hành     |
| stock          | object | No       | Thông tin tồn kho      |
| notes          | string | No       | Ghi chú bổ sung        |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "id": 1,
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor Pro",
    "description": "Enhanced high-precision sensor for battery management system",
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
      "weight": "0.1kg",
      "accuracy": "±0.1%"
    },
    "pricing": {
      "cost": 160.0,
      "retailPrice": 220.0,
      "warrantyPrice": 200.0,
      "currency": "USD"
    },
    "supplier": {
      "name": "Tesla Parts Supplier Pro",
      "contact": "supplier.pro@tesla.com",
      "phone": "+1-555-0124",
      "address": "456 Enhanced Street, CA 90210"
    },
    "warranty": {
      "period": 36,
      "unit": "MONTHS",
      "conditions": "Enhanced warranty terms apply"
    },
    "stock": {
      "minQuantity": 15,
      "maxQuantity": 120,
      "reorderPoint": 25,
      "currentQuantity": 45
    },
    "status": "ACTIVE",
    "lastUpdated": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com",
    "notes": "Updated with enhanced specifications"
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
      "field": "pricing.cost",
      "message": "Cost must be a positive number"
    },
    {
      "field": "supplier.contact",
      "message": "Invalid email format"
    }
  ],
  "path": "/api/parts/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Phụ tùng không tồn tại

```json
{
  "error": "Part not found",
  "path": "/api/parts/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Supplier không tồn tại

```json
{
  "error": "Supplier not found",
  "path": "/api/parts/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to update part",
  "path": "/api/parts/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update part: Internal server error",
  "path": "/api/parts/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **Part:** Phụ tùng phải tồn tại
- **Supplier:** Supplier phải tồn tại (nếu được cung cấp)

## Test Cases

### Test Case 1: Cập nhật thông tin cơ bản

**Request:**

```json
{
  "name": "Battery Management System Sensor Pro",
  "description": "Enhanced high-precision sensor for battery management system",
  "notes": "Updated with enhanced specifications"
}
```

**Expected Response:** 200 OK với thông tin đã cập nhật

### Test Case 2: Cập nhật thông số kỹ thuật

**Request:**

```json
{
  "specifications": {
    "voltage": "12V",
    "current": "5A",
    "temperature": "-40°C to +85°C",
    "dimensions": "50x30x20mm",
    "weight": "0.1kg",
    "accuracy": "±0.1%"
  }
}
```

**Expected Response:** 200 OK với thông số đã cập nhật

### Test Case 3: Cập nhật giá cả

**Request:**

```json
{
  "pricing": {
    "cost": 160.0,
    "retailPrice": 220.0,
    "warrantyPrice": 200.0,
    "currency": "USD"
  }
}
```

**Expected Response:** 200 OK với giá cả đã cập nhật

### Test Case 4: Phụ tùng không tồn tại

**Request:**

```json
{
  "name": "Updated Part Name"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Supplier không tồn tại

**Request:**

```json
{
  "supplier": {
    "name": "Non-existent Supplier",
    "contact": "test@nonexistent.com",
    "phone": "+1-555-0999",
    "address": "999 Fake Street, CA 90210"
  }
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/parts/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Battery Management System Sensor Pro",
    "description": "Enhanced high-precision sensor for battery management system",
    "specifications": {
      "voltage": "12V",
      "current": "5A",
      "temperature": "-40°C to +85°C",
      "dimensions": "50x30x20mm",
      "weight": "0.1kg",
      "accuracy": "±0.1%"
    },
    "pricing": {
      "cost": 160.0,
      "retailPrice": 220.0,
      "warrantyPrice": 200.0,
      "currency": "USD"
    },
    "supplier": {
      "name": "Tesla Parts Supplier Pro",
      "contact": "supplier.pro@tesla.com",
      "phone": "+1-555-0124",
      "address": "456 Enhanced Street, CA 90210"
    },
    "warranty": {
      "period": 36,
      "unit": "MONTHS",
      "conditions": "Enhanced warranty terms apply"
    },
    "stock": {
      "minQuantity": 15,
      "maxQuantity": 120,
      "reorderPoint": 25
    },
    "notes": "Updated with enhanced specifications"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/parts/1", {
  method: "PUT",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    name: "Battery Management System Sensor Pro",
    description: "Enhanced high-precision sensor for battery management system",
    specifications: {
      voltage: "12V",
      current: "5A",
      temperature: "-40°C to +85°C",
      dimensions: "50x30x20mm",
      weight: "0.1kg",
      accuracy: "±0.1%",
    },
    pricing: {
      cost: 160.0,
      retailPrice: 220.0,
      warrantyPrice: 200.0,
      currency: "USD",
    },
    supplier: {
      name: "Tesla Parts Supplier Pro",
      contact: "supplier.pro@tesla.com",
      phone: "+1-555-0124",
      address: "456 Enhanced Street, CA 90210",
    },
    warranty: {
      period: 36,
      unit: "MONTHS",
      conditions: "Enhanced warranty terms apply",
    },
    stock: {
      minQuantity: 15,
      maxQuantity: 120,
      reorderPoint: 25,
    },
    notes: "Updated with enhanced specifications",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/parts/1"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "name": "Battery Management System Sensor Pro",
    "description": "Enhanced high-precision sensor for battery management system",
    "specifications": {
        "voltage": "12V",
        "current": "5A",
        "temperature": "-40°C to +85°C",
        "dimensions": "50x30x20mm",
        "weight": "0.1kg",
        "accuracy": "±0.1%"
    },
    "pricing": {
        "cost": 160.0,
        "retailPrice": 220.0,
        "warrantyPrice": 200.0,
        "currency": "USD"
    },
    "supplier": {
        "name": "Tesla Parts Supplier Pro",
        "contact": "supplier.pro@tesla.com",
        "phone": "+1-555-0124",
        "address": "456 Enhanced Street, CA 90210"
    },
    "warranty": {
        "period": 36,
        "unit": "MONTHS",
        "conditions": "Enhanced warranty terms apply"
    },
    "stock": {
        "minQuantity": 15,
        "maxQuantity": 120,
        "reorderPoint": 25
    },
    "notes": "Updated with enhanced specifications"
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

### Bước 2: Cập nhật thông tin phụ tùng

```bash
curl -X PUT "http://localhost:8080/api/parts/1" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Battery Management System Sensor Pro",
    "description": "Enhanced high-precision sensor for battery management system",
    "specifications": {
      "voltage": "12V",
      "current": "5A",
      "temperature": "-40°C to +85°C",
      "dimensions": "50x30x20mm",
      "weight": "0.1kg",
      "accuracy": "±0.1%"
    },
    "pricing": {
      "cost": 160.0,
      "retailPrice": 220.0,
      "warrantyPrice": 200.0,
      "currency": "USD"
    },
    "supplier": {
      "name": "Tesla Parts Supplier Pro",
      "contact": "supplier.pro@tesla.com",
      "phone": "+1-555-0124",
      "address": "456 Enhanced Street, CA 90210"
    },
    "warranty": {
      "period": 36,
      "unit": "MONTHS",
      "conditions": "Enhanced warranty terms apply"
    },
    "stock": {
      "minQuantity": 15,
      "maxQuantity": 120,
      "reorderPoint": 25
    },
    "notes": "Updated with enhanced specifications"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor Pro",
    "description": "Enhanced high-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
    "oemName": "Tesla Motors",
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "specifications": {
      "voltage": "12V",
      "current": "5A",
      "temperature": "-40°C to +85°C",
      "dimensions": "50x30x20mm",
      "weight": "0.1kg",
      "accuracy": "±0.1%"
    },
    "pricing": {
      "cost": 160.0,
      "retailPrice": 220.0,
      "warrantyPrice": 200.0,
      "currency": "USD"
    },
    "supplier": {
      "name": "Tesla Parts Supplier Pro",
      "contact": "supplier.pro@tesla.com",
      "phone": "+1-555-0124",
      "address": "456 Enhanced Street, CA 90210"
    },
    "warranty": {
      "period": 36,
      "unit": "MONTHS",
      "conditions": "Enhanced warranty terms apply"
    },
    "stock": {
      "minQuantity": 15,
      "maxQuantity": 120,
      "reorderPoint": 25,
      "currentQuantity": 45
    },
    "status": "ACTIVE",
    "lastUpdated": "2024-01-15T00:00:00Z",
    "updatedBy": "evmstaff@example.com",
    "notes": "Updated with enhanced specifications"
  }
}
```

## Response Fields Explanation

### Basic Part Information

- `id`: ID duy nhất của phụ tùng
- `partNumber`: Số phụ tùng (duy nhất)
- `name`: Tên phụ tùng (đã cập nhật)
- `description`: Mô tả phụ tùng (đã cập nhật)
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

- `specifications`: Thông số kỹ thuật (đã cập nhật)
  - `voltage`: Điện áp
  - `current`: Dòng điện
  - `temperature`: Nhiệt độ hoạt động
  - `dimensions`: Kích thước
  - `weight`: Trọng lượng
  - `accuracy`: Độ chính xác

### Pricing Information

- `pricing`: Thông tin giá cả (đã cập nhật)
  - `cost`: Giá gốc
  - `retailPrice`: Giá bán lẻ
  - `warrantyPrice`: Giá bảo hành
  - `currency`: Đơn vị tiền tệ

### Supplier Information

- `supplier`: Thông tin nhà cung cấp (đã cập nhật)
  - `name`: Tên nhà cung cấp
  - `contact`: Liên hệ
  - `phone`: Số điện thoại
  - `address`: Địa chỉ

### Warranty Information

- `warranty`: Thông tin bảo hành (đã cập nhật)
  - `period`: Thời gian bảo hành
  - `unit`: Đơn vị thời gian
  - `conditions`: Điều kiện bảo hành

### Stock Information

- `stock`: Thông tin tồn kho (đã cập nhật)
  - `minQuantity`: Số lượng tối thiểu
  - `maxQuantity`: Số lượng tối đa
  - `reorderPoint`: Điểm đặt hàng lại
  - `currentQuantity`: Số lượng hiện tại

### Status Information

- `status`: Trạng thái phụ tùng
- `lastUpdated`: Thời gian cập nhật
- `updatedBy`: Người cập nhật
- `notes`: Ghi chú bổ sung (đã cập nhật)

## Updatable Fields

### Basic Information

- `name`: Tên phụ tùng
- `description`: Mô tả phụ tùng

### Technical Information

- `specifications`: Thông số kỹ thuật
  - `voltage`: Điện áp
  - `current`: Dòng điện
  - `temperature`: Nhiệt độ hoạt động
  - `dimensions`: Kích thước
  - `weight`: Trọng lượng
  - `accuracy`: Độ chính xác

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

### Additional Information

- `notes`: Ghi chú bổ sung

## Validation Rules

### Name

- Không được trống nếu được cung cấp
- Tối đa 100 ký tự
- Phải mô tả rõ ràng

### Description

- Không được trống nếu được cung cấp
- Tối đa 500 ký tự
- Phải mô tả chi tiết

### Specifications

- Tất cả field phải hợp lệ nếu được cung cấp
- Voltage phải là format hợp lệ
- Current phải là format hợp lệ
- Temperature phải là format hợp lệ
- Dimensions phải là format hợp lệ
- Weight phải là format hợp lệ
- Accuracy phải là format hợp lệ

### Pricing

- `cost`: Phải là số dương nếu được cung cấp
- `retailPrice`: Phải là số dương nếu được cung cấp
- `warrantyPrice`: Phải là số dương nếu được cung cấp
- `currency`: Phải là currency hợp lệ nếu được cung cấp

### Supplier

- `name`: Không được trống nếu được cung cấp, tối đa 100 ký tự
- `contact`: Phải có format email hợp lệ nếu được cung cấp
- `phone`: Phải có format số điện thoại hợp lệ nếu được cung cấp
- `address`: Không được trống nếu được cung cấp, tối đa 200 ký tự

### Warranty

- `period`: Phải là số dương nếu được cung cấp
- `unit`: Phải là unit hợp lệ nếu được cung cấp
- `conditions`: Tối đa 200 ký tự nếu được cung cấp

### Stock

- `minQuantity`: Phải là số không âm nếu được cung cấp
- `maxQuantity`: Phải là số dương nếu được cung cấp
- `reorderPoint`: Phải là số không âm nếu được cung cấp

### Notes

- Tối đa 500 ký tự
- Có thể chứa HTML tags
- Không được trống nếu được cung cấp

## Permission Matrix

### Admin

- ✅ Có thể cập nhật tất cả phụ tùng
- ✅ Có thể cập nhật với bất kỳ supplier nào
- ✅ Có thể cập nhật tất cả thông tin

### EVM_Staff

- ✅ Có thể cập nhật phụ tùng của OEM
- ✅ Có thể cập nhật với supplier trong OEM
- ❌ Không thể cập nhật phụ tùng OEM khác

### SC_Staff

- ❌ Không có quyền cập nhật phụ tùng
- ❌ Chỉ có thể xem thông tin phụ tùng

### SC_Technician

- ❌ Không có quyền cập nhật phụ tùng
- ❌ Chỉ có thể xem thông tin phụ tùng

## Automatic Actions

### Part Update

- Cập nhật thông tin trong database
- Cập nhật lastUpdated timestamp
- Tạo audit log

### Supplier Change

- Cập nhật supplier nếu được cung cấp
- Cập nhật supplier relationship
- Tạo supplier record

### Pricing Update

- Cập nhật pricing nếu được cung cấp
- Cập nhật pricing history
- Tạo pricing record

### Stock Update

- Cập nhật stock settings nếu được cung cấp
- Cập nhật stock thresholds
- Tạo stock record

### Notification

- Gửi email đến supplier về thay đổi
- Gửi thông báo đến EVM_Staff
- Tạo task cho inventory management

## Manual Actions Required

### Supplier Notification

- Supplier cần xác nhận thay đổi
- Cập nhật thông tin part
- Thiết lập pricing mới

### Inventory Update

- Cập nhật inventory system
- Thiết lập stock levels mới
- Cấu hình reorder points

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Chỉ cập nhật các field được cung cấp
- Các field không được cung cấp sẽ giữ nguyên
- Supplier phải tồn tại và active
- Pricing information phải hợp lệ
- Warranty information là optional
- Stock information là optional
- Specifications là optional
- Notes là optional
- Part được cập nhật với status ACTIVE
- Stock settings được cập nhật
- Supplier relationship được cập nhật
- Pricing history được tạo
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Supplier notification được gửi về thay đổi
- Inventory notification được gửi về stock update
- EVM_Staff notification được gửi về part update
- Task được tạo cho inventory management
- Email được gửi đến supplier
- SMS được gửi nếu cần thiết
- Push notification được gửi đến mobile app
- Webhook được gửi đến external systems
