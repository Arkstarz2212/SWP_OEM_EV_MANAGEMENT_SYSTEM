# GET /api/inventory/status

## Mô tả

Lấy trạng thái tồn kho tổng quan của hệ thống. Endpoint này cho phép xem thông tin tổng quan về tồn kho bao gồm số lượng phụ tùng, giá trị tồn kho, và các cảnh báo.

## Endpoint

```
GET /api/inventory/status
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type    | Required | Description                         |
| --------------- | ------- | -------- | ----------------------------------- |
| serviceCenterId | long    | No       | Filter theo service center ID       |
| oemId           | long    | No       | Filter theo OEM ID                  |
| category        | string  | No       | Filter theo danh mục phụ tùng       |
| status          | string  | No       | Filter theo trạng thái phụ tùng     |
| lowStock        | boolean | No       | Filter theo tình trạng tồn kho thấp |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "summary": {
      "totalParts": 1250,
      "totalValue": 312500.0,
      "activeParts": 1100,
      "inactiveParts": 150,
      "outOfStockParts": 50,
      "lowStockParts": 75,
      "averageStockLevel": 45.5,
      "totalCategories": 5,
      "totalSuppliers": 25
    },
    "categories": [
      {
        "category": "ELECTRICAL",
        "totalParts": 500,
        "totalValue": 125000.0,
        "activeParts": 450,
        "outOfStockParts": 20,
        "lowStockParts": 30,
        "averageStockLevel": 42.5
      },
      {
        "category": "MECHANICAL",
        "totalParts": 350,
        "totalValue": 87500.0,
        "activeParts": 320,
        "outOfStockParts": 15,
        "lowStockParts": 25,
        "averageStockLevel": 48.0
      },
      {
        "category": "BODY",
        "totalParts": 200,
        "totalValue": 50000.0,
        "activeParts": 180,
        "outOfStockParts": 10,
        "lowStockParts": 15,
        "averageStockLevel": 52.5
      },
      {
        "category": "ENGINE",
        "totalParts": 150,
        "totalValue": 37500.0,
        "activeParts": 130,
        "outOfStockParts": 5,
        "lowStockParts": 5,
        "averageStockLevel": 38.0
      },
      {
        "category": "TRANSMISSION",
        "totalParts": 50,
        "totalValue": 12500.0,
        "activeParts": 45,
        "outOfStockParts": 0,
        "lowStockParts": 0,
        "averageStockLevel": 60.0
      }
    ],
    "alerts": [
      {
        "type": "LOW_STOCK",
        "severity": "HIGH",
        "message": "25 parts are running low on stock",
        "count": 25,
        "actionRequired": "Reorder parts immediately"
      },
      {
        "type": "OUT_OF_STOCK",
        "severity": "CRITICAL",
        "message": "10 parts are completely out of stock",
        "count": 10,
        "actionRequired": "Emergency reorder required"
      },
      {
        "type": "EXPIRING_WARRANTY",
        "severity": "MEDIUM",
        "message": "5 parts have warranty expiring in 30 days",
        "count": 5,
        "actionRequired": "Review warranty status"
      }
    ],
    "topSuppliers": [
      {
        "supplierId": 1,
        "supplierName": "Tesla Parts Supplier",
        "totalParts": 200,
        "totalValue": 50000.0,
        "activeParts": 180,
        "outOfStockParts": 10,
        "lowStockParts": 15
      },
      {
        "supplierId": 2,
        "supplierName": "BMW Parts Supplier",
        "totalParts": 150,
        "totalValue": 37500.0,
        "activeParts": 140,
        "outOfStockParts": 5,
        "lowStockParts": 10
      }
    ],
    "recentActivity": [
      {
        "id": 1,
        "type": "STOCK_UPDATE",
        "partNumber": "TESLA-BMS-001",
        "partName": "Battery Management System Sensor",
        "action": "Stock increased by 50 units",
        "timestamp": "2024-01-15T10:30:00Z",
        "user": "scstaff@example.com"
      },
      {
        "id": 2,
        "type": "REORDER",
        "partNumber": "TESLA-BMS-002",
        "partName": "Battery Management System Controller",
        "action": "Reorder triggered - 25 units",
        "timestamp": "2024-01-15T09:15:00Z",
        "user": "system"
      }
    ],
    "filters": {
      "serviceCenterId": null,
      "oemId": null,
      "category": null,
      "status": null,
      "lowStock": null
    },
    "lastUpdated": "2024-01-15T11:00:00Z"
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: serviceCenterId must be a positive number",
  "path": "/api/inventory/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve inventory status: Internal server error",
  "path": "/api/inventory/status",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem tồn kho trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy trạng thái tồn kho tổng quan

**Request:**

```
GET /api/inventory/status
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin tồn kho tổng quan

