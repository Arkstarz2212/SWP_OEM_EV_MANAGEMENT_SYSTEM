# GET /api/appointments/campaign/{campaignId}

## Mô tả

Danh sách lịch hẹn theo campaign, có thể filter theo service center.

## Endpoint

```
GET /api/appointments/campaign/{campaignId}?serviceCenterId=2
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter  | Type | Required | Description |
| ---------- | ---- | -------- | ----------- |
| campaignId | long | Yes      | ID campaign |

### Query Parameters

| Parameter       | Type | Required | Description             |
| --------------- | ---- | -------- | ----------------------- |
| serviceCenterId | long | No       | Lọc theo service center |

## Response

### Success (200)

```json
[
  {
    "appointmentId": 3001,
    "serviceCenterId": 2,
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
4. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/appointments/campaign/10?serviceCenterId=2" -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/campaign/10?serviceCenterId=2",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/appointments/campaign/10", headers={"Authorization":"Bearer <token>"}, params={"serviceCenterId":2}).json())
```

## Notes

- Phục vụ tổng hợp lịch hẹn theo chiến dịch.
