# GET /api/roles/{id}

## Mô tả

Lấy chi tiết role theo ID.

## Endpoint

```
GET /api/roles/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| id        | long | Yes      | ID role     |

## Response

### Success Response (200 OK)

```json
{
  "id": 2,
  "code": "EVM_Staff",
  "name": "OEM Staff",
  "description": "OEM staff with OEM-scoped permissions"
}
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "Role not found",
  "path": "/api/roles/9999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Bắt buộc: Đăng nhập (`POST /api/auth/login`)
- Quyền: Admin hoặc có quyền xem role

## Test Cases

1. Lấy role tồn tại (200)
2. Role không tồn tại (404)
3. Thiếu token (401)
4. Token hết hạn (401)
5. Không đủ quyền (403)

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/roles/2" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/roles/2", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/roles/2", headers={"Authorization": "Bearer <token>"}).json())
```

## Notes

- Dùng để hiển thị form chi tiết role.
