# Soft Delete Vehicle API

## Endpoint: `DELETE /api/vehicles/{vin}/soft-delete`

Soft delete a vehicle by VIN (sets status to 'deleted'). The vehicle can be restored later.

### Authorization

- **Roles**: Admin, EVM_Staff
- **Authentication**: Bearer token required

### Parameters

- `vin` (path parameter): Vehicle Identification Number (required)

### Request Example

```http
DELETE /api/vehicles/1HGBH41JXMN109186/soft-delete
Authorization: Bearer <token>
```

### Response Examples

#### Success (200 OK)

```json
{
  "success": true,
  "message": "Vehicle soft deleted successfully",
  "vin": "1HGBH41JXMN109186"
}
```

#### Error Responses

- **400 Bad Request**: Invalid VIN
- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Vehicle not found

---

# Restore Vehicle API

## Endpoint: `POST /api/vehicles/{vin}/restore`

Restore a soft deleted vehicle by VIN (sets status to 'active').

### Authorization

- **Roles**: Admin, EVM_Staff
- **Authentication**: Bearer token required

### Parameters

- `vin` (path parameter): Vehicle Identification Number (required)

### Request Example

```http
POST /api/vehicles/1HGBH41JXMN109186/restore
Authorization: Bearer <token>
```

### Response Examples

#### Success (200 OK)

```json
{
  "vehicleId": 1,
  "vin": "1HGBH41JXMN109186",
  "model": "Model 3",
  "variant": "Standard Range Plus",
  "modelYear": 2023,
  "currentOdometerKm": 15000,
  "warrantyStartDate": "2023-01-15",
  "warrantyEndDate": "2028-01-15",
  "warrantyStatus": "Active",
  "status": "active",
  "customer": {
    "fullName": "Nguyễn Văn An",
    "phoneNumber": "0901234567",
    "email": "nguyenvanan@email.com",
    "address": "123 Đường ABC",
    "city": "Hà Nội",
    "province": "Hà Nội",
    "postalCode": "100000"
  },
  "components": [],
  "serviceHistory": [],
  "warrantyClaims": []
}
```

#### Error Responses

- **400 Bad Request**: Invalid VIN
- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Vehicle not found or not deleted

---

# Get Deleted Vehicles API

## Endpoint: `GET /api/vehicles/deleted`

Retrieve a list of soft deleted vehicles.

### Authorization

- **Roles**: Admin, EVM_Staff
- **Authentication**: Bearer token required

### Query Parameters

- `limit` (optional): Number of results per page (default: 20, max: 100)
- `offset` (optional): Number of results to skip (default: 0)

### Request Example

```http
GET /api/vehicles/deleted?limit=20&offset=0
Authorization: Bearer <token>
```

### Response Example

#### Success (200 OK)

```json
{
  "vehicles": [
    {
      "id": 1,
      "vin": "1HGBH41JXMN109186",
      "model": "Model 3",
      "modelYear": 2023,
      "customerName": "Nguyễn Văn An",
      "status": "deleted",
      "oemId": 1,
      "oemName": "OEM 1",
      "variant": "Standard Range Plus",
      "odometerKm": 15000,
      "warrantyStartDate": "2023-01-15",
      "warrantyEndDate": "2028-01-15",
      "warrantyKmLimit": 100000
    }
  ],
  "total": 1,
  "limit": 20,
  "offset": 0
}
```

#### Error Responses

- **400 Bad Request**: Invalid parameters
- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions

---

# Get Vehicles by Status API

## Endpoint: `GET /api/vehicles/status/{status}`

Retrieve vehicles filtered by status (active, inactive, deleted).

### Authorization

- **Roles**: Admin, EVM_Staff
- **Authentication**: Bearer token required

### Parameters

- `status` (path parameter): Vehicle status - must be one of: `active`, `inactive`, `deleted`
- `limit` (query parameter, optional): Number of results per page (default: 20, max: 100)
- `offset` (query parameter, optional): Number of results to skip (default: 0)

### Request Examples

```http
GET /api/vehicles/status/active?limit=20&offset=0
GET /api/vehicles/status/inactive?limit=20&offset=0
GET /api/vehicles/status/deleted?limit=20&offset=0
Authorization: Bearer <token>
```

### Response Example

#### Success (200 OK)

```json
{
  "vehicles": [
    {
      "id": 1,
      "vin": "1HGBH41JXMN109186",
      "model": "Model 3",
      "modelYear": 2023,
      "customerName": "Nguyễn Văn An",
      "status": "active",
      "oemId": 1,
      "oemName": "OEM 1",
      "variant": "Standard Range Plus",
      "odometerKm": 15000,
      "warrantyStartDate": "2023-01-15",
      "warrantyEndDate": "2028-01-15",
      "warrantyKmLimit": 100000
    }
  ],
  "status": "active",
  "total": 1,
  "limit": 20,
  "offset": 0
}
```

#### Error Responses

- **400 Bad Request**: Invalid status or parameters
- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions

## Status Values

The system supports these vehicle status values:

- **`active`**: Normal, active vehicles (default)
- **`inactive`**: Deactivated vehicles (not deleted, but not active)
- **`deleted`**: Soft deleted vehicles (can be restored)

## Notes

1. **Soft Delete vs Hard Delete**: Soft delete only changes the status to 'deleted' - the vehicle data remains in the database and can be restored.

2. **Filtering**: All standard vehicle queries automatically exclude deleted vehicles unless specifically requested.

3. **Restoration**: Only vehicles with status 'deleted' can be restored to 'active' status.

4. **Authorization**: Only Admin and EVM_Staff roles can perform soft delete, restore, and view deleted vehicles operations.

5. **Pagination**: All list endpoints support pagination with `limit` and `offset` parameters.
