# GET /api/warranty-policies

## Mô tả

Danh sách các chính sách bảo hành, có thể filter theo `oemId` và `active`.

## Endpoint

```
GET /api/warranty-policies?oemId=1&active=true
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter | Type    | Required | Description                |
| --------- | ------- | -------- | -------------------------- |
| oemId     | long    | No       | Lọc theo OEM ID            |
| active    | boolean | No       | Chỉ lấy policy đang active |

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

#### 400 Bad Request

```json
{
  "error": "Invalid filter"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem policies

## Test Cases

1. Không filter: 200 OK (trả về active mặc định)
2. Filter `oemId`: 200 OK
3. Filter `active=false`: 200 OK (có thể rỗng)
4. Không token: 401 Unauthorized
5. Filter không hợp lệ: 400 Bad Request

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-policies?oemId=1&active=true" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-policies?oemId=1&active=true",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-policies", headers={"Authorization":"Bearer <token>"}, params={"oemId":1, "active": True}).json())
```

## Notes

- Nếu không có `oemId`, controller trả về active policies của OEM mặc định.
