# POST /api/claims

## Mô tả

Tạo warranty claim mới cho xe bị lỗi. Endpoint này cho phép SC Staff tạo claim và gửi đến EVM Staff để xem xét và phê duyệt.

## Endpoint

```
POST /api/claims
```

## Request

### Headers

```
Content-Type: application/json
Authorization: Bearer <session_token>
```

### Body

```json
{
  "vin": "1HGBH41JXMN109186",
  "serviceCenterId": 1,
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure",
  "estimatedRepairCost": 1500.0,
  "partsNeeded": [
    {
      "partNumber": "BMS-001",
      "quantity": 1,
      "description": "Battery Management System Sensor"
    }
  ],
  "attachments": [
    {
      "type": "IMAGE",
      "url": "https://storage.example.com/diagnosis_photo.jpg",
      "description": "BMS error photo"
    }
  ]
}
```

### Parameters

| Field               | Type   | Required | Description                  |
| ------------------- | ------ | -------- | ---------------------------- |
| vin                 | string | Yes      | VIN của xe cần bảo hành      |
| serviceCenterId     | long   | Yes      | ID service center tạo claim  |
| issueDescription    | string | Yes      | Mô tả vấn đề của xe          |
| diagnosis           | string | Yes      | Chẩn đoán kỹ thuật           |
| estimatedRepairCost | number | Yes      | Chi phí sửa chữa ước tính    |
| partsNeeded         | array  | No       | Danh sách phụ tùng cần thiết |
| attachments         | array  | No       | Danh sách file đính kèm      |

## Response

### Success Response (201 Created)

```json
{
  "id": 1,
  "claimNumber": "WC-2024-001",
  "vin": "1HGBH41JXMN109186",
  "serviceCenterId": 1,
  "serviceCenterName": "Downtown Service Center",
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure",
  "estimatedRepairCost": 1500.0,
  "status": "PENDING_APPROVAL",
  "createdAt": "2024-01-01T00:00:00Z",
  "createdBy": 2,
  "createdByName": "John SC Staff",
  "partsNeeded": [
    {
      "partNumber": "BMS-001",
      "quantity": 1,
      "description": "Battery Management System Sensor"
    }
  ],
  "attachments": [
    {
      "id": 1,
      "type": "IMAGE",
      "url": "https://storage.example.com/diagnosis_photo.jpg",
      "description": "BMS error photo"
    }
  ]
}
```

### Error Responses

#### 400 Bad Request - Thiếu thông tin bắt buộc

