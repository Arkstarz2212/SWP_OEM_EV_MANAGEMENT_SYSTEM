package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.dto.request.WarrantyPolicyUpdateRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.WarrantyPolicyResponse;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IWarrantyPolicyService;
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
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warranty-policies")
@Tag(name = "Warranty Policies", description = "Warranty policies management APIs for creating, updating, and managing warranty policies")
public class WarrantyPoliciesController {

    @Autowired
    private IWarrantyPolicyService warrantyPolicyService;

    @Autowired
    private IAuthenticationService authenticationService;

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
     * Helper method to check if user has Admin or EVM_Staff role
     */
    private boolean hasAdminOrEVMStaffRole(UserRole role) {
        return role == UserRole.Admin || role == UserRole.EVM_Staff;
    }

    /**
     * Helper method to get current user role from request
     */
    private UserRole getCurrentUserRole() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return null;
            }
            Object roleObj = session.get("role");
            return convertToUserRole(roleObj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Helper method to verify authentication
     */
    private ResponseEntity<?> verifyAuthentication() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiErrorResponse.unauthorized("Missing or invalid Authorization header",
                            request.getRequestURI()));
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        java.util.Map<String, Object> session = authenticationService.getSessionByToken(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
        }
        return null;
    }

    @PostMapping
    @Operation(summary = "Create Warranty Policy", description = "Create a new warranty policy. Roles: Admin, EVM_Staff.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Warranty policy creation data", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = WarrantyPolicyCreateRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy created successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only Admin or EVM_Staff can create policies", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> createPolicy(@Valid @RequestBody WarrantyPolicyCreateRequest request) {
        try {
            // RBAC: Only Admin and EVM_Staff can create policies
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            UserRole currentRole = getCurrentUserRole();
            if (currentRole == null || !hasAdminOrEVMStaffRole(currentRole)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes();
                jakarta.servlet.http.HttpServletRequest request_obj = attributes.getRequest();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can create warranty policies",
                                request_obj.getRequestURI()));
            }

            WarrantyPolicyResponse response = warrantyPolicyService.createPolicy(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), ""));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to create warranty policy: " + e.getMessage(),
                            ""));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Warranty Policy by ID", description = "Retrieve detailed information about a specific warranty policy by its ID. Roles: All authenticated users.", parameters = {
            @Parameter(name = "id", description = "Warranty policy ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getPolicyById(@PathVariable("id") Long id) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            WarrantyPolicyResponse response = warrantyPolicyService.getPolicyById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to retrieve warranty policy: " + e.getMessage(),
                            ""));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Warranty Policy", description = "Update an existing warranty policy. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "id", description = "Warranty policy ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy updated successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only Admin or EVM_Staff can update policies", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updatePolicy(@PathVariable("id") Long id,
            @RequestBody WarrantyPolicyUpdateRequest request) {
        try {
            // RBAC: Only Admin and EVM_Staff can update policies
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            UserRole currentRole = getCurrentUserRole();
            if (currentRole == null || !hasAdminOrEVMStaffRole(currentRole)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes();
                jakarta.servlet.http.HttpServletRequest request_obj = attributes.getRequest();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can update warranty policies",
                                request_obj.getRequestURI()));
            }

            WarrantyPolicyResponse response = warrantyPolicyService.updatePolicy(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), ""));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to update warranty policy: " + e.getMessage(),
                            ""));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Warranty Policy", description = "Delete a warranty policy by its ID. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "id", description = "Warranty policy ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy deleted successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only Admin or EVM_Staff can delete policies", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> deletePolicy(@PathVariable("id") Long id) {
        try {
            // RBAC: Only Admin and EVM_Staff can delete policies
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            UserRole currentRole = getCurrentUserRole();
            if (currentRole == null || !hasAdminOrEVMStaffRole(currentRole)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes();
                jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can delete warranty policies",
                                request.getRequestURI()));
            }

            boolean deleted = warrantyPolicyService.deletePolicy(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Warranty policy deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Warranty policy not found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to delete warranty policy: " + e.getMessage(),
                            ""));
        }
    }

    @GetMapping
    @Operation(summary = "List All Warranty Policies", description = "Retrieve a list of all warranty policies with pagination. Roles: All authenticated users.", parameters = {
            @Parameter(name = "limit", description = "Maximum number of policies to return", required = false, example = "20", schema = @Schema(defaultValue = "20")),
            @Parameter(name = "offset", description = "Number of policies to skip for pagination", required = false, example = "0", schema = @Schema(defaultValue = "0"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getAllPolicies(
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getAllPolicies(limit, offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse
                            .internalServerError("Failed to retrieve warranty policies: " + e.getMessage(), ""));
        }
    }

    @GetMapping("/oem/{oemId}")
    @Operation(summary = "Get Warranty Policies by OEM ID", description = "Retrieve all warranty policies for a specific OEM. Roles: All authenticated users.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID", required = true, example = "1"),
            @Parameter(name = "limit", description = "Maximum number of policies to return", required = false, example = "20"),
            @Parameter(name = "offset", description = "Number of policies to skip for pagination", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getPoliciesByOemId(@PathVariable("oemId") Long oemId,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getPoliciesByOemId(oemId, limit, offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse
                            .internalServerError("Failed to retrieve warranty policies: " + e.getMessage(), ""));
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Get Active Warranty Policies", description = "Retrieve all active warranty policies. Roles: All authenticated users.", parameters = {
            @Parameter(name = "limit", description = "Maximum number of policies to return", required = false, example = "20"),
            @Parameter(name = "offset", description = "Number of policies to skip for pagination", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getActivePolicies(
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getActivePolicies(limit, offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse
                            .internalServerError("Failed to retrieve active warranty policies: " + e.getMessage(), ""));
        }
    }

    @GetMapping("/code/{policyCode}")
    @Operation(summary = "Get Warranty Policy by Policy Code", description = "Retrieve a warranty policy by its policy code. Roles: All authenticated users.", parameters = {
            @Parameter(name = "policyCode", description = "Policy code", required = true, example = "VFS-STD-2024")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getPolicyByPolicyCode(@PathVariable("policyCode") String policyCode) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            WarrantyPolicyResponse response = warrantyPolicyService.getPolicyByPolicyCode(policyCode);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to retrieve warranty policy: " + e.getMessage(),
                            ""));
        }
    }

    @GetMapping("/oem/{oemId}/default")
    @Operation(summary = "Get Default Warranty Policy by OEM ID", description = "Retrieve the default warranty policy for a specific OEM. Roles: All authenticated users.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default warranty policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Default warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getDefaultPolicyByOemId(@PathVariable("oemId") Long oemId) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            WarrantyPolicyResponse response = warrantyPolicyService.getDefaultPolicyByOemId(oemId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse
                            .internalServerError("Failed to retrieve default warranty policy: " + e.getMessage(), ""));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search Warranty Policies", description = "Search warranty policies by keyword (policy name). Roles: All authenticated users.", parameters = {
            @Parameter(name = "keyword", description = "Search keyword", required = true, example = "VinFast"),
            @Parameter(name = "limit", description = "Maximum number of policies to return", required = false, example = "20"),
            @Parameter(name = "offset", description = "Number of policies to skip for pagination", required = false, example = "0")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> searchPolicies(@RequestParam("keyword") String keyword,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
            // RBAC: All authenticated users can view
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            List<WarrantyPolicyResponse> policies = warrantyPolicyService.searchPolicies(keyword, limit, offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to search warranty policies: " + e.getMessage(),
                            ""));
        }
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate Warranty Policy", description = "Activate a warranty policy. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "id", description = "Warranty policy ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy activated successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only Admin or EVM_Staff can activate policies", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> activatePolicy(@PathVariable("id") Long id) {
        try {
            // RBAC: Only Admin and EVM_Staff can activate policies
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            UserRole currentRole = getCurrentUserRole();
            if (currentRole == null || !hasAdminOrEVMStaffRole(currentRole)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes();
                jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can activate warranty policies",
                                request.getRequestURI()));
            }

            boolean success = warrantyPolicyService.activatePolicy(id);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Warranty policy activated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Warranty policy not found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to activate warranty policy: " + e.getMessage(),
                            ""));
        }
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Warranty Policy", description = "Deactivate a warranty policy. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "id", description = "Warranty policy ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy deactivated successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only Admin or EVM_Staff can deactivate policies", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> deactivatePolicy(@PathVariable("id") Long id) {
        try {
            // RBAC: Only Admin and EVM_Staff can deactivate policies
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            UserRole currentRole = getCurrentUserRole();
            if (currentRole == null || !hasAdminOrEVMStaffRole(currentRole)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes();
                jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can deactivate warranty policies",
                                request.getRequestURI()));
            }

            boolean success = warrantyPolicyService.deactivatePolicy(id);
            if (success) {
                return ResponseEntity
                        .ok(Map.of("success", true, "message", "Warranty policy deactivated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Warranty policy not found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse
                            .internalServerError("Failed to deactivate warranty policy: " + e.getMessage(), ""));
        }
    }

    @PostMapping("/{id}/set-default")
    @Operation(summary = "Set Warranty Policy as Default", description = "Set a warranty policy as the default policy for its OEM. Roles: Admin, EVM_Staff.", parameters = {
            @Parameter(name = "id", description = "Warranty policy ID", required = true, example = "1"),
            @Parameter(name = "oemId", description = "OEM ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy set as default successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only Admin or EVM_Staff can set default policies", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> setAsDefault(@PathVariable("id") Long id,
            @RequestParam("oemId") Long oemId) {
        try {
            // RBAC: Only Admin and EVM_Staff can set default policies
            ResponseEntity<?> authError = verifyAuthentication();
            if (authError != null) {
                return authError;
            }

            UserRole currentRole = getCurrentUserRole();
            if (currentRole == null || !hasAdminOrEVMStaffRole(currentRole)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes();
                jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can set default warranty policies",
                                request.getRequestURI()));
            }

            boolean success = warrantyPolicyService.setAsDefault(id, oemId);
            if (success) {
                return ResponseEntity
                        .ok(Map.of("success", true, "message", "Warranty policy set as default successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Warranty policy not found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse
                            .internalServerError("Failed to set default warranty policy: " + e.getMessage(), ""));
        }
    }
}
