# GET /api/roles/by-code/{code}

## Mô tả

Lấy role theo mã code (ví dụ: ADMIN, EVM_Staff).

## Endpoint

```
GET /api/roles/by-code/{code}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type   | Required | Description      |
| --------- | ------ | -------- | ---------------- |
| code      | string | Yes      | Mã code của role |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "code": "ADMIN",
  "name": "Administrator",
  "description": "System administrator with full permissions"
}
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "Role not found",
  "path": "/api/roles/by-code/UNKNOWN",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Bắt buộc: Đăng nhập (`POST /api/auth/login`)
- Quyền: Admin hoặc có quyền xem role

## Test Cases

1. Lấy role code hợp lệ (200)
2. Code không tồn tại (404)
3. Thiếu token (401)
4. Token hết hạn (401)
5. Không đủ quyền (403)

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/roles/by-code/ADMIN" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/roles/by-code/ADMIN", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/roles/by-code/ADMIN", headers={"Authorization": "Bearer <token>"}).json())
```

## Notes

- Hữu ích cho tra cứu nhanh role theo code.
