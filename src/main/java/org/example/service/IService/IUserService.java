package org.example.service.IService;

import java.util.List;

import org.example.models.dto.request.CreateUserRequest;
import org.example.models.dto.request.PasswordChangeRequest;
import org.example.models.dto.request.PasswordResetRequest;
import org.example.models.dto.request.ProfileUpdateRequest;
import org.example.models.dto.response.UserResponse;
import org.example.models.enums.UserRole;

public interface IUserService {
    // Admin Operations - User Management
    UserResponse createScStaff(CreateUserRequest request, Long serviceCenterId);

    UserResponse createScTechnician(CreateUserRequest request, Long serviceCenterId);

    UserResponse createEvmStaff(CreateUserRequest request, Long oemId);

    UserResponse createAdminUser(CreateUserRequest request);

    boolean assignUserToServiceCenter(Long userId, Long serviceCenterId);

    boolean assignUserToOem(Long userId, Long oemId);

    boolean deactivateUser(Long userId, String reason);

    boolean reactivateUser(Long userId);

    boolean deleteUser(Long userId);

    boolean updateUserStatus(Long userId, boolean active);

    List<UserResponse> getAllUsers(int limit, int offset);

    // Role-based Access Control
    boolean updateUserRole(Long userId, UserRole newRole);

    boolean hasPermission(Long userId, String permission);

    List<String> getUserPermissions(Long userId);

    boolean assignPermission(Long userId, String permission);

    boolean revokePermission(Long userId, String permission);

    // User Profile Management
    UserResponse getProfile(Long userId);

    UserResponse updateProfile(Long userId, ProfileUpdateRequest request);

    boolean changePassword(Long userId, PasswordChangeRequest request);

    boolean resetPassword(PasswordResetRequest request);

    // User Queries and Search
    List<UserResponse> getUsersByServiceCenter(Long serviceCenterId);

    List<UserResponse> getTechniciansByServiceCenter(Long serviceCenterId);

    List<UserResponse> getStaffByServiceCenter(Long serviceCenterId);

    List<UserResponse> getUsersByOem(Long oemId);

    List<UserResponse> getUsersByRole(UserRole role);

    List<UserResponse> getActiveUsers(Long organizationId);

    List<UserResponse> searchUsers(String searchQuery, UserRole role, Long organizationId);

    // Authentication Support
    UserResponse getUserByUsername(String username);

    UserResponse getUserByEmail(String email);

    boolean validateUserCredentials(String username, String password);

    boolean isUserActive(Long userId);

    // Session Management Support
    boolean recordUserLogin(Long userId, String ipAddress, String userAgent);

    boolean recordUserLogout(Long userId);

    List<String> getActiveUserSessions(Long userId);

    // Role-specific Operations
    List<UserResponse> getAvailableTechnicians(Long serviceCenterId);

    List<UserResponse> getEvmReviewers(Long oemId);

    UserResponse assignTechnicianToWorkstation(Long technicianId, String workstationId);

    // Organizational Hierarchy
    List<UserResponse> getUsersByManager(Long managerId);

    UserResponse setUserManager(Long userId, Long managerId);

    // Notification and Communication
    boolean updateNotificationPreferences(Long userId, List<String> channels);

    List<String> getNotificationChannels(Long userId);

    boolean sendUserNotification(Long userId, String message, String channel);

    // Audit and Compliance
    List<UserResponse> getUsersWithExpiredPasswords();

    List<UserResponse> getInactiveUsers(int daysSinceLastLogin);

    boolean forcePasswordReset(Long userId);

    // Integration Support
    boolean syncUserWithExternalSystem(Long userId, String externalSystemId);

    UserResponse getUserByExternalId(String externalId);
}
