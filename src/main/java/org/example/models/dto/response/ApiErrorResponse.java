package org.example.models.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API error response with detailed error information")
public class ApiErrorResponse {
    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error type", example = "Bad Request")
    private String error;

    @Schema(description = "Error message", example = "Invalid request data")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/api/users")
    private String path;

    @Schema(description = "Error timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Additional error details")
    private Map<String, Object> details;

    @Schema(description = "Validation error messages")
    private List<String> validationErrors;

    public ApiErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    // Builder methods for common error scenarios
    public static ApiErrorResponse badRequest(String message, String path) {
        return new ApiErrorResponse(400, "Bad Request", message, path);
    }

    public static ApiErrorResponse unauthorized(String message, String path) {
        return new ApiErrorResponse(401, "Unauthorized", message, path);
    }

    public static ApiErrorResponse forbidden(String message, String path) {
        return new ApiErrorResponse(403, "Forbidden", message, path);
    }

    public static ApiErrorResponse notFound(String message, String path) {
        return new ApiErrorResponse(404, "Not Found", message, path);
    }

    public static ApiErrorResponse conflict(String message, String path) {
        return new ApiErrorResponse(409, "Conflict", message, path);
    }

    public static ApiErrorResponse internalServerError(String message, String path) {
        return new ApiErrorResponse(500, "Internal Server Error", message, path);
    }
}