```json
{
  "error": "VIN is required",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - VIN không hợp lệ

```json
{
  "error": "Invalid VIN format",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - VIN không tồn tại

```json
{
  "error": "Vehicle not found with VIN: 1HGBH41JXMN109186",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 409 Conflict - Claim đã tồn tại

```json
{
  "error": "Claim already exists for this VIN and issue",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to create claim: Internal server error",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với quyền SC_Staff hoặc SC_Technician
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token hợp lệ
- **Permission:** Chỉ SC_Staff và SC_Technician mới có quyền tạo claim
- **Vehicle:** VIN phải tồn tại trong hệ thống
- **Service Center:** Service center phải tồn tại và active

## Test Cases

### Test Case 1: Tạo claim thành công

**Request:**

```json
{
  "vin": "1HGBH41JXMN109186",
  "serviceCenterId": 1,
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure",
  "estimatedRepairCost": 1500.0
}
```

**Expected Response:** 201 Created với thông tin claim

### Test Case 2: Tạo claim với phụ tùng

**Request:**

```json
{
  "vin": "1HGBH41JXMN109186",
  "serviceCenterId": 1,
  "issueDescription": "Engine oil leak",
  "diagnosis": "Oil pan gasket failure",
  "estimatedRepairCost": 800.0,
  "partsNeeded": [
    {
      "partNumber": "OIL-GASKET-001",
      "quantity": 1,
      "description": "Oil Pan Gasket"
    }
  ]
}
```

**Expected Response:** 201 Created với danh sách phụ tùng

### Test Case 3: Tạo claim với file đính kèm

**Request:**

```json
{
  "vin": "1HGBH41JXMN109186",
  "serviceCenterId": 1,
  "issueDescription": "Brake system failure",
  "diagnosis": "Brake pad wear",
  "estimatedRepairCost": 300.0,
  "attachments": [
    {
      "type": "IMAGE",
      "url": "https://storage.example.com/brake_photo.jpg",
      "description": "Brake pad photo"
    }
  ]
}
```

**Expected Response:** 201 Created với file đính kèm

### Test Case 4: VIN không tồn tại

**Request:**

```json
{
  "vin": "INVALID_VIN_123456",
  "serviceCenterId": 1,
  "issueDescription": "Test issue",
  "diagnosis": "Test diagnosis",
  "estimatedRepairCost": 100.0
}
```

**Expected Response:** 404 Not Found

### Test Case 5: Thiếu thông tin bắt buộc

**Request:**

```json
{
  "serviceCenterId": 1,
  "issueDescription": "Test issue",
  "diagnosis": "Test diagnosis"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST http://localhost:8080/api/claims \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "vin": "1HGBH41JXMN109186",
    "serviceCenterId": 1,
    "issueDescription": "Battery management system error",
    "diagnosis": "BMS sensor failure",
    "estimatedRepairCost": 1500.00,
    "partsNeeded": [
      {
        "partNumber": "BMS-001",
        "quantity": 1,
        "description": "Battery Management System Sensor"
      }
    ]
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/claims", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    Authorization: "Bearer sess_abc123def456ghi789",
  },
  body: JSON.stringify({
    vin: "1HGBH41JXMN109186",
    serviceCenterId: 1,
    issueDescription: "Battery management system error",
    diagnosis: "BMS sensor failure",
    estimatedRepairCost: 1500.0,
    partsNeeded: [
      {
        partNumber: "BMS-001",
        quantity: 1,
        description: "Battery Management System Sensor",
      },
    ],
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/claims"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer sess_abc123def456ghi789"
}
data = {
    "vin": "1HGBH41JXMN109186",
    "serviceCenterId": 1,
    "issueDescription": "Battery management system error",
    "diagnosis": "BMS sensor failure",
    "estimatedRepairCost": 1500.00,
    "partsNeeded": [
        {
            "partNumber": "BMS-001",
            "quantity": 1,
            "description": "Battery Management System Sensor"
        }
    ]
}

response = requests.post(url, json=data, headers=headers)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập với quyền SC_Staff

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "scstaff@service.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 2,
    "role": "SC_Staff",
    "serviceCenterId": 1,
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Tạo warranty claim

```bash
curl -X POST http://localhost:8080/api/claims \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "vin": "1HGBH41JXMN109186",
    "serviceCenterId": 1,
    "issueDescription": "Battery management system error",
    "diagnosis": "BMS sensor failure",
    "estimatedRepairCost": 1500.00,
    "partsNeeded": [
      {
        "partNumber": "BMS-001",
        "quantity": 1,
        "description": "Battery Management System Sensor"
      }
    ]
  }'
```

**Response:**

```json
{
  "id": 1,
  "claimNumber": "WC-2024-001",
  "vin": "1HGBH41JXMN109186",
  "serviceCenterId": 1,
  "serviceCenterName": "Downtown Service Center",
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure",
  "estimatedRepairCost": 1500.0,
  "status": "PENDING_APPROVAL",
  "createdAt": "2024-01-01T00:00:00Z",
  "createdBy": 2,
  "createdByName": "John SC Staff"
}
```

### Bước 3: EVM Staff xem xét claim

```bash
curl -X GET http://localhost:8080/api/claims/1 \
  -H "Authorization: Bearer sess_evmstaff_token"
```

## Claim Status Flow

```
PENDING_APPROVAL → APPROVED → IN_PROGRESS → COMPLETED
       ↓              ↓           ↓
   REJECTED      CANCELLED    ON_HOLD
```

### Status Transitions

- **PENDING_APPROVAL → APPROVED:** Khi EVM Staff phê duyệt
- **PENDING_APPROVAL → REJECTED:** Khi EVM Staff từ chối
- **APPROVED → IN_PROGRESS:** Khi bắt đầu sửa chữa
- **IN_PROGRESS → COMPLETED:** Khi hoàn thành sửa chữa
- **IN_PROGRESS → ON_HOLD:** Khi tạm dừng sửa chữa
- **APPROVED → CANCELLED:** Khi hủy claim

## Validation Rules

### VIN

- Phải có format VIN hợp lệ (17 ký tự)
- VIN phải tồn tại trong hệ thống
- VIN phải thuộc về OEM của service center

### Service Center

- Service center phải tồn tại và active
- Service center phải có quyền xử lý VIN này
- Service center phải thuộc về OEM của VIN

### Issue Description

- Không được trống
- Tối đa 1000 ký tự
- Phải mô tả rõ ràng vấn đề

### Diagnosis

- Không được trống
- Tối đa 500 ký tự
- Phải là chẩn đoán kỹ thuật chính xác

### Estimated Repair Cost

- Phải là số dương
- Tối đa 1,000,000.00
- Phải hợp lý với loại sửa chữa

### Parts Needed

- Mỗi part phải có partNumber hợp lệ
- Quantity phải là số dương
- Description phải rõ ràng

### Attachments

- Mỗi attachment phải có type hợp lệ
- URL phải accessible
- Description phải rõ ràng

## Permission Matrix

### SC_Staff

- ✅ Có thể tạo claim cho service center của mình
- ✅ Có thể tạo claim với phụ tùng và file đính kèm
- ❌ Không thể tạo claim cho service center khác

### SC_Technician

- ✅ Có thể tạo claim cho service center của mình
- ✅ Có thể tạo claim với phụ tùng và file đính kèm
- ❌ Không thể tạo claim cho service center khác

### EVM_Staff

- ❌ Không có quyền tạo claim
- ✅ Có thể xem và phê duyệt claim

### Admin

- ❌ Không có quyền tạo claim
- ✅ Có thể xem và quản lý tất cả claim

## What Happens After Creation

### Automatic Actions

- Tạo claim number tự động (format: WC-YYYY-XXX)
- Gửi thông báo đến EVM Staff
- Tạo notification records
- Ghi log trong audit trail

### Manual Actions Required

- EVM Staff xem xét và phê duyệt
- Cập nhật status khi có thay đổi
- Upload thêm file đính kèm nếu cần
- Cập nhật thông tin sửa chữa

## Notes

- Endpoint này chỉ dành cho SC_Staff và SC_Technician
- EVM_Staff và Admin không có quyền tạo claim
- Claim mới tạo sẽ ở trạng thái PENDING_APPROVAL
- VIN phải tồn tại và thuộc về OEM của service center
- Service center phải có quyền xử lý VIN này
- Tất cả thông tin được ghi log để audit
- Claim number được tạo tự động và unique
- File đính kèm phải được upload trước khi tạo claim
- Phụ tùng phải có trong catalog của OEM
- Chi phí sửa chữa phải hợp lý với loại lỗi
