# POST /api/roles

## Mô tả

Tạo role mới với code, name và description.

## Endpoint

```
POST /api/roles
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
  "code": "TECH_LEAD",
  "name": "Technical Lead",
  "description": "Lead technician role"
}
```

### Parameters

| Field       | Type   | Required | Description      |
| ----------- | ------ | -------- | ---------------- |
| code        | string | Yes      | Mã role duy nhất |
| name        | string | Yes      | Tên role         |
| description | string | No       | Mô tả            |

## Response

### Success Response (200 OK)

```json
{
  "id": 10,
  "code": "TECH_LEAD",
  "name": "Technical Lead",
  "description": "Lead technician role"
}
```

### Error Responses

#### 400 Bad Request - Trùng code

```json
{
  "error": "Role code already exists",
  "path": "/api/roles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Bắt buộc: Đăng nhập (`POST /api/auth/login`)
- Quyền: Admin

## Test Cases

1. Tạo role hợp lệ (200)
2. Code trùng (400)
3. Thiếu field bắt buộc (400)
4. Thiếu token (401)
5. Không đủ quyền (403)

## Usage Examples

### cURL

```bash
curl -X POST "http://localhost:8080/api/roles" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"code":"TECH_LEAD","name":"Technical Lead","description":"Lead technician role"}'
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/roles", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    code: "TECH_LEAD",
    name: "Technical Lead",
    description: "Lead technician role",
  }),
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.post(
  "http://localhost:8080/api/roles",
  headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"},
  json={"code":"TECH_LEAD","name":"Technical Lead","description":"Lead technician role"}
).json())
```

## Notes

- Code nên uppercase và không có khoảng trắng.
