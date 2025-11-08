package org.example.config;

import java.util.ArrayList;
import java.util.List;

import org.example.models.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.badRequest(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });

        ApiErrorResponse apiError = ApiErrorResponse.badRequest("Validation failed", request.getRequestURI());
        apiError.setValidationErrors(errors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String parameterName = ex.getName() != null ? ex.getName() : "unknown";
        String typeName = "unknown";
        try {
            Class<?> requiredType = ex.getRequiredType();
            if (requiredType != null) {
                typeName = requiredType.getSimpleName();
            }
        } catch (Exception ignored) {
            // Keep default "unknown" value
        }
        String message = String.format("Parameter '%s' should be of type %s",
                parameterName, typeName);
        ApiErrorResponse error = ApiErrorResponse.badRequest(message, request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Data integrity violation: " + extractConstraintMessage(ex.getMessage());
        ApiErrorResponse error = ApiErrorResponse.conflict(message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleEmptyResult(
            org.springframework.dao.EmptyResultDataAccessException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorResponse.notFound("Resource not found", request.getRequestURI()));
    }

    // Spring Security exception handlers (commented out as Spring Security is not
    // configured)
    // Uncomment these when Spring Security is properly configured

    // @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    // public ResponseEntity<ApiErrorResponse> handleAccessDenied(
    // org.springframework.security.access.AccessDeniedException ex,
    // HttpServletRequest request) {
    // ApiErrorResponse error = ApiErrorResponse.forbidden("Access denied",
    // request.getRequestURI());
    // return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    // }

    // @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    // public ResponseEntity<ApiErrorResponse> handleBadCredentials(
    // org.springframework.security.authentication.BadCredentialsException ex,
    // HttpServletRequest request) {
    // ApiErrorResponse error = ApiErrorResponse.unauthorized("Invalid credentials",
    // request.getRequestURI());
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    // }

    // @ExceptionHandler(org.springframework.security.authentication.AuthenticationException.class)
    // public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
    // org.springframework.security.authentication.AuthenticationException ex,
    // HttpServletRequest request) {
    // ApiErrorResponse error = ApiErrorResponse.unauthorized("Authentication
    // failed", request.getRequestURI());
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    // }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        StringBuilder msg = new StringBuilder("An unexpected error occurred: ").append(ex.getMessage());
        Throwable cause = ex.getCause();
        if (cause != null && cause.getMessage() != null) {
            msg.append(" | cause: ").append(cause.getMessage());
        }
        ApiErrorResponse error = ApiErrorResponse.internalServerError(msg.toString(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(org.springframework.jdbc.BadSqlGrammarException.class)
    public ResponseEntity<ApiErrorResponse> handleBadSql(
            org.springframework.jdbc.BadSqlGrammarException ex, HttpServletRequest request) {
        StringBuilder msg = new StringBuilder("Bad SQL grammar: ").append(ex.getMessage());
        Throwable cause = ex.getCause();
        if (cause != null && cause.getMessage() != null) {
            msg.append(" | cause: ").append(cause.getMessage());
        }
        ApiErrorResponse error = ApiErrorResponse.internalServerError(msg.toString(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String extractConstraintMessage(String message) {
        if (message != null) {
            String lowerMessage = message.toLowerCase();
            if (lowerMessage.contains("duplicate key")) {
                return "Record already exists with this information";
            } else if (lowerMessage.contains("foreign key")) {
                return "Referenced record does not exist";
            } else if (lowerMessage.contains("not null")) {
                return "Required field is missing";
            } else if (lowerMessage.contains("unique")) {
                return "Value must be unique";
            }
        }
        return "Data constraint violation";
    }
}
