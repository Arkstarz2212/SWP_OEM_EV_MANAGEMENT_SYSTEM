package org.example.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.example.service.IService.IJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT Authentication Filter to handle Authorization headers
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private IJwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            logger.info("Authorization header: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = jwtService.extractTokenFromHeader(authHeader);

                if (token != null && !jwtService.isTokenExpired(token)) {
                    Map<String, Object> claims = jwtService.validateToken(token);

                    logger.info("Claims: " + claims);
                    if (claims != null && "access".equals(claims.get("type"))) {
                        String email = (String) claims.get("email");
                        String role = (String) claims.get("role");
                        logger.info("Email: " + email + ", Role: " + role);
                        Long userId = claims.get("userId") != null ? ((Number) claims.get("userId")).longValue() : null;
                        Long serviceCenterId = claims.get("serviceCenterId") != null
                                ? ((Number) claims.get("serviceCenterId")).longValue()
                                : null;

                        if (email != null && role != null) {
                            // Create authentication object
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                            // Set additional details
                            Map<String, Object> details = new HashMap<>();
                            details.put("userId", userId);
                            details.put("email", email);
                            details.put("role", role);
                            if (serviceCenterId != null) {
                                details.put("serviceCenterId", serviceCenterId);
                            }
                            authentication.setDetails(details);

                            // Set authentication in security context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            logger.info("Authentication set successfully for user: " + email);
                        } else {
                            logger.warn("Invalid claims or token type");
                        }
                    } else {
                        logger.warn("Token is expired or invalid");
                    }
                } else {
                    logger.warn("No token found in Authorization header");
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("JWT Authentication failed", e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }
}
