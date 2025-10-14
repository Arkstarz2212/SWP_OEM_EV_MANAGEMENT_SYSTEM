# POST /api/appointments/{appointmentId}/complete

## Mô tả

Hoàn tất lịch hẹn với ghi chú và kỹ thuật viên thực hiện.

## Endpoint

```
POST /api/appointments/{appointmentId}/complete
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
  "completionNotes": "Completed battery inspection and campaign tasks",
  "technicianId": "tech_005"
}
```

### Parameters

| Field           | Type   | Required | Description      |
| --------------- | ------ | -------- | ---------------- |
| completionNotes | string | No       | Ghi chú hoàn tất |
| technicianId    | string | Yes      | ID kỹ thuật viên |

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

1. Hoàn tất hợp lệ: 200 OK
2. Appointment không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/appointments/3001/complete \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"completionNotes":"Completed battery inspection and campaign tasks","technicianId":"tech_005"}'
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/3001/complete",
  {
    method: "POST",
    headers: {
      Authorization: "Bearer <token>",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      completionNotes: "Completed battery inspection and campaign tasks",
      technicianId: "tech_005",
    }),
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post("http://localhost:8080/api/appointments/3001/complete", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"completionNotes":"Completed battery inspection and campaign tasks","technicianId":"tech_005"}).json())
```

## Notes

- Tự động cập nhật status thành `completed`.
