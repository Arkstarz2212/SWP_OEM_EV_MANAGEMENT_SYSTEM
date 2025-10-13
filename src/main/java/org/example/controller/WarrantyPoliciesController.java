package org.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;
import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IWarrantyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@RequestMapping("/api/warranty-policies")
@Tag(name = "Warranty Policies", description = "Warranty policy management including creation, lookup, and validation")
public class WarrantyPoliciesController {

    @Autowired
    private IWarrantyPolicyService warrantyPolicyService;

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping
    @Operation(summary = "Create Warranty Policy", description = "Create a new warranty policy for a vehicle model or component category.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Warranty policy creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"model\": \"Model 3\", \"componentCategory\": \"BATTERY\", \"monthsCoverage\": 24, \"kmCoverage\": 50000, \"notes\": \"OEM standard warranty\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy created successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicy.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> createPolicy(@RequestBody Map<String, Object> body) {
        try {
            // RBAC: Admin, EVM_Staff
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole) ||
                    (((UserRole) roleObj) != UserRole.Admin && ((UserRole) roleObj) != UserRole.EVM_Staff)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin or EVM_Staff can create policies"));
            }
            WarrantyPolicyCreateRequest req = new WarrantyPolicyCreateRequest();
            Object model = body.get("model");
            Object componentCategory = body.get("componentCategory");
            Object monthsCoverage = body.get("monthsCoverage");
            Object kmCoverage = body.get("kmCoverage");
            Object notes = body.get("notes");

            req.setModel(model != null ? String.valueOf(model) : null);
            req.setComponentCategory(componentCategory != null ? String.valueOf(componentCategory) : null);
            req.setMonthsCoverage(monthsCoverage != null ? Integer.valueOf(monthsCoverage.toString()) : null);
            req.setKmCoverage(kmCoverage != null ? Integer.valueOf(kmCoverage.toString()) : null);
            req.setNotes(notes != null ? String.valueOf(notes) : null);

            WarrantyPolicy res = warrantyPolicyService.createWarrantyPolicy(req);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Policy by ID", description = "Retrieve a warranty policy by its unique identifier.", parameters = {
            @Parameter(name = "id", description = "Policy ID", required = true, example = "1") })
    public ResponseEntity<?> getPolicyById(@PathVariable("id") Long id) {
        try {
            // RBAC: any authenticated role
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Optional<WarrantyPolicy> res = warrantyPolicyService.getPolicyById(id);
            if (res.isPresent()) {
                return ResponseEntity.ok(res.get());
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Policy not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate Warranty Coverage", description = "Validate whether a vehicle model and component category are covered under warranty for given usage.", parameters = {
            @Parameter(name = "model", description = "Vehicle model", required = true, example = "Model 3"),
            @Parameter(name = "category", description = "Component category", required = true, example = "BATTERY"),
            @Parameter(name = "months", description = "Months in service", required = true, example = "18"),
            @Parameter(name = "km", description = "Kilometers driven", required = true, example = "30000")
    })
    public ResponseEntity<?> validateWarranty(@RequestParam("model") String model,
            @RequestParam("category") String category,
            @RequestParam("months") Integer months,
            @RequestParam("km") Integer km) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            // Note: This would need proper validation logic based on available methods
            boolean isValid = true; // Placeholder
            return ResponseEntity.ok(Map.of("valid", isValid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/model/{model}")
    @Operation(summary = "List Policies by Model", description = "Get all warranty policies applicable for a specific vehicle model.", parameters = {
            @Parameter(name = "model", description = "Vehicle model", required = true, example = "Model 3") })
    public ResponseEntity<?> getPoliciesByModel(@PathVariable("model") String model) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            List<WarrantyPolicy> policies = warrantyPolicyService.getPoliciesByModel(model, 1L); // Default OEM
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/component/{component}")
    @Operation(summary = "List Policies by Component", description = "Get all warranty policies applicable for a specific component category.", parameters = {
            @Parameter(name = "component", description = "Component category", required = true, example = "BATTERY") })
    public ResponseEntity<?> getPoliciesByComponent(@PathVariable("component") String component) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            List<WarrantyPolicy> policies = warrantyPolicyService.getPoliciesByComponent(component, 1L); // Default OEM
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats/model/{model}")
    @Operation(summary = "Policy Statistics by Model", description = "Get simple statistics for warranty policies by model (demo endpoint).", parameters = {
            @Parameter(name = "model", description = "Vehicle model", required = true, example = "Model 3") })
    public ResponseEntity<?> getPolicyStatsByModel(@PathVariable("model") String model) {
        try {
            // Note: This would need proper statistics calculation
            Map<String, Object> stats = Map.of("model", model, "count", 0);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "List Warranty Policies", description = "List policies by OEM or return active policies for default OEM when not specified.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID to filter policies", required = false, example = "1"),
            @Parameter(name = "active", description = "Filter active policies", required = false, example = "true")
    })
    public ResponseEntity<?> listPolicies(@RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "active", required = false) Boolean active) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            List<WarrantyPolicy> policies;
            if (oemId != null) {
                policies = warrantyPolicyService.getPoliciesByOem(oemId);
            } else {
                policies = warrantyPolicyService.getActivePolicies(1L); // Default OEM
            }
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/by-vin/{vin}")
    @Operation(summary = "Get warranty info by VIN (read-only)")
    public ResponseEntity<?> getWarrantyInfoByVin(@PathVariable("vin") String vin) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            // Delegate to service to read from vehicle.warranty_info
            java.time.LocalDate expiry = warrantyPolicyService.getWarrantyExpiryDate(vin, null);
            boolean under = warrantyPolicyService.isVehicleUnderWarranty(vin);
            return ResponseEntity.ok(Map.of("vin", vin, "underWarranty", under, "expiryDate", expiry));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/by-model/{model}")
    @Operation(summary = "Get aggregated warranty info by model (read-only)")
    public ResponseEntity<?> getWarrantyInfoByModel(@PathVariable("model") String model) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            // For now return counts via policy service active list filtered by model name
            // if any
            java.util.List<org.example.models.core.WarrantyPolicy> policies = warrantyPolicyService
                    .getPoliciesByModel(model, 1L);
            return ResponseEntity.ok(Map.of("model", model, "policyCount", policies.size()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
