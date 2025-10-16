package org.example.service.Serviceimpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.example.models.core.User;
import org.example.models.dto.request.LoginRequest;
import org.example.models.dto.request.PasswordResetRequest;
import org.example.models.dto.response.LoginResponse;
import org.example.models.enums.UserRole;
import org.example.repository.IRepository.IUserRepository;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IJwtService;
import org.example.ulti.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IJwtService jwtService;

    // In-memory session storage (in production, use Redis or database)
    private final Map<String, Map<String, Object>> sessions = new ConcurrentHashMap<>();

    // Token expiration time (24 hours)
    private static final long TOKEN_EXPIRATION_SECONDS = 24 * 60 * 60;

    @Override
    public LoginResponse login(LoginRequest request) {
        // Validate input
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();

        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is deactivated");
        }

        // Verify password
        String hashedPassword = SecurityUtil.sha256(request.getPassword());
        if (!hashedPassword.equals(user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getServiceCenterId());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

        // Update user's last login
        updateUserLastLogin(user);

        // Create response
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(86400L); // 24 hours in seconds

        // Set user info
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setFullName(user.getFullName());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());
        // Map stored string role to enum for permission derivation
        UserRole roleEnum;
        try {
            roleEnum = user.getRole() != null ? UserRole.valueOf(user.getRole()) : null;
        } catch (IllegalArgumentException ex) {
            roleEnum = null;
        }
        userInfo.setPermissions(getUserPermissions(roleEnum));
        userInfo.setServiceCenterName(getServiceCenterName(user.getServiceCenterId()));
        userInfo.setLastLogin(LocalDateTime.now());

        response.setUser(userInfo);

        return response;
    }

    @Override
    public LoginResponse getCurrentUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("No valid authentication found");
        }

        // Extract user email from authentication principal
        String email = authentication.getName();
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email not found in authentication");
        }

        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        // Create response without generating new tokens
        LoginResponse response = new LoginResponse();

        // Set user info
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setFullName(user.getFullName());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());

        // Map stored string role to enum for permission derivation
        UserRole roleEnum;
        try {
            roleEnum = user.getRole() != null ? UserRole.valueOf(user.getRole()) : null;
        } catch (IllegalArgumentException ex) {
            roleEnum = null;
        }
        userInfo.setPermissions(getUserPermissions(roleEnum));
        userInfo.setServiceCenterName(getServiceCenterName(user.getServiceCenterId()));
        userInfo.setLastLogin(LocalDateTime.now());

        response.setUser(userInfo);
        response.setAccessToken(null); // Don't expose token in /me endpoint
        response.setRefreshToken(null);
        response.setExpiresIn(0L);

        return response;
    }

    @Override
    public boolean logout(Long userId, String sessionId) {
        // For JWT, logout is handled on client side by removing the token
        // In a stateless JWT system, we can't invalidate tokens server-side
        // unless we maintain a blacklist, which defeats the purpose of statelessness
        // For now, just return true - actual logout happens when client discards the
        // token
        return true;
    }

    @Override
    public boolean requestPasswordReset(PasswordResetRequest request) {
        if (request.getEmail() == null && request.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Email or phone number is required");
        }

        Optional<User> userOpt = Optional.empty();

        if (request.getEmail() != null) {
            userOpt = userRepository.findByEmail(request.getEmail());
        } else if (request.getPhoneNumber() != null) {
            userOpt = userRepository.findByPhoneNumber(request.getPhoneNumber());
        }

        if (userOpt.isEmpty()) {
            // Don't reveal if user exists or not for security
            return true;
        }

        User user = userOpt.get();

        // Generate reset token
        String resetToken = SecurityUtil.randomToken(32);

        // Store reset token in user's auth info
        // TODO: Implement proper token storage with expiration

        // TODO: Send reset email/SMS
        // For now, just return true

        return true;
    }

    @Override
    public boolean resetPasswordWithToken(String token, String newPassword) {
        if (token == null || newPassword == null) {
            throw new IllegalArgumentException("Token and new password are required");
        }

        // Find user by reset token
        Optional<User> userOpt = userRepository.findByPasswordResetToken(token);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        User user = userOpt.get();

        // Hash new password
        String hashedPassword = SecurityUtil.sha256(newPassword);
        user.setPasswordHash(hashedPassword);

        // Clear reset token
        // TODO: Clear reset token from user's auth info

        // Save user
        userRepository.save(user);

        return true;
    }

    @Override
    public Map<String, Object> createSession(Long userId, String sessionToken, String ip) {
        String sessionId = SecurityUtil.randomToken(16);

        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("userId", userId);
        sessionData.put("sessionToken", sessionToken);
        sessionData.put("ip", ip);
        sessionData.put("createdAt", LocalDateTime.now());
        sessionData.put("expiresAt", LocalDateTime.now().plusSeconds(TOKEN_EXPIRATION_SECONDS));

        sessions.put(sessionId, sessionData);

        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", sessionId);
        result.put("expiresAt", sessionData.get("expiresAt"));

        return result;
    }

    @Override
    public Map<String, Object> getSessionByToken(String sessionToken) {
        // For JWT, validate the token directly
        return jwtService.validateToken(sessionToken);
    }

    @Override
    public boolean terminateSession(String sessionToken) {
        for (Map.Entry<String, Map<String, Object>> entry : sessions.entrySet()) {
            Map<String, Object> session = entry.getValue();
            if (sessionToken.equals(session.get("sessionToken"))) {
                sessions.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean terminateAllUserSessions(Long userId) {
        boolean terminated = false;
        sessions.entrySet().removeIf(entry -> {
            Map<String, Object> session = entry.getValue();
            return userId.equals(session.get("userId"));
        });
        return terminated;
    }

    @Override
    public boolean terminateExpiredSessions() {
        int initialSize = sessions.size();
        sessions.entrySet().removeIf(entry -> {
            Map<String, Object> session = entry.getValue();
            LocalDateTime expiresAt = (LocalDateTime) session.get("expiresAt");
            return expiresAt.isBefore(LocalDateTime.now());
        });
        return sessions.size() < initialSize;
    }

    @Override
    public List<Map<String, Object>> getSessionsByUser(Long userId) {
        return sessions.values().stream()
                .filter(session -> userId.equals(session.get("userId")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> getActiveSessions() {
        // Clean expired sessions first
        terminateExpiredSessions();
        return sessions.values().stream().toList();
    }

    // Helper methods
    private void updateUserLastLogin(User user) {
        // Update user's last login time
        // TODO: Implement proper last login tracking
    }

    private List<String> getUserPermissions(UserRole role) {
        // Return permissions based on role
        return switch (role) {
            case Admin -> List.of("read", "write", "delete", "admin");
            case EVM_Staff -> List.of("read", "write", "evm_operations");
            case SC_Staff -> List.of("read", "write", "sc_operations");
            case SC_Technician -> List.of("read", "sc_technician_operations");
        };
    }

    private String getServiceCenterName(Long serviceCenterId) {
        if (serviceCenterId == null) {
            return null;
        }
        // TODO: Implement service center name lookup
        return "Service Center " + serviceCenterId;
    }
}
