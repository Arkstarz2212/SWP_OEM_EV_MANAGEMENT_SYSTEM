# POST /api/warranty-policies

## Mô tả

Tạo chính sách bảo hành cho model xe hoặc danh mục component.

## Endpoint

```
POST /api/warranty-policies
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
  "model": "Model 3",
  "componentCategory": "BATTERY",
  "monthsCoverage": 24,
  "kmCoverage": 50000,
  "notes": "OEM standard warranty"
}
```

### Parameters

| Field             | Type   | Required | Description                      |
| ----------------- | ------ | -------- | -------------------------------- |
| model             | string | Yes      | Model xe áp dụng                 |
| componentCategory | string | Yes      | Danh mục component (vd: BATTERY) |
| monthsCoverage    | int    | Yes      | Số tháng bảo hành                |
| kmCoverage        | int    | Yes      | Số km bảo hành                   |
| notes             | string | No       | Ghi chú                          |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "model": "Model 3",
  "componentCategory": "BATTERY",
  "monthsCoverage": 24,
  "kmCoverage": 50000,
  "notes": "OEM standard warranty"
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
- Quyền: Admin hoặc EVM_Staff

## Test Cases

1. Tạo hợp lệ: 200 OK trả về policy
2. Thiếu `model`: 400 Bad Request
3. `monthsCoverage` âm: 400 Bad Request
4. Không có token: 401 Unauthorized
5. Không đủ quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST "http://localhost:8080/api/warranty-policies" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"model":"Model 3","componentCategory":"BATTERY","monthsCoverage":24,"kmCoverage":50000,"notes":"OEM standard warranty"}'
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/warranty-policies", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    model: "Model 3",
    componentCategory: "BATTERY",
    monthsCoverage: 24,
    kmCoverage: 50000,
    notes: "OEM standard warranty",
  }),
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.post("http://localhost:8080/api/warranty-policies", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"model":"Model 3","componentCategory":"BATTERY","monthsCoverage":24,"kmCoverage":50000,"notes":"OEM standard warranty"}).json())
```

## Validation Rules

- `model`: không trống
- `componentCategory`: thuộc danh sách hợp lệ
- `monthsCoverage`, `kmCoverage`: số không âm

## Notes

- Chính sách có thể gắn với OEM cụ thể tùy triển khai service.
