# GET /api/appointments/service-center/{serviceCenterId}

## Mô tả

Danh sách lịch hẹn theo service center, có thể filter theo ngày và trạng thái.

## Endpoint

```
GET /api/appointments/service-center/{serviceCenterId}?date=2024-02-01&status=scheduled
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter       | Type | Required | Description       |
| --------------- | ---- | -------- | ----------------- |
| serviceCenterId | long | Yes      | ID service center |

### Query Parameters

| Parameter | Type   | Required | Description                                          |
| --------- | ------ | -------- | ---------------------------------------------------- |
| date      | string | No       | Ngày (YYYY-MM-DD)                                    |
| status    | string | No       | Trạng thái (scheduled/confirmed/cancelled/completed) |

## Response

### Success (200)

```json
[
  {
    "appointmentId": 3001,
    "campaignId": 10,
    "vin": "1HGBH41JXMN109186",
    "date": "2024-02-01",
    "time": "09:30",
    "status": "scheduled"
  }
]
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem appointments

## Test Cases

1. Có dữ liệu: 200 OK
2. Không có dữ liệu: 200 OK (mảng rỗng)
3. Không token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/appointments/service-center/2?date=2024-02-01&status=scheduled" -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/service-center/2?date=2024-02-01&status=scheduled",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/appointments/service-center/2", headers={"Authorization":"Bearer <token>"}, params={"date":"2024-02-01","status":"scheduled"}).json())
```

## Notes

- Dùng để điều phối lịch trong ngày của service center.
