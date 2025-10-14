# GET /api/warranty-certificates/validate/{vin}

## Mô tả

Kiểm tra chứng chỉ bảo hành của xe có cover `componentType` hay không.

## Endpoint

```
GET /api/warranty-certificates/validate/{vin}?componentType=BATTERY
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type   | Required | Description |
| --------- | ------ | -------- | ----------- |
| vin       | string | Yes      | VIN của xe  |

### Query Parameters

| Parameter     | Type   | Required | Description                 |
| ------------- | ------ | -------- | --------------------------- |
| componentType | string | Yes      | Loại component cần kiểm tra |

## Response

### Success (200)

```json
{
  "valid": true
}
```

### Errors

- 400: Missing componentType
- 404: Certificate not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền kiểm tra bảo hành

## Test Cases

1. Hợp lệ (còn hạn, cover component): 200 `{ "valid": true }`
2. Không cover component: 200 `{ "valid": false }`
3. VIN không có certificate: 404 Not Found
4. Thiếu componentType: 400 Bad Request
5. Không token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-certificates/validate/1HGBH41JXMN109186?componentType=BATTERY" \
 -H "Authorization: Bearer <token>"
```

### JavaScript

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-certificates/validate/1HGBH41JXMN109186?componentType=BATTERY",
  { headers: { Authorization: "Bearer <token>" } }
);
console.log(await res.json());
```

### Python

```python
import requests
print(requests.get(
  "http://localhost:8080/api/warranty-certificates/validate/1HGBH41JXMN109186",
  headers={"Authorization":"Bearer <token>"},
  params={"componentType":"BATTERY"}
).json())
```

## Notes

- Kết quả phụ thuộc logic `validateCertificate(vin, componentType)` của service.
