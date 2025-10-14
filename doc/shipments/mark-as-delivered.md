# POST /api/shipments/{shipmentId}/deliver

## Mô tả

Đánh dấu lô hàng đã giao với thông tin người giao và ghi chú.

## Endpoint

```
POST /api/shipments/{shipmentId}/deliver
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
  "deliveredBy": "John Smith",
  "deliveryNotes": "Delivered to service center reception"
}
```

### Parameters

| Field         | Type   | Required | Description      |
| ------------- | ------ | -------- | ---------------- |
| deliveredBy   | string | Yes      | Người giao       |
| deliveryNotes | string | No       | Ghi chú khi giao |

## Response

### Success (200)

```json
{ "success": true }
```

### Errors

- 404: Shipment not found
- 400: Invalid state transition

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền cập nhật shipments

## Test Cases

1. Giao thành công: 200 OK
2. Shipment đã delivered: 400 Bad Request
3. Shipment không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/shipments/1001/deliver \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"deliveredBy":"John Smith","deliveryNotes":"Delivered to service center reception"}'
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/shipments/1001/deliver", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    deliveredBy: "John Smith",
    deliveryNotes: "Delivered to service center reception",
  }),
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post("http://localhost:8080/api/shipments/1001/deliver", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"deliveredBy":"John Smith","deliveryNotes":"Delivered to service center reception"}).json())
```

## Notes

- Cập nhật trạng thái shipment thành `delivered` và lưu thời gian giao.
