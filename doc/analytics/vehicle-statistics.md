# GET /api/analytics/statistics/vehicles

## Mô tả

Lấy thống kê toàn diện về các phương tiện (vehicles) bao gồm:

- Tổng số xe
- Phân loại theo trạng thái (active, inactive, deleted)
- Phân bố theo model và năm sản xuất
- Phân bố theo variant và màu sắc
- Thống kê bảo hành (active/expired)
- Tỷ lệ phủ bảo hành

## Endpoint

```
GET /api/analytics/statistics/vehicles
```

## Vai trò có quyền truy cập

- Admin
- EVM_Staff
- SC_Staff (chỉ xem được của service center mình)

## Query Parameters

| Tham số | Kiểu | Bắt buộc | Mô tả                                                                          | Ví dụ |
| ------- | ---- | -------- | ------------------------------------------------------------------------------ | ----- |
| oemId   | Long | Không    | ID của OEM để lọc thống kê. Nếu không cung cấp, trả về thống kê cho tất cả OEM | 1     |

## Request Example

### Lấy thống kê cho tất cả OEM

```bash
curl -X GET "http://localhost:8080/api/analytics/statistics/vehicles" \
  -H "Authorization: Bearer {token}"
```

### Lấy thống kê cho OEM cụ thể

```bash
curl -X GET "http://localhost:8080/api/analytics/statistics/vehicles?oemId=1" \
  -H "Authorization: Bearer {token}"
```

## Response

### Success Response (200 OK)

```json
{
  "totalVehicles": 12500,
  "activeVehicles": 11200,
  "inactiveVehicles": 800,
  "deletedVehicles": 500,
  "vehiclesByStatus": [
    {
      "status": "active",
      "count": 11200
    },
    {
      "status": "inactive",
      "count": 800
    },
    {
      "status": "deleted",
      "count": 500
    }
  ],
  "vehiclesByModel": [
    {
      "model": "Model 3",
      "count": 5000
    },
    {
      "model": "Model Y",
      "count": 3500
    },
    {
      "model": "Model S",
      "count": 2500
    },
    {
      "model": "Model X",
      "count": 1500
    }
  ],
  "vehiclesByYear": [
    {
      "model_year": 2024,
      "count": 3200
    },
    {
      "model_year": 2023,
      "count": 4800
    },
    {
      "model_year": 2022,
      "count": 2500
    },
    {
      "model_year": 2021,
      "count": 1500
    },
    {
      "model_year": 2020,
      "count": 500
    }
  ],
  "vehiclesByVariant": [
    {
      "variant": "Standard Range Plus",
      "count": 4500
    },
    {
      "variant": "Long Range",
      "count": 3800
    },
    {
      "variant": "Performance",
      "count": 2200
    },
    {
      "variant": "Plaid",
      "count": 1000
    }
  ],
  "vehiclesByColor": [
    {
      "color": "Pearl White",
      "count": 3200
    },
    {
      "color": "Midnight Silver",
      "count": 2800
    },
    {
      "color": "Deep Blue",
      "count": 2500
    },
    {
      "color": "Red",
      "count": 2000
    },
    {
      "color": "Black",
      "count": 2000
    }
  ],
  "activeWarrantyVehicles": 8562,
  "expiredWarrantyVehicles": 3938,
  "warrantyCoverageRate": 68.5
}
```

### Error Response (500 Internal Server Error)

```json
{
  "error": "Database connection error"
}
```

## Response Fields Explanation

### Tổng quan về xe

- `totalVehicles`: Tổng số xe trong hệ thống
- `activeVehicles`: Số xe đang hoạt động (status = active hoặc null)
- `inactiveVehicles`: Số xe không hoạt động (status = inactive)
- `deletedVehicles`: Số xe đã bị xóa (soft delete, status = deleted)

### Phân bố theo trạng thái

- `vehiclesByStatus`: Mảng các đối tượng chứa trạng thái và số lượng
  - `status`: Trạng thái của xe (active, inactive, deleted)
  - `count`: Số lượng xe có trạng thái đó

