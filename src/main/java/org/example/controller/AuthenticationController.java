package org.example.controller;

import java.util.Map;

import org.example.models.dto.request.LoginRequest;
import org.example.models.dto.request.PasswordResetRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.LoginResponse;
import org.example.service.IService.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and session management APIs")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user with email and password. Returns session token and user information on successful login.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login credentials", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class), examples = @ExampleObject(name = "Login Example", value = "{\"email\": \"admin@example.com\", \"password\": \"password123\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or account issues", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Validate required fields
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Email is required", getCurrentPath()));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Password is required", getCurrentPath()));
            }

            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                    e.getMessage(), getCurrentPath()));
        } catch (Exception e) {
            // Check if it's an authentication-related error
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("invalid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiErrorResponse.unauthorized("Invalid email or password", getCurrentPath()));
            } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("disabled")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiErrorResponse.unauthorized("Account is disabled", getCurrentPath()));
            } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("locked")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiErrorResponse.unauthorized("Account is locked", getCurrentPath()));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        ApiErrorResponse.internalServerError("Login failed: " + e.getMessage(), getCurrentPath()));
            }
        }
    }

    @PostMapping("/password/reset/request")
    @Operation(summary = "Request Password Reset", description = "Request a password reset link to be sent to user's email or phone. User can provide either username, email, or phone number.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User identification for password reset", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Password Reset Request Example", value = "{\"usernameOrEmail\": \"user@example.com\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset link sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, Object> body) {
        try {
            if (body == null || body.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Request body is required", getCurrentPath()));
            }

            String usernameOrEmail = body.get("usernameOrEmail") != null
                    ? String.valueOf(body.get("usernameOrEmail")).trim()
                    : "";

            if (usernameOrEmail.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "usernameOrEmail is required", getCurrentPath()));
            }

            PasswordResetRequest req = new PasswordResetRequest();
            // Assume it's email if contains '@'
            if (usernameOrEmail.contains("@")) {
                req.setEmail(usernameOrEmail);
            } else {
                req.setPhoneNumber(usernameOrEmail);
            }

            boolean ok = authenticationService.requestPasswordReset(req);
            if (ok) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Password reset link sent"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiErrorResponse.notFound("User not found with provided credentials", getCurrentPath()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                    e.getMessage(), getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to send password reset: " + e.getMessage(),
                            getCurrentPath()));
        }
    }

    @PostMapping("/password/reset/confirm")
    @Operation(summary = "Confirm Password Reset", description = "Confirm password reset using the token received via email/SMS. The new password must be at least 6 characters long.", parameters = {
            @Parameter(name = "token", description = "Password reset token received via email/SMS", required = true, example = "abc123def456ghi789"),
            @Parameter(name = "newPassword", description = "New password (minimum 6 characters)", required = true, example = "newpassword123")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password requirements not met", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> confirmPasswordReset(@RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {
        try {
            // Validate required parameters
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Token parameter is required", getCurrentPath()));
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "newPassword parameter is required", getCurrentPath()));
            }
            if (newPassword.length() < 6) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "Password must be at least 6 characters long", getCurrentPath()));
            }

            boolean ok = authenticationService.resetPasswordWithToken(token, newPassword);
            if (ok) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiErrorResponse.badRequest("Invalid or expired token", getCurrentPath()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                    e.getMessage(), getCurrentPath()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiErrorResponse.badRequest(e.getMessage(), getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Password reset failed: " + e.getMessage(), getCurrentPath()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Logout user by invalidating their session. Both userId and sessionId are required to ensure secure logout.", parameters = {
            @Parameter(name = "userId", description = "User ID to logout", required = true, example = "1"),
            @Parameter(name = "sessionId", description = "Session ID to invalidate", required = true, example = "sess_abc123def456")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Missing required parameters", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Session not found or already expired", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> logout(@RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "sessionId", required = false) String sessionId) {
        try {
            if (userId == null || sessionId == null) {
                return ResponseEntity.badRequest().body(ApiErrorResponse.badRequest(
                        "userId and sessionId parameters are required", getCurrentPath()));
            }

            boolean ok = authenticationService.logout(userId, sessionId);
            if (ok) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiErrorResponse.notFound("Session not found or already expired", getCurrentPath()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Logout failed: " + e.getMessage(), getCurrentPath()));
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get Current User Info", description = "Get information about the currently authenticated user. Requires valid JWT token in Authorization header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getCurrentUser() {
        try {
            // Get current authentication from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiErrorResponse.unauthorized("No valid authentication found", getCurrentPath()));
            }

            // Get user info from authentication service
            LoginResponse userInfo = authenticationService.getCurrentUserInfo(authentication);
            return ResponseEntity.ok(userInfo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiErrorResponse.unauthorized(e.getMessage(), getCurrentPath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.internalServerError("Failed to get user info: " + e.getMessage(),
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
            return "/api/auth";
        }
    }
}