### Test Case 2: Filter theo service center

**Request:**

```
GET /api/inventory/status?serviceCenterId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin tồn kho của service center 1

### Test Case 3: Filter theo OEM

**Request:**

```
GET /api/inventory/status?oemId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin tồn kho của OEM 1

### Test Case 4: Filter theo category

**Request:**

```
GET /api/inventory/status?category=ELECTRICAL
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin tồn kho ELECTRICAL

### Test Case 5: Filter theo low stock

**Request:**

```
GET /api/inventory/status?lowStock=true
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin tồn kho thấp

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/inventory/status?serviceCenterId=1&category=ELECTRICAL" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/inventory/status?serviceCenterId=1&category=ELECTRICAL",
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

url = "http://localhost:8080/api/inventory/status"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "serviceCenterId": 1,
    "category": "ELECTRICAL"
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

### Bước 2: Lấy trạng thái tồn kho

```bash
curl -X GET "http://localhost:8080/api/inventory/status?serviceCenterId=1" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "summary": {
      "totalParts": 250,
      "totalValue": 62500.0,
      "activeParts": 220,
      "inactiveParts": 30,
      "outOfStockParts": 5,
      "lowStockParts": 15,
      "averageStockLevel": 45.5,
      "totalCategories": 5,
      "totalSuppliers": 10
    },
    "categories": [
      {
        "category": "ELECTRICAL",
        "totalParts": 100,
        "totalValue": 25000.0,
        "activeParts": 90,
        "outOfStockParts": 2,
        "lowStockParts": 5,
        "averageStockLevel": 42.5
      }
    ],
    "alerts": [
      {
        "type": "LOW_STOCK",
        "severity": "HIGH",
        "message": "15 parts are running low on stock",
        "count": 15,
        "actionRequired": "Reorder parts immediately"
      }
    ],
    "lastUpdated": "2024-01-15T11:00:00Z"
  }
}
```

## Response Fields Explanation

### Summary Information

- `totalParts`: Tổng số phụ tùng
- `totalValue`: Tổng giá trị tồn kho
- `activeParts`: Số phụ tùng đang hoạt động
- `inactiveParts`: Số phụ tùng không hoạt động
- `outOfStockParts`: Số phụ tùng hết hàng
- `lowStockParts`: Số phụ tùng tồn kho thấp
- `averageStockLevel`: Mức tồn kho trung bình
- `totalCategories`: Tổng số danh mục
- `totalSuppliers`: Tổng số nhà cung cấp

### Category Information

- `category`: Danh mục phụ tùng
- `totalParts`: Tổng số phụ tùng trong danh mục
- `totalValue`: Tổng giá trị trong danh mục
- `activeParts`: Số phụ tùng đang hoạt động
- `outOfStockParts`: Số phụ tùng hết hàng
- `lowStockParts`: Số phụ tùng tồn kho thấp
- `averageStockLevel`: Mức tồn kho trung bình

### Alert Information

- `type`: Loại cảnh báo
- `severity`: Mức độ nghiêm trọng
- `message`: Thông báo cảnh báo
- `count`: Số lượng
- `actionRequired`: Hành động cần thiết

