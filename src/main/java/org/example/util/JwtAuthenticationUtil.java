package org.example.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * Utility class for JWT authentication operations
 */
public class JwtAuthenticationUtil {

    /**
     * Get current authenticated user ID
     * @return User ID or null if not authenticated
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (Long) details.get("userId");
        }
        return null;
    }

    /**
     * Get current authenticated user email
     * @return Email or null if not authenticated
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Get current authenticated user role
     * @return Role or null if not authenticated
     */
    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("role");
        }
        return null;
    }

    /**
     * Get current authenticated user service center ID
     * @return Service center ID or null if not authenticated
     */
    public static Long getCurrentUserServiceCenterId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (Long) details.get("serviceCenterId");
        }
        return null;
    }

    /**
     * Check if current user has specific role
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public static boolean hasRole(String role) {
        String currentRole = getCurrentUserRole();
        return role != null && role.equals(currentRole);
    }

    /**
     * Check if current user is admin
     * @return true if user is admin, false otherwise
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Check if current user is manager
     * @return true if user is manager, false otherwise
     */
    public static boolean isManager() {
        return hasRole("MANAGER");
    }

    /**
     * Check if current user is technician
     * @return true if user is technician, false otherwise
     */
    public static boolean isTechnician() {
        return hasRole("TECHNICIAN");
    }

    /**
     * Check if current user is authenticated
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
               && !"anonymousUser".equals(authentication.getName());
    }
}
