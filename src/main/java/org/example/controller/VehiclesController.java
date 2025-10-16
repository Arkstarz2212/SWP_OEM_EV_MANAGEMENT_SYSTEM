package org.example.controller;

import java.util.Map;

import org.example.models.dto.request.CreateVehicleRequest;
import org.example.models.dto.request.VehicleRegisterRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.VehicleDetailResponse;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IServiceRecordService;
import org.example.service.IService.IVehicleService;
import org.example.service.IService.IWarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Register Vehicle", description = "Register a new vehicle in the system using VIN and model information.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Vehicle registration data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Register Vehicle Example", value = "{\n  \"vin\": \"1HGBH41JXMN109186\",\n  \"oemId\": 1,\n  \"model\": \"Model 3\",\n  \"modelYear\": 2023,\n  \"customerId\": 1,\n  \"vehicleData\": {\n    \"variant\": \"Standard Range Plus\",\n    \"color\": \"Pearl White\",\n    \"batteryCapacity\": \"54 kWh\",\n    \"odometerKm\": 15000,\n    \"motorType\": \"Electric\",\n    \"chargingType\": \"AC\",\n    \"maxChargingPower\": 11,\n    \"drivingRange\": 400,\n    \"driveTrain\": \"RWD\"\n  },\n  \"warrantyInfo\": {\n    \"startDate\": \"2023-01-15\",\n    \"endDate\": \"2028-01-15\",\n    \"kmLimit\": 100000,\n    \"batteryFullCoverage\": true,\n    \"motorFullCoverage\": true,\n    \"inverterFullCoverage\": true,\n    \"bmsFullCoverage\": true,\n    \"chargerPartialCoverage\": true\n  }\n}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle registered successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class), examples = @ExampleObject(name = "Success Response", value = "{\n  \"vehicleId\": 4,\n  \"vin\": \"1HGBH41JXMN109186\",\n  \"model\": \"Model 3\",\n  \"variant\": \"Standard Range Plus\",\n  \"modelYear\": 2023,\n  \"currentOdometerKm\": 15000,\n  \"warrantyStartDate\": \"2023-01-15\",\n  \"warrantyEndDate\": \"2028-01-15\",\n  \"warrantyStatus\": \"Active\",\n  \"customer\": {\n    \"customerId\": 1,\n    \"fullName\": \"John Doe\"\n  },\n  \"components\": [],\n  \"serviceHistory\": [],\n  \"warrantyClaims\": []\n}"))),
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
            req.setCustomerId(body.getCustomerId());
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
    @Operation(summary = "Get Vehicle by VIN", description = "Retrieve detailed vehicle information by Vehicle Identification Number (VIN).", parameters = {
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
    @Operation(summary = "Get Vehicle Service History", description = "Retrieve service records for a vehicle by VIN.", parameters = {
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
    @Operation(summary = "Get Vehicle Claim History", description = "Retrieve warranty claim history for a vehicle by VIN.", parameters = {
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
    @Operation(summary = "Update Vehicle Odometer", description = "Update the odometer reading for a specific vehicle identified by VIN.", parameters = {
            @Parameter(name = "vin", description = "Vehicle Identification Number", required = true, example = "1HGBH41JXMN109186"),
            @Parameter(name = "km", description = "New odometer reading in kilometers", required = true, example = "50000")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odometer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid odometer reading or VIN")
    })
    public ResponseEntity<?> updateOdometer(@PathVariable("vin") String vin,
            @RequestParam("km") Integer km) {
        try {
            if (km == null || km < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Invalid odometer reading", getCurrentPath()));
            }
            boolean success = vehicleService.updateOdometer(vin, km, "manual_update");
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        }
    }
}
