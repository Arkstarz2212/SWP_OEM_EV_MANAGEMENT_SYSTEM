# POST /api/shipments

## Mô tả

Tạo lô hàng phụ tùng đến service center cho một warranty claim.

## Endpoint

```
POST /api/shipments
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
  "claimId": 2001,
  "serviceCenterId": 2,
  "partNumbers": ["BAT001", "MOTOR002"],
  "priority": "urgent",
  "notes": "Rush delivery for customer"
}
```

### Parameters

| Field           | Type   | Required | Description                 |
| --------------- | ------ | -------- | --------------------------- |
| claimId         | long   | Yes      | ID warranty claim           |
| serviceCenterId | long   | Yes      | ID service center           |
| partNumbers     | array  | Yes      | Danh sách partNumber        |
| priority        | string | No       | Mức ưu tiên (urgent/normal) |
| notes           | string | No       | Ghi chú                     |

## Response

### Success (200)

```json
{
  "shipmentId": 1001,
  "trackingNumber": "1Z999AA1234567890",
  "status": "pending"
}
```

### Errors

- 400: Invalid request data
- 404: Claim/Service center not found

## Prerequisites

- Đăng nhập: `POST /api/auth/login`
- Quyền: Admin/EVM_Staff

## Test Cases

1. Tạo hợp lệ: 200 OK trả về shipmentId
2. Thiếu partNumbers: 400 Bad Request
3. serviceCenterId không tồn tại: 404 Not Found
4. Không token: 401 Unauthorized
5. Không quyền: 403 Forbidden

## Usage Examples

### cURL

```bash
curl -X POST http://localhost:8080/api/shipments \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"claimId":2001,"serviceCenterId":2,"partNumbers":["BAT001","MOTOR002"],"priority":"urgent","notes":"Rush delivery for customer"}'
```

### JavaScript

```javascript
const res = await fetch("http://localhost:8080/api/shipments", {
  method: "POST",
  headers: {
    Authorization: "Bearer <token>",
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    claimId: 2001,
    serviceCenterId: 2,
    partNumbers: ["BAT001", "MOTOR002"],
    priority: "urgent",
    notes: "Rush delivery for customer",
  }),
});
console.log(await res.json());
```

### Python

```python
import requests
print(requests.post("http://localhost:8080/api/shipments", headers={"Authorization":"Bearer <token>", "Content-Type":"application/json"}, json={"claimId":2001,"serviceCenterId":2,"partNumbers":["BAT001","MOTOR002"],"priority":"urgent","notes":"Rush delivery for customer"}).json())
```

## Notes

- Tự động sinh trackingNumber và set status ban đầu `pending`.
