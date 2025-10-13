package org.example.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.OemServiceCenter;
import org.example.repository.IRepository.IOemServiceCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of IOemServiceCenterRepository
 * Handles Many-to-Many relationship between OEM and Service Centers
 */
@Repository
public class OemServiceCenterRepositoryImpl implements IOemServiceCenterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<OemServiceCenter> rowMapper = new OemServiceCenterRowMapper();

    private static class OemServiceCenterRowMapper implements RowMapper<OemServiceCenter> {
        @Override
        public OemServiceCenter mapRow(ResultSet rs, int rowNum) throws SQLException {
            OemServiceCenter mapping = new OemServiceCenter();
            mapping.setId(rs.getLong("id"));
            mapping.setOem_id(rs.getLong("oem_id"));
            mapping.setService_center_id(rs.getLong("service_center_id"));
            return mapping;
        }
    }

    @Override
    public OemServiceCenter save(OemServiceCenter oemServiceCenter) {
        String sql = "INSERT INTO aoem.oem_service_centers (oem_id, service_center_id) VALUES (?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                oemServiceCenter.getOem_id(),
                oemServiceCenter.getService_center_id());
        oemServiceCenter.setId(id);
        return oemServiceCenter;
    }

    @Override
    public Optional<OemServiceCenter> findById(Long id) {
        // Note: This table uses composite key, so this method may not be meaningful
        // Returning empty for composite key table
        String sql = "SELECT * FROM aoem.oem_service_centers WHERE id = ?";
        List<OemServiceCenter> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<OemServiceCenter> findAll() {
        String sql = "SELECT * FROM aoem.oem_service_centers ORDER BY oem_id, service_center_id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.oem_service_centers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_service_centers WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<OemServiceCenter> findByOemIdAndServiceCenterId(Long oemId, Long serviceCenterId) {
        String sql = "SELECT * FROM aoem.oem_service_centers WHERE oem_id = ? AND service_center_id = ?";
        List<OemServiceCenter> results = jdbcTemplate.query(sql, rowMapper, oemId, serviceCenterId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean existsByOemIdAndServiceCenterId(Long oemId, Long serviceCenterId) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_service_centers WHERE oem_id = ? AND service_center_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, oemId, serviceCenterId);
        return count != null && count > 0;
    }

    @Override
    public void deleteByOemIdAndServiceCenterId(Long oemId, Long serviceCenterId) {
        String sql = "DELETE FROM aoem.oem_service_centers WHERE oem_id = ? AND service_center_id = ?";
        jdbcTemplate.update(sql, oemId, serviceCenterId);
    }

    @Override
    public List<OemServiceCenter> findByOemId(Long oemId) {
        String sql = "SELECT * FROM aoem.oem_service_centers WHERE oem_id = ? ORDER BY service_center_id";
        return jdbcTemplate.query(sql, rowMapper, oemId);
    }

    @Override
    public List<Long> findServiceCenterIdsByOemId(Long oemId) {
        String sql = "SELECT service_center_id FROM aoem.oem_service_centers WHERE oem_id = ? ORDER BY service_center_id";
        return jdbcTemplate.queryForList(sql, Long.class, oemId);
    }

    @Override
    public Long countByOemId(Long oemId) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_service_centers WHERE oem_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, oemId);
    }

    @Override
    public void deleteByOemId(Long oemId) {
        String sql = "DELETE FROM aoem.oem_service_centers WHERE oem_id = ?";
        jdbcTemplate.update(sql, oemId);
    }

    @Override
    public List<OemServiceCenter> findByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT * FROM aoem.oem_service_centers WHERE service_center_id = ? ORDER BY oem_id";
        return jdbcTemplate.query(sql, rowMapper, serviceCenterId);
    }

    @Override
    public List<Long> findOemIdsByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT oem_id FROM aoem.oem_service_centers WHERE service_center_id = ? ORDER BY oem_id";
        return jdbcTemplate.queryForList(sql, Long.class, serviceCenterId);
    }

    @Override
    public Long countByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT COUNT(*) FROM aoem.oem_service_centers WHERE service_center_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, serviceCenterId);
    }

    @Override
    public void deleteByServiceCenterId(Long serviceCenterId) {
        String sql = "DELETE FROM aoem.oem_service_centers WHERE service_center_id = ?";
        jdbcTemplate.update(sql, serviceCenterId);
    }

    @Override
    public List<OemServiceCenter> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM aoem.oem_service_centers WHERE created_at BETWEEN ? AND ? ORDER BY created_at";
        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    @Override
    public List<OemServiceCenter> findByCreatedAtAfter(LocalDateTime date) {
        String sql = "SELECT * FROM aoem.oem_service_centers WHERE created_at > ? ORDER BY created_at";
        return jdbcTemplate.query(sql, rowMapper, date);
    }

    @Override
    public List<OemServiceCenter> findByOemIdIn(List<Long> oemIds) {
        if (oemIds.isEmpty())
            return List.of();

        String sql = "SELECT * FROM aoem.oem_service_centers WHERE oem_id IN (" +
                String.join(",", oemIds.stream().map(Object::toString).toArray(String[]::new)) +
                ") ORDER BY oem_id, service_center_id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<OemServiceCenter> findByServiceCenterIdIn(List<Long> serviceCenterIds) {
        if (serviceCenterIds.isEmpty())
            return List.of();

        String sql = "SELECT * FROM aoem.oem_service_centers WHERE service_center_id IN (" +
                String.join(",", serviceCenterIds.stream().map(Object::toString).toArray(String[]::new)) +
                ") ORDER BY oem_id, service_center_id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteByOemIdIn(List<Long> oemIds) {
        if (oemIds.isEmpty())
            return;

        String sql = "DELETE FROM aoem.oem_service_centers WHERE oem_id IN (" +
                String.join(",", oemIds.stream().map(Object::toString).toArray(String[]::new)) + ")";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteByServiceCenterIdIn(List<Long> serviceCenterIds) {
        if (serviceCenterIds.isEmpty())
            return;

        String sql = "DELETE FROM aoem.oem_service_centers WHERE service_center_id IN (" +
                String.join(",", serviceCenterIds.stream().map(Object::toString).toArray(String[]::new)) + ")";
        jdbcTemplate.update(sql);
    }

    @Override
    public void saveAllMappings(Long oemId, List<Long> serviceCenterIds) {
        if (serviceCenterIds.isEmpty())
            return;

        String sql = "INSERT INTO aoem.oem_service_centers (oem_id, service_center_id) VALUES (?, ?) " +
                "ON CONFLICT (oem_id, service_center_id) DO NOTHING";

        for (Long serviceCenterId : serviceCenterIds) {
            jdbcTemplate.update(sql, oemId, serviceCenterId);
        }
    }

    @Override
    public void removeAllMappings(Long oemId, List<Long> serviceCenterIds) {
        if (serviceCenterIds.isEmpty())
            return;

        String sql = "DELETE FROM aoem.oem_service_centers WHERE oem_id = ? AND service_center_id IN (" +
                String.join(",", serviceCenterIds.stream().map(Object::toString).toArray(String[]::new)) + ")";
        jdbcTemplate.update(sql, oemId);
    }

    @Override
    public void replaceAllMappings(Long oemId, List<Long> newServiceCenterIds) {
        // First delete all existing mappings
        deleteByOemId(oemId);
        // Then add new mappings
        saveAllMappings(oemId, newServiceCenterIds);
    }

    @Override
    public boolean isValidMapping(Long oemId, Long serviceCenterId) {
        // Check if both OEM and Service Center exist and are active
        String sql = "SELECT COUNT(*) FROM aoem.oem_manufacturers o " +
                "INNER JOIN aoem.service_centers sc ON sc.id = ? " +
                "WHERE o.id = ? AND o.is_active = 1 AND sc.is_active = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, serviceCenterId, oemId);
        return count != null && count > 0;
    }

    @Override
    public List<Long> findUnmappedServiceCenters(Long oemId) {
        String sql = "SELECT id FROM aoem.service_centers WHERE is_active = 1 AND id NOT IN " +
                "(SELECT service_center_id FROM aoem.oem_service_centers WHERE oem_id = ?) " +
                "ORDER BY name";
        return jdbcTemplate.queryForList(sql, Long.class, oemId);
    }

    @Override
    public List<Long> findUnmappedOems(Long serviceCenterId) {
        String sql = "SELECT id FROM aoem.oem_manufacturers WHERE is_active = 1 AND id NOT IN " +
                "(SELECT oem_id FROM aoem.oem_service_centers WHERE service_center_id = ?) " +
                "ORDER BY name";
        return jdbcTemplate.queryForList(sql, Long.class, serviceCenterId);
    }
}