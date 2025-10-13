# POST /api/parts

## Mô tả

Tạo phụ tùng mới trong hệ thống AOEM. Endpoint này cho phép Admin và EVM_Staff tạo phụ tùng với thông tin chi tiết bao gồm thông số kỹ thuật, giá cả, và thông tin nhà cung cấp.

## Endpoint

```
POST /api/parts
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
  "partNumber": "TESLA-BMS-001",
  "name": "Battery Management System Sensor",
  "description": "High-precision sensor for battery management system",
  "category": "ELECTRICAL",
  "subcategory": "BATTERY_SYSTEM",
  "oemId": 1,
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
    "reorderPoint": 20
  },
  "notes": "Critical component for battery safety"
}
```

### Parameters

| Field              | Type    | Required | Description              |
| ------------------ | ------- | -------- | ------------------------ |
| partNumber         | string  | Yes      | Số phụ tùng (duy nhất)   |
| name               | string  | Yes      | Tên phụ tùng             |
| description        | string  | Yes      | Mô tả phụ tùng           |
| category           | string  | Yes      | Danh mục phụ tùng        |
| subcategory        | string  | Yes      | Danh mục con             |
| oemId              | long    | Yes      | ID của OEM               |
| make               | string  | Yes      | Hãng xe tương thích      |
| model              | string  | Yes      | Model xe tương thích     |
| year               | integer | Yes      | Năm sản xuất tương thích |
| compatibleVehicles | array   | No       | Danh sách xe tương thích |
| specifications     | object  | No       | Thông số kỹ thuật        |
| pricing            | object  | Yes      | Thông tin giá cả         |
| supplier           | object  | Yes      | Thông tin nhà cung cấp   |
| warranty           | object  | No       | Thông tin bảo hành       |
| stock              | object  | No       | Thông tin tồn kho        |
| notes              | string  | No       | Ghi chú bổ sung          |

## Response

### Success Response (201 Created)

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
      "currentQuantity": 0
    },
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "admin@example.com"
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
      "field": "partNumber",
      "message": "Part number is required"
    },
    {
      "field": "pricing.cost",
      "message": "Cost must be a positive number"
    }
  ],
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Part number đã tồn tại

