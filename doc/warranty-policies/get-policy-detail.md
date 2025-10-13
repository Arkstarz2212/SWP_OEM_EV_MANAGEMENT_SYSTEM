# GET /api/warranty-policies/{id}

## Mô tả

Lấy chi tiết chính sách bảo hành theo ID.

## Endpoint

```
GET /api/warranty-policies/{id}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| id        | long | Yes      | ID policy   |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "model": "Model 3",
  "componentCategory": "BATTERY",
  "monthsCoverage": 24,
  "kmCoverage": 50000,
  "notes": "OEM standard warranty"
}
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "Policy not found",
  "path": "/api/warranty-policies/999",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin/EVM_Staff hoặc người dùng có quyền xem

## Test Cases

1. Policy tồn tại: 200 OK
2. Policy không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không đủ quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-policies/1" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/warranty-policies/1", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-policies/1", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Phục vụ hiển thị chi tiết policy.
