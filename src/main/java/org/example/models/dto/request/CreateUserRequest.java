package org.example.models.dto.request;

import org.example.models.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request data for creating a new user account")
public class CreateUserRequest {
    /**
     * User email address
     * Example: "user@example.com"
     */
    @Schema(description = "User email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    /**
     * User password (minimum 6 characters)
     * Example: "password123"
     */
    @Schema(description = "User password (minimum 6 characters)", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6)
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    /**
     * Full name of the user
     * Example: "Nguyễn Văn A"
     */
    @Schema(description = "Full name of the user", example = "Nguyễn Văn A", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    /**
     * User role in the system
     * Valid values: ADMIN, TECHNICIAN, MANAGER, CUSTOMER
     * Example: "ADMIN"
     */
    @Schema(description = "User role in the system", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {
            "Admin",
            "EVM_Staff", "SC_Staff", "SC_Technician" })
    @NotNull(message = "Role không được để trống")
    private UserRole role;

    /**
     * Service center ID (optional)
     * Example: 1
     */
    @Schema(description = "Service center ID (optional)", example = "1")
    private Long serviceCenterId;

    /**
     * Phone number (optional)
     * Example: "+84123456789"
     */
    @Schema(description = "Phone number (optional)", example = "+84123456789")
    private String phone;

    /**
     * Address (optional)
     * Example: "123 Đường ABC, Quận 1, TP.HCM"
     */
    @Schema(description = "Address (optional)", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    /**
     * Multi-factor authentication enabled (optional)
     * Default: false
     * Example: true
     */
    @Schema(description = "Multi-factor authentication enabled (optional)", example = "false", defaultValue = "false")
    private Boolean mfaEnabled;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
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
}
