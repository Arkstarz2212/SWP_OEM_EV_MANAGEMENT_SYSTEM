package G1swp.proj.dto.role;

import G1swp.proj.model.core.Role;

public final class RoleMapper {

    private RoleMapper() { }

    public static RoleDto toDto(Role role) {
        if (role == null) return null;
        return new RoleDto(role.getId(), role.getName(), role.getLevel());
    }

    public static Role toEntity(RoleDto dto) {
        if (dto == null) return null;
        Role role = new Role();
        // Keep id null on create; if DTO provides id for update we set it
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setLevel(dto.getLevel());
        return role;
    }
}

