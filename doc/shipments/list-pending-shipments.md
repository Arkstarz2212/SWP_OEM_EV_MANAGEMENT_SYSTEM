# GET /api/shipments/pending

## Mô tả

Danh sách lô hàng đang ở trạng thái pending.

## Endpoint

```
GET /api/shipments/pending
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

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

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem shipments

## Test Cases

1. Có pending shipments: 200 OK
2. Không có: 200 OK (mảng rỗng)
3. Không token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/shipments/pending -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/shipments/pending", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/shipments/pending", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Dùng cho bảng công việc logistics.
