# GET /api/warranty-certificates/expiring

## Mô tả

Danh sách chứng chỉ sắp hết hạn trong N ngày tới.

## Endpoint

```
GET /api/warranty-certificates/expiring?days=30
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter | Type | Required | Description                               |
| --------- | ---- | -------- | ----------------------------------------- |
| days      | int  | No       | Số ngày tới hạn (mặc định 30, tối đa 180) |

## Response

### Success (200)

```json
[
  {
    "certificateId": 1001,
    "vin": "1HGBH41JXMN109186",
    "expiresAt": "2024-02-01",
    "daysLeft": 15,
    "status": "ACTIVE"
  }
]
```

### Errors

- 400: Invalid days

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin/EVM_Staff

## Test Cases

1. Mặc định 30 ngày: 200 OK
2. days=7: 200 OK
3. days quá lớn (>180): 400 Bad Request
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-certificates/expiring?days=30" \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/expiring?days=30",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/warranty-certificates/expiring", headers={"Authorization":"Bearer <token>"}, params={"days":30}).json())
```

## Notes

- Dùng để chủ động nhắc gia hạn cho khách hàng.
