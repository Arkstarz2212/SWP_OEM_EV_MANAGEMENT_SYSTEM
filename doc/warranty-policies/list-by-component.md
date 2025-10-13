# GET /api/warranty-policies/component/{component}

## Mô tả

Danh sách các chính sách bảo hành áp dụng cho một danh mục component.

## Endpoint

```
GET /api/warranty-policies/component/{component}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type   | Required | Description        |
| --------- | ------ | -------- | ------------------ |
| component | string | Yes      | Danh mục component |

## Response

### Success Response (200 OK)

```json
[
  {
    "id": 2,
    "model": "Model 3",
    "componentCategory": "ENGINE",
    "monthsCoverage": 18,
    "kmCoverage": 30000,
    "notes": "Engine warranty"
  }
]
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "No policies found",
  "path": "/api/warranty-policies/component/UNKNOWN",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem policies theo component

## Test Cases

1. Component có policy: 200 OK (mảng ≥ 1)
2. Component không có policy: 200 OK (mảng rỗng) hoặc 404 tùy implement
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không đủ quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-policies/component/BATTERY" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-policies/component/BATTERY",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-policies/component/BATTERY", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Trả về các policy ACTIVE hoặc theo logic service.
