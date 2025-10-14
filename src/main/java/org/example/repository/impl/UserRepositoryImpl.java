package org.example.repository.impl;

import java.util.List;
import java.util.Optional;

import org.example.models.core.User;
import org.example.models.enums.UserRole;
import org.example.repository.IRepository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFull_name(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRoleEnum(UserRole.valueOf(rs.getString("role")));
        Long sc = rs.getLong("service_center_id");
        if (rs.wasNull())
            sc = null;
        user.setService_center_id(sc);
        user.setPassword_hash(rs.getString("password_hash"));
        user.setProfile_data(rs.getString("profile_data"));
        user.setAuth_info(rs.getString("auth_info"));
        // Set is_active field
        Boolean isActive = rs.getBoolean("is_active");
        if (rs.wasNull()) {
            isActive = true; // Default to true if null
        }
        user.setIsActive(isActive);
        return user;
    };

    @Override
    public User save(User user) {
        String sql = "INSERT INTO aoem.users (full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Long newId = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                user.getFull_name(),
                user.getEmail(),
                user.getRoleEnum() != null ? user.getRoleEnum().name() : null,
                user.getService_center_id(),
                user.getPassword_hash(),
                user.getProfile_data(),
                user.getAuth_info(),
                user.getIsActive() != null ? user.getIsActive() : true);

        user.setId(newId);
        return user;
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE (profile_data::json->>'phone') = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, phoneNumber);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<User> findByPasswordResetToken(String resetToken) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE (auth_info::json->>'reset_token') = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, resetToken);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM aoem.users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public List<User> findByRole(UserRole role) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE role = ?";
        return jdbcTemplate.query(sql, userRowMapper, role.name());
    }

    @Override
    public List<User> findByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE service_center_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, serviceCenterId);
    }

    @Override
    public List<User> findByIsActive(Boolean isActive) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE is_active = ?";
        return jdbcTemplate.query(sql, userRowMapper, isActive);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE LOWER(full_name) LIKE ? OR LOWER(email) LIKE ? OR (profile_data::json->>'phone') LIKE ?";
        String searchPattern = "%" + keyword.toLowerCase() + "%";
        return jdbcTemplate.query(sql, userRowMapper, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<User> findByFullNameContainingIgnoreCase(String fullName) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE LOWER(full_name) LIKE ?";
        String searchPattern = "%" + fullName.toLowerCase() + "%";
        return jdbcTemplate.query(sql, userRowMapper, searchPattern);
    }

    // Missing required methods from interface
    @Override
    public List<User> findAll() {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // Match by email or auth_info.username if present
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE email = ? OR (auth_info::json->>'username') = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username, username);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM aoem.users WHERE (auth_info::json->>'username') = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public List<User> findByRoleAndServiceCenterId(UserRole role, Long serviceCenterId) {
        String sql = "SELECT id, full_name, email, role, service_center_id, password_hash, profile_data, auth_info, is_active FROM aoem.users WHERE role = ? AND service_center_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, role.name(), serviceCenterId);
    }

    @Override
    public Long countByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT COUNT(*) FROM aoem.users WHERE service_center_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, serviceCenterId);
    }

    @Override
    public Long countByRole(UserRole role) {
        String sql = "SELECT COUNT(*) FROM aoem.users WHERE role = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, role.name());
    }

    @Override
    public Long countByIsActive(Boolean isActive) {
        // Schema does not store is_active; return total count for compatibility when
        // true, 0 when false
        if (Boolean.FALSE.equals(isActive)) {
            return 0L;
        }
        String sql = "SELECT COUNT(*) FROM aoem.users";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}