```json
{
  "error": "Part number already exists",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - OEM không tồn tại

```json
{
  "error": "OEM not found",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 403 Forbidden - Không có quyền

```json
{
  "error": "Insufficient permissions to create part",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to create part: Internal server error",
  "path": "/api/parts",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Admin hoặc EVM_Staff role
- **OEM:** OEM phải tồn tại trong hệ thống
- **Part Number:** Part number phải duy nhất trong hệ thống

## Test Cases

### Test Case 1: Tạo phụ tùng thành công

**Request:**

```json
{
  "partNumber": "TESLA-BMS-001",
  "name": "Battery Management System Sensor",
  "description": "High-precision sensor for battery management system",
  "category": "ELECTRICAL",
  "subcategory": "BATTERY_SYSTEM",
  "oemId": 1,
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
  }
}
```

**Expected Response:** 201 Created với phụ tùng mới

### Test Case 2: Tạo phụ tùng với thông tin đầy đủ

**Request:**

```json
{
  "partNumber": "TESLA-BMS-002",
  "name": "Battery Management System Controller",
  "description": "Advanced controller for battery management system",
  "category": "ELECTRICAL",
  "subcategory": "BATTERY_SYSTEM",
  "oemId": 1,
  "make": "Tesla",
  "model": "Model S",
  "year": 2023,
  "compatibleVehicles": ["Model S", "Model X"],
  "specifications": {
    "voltage": "24V",
    "current": "10A",
    "temperature": "-40°C to +85°C",
    "dimensions": "100x60x40mm",
    "weight": "0.5kg"
  },
  "pricing": {
    "cost": 300.0,
    "retailPrice": 400.0,
    "warrantyPrice": 360.0,
    "currency": "USD"
  },
  "supplier": {
    "name": "Tesla Parts Supplier",
    "contact": "supplier@tesla.com",
    "phone": "+1-555-0123",
    "address": "123 Supplier Street, CA 90210"
  },
  "warranty": {
    "period": 36,
    "unit": "MONTHS",
    "conditions": "Extended warranty terms apply"
  },
  "stock": {
    "minQuantity": 5,
    "maxQuantity": 50,
    "reorderPoint": 10
  }
}
```

**Expected Response:** 201 Created với phụ tùng và thông tin đầy đủ

### Test Case 3: Part number đã tồn tại

**Request:**

```json
{
  "partNumber": "TESLA-BMS-001",
  "name": "Duplicate Part",
  "description": "This part number already exists",
  "category": "ELECTRICAL",
  "oemId": 1,
  "make": "Tesla",
  "model": "Model 3",
  "year": 2023,
  "pricing": {
    "cost": 100.0,
    "retailPrice": 150.0,
    "warrantyPrice": 135.0,
    "currency": "USD"
  },
  "supplier": {
    "name": "Test Supplier",
    "contact": "test@supplier.com",
    "phone": "+1-555-0999",
    "address": "999 Test Street, CA 90210"
  }
}
```

**Expected Response:** 400 Bad Request

### Test Case 4: OEM không tồn tại

**Request:**

```json
{
  "partNumber": "TEST-PART-001",
  "name": "Test Part",
  "description": "Test part for non-existent OEM",
  "category": "ELECTRICAL",
  "oemId": 999,
  "make": "Test",
  "model": "Test Model",
  "year": 2023,
  "pricing": {
    "cost": 100.0,
    "retailPrice": 150.0,
    "warrantyPrice": 135.0,
    "currency": "USD"
  },
  "supplier": {
    "name": "Test Supplier",
    "contact": "test@supplier.com",
    "phone": "+1-555-0999",
    "address": "999 Test Street, CA 90210"
  }
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Validation error - thiếu thông tin bắt buộc

**Request:**

```json
{
  "name": "Incomplete Part",
  "description": "Part missing required fields",
  "category": "ELECTRICAL",
  "oemId": 1,
  "make": "Tesla",
  "model": "Model 3",
  "year": 2023
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST "http://localhost:8080/api/parts" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
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
    }
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/parts", {
  method: "POST",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    partNumber: "TESLA-BMS-001",
    name: "Battery Management System Sensor",
    description: "High-precision sensor for battery management system",
    category: "ELECTRICAL",
    subcategory: "BATTERY_SYSTEM",
    oemId: 1,
    make: "Tesla",
    model: "Model 3",
    year: 2023,
    pricing: {
      cost: 150.0,
      retailPrice: 200.0,
      warrantyPrice: 180.0,
      currency: "USD",
    },
    supplier: {
      name: "Tesla Parts Supplier",
      contact: "supplier@tesla.com",
      phone: "+1-555-0123",
      address: "123 Supplier Street, CA 90210",
    },
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/parts"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789",
    "Content-Type": "application/json"
}
data = {
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
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
    }
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

### Bước 2: Tạo phụ tùng mới

```bash
curl -X POST "http://localhost:8080/api/parts" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -H "Content-Type: application/json" \
  -d '{
    "partNumber": "TESLA-BMS-001",
    "name": "Battery Management System Sensor",
    "description": "High-precision sensor for battery management system",
    "category": "ELECTRICAL",
    "subcategory": "BATTERY_SYSTEM",
    "oemId": 1,
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
    }
  }'
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
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "evmstaff@example.com"
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

### Status Information

- `status`: Trạng thái phụ tùng
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo

## Part Categories

### ELECTRICAL

- Hệ thống điện
- Cảm biến, điều khiển
- Dây điện, cầu chì

### MECHANICAL

- Hệ thống cơ khí
- Động cơ, hộp số
- Phanh, treo

### BODY

- Thân xe
- Cửa, cửa sổ
- Nội thất

### ENGINE

- Động cơ
- Hệ thống làm mát
- Hệ thống nhiên liệu

