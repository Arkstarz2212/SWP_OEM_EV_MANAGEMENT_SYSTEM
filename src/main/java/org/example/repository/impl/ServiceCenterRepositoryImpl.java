package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.example.models.core.ServiceCenter;
import org.example.repository.IRepository.IServiceCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceCenterRepositoryImpl implements IServiceCenterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ServiceCenter> serviceCenterRowMapper = (rs, rowNum) -> {
        ServiceCenter serviceCenter = new ServiceCenter();
        serviceCenter.setId(rs.getLong("id"));
        serviceCenter.setCode(rs.getString("code"));
        serviceCenter.setName(rs.getString("name"));
        serviceCenter.setRegion(rs.getString("region"));
        serviceCenter.setContact_info(rs.getString("contact_info"));
        return serviceCenter;
    };

    @Override
    public ServiceCenter save(ServiceCenter serviceCenter) {
        String sql = "INSERT INTO aoem.service_centers (code, name, region, contact_info) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, serviceCenter.getCode());
            ps.setString(2, serviceCenter.getName());
            ps.setString(3, serviceCenter.getRegion());
            ps.setString(4, serviceCenter.getContact_info());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            serviceCenter.setId(key.longValue());
        }
        return serviceCenter;
    }

    @Override
    public Optional<ServiceCenter> findById(Long id) {
        String sql = "SELECT * FROM aoem.service_centers WHERE id = ?";
        List<ServiceCenter> serviceCenters = jdbcTemplate.query(sql, serviceCenterRowMapper, id);
        return serviceCenters.isEmpty() ? Optional.empty() : Optional.of(serviceCenters.get(0));
    }

    @Override
    public List<ServiceCenter> findAll() {
        String sql = "SELECT * FROM aoem.service_centers";
        return jdbcTemplate.query(sql, serviceCenterRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.service_centers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.service_centers WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<ServiceCenter> findByNameContainingIgnoreCase(String name) {
        String sql = "SELECT * FROM aoem.service_centers WHERE LOWER(name) LIKE ?";
        String searchPattern = "%" + name.toLowerCase() + "%";
        return jdbcTemplate.query(sql, serviceCenterRowMapper, searchPattern);
    }

    @Override
    public List<ServiceCenter> findByAddressContainingIgnoreCase(String address) {
        String sql = "SELECT * FROM aoem.service_centers WHERE (contact_info::json->>'address') ILIKE ?";
        String searchPattern = "%" + address.toLowerCase() + "%";
        return jdbcTemplate.query(sql, serviceCenterRowMapper, searchPattern);
    }

    @Override
    public List<ServiceCenter> findByContactPersonContainingIgnoreCase(String contactPerson) {
        String sql = "SELECT * FROM aoem.service_centers WHERE (contact_info::json->>'contact_person') ILIKE ?";
        String searchPattern = "%" + contactPerson.toLowerCase() + "%";
        return jdbcTemplate.query(sql, serviceCenterRowMapper, searchPattern);
    }

    @Override
    public Optional<ServiceCenter> findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM aoem.service_centers WHERE (contact_info::json->>'phone') = ?";
        List<ServiceCenter> serviceCenters = jdbcTemplate.query(sql, serviceCenterRowMapper, phoneNumber);
        return serviceCenters.isEmpty() ? Optional.empty() : Optional.of(serviceCenters.get(0));
    }

    @Override
    public Optional<ServiceCenter> findByEmail(String email) {
        String sql = "SELECT * FROM aoem.service_centers WHERE (contact_info::json->>'email') = ?";
        List<ServiceCenter> serviceCenters = jdbcTemplate.query(sql, serviceCenterRowMapper, email);
        return serviceCenters.isEmpty() ? Optional.empty() : Optional.of(serviceCenters.get(0));
    }

    @Override
    public List<ServiceCenter> findByIsActive(Boolean isActive) {
        // Schema has no is_active; return all when true, empty when false
        if (Boolean.FALSE.equals(isActive))
            return List.of();
        return findAll();
    }

    @Override
    public List<ServiceCenter> findActiveServiceCenters() {
        return findAll();
    }

    @Override
    public Optional<ServiceCenter> findByLicenseNumber(String licenseNumber) {
        String sql = "SELECT * FROM aoem.service_centers WHERE (contact_info::json->>'license') = ?";
        List<ServiceCenter> serviceCenters = jdbcTemplate.query(sql, serviceCenterRowMapper, licenseNumber);
        return serviceCenters.isEmpty() ? Optional.empty() : Optional.of(serviceCenters.get(0));
    }

    @Override
    public boolean existsByLicenseNumber(String licenseNumber) {
        String sql = "SELECT COUNT(*) FROM aoem.service_centers WHERE (contact_info::json->>'license') = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, licenseNumber);
        return count != null && count > 0;
    }

    @Override
    public List<ServiceCenter> searchServiceCenters(String keyword) {
        String sql = "SELECT * FROM aoem.service_centers WHERE LOWER(name) LIKE ? OR LOWER(code) LIKE ? OR (contact_info::json->>'address') ILIKE ? OR (contact_info::json->>'phone') ILIKE ?";
        String searchPattern = "%" + keyword.toLowerCase() + "%";
        return jdbcTemplate.query(sql, serviceCenterRowMapper, searchPattern, searchPattern, searchPattern, keyword);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM aoem.service_centers WHERE (contact_info::json->>'email') = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        String sql = "SELECT COUNT(*) FROM aoem.service_centers WHERE (contact_info::json->>'phone') = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phoneNumber);
        return count != null && count > 0;
    }

    @Override
    public Long countByIsActive(Boolean isActive) {
        if (Boolean.FALSE.equals(isActive))
            return 0L;
        String sql = "SELECT COUNT(*) FROM aoem.service_centers";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public Long countActiveServiceCenters() {
        String sql = "SELECT COUNT(*) FROM aoem.service_centers";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}