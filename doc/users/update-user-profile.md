# PUT /api/users/{id}/profile

## Mô tả

Cập nhật thông tin profile của người dùng bao gồm thông tin cá nhân, liên lạc và các tùy chọn bảo mật. Endpoint này cho phép người dùng cập nhật thông tin của chính mình hoặc Admin/EVM_Staff cập nhật thông tin người dùng khác.

## Endpoint

```
PUT /api/users/{id}/profile
```

## Request

### Headers

```
Content-Type: application/json
Authorization: Bearer <session_token>
```

### Path Parameters

| Parameter | Type | Required | Description                    |
| --------- | ---- | -------- | ------------------------------ |
| id        | long | Yes      | ID của người dùng cần cập nhật |

### Body

```json
{
  "fullName": "John Doe Updated",
  "phone": "+1234567890",
  "address": "456 New Street, City, State",
  "mfaEnabled": true
}
```

### Parameters

| Field      | Type    | Required | Description       |
| ---------- | ------- | -------- | ----------------- |
| fullName   | string  | No       | Họ tên đầy đủ mới |
| phone      | string  | No       | Số điện thoại mới |
| address    | string  | No       | Địa chỉ mới       |
| mfaEnabled | boolean | No       | Bật/tắt 2FA       |

## Response

### Success Response (200 OK)

```json
{
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe Updated",
  "role": "SC_Staff",
  "serviceCenterId": 1,
  "serviceCenterName": "Downtown Service Center",
  "phone": "+1234567890",
  "address": "456 New Street, City, State",
  "mfaEnabled": true,
  "isActive": true,
  "lastLoginAt": "2024-01-01T10:30:00Z",
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T12:00:00Z"
}
```

### Error Responses

#### 400 Bad Request - ID không hợp lệ

```json
{
  "error": "Invalid user ID",
  "path": "/api/users/0/profile",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 400 Bad Request - Thiếu request body

```json
{
  "error": "Request body is required",
  "path": "/api/users/1/profile",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 404 Not Found - Người dùng không tồn tại

```json
{
  "error": "User not found",
  "path": "/api/users/999/profile",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 409 Conflict - Email đã tồn tại

```json
{
  "error": "Email already exists",
  "path": "/api/users/1/profile",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 500 Internal Server Error

```json
{
  "error": "Failed to update profile: Internal server error",
  "path": "/api/users/1/profile",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Prerequisites

- **Bắt buộc:** Phải đăng nhập với session token hợp lệ
- **Endpoint cần thiết:** `POST /api/auth/login`
- **Authorization:** Session token trong header
- **Permission:** Có thể cập nhật profile của chính mình hoặc có quyền quản lý người dùng

## Test Cases

### Test Case 1: Cập nhật profile thành công

**Request:**

```json
{
  "fullName": "John Doe Updated",
  "phone": "+1234567890",
  "address": "456 New Street"
}
```

**Expected Response:** 200 OK với thông tin đã cập nhật

### Test Case 2: Cập nhật chỉ một field

**Request:**

```json
{
  "fullName": "John Doe Updated"
}
```

**Expected Response:** 200 OK với chỉ fullName được cập nhật

### Test Case 3: Cập nhật 2FA

**Request:**

```json
{
  "mfaEnabled": true
}
```

**Expected Response:** 200 OK với mfaEnabled = true

### Test Case 4: ID không hợp lệ

**Request:**

```
PUT /api/users/0/profile
```

**Expected Response:** 400 Bad Request

### Test Case 5: Người dùng không tồn tại

**Request:**

```
PUT /api/users/999/profile
```

**Expected Response:** 404 Not Found

## Usage Example

### cURL

```bash
curl -X PUT http://localhost:8080/api/users/1/profile \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "fullName": "John Doe Updated",
    "phone": "+1234567890",
    "address": "456 New Street, City, State",
    "mfaEnabled": true
  }'
```

### JavaScript (Fetch)

```javascript
const response = await fetch("http://localhost:8080/api/users/1/profile", {
  method: "PUT",
  headers: {
    "Content-Type": "application/json",
    Authorization: "Bearer sess_abc123def456ghi789",
  },
  body: JSON.stringify({
    fullName: "John Doe Updated",
    phone: "+1234567890",
    address: "456 New Street, City, State",
    mfaEnabled: true,
  }),
});

const data = await response.json();
console.log(data);
```

### Python (Requests)

```python
import requests

url = "http://localhost:8080/api/users/1/profile"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer sess_abc123def456ghi789"
}
data = {
    "fullName": "John Doe Updated",
    "phone": "+1234567890",
    "address": "456 New Street, City, State",
    "mfaEnabled": True
}

