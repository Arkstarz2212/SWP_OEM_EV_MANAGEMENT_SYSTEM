# GET /api/claims

## Mô tả

Lấy danh sách warranty claims với khả năng filter theo status, service center và hỗ trợ pagination. Endpoint này cho phép xem tất cả claims hoặc filter theo các tiêu chí cụ thể.

## Endpoint

```
GET /api/claims
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type    | Required | Description                                                        |
| --------------- | ------- | -------- | ------------------------------------------------------------------ |
| status          | string  | No       | Filter theo status (PENDING_APPROVAL, APPROVED, IN_PROGRESS, etc.) |
| serviceCenterId | long    | No       | Filter theo service center ID                                      |
| vin             | string  | No       | Filter theo VIN                                                    |
| limit           | integer | No       | Số lượng kết quả (1-100, mặc định 20)                              |
| offset          | integer | No       | Vị trí bắt đầu (mặc định 0)                                        |

## Response

### Success Response (200 OK)

```json
{
  "claims": [
    {
      "id": 1,
      "claimNumber": "WC-2024-001",
      "vin": "1HGBH41JXMN109186",
      "vehicleInfo": {
        "make": "Tesla",
        "model": "Model 3",
        "year": 2023
      },
      "serviceCenterId": 1,
      "serviceCenterName": "Downtown Service Center",
      "issueDescription": "Battery management system error",
      "diagnosis": "BMS sensor failure",
      "estimatedRepairCost": 1500.0,
      "status": "PENDING_APPROVAL",
      "createdAt": "2024-01-01T00:00:00Z",
      "createdBy": 2,
      "createdByName": "John SC Staff",
      "progress": {
        "currentStep": "Waiting for approval",
        "percentage": 20
      }
    },
    {
      "id": 2,
      "claimNumber": "WC-2024-002",
      "vin": "1HGBH41JXMN109187",
      "vehicleInfo": {
        "make": "Tesla",
        "model": "Model S",
        "year": 2022
      },
      "serviceCenterId": 1,
      "serviceCenterName": "Downtown Service Center",
      "issueDescription": "Brake system failure",
      "diagnosis": "Brake pad wear",
      "estimatedRepairCost": 300.0,
      "status": "IN_PROGRESS",
      "createdAt": "2024-01-02T00:00:00Z",
      "createdBy": 2,
      "createdByName": "John SC Staff",
      "progress": {
        "currentStep": "Repair in progress",
        "percentage": 80
      }
    }
  ],
  "total": 2,
  "offset": 0,
  "limit": 20
}
```

### Error Responses

#### 400 Bad Request - Limit không hợp lệ

```json
{
  "error": "Limit must be between 1 and 100",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Offset không hợp lệ

```json
{
  "error": "Offset must be 0 or greater",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Status không hợp lệ

```json
{
  "error": "Invalid status. Valid statuses are: PENDING_APPROVAL, APPROVED, IN_PROGRESS, ON_HOLD, COMPLETED, REJECTED, CANCELLED",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve claims: Internal server error",
  "path": "/api/claims",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem claims trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy tất cả claims

**Request:**

```
GET /api/claims
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách tất cả claims

### Test Case 2: Filter theo status PENDING_APPROVAL

**Request:**

```
GET /api/claims?status=PENDING_APPROVAL
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ PENDING_APPROVAL claims

### Test Case 3: Filter theo service center

**Request:**

```
GET /api/claims?serviceCenterId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với claims của service center 1

### Test Case 4: Filter theo VIN

**Request:**

```
GET /api/claims?vin=1HGBH41JXMN109186
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với claims của VIN cụ thể

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/claims?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 claims từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/claims?status=PENDING_APPROVAL&limit=10&offset=0" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/claims?status=PENDING_APPROVAL&limit=10&offset=0",
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

url = "http://localhost:8080/api/claims"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "status": "PENDING_APPROVAL",
    "limit": 10,
    "offset": 0
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

### Bước 2: Lấy danh sách tất cả claims

```bash
curl -X GET http://localhost:8080/api/claims \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "claims": [
    {
      "id": 1,
      "claimNumber": "WC-2024-001",
      "vin": "1HGBH41JXMN109186",
      "status": "PENDING_APPROVAL",
      "progress": {
        "currentStep": "Waiting for approval",
        "percentage": 20
      }
    },
    {
      "id": 2,
      "claimNumber": "WC-2024-002",
      "vin": "1HGBH41JXMN109187",
      "status": "IN_PROGRESS",
      "progress": {
        "currentStep": "Repair in progress",
        "percentage": 80
      }
    }
  ],
  "total": 2,
  "offset": 0,
  "limit": 20
}
```

### Bước 3: Filter theo status PENDING_APPROVAL

```bash
curl -X GET "http://localhost:8080/api/claims?status=PENDING_APPROVAL" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "claims": [
    {
      "id": 1,
      "claimNumber": "WC-2024-001",
      "vin": "1HGBH41JXMN109186",
      "status": "PENDING_APPROVAL",
      "progress": {
        "currentStep": "Waiting for approval",
        "percentage": 20
      }
    }
  ],
  "total": 1,
  "offset": 0,
  "limit": 20
}
```

## Filter Options

### Status Filter

- `PENDING_APPROVAL`: Chỉ lấy claims chờ phê duyệt
- `APPROVED`: Chỉ lấy claims đã phê duyệt
- `IN_PROGRESS`: Chỉ lấy claims đang sửa chữa
- `ON_HOLD`: Chỉ lấy claims tạm dừng
- `COMPLETED`: Chỉ lấy claims đã hoàn thành
- `REJECTED`: Chỉ lấy claims bị từ chối
- `CANCELLED`: Chỉ lấy claims bị hủy

### Service Center Filter

- `serviceCenterId`: Lấy claims của service center cụ thể
- Chỉ áp dụng cho SC_Staff và SC_Technician
- EVM_Staff và Admin có thể xem tất cả

### VIN Filter

- `vin`: Lấy claims của VIN cụ thể
- VIN phải có format hợp lệ
- Chỉ lấy claims của VIN tồn tại

### Pagination

- `limit`: Số lượng kết quả (1-100, mặc định 20)
- `offset`: Vị trí bắt đầu (0 hoặc lớn hơn, mặc định 0)
- Response bao gồm `total`, `offset`, `limit` để client biết thông tin pagination

## Response Fields Explanation

### Claim Object

- `id`: ID duy nhất của claim
- `claimNumber`: Số claim (format: WC-YYYY-XXX)
- `vin`: VIN của xe
- `vehicleInfo`: Thông tin xe (make, model, year)
- `serviceCenterId`: ID service center
- `serviceCenterName`: Tên service center
- `issueDescription`: Mô tả vấn đề
- `diagnosis`: Chẩn đoán kỹ thuật
- `estimatedRepairCost`: Chi phí sửa chữa ước tính
- `status`: Trạng thái hiện tại
- `createdAt`: Thời gian tạo
- `createdBy`: ID người tạo
- `createdByName`: Tên người tạo
- `progress`: Theo dõi tiến độ
  - `currentStep`: Bước hiện tại
  - `percentage`: Phần trăm hoàn thành

### Summary Info

- `total`: Tổng số claims thỏa mãn filter
- `offset`: Vị trí bắt đầu hiện tại
- `limit`: Số lượng kết quả hiện tại

## Permission Matrix

### SC_Staff

- ✅ Có thể xem claims của service center
- ✅ Có thể filter theo status và VIN
- ❌ Không thể xem claims của service center khác

### SC_Technician

- ✅ Có thể xem claims được gán
- ✅ Có thể filter theo status và VIN
- ❌ Không thể xem claims không được gán

### EVM_Staff

- ✅ Có thể xem tất cả claims
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### Admin

- ✅ Có thể xem tất cả claims
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter (status, serviceCenterId, vin)
- Join với bảng vehicles để lấy thông tin xe
- Join với bảng service_centers để lấy tên
- Sắp xếp theo createdAt (mới nhất trước)

### Caching

- Cache danh sách claims trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thay đổi claim

## Notes

- Endpoint này không yêu cầu quyền đặc biệt, chỉ cần đăng nhập
- Response sẽ khác nhau tùy theo quyền của người dùng
- Thông tin nhạy cảm chỉ hiển thị cho EVM_Staff và Admin
- Claims được sắp xếp theo createdAt (mới nhất trước)
- Nếu không có filter, trả về tất cả claims trong phạm vi quyền
- Limit tối đa là 100 để tránh overload server
- Vehicle info được join từ bảng vehicles
- Service center name được join từ bảng service_centers
- Progress tracking được tính real-time
- Nếu không có claims thỏa mãn filter, danh sách sẽ rỗng
- Status được hiển thị với màu sắc tương ứng trong UI
- Progress percentage được tính dựa trên số bước hoàn thành
- Claim number được format theo chuẩn WC-YYYY-XXX
