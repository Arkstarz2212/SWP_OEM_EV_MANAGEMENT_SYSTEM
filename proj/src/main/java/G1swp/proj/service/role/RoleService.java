package G1swp.proj.service.role;

import G1swp.proj.dto.role.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleDto> getAllRoles();
    RoleDto getRoleById(UUID id);
    RoleDto getRoleByName(String name);
    RoleDto createRole(RoleDto dto);
    RoleDto updateRole(UUID id, RoleDto dto);
    void deleteRole(UUID id);
}

