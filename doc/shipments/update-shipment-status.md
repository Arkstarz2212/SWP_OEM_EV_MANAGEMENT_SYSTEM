# PUT /api/shipments/{shipmentId}/status

## Mô tả

Cập nhật trạng thái của lô hàng với ghi chú tùy chọn.

## Endpoint

```
PUT /api/shipments/{shipmentId}/status?status=shipped&notes=Picked%20up
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

### Query Parameters

| Parameter | Type   | Required | Description                                   |
| --------- | ------ | -------- | --------------------------------------------- |
| status    | string | Yes      | Trạng thái mới (shipped/in_transit/delivered) |
| notes     | string | No       | Ghi chú cập nhật                              |

## Response

### Success (200)

```json
{ "success": true }
```

### Errors

- 400: Invalid status
- 404: Shipment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền cập nhật shipments

## Test Cases

1. Cập nhật hợp lệ: 200 OK `{ success: true }`
2. Status không hợp lệ: 400 Bad Request
3. Shipment không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X PUT "http://localhost:8080/api/shipments/1001/status?status=shipped&notes=Picked%20up" \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/shipments/1001/status?status=shipped&notes=Picked%20up",
  { method: "PUT", headers: { Authorization: "Bearer <token>" } }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.put(
  "http://localhost:8080/api/shipments/1001/status",
  headers={"Authorization":"Bearer <token>"},
  params={"status":"shipped","notes":"Picked up"}
).json())
```

## Notes

- Nên lưu lịch sử trạng thái để theo dõi.
