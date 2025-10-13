# GET /api/appointments/availability

## Mô tả

Kiểm tra lịch trống của service center theo ngày và (tùy chọn) time slot.

## Endpoint

```
GET /api/appointments/availability?serviceCenterId=2&date=2024-02-01&timeSlot=morning
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter       | Type   | Required | Description                           |
| --------------- | ------ | -------- | ------------------------------------- |
| serviceCenterId | long   | Yes      | ID service center                     |
| date            | string | Yes      | Ngày (YYYY-MM-DD)                     |
| timeSlot        | string | No       | Khung giờ (morning/afternoon/evening) |

## Response

### Success (200)

```json
[
  { "time": "09:00", "available": true },
  { "time": "09:30", "available": false }
]
```

### Errors

- 404: Service center not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem availability

## Test Cases

1. Có availability: 200 OK
2. Không có availability: 200 OK (mảng rỗng)
3. Không token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/appointments/availability?serviceCenterId=2&date=2024-02-01&timeSlot=morning" -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/availability?serviceCenterId=2&date=2024-02-01&timeSlot=morning",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/appointments/availability", headers={"Authorization":"Bearer <token>"}, params={"serviceCenterId":2,"date":"2024-02-01","timeSlot":"morning"}).json())
```

## Notes

- Có thể kết hợp với reschedule để đề xuất slot trống.
