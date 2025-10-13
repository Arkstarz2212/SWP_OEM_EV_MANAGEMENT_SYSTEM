# GET /api/vehicle-components/vehicle/{vehicleId}

## Mô tả

Danh sách tất cả components đang active của một xe.

## Endpoint

```
GET /api/vehicle-components/vehicle/{vehicleId}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| vehicleId | long | Yes      | ID xe       |

## Response

### Success Response (200 OK)

```json
[
  {
    "id": 1,
    "vehicleId": 1,
    "partCatalogId": 10,
    "serialNumber": "SN123456",
    "status": "ACTIVE"
  }
]
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "Vehicle not found or no components",
  "path": "/api/vehicle-components/vehicle/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem components của xe

## Test Cases

1. Xe có components: 200 OK (mảng ≥ 1)
2. Xe không có components: 200 OK (mảng rỗng) hoặc 404 tùy implement
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không có quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/vehicle-components/vehicle/1" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/vehicle-components/vehicle/1",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/vehicle-components/vehicle/1", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Trả về chỉ components ACTIVE.
