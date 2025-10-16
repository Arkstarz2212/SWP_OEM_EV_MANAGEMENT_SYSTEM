package org.example.service.Serviceimpl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.models.core.User;
import org.example.models.dto.request.CreateUserRequest;
import org.example.models.dto.request.PasswordChangeRequest;
import org.example.models.dto.request.PasswordResetRequest;
import org.example.models.dto.request.ProfileUpdateRequest;
import org.example.models.dto.response.UserResponse;
import org.example.models.enums.UserRole;
import org.example.models.json.AuthInfo;
import org.example.models.json.ProfileData;
import org.example.repository.IRepository.IUserRepository;
import org.example.service.IService.IRoleService;
import org.example.service.IService.IUserService;
import org.example.ulti.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleService roleService;

    @Override
    public UserResponse createScStaff(CreateUserRequest request, Long serviceCenterId) {
        return createUser(request, serviceCenterId, UserRole.SC_Staff);
    }

    @Override
    public UserResponse createScTechnician(CreateUserRequest request, Long serviceCenterId) {
        return createUser(request, serviceCenterId, UserRole.SC_Technician);
    }

    @Override
    public UserResponse createEvmStaff(CreateUserRequest request, Long oemId) {
        // EVM staff doesn't belong to a specific service center
        return createUser(request, null, UserRole.EVM_Staff);
    }

    @Override
    public UserResponse createAdminUser(CreateUserRequest request) {
        return createUser(request, null, UserRole.Admin);
    }

    private UserResponse createUser(CreateUserRequest request, Long serviceCenterId, UserRole role) {
        // Validate input
        if (request.getEmail() == null || request.getPassword() == null || request.getFullName() == null) {
            throw new IllegalArgumentException("Email, password, and full name are required");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        // Hash password
        String hashedPassword = SecurityUtil.sha256(request.getPassword());

        // Validate role exists in aoem.roles
        if (roleService.getByCode(role.name()) == null) {
            throw new IllegalArgumentException("Role not found: " + role.name());
        }

        // Create user entity
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(hashedPassword);
        user.setFullName(request.getFullName());
        user.setRole(role != null ? role.name() : null);
        user.setServiceCenterId(serviceCenterId);
        user.setIsActive(true);
        user.setCreatedAt(OffsetDateTime.now());

        // Set profile data
        ProfileData profileData = new ProfileData();
        profileData.setPhone(request.getPhone());
        profileData.setAddress(request.getAddress());
        profileData.setMfaEnabled(request.getMfaEnabled() != null ? request.getMfaEnabled() : false);
        user.setProfileData(profileData);

        // Set auth info
        AuthInfo authInfo = new AuthInfo();
        authInfo.setLastLogin(null);
        authInfo.setResetToken(null);
        authInfo.setExpires(null);
        user.setAuthInfo(authInfo);

        // Save user
        User savedUser = userRepository.save(user);

        // Convert to response
        return convertToUserResponse(savedUser);
    }

    @Override
    public boolean assignUserToServiceCenter(Long userId, Long serviceCenterId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setServiceCenterId(serviceCenterId);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean assignUserToOem(Long userId, Long oemId) {
        // For EVM staff, we might store OEM ID in profile data
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        if (user.getRole() != null && user.getRole().equals(UserRole.EVM_Staff.name())) {
            ProfileData profileData = user.getProfileData();
            if (profileData == null) {
                profileData = new ProfileData();
            }
            // Store OEM ID in profile data (assuming we have a field for it)
            // profileData.setOemId(oemId);
            user.setProfileData(profileData);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateUser(Long userId, String reason) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setIsActive(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean reactivateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setIsActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUserRole(Long userId, UserRole newRole) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        // Validate role exists in aoem.roles
        if (roleService.getByCode(newRole.name()) == null) {
            throw new IllegalArgumentException("Role not found: " + newRole.name());
        }

        User user = userOpt.get();
        user.setRole(newRole != null ? newRole.name() : null);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        UserRole roleEnum;
        try {
            roleEnum = user.getRole() != null ? UserRole.valueOf(user.getRole()) : null;
        } catch (IllegalArgumentException ex) {
            roleEnum = null;
        }
        List<String> permissions = getUserPermissions(roleEnum);
        return permissions.contains(permission);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        User user = userOpt.get();
        UserRole roleEnum2;
        try {
            roleEnum2 = user.getRole() != null ? UserRole.valueOf(user.getRole()) : null;
        } catch (IllegalArgumentException ex) {
            roleEnum2 = null;
        }
        return getUserPermissions(roleEnum2);
    }

    @Override
    public boolean assignPermission(Long userId, String permission) {
        // In this simple implementation, permissions are role-based
        // For more complex permission system, you'd need a separate permission table
        return true;
    }

    @Override
    public boolean revokePermission(Long userId, String permission) {
        // In this simple implementation, permissions are role-based
        return true;
    }

    @Override
    public UserResponse getProfile(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return convertToUserResponse(userOpt.get());
    }

    @Override
    public UserResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        // Update basic info
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        // Update profile data
        ProfileData profileData = user.getProfileData();
        if (profileData == null) {
            profileData = new ProfileData();
        }

        if (request.getPhoneNumber() != null) {
            profileData.setPhone(request.getPhoneNumber());
        }

        // Update other profile fields as needed
        user.setProfileData(profileData);

        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }

    @Override
    public boolean changePassword(Long userId, PasswordChangeRequest request) {
        if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
            throw new IllegalArgumentException("Current password and new password are required");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Verify current password
        String currentPasswordHash = SecurityUtil.sha256(request.getCurrentPassword());
        if (!currentPasswordHash.equals(user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Update password
        String newPasswordHash = SecurityUtil.sha256(request.getNewPassword());
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean resetPassword(PasswordResetRequest request) {
        if (request.getEmail() == null || request.getNewPassword() == null) {
            throw new IllegalArgumentException("Email and new password are required");
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        String newPasswordHash = SecurityUtil.sha256(request.getNewPassword());
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);

        return true;
    }

    @Override
    public List<UserResponse> getUsersByServiceCenter(Long serviceCenterId) {
        List<User> users = userRepository.findByServiceCenterId(serviceCenterId);
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getTechniciansByServiceCenter(Long serviceCenterId) {
        List<User> users = userRepository.findByRoleAndServiceCenterId(UserRole.SC_Technician, serviceCenterId);
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getStaffByServiceCenter(Long serviceCenterId) {
        List<User> users = userRepository.findByRoleAndServiceCenterId(UserRole.SC_Staff, serviceCenterId);
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getUsersByOem(Long oemId) {
        // For EVM staff, we'd need to filter by OEM ID stored in profile data
        List<User> users = userRepository.findByRole(UserRole.EVM_Staff);
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getUsersByRole(UserRole role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getActiveUsers(Long organizationId) {
        List<User> users = userRepository.findByIsActive(true);
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> searchUsers(String searchQuery, UserRole role, Long organizationId) {
        List<User> users;
        if (role != null) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findAll();
        }

        // Filter by search query
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getFullName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(searchQuery.toLowerCase()))
                    .toList();
        }

        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return convertToUserResponse(userOpt.get());
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return convertToUserResponse(userOpt.get());
    }

    @Override
    public boolean validateUserCredentials(String username, String password) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        String hashedPassword = SecurityUtil.sha256(password);
        return hashedPassword.equals(user.getPasswordHash());
    }

    @Override
    public boolean isUserActive(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.isPresent() && userOpt.get().getIsActive();
    }

    @Override
    public boolean recordUserLogin(Long userId, String ipAddress, String userAgent) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        AuthInfo authInfo = user.getAuthInfo();
        if (authInfo == null) {
            authInfo = new AuthInfo();
        }
        authInfo.setLastLogin(OffsetDateTime.now());
        user.setAuthInfo(authInfo);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean recordUserLogout(Long userId) {
        // In this simple implementation, we just return true
        // In a more complex system, you might track logout time
        return true;
    }

    @Override
    public List<String> getActiveUserSessions(Long userId) {
        // In this simple implementation, return empty list
        // In a more complex system, you'd query active sessions
        return new ArrayList<>();
    }

    @Override
    public List<UserResponse> getAvailableTechnicians(Long serviceCenterId) {
        List<User> users = userRepository.findByRoleAndServiceCenterId(UserRole.SC_Technician, serviceCenterId);
        return users.stream()
                .filter(User::getIsActive)
                .map(this::convertToUserResponse)
                .toList();
    }

    @Override
    public List<UserResponse> getEvmReviewers(Long oemId) {
        List<User> users = userRepository.findByRole(UserRole.EVM_Staff);
        return users.stream()
                .filter(User::getIsActive)
                .map(this::convertToUserResponse)
                .toList();
    }

    @Override
    public UserResponse assignTechnicianToWorkstation(Long technicianId, String workstationId) {
        Optional<User> userOpt = userRepository.findById(technicianId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Technician not found");
        }

        User user = userOpt.get();
        if (user.getRole() == null || !user.getRole().equals(UserRole.SC_Technician.name())) {
            throw new RuntimeException("User is not a technician");
        }

        // Store workstation assignment in profile data
        ProfileData profileData = user.getProfileData();
        if (profileData == null) {
            profileData = new ProfileData();
        }
        // profileData.setWorkstationId(workstationId);
        user.setProfileData(profileData);

        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }

    @Override
    public List<UserResponse> getUsersByManager(Long managerId) {
        // In this simple implementation, return empty list
        // In a more complex system, you'd have a manager hierarchy
        return new ArrayList<>();
    }

    @Override
    public UserResponse setUserManager(Long userId, Long managerId) {
        // In this simple implementation, just return the user
        // In a more complex system, you'd store manager relationship
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return convertToUserResponse(userOpt.get());
    }

    @Override
    public boolean updateNotificationPreferences(Long userId, List<String> channels) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        ProfileData profileData = user.getProfileData();
        if (profileData == null) {
            profileData = new ProfileData();
        }
        // Store notification preferences in profile data
        user.setProfileData(profileData);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<String> getNotificationChannels(Long userId) {
        // Return default notification channels
        return List.of("email", "sms");
    }

    @Override
    public boolean sendUserNotification(Long userId, String message, String channel) {
        // In this simple implementation, just return true
        // In a real system, you'd integrate with notification service
        return true;
    }

    @Override
    public List<UserResponse> getUsersWithExpiredPasswords() {
        // In this simple implementation, return empty list
        // In a more complex system, you'd check password age
        return new ArrayList<>();
    }

    @Override
    public List<UserResponse> getInactiveUsers(int daysSinceLastLogin) {
        // In this simple implementation, return empty list
        // In a more complex system, you'd check last login date
        return new ArrayList<>();
    }

    @Override
    public boolean forcePasswordReset(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        AuthInfo authInfo = user.getAuthInfo();
        if (authInfo == null) {
            authInfo = new AuthInfo();
        }
        // Set flag to force password reset
        user.setAuthInfo(authInfo);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean syncUserWithExternalSystem(Long userId, String externalSystemId) {
        // In this simple implementation, just return true
        return true;
    }

    @Override
    public UserResponse getUserByExternalId(String externalId) {
        // In this simple implementation, throw exception
        throw new RuntimeException("External ID lookup not implemented");
    }

    // Helper methods
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        try {
            response.setRole(UserRole.valueOf(user.getRole()));
        } catch (Exception e) {
            response.setRole(null);
        }
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setServiceCenterId(user.getServiceCenterId());

        // Set profile data
        ProfileData profileData = user.getProfileData();
        if (profileData != null) {
            response.setPhone(profileData.getPhone());
            response.setAddress(profileData.getAddress());
            response.setMfaEnabled(profileData.getMfaEnabled());
        }

        // Set auth info
        AuthInfo authInfo = user.getAuthInfo();
        if (authInfo != null) {
            response.setLastLogin(authInfo.getLastLogin());
        }

        // Set service center name
        if (user.getServiceCenterId() != null) {
            response.setServiceCenterName("Service Center " + user.getServiceCenterId());
        }

        return response;
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                userRepository.deleteById(userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateUserStatus(Long userId, boolean active) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setIsActive(active);
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<UserResponse> getAllUsers(int limit, int offset) {
        try {
            return userRepository.findAll().stream()
                    .skip(offset)
                    .limit(limit)
                    .map(this::convertToUserResponse)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    private List<String> getUserPermissions(UserRole role) {
        return switch (role) {
            case Admin -> List.of("read", "write", "delete", "admin");
            case EVM_Staff -> List.of("read", "write", "evm_operations");
            case SC_Staff -> List.of("read", "write", "sc_operations");
            case SC_Technician -> List.of("read", "sc_technician_operations");
        };
    }
}
