# GET /api/appointments/customer/{vin}

## Mô tả

Danh sách lịch hẹn theo VIN (khách hàng/xe).

## Endpoint

```
GET /api/appointments/customer/{vin}
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type   | Required | Description |
| --------- | ------ | -------- | ----------- |
| vin       | string | Yes      | VIN xe      |

## Response

### Success (200)

```json
[
  {
    "appointmentId": 3001,
    "serviceCenterId": 2,
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
curl -X GET http://localhost:8080/api/appointments/customer/1HGBH41JXMN109186 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/customer/1HGBH41JXMN109186",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/appointments/customer/1HGBH41JXMN109186", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Hữu ích cho lịch sử hẹn theo xe/khách hàng.
