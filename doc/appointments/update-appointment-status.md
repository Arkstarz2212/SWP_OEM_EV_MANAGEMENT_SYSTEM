# PUT /api/appointments/{appointmentId}/status

## Mô tả

Cập nhật trạng thái lịch hẹn với ghi chú tùy chọn.

## Endpoint

```
PUT /api/appointments/{appointmentId}/status?status=confirmed&notes=Confirmed%20by%20phone
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

| Parameter | Type   | Required | Description                                              |
| --------- | ------ | -------- | -------------------------------------------------------- |
| status    | string | Yes      | Trạng thái mới (scheduled/confirmed/cancelled/completed) |
| notes     | string | No       | Ghi chú cập nhật                                         |

## Response

### Success (200)

```json
{ "success": true }
```

### Errors

- 400: Invalid status
- 404: Appointment not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền cập nhật appointments

## Test Cases

1. Cập nhật hợp lệ: 200 OK `{ success: true }`
2. Status không hợp lệ: 400 Bad Request
3. Appointment không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X PUT "http://localhost:8080/api/appointments/3001/status?status=confirmed&notes=Confirmed%20by%20phone" \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/appointments/3001/status?status=confirmed&notes=Confirmed%20by%20phone",
  { method: "PUT", headers: { Authorization: "Bearer <token>" } }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.put(
  "http://localhost:8080/api/appointments/3001/status",
  headers={"Authorization":"Bearer <token>"},
  params={"status":"confirmed","notes":"Confirmed by phone"}
).json())
```

## Notes

- Lưu lịch sử trạng thái để phục vụ báo cáo.
