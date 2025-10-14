# GET /api/service-centers

## Mô tả

Lấy danh sách service centers với khả năng filter theo nhiều tiêu chí. Endpoint này cho phép xem danh sách service centers với thông tin tóm tắt và phân trang.

## Endpoint

```
GET /api/service-centers
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type    | Required | Description                                        |
| --------------- | ------- | -------- | -------------------------------------------------- |
| oemId           | long    | No       | Filter theo OEM ID                                 |
| status          | string  | No       | Filter theo status (ACTIVE, INACTIVE, MAINTENANCE) |
| region          | string  | No       | Filter theo khu vực                                |
| services        | string  | No       | Filter theo dịch vụ (WARRANTY_REPAIR, MAINTENANCE) |
| capacity        | integer | No       | Filter theo capacity tối thiểu                     |
| utilizationRate | number  | No       | Filter theo tỷ lệ sử dụng tối thiểu                |
| limit           | integer | No       | Số lượng kết quả (1-100, mặc định 20)              |
| offset          | integer | No       | Vị trí bắt đầu (mặc định 0)                        |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "serviceCenters": [
      {
        "id": 1,
        "name": "Downtown Service Center",
        "address": "123 Main Street, New York, NY 10001",
        "phone": "+1-555-0123",
        "email": "downtown@servicecenter.com",
        "oemId": 1,
        "oemName": "Tesla Motors",
        "capacity": 50,
        "currentUtilization": 38,
        "utilizationRate": 76.0,
        "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION"],
        "status": "ACTIVE",
        "managerName": "John Manager",
        "averageRating": 4.5,
        "totalServices": 1250,
        "completionRate": 94.4,
        "createdAt": "2024-01-01T00:00:00Z"
      },
      {
        "id": 2,
        "name": "Uptown Service Center",
        "address": "456 Oak Avenue, Los Angeles, CA 90210",
        "phone": "+1-555-0456",
        "email": "uptown@servicecenter.com",
        "oemId": 1,
        "oemName": "Tesla Motors",
        "capacity": 75,
        "currentUtilization": 60,
        "utilizationRate": 80.0,
        "services": ["WARRANTY_REPAIR", "MAINTENANCE"],
        "status": "ACTIVE",
        "managerName": "Jane Manager",
        "averageRating": 4.3,
        "totalServices": 980,
        "completionRate": 88.0,
        "createdAt": "2024-01-02T00:00:00Z"
      }
    ],
    "pagination": {
      "total": 25,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalServiceCenters": 25,
      "activeServiceCenters": 23,
      "inactiveServiceCenters": 2,
      "averageUtilization": 72.5,
      "averageRating": 4.4
    },
    "filters": {
      "oemId": 1,
      "status": "ACTIVE",
      "region": null,
      "services": null,
      "capacity": null,
      "utilizationRate": null
    }
  }
}
```

### Error Responses

#### 400 Bad Request - Parameter không hợp lệ

