### GET /api/warranty-policies/by-model/{model}

- Mô tả: Tổng hợp thông tin chính sách theo `model`. Hiện trả về `policyCount` dựa trên dịch vụ policy hiện hành; có thể mở rộng để đọc từ `vehicle.warranty_info` khi cần.
- RBAC: Tất cả 4 vai trò, yêu cầu `Authorization: Bearer <token>`

Request

```
GET /api/warranty-policies/by-model/Model%203
Authorization: Bearer <token>
```

Response (200)

```json
{
  "model": "Model 3",
  "policyCount": 3
}
```

Ghi chú

- Theo ràng buộc không thay DB/model, endpoint ở chế độ đọc-only.
