package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.CreateUserRequest;
import org.example.models.dto.request.ProfileUpdateRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.UserResponse;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IJwtService;
import org.example.service.IService.IUserService;
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
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management operations including creation, profile updates, and role management")
public class UsersController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private IJwtService jwtService;

    @PostMapping
    @Operation(summary = "Create New User", description = "Create a new user account with specified role and profile information. Roles: Admin only. Required fields: email, password, fullName, role. Optional: serviceCenterId, phone, address, mfaEnabled.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create User Example", value = "{\"email\": \"user@example.com\", \"password\": \"password123\", \"fullName\": \"John Doe\", \"role\": \"SC_Staff\", \"serviceCenterId\": 1, \"phone\": \"+1234567890\", \"address\": \"123 Main St\", \"mfaEnabled\": false}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data or validation errors", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> body) {
        try {
            // Authorization: Only Admin, EVM_Staff, SC_Staff can create users
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiErrorResponse.unauthorized("Missing or invalid Authorization header", getCurrentPath()));
            }
            String token = authHeader.substring("Bearer ".length()).trim();

            // Use JWT service to validate token instead of session-based auth
            Map<String, Object> tokenClaims = jwtService.validateToken(token);
            if (tokenClaims == null || !"access".equals(tokenClaims.get("type"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.unauthorized("Invalid or expired token", getCurrentPath()));
            }

            String roleStr = (String) tokenClaims.get("role");
            if (roleStr == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Insufficient permissions", getCurrentPath()));
            }

            UserRole currentRole = parseUserRole(roleStr);
            if (currentRole == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiErrorResponse.forbidden("Invalid user role", getCurrentPath()));
            }

            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiErrorResponse
                        .forbidden("Only Admin, EVM_Staff or SC_Staff can create users", getCurrentPath()));
            }

            if (body == null || body.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Request body is required", getCurrentPath()));
            }

            // Validate required fields
            String email = body.get("email") != null ? String.valueOf(body.get("email")).trim() : null;
            String password = body.get("password") != null ? String.valueOf(body.get("password")) : null;
            String fullName = body.get("fullName") != null ? String.valueOf(body.get("fullName")).trim()
                    : body.get("username") != null ? String.valueOf(body.get("username")).trim() : null;
            String requestRoleStr = body.get("role") != null ? String.valueOf(body.get("role")) : null;

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Email is required", getCurrentPath()));
            }
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Password is required", getCurrentPath()));
            }
            if (password.length() < 6) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Password must be at least 6 characters long", getCurrentPath()));
            }
            if (fullName == null || fullName.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Full name is required", getCurrentPath()));
            }
            if (requestRoleStr == null || requestRoleStr.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Role is required", getCurrentPath()));
            }

            CreateUserRequest req = new CreateUserRequest();
            req.setEmail(email);
            req.setPassword(password);
            req.setFullName(fullName);

            UserRole parsedRole = parseUserRole(requestRoleStr);
            if (parsedRole == null) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid role. Valid roles are: Admin, EVM_Staff, SC_Staff, SC_Technician", getCurrentPath()));
            }
            req.setRole(parsedRole);

            // Set optional fields
            if (body.get("serviceCenterId") != null) {
                try {
                    req.setServiceCenterId(Long.valueOf(body.get("serviceCenterId").toString()));
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                            "serviceCenterId must be a valid number", getCurrentPath()));
                }
            }
            if (body.get("phone") != null) {
                req.setPhone(String.valueOf(body.get("phone")).trim());
            }
            if (body.get("address") != null) {
                req.setAddress(String.valueOf(body.get("address")).trim());
            }
            if (body.get("mfaEnabled") != null) {
                req.setMfaEnabled(Boolean.valueOf(body.get("mfaEnabled").toString()));
            }

            // Validate serviceCenterId for SC roles
            if ((parsedRole == UserRole.SC_Staff || parsedRole == UserRole.SC_Technician)
                    && req.getServiceCenterId() == null) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "serviceCenterId is required for " + parsedRole.name(), getCurrentPath()));
            }

            UserResponse res = switch (parsedRole) {
                case Admin -> userService.createAdminUser(req);
                case EVM_Staff -> userService.createEvmStaff(req, null);
                case SC_Staff -> userService.createScStaff(req, req.getServiceCenterId());
                case SC_Technician -> userService.createScTechnician(req, req.getServiceCenterId());
            };
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                    e.getMessage(), getCurrentPath()));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiErrorResponse.conflict("Email already exists", getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to create user: " + e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User Profile", description = "Retrieve detailed profile information for a specific user by their ID.", parameters = {
            @Parameter(name = "id", description = "User ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getProfile(@PathVariable("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid user ID", getCurrentPath()));
            }

            UserResponse res = userService.getProfile(id);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiErrorResponse.notFound("User not found", getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to retrieve user profile: " + e.getMessage(),
                            getCurrentPath()));
        }
    }

    @GetMapping
    @Operation(summary = "List Users", description = "Retrieve a paginated list of users with optional filtering by role or service center. Roles: Admin only. Supports pagination with limit and offset parameters.", parameters = {
            @Parameter(name = "role", description = "Filter by user role", required = false, example = "SC_Staff", schema = @Schema(allowableValues = {
                    "Admin", "EVM_Staff", "SC_Staff", "SC_Technician" })),
            @Parameter(name = "serviceCenterId", description = "Filter by service center ID", required = false, example = "1"),
            @Parameter(name = "limit", description = "Maximum number of users to return (1-100)", required = false, example = "20", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "100")),
            @Parameter(name = "offset", description = "Number of users to skip for pagination", required = false, example = "0", schema = @Schema(defaultValue = "0", minimum = "0"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> listUsers(@RequestParam(value = "role", required = false) UserRole role,
            @RequestParam(value = "serviceCenterId", required = false) Long serviceCenterId,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        try {
            // Validate parameters
            if (limit != null && (limit <= 0 || limit > 100)) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Limit must be between 1 and 100", getCurrentPath()));
            }
            if (offset != null && offset < 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Offset must be 0 or greater", getCurrentPath()));
            }
            if (serviceCenterId != null && serviceCenterId <= 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid service center ID", getCurrentPath()));
            }

            List<UserResponse> users;
            if (serviceCenterId != null) {
                users = userService.getUsersByServiceCenter(serviceCenterId);
            } else if (role != null) {
                users = userService.getUsersByRole(role);
            } else {
                users = userService.searchUsers(null, null, null);
            }

            // Apply pagination
            int from = Math.min(offset != null ? offset : 0, users.size());
            int to = Math.min(from + (limit != null ? limit : 20), users.size());

            return ResponseEntity.ok(Map.of(
                    "users", users.subList(from, to),
                    "total", users.size(),
                    "offset", offset,
                    "limit", limit));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                    e.getMessage(), getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to retrieve users: " + e.getMessage(),
                            getCurrentPath()));
        }
    }

    @PutMapping("/{id}/profile")
    @Operation(summary = "Update User Profile", description = "Update user profile information including personal details, contact information, and preferences. Roles: Admin only.", parameters = {
            @Parameter(name = "id", description = "User ID to update", required = true, example = "1")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated profile information", required = true, content = @Content(schema = @Schema(implementation = ProfileUpdateRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updateProfile(@PathVariable("id") Long id, @Valid @RequestBody ProfileUpdateRequest req) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid user ID", getCurrentPath()));
            }
            if (req == null) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Request body is required", getCurrentPath()));
            }

            UserResponse res = userService.updateProfile(id, req);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiErrorResponse.notFound("User not found", getCurrentPath()));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiErrorResponse.conflict("Email already exists", getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to update profile: " + e.getMessage(),
                            getCurrentPath()));
        }
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update User Role", description = "Update the role of a specific user. Roles: Admin only. Valid roles: Admin, EVM_Staff, SC_Staff, SC_Technician.", parameters = {
            @Parameter(name = "id", description = "User ID to update", required = true, example = "1"),
            @Parameter(name = "role", description = "New role for the user", required = true, example = "SC_Staff", schema = @Schema(allowableValues = {
                    "Admin", "EVM_Staff", "SC_Staff", "SC_Technician" }))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role or user ID", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updateRole(@PathVariable("id") Long id, @RequestParam("role") String role) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid user ID", getCurrentPath()));
            }
            if (role == null || role.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Role parameter is required", getCurrentPath()));
            }

            UserRole newRole = parseUserRole(role);
            if (newRole == null) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid role. Valid roles are: Admin, EVM_Staff, SC_Staff, SC_Technician", getCurrentPath()));
            }

            boolean ok = userService.updateUserRole(id, newRole);
            if (ok) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Role updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiErrorResponse.notFound("User not found", getCurrentPath()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiErrorResponse.notFound("User not found", getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to update role: " + e.getMessage(), getCurrentPath()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User", description = "Delete a user by their ID. Roles: Admin only. This action cannot be undone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid user ID", getCurrentPath()));
            }

            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiErrorResponse.notFound("User not found", getCurrentPath()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to delete user: " + e.getMessage(), getCurrentPath()));
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update User Status", description = "Update the active status of a user (activate/deactivate). Roles: Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status updated successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") Long id, @RequestParam("active") boolean active) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Invalid user ID", getCurrentPath()));
            }

            boolean updated = userService.updateUserStatus(id, active);
            if (updated) {
                String status = active ? "activated" : "deactivated";
                return ResponseEntity.ok(Map.of("success", true, "message", "User " + status + " successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiErrorResponse.notFound("User not found", getCurrentPath()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to update user status: " + e.getMessage(),
                            getCurrentPath()));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Users", description = "Retrieve all users with pagination support. Roles: Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "limit", defaultValue = "20") String limit,
            @RequestParam(value = "offset", defaultValue = "0") String offset) {
        try {
            List<UserResponse> users = userService.getAllUsers(Integer.parseInt(limit), Integer.parseInt(offset));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to retrieve users: " + e.getMessage(),
                            getCurrentPath()));
        }
    }

    // Helper method to get current request path
    private String getCurrentPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return "/api/users";
        }
    }

    private UserRole parseUserRole(String input) {
        if (input == null)
            return null;
        for (UserRole r : UserRole.values()) {
            if (r.name().equalsIgnoreCase(input))
                return r;
        }
        return null;
    }
}
