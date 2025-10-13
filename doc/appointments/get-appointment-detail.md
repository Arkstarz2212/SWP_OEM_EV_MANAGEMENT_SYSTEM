# GET /api/appointments/{appointmentId}

## Mô tả

Lấy chi tiết lịch hẹn theo ID.

## Endpoint

```
GET /api/appointments/{appointmentId}
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

## Response

### Success (200)

```json
{
  "appointmentId": 3001,
  "campaignId": 10,
  "serviceCenterId": 2,
  "vin": "1HGBH41JXMN109186",
  "appointmentDate": "2024-02-01",
  "appointmentTime": "09:30",
  "customerName": "Nguyen Van A",
  "customerPhone": "+84-912345678",
  "status": "scheduled"
}
```

### Errors

- 404: Appointment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem appointments

## Test Cases

1. Tồn tại: 200 OK
2. Không tồn tại: 404 Not Found
3. Không token: 401 Unauthorized
4. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET http://localhost:8080/api/appointments/3001 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/appointments/3001", {
  headers: { Authorization: "Bearer <token>" },
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/appointments/3001", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Dùng để hiển thị chi tiết lịch hẹn.
