# POST /api/warranty-certificates/{certificateId}/extend

## Mô tả

Gia hạn chứng chỉ bảo hành thêm số tháng.

## Endpoint

```
POST /api/warranty-certificates/{certificateId}/extend
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter     | Type | Required | Description           |
| ------------- | ---- | -------- | --------------------- |
| certificateId | long | Yes      | ID chứng chỉ bảo hành |

### Body

```json
{
  "additionalMonths": 12,
  "reason": "Goodwill extension"
}
```

### Parameters

| Field            | Type   | Required | Description      |
| ---------------- | ------ | -------- | ---------------- |
| additionalMonths | int    | Yes      | Số tháng gia hạn |
| reason           | string | No       | Lý do gia hạn    |

## Response

### Success (200)

```json
{
  "certificateId": 1001,
  "extended": true,
  "additionalMonths": 12
}
```

### Errors

- 404: Certificate not found
- 400: Invalid months

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin/EVM_Staff

## Test Cases

1. Gia hạn hợp lệ: 200 OK
2. additionalMonths <= 0: 400 Bad Request
3. Không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/warranty-certificates/1001/extend \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"additionalMonths":12,"reason":"Goodwill extension"}'
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/1001/extend",
  {
    method: "POST",
    headers: {
      Authorization: "Bearer <token>",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      additionalMonths: 12,
      reason: "Goodwill extension",
    }),
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post(
  "http://localhost:8080/api/warranty-certificates/1001/extend",
  headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"},
  json={"additionalMonths":12,"reason":"Goodwill extension"}
).json())
```

## Notes

- Cập nhật ngày hết hạn dựa trên `additionalMonths`.
