# GET /api/shipments/tracking/{trackingNumber}

## Mô tả

Tra cứu lô hàng bằng tracking number.

## Endpoint

```
GET /api/shipments/tracking/{trackingNumber}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter      | Type   | Required | Description |
| -------------- | ------ | -------- | ----------- |
| trackingNumber | string | Yes      | Số tracking |

## Response

### Success (200)

```json
{
  "shipmentId": 1001,
  "trackingNumber": "1Z999AA1234567890",
  "status": "in_transit"
}
```

### Errors

- 404: Shipment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem shipments

## Test Cases

1. Tracking hợp lệ: 200 OK
2. Tracking không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/shipments/tracking/1Z999AA1234567890 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/shipments/tracking/1Z999AA1234567890",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/shipments/tracking/1Z999AA1234567890", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Hỗ trợ tra cứu nhanh cho CSKH.
