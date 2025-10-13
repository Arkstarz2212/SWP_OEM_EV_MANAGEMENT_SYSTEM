# GET /api/warranty-policies/model/{model}

## Mô tả

Danh sách các chính sách bảo hành áp dụng cho một model xe.

## Endpoint

```
GET /api/warranty-policies/model/{model}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type   | Required | Description |
| --------- | ------ | -------- | ----------- |
| model     | string | Yes      | Model xe    |

## Response

### Success Response (200 OK)

```json
[
  {
    "id": 1,
    "model": "Model 3",
    "componentCategory": "BATTERY",
    "monthsCoverage": 24,
    "kmCoverage": 50000,
    "notes": "OEM standard warranty"
  }
]
```

### Error Responses

#### 404 Not Found

```json
{
  "error": "No policies found",
  "path": "/api/warranty-policies/model/UNKNOWN",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem policies theo model

## Test Cases

1. Model có policy: 200 OK (mảng ≥ 1)
2. Model không có policy: 200 OK (mảng rỗng) hoặc 404 tùy implement
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không đủ quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-policies/model/Model%203" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-policies/model/Model%203",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-policies/model/Model 3", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Trả về các policy ACTIVE hoặc theo logic service.
