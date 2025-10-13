### GET /api/warranty-policies/by-vin/{vin}

- Mô tả: Trích xuất thông tin bảo hành từ `vehicle.warranty_info` theo VIN
- RBAC: Tất cả 4 vai trò, yêu cầu `Authorization: Bearer <token>`

Request

```
GET /api/warranty-policies/by-vin/1HGBH41JXMN109186
Authorization: Bearer <token>
```

Response (200)

```json
{
  "vin": "1HGBH41JXMN109186",
  "underWarranty": true,
  "expiryDate": "2026-01-01"
}
```

Ghi chú

- Dữ liệu lấy từ `vehicles.warranty_info` (JSON). Không thay đổi schema/model.
