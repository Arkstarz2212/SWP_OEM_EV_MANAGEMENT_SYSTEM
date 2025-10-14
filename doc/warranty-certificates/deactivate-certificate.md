# PUT /api/warranty-certificates/{certificateId}/deactivate

## Mô tả

Hủy kích hoạt chứng chỉ bảo hành với lý do tùy chọn.

## Endpoint

```
PUT /api/warranty-certificates/{certificateId}/deactivate?reason=...
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter     | Type | Required | Description           |
| ------------- | ---- | -------- | --------------------- |
| certificateId | long | Yes      | ID chứng chỉ bảo hành |

### Query Parameters

| Parameter | Type   | Required | Description         |
| --------- | ------ | -------- | ------------------- |
| reason    | string | No       | Lý do hủy kích hoạt |

## Response

### Success (200)

```json
{
  "certificateId": 1001,
  "status": "INACTIVE",
  "reason": "Expired policy"
}
```

### Errors

- 404: Certificate not found
- 400: Invalid state transition

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin/EVM_Staff

## Test Cases

1. Hủy kích hoạt thành công: 200 OK
2. Đã INACTIVE từ trước: 400 Bad Request
3. Không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X PUT "http://localhost:8080/api/warranty-certificates/1001/deactivate?reason=Expired%20policy" \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/1001/deactivate?reason=Expired%20policy",
  { method: "PUT", headers: { Authorization: "Bearer <token>" } }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.put(
  "http://localhost:8080/api/warranty-certificates/1001/deactivate",
  headers={"Authorization":"Bearer <token>"}, params={"reason":"Expired policy"}
).json())
```

## Notes

- Nên lưu audit log với lý do hủy.
