package org.example.models.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.models.enums.UserRole;

public class AssignRoleRequest {
    @NotNull(message = "Role không được để trống")
    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
