package G1swp.proj.service.role;

import G1swp.proj.dto.role.RoleDto;
import G1swp.proj.dto.role.RoleMapper;
import G1swp.proj.model.Role;
import G1swp.proj.repository.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        log.debug("Fetching all roles");
        List<RoleDto> roles = roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Found {} roles", roles.size());
        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(UUID id) {
        log.debug("Fetching role by id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
        return RoleMapper.toDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleByName(String name) {
        log.debug("Fetching role by name: {}", name);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or blank");
        }
        Role role = roleRepository.findByName(name.trim())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with name: " + name));
        return RoleMapper.toDto(role);
    }

    @Override
    public RoleDto createRole(RoleDto dto) {
        log.debug("Creating new role: {}", dto);

        // Validate DTO
        if (dto == null) {
            throw new IllegalArgumentException("Role data is required");
        }

        // Validate and normalize name
        String rawName = dto.getName();
        if (rawName == null || rawName.isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }

        String name = rawName.trim().toLowerCase();
        if (name.length() < 2) {
            throw new IllegalArgumentException("Role name must be at least 2 characters long");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Role name cannot exceed 50 characters");
        }

        // Check for duplicates
        if (roleRepository.existsByName(name)) {
            log.warn("Attempt to create duplicate role: {}", name);
            throw new IllegalArgumentException("Role with name '" + name + "' already exists");
        }

        // Validate level
        int level = dto.getLevel();
        if (level < 0) {
            log.warn("Negative role level provided ({}), defaulting to 0", level);
            level = 0;
        }
        if (level > 100) {
            throw new IllegalArgumentException("Role level cannot exceed 100");
        }

        // Create and save role
        Role role = new Role();
        role.setName(name);
        role.setLevel(level);

        Role saved = roleRepository.save(role);
        log.info("Role created successfully: {} (id: {})", saved.getName(), saved.getId());
        return RoleMapper.toDto(saved);
    }

    @Override
    public RoleDto updateRole(UUID id, RoleDto dto) {
        log.debug("Updating role with id: {}", id);

        // Validate inputs
        if (id == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Role data is required");
        }

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));

        boolean updated = false;

        // Update name if provided
        if (dto.getName() != null && !dto.getName().isBlank()) {
            String newName = dto.getName().trim().toLowerCase();

            if (newName.length() < 2) {
                throw new IllegalArgumentException("Role name must be at least 2 characters long");
            }
            if (newName.length() > 50) {
                throw new IllegalArgumentException("Role name cannot exceed 50 characters");
            }

            if (!newName.equals(existing.getName())) {
                if (roleRepository.existsByName(newName)) {
                    log.warn("Attempt to update role to duplicate name: {}", newName);
                    throw new IllegalArgumentException("Role with name '" + newName + "' already exists");
                }
                existing.setName(newName);
                updated = true;
            }
        }

        // Update level
        int newLevel = dto.getLevel();
        if (newLevel < 0) {
            throw new IllegalArgumentException("Role level must be non-negative");
        }
        if (newLevel > 100) {
            throw new IllegalArgumentException("Role level cannot exceed 100");
        }
        if (existing.getLevel() != newLevel) {
            existing.setLevel(newLevel);
            updated = true;
        }

        if (updated) {
            Role saved = roleRepository.save(existing);
            log.info("Role updated successfully: {} (id: {})", saved.getName(), saved.getId());
            return RoleMapper.toDto(saved);
        } else {
            log.debug("No changes detected for role: {}", id);
            return RoleMapper.toDto(existing);
        }
    }

    @Override
    public void deleteRole(UUID id) {
        log.debug("Deleting role with id: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));

        // Optional: Check if role is in use by users before deleting
        // This would require UserRepository injection and a count query

        roleRepository.delete(existing);
        log.info("Role deleted successfully: {} (id: {})", existing.getName(), id);
    }
}
