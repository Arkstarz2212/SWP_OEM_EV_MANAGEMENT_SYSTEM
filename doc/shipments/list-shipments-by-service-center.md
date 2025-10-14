# GET /api/shipments/service-center/{serviceCenterId}

## Mô tả

Danh sách lô hàng theo service center với filter trạng thái tùy chọn.

## Endpoint

```
GET /api/shipments/service-center/{serviceCenterId}?status=pending
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

| Parameter | Type   | Required | Description         |
| --------- | ------ | -------- | ------------------- |
| status    | string | No       | Lọc theo trạng thái |

## Response

### Success (200)

```json
[
  {
    "shipmentId": 1001,
    "trackingNumber": "1Z999AA1234567890",
    "status": "pending"
  }
]
```

### Errors

- 404: Service center not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem shipments

## Test Cases

1. Có shipments: 200 OK (mảng ≥ 1)
2. Không có shipments: 200 OK (mảng rỗng)
3. Service center không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/shipments/service-center/2?status=pending" -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/shipments/service-center/2?status=pending",
  {
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get("http://localhost:8080/api/shipments/service-center/2", headers={"Authorization":"Bearer <token>"}, params={"status":"pending"}).json())
```

## Notes

- Dùng cho dashboard của service center.
