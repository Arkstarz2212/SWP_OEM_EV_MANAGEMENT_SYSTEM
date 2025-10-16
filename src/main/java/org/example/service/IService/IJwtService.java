package org.example.service.IService;

import java.util.Map;

/**
 * Interface for JWT token operations
 */
public interface IJwtService {
    
    /**
     * Generate JWT access token for user
     * @param userId User ID
     * @param email User email
     * @param role User role
     * @param serviceCenterId Service center ID (optional)
     * @return JWT access token
     */
    String generateAccessToken(Long userId, String email, String role, Long serviceCenterId);
    
    /**
     * Generate JWT refresh token for user
     * @param userId User ID
     * @param email User email
     * @return JWT refresh token
     */
    String generateRefreshToken(Long userId, String email);
    
    /**
     * Validate JWT token and extract claims
     * @param token JWT token
     * @return Claims from token, or null if invalid
     */
    Map<String, Object> validateToken(String token);
    
    /**
     * Extract user ID from token
     * @param token JWT token
     * @return User ID or null if invalid
     */
    Long getUserIdFromToken(String token);
    
    /**
     * Extract email from token
     * @param token JWT token
     * @return Email or null if invalid
     */
    String getEmailFromToken(String token);
    
    /**
     * Extract role from token
     * @param token JWT token
     * @return Role or null if invalid
     */
    String getRoleFromToken(String token);
    
    /**
     * Check if token is expired
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    boolean isTokenExpired(String token);
    
    /**
     * Extract token from Authorization header
     * @param authHeader Authorization header value
     * @return Token string or null if invalid format
     */
    String extractTokenFromHeader(String authHeader);
}
