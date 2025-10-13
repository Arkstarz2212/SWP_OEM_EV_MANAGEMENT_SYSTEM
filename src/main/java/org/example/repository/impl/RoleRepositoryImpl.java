package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.example.models.core.Role;
import org.example.repository.IRepository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements IRoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Role> rowMapper = (rs, rowNum) -> {
        Role r = new Role();
        r.setId(rs.getLong("id"));
        r.setCode(rs.getString("code"));
        r.setName(rs.getString("name"));
        r.setDescription(rs.getString("description"));
        return r;
    };

    @Override
    public Role save(Role role) {
        String sql = "INSERT INTO aoem.roles (code, name, description) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, role.getCode());
            ps.setString(2, role.getName());
            ps.setString(3, role.getDescription());
            return ps;
        }, keyHolder);
        role.setId(keyHolder.getKey().longValue());
        return role;
    }

    @Override
    public Role update(Role role) {
        String sql = "UPDATE aoem.roles SET name = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, role.getName(), role.getDescription(), role.getId());
        return role;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM aoem.roles WHERE id = ?", id);
    }

    @Override
    public Optional<Role> findById(Long id) {
        List<Role> list = jdbcTemplate.query("SELECT id, code, name, description FROM aoem.roles WHERE id = ?",
                rowMapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Role> findByCode(String code) {
        List<Role> list = jdbcTemplate.query("SELECT id, code, name, description FROM aoem.roles WHERE code = ?",
                rowMapper, code);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Role> findAll() {
        return jdbcTemplate.query("SELECT id, code, name, description FROM aoem.roles ORDER BY code", rowMapper);
    }
}
