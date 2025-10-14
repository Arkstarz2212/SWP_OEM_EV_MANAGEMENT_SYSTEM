# PUT /api/warranty-certificates/{certificateId}/activate

## Mô tả

Kích hoạt chứng chỉ bảo hành.

## Endpoint

```
PUT /api/warranty-certificates/{certificateId}/activate
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

## Response

### Success (200)

```json
{
  "certificateId": 1001,
  "status": "ACTIVE"
}
```

### Errors

- 404: Certificate not found
- 400: Invalid state transition

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin/EVM_Staff

## Test Cases

1. Kích hoạt thành công: 200 OK
2. Đã ACTIVE từ trước: 400 Bad Request
3. Không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X PUT http://localhost:8080/api/warranty-certificates/1001/activate \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/1001/activate",
  {
    method: "PUT",
    headers: { Authorization: "Bearer <token>" },
  }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.put("http://localhost:8080/api/warranty-certificates/1001/activate", headers={"Authorization":"Bearer <token>"}).json())
```

## Notes

- Chỉ kích hoạt khi đủ điều kiện (ví dụ đã tạo, chưa hủy kích hoạt).
