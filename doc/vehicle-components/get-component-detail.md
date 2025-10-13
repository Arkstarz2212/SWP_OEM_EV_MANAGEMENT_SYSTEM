# GET /api/vehicle-components/{id}

## Mô tả

Lấy chi tiết component theo ID.

## Endpoint

```
GET /api/vehicle-components/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description          |
| --------- | ---- | -------- | -------------------- |
| id        | long | Yes      | ID component cần lấy |

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
  "path": "/api/vehicle-components/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem component tương ứng

## Test Cases

1. Tồn tại ID: 200 OK
2. Không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không có quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/vehicle-components/1" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/vehicle-components/1", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/vehicle-components/1", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Phục vụ hiển thị chi tiết component.
