# POST /api/warranty-certificates

## Mô tả

Tạo chứng chỉ bảo hành cho xe theo `VIN` và `policyId`.

## Endpoint

```
POST /api/warranty-certificates
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
  "vin": "1HGBH41JXMN109186",
  "policyId": 1,
  "startDate": "2024-01-01"
}
```

### Parameters

| Field     | Type   | Required | Description               |
| --------- | ------ | -------- | ------------------------- |
| vin       | string | Yes      | VIN của xe                |
| policyId  | long   | Yes      | ID policy bảo hành        |
| startDate | string | Yes      | Ngày bắt đầu (YYYY-MM-DD) |

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

- 400: Invalid request data
- 404: Policy not found / VIN invalid

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin hoặc EVM_Staff

## Test Cases

1. Tạo hợp lệ: 200 OK trả về certificateId
2. Thiếu `vin`: 400 Bad Request
3. `policyId` không tồn tại: 404 Not Found
4. `startDate` sai format: 400 Bad Request
5. Không token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/warranty-certificates \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"vin":"1HGBH41JXMN109186","policyId":1,"startDate":"2024-01-01"}'
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/warranty-certificates", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    vin: "1HGBH41JXMN109186",
    policyId: 1,
    startDate: "2024-01-01",
  }),
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post(
  "http://localhost:8080/api/warranty-certificates",
  headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"},
  json={"vin":"1HGBH41JXMN109186","policyId":1,"startDate":"2024-01-01"}
).json())
```

## Notes

- `startDate` dùng để tính thời hạn bảo hành.