### Top Suppliers Information

- `supplierId`: ID nhà cung cấp
- `supplierName`: Tên nhà cung cấp
- `totalParts`: Tổng số phụ tùng
- `totalValue`: Tổng giá trị
- `activeParts`: Số phụ tùng đang hoạt động
- `outOfStockParts`: Số phụ tùng hết hàng
- `lowStockParts`: Số phụ tùng tồn kho thấp

### Recent Activity Information

- `id`: ID hoạt động
- `type`: Loại hoạt động
- `partNumber`: Số phụ tùng
- `partName`: Tên phụ tùng
- `action`: Hành động
- `timestamp`: Thời gian
- `user`: Người thực hiện

### Filter Information

- `serviceCenterId`: Service center được filter
- `oemId`: OEM được filter
- `category`: Danh mục được filter
- `status`: Trạng thái được filter
- `lowStock`: Tình trạng tồn kho thấp được filter

## Alert Types

### LOW_STOCK

- Cảnh báo tồn kho thấp
- Severity: HIGH
- Action: Reorder parts immediately

### OUT_OF_STOCK

- Cảnh báo hết hàng
- Severity: CRITICAL
- Action: Emergency reorder required

### EXPIRING_WARRANTY

- Cảnh báo bảo hành sắp hết hạn
- Severity: MEDIUM
- Action: Review warranty status

### OVERSTOCK

- Cảnh báo tồn kho quá nhiều
- Severity: LOW
- Action: Review stock levels

### EXPIRED_WARRANTY

- Cảnh báo bảo hành đã hết hạn
- Severity: MEDIUM
- Action: Update warranty status

## Severity Levels

### CRITICAL

- Cần hành động ngay lập tức
- Ảnh hưởng nghiêm trọng đến hoạt động
- Ưu tiên cao nhất

### HIGH

- Cần hành động sớm
- Ảnh hưởng đến hiệu suất
- Ưu tiên cao

### MEDIUM

- Cần hành động trong thời gian hợp lý
- Ảnh hưởng vừa phải
- Ưu tiên trung bình

### LOW

- Cần hành động khi có thời gian
- Ảnh hưởng ít
- Ưu tiên thấp

## Activity Types

### STOCK_UPDATE

- Cập nhật tồn kho
- Thay đổi số lượng
- Thay đổi giá trị

### REORDER

- Đặt hàng lại
- Kích hoạt reorder
- Cập nhật supplier

### PART_ADDED

- Thêm phụ tùng mới
- Tạo part record
- Cập nhật inventory

### PART_REMOVED

- Xóa phụ tùng
- Cập nhật inventory
- Cập nhật stock

### PART_UPDATED

- Cập nhật phụ tùng
- Thay đổi thông tin
- Cập nhật inventory

## Permission Matrix

### Admin

- ✅ Có thể xem tất cả tồn kho
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem tồn kho của OEM
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem tồn kho OEM khác

### SC_Staff

- ✅ Có thể xem tồn kho của service center
- ❌ Không thể xem tồn kho service center khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem tồn kho được gán
- ❌ Không thể xem tồn kho không được gán
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng parts để lấy thông tin
- Sử dụng aggregation để tính toán metrics

### Caching

- Cache inventory status trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thay đổi inventory

### Data Aggregation

- Tính toán summary metrics real-time
- Sử dụng background jobs cho heavy calculations
- Cache kết quả trong database

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Filter được áp dụng theo thứ tự ưu tiên
- Summary metrics được tính toán real-time
- Alert information được cập nhật real-time
- Recent activity được lấy từ activity logs
- Top suppliers được sắp xếp theo total value
- Category information được nhóm theo category
- Filter information được trả về để debug
- Last updated được cập nhật khi có thay đổi
- Performance được tối ưu thông qua caching
- Database optimization được áp dụng
- Security được đảm bảo thông qua permissions
