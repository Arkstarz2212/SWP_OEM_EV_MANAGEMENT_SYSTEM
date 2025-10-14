# PUT /api/roles

## Mô tả

Cập nhật tên và mô tả của role theo ID (code không đổi).

## Endpoint

```
PUT /api/roles
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Body

```json
{
  "id": 10,
  "name": "Tech Lead",
  "description": "Updated description"
}
```

### Parameters

| Field       | Type   | Required | Description |
| ----------- | ------ | -------- | ----------- |
| id          | long   | Yes      | ID role     |
| name        | string | Yes      | Tên role    |
| description | string | No       | Mô tả       |

## Response

### Success Response (200 OK)

```json
{
  "id": 10,
  "code": "TECH_LEAD",
  "name": "Tech Lead",
  "description": "Updated description"
}
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "Role not found",
  "path": "/api/roles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Bắt buộc: Đăng nhập (`POST /api/auth/login`)
- Quyền: Admin

## Test Cases

1. Cập nhật role hợp lệ (200)
2. Role không tồn tại (404)
3. Thiếu tên (400)
4. Thiếu token (401)
5. Không đủ quyền (403)

## Usage Examples

### cURL

```bash
curl -X PUT "http://localhost:8080/api/roles" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"id":10,"name":"Tech Lead","description":"Updated description"}'
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/roles", {
  method: "PUT",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    id: 10,
    name: "Tech Lead",
    description: "Updated description",
  }),
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.put("http://localhost:8080/api/roles", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"id":10, "name":"Tech Lead", "description":"Updated description"}).json())
```

## Notes

- Không cho phép đổi code role.
