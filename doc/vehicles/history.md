### Vehicle History Endpoints

- `GET /api/vehicles/{vin}/service-history`

  - Trả về danh sách service records của xe theo VIN
  - RBAC: Tất cả 4 vai trò, yêu cầu `Authorization: Bearer <token>`

- `GET /api/vehicles/{vin}/claim-history`
  - Trả về danh sách warranty claims của xe theo VIN
  - RBAC: Tất cả 4 vai trò, yêu cầu `Authorization: Bearer <token>`

Ví dụ

```
GET /api/vehicles/1HGBH41JXMN109186/service-history
Authorization: Bearer <token>
```

```
GET /api/vehicles/1HGBH41JXMN109186/claim-history
Authorization: Bearer <token>
```
