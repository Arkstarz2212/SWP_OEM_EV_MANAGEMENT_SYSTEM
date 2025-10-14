# POST /api/users

## Mô tả

Tạo người dùng mới trong hệ thống với thông tin profile và phân quyền. Endpoint này cho phép Admin hoặc EVM Staff tạo tài khoản cho các role khác nhau trong hệ thống.

## Endpoint

```
POST /api/users
```

## Request

### Headers

```
Content-Type: application/json
Authorization: Bearer <session_token>
```

### Body

```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "phone": "+1234567890",
  "address": "123 Main St",
  "mfaEnabled": false
}
```

### Parameters

| Field           | Type    | Required | Description                                                     |
| --------------- | ------- | -------- | --------------------------------------------------------------- |
| email           | string  | Yes      | Email của người dùng (phải unique)                              |
| password        | string  | Yes      | Mật khẩu (tối thiểu 6 ký tự)                                    |
| fullName        | string  | Yes      | Họ tên đầy đủ                                                   |
| role            | string  | Yes      | Role của người dùng (Admin, EVM_Staff, SC_Staff, SC_Technician) |
| serviceCenterId | long    | No       | ID service center (bắt buộc cho SC_Staff, SC_Technician)        |
| phone           | string  | No       | Số điện thoại                                                   |
| address         | string  | No       | Địa chỉ                                                         |
| mfaEnabled      | boolean | No       | Bật 2FA (mặc định false)                                        |

## Response

### Success Response (201 Created)

```json
{
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "phone": "+1234567890",
  "address": "123 Main St",
  "mfaEnabled": false,
  "isActive": true,
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### Error Responses

#### 400 Bad Request - Thiếu thông tin bắt buộc

```json
{
  "error": "Email is required",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Mật khẩu quá ngắn

```json
{
  "error": "Password must be at least 6 characters long",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Role không hợp lệ

```json
{
  "error": "Invalid role. Valid roles are: Admin, EVM_Staff, SC_Staff, SC_Technician",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 409 Conflict - Email đã tồn tại

```json
{
  "error": "Email already exists",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to create user: Internal server error",
  "path": "/api/users",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với quyền Admin hoặc EVM_Staff
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token hợp lệ
- **Service Center:** Nếu tạo SC_Staff/SC_Technician, service center phải tồn tại

## Test Cases

### Test Case 1: Tạo Admin thành công

**Request:**

```json
{
  "email": "admin@example.com",
  "password": "admin123",
  "fullName": "System Administrator",
  "role": "Admin"
}
```

**Expected Response:** 201 Created với thông tin admin

### Test Case 2: Tạo SC Staff thành công

**Request:**

```json
{
  "email": "scstaff@service.com",
  "password": "scstaff123",
  "fullName": "Service Center Staff",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "phone": "+1234567890"
}
```

**Expected Response:** 201 Created với serviceCenterId

### Test Case 3: Email đã tồn tại

**Request:**

```json
{
  "email": "existing@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "role": "EVM_Staff"
}
```

**Expected Response:** 409 Conflict

### Test Case 4: Thiếu thông tin bắt buộc

**Request:**

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Expected Response:** 400 Bad Request

### Test Case 5: Role không hợp lệ

**Request:**

```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "role": "InvalidRole"
}
```

**Expected Response:** 400 Bad Request

## Usage Example

### cURL

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "role": "SC_Staff",
    "serviceCenterId": 1,
    "phone": "+1234567890"
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/users", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    Authorization: "Bearer sess_abc123def456ghi789",
  },
  body: JSON.stringify({
    email: "user@example.com",
    password: "password123",
    fullName: "John Doe",
    role: "SC_Staff",
    serviceCenterId: 1,
    phone: "+1234567890",
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/users"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer sess_abc123def456ghi789"
}
data = {
    "email": "user@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "role": "SC_Staff",
    "serviceCenterId": 1,
    "phone": "+1234567890"
}

response = requests.post(url, json=data, headers=headers)
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

### Bước 2: Tạo người dùng mới

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "email": "newuser@example.com",
    "password": "password123",
    "fullName": "New User",
    "role": "SC_Staff",
    "serviceCenterId": 1
  }'
```

**Response:**

```json
{
  "id": 2,
  "email": "newuser@example.com",
  "fullName": "New User",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "isActive": true,
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### Bước 3: Người dùng mới có thể đăng nhập

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser@example.com",
    "password": "password123"
  }'
```

## Role Permissions

### Admin

- Có thể tạo tất cả loại người dùng
- Không cần serviceCenterId
- Có quyền cao nhất trong hệ thống

### EVM_Staff

- Có thể tạo SC_Staff, SC_Technician
- Có thể tạo EVM_Staff khác
- Không thể tạo Admin

### SC_Staff

- Không thể tạo người dùng mới
- Chỉ có thể quản lý trong service center của mình

### SC_Technician

- Không thể tạo người dùng mới
- Chỉ có thể thực hiện các tác vụ kỹ thuật

## Validation Rules

### Email

- Phải có format email hợp lệ
- Phải unique trong hệ thống
- Không được trống

### Password

- Tối thiểu 6 ký tự
- Nên bao gồm chữ hoa, chữ thường, số
- Không được trống

### Role

- Chỉ chấp nhận: Admin, EVM_Staff, SC_Staff, SC_Technician
- Case sensitive
- Bắt buộc phải có

### Service Center

- Bắt buộc cho SC_Staff và SC_Technician
- Service center phải tồn tại và active
- Không cần cho Admin và EVM_Staff

## Notes

- Sau khi tạo thành công, người dùng có thể đăng nhập ngay
- Tài khoản mới sẽ ở trạng thái active
- Mật khẩu sẽ được hash trước khi lưu vào database
- Email sẽ được gửi thông báo tạo tài khoản thành công
- Chỉ Admin và EVM_Staff mới có quyền tạo người dùng
- Service center phải tồn tại và active trước khi tạo SC_Staff/SC_Technician
