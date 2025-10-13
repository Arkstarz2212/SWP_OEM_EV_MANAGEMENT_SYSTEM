# GET /api/roles

## Mô tả

Lấy danh sách tất cả roles trong hệ thống.

## Endpoint

```
GET /api/roles
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

## Response

### Success Response (200 OK)

```json
[
  {
    "id": 1,
    "code": "ADMIN",
    "name": "Administrator",
    "description": "System administrator with full permissions"
  },
  {
    "id": 2,
    "code": "EVM_Staff",
    "name": "OEM Staff",
    "description": "OEM staff with OEM-scoped permissions"
  }
]
```

### Error Responses

#### 401 Unauthorized

```json
{
  "error": "Unauthorized",
  "path": "/api/roles",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Bắt buộc: Đăng nhập với token hợp lệ (`POST /api/auth/login`)
- Quyền: Admin hoặc có quyền xem danh sách roles

## Test Cases

1. Lấy tất cả roles (200): Trả về mảng roles ≥ 1
2. Không có token (401): Unauthorized
3. Token hết hạn (401): Unauthorized
4. Không đủ quyền (403): Forbidden
5. Lỗi hệ thống (500): Internal Server Error

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/roles" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/roles", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/roles", headers={"Authorization": "Bearer <token>"}).json())
```

## Notes

- Dùng cho trang quản trị phân quyền.
- Trả về danh sách đầy đủ để hiển thị và chọn.
