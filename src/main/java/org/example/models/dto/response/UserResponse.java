package org.example.models.dto.response;

import java.time.OffsetDateTime;

import org.example.models.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User information response data")
public class UserResponse {
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Email address", example = "user@example.com")
    private String email;

    @Schema(description = "Full name", example = "John Doe")
    private String fullName;

    @Schema(description = "User role", example = "SC_Staff")
    private UserRole role;

    @Schema(description = "Account active status", example = "true")
    private Boolean isActive;

    @Schema(description = "Account creation timestamp")
    private OffsetDateTime createdAt;

    // Profile data
    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Address", example = "123 Main St, City, State")
    private String address;

    @Schema(description = "Multi-factor authentication enabled", example = "false")
    private Boolean mfaEnabled;

    // Auth info
    @Schema(description = "Last login timestamp")
    private OffsetDateTime lastLogin;

    // Service center info
    @Schema(description = "Service center ID", example = "1")
    private Long serviceCenterId;

    @Schema(description = "Service center name", example = "Downtown Service Center")
    private String serviceCenterName;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public OffsetDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(OffsetDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }
}