```json
{
  "error": "Invalid parameter: limit must be between 1 and 100",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Status không hợp lệ

```json
{
  "error": "Invalid status. Valid statuses are: ACTIVE, INACTIVE, MAINTENANCE, OVERLOADED",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to retrieve service centers: Internal server error",
  "path": "/api/service-centers",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể xem service centers trong phạm vi quyền

## Test Cases

### Test Case 1: Lấy danh sách service centers mặc định

**Request:**

```
GET /api/service-centers
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với danh sách service centers

### Test Case 2: Filter theo status ACTIVE

**Request:**

```
GET /api/service-centers?status=ACTIVE
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với chỉ ACTIVE service centers

### Test Case 3: Filter theo OEM

**Request:**

```
GET /api/service-centers?oemId=1
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với service centers của OEM 1

### Test Case 4: Filter theo services

**Request:**

```
GET /api/service-centers?services=WARRANTY_REPAIR
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với service centers có WARRANTY_REPAIR

### Test Case 5: Pagination với limit và offset

**Request:**

```
GET /api/service-centers?limit=10&offset=20
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với 10 service centers từ vị trí 20

## Usage Example

### cURL

```bash
curl -X GET "http://localhost:8080/api/service-centers?status=ACTIVE&oemId=1&limit=20" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/service-centers?status=ACTIVE&oemId=1&limit=20",
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

url = "http://localhost:8080/api/service-centers"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "status": "ACTIVE",
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

### Bước 2: Lấy danh sách service centers

```bash
curl -X GET "http://localhost:8080/api/service-centers?status=ACTIVE" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "data": {
    "serviceCenters": [
      {
        "id": 1,
        "name": "Downtown Service Center",
        "address": "123 Main Street, New York, NY 10001",
        "phone": "+1-555-0123",
        "email": "downtown@servicecenter.com",
        "oemId": 1,
        "oemName": "Tesla Motors",
        "capacity": 50,
        "currentUtilization": 38,
        "utilizationRate": 76.0,
        "services": ["WARRANTY_REPAIR", "MAINTENANCE", "INSPECTION"],
        "status": "ACTIVE",
        "managerName": "John Manager",
        "averageRating": 4.5,
        "totalServices": 1250,
        "completionRate": 94.4
      }
    ],
    "pagination": {
      "total": 25,
      "limit": 20,
      "offset": 0,
      "hasNext": true,
      "hasPrevious": false
    },
    "summary": {
      "totalServiceCenters": 25,
      "activeServiceCenters": 23,
      "inactiveServiceCenters": 2,
      "averageUtilization": 72.5,
      "averageRating": 4.4
    }
  }
}
```

## Response Fields Explanation

### Service Center Object

- `id`: ID duy nhất của service center
- `name`: Tên service center
- `address`: Địa chỉ service center
- `phone`: Số điện thoại liên hệ
- `email`: Email liên hệ
- `oemId`: ID của OEM
- `oemName`: Tên OEM
- `capacity`: Sức chứa tối đa
- `currentUtilization`: Số lượng hiện tại đang sử dụng
- `utilizationRate`: Tỷ lệ sử dụng (%)
- `services`: Danh sách dịch vụ cung cấp
- `status`: Trạng thái service center
- `managerName`: Tên manager
- `averageRating`: Đánh giá trung bình (1-5)
- `totalServices`: Tổng số dịch vụ
- `completionRate`: Tỷ lệ hoàn thành (%)
- `createdAt`: Thời gian tạo

### Pagination Information

- `total`: Tổng số service centers thỏa mãn filter
- `limit`: Số lượng kết quả hiện tại
- `offset`: Vị trí bắt đầu hiện tại
- `hasNext`: Có trang tiếp theo không
- `hasPrevious`: Có trang trước không

### Summary Information

- `totalServiceCenters`: Tổng số service centers
- `activeServiceCenters`: Số service centers đang hoạt động
- `inactiveServiceCenters`: Số service centers không hoạt động
- `averageUtilization`: Tỷ lệ sử dụng trung bình (%)
- `averageRating`: Đánh giá trung bình (1-5)

### Filter Information

- `oemId`: OEM được filter
- `status`: Status được filter
- `region`: Khu vực được filter
- `services`: Dịch vụ được filter
- `capacity`: Capacity được filter
- `utilizationRate`: Tỷ lệ sử dụng được filter

## Filter Options

### OEM Filter

- `oemId`: Filter theo OEM cụ thể
- Chỉ áp dụng cho EVM_Staff và Admin
- SC_Staff và SC_Technician chỉ thấy service center của mình

### Status Filter

- `ACTIVE`: Chỉ service centers đang hoạt động
- `INACTIVE`: Chỉ service centers không hoạt động
- `MAINTENANCE`: Chỉ service centers đang bảo trì
- `OVERLOADED`: Chỉ service centers quá tải

### Region Filter

- `North America`: Chỉ service centers ở Bắc Mỹ
- `Europe`: Chỉ service centers ở Châu Âu
- `Asia`: Chỉ service centers ở Châu Á
- `Other`: Chỉ service centers ở khu vực khác

### Services Filter

- `WARRANTY_REPAIR`: Chỉ service centers có sửa chữa bảo hành
- `MAINTENANCE`: Chỉ service centers có bảo trì
- `INSPECTION`: Chỉ service centers có kiểm tra
- `DIAGNOSTICS`: Chỉ service centers có chẩn đoán

### Capacity Filter

- `capacity`: Filter theo capacity tối thiểu
- Chỉ lấy service centers có capacity >= giá trị

### Utilization Rate Filter

- `utilizationRate`: Filter theo tỷ lệ sử dụng tối thiểu
- Chỉ lấy service centers có utilization rate >= giá trị

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

- ✅ Có thể xem tất cả service centers
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ✅ Có thể xem thông tin nhạy cảm

### EVM_Staff

- ✅ Có thể xem service centers của OEM
- ✅ Có thể filter theo bất kỳ tiêu chí nào
- ❌ Không thể xem service centers OEM khác

### SC_Staff

- ✅ Có thể xem service center được gán
- ❌ Không thể xem service centers khác
- ❌ Không thể xem thông tin nhạy cảm

### SC_Technician

- ✅ Có thể xem service center được gán
- ❌ Không thể xem service centers khác
- ❌ Không thể xem thông tin nhạy cảm

## Performance Considerations

### Database Optimization

- Sử dụng index trên các field filter
- Join với bảng users để lấy thông tin manager
- Sử dụng pagination để giới hạn kết quả

### Caching

- Cache danh sách service centers trong 5 phút
- Cache riêng cho từng filter combination
- Invalidate cache khi có thay đổi service center

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
- Service center object được tối ưu cho danh sách
- Performance metrics được tính toán từ service history
- Manager information được lấy từ user management system
- OEM information được lấy từ OEM management system
- Status được cập nhật real-time
- Utilization rate được tính từ current appointments
- Average rating được tính từ customer feedback
- Completion rate được tính từ service history
- Total services được tính từ service records
- Created at được lấy từ audit logs
- Filter information được trả về để debug
- Pagination information được trả về để navigation
- Summary information được trả về để overview
