# DELETE /api/roles/{id}

## Mô tả

Xóa role theo ID (không thể hoàn tác).

## Endpoint

```
DELETE /api/roles/{id}
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

### Success Response (204 No Content)

Không có body trả về.

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
- Quyền: Admin

## Test Cases

1. Xóa role tồn tại (204)
2. Role không tồn tại (404)
3. Thiếu token (401)
4. Không đủ quyền (403)
5. Role đang được gán cho user (409 Conflict – nếu hệ thống chặn xóa khi còn ràng buộc)

## Usage Examples

### cURL

```bash
curl -X DELETE "http://localhost:8080/api/roles/10" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/roles/10", {
  method: "DELETE",
  headers: { Authorization: "Bearer <token>" },
});
console.log(res.status); // 204
```

### Python (Requests)

```python
import requests
r = requests.delete("http://localhost:8080/api/roles/10", headers={"Authorization": "Bearer <token>"})
print(r.status_code)
```

## Notes

- Nên kiểm tra ràng buộc (users/permissions) trước khi xóa.
