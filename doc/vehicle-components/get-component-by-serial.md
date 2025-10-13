# GET /api/vehicle-components/by-serial/{serialNumber}

## Mô tả

Tra cứu component theo số serial.

## Endpoint

```
GET /api/vehicle-components/by-serial/{serialNumber}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter    | Type   | Required | Description         |
| ------------ | ------ | -------- | ------------------- |
| serialNumber | string | Yes      | Số serial component |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "vehicleId": 1,
  "partCatalogId": 10,
  "serialNumber": "SN123456",
  "status": "ACTIVE"
}
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "Component not found",
  "path": "/api/vehicle-components/by-serial/SN-UNKNOWN",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem component tương ứng

## Test Cases

1. Tìm thấy theo serial: 200 OK
2. Serial không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không có quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/vehicle-components/by-serial/SN123456" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/vehicle-components/by-serial/SN123456",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/vehicle-components/by-serial/SN123456", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Phục vụ tra cứu nhanh theo serial.
