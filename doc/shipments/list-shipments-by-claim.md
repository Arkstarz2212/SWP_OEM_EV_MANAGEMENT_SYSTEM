# GET /api/shipments/claim/{claimId}

## Mô tả

Danh sách lô hàng thuộc một warranty claim.

## Endpoint

```
GET /api/shipments/claim/{claimId}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description       |
| --------- | ---- | -------- | ----------------- |
| claimId   | long | Yes      | ID warranty claim |

## Response

### Success (200)

```json
[
  {
    "shipmentId": 1001,
    "trackingNumber": "1Z999AA1234567890",
    "status": "pending"
  }
]
```

### Errors

- 404: Claim not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem shipments

## Test Cases

1. Claim có shipments: 200 OK (mảng ≥ 1)
2. Claim không có shipments: 200 OK (mảng rỗng)
3. Claim không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/shipments/claim/2001 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/shipments/claim/2001", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/shipments/claim/2001", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Hữu ích để theo dõi vận chuyển theo claim.
