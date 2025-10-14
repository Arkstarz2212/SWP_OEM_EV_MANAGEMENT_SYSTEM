# POST /api/vehicle-components/{id}/replace

## Mô tả

Thay thế component hiện tại bằng component mới, ghi nhận lý do và người thực hiện.

## Endpoint

```
POST /api/vehicle-components/{id}/replace
```

## Request

### Headers

```
Authorization: Bearer <session_token>
Content-Type: application/json
```

### Path Parameters

| Parameter | Type | Required | Description               |
| --------- | ---- | -------- | ------------------------- |
| id        | long | Yes      | ID component cần thay thế |

### Body

```json
{
  "partCatalogId": 12,
  "serialNumber": "SN987654",
  "replacementReason": "Defective",
  "performedByUserId": 5
}
```

### Parameters

| Field             | Type   | Required | Description          |
| ----------------- | ------ | -------- | -------------------- |
| partCatalogId     | long   | Yes      | ID phụ tùng thay thế |
| serialNumber      | string | Yes      | Serial mới           |
| replacementReason | string | No       | Lý do thay thế       |
| performedByUserId | long   | No       | ID người thực hiện   |

## Response

### Success Response (200 OK)

```json
{
  "message": "Component replaced successfully",
  "oldComponentId": 1,
  "newPartCatalogId": 12,
  "newSerialNumber": "SN987654",
  "replacementReason": "Defective"
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
- Quyền: Admin, EVM_Staff hoặc SC_Staff
- Component tồn tại và ACTIVE
- `partCatalogId` hợp lệ và ACTIVE

## Test Cases

1. Thay thế hợp lệ: 200 OK
2. Thiếu `serialNumber`: 400 Bad Request
3. `partCatalogId` không tồn tại: 400 Bad Request
4. Không token: 401 Unauthorized
5. Không đủ quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST "http://localhost:8080/api/vehicle-components/1/replac e" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"partCatalogId":12,"serialNumber":"SN987654","replacementReason":"Defective","performedByUserId":5}'
```

### JavaScript (Fetch)

```javascript
const res = await fetch(
  "http://localhost:8080/api/vehicle-components/1/replace",
  {
    method: "POST",
    headers: {
      Authorization: "Bearer <token>",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      partCatalogId: 12,
      serialNumber: "SN987654",
      replacementReason: "Defective",
      performedByUserId: 5,
    }),
  }
);
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.post(
  "http://localhost:8080/api/vehicle-components/1/replace",
  headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"},
  json={"partCatalogId":12,"serialNumber":"SN987654","replacementReason":"Defective","performedByUserId":5}
).json())
```

## Validation Rules

- `serialNumber`: không rỗng, duy nhất
- `partCatalogId`: tồn tại, active
- Component cũ: ở trạng thái thay thế được

## Permission Matrix

- Admin: ✅ thay thế mọi component
- EVM_Staff: ✅ trong phạm vi OEM
- SC_Staff: ✅ trong phạm vi service center
- SC_Technician: ❌ không được thay thế

## Notes

- Controller khai báo response chung; khi implement cần ghi lại lịch sử thay thế, update trạng thái component cũ.
