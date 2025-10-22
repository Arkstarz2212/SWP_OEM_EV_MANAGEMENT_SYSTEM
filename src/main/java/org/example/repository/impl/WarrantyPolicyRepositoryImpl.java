package org.example.repository.impl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;
import org.example.repository.IRepository.IWarrantyPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for Warranty Policies
 * Maps to: aoem.warranty_policies table
 */
@Repository
public class WarrantyPolicyRepositoryImpl implements IWarrantyPolicyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public WarrantyPolicy save(WarrantyPolicy warrantyPolicy) {
        if (warrantyPolicy.getId() == null) {
            // Insert new policy
            String sql = """
                    INSERT INTO aoem.warranty_policies
                    (oem_id, policy_code, policy_name, description, effective_from, effective_to,
                     warranty_period_months, warranty_km_limit, coverage_details, is_active, is_default,
                     created_at, created_by_user_id, updated_at, updated_by_user_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(sql,
                    warrantyPolicy.getOemId(),
                    warrantyPolicy.getPolicyCode(),
                    warrantyPolicy.getPolicyName(),
                    warrantyPolicy.getDescription(),
                    warrantyPolicy.getEffectiveFrom(),
                    warrantyPolicy.getEffectiveTo(),
                    warrantyPolicy.getWarrantyPeriodMonths(),
                    warrantyPolicy.getWarrantyKmLimit(),
                    warrantyPolicy.getCoverageDetails(),
                    warrantyPolicy.getIsActive(),
                    warrantyPolicy.getIsDefault(),
                    OffsetDateTime.now(),
                    warrantyPolicy.getCreatedByUserId(),
                    OffsetDateTime.now(),
                    warrantyPolicy.getUpdatedByUserId());

            // Get the generated ID
            Long id = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);
            warrantyPolicy.setId(id);
        } else {
            // Update existing policy
            String sql = """
                    UPDATE aoem.warranty_policies SET
                    oem_id = ?, policy_code = ?, policy_name = ?, description = ?, effective_from = ?, effective_to = ?,
                    warranty_period_months = ?, warranty_km_limit = ?, coverage_details = ?, is_active = ?, is_default = ?,
                    updated_at = ?, updated_by_user_id = ?
                    WHERE id = ?
                    """;

            jdbcTemplate.update(sql,
                    warrantyPolicy.getOemId(),
                    warrantyPolicy.getPolicyCode(),
                    warrantyPolicy.getPolicyName(),
                    warrantyPolicy.getDescription(),
                    warrantyPolicy.getEffectiveFrom(),
                    warrantyPolicy.getEffectiveTo(),
                    warrantyPolicy.getWarrantyPeriodMonths(),
                    warrantyPolicy.getWarrantyKmLimit(),
                    warrantyPolicy.getCoverageDetails(),
                    warrantyPolicy.getIsActive(),
                    warrantyPolicy.getIsDefault(),
                    OffsetDateTime.now(),
                    warrantyPolicy.getUpdatedByUserId(),
                    warrantyPolicy.getId());
        }

        return warrantyPolicy;
    }

    @Override
    public Optional<WarrantyPolicy> findById(Long id) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE id = ?";
        try {
            WarrantyPolicy policy = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(WarrantyPolicy.class), id);
            return Optional.ofNullable(policy);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WarrantyPolicy> findAll() {
        String sql = "SELECT * FROM aoem.warranty_policies ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.warranty_policies WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_policies WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<WarrantyPolicy> findByPolicyCode(String policyCode) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE policy_code = ?";
        try {
            WarrantyPolicy policy = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(WarrantyPolicy.class), policyCode);
            return Optional.ofNullable(policy);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByPolicyCode(String policyCode) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_policies WHERE policy_code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, policyCode);
        return count != null && count > 0;
    }

    @Override
    public List<WarrantyPolicy> findByOemId(Long oemId) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE oem_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), oemId);
    }

    @Override
    public List<WarrantyPolicy> findByOemIdAndIsActive(Long oemId, Boolean isActive) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE oem_id = ? AND is_active = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), oemId, isActive);
    }

    @Override
    public Long countByOemId(Long oemId) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_policies WHERE oem_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, oemId);
    }

    @Override
    public List<WarrantyPolicy> findByIsActive(Boolean isActive) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_active = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), isActive);
    }

    @Override
    public List<WarrantyPolicy> findAllActive() {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_active = true ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class));
    }

    @Override
    public Long countByIsActive(Boolean isActive) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_policies WHERE is_active = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, isActive);
    }

    @Override
    public Optional<WarrantyPolicy> findByIsDefaultAndOemId(Boolean isDefault, Long oemId) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_default = ? AND oem_id = ?";
        try {
            WarrantyPolicy policy = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(WarrantyPolicy.class), isDefault, oemId);
            return Optional.ofNullable(policy);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WarrantyPolicy> findByIsDefault(Boolean isDefault) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_default = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), isDefault);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveFromBefore(LocalDate date) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_from < ? ORDER BY effective_from DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), date);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveToAfter(LocalDate date) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_to > ? ORDER BY effective_to ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), date);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveFromBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_from BETWEEN ? AND ? ORDER BY effective_from ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), startDate, endDate);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveToBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_to BETWEEN ? AND ? ORDER BY effective_to ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), startDate, endDate);
    }

    @Override
    public List<WarrantyPolicy> findActivePoliciesForDate(LocalDate date) {
        String sql = """
                SELECT * FROM aoem.warranty_policies
                WHERE is_active = true
                AND effective_from <= ?
                AND (effective_to IS NULL OR effective_to >= ?)
                ORDER BY effective_from DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), date, date);
    }

    @Override
    public List<WarrantyPolicy> findExpiringPolicies(LocalDate beforeDate) {
        String sql = """
                SELECT * FROM aoem.warranty_policies
                WHERE is_active = true
                AND effective_to IS NOT NULL
                AND effective_to <= ?
                ORDER BY effective_to ASC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), beforeDate);
    }

    @Override
    public List<WarrantyPolicy> findByPolicyNameContainingIgnoreCase(String policyName) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE LOWER(policy_name) LIKE LOWER(?) ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), "%" + policyName + "%");
    }

    @Override
    public List<WarrantyPolicy> searchPolicies(String keyword) {
        String sql = """
                SELECT * FROM aoem.warranty_policies
                WHERE LOWER(policy_name) LIKE LOWER(?)
                OR LOWER(policy_code) LIKE LOWER(?)
                ORDER BY created_at DESC
                """;
        String searchTerm = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), searchTerm, searchTerm);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyMonthsGreaterThanEqual(Integer months) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_period_months >= ? ORDER BY warranty_period_months DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), months);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyKmGreaterThanEqual(Integer km) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_km_limit >= ? ORDER BY warranty_km_limit DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), km);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyMonthsBetween(Integer minMonths, Integer maxMonths) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_period_months BETWEEN ? AND ? ORDER BY warranty_period_months ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), minMonths, maxMonths);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyKmBetween(Integer minKm, Integer maxKm) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_km_limit BETWEEN ? AND ? ORDER BY warranty_km_limit ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), minKm, maxKm);
    }

    @Override
    public List<WarrantyPolicy> findByBatteryCoverageMonthsGreaterThanEqual(Integer months) {
        // This would require JSON parsing of coverage_details field
        // For now, return empty list or implement JSON query
        String sql = "SELECT * FROM aoem.warranty_policies WHERE coverage_details::jsonb->'battery'->>'months' >= ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), months.toString());
    }

    @Override
    public List<WarrantyPolicy> findByMotorCoverageMonthsGreaterThanEqual(Integer months) {
        // This would require JSON parsing of coverage_details field
        String sql = "SELECT * FROM aoem.warranty_policies WHERE coverage_details::jsonb->'motor'->>'months' >= ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), months.toString());
    }

    @Override
    public List<WarrantyPolicy> findByInverterCoverageMonthsGreaterThanEqual(Integer months) {
        // This would require JSON parsing of coverage_details field
        String sql = "SELECT * FROM aoem.warranty_policies WHERE coverage_details::jsonb->'inverter'->>'months' >= ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WarrantyPolicy.class), months.toString());
    }

    @Override
    public Long countByOemIdAndIsActive(Long oemId, Boolean isActive) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_policies WHERE oem_id = ? AND is_active = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, oemId, isActive);
    }

    @Override
    public Double averageWarrantyMonthsByOem(Long oemId) {
        String sql = "SELECT AVG(warranty_period_months) FROM aoem.warranty_policies WHERE oem_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, oemId);
    }

    @Override
    public Double averageWarrantyKmByOem(Long oemId) {
        String sql = "SELECT AVG(warranty_km_limit) FROM aoem.warranty_policies WHERE oem_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, oemId);
    }
}