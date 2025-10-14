# GET /api/claims/{id}

## Mô tả

Lấy thông tin chi tiết của một warranty claim bao gồm tất cả thông tin liên quan như status history, attachments, và progress tracking.

## Endpoint

```
GET /api/claims/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description          |
| --------- | ---- | -------- | -------------------- |
| id        | long | Yes      | ID của claim cần lấy |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "claimNumber": "WC-2024-001",
  "vin": "1HGBH41JXMN109186",
  "vehicleInfo": {
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "color": "Pearl White"
  },
  "serviceCenterId": 1,
  "serviceCenterName": "Downtown Service Center",
  "issueDescription": "Battery management system error",
  "diagnosis": "BMS sensor failure",
  "estimatedRepairCost": 1500.0,
  "actualRepairCost": 0.0,
  "status": "PENDING_APPROVAL",
  "createdAt": "2024-01-01T00:00:00Z",
  "createdBy": 2,
  "createdByName": "John SC Staff",
  "approvedAt": null,
  "approvedBy": null,
  "approvedByName": null,
  "completedAt": null,
  "partsNeeded": [
    {
      "partNumber": "BMS-001",
      "quantity": 1,
      "description": "Battery Management System Sensor",
      "unitPrice": 800.0,
      "totalPrice": 800.0,
      "status": "PENDING"
    }
  ],
  "attachments": [
    {
      "id": 1,
      "type": "IMAGE",
      "url": "https://storage.example.com/diagnosis_photo.jpg",
      "description": "BMS error photo",
      "uploadedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "statusHistory": [
    {
      "status": "PENDING_APPROVAL",
      "timestamp": "2024-01-01T00:00:00Z",
      "changedBy": "John SC Staff",
      "notes": "Claim created"
    }
  ],
  "progress": {
    "currentStep": "Waiting for approval",
    "completedSteps": 1,
    "totalSteps": 5,
    "percentage": 20
  }
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid claim ID",
  "path": "/api/claims/0",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Claim không tồn tại

```json
{
  "error": "Claim not found",
  "path": "/api/claims/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve claim: Internal server error",
  "path": "/api/claims/1",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem claim trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy claim detail thành công

**Request:**

```
GET /api/claims/1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với thông tin đầy đủ

### Test Case 2: Lấy claim đã hoàn thành

**Request:**

```
GET /api/claims/2
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với status COMPLETED

### Test Case 3: ID không hợp lệ (0 hoặc âm)

**Request:**

```
GET /api/claims/0
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Claim không tồn tại

**Request:**

```
GET /api/claims/999
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found

### Test Case 5: Không có authorization

**Request:**

```
GET /api/claims/1
```

**Expected Response:** 401 Unauthorized

## Usage Example

### cURL

```bash
curl -X GET http://localhost:8080/api/claims/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/claims/1", {
  method: "GET",
  headers: {
    Authorization: "Bearer sess_abc123def456ghi789",
  },
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/claims/1"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}

response = requests.get(url, headers=headers)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập để lấy session token

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
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Lấy thông tin claim

```bash
curl -X GET http://localhost:8080/api/claims/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 1,
  "claimNumber": "WC-2024-001",
  "vin": "1HGBH41JXMN109186",
  "vehicleInfo": {
    "make": "Tesla",
    "model": "Model 3",
    "year": 2023,
    "color": "Pearl White"
  },
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

## Response Fields Explanation

### Basic Information

- `id`: ID duy nhất của claim
- `claimNumber`: Số claim (format: WC-YYYY-XXX)
- `vin`: VIN của xe
- `vehicleInfo`: Thông tin xe (make, model, year, color)

### Service Center Information

- `serviceCenterId`: ID service center
- `serviceCenterName`: Tên service center

### Issue Information

- `issueDescription`: Mô tả vấn đề
- `diagnosis`: Chẩn đoán kỹ thuật
- `estimatedRepairCost`: Chi phí sửa chữa ước tính
- `actualRepairCost`: Chi phí sửa chữa thực tế

### Status Information

- `status`: Trạng thái hiện tại
- `createdAt`: Thời gian tạo
- `createdBy`: ID người tạo
- `createdByName`: Tên người tạo
- `approvedAt`: Thời gian phê duyệt
- `approvedBy`: ID người phê duyệt
- `approvedByName`: Tên người phê duyệt
- `completedAt`: Thời gian hoàn thành

### Parts Information

- `partsNeeded`: Danh sách phụ tùng cần thiết
  - `partNumber`: Số phụ tùng
  - `quantity`: Số lượng
  - `description`: Mô tả
  - `unitPrice`: Giá đơn vị
  - `totalPrice`: Tổng giá
  - `status`: Trạng thái phụ tùng

### Attachments

- `attachments`: Danh sách file đính kèm
  - `id`: ID file
  - `type`: Loại file (IMAGE, DOCUMENT, VIDEO)
  - `url`: URL file
  - `description`: Mô tả
  - `uploadedAt`: Thời gian upload

### Status History

- `statusHistory`: Lịch sử thay đổi trạng thái
  - `status`: Trạng thái cũ
  - `timestamp`: Thời gian thay đổi
  - `changedBy`: Người thay đổi
  - `notes`: Ghi chú

### Progress Tracking

- `progress`: Theo dõi tiến độ
  - `currentStep`: Bước hiện tại
  - `completedSteps`: Số bước đã hoàn thành
  - `totalSteps`: Tổng số bước
  - `percentage`: Phần trăm hoàn thành

## Permission Matrix

### SC_Staff

- ✅ Có thể xem claim của service center
- ✅ Có thể xem thông tin chi tiết
- ❌ Không thể xem claim của service center khác

### SC_Technician

- ✅ Có thể xem claim được gán
- ✅ Có thể xem thông tin chi tiết
- ❌ Không thể xem claim không được gán

### EVM_Staff

- ✅ Có thể xem tất cả claim
- ✅ Có thể xem thông tin nhạy cảm
- ✅ Có thể xem progress tracking

### Admin

- ✅ Có thể xem tất cả claim
- ✅ Có thể xem thông tin nhạy cảm
- ✅ Có thể xem progress tracking

## Claim Status Values

### PENDING_APPROVAL

- Claim mới tạo, chờ phê duyệt
- EVM Staff cần xem xét
- Chưa bắt đầu sửa chữa

### APPROVED

- Claim đã được phê duyệt
- Có thể bắt đầu sửa chữa
- Phụ tùng có thể được order

### IN_PROGRESS

- Đang trong quá trình sửa chữa
- Technician đang thực hiện
- Có thể cập nhật tiến độ

### ON_HOLD

- Tạm dừng sửa chữa
- Cần thêm thông tin hoặc phụ tùng
- Có thể tiếp tục sau

### COMPLETED

- Sửa chữa hoàn thành
- Claim đã kết thúc
- Không thể chỉnh sửa

### REJECTED

- Claim bị từ chối
- Không được sửa chữa
- Cần tạo claim mới

### CANCELLED

- Claim bị hủy
- Không được sửa chữa
- Có thể tạo claim mới

## Progress Steps

### Typical Workflow

1. **Claim Created** (20%)
2. **Pending Approval** (40%)
3. **Approved** (60%)
4. **In Progress** (80%)
5. **Completed** (100%)

### Step Descriptions

- **Claim Created**: Claim đã được tạo
- **Pending Approval**: Chờ EVM Staff phê duyệt
- **Approved**: Đã được phê duyệt, có thể bắt đầu
- **In Progress**: Đang sửa chữa
- **Completed**: Hoàn thành sửa chữa

## Performance Considerations

### Database Optimization

- Sử dụng index trên claim ID
- Join với bảng vehicles để lấy thông tin xe
- Join với bảng service_centers để lấy tên
- Lazy load cho attachments nếu danh sách lớn

### Caching

- Cache claim details trong 5 phút
- Invalidate cache khi có thay đổi
- Cache progress tracking riêng biệt

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Thông tin nhạy cảm chỉ hiển thị cho EVM_Staff và Admin
- Vehicle info được join từ bảng vehicles
- Service center name được join từ bảng service_centers
- Status history được sắp xếp theo thời gian (mới nhất trước)
- Progress tracking được tính real-time
- Nếu claim không có attachments, danh sách sẽ rỗng
- Nếu claim không có parts needed, danh sách sẽ rỗng
- Actual repair cost sẽ được cập nhật khi hoàn thành
- Status history bao gồm tất cả thay đổi trạng thái
- Progress percentage được tính dựa trên số bước hoàn thành
