package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.PartCatalogCreateRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.PartCatalogResponse;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IPartCatalogService;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/parts")
@Tag(name = "Parts", description = "Parts catalog and inventory management including creation, search, and status updates")
public class PartsController {

    @Autowired
    private IPartCatalogService partCatalogService;

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

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping
    @Operation(summary = "Create New Part", description = "Create a new part in the catalog with part number, name, category, description, unit cost, and manufacturer information. Roles: EVM_Staff.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Part creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create Part Example", value = "{\"partNumber\": \"BAT001\", \"name\": \"Battery Pack\", \"category\": \"BATTERY\", \"description\": \"High capacity battery pack\", \"unitCost\": 1500.00, \"oemId\": 1, \"manufacturer\": \"Tesla\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part created successfully", content = @Content(schema = @Schema(implementation = PartCatalogResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> createPart(@Valid @RequestBody PartCatalogCreateRequest req) {
        try {
            // RBAC: Admin, EVM_Staff can create parts
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
            Object roleObj = session.get("role");
            UserRole currentRole = convertToUserRole(roleObj);
            if (currentRole == null || (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can create parts",
                                request.getRequestURI()));
            }
            PartCatalogResponse res = partCatalogService.createPart(req);
            return ResponseEntity.ok(res);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiErrorResponse.conflict("Data integrity violation: duplicate or constraint issue", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), ""));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Part by ID", description = "Retrieve detailed information about a specific part by its unique identifier. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.", parameters = {
            @Parameter(name = "id", description = "Part ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part retrieved successfully", content = @Content(schema = @Schema(implementation = PartCatalogResponse.class))),
            @ApiResponse(responseCode = "404", description = "Part not found")
    })
    public ResponseEntity<?> getPartById(@PathVariable("id") Long id) {
        try {
            // RBAC: any authenticated role
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
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            PartCatalogResponse res = partCatalogService.getPartById(id);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        }
    }

    @GetMapping("/number/{partNumber}")
    @Operation(summary = "Get Part by Part Number", description = "Retrieve detailed information about a specific part by its part number. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.", parameters = {
            @Parameter(name = "partNumber", description = "Part number", required = true, example = "BAT001")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part retrieved successfully", content = @Content(schema = @Schema(implementation = PartCatalogResponse.class))),
            @ApiResponse(responseCode = "404", description = "Part not found")
    })
    public ResponseEntity<?> getPartByNumber(@PathVariable("partNumber") String partNumber) {
        try {
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
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            PartCatalogResponse res = partCatalogService.getPartByPartNumber(partNumber);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        }
    }

    @GetMapping
    @Operation(summary = "Search Parts", description = "Search and filter parts by keyword, category, or manufacturer with pagination support. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.", parameters = {
            @Parameter(name = "keyword", description = "Search keyword for part name or description", required = false, example = "battery"),
            @Parameter(name = "category", description = "Filter by part category", required = false, example = "BATTERY"),
            @Parameter(name = "manufacturer", description = "Filter by manufacturer", required = false, example = "Tesla"),
            @Parameter(name = "limit", description = "Maximum number of parts to return", required = false, example = "20", schema = @Schema(defaultValue = "20")),
            @Parameter(name = "offset", description = "Number of parts to skip for pagination", required = false, example = "0", schema = @Schema(defaultValue = "0"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parts retrieved successfully", content = @Content(schema = @Schema(implementation = PartCatalogResponse.class))),
            @ApiResponse(responseCode = "404", description = "No parts found")
    })
    public ResponseEntity<?> searchParts(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "manufacturer", required = false) String manufacturer,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
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
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            List<PartCatalogResponse> parts;
            if (keyword != null) {
                parts = partCatalogService.searchParts(keyword, category, manufacturer, limit, offset);
            } else if (category != null) {
                parts = partCatalogService.getPartsByCategory(category);
            } else {
                parts = partCatalogService.getAllParts(limit, offset);
            }

            return ResponseEntity.ok(parts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        }
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate Part", description = "Activate a part in the catalog. Roles: Admin, EVM_Staff.")
    public ResponseEntity<?> activatePart(@PathVariable("id") Long id) {
        try {
            // RBAC: Admin, EVM_Staff
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
            Object roleObj = session.get("role");
            UserRole currentRole = convertToUserRole(roleObj);
            if (currentRole == null || (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can activate parts",
                                request.getRequestURI()));
            }
            boolean success = partCatalogService.activatePart(id);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), ""));
        }
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Part", description = "Deactivate a part in the catalog. Roles: Admin, EVM_Staff.")
    public ResponseEntity<?> deactivatePart(@PathVariable("id") Long id) {
        try {
            // RBAC: Admin, EVM_Staff
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
            Object roleObj = session.get("role");
            UserRole currentRole = convertToUserRole(roleObj);
            if (currentRole == null || (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Only Admin or EVM_Staff can deactivate parts",
                                request.getRequestURI()));
            }
            boolean success = partCatalogService.deactivatePart(id);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(e.getMessage(), ""));
        }
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get Parts by Category", description = "List parts by category. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    public ResponseEntity<?> getPartsByCategory(@PathVariable("category") String category) {
        try {
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
            if (session == null || !(session.get("role") instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired session", request.getRequestURI()));
            }
            List<PartCatalogResponse> parts = partCatalogService.getPartsByCategory(category);
            return ResponseEntity.ok(parts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiErrorResponse.notFound(e.getMessage(), ""));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Part", description = "Delete a part from the catalog by its ID. Roles: EVM_Staff. This action cannot be undone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part deleted successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Part not found", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid part ID", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<?> deletePart(@PathVariable("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Invalid part ID", ""));
            }

            boolean deleted = partCatalogService.deletePart(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Part deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Part not found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to delete part: " + e.getMessage(), ""));
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update Part Status", description = "Update the active status of a part (activate/deactivate). Roles: Admin, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part status updated successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Part not found", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<?> updatePartStatus(@PathVariable("id") Long id, @RequestParam("active") boolean active) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiErrorResponse.badRequest("Invalid part ID", ""));
            }

            boolean updated = partCatalogService.updatePartStatus(id, active);
            if (updated) {
                String status = active ? "activated" : "deactivated";
                return ResponseEntity.ok(Map.of("success", true, "message", "Part " + status + " successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiErrorResponse.notFound("Part not found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to update part status: " + e.getMessage(), ""));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Parts", description = "Retrieve all parts in the catalog with pagination support. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parts retrieved successfully", content = @Content(schema = @Schema(implementation = PartCatalogResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<?> getAllParts(@RequestParam(value = "limit", defaultValue = "20") String limit,
            @RequestParam(value = "offset", defaultValue = "0") String offset) {
        try {
            List<PartCatalogResponse> parts = partCatalogService.getAllParts(Integer.parseInt(limit),
                    Integer.parseInt(offset));
            return ResponseEntity.ok(parts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiErrorResponse.internalServerError("Failed to retrieve parts: " + e.getMessage(), ""));
        }
    }
}
