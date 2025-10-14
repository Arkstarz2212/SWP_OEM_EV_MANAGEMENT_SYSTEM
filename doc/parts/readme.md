### Parts RBAC & Read Endpoints

- Read endpoints (`GET /api/parts/**`) yêu cầu `Authorization: Bearer <token>`.
- Cho phép tất cả 4 vai trò: Admin, EVM_Staff, SC_Staff, SC_Technician.

Search

```
GET /api/parts?keyword=battery&category=BATTERY&manufacturer=Tesla
Authorization: Bearer <token>
```

Get by ID/Number

```
GET /api/parts/1
GET /api/parts/number/BAT001
Authorization: Bearer <token>
```

Write (Admin/EVM_Staff)

```
POST /api/parts
POST /api/parts/{id}/activate
POST /api/parts/{id}/deactivate
```