### Phân bố theo model

- `vehiclesByModel`: Top 10 model có nhiều xe nhất
  - `model`: Tên model xe
  - `count`: Số lượng xe thuộc model đó

### Phân bố theo năm sản xuất

- `vehiclesByYear`: Phân bố xe theo năm sản xuất (sắp xếp giảm dần)
  - `model_year`: Năm sản xuất
  - `count`: Số lượng xe sản xuất năm đó

### Phân bố theo variant

- `vehiclesByVariant`: Top 10 variant phổ biến nhất
  - `variant`: Biến thể của xe (từ vehicle_data JSON)
  - `count`: Số lượng xe thuộc variant đó

### Phân bố theo màu sắc

- `vehiclesByColor`: Top 10 màu sắc phổ biến nhất
  - `color`: Màu sắc của xe (từ vehicle_data JSON)
  - `count`: Số lượng xe có màu đó

### Thống kê bảo hành

- `activeWarrantyVehicles`: Số xe còn bảo hành (warranty chưa hết hạn)
- `expiredWarrantyVehicles`: Số xe hết bảo hành
- `warrantyCoverageRate`: Tỷ lệ phủ bảo hành (%) = (activeWarrantyVehicles / totalVehicles) \* 100

## Notes

- Endpoint này dành cho Admin và EVM_Staff
- SC_Staff chỉ có thể xem thống kê của service center mình quản lý
- Nếu không cung cấp `oemId`, hệ thống sẽ trả về thống kê cho tất cả OEM
- Trạng thái mặc định của xe là "active" nếu không có giá trị status
- Thống kê bảo hành dựa trên ngày hết hạn trong warranty_info JSON
- Top 10 model, variant và color được sắp xếp theo số lượng giảm dần
- Năm sản xuất được sắp xếp từ mới nhất đến cũ nhất
- Tỷ lệ phủ bảo hành được làm tròn 2 chữ số thập phân

## Business Rules

1. **Trạng thái xe**:

   - `active`: Xe đang hoạt động bình thường
   - `inactive`: Xe tạm ngưng hoạt động
   - `deleted`: Xe đã bị xóa khỏi hệ thống (soft delete)

2. **Bảo hành**:

   - Active: warranty_info->>'endDate' >= ngày hiện tại
   - Expired: warranty_info->>'endDate' < ngày hiện tại

3. **Phân quyền**:
   - Admin: Xem tất cả thống kê
   - EVM_Staff: Xem thống kê của OEM mình quản lý
   - SC_Staff: Xem thống kê xe liên quan đến service center

## Use Cases

### Use Case 1: Dashboard tổng quan cho Admin

Admin muốn xem tổng quan toàn bộ xe trong hệ thống để đánh giá quy mô hoạt động.

```bash
GET /api/analytics/statistics/vehicles
```

### Use Case 2: Báo cáo cho OEM cụ thể

EVM Staff muốn xem thống kê xe của Tesla để báo cáo cho ban quản lý.

```bash
GET /api/analytics/statistics/vehicles?oemId=1
```

### Use Case 3: Phân tích thị trường

Phân tích model nào bán chạy nhất và màu sắc phổ biến để đưa ra quyết định sản xuất.

```bash
GET /api/analytics/statistics/vehicles?oemId=1
# Xem vehiclesByModel và vehiclesByColor
```

### Use Case 4: Theo dõi tỷ lệ bảo hành

Kiểm tra tỷ lệ xe còn bảo hành để dự đoán chi phí bảo hành trong tương lai.

```bash
GET /api/analytics/statistics/vehicles?oemId=1
# Xem warrantyCoverageRate, activeWarrantyVehicles, expiredWarrantyVehicles
```

## Example Scenarios

### Scenario 1: Hệ thống mới, ít xe

