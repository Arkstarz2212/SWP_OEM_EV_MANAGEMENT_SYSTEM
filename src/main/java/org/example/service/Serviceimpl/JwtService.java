package org.example.service.Serviceimpl;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.example.service.IService.IJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * JWT Service implementation for token generation and validation
 */
@Service
public class JwtService implements IJwtService {

    @Value("${jwt.secret:AOEM_JWT_SECRET_KEY_FOR_EV_WARRANTY_MANAGEMENT_SYSTEM_2024}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration:86400000}") // 24 hours in milliseconds
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}") // 7 days in milliseconds
    private long refreshTokenExpiration;

    /**
     * Generate JWT access token
     */
    @Override
    public String generateAccessToken(Long userId, String email, String role, Long serviceCenterId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);
        claims.put("serviceCenterId", serviceCenterId);
        claims.put("type", "access");

        return createToken(claims, email, accessTokenExpiration);
    }

    /**
     * Generate JWT refresh token
     */
    @Override
    public String generateRefreshToken(Long userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("type", "refresh");

        return createToken(claims, email, refreshTokenExpiration);
    }

    /**
     * Create JWT token with claims
     */
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Validate JWT token and extract claims
     */
    @Override
    public Map<String, Object> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Map<String, Object> result = new HashMap<>();
            Object userIdObj = claims.get("userId");
            Object serviceCenterIdObj = claims.get("serviceCenterId");

            result.put("userId", userIdObj instanceof Number ? ((Number) userIdObj).longValue() : userIdObj);
            result.put("email", claims.get("email"));
            result.put("role", claims.get("role"));
            result.put("serviceCenterId",
                    serviceCenterIdObj instanceof Number ? ((Number) serviceCenterIdObj).longValue()
                            : serviceCenterIdObj);
            result.put("type", claims.get("type"));
            result.put("expiration", claims.getExpiration());

            return result;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Extract user ID from token
     */
    @Override
    public Long getUserIdFromToken(String token) {
        Map<String, Object> claims = validateToken(token);
        return claims != null ? (Long) claims.get("userId") : null;
    }

    /**
     * Extract email from token
     */
    @Override
    public String getEmailFromToken(String token) {
        Map<String, Object> claims = validateToken(token);
        return claims != null ? (String) claims.get("email") : null;
    }

    /**
     * Extract role from token
     */
    @Override
    public String getRoleFromToken(String token) {
        Map<String, Object> claims = validateToken(token);
        return claims != null ? (String) claims.get("role") : null;
    }

    /**
     * Check if token is expired
     */
    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true; // Consider invalid tokens as expired
        }
    }

    /**
     * Extract token from Authorization header
     */
    @Override
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Get signing key from secret
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
