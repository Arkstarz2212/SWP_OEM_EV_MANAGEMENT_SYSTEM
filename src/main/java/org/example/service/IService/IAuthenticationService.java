package org.example.service.IService;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.LoginRequest;
import org.example.models.dto.request.PasswordResetRequest;
import org.example.models.dto.response.LoginResponse;
import org.springframework.security.core.Authentication;

public interface IAuthenticationService {
    // Authentication Operations
    LoginResponse login(LoginRequest request);

    LoginResponse getCurrentUserInfo(Authentication authentication);

    boolean logout(Long userId, String sessionId);

    boolean requestPasswordReset(PasswordResetRequest request);

    boolean resetPasswordWithToken(String token, String newPassword);

    // Session Management (merged from ISessionManagementService)
    Map<String, Object> createSession(Long userId, String sessionToken, String ip);

    Map<String, Object> getSessionByToken(String sessionToken);

    boolean terminateSession(String sessionToken);

    boolean terminateAllUserSessions(Long userId);

    boolean terminateExpiredSessions();

    List<Map<String, Object>> getSessionsByUser(Long userId);

    List<Map<String, Object>> getActiveSessions();
}
