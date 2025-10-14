# PUT /api/users/{id}/role

## Mô tả

Cập nhật role của người dùng trong hệ thống. Endpoint này chỉ dành cho Admin và EVM_Staff để quản lý phân quyền người dùng.

## Endpoint

```
PUT /api/users/{id}/role
```

## Request

### Headers

```
Content-Type: application/json
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description                         |
| --------- | ---- | -------- | ----------------------------------- |
| id        | long | Yes      | ID của người dùng cần cập nhật role |

### Query Parameters

| Parameter | Type   | Required | Description                                          |
| --------- | ------ | -------- | ---------------------------------------------------- |
| role      | string | Yes      | Role mới (Admin, EVM_Staff, SC_Staff, SC_Technician) |

## Response

### Success Response (200 OK)

```json
{
  "success": true,
  "message": "Role updated successfully"
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid user ID",
  "path": "/api/users/0/role",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Thiếu role parameter

```json
{
  "error": "Role parameter is required",
  "path": "/api/users/1/role",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Role không hợp lệ

```json
{
  "error": "Invalid role. Valid roles are: Admin, EVM_Staff, SC_Staff, SC_Technician",
  "path": "/api/users/1/role",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Người dùng không tồn tại

```json
{
  "error": "User not found",
  "path": "/api/users/999/role",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update role: Internal server error",
  "path": "/api/users/1/role",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với quyền Admin hoặc EVM_Staff
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token hợp lệ
- **Permission:** Chỉ Admin và EVM_Staff mới có quyền thay đổi role

## Test Cases

### Test Case 1: Cập nhật role thành công

**Request:**

```
PUT /api/users/1/role?role=SC_Staff
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với message thành công

### Test Case 2: Thay đổi từ SC_Staff sang EVM_Staff

**Request:**

```
PUT /api/users/2/role?role=EVM_Staff
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 200 OK với message thành công

### Test Case 3: Role không hợp lệ

**Request:**

```
PUT /api/users/1/role?role=InvalidRole
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 4: Thiếu role parameter

**Request:**

```
PUT /api/users/1/role
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 400 Bad Request

### Test Case 5: Người dùng không tồn tại

**Request:**

```
PUT /api/users/999/role?role=SC_Staff
Authorization: Bearer sess_abc123def456ghi789
```

**Expected Response:** 404 Not Found

## Usage Example

### cURL

```bash
curl -X PUT "http://localhost:8080/api/users/1/role?role=SC_Staff" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

### JavaScript (Fetch)

```javascript
const response = await fetch(
  "http://localhost:8080/api/users/1/role?role=SC_Staff",
  {
    method: "PUT",
    headers: {
      Authorization: "Bearer sess_abc123def456ghi789",
    },
  }
);

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/users/1/role"
headers = {
    "Authorization": "Bearer sess_abc123def456ghi789"
}
params = {
    "role": "SC_Staff"
}

response = requests.put(url, headers=headers, params=params)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập với quyền Admin

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@example.com",
    "password": "admin123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "role": "Admin",
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Kiểm tra role hiện tại của người dùng

```bash
curl -X GET http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 2,
  "email": "user@example.com",
  "role": "SC_Staff",
  "serviceCenterId": 1
}
```

### Bước 3: Cập nhật role

```bash
curl -X PUT "http://localhost:8080/api/users/2/role?role=EVM_Staff" \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "success": true,
  "message": "Role updated successfully"
}
```

### Bước 4: Xác nhận role đã thay đổi

```bash
curl -X GET http://localhost:8080/api/users/2 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 2,
  "email": "user@example.com",
  "role": "EVM_Staff",
  "serviceCenterId": null
}
```

## Role Transitions

### Valid Role Changes

- **Admin → EVM_Staff**: Giảm quyền từ Admin xuống EVM_Staff
- **EVM_Staff → Admin**: Tăng quyền từ EVM_Staff lên Admin
- **EVM_Staff → SC_Staff**: Chuyển từ EVM_Staff sang SC_Staff
- **SC_Staff → EVM_Staff**: Chuyển từ SC_Staff sang EVM_Staff
- **SC_Staff → SC_Technician**: Chuyển từ SC_Staff sang SC_Technician
- **SC_Technician → SC_Staff**: Chuyển từ SC_Technician sang SC_Staff

### Role Hierarchy

```
Admin (Highest)
├── EVM_Staff
└── SC_Staff
    └── SC_Technician (Lowest)
```

## Permission Matrix

### Admin

- ✅ Có thể thay đổi role của tất cả người dùng
- ✅ Có thể thăng cấp/giáng cấp bất kỳ ai
- ✅ Có thể tạo Admin mới

### EVM_Staff

- ✅ Có thể thay đổi role của SC_Staff và SC_Technician
- ✅ Có thể thăng cấp SC_Staff lên EVM_Staff
- ❌ Không thể thay đổi role của Admin
- ❌ Không thể tạo Admin mới

### SC_Staff

- ❌ Không có quyền thay đổi role
- ❌ Chỉ có thể quản lý trong service center

### SC_Technician

- ❌ Không có quyền thay đổi role
- ❌ Chỉ có thể thực hiện tác vụ kỹ thuật

## Impact of Role Changes

### When Changing to Admin

- Người dùng có quyền cao nhất
- Có thể quản lý tất cả service centers
- Có thể tạo người dùng mới
- Có thể thay đổi role của người khác

### When Changing to EVM_Staff

- Có thể quản lý service centers
- Có thể tạo SC_Staff và SC_Technician
- Không thể tạo Admin
- Có thể thay đổi role của SC users

### When Changing to SC_Staff

- Chỉ quản lý trong service center của mình
- Không thể tạo người dùng mới
- Không thể thay đổi role
- Cần có serviceCenterId

### When Changing to SC_Technician

- Chỉ thực hiện tác vụ kỹ thuật
- Không có quyền quản lý
- Cần có serviceCenterId
- Không thể tạo người dùng mới

## Security Considerations

### Audit Trail

- Tất cả thay đổi role được ghi log
- Lưu thông tin ai thay đổi, khi nào thay đổi
- Có thể rollback nếu cần thiết

### Session Invalidation

- Khi thay đổi role, tất cả session cũ sẽ bị vô hiệu hóa
- Người dùng cần đăng nhập lại để có quyền mới
- Điều này đảm bảo bảo mật khi role thay đổi

### Validation

- Không thể thay đổi role của chính mình
- Không thể thay đổi role của Admin khác (trừ khi là Admin)
- Role phải hợp lệ và tồn tại

## Notes

- Endpoint này chỉ dành cho Admin và EVM_Staff
- SC_Staff và SC_Technician không có quyền sử dụng
- Sau khi thay đổi role, người dùng cần đăng nhập lại
- Tất cả session cũ sẽ bị vô hiệu hóa
- Role mới sẽ có hiệu lực ngay lập tức
- Nếu chuyển sang SC_Staff/SC_Technician, cần có serviceCenterId
- Nếu chuyển sang Admin/EVM_Staff, serviceCenterId sẽ được set null
- Thay đổi role là thao tác nhạy cảm và cần được ghi log đầy đủ
