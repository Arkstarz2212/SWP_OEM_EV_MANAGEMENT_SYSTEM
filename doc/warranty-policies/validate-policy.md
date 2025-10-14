# GET /api/warranty-policies/validate

## Mô tả

Kiểm tra quyền bảo hành dựa vào model, danh mục component, số tháng sử dụng và số km.

## Endpoint

```
GET /api/warranty-policies/validate?model=Model%203&category=BATTERY&months=18&km=30000
```

## Request

### Headers

```
Authorization: Bearer <session_token>
```

### Query Parameters

| Parameter | Type   | Required | Description        |
| --------- | ------ | -------- | ------------------ |
| model     | string | Yes      | Model xe           |
| category  | string | Yes      | Danh mục component |
| months    | int    | Yes      | Tháng sử dụng      |
| km        | int    | Yes      | Số km đã chạy      |

## Response

### Success Response (200 OK)

```json
{
  "valid": true
}
```

### Error Responses

#### 400 Bad Request

```json
{
  "error": "Invalid request data"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền xem xác thực bảo hành

## Test Cases

1. Hợp lệ, trong phạm vi bảo hành: 200 OK `{ "valid": true }`
2. Hết hạn theo tháng: 200 OK `{ "valid": false }`
3. Vượt km bảo hành: 200 OK `{ "valid": false }`
4. Thiếu tham số: 400 Bad Request
5. Không token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X GET "http://localhost:8080/api/warranty-policies/validate?model=Model%203&category=BATTERY&months=18&km=30000" -H "Authorization: Bearer <token>"
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/warranty-policies/validate?model=Model%203&category=BATTERY&months=18&km=30000",
  { headers: { Authorization: "Bearer <token>" } }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.get(
  "http://localhost:8080/api/warranty-policies/validate",
  headers={"Authorization":"Bearer <token>"},
  params={"model":"Model 3","category":"BATTERY","months":18,"km":30000}
).json())
```

## Notes

- Controller hiện trả về placeholder `{ valid: true }`. Cần tích hợp logic thực tế với service.
