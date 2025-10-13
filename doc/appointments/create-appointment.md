# POST /api/appointments

## Mô tả

Tạo lịch hẹn cho campaign tại service center cho một xe theo VIN.

## Endpoint

```
POST /api/appointments
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Body

```json
{
  "campaignId": 10,
  "serviceCenterId": 2,
  "vin": "1HGBH41JXMN109186",
  "appointmentDate": "2024-02-01",
  "appointmentTime": "09:30",
  "customerName": "Nguyen Van A",
  "customerPhone": "+84-912345678",
  "notes": "Customer requests loaner car"
}
```

### Parameters

| Field           | Type   | Required | Description           |
| --------------- | ------ | -------- | --------------------- |
| campaignId      | long   | Yes      | ID campaign           |
| serviceCenterId | long   | Yes      | ID service center     |
| vin             | string | Yes      | VIN xe                |
| appointmentDate | string | Yes      | Ngày hẹn (YYYY-MM-DD) |
| appointmentTime | string | Yes      | Giờ hẹn (HH:mm, 24h)  |
| customerName    | string | Yes      | Tên khách hàng        |
| customerPhone   | string | Yes      | SĐT khách hàng        |
| notes           | string | No       | Ghi chú               |

## Response

### Success (200)

```json
{
  "appointmentId": 3001,
  "status": "scheduled"
}
```

### Errors

- 400: Invalid request data
- 404: Campaign/Service center/VIN not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: SC_Staff/EVM_Staff/Admin

## Test Cases

1. Tạo hợp lệ: 200 OK trả về appointmentId
2. Thiếu thời gian: 400 Bad Request
3. VIN không hợp lệ: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/appointments \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"campaignId":10,"serviceCenterId":2,"vin":"1HGBH41JXMN109186","appointmentDate":"2024-02-01","appointmentTime":"09:30","customerName":"Nguyen Van A","customerPhone":"+84-912345678","notes":"Customer requests loaner car"}'
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/appointments", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    campaignId: 10,
    serviceCenterId: 2,
    vin: "1HGBH41JXMN109186",
    appointmentDate: "2024-02-01",
    appointmentTime: "09:30",
    customerName: "Nguyen Van A",
    customerPhone: "+84-912345678",
    notes: "Customer requests loaner car",
  }),
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post(
  "http://localhost:8080/api/appointments",
  headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"},
  json={"campaignId":10,"serviceCenterId":2,"vin":"1HGBH41JXMN109186","appointmentDate":"2024-02-01","appointmentTime":"09:30","customerName":"Nguyen Van A","customerPhone":"+84-912345678","notes":"Customer requests loaner car"}
).json())
```

## Notes

- Có thể gửi notification cho khách hàng sau khi đặt lịch.