### TRANSMISSION

- Hộp số
- Ly hợp
- Cầu xe

## Validation Rules

### Part Number

- Không được trống
- Phải duy nhất trong hệ thống
- Tối đa 50 ký tự
- Chỉ chứa chữ cái, số và dấu gạch ngang

### Name

- Không được trống
- Tối đa 100 ký tự
- Phải mô tả rõ ràng

### Description

- Không được trống
- Tối đa 500 ký tự
- Phải mô tả chi tiết

### Category

- Phải là category hợp lệ
- Chỉ được chọn từ danh sách có sẵn
- Không được trống

### OEM

- Phải tồn tại trong hệ thống
- Phải active

### Pricing

- `cost`: Phải là số dương
- `retailPrice`: Phải là số dương
- `warrantyPrice`: Phải là số dương
- `currency`: Phải là currency hợp lệ

### Supplier

- `name`: Không được trống, tối đa 100 ký tự
- `contact`: Phải có format email hợp lệ
- `phone`: Phải có format số điện thoại hợp lệ
- `address`: Không được trống, tối đa 200 ký tự

### Warranty

- `period`: Phải là số dương
- `unit`: Phải là unit hợp lệ
- `conditions`: Tối đa 200 ký tự

### Stock

- `minQuantity`: Phải là số không âm
- `maxQuantity`: Phải là số dương
- `reorderPoint`: Phải là số không âm

## Permission Matrix

### Admin

- ✅ Có thể tạo phụ tùng cho bất kỳ OEM nào
- ✅ Có thể tạo phụ tùng với đầy đủ thông tin
- ✅ Có thể tạo phụ tùng với bất kỳ supplier nào

### EVM_Staff

- ✅ Có thể tạo phụ tùng cho OEM của mình
- ✅ Có thể tạo phụ tùng với đầy đủ thông tin
- ❌ Không thể tạo phụ tùng cho OEM khác

### SC_Staff

- ❌ Không có quyền tạo phụ tùng
- ❌ Chỉ có thể xem thông tin phụ tùng

### SC_Technician

- ❌ Không có quyền tạo phụ tùng
- ❌ Chỉ có thể xem thông tin phụ tùng

## Automatic Actions

### Part Creation

- Tạo part record trong database
- Gán ID duy nhất
- Tạo audit log

### Stock Setup

- Thiết lập stock information
- Tạo stock record
- Cập nhật inventory

### Supplier Assignment

- Gán supplier nếu được cung cấp
- Tạo supplier relationship
- Cập nhật supplier capacity

### Notification

- Gửi email đến supplier về part mới
- Gửi thông báo đến EVM_Staff
- Tạo task cho inventory management

## Manual Actions Required

### Supplier Notification

- Supplier cần xác nhận part mới
- Cập nhật thông tin part
- Thiết lập pricing

### Inventory Setup

- Cập nhật inventory system
- Thiết lập reorder points
- Cấu hình stock levels

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Part number phải duy nhất trong hệ thống
- OEM phải tồn tại và active
- Supplier phải tồn tại và active
- Pricing information phải hợp lệ
- Warranty information là optional
- Stock information là optional
- Specifications là optional
- Compatible vehicles là optional
- Notes là optional
- Part được tạo với status ACTIVE
- Stock được set về 0 ban đầu
- Supplier relationship được tạo
- Inventory record được tạo
- Audit log được tạo cho mọi thay đổi
- Notification được gửi đến stakeholders
- Cache được invalidate khi có thay đổi
- Performance được tối ưu thông qua caching
- Security được đảm bảo thông qua permissions
- Validation được thực hiện nghiêm ngặt
- Business rules được áp dụng cho từng field
- Supplier notification được gửi về part mới
- Inventory notification được gửi về stock setup
- EVM_Staff notification được gửi về part creation
- Task được tạo cho inventory management
- Email được gửi đến supplier
- SMS được gửi nếu cần thiết
- Push notification được gửi đến mobile app
- Webhook được gửi đến external systems