```json
{
  "totalVehicles": 10,
  "activeVehicles": 8,
  "inactiveVehicles": 2,
  "deletedVehicles": 0,
  "vehiclesByStatus": [
    { "status": "active", "count": 8 },
    { "status": "inactive", "count": 2 }
  ],
  "vehiclesByModel": [{ "model": "Model 3", "count": 10 }],
  "vehiclesByYear": [{ "model_year": 2024, "count": 10 }],
  "vehiclesByVariant": [{ "variant": "Standard Range Plus", "count": 10 }],
  "vehiclesByColor": [
    { "color": "Pearl White", "count": 5 },
    { "color": "Black", "count": 5 }
  ],
  "activeWarrantyVehicles": 10,
  "expiredWarrantyVehicles": 0,
  "warrantyCoverageRate": 100.0
}
```

### Scenario 2: Hệ thống lớn, nhiều xe hết bảo hành

```json
{
  "totalVehicles": 50000,
  "activeVehicles": 45000,
  "inactiveVehicles": 3000,
  "deletedVehicles": 2000,
  "vehiclesByStatus": [
    {"status": "active", "count": 45000},
    {"status": "inactive", "count": 3000},
    {"status": "deleted", "count": 2000}
  ],
  "vehiclesByModel": [
    {"model": "Model 3", "count": 20000},
    {"model": "Model Y", "count": 15000},
    {"model": "Model S", "count": 10000},
    {"model": "Model X", "count": 5000}
  ],
  "vehiclesByYear": [
    {"model_year": 2024, "count": 8000},
    {"model_year": 2023, "count": 12000},
    {"model_year": 2022, "count": 10000},
    {"model_year": 2021, "count": 8000},
    {"model_year": 2020, "count": 6000},
    {"model_year": 2019, "count": 4000},
    {"model_year": 2018, "count": 2000}
  ],
  "vehiclesByVariant": [...],
  "vehiclesByColor": [...],
  "activeWarrantyVehicles": 20000,
  "expiredWarrantyVehicles": 30000,
  "warrantyCoverageRate": 40.0
}
```

## Related Endpoints

- `GET /api/analytics/statistics/parts` - Thống kê phụ tùng
- `GET /api/analytics/dashboard/evm/{oemId}` - Dashboard tổng quan cho OEM
- `GET /api/vehicles` - Danh sách xe
- `GET /api/vehicles/status/{status}` - Lọc xe theo trạng thái

## Database Queries

API này sử dụng các câu query SQL sau:

1. **Đếm tổng số xe**:

```sql
SELECT COUNT(*) FROM aoem.vehicles WHERE oem_id = ?
```

2. **Đếm xe theo trạng thái**:

```sql
SELECT COALESCE(status, 'active') as status, COUNT(*) as count
FROM aoem.vehicles
WHERE oem_id = ?
GROUP BY COALESCE(status, 'active')
```

3. **Phân bố theo model**:

```sql
SELECT model, COUNT(*) as count
FROM aoem.vehicles
WHERE oem_id = ?
GROUP BY model
ORDER BY count DESC
LIMIT 10
```

4. **Xe có bảo hành còn hiệu lực**:

```sql
SELECT COUNT(*) FROM aoem.vehicles
WHERE warranty_info->>'endDate' IS NOT NULL
AND TO_DATE(warranty_info->>'endDate', 'YYYY-MM-DD') >= CURRENT_DATE
AND oem_id = ?
```

## Performance Considerations

- Các query sử dụng index trên cột `oem_id` để tối ưu hiệu suất
- JSON field query (variant, color) có thể chậm với lượng dữ liệu lớn
- Nên tạo index cho JSON fields nếu cần:
  ```sql
  CREATE INDEX idx_vehicle_data_variant ON aoem.vehicles
  USING GIN ((vehicle_data->'variant'));
  ```
- Cache kết quả trong 5-10 phút nếu dữ liệu không thay đổi thường xuyên

## Security

- Requires authentication token in header
- Role-based access control (RBAC):
  - Admin: Full access
  - EVM_Staff: Access to their OEM only
  - SC_Staff: Limited access to vehicles in their service center
- Input validation cho `oemId` parameter
- SQL injection protection thông qua JdbcTemplate parameterized queries
