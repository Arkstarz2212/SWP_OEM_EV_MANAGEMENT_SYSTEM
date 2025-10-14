package org.example.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.example.models.core.OemManufacturer;
import org.example.repository.IRepository.IOemManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of IOemManufacturerRepository
 * Uses JdbcTemplate for optimized SQL queries with JSON field support
 */
@Repository
public class OemManufacturerRepositoryImpl implements IOemManufacturerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<OemManufacturer> rowMapper = new OemManufacturerRowMapper();

    private static class OemManufacturerRowMapper implements RowMapper<OemManufacturer> {
        @Override
        public OemManufacturer mapRow(ResultSet rs, int rowNum) throws SQLException {
            OemManufacturer oem = new OemManufacturer();
            oem.setId(rs.getLong("id"));
            oem.setCode(rs.getString("code"));
            oem.setName(rs.getString("name"));
            oem.setContact(rs.getString("contact")); // raw JSON text
            return oem;
        }
    }

    @Override
    public OemManufacturer save(OemManufacturer oemManufacturer) {
        if (oemManufacturer.getId() == null) {
            String sql = "INSERT INTO aoem.oem_manufacturers (code, name, contact) VALUES (?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    oemManufacturer.getCode(),
                    oemManufacturer.getName(),
                    oemManufacturer.getContact());
            oemManufacturer.setId(id);
            return oemManufacturer;
        } else {
            String sql = "UPDATE aoem.oem_manufacturers SET code = ?, name = ?, contact = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    oemManufacturer.getCode(),
                    oemManufacturer.getName(),
                    oemManufacturer.getContact(),
                    oemManufacturer.getId());
            return oemManufacturer;
        }
    }

    @Override
    public Optional<OemManufacturer> findById(Long id) {
        String sql = "SELECT * FROM aoem.oem_manufacturers WHERE id = ?";
        List<OemManufacturer> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<OemManufacturer> findAll() {
        String sql = "SELECT * FROM aoem.oem_manufacturers ORDER BY name";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.oem_manufacturers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_manufacturers WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<OemManufacturer> findByCode(String code) {
        String sql = "SELECT * FROM aoem.oem_manufacturers WHERE code = ?";
        List<OemManufacturer> results = jdbcTemplate.query(sql, rowMapper, code);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean existsByCode(String code) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_manufacturers WHERE code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code);
        return count != null && count > 0;
    }

    @Override
    public Optional<OemManufacturer> findByName(String name) {
        String sql = "SELECT * FROM aoem.oem_manufacturers WHERE name = ?";
        List<OemManufacturer> results = jdbcTemplate.query(sql, rowMapper, name);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_manufacturers WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    @Override
    public List<OemManufacturer> findByIsActive(boolean isActive) {
        // Schema has no is_active; return all when true, empty when false
        if (!isActive)
            return List.of();
        return findAll();
    }

    @Override
    public List<OemManufacturer> findAllActive() {
        return findByIsActive(true);
    }

    @Override
    public Long countByIsActive(boolean isActive) {
        if (!isActive)
            return 0L;
        String sql = "SELECT COUNT(*) FROM aoem.oem_manufacturers";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public List<OemManufacturer> findByCodeContainingIgnoreCase(String code) {
        String sql = "SELECT * FROM aoem.oem_manufacturers WHERE LOWER(code) LIKE LOWER(?) ORDER BY code";
        return jdbcTemplate.query(sql, rowMapper, "%" + code + "%");
    }

    @Override
    public List<OemManufacturer> findByNameContainingIgnoreCase(String name) {
        String sql = "SELECT * FROM aoem.oem_manufacturers WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        return jdbcTemplate.query(sql, rowMapper, "%" + name + "%");
    }

    @Override
    public List<OemManufacturer> searchByContactInfo(String searchTerm) {
        String sql = "SELECT * FROM aoem.oem_manufacturers WHERE " +
                "(contact::json->>'email') ILIKE ? OR " +
                "(contact::json->>'phone') ILIKE ? OR " +
                "(contact::json->>'address') ILIKE ? " +
                "ORDER BY name";
        String pattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, rowMapper, pattern, pattern, pattern);
    }

    @Override
    public List<OemManufacturer> findByServiceCenterIds(List<Long> serviceCenterIds) {
        if (serviceCenterIds.isEmpty())
            return List.of();

        String sql = "SELECT DISTINCT o.* FROM aoem.oem_manufacturers o " +
                "INNER JOIN aoem.oem_service_centers osc ON o.id = osc.oem_id " +
                "WHERE osc.service_center_id IN (" +
                String.join(",", serviceCenterIds.stream().map(Object::toString).toArray(String[]::new)) +
                ") ORDER BY o.name";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean hasServiceCenterMapping(Long oemId, Long serviceCenterId) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_service_centers WHERE oem_id = ? AND service_center_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, oemId, serviceCenterId);
        return count != null && count > 0;
    }
}