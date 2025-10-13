# GET /api/shipments/{shipmentId}

## Mô tả

Lấy chi tiết một lô hàng theo ID.

## Endpoint

```
GET /api/shipments/{shipmentId}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter  | Type | Required | Description |
| ---------- | ---- | -------- | ----------- |
| shipmentId | long | Yes      | ID lô hàng  |

## Response

### Success (200)

```json
{
  "shipmentId": 1001,
  "claimId": 2001,
  "serviceCenterId": 2,
  "partNumbers": ["BAT001", "MOTOR002"],
  "trackingNumber": "1Z999AA1234567890",
  "status": "pending",
  "history": [{ "status": "pending", "timestamp": "2024-01-10T08:00:00Z" }]
}
```

### Errors

- 404: Shipment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem shipments

## Test Cases

1. Tồn tại: 200 OK
2. Không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/shipments/1001 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/shipments/1001", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/shipments/1001", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Bao gồm `history` nếu có để theo dõi trạng thái.
