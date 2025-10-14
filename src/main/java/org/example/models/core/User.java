package org.example.models.core;

import java.time.OffsetDateTime;

import org.example.models.enums.UserRole;
import org.example.models.json.AuthInfo;
import org.example.models.json.ProfileData;

import com.fasterxml.jackson.databind.ObjectMapper;

public class User {
    private Long id; // aoem.users.id
    private String email; // aoem.users.email
    private String password_hash; // aoem.users.password_hash
    private String full_name; // aoem.users.full_name
    private String role; // aoem.users.role (VARCHAR references roles.code)
    private Long service_center_id; // aoem.users.service_center_id
    private String profile_data; // aoem.users.profile_data (JSON text)
    private String auth_info; // aoem.users.auth_info (JSON text)

    // Backward-compatible/transient helpers
    private transient Boolean isActive;
    private transient OffsetDateTime createdAt;
    private transient ProfileData profileDataCache;
    private transient AuthInfo authInfoCache;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getService_center_id() {
        return service_center_id;
    }

    public void setService_center_id(Long service_center_id) {
        this.service_center_id = service_center_id;
    }

    public String getProfile_data() {
        return profile_data;
    }

    public void setProfile_data(String profile_data) {
        this.profile_data = profile_data;
    }

    public String getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(String auth_info) {
        this.auth_info = auth_info;
        this.authInfoCache = null;
    }

    // Backward-compatible camelCase methods used by services
    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String v) {
        this.password_hash = v;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String v) {
        this.full_name = v;
    }

    public UserRole getRoleEnum() {
        return role != null ? UserRole.valueOf(role) : null;
    }

    public void setRoleEnum(UserRole r) {
        this.role = r != null ? r.name() : null;
    }

    public Long getServiceCenterId() {
        return service_center_id;
    }

    public void setServiceCenterId(Long v) {
        this.service_center_id = v;
    }

    public Boolean getIsActive() {
        return isActive == null ? Boolean.TRUE : isActive;
    }

    public void setIsActive(Boolean v) {
        this.isActive = v;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime v) {
        this.createdAt = v;
    }

    public ProfileData getProfileData() {
        if (profileDataCache == null && profile_data != null) {
            try {
                profileDataCache = objectMapper.readValue(profile_data, ProfileData.class);
            } catch (Exception ignored) {
            }
        }
        return profileDataCache;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileDataCache = profileData;
        if (profileData != null) {
            try {
                this.profile_data = objectMapper.writeValueAsString(profileData);
            } catch (Exception ignored) {
                this.profile_data = "{}";
            }
        } else {
            this.profile_data = null;
        }
    }

    public AuthInfo getAuthInfo() {
        if (authInfoCache == null && auth_info != null) {
            try {
                authInfoCache = objectMapper.readValue(auth_info, AuthInfo.class);
            } catch (Exception ignored) {
            }
        }
        return authInfoCache;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        this.authInfoCache = authInfo;
        if (authInfo != null) {
            try {
                this.auth_info = objectMapper.writeValueAsString(authInfo);
            } catch (Exception ignored) {
                this.auth_info = "{}";
            }
        } else {
            this.auth_info = null;
        }
    }
}
