# POST /api/appointments/{appointmentId}/cancel

## Mô tả

Hủy lịch hẹn với lý do tùy chọn.

## Endpoint

```
POST /api/appointments/{appointmentId}/cancel?reason=Customer%20cancelled
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter     | Type | Required | Description |
| ------------- | ---- | -------- | ----------- |
| appointmentId | long | Yes      | ID lịch hẹn |

### Query Parameters

| Parameter | Type   | Required | Description |
| --------- | ------ | -------- | ----------- |
| reason    | string | No       | Lý do       |

## Response

### Success (200)

```json
{ "success": true }
```

### Errors

- 404: Appointment not found
- 400: Invalid state transition

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền cập nhật appointments

## Test Cases

1. Hủy hợp lệ: 200 OK
2. Appointment không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST "http://localhost:8080/api/appointments/3001/cancel?reason=Customer%20cancelled" -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/3001/cancel?reason=Customer%20cancelled",
  {
    method: "POST",
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post("http://localhost:8080/api/appointments/3001/cancel", headers={"Authorization":"Bearer <token>"}, params={"reason":"Customer cancelled"}).json())
```

## Notes

- Có thể gửi SMS/email xác nhận hủy cho khách.
