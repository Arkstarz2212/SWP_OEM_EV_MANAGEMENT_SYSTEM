# GET /api/warranty-certificates/{certificateId}

## Mô tả

Lấy chi tiết chứng chỉ bảo hành theo `certificateId`.

## Endpoint

```
GET /api/warranty-certificates/{certificateId}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter     | Type | Required | Description           |
| ------------- | ---- | -------- | --------------------- |
| certificateId | long | Yes      | ID chứng chỉ bảo hành |

## Response

### Success (200)

```json
{
  "certificateId": 1001,
  "vin": "1HGBH41JXMN109186",
  "policyId": 1,
  "startDate": "2024-01-01",
  "status": "ACTIVE"
}
```

### Errors

- 404: Certificate not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem chứng chỉ

## Test Cases

1. Tồn tại: 200 OK
2. Không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Token hết hạn: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/warranty-certificates/1001 \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/1001",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-certificates/1001", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Dùng để hiển thị chi tiết chứng chỉ cho VIN.
