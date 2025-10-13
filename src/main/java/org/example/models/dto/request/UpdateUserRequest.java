package org.example.models.dto.request;

import org.example.models.enums.UserRole;

import jakarta.validation.constraints.Email;

public class UpdateUserRequest {
    @Email(message = "Email không hợp lệ")
    private String email;
    private String fullName; // core: User.full_name
    private String phone; // mapped to core: User.profile_data.phone
    private String address; // mapped to core: User.profile_data.address
    private Boolean mfaEnabled; // mapped to core: User.profile_data.mfa_enabled
    private UserRole role;
    private Long serviceCenterId; // core: User.service_center_id

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
}
