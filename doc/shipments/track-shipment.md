# POST /api/shipments/{shipmentId}/track

## Mô tả

Thêm bản ghi tracking cho lô hàng (event theo thời gian).

## Endpoint

```
POST /api/shipments/{shipmentId}/track
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter  | Type | Required | Description |
| ---------- | ---- | -------- | ----------- |
| shipmentId | long | Yes      | ID lô hàng  |

### Body

```json
{
  "event": "Package scanned at hub",
  "timestamp": "2024-01-10T10:00:00Z"
}
```

### Parameters

| Field     | Type   | Required | Description            |
| --------- | ------ | -------- | ---------------------- |
| event     | string | Yes      | Mô tả sự kiện tracking |
| timestamp | string | Yes      | ISO-8601 timestamp     |

## Response

### Success (200)

```json
{ "success": true }
```

### Errors

- 400: Invalid data
- 404: Shipment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền cập nhật shipments

## Test Cases

1. Tracking hợp lệ: 200 OK
2. Thiếu event: 400 Bad Request
3. Shipment không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/shipments/1001/track \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"event":"Package scanned at hub","timestamp":"2024-01-10T10:00:00Z"}'
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/shipments/1001/track", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    event: "Package scanned at hub",
    timestamp: "2024-01-10T10:00:00Z",
  }),
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post("http://localhost:8080/api/shipments/1001/track", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"event":"Package scanned at hub","timestamp":"2024-01-10T10:00:00Z"}).json())
```

## Notes

- Dùng để theo dõi chi tiết hành trình lô hàng.
