# POST /api/vehicle-components

## Mô tả

Đăng ký component cho xe với tham chiếu `partCatalogId` và số serial.

## Endpoint

```
POST /api/vehicle-components
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
  "vehicleId": 1,
  "partCatalogId": 10,
  "serialNumber": "SN123456"
}
```

### Parameters

| Field         | Type   | Required | Description               |
| ------------- | ------ | -------- | ------------------------- |
| vehicleId     | long   | Yes      | ID xe                     |
| partCatalogId | long   | Yes      | ID phụ tùng trong catalog |
| serialNumber  | string | Yes      | Số serial của component   |

## Response

### Success Response (200 OK)

```json
{
  "message": "Component registration would require VIN mapping",
  "vehicleId": 1,
  "partCatalogId": 10,
  "serialNumber": "SN123456"
}
```

### Error Responses

#### 400 Bad Request

```json
{
  "error": "Serial number is required"
}
```

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin, EVM_Staff hoặc SC_Staff
- `vehicleId` và `partCatalogId` tồn tại, hợp lệ

## Test Cases

1. Đăng ký hợp lệ: 200 OK trả về object echo như trên
2. Thiếu `serialNumber`: 400 Bad Request
3. `vehicleId` không tồn tại: 400 Bad Request
4. `partCatalogId` không tồn tại: 400 Bad Request
5. Không có token: 401 Unauthorized

## Usage Examples

### cURL

```bash
curl -X POST "http://localhost:8080/api/vehicle-components" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"vehicleId":1,"partCatalogId":10,"serialNumber":"SN123456"}'
```

### JavaScript (Fetch)

```javascript
const res = await fetch("http://localhost:8080/api/vehicle-components", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    vehicleId: 1,
    partCatalogId: 10,
    serialNumber: "SN123456",
  }),
});
console.log(await res.json());
```

### Python (Requests)

```python
import requests
print(requests.post(
  "http://localhost:8080/api/vehicle-components",
  headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"},
  json={"vehicleId":1,"partCatalogId":10,"serialNumber":"SN123456"}
).json())
```

## Validation Rules

- `serialNumber`: không rỗng, duy nhất trong hệ thống
- `vehicleId`: phải tồn tại, thuộc phạm vi quyền
- `partCatalogId`: phải tồn tại, active

## Permission Matrix

- Admin: ✅ đăng ký cho mọi xe
- EVM_Staff: ✅ đăng ký trong phạm vi OEM
- SC_Staff: ✅ đăng ký cho xe của service center
- SC_Technician: ❌ không được đăng ký

## Notes

- Controller hiện trả về mock/echo (yêu cầu VIN mapping). Khi triển khai thực, cần validate VIN và tạo bản ghi `VehicleComponent`.
