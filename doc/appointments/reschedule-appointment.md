# POST /api/appointments/{appointmentId}/reschedule

## Mô tả

Đổi lịch hẹn sang ngày/giờ mới với lý do.

## Endpoint

```
POST /api/appointments/{appointmentId}/reschedule
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter     | Type | Required | Description |
| ------------- | ---- | -------- | ----------- |
| appointmentId | long | Yes      | ID lịch hẹn |

### Body

```json
{
  "newDate": "2024-02-03",
  "newTime": "10:30",
  "reason": "Customer request"
}
```

### Parameters

| Field   | Type   | Required | Description           |
| ------- | ------ | -------- | --------------------- |
| newDate | string | Yes      | Ngày mới (YYYY-MM-DD) |
| newTime | string | Yes      | Giờ mới (HH:mm)       |
| reason  | string | No       | Lý do                 |

## Response

### Success (200)

```json
{ "success": true }
```

### Errors

- 400: Invalid date/time
- 404: Appointment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền cập nhật appointments

## Test Cases

1. Reschedule hợp lệ: 200 OK
2. Ngày/giờ không hợp lệ: 400 Bad Request
3. Appointment không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/appointments/3001/reschedule \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"newDate":"2024-02-03","newTime":"10:30","reason":"Customer request"}'
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/3001/reschedule",
  {
    method: "POST",
    headers: {
      Authorization: "Bearer <token>",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      newDate: "2024-02-03",
      newTime: "10:30",
      reason: "Customer request",
    }),
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post("http://localhost:8080/api/appointments/3001/reschedule", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"newDate":"2024-02-03","newTime":"10:30","reason":"Customer request"}).json())
```

## Notes

- Nên kiểm tra xung đột với các lịch hẹn khác.
