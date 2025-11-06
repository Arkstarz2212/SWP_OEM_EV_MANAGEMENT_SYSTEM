package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.CreateVehicleRequest;
import org.example.models.dto.request.UpdateVehicleRequest;
import org.example.models.dto.request.VehicleRegisterRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.VehicleDetailResponse;
import org.example.models.dto.response.VehicleResponse;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IServiceRecordService;
import org.example.service.IService.IVehicleService;
import org.example.service.IService.IWarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Vehicle registration and management including VIN lookup and odometer updates")
public class VehiclesController {

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private IServiceRecordService serviceRecordService;

    @Autowired
    private IWarrantyClaimService warrantyClaimService;

    /**
     * Helper method to convert role from session to UserRole enum
     */
    private UserRole convertToUserRole(Object roleObj) {
        if (roleObj instanceof UserRole) {
            return (UserRole) roleObj;
        } else if (roleObj instanceof String) {
            try {
                return UserRole.valueOf((String) roleObj);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Resolve current request path for consistent error responses
     */
    private String getCurrentPath() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getRequestURI();
    }

    @PostMapping
    @Operation(summary = "Register Vehicle", description = "Register a new vehicle in the system using VIN and model information. Roles: Admin, EVM_Staff, SC_Staff.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Vehicle registration data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Register Vehicle Example", value = "{\n  \"vin\": \"1HGBH41JXMN109186\",\n  \"oemId\": 1,\n  \"model\": \"Model 3\",\n  \"modelYear\": 2023,\n  \"image\": \"https://cdn.example.com/vehicles/model3.jpg\",\n  \"customerInfo\": {\n    \"fullName\": \"Nguyễn Văn An\",\n    \"phoneNumber\": \"0901234567\",\n    \"email\": \"nguyenvanan@email.com\",\n    \"address\": \"123 Đường ABC\",\n    \"city\": \"Hà Nội\",\n    \"province\": \"Hà Nội\",\n    \"postalCode\": \"100000\"\n  },\n  \"vehicleData\": {\n    \"variant\": \"Standard Range Plus\",\n    \"color\": \"Pearl White\",\n    \"batteryCapacity\": \"54 kWh\",\n    \"odometerKm\": 15000,\n    \"motorType\": \"Electric\",\n    \"chargingType\": \"AC\",\n    \"maxChargingPower\": 11,\n    \"drivingRange\": 400,\n    \"driveTrain\": \"RWD\"\n  },\n  \"warrantyInfo\": {\n    \"startDate\": \"2023-01-15\",\n    \"endDate\": \"2028-01-15\",\n    \"kmLimit\": 100000,\n    \"batteryFullCoverage\": true,\n    \"motorFullCoverage\": true,\n    \"inverterFullCoverage\": true,\n    \"bmsFullCoverage\": true,\n    \"chargerPartialCoverage\": true\n  }\n}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle registered successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class), examples = @ExampleObject(name = "Success Response", value = "{\n  \"vehicleId\": 4,\n  \"vin\": \"1HGBH41JXMN109186\",\n  \"model\": \"Model 3\",\n  \"variant\": \"Standard Range Plus\",\n  \"modelYear\": 2023,\n  \"currentOdometerKm\": 15000,\n  \"warrantyStartDate\": \"2023-01-15\",\n  \"warrantyEndDate\": \"2028-01-15\",\n  \"warrantyStatus\": \"Active\",\n  \"image\": \"https://cdn.example.com/vehicles/model3.jpg\",\n  \"customer\": {\n    \"fullName\": \"Nguyễn Văn An\",\n    \"phoneNumber\": \"0901234567\",\n    \"email\": \"nguyenvanan@email.com\",\n    \"address\": \"123 Đường ABC\",\n    \"city\": \"Hà Nội\",\n    \"province\": \"Hà Nội\",\n    \"postalCode\": \"100000\"\n  },\n  \"components\": [],\n  \"serviceHistory\": [],\n  \"warrantyClaims\": []\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(examples = @ExampleObject(name = "Error Response", value = "{\"error\": \"VIN, OEM ID, model, and model year are required\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token", content = @Content(examples = @ExampleObject(name = "Unauthorized", value = "{\"error\": \"Missing or invalid Authorization header\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions", content = @Content(examples = @ExampleObject(name = "Forbidden", value = "{\"error\": \"Only Admin, EVM_Staff or SC_Staff can register vehicles\"}")))
    })
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody CreateVehicleRequest body) {
        try {
            // Authorization: Only Admin, EVM_Staff, SC_Staff can register vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            Object roleObj = session.get("role");
            if (roleObj == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Insufficient permissions", request.getRequestURI()));
            }

            UserRole currentRole;
            if (roleObj instanceof UserRole roleEnum) {
                currentRole = roleEnum;
            } else if (roleObj instanceof String roleStr) {
                try {
                    currentRole = UserRole.valueOf(roleStr);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Insufficient permissions", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin, EVM_Staff or SC_Staff can register vehicles",
                                request.getRequestURI()));
            }

            VehicleRegisterRequest req = new VehicleRegisterRequest();
            req.setVin(body.getVin());
            req.setOemId(body.getOemId());
            req.setModel(body.getModel());
            req.setModelYear(body.getModelYear());
            // No longer using customerId mapping

            // Set customer information directly
            if (body.getCustomerInfo() != null) {
                req.setCustomerFullName(body.getCustomerInfo().getFullName());
                req.setCustomerPhoneNumber(body.getCustomerInfo().getPhoneNumber());
                req.setCustomerEmail(body.getCustomerInfo().getEmail());
                req.setCustomerAddress(body.getCustomerInfo().getAddress());
                req.setCustomerCity(body.getCustomerInfo().getCity());
                req.setCustomerProvince(body.getCustomerInfo().getProvince());
                req.setCustomerPostalCode(body.getCustomerInfo().getPostalCode());
            }
            if (body.getVehicleData() != null) {
                req.setVariant(body.getVehicleData().getVariant());
                req.setColor(body.getVehicleData().getColor());
                req.setBatteryCapacity(body.getVehicleData().getBatteryCapacity());
                req.setOdometerKm(body.getVehicleData().getOdometerKm());
            }
            if (body.getWarrantyInfo() != null) {
                req.setWarrantyStartDate(body.getWarrantyInfo().getStartDate());
                req.setWarrantyEndDate(body.getWarrantyInfo().getEndDate());
                req.setKmLimit(body.getWarrantyInfo().getKmLimit());
            }
            // Map image if provided
            if (body.getImage() != null) {
                req.setImage(body.getImage());
            }

            VehicleDetailResponse res = vehicleService.registerVehicleByVin(req);
            return ResponseEntity.ok(res);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiErrorResponse.conflict("Data integrity violation: duplicate or constraint issue",
                            getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/{vin}")
    @Operation(summary = "Get Vehicle by VIN", description = "Retrieve detailed vehicle information by Vehicle Identification Number (VIN). Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle retrieved successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> getVehicleByVin(@PathVariable("vin") String vin) {
        try {
            VehicleDetailResponse res = vehicleService.getVehicleByVin(vin);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/{vin}/service-history")
    @Operation(summary = "Get Vehicle Service History", description = "Retrieve service records for a vehicle by VIN. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186")
    })
    public ResponseEntity<?> getVehicleServiceHistory(@PathVariable("vin") String vin) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff, SC_Technician can view
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            VehicleDetailResponse vehicle = vehicleService.getVehicleByVin(vin);
            if (vehicle == null || vehicle.getVehicleId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Vehicle not found", request.getRequestURI()));
            }
            return ResponseEntity.ok(serviceRecordService.getByVehicle(vehicle.getVehicleId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/{vin}/claim-history")
    @Operation(summary = "Get Vehicle Claim History", description = "Retrieve warranty claim history for a vehicle by VIN. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186")
    })
    public ResponseEntity<?> getVehicleClaimHistory(@PathVariable("vin") String vin) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff can view
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            VehicleDetailResponse vehicle = vehicleService.getVehicleByVin(vin);
            if (vehicle == null || vehicle.getVehicleId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Vehicle not found", request.getRequestURI()));
            }
            return ResponseEntity.ok(warrantyClaimService.getClaimHistory(vehicle.getVehicleId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), getCurrentPath()));
        }
    }

    @PutMapping("/{vin}/odometer")
    @Operation(summary = "Update Vehicle Odometer", description = "Update the odometer reading for a specific vehicle identified by VIN. Roles: Admin, EVM_Staff, SC_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186"),
            @Parameter(name = "km", description = "New odometer reading in kilometers", required = true, example = "50000")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odometer updated successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class), examples = @ExampleObject(name = "Success Response", value = "{\n  \"vehicleId\": 1,\n  \"vin\": \"1HGBH41JXMN109186\",\n  \"model\": \"Model 3\",\n  \"variant\": \"Standard Range Plus\",\n  \"modelYear\": 2023,\n  \"currentOdometerKm\": 50000,\n  \"warrantyStartDate\": \"2023-01-15\",\n  \"warrantyEndDate\": \"2028-01-15\",\n  \"warrantyStatus\": \"Active\",\n  \"image\": \"https://cdn.example.com/vehicles/model3.jpg\",\n  \"customer\": {\n    \"fullName\": \"John Doe\"\n  }\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid odometer reading or VIN"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> updateOdometer(@PathVariable("vin") String vin,
            @RequestParam("km") Integer km) {
        try {
            if (vin == null || vin.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("VIN is required", getCurrentPath()));
            }
            if (km == null || km < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Invalid odometer reading", getCurrentPath()));
            }

            boolean success = vehicleService.updateOdometer(vin, km, "manual_update");
            if (!success) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Vehicle not found", getCurrentPath()));
            }

            // Return the updated vehicle data
            VehicleDetailResponse updatedVehicle = vehicleService.getVehicleByVin(vin);
            return ResponseEntity.ok(updatedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping
    @Operation(summary = "Get All Vehicles", description = "Retrieve a list of all vehicles with optional filtering and pagination. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "oemId", description = "Filter by OEM ID", required = false, example = "1"),
            @Parameter(name = "customerId", description = "Filter by customer ID", required = false, example = "1"),
            @Parameter(name = "model", description = "Filter by vehicle model", required = false, example = "Model 3"),
            @Parameter(name = "status", description = "Filter by vehicle status", required = false, example = "active"),
            @Parameter(name = "limit", description = "Number of results per page (default: 20, max: 100)", required = false, example = "20"),
            @Parameter(name = "offset", description = "Number of results to skip (default: 0)", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<?> getAllVehicles(
            @RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        try {
            // Authorization: All authenticated users can view vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            // Validate pagination parameters
            if (limit < 1 || limit > 100) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Limit must be between 1 and 100", getCurrentPath()));
            }
            if (offset < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Offset must be non-negative", getCurrentPath()));
            }

            List<VehicleResponse> vehicles;

            if (oemId != null) {
                vehicles = vehicleService.getVehiclesByOem(oemId);
            } else if (customerId != null) {
                vehicles = vehicleService.getVehiclesByCustomer(customerId);
            } else if (status != null) {
                vehicles = vehicleService.getVehiclesByStatus(status, 1L); // Default OEM ID
            } else {
                // Get all vehicles with pagination (this would need to be implemented in
                // service)
                // For now, we'll get vehicles by default OEM
                vehicles = vehicleService.getVehiclesByOem(1L);
            }

            // Apply pagination
            int totalSize = vehicles.size();
            int endIndex = Math.min(offset + limit, totalSize);
            if (offset >= totalSize) {
                vehicles = List.of();
            } else {
                vehicles = vehicles.subList(offset, endIndex);
            }

            return ResponseEntity.ok(Map.of(
                    "vehicles", vehicles,
                    "total", totalSize,
                    "limit", limit,
                    "offset", offset));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @PutMapping("/{vin}")
    @Operation(summary = "Update Vehicle", description = "Update vehicle information by VIN. Roles: Admin, EVM_Staff, SC_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Vehicle update data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Update Vehicle Example", value = "{\n  \"model\": \"Model 3 Updated\",\n  \"modelYear\": 2024,\n  \"image\": \"https://cdn.example.com/vehicles/model3-updated.jpg\",\n  \"customerInfo\": {\n    \"fullName\": \"Trần Thị Bình\",\n    \"phoneNumber\": \"0907654321\",\n    \"email\": \"tranthibinh@email.com\",\n    \"address\": \"456 Đường XYZ\",\n    \"city\": \"TP.HCM\",\n    \"province\": \"TP.HCM\",\n    \"postalCode\": \"700000\"\n  },\n  \"vehicleData\": {\n    \"variant\": \"Long Range\",\n    \"color\": \"Deep Blue Metallic\",\n    \"batteryCapacity\": \"75 kWh\"\n  },\n  \"warrantyInfo\": {\n    \"startDate\": \"2024-01-15\",\n    \"endDate\": \"2029-01-15\",\n    \"kmLimit\": 120000\n  }\n}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> updateVehicle(@PathVariable("vin") String vin,
            @Valid @RequestBody UpdateVehicleRequest body) {
        try {
            // Authorization: Only Admin, EVM_Staff, SC_Staff can update vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            UserRole currentRole = convertToUserRole(session.get("role"));
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin, EVM_Staff or SC_Staff can update vehicles",
                                request.getRequestURI()));
            }

            // Convert UpdateVehicleRequest to VehicleRegisterRequest for service layer
            VehicleRegisterRequest updateRequest = new VehicleRegisterRequest();
            updateRequest.setVin(vin);
            updateRequest.setModel(body.getModel());
            updateRequest.setModelYear(body.getModelYear());
            // No longer using customerId mapping

            // Set customer information directly
            if (body.getCustomerInfo() != null) {
                updateRequest.setCustomerFullName(body.getCustomerInfo().getFullName());
                updateRequest.setCustomerPhoneNumber(body.getCustomerInfo().getPhoneNumber());
                updateRequest.setCustomerEmail(body.getCustomerInfo().getEmail());
                updateRequest.setCustomerAddress(body.getCustomerInfo().getAddress());
                updateRequest.setCustomerCity(body.getCustomerInfo().getCity());
                updateRequest.setCustomerProvince(body.getCustomerInfo().getProvince());
                updateRequest.setCustomerPostalCode(body.getCustomerInfo().getPostalCode());
            }

            if (body.getVehicleData() != null) {
                updateRequest.setVariant(body.getVehicleData().getVariant());
                updateRequest.setColor(body.getVehicleData().getColor());
                updateRequest.setBatteryCapacity(body.getVehicleData().getBatteryCapacity());
                updateRequest.setOdometerKm(body.getVehicleData().getOdometerKm());
            }

            if (body.getWarrantyInfo() != null) {
                updateRequest.setWarrantyStartDate(body.getWarrantyInfo().getStartDate());
                updateRequest.setWarrantyEndDate(body.getWarrantyInfo().getEndDate());
                updateRequest.setKmLimit(body.getWarrantyInfo().getKmLimit());
            }
            // Map image if provided in update
            if (body.getImage() != null) {
                updateRequest.setImage(body.getImage());
            }

            VehicleDetailResponse res = vehicleService.updateVehicleInfo(vin, updateRequest);
            return ResponseEntity.ok(res);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiErrorResponse.conflict("Data integrity violation: duplicate or constraint issue",
                            getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @DeleteMapping("/{vin}")
    @Operation(summary = "Delete Vehicle", description = "Delete a vehicle from the system by VIN. Roles: Admin only. This action cannot be undone.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186"),
            @Parameter(name = "reason", description = "Reason for deletion", required = true, example = "Vehicle scrapped")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid VIN or missing reason"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> deleteVehicle(@PathVariable("vin") String vin,
            @RequestParam(value = "reason", required = true) String reason) {
        try {
            if (vin == null || vin.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("VIN is required", getCurrentPath()));
            }
            if (reason == null || reason.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Reason for deletion is required", getCurrentPath()));
            }

            // Authorization: Only Admin and EVM_Staff can delete vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            UserRole currentRole = convertToUserRole(session.get("role"));
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can delete vehicles",
                                request.getRequestURI()));
            }

            boolean success = vehicleService.deactivateVehicle(vin, reason);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Vehicle deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Vehicle not found", getCurrentPath()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/vins")
    @Operation(summary = "Get All VIN IDs", description = "Retrieve a list of all VIN IDs in the system with optional filtering. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "oemId", description = "Filter by OEM ID", required = false, example = "1"),
            @Parameter(name = "customerId", description = "Filter by customer ID", required = false, example = "1"),
            @Parameter(name = "model", description = "Filter by vehicle model", required = false, example = "Model 3"),
            @Parameter(name = "status", description = "Filter by vehicle status", required = false, example = "active"),
            @Parameter(name = "limit", description = "Number of results per page (default: 100, max: 1000)", required = false, example = "100"),
            @Parameter(name = "offset", description = "Number of results to skip (default: 0)", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "VIN IDs retrieved successfully", content = @Content(examples = @ExampleObject(name = "Success Response", value = "{\n  \"vins\": [\n    \"1HGBH41JXMN109186\",\n    \"1HGBH41JXMN109187\",\n    \"1HGBH41JXMN109188\"\n  ],\n  \"total\": 3,\n  \"limit\": 100,\n  \"offset\": 0\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<?> getAllVinIds(
            @RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        try {
            // Authorization: All authenticated users can view VIN IDs
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            // Validate pagination parameters
            if (limit < 1 || limit > 1000) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Limit must be between 1 and 1000", getCurrentPath()));
            }
            if (offset < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Offset must be non-negative", getCurrentPath()));
            }

            List<VehicleResponse> vehicles;

            if (oemId != null) {
                vehicles = vehicleService.getVehiclesByOem(oemId);
            } else if (customerId != null) {
                vehicles = vehicleService.getVehiclesByCustomer(customerId);
            } else if (status != null) {
                vehicles = vehicleService.getVehiclesByStatus(status, 1L); // Default OEM ID
            } else {
                // Get all vehicles by default OEM
                vehicles = vehicleService.getVehiclesByOem(1L);
            }

            // Extract VIN IDs from vehicles
            List<String> vins = vehicles.stream()
                    .map(VehicleResponse::getVin)
                    .filter(vin -> vin != null && !vin.trim().isEmpty())
                    .toList();

            // Apply pagination
            int totalSize = vins.size();
            int endIndex = Math.min(offset + limit, totalSize);
            List<String> paginatedVins;
            if (offset >= totalSize) {
                paginatedVins = List.of();
            } else {
                paginatedVins = vins.subList(offset, endIndex);
            }

            return ResponseEntity.ok(Map.of(
                    "vins", paginatedVins,
                    "total", totalSize,
                    "limit", limit,
                    "offset", offset));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    // ==================== SOFT DELETE ENDPOINTS ====================

    @DeleteMapping("/{vin}/soft-delete")
    @Operation(summary = "Soft Delete Vehicle", description = "Soft delete a vehicle by VIN (sets status to 'deleted'). The vehicle can be restored later. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle soft deleted successfully", content = @Content(examples = @ExampleObject(name = "Success Response", value = "{\n  \"success\": true,\n  \"message\": \"Vehicle soft deleted successfully\",\n  \"vin\": \"1HGBH41JXMN109186\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid VIN"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<?> softDeleteVehicle(@PathVariable("vin") String vin) {
        try {
            if (vin == null || vin.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("VIN is required", getCurrentPath()));
            }

            // Authorization: Only Admin and EVM_Staff can soft delete vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            UserRole currentRole = convertToUserRole(session.get("role"));
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can soft delete vehicles",
                                request.getRequestURI()));
            }

            boolean success = vehicleService.softDeleteVehicle(vin);
            if (success) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Vehicle soft deleted successfully",
                        "vin", vin));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Vehicle not found", getCurrentPath()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @PostMapping("/{vin}/restore")
    @Operation(summary = "Restore Vehicle", description = "Restore a soft deleted vehicle by VIN (sets status to 'active'). Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle restored successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid VIN"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found or not deleted")
    })
    public ResponseEntity<?> restoreVehicle(@PathVariable("vin") String vin) {
        try {
            if (vin == null || vin.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("VIN is required", getCurrentPath()));
            }

            // Authorization: Only Admin and EVM_Staff can restore vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            UserRole currentRole = convertToUserRole(session.get("role"));
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can restore vehicles",
                                request.getRequestURI()));
            }

            boolean success = vehicleService.restoreVehicle(vin);
            if (success) {
                VehicleDetailResponse restoredVehicle = vehicleService.getVehicleByVin(vin);
                return ResponseEntity.ok(restoredVehicle);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Vehicle not found or not deleted", getCurrentPath()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/deleted")
    @Operation(summary = "Get Deleted Vehicles", description = "Retrieve a list of soft deleted vehicles. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "limit", description = "Number of results per page (default: 20, max: 100)", required = false, example = "20"),
            @Parameter(name = "offset", description = "Number of results to skip (default: 0)", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted vehicles retrieved successfully", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<?> getDeletedVehicles(
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        try {
            // Authorization: Only Admin and EVM_Staff can view deleted vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            UserRole currentRole = convertToUserRole(session.get("role"));
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can view deleted vehicles",
                                request.getRequestURI()));
            }

            // Validate pagination parameters
            if (limit < 1 || limit > 100) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Limit must be between 1 and 100", getCurrentPath()));
            }
            if (offset < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Offset must be non-negative", getCurrentPath()));
            }

            List<VehicleResponse> deletedVehicles = vehicleService.getDeletedVehicles();

            // Apply pagination
            int totalSize = deletedVehicles.size();
            int endIndex = Math.min(offset + limit, totalSize);
            if (offset >= totalSize) {
                deletedVehicles = List.of();
            } else {
                deletedVehicles = deletedVehicles.subList(offset, endIndex);
            }

            return ResponseEntity.ok(Map.of(
                    "vehicles", deletedVehicles,
                    "total", totalSize,
                    "limit", limit,
                    "offset", offset));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Vehicles by Status", description = "Retrieve vehicles filtered by status (active, inactive, deleted). Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "status", description = "Vehicle status (active, inactive, deleted)", required = true, example = "active"),
            @Parameter(name = "limit", description = "Number of results per page (default: 20, max: 100)", required = false, example = "20"),
            @Parameter(name = "offset", description = "Number of results to skip (default: 0)", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully", content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status or parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<?> getVehiclesByStatus(
            @PathVariable("status") String status,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        try {
            // Validate status parameter
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Status is required", getCurrentPath()));
            }

            if (!status.equals("active") && !status.equals("inactive") && !status.equals("deleted")) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Status must be 'active', 'inactive', or 'deleted'",
                                getCurrentPath()));
            }

            // Authorization: Only Admin and EVM_Staff can filter by status
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                                request.getRequestURI()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }

            UserRole currentRole = convertToUserRole(session.get("role"));
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", request.getRequestURI()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can filter vehicles by status",
                                request.getRequestURI()));
            }

            // Validate pagination parameters
            if (limit < 1 || limit > 100) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Limit must be between 1 and 100", getCurrentPath()));
            }
            if (offset < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Offset must be non-negative", getCurrentPath()));
            }

            List<VehicleResponse> vehicles = vehicleService.getVehiclesByStatus(status, null);

            // Apply pagination
            int totalSize = vehicles.size();
            int endIndex = Math.min(offset + limit, totalSize);
            if (offset >= totalSize) {
                vehicles = List.of();
            } else {
                vehicles = vehicles.subList(offset, endIndex);
            }

            return ResponseEntity.ok(Map.of(
                    "vehicles", vehicles,
                    "status", status,
                    "total", totalSize,
                    "limit", limit,
                    "offset", offset));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }
}
