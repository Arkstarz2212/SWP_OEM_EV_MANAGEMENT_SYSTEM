# GET /api/warranty-certificates/vin/{vin}

## Mô tả

Lấy chứng chỉ bảo hành theo VIN.

## Endpoint

```
GET /api/warranty-certificates/vin/{vin}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type   | Required | Description |
| --------- | ------ | -------- | ----------- |
| vin       | string | Yes      | VIN của xe  |

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

- 404: Certificate not found for VIN

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem chứng chỉ

## Test Cases

1. VIN có chứng chỉ: 200 OK
2. VIN không có chứng chỉ: 404 Not Found
3. VIN sai định dạng: 400 Bad Request
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/warranty-certificates/vin/1HGBH41JXMN109186 \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/vin/1HGBH41JXMN109186",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-certificates/vin/1HGBH41JXMN109186", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Hữu ích để tra cứu nhanh theo VIN.
