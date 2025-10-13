package org.example.controller;

import java.util.Map;

import org.example.models.dto.request.CreateVehicleRequest;
import org.example.models.dto.request.VehicleRegisterRequest;
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

    @PostMapping
    @Operation(summary = "Register Vehicle", description = "Register a new vehicle in the system using VIN and model information.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Vehicle registration data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Register Vehicle Example", value = "{\"vin\": \"1HGBH41JXMN109186\", \"model\": \"Model 3\", \"ownerName\": \"John Doe\", \"ownerPhone\": \"+1234567890\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle registered successfully", content = @Content(schema = @Schema(implementation = VehicleDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> registerVehicle(@RequestBody CreateVehicleRequest body) {
        try {
            // Authorization: Only Admin, EVM_Staff, SC_Staff can register vehicles
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin, EVM_Staff or SC_Staff can register vehicles"));
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
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
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            VehicleDetailResponse vehicle = vehicleService.getVehicleByVin(vin);
            if (vehicle == null || vehicle.getVehicleId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Vehicle not found"));
            }
            return ResponseEntity.ok(serviceRecordService.getByVehicle(vehicle.getVehicleId()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
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
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            VehicleDetailResponse vehicle = vehicleService.getVehicleByVin(vin);
            if (vehicle == null || vehicle.getVehicleId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Vehicle not found"));
            }
            return ResponseEntity.ok(warrantyClaimService.getClaimHistory(vehicle.getVehicleId()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
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
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid odometer reading"));
            }
            boolean success = vehicleService.updateOdometer(vin, km, "manual_update");
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