response = requests.put(url, json=data, headers=headers)
print(response.json())
```

## Workflow Example

### Bước 1: Đăng nhập để lấy session token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "sessionToken": "sess_abc123def456ghi789"
  }
}
```

### Bước 2: Lấy thông tin profile hiện tại

```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer sess_abc123def456ghi789"
```

**Response:**

```json
{
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "phone": "+1234567890",
  "address": "123 Main St",
  "mfaEnabled": false
}
```

### Bước 3: Cập nhật profile

```bash
curl -X PUT http://localhost:8080/api/users/1/profile \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sess_abc123def456ghi789" \
  -d '{
    "fullName": "John Doe Updated",
    "phone": "+9876543210",
    "address": "456 New Street, City, State",
    "mfaEnabled": true
  }'
```

**Response:**

```json
{
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe Updated",
  "phone": "+9876543210",
  "address": "456 New Street, City, State",
  "mfaEnabled": true,
  "updatedAt": "2024-01-01T12:00:00Z"
}
```

## Permission Matrix

### Admin

- ✅ Có thể cập nhật profile của tất cả người dùng
- ✅ Có thể thay đổi tất cả thông tin
- ✅ Có thể bật/tắt 2FA cho người dùng khác

### EVM_Staff

- ✅ Có thể cập nhật profile của tất cả người dùng
- ✅ Có thể thay đổi tất cả thông tin
- ✅ Có thể bật/tắt 2FA cho người dùng khác

### SC_Staff

- ✅ Có thể cập nhật profile của chính mình
- ✅ Có thể cập nhật profile của SC_Staff/SC_Technician trong cùng service center
- ❌ Không thể cập nhật profile của Admin/EVM_Staff

### SC_Technician

- ✅ Có thể cập nhật profile của chính mình
- ❌ Không thể cập nhật profile của người dùng khác

## Validation Rules

### fullName

- Không được trống nếu được cung cấp
- Tối đa 100 ký tự
- Chỉ chứa chữ cái, số, khoảng trắng và dấu gạch ngang

### phone

- Format quốc tế với dấu +
- Tối đa 20 ký tự
- Chỉ chứa số, dấu +, dấu gạch ngang, khoảng trắng

### address

- Tối đa 500 ký tự
- Có thể chứa chữ cái, số, ký tự đặc biệt

### mfaEnabled

- Chỉ chấp nhận true/false
- Nếu bật 2FA, người dùng cần setup 2FA app

## Security Considerations

### Data Privacy

- Email không thể thay đổi qua endpoint này
- Role không thể thay đổi qua endpoint này
- Service center không thể thay đổi qua endpoint này
- Password không thể thay đổi qua endpoint này (cần endpoint riêng)

### Access Control

- Người dùng chỉ có thể cập nhật thông tin trong phạm vi quyền của mình
- Admin và EVM_Staff có quyền cao nhất
- SC_Staff chỉ quản lý trong service center của mình

### Audit Trail

- Tất cả thay đổi được ghi log
- Lưu thông tin ai thay đổi, khi nào thay đổi
- Có thể rollback nếu cần thiết

## Notes

- Endpoint này không cho phép thay đổi email, role, service center
- Chỉ cập nhật các field được cung cấp trong request
- Các field không được cung cấp sẽ giữ nguyên giá trị cũ
- updatedAt sẽ được cập nhật tự động
- Nếu bật 2FA, người dùng cần setup 2FA app trong lần đăng nhập tiếp theo
- Nếu tắt 2FA, người dùng sẽ không cần 2FA trong lần đăng nhập tiếp theo
- Thay đổi 2FA sẽ có hiệu lực ngay lập tức
