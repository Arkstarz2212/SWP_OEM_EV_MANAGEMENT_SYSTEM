package org.example.repository.impl;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;
import org.example.repository.IRepository.IWarrantyPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for Warranty Policies
 * Maps to: aoem.warranty_policies table
 */
@Repository
public class WarrantyPolicyRepositoryImpl implements IWarrantyPolicyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            java.sql.ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (columnName.equalsIgnoreCase(meta.getColumnLabel(i))) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private final RowMapper<WarrantyPolicy> rowMapper = (ResultSet rs, int rowNum) -> {
        WarrantyPolicy policy = new WarrantyPolicy();
        policy.setId(rs.getLong("id"));
        policy.setOemId(rs.getLong("oem_id"));
        policy.setPolicyCode(rs.getString("policy_code"));
        policy.setPolicyName(rs.getString("policy_name"));
        if (hasColumn(rs, "description")) {
            policy.setDescription(rs.getString("description"));
        }

        java.sql.Date effectiveFrom = rs.getDate("effective_from");
        if (effectiveFrom != null) {
            policy.setEffectiveFrom(effectiveFrom.toLocalDate());
        }

        java.sql.Date effectiveTo = rs.getDate("effective_to");
        if (effectiveTo != null) {
            policy.setEffectiveTo(effectiveTo.toLocalDate());
        }

        if (hasColumn(rs, "warranty_period_months")) {
            Integer warrantyPeriodMonths = rs.getInt("warranty_period_months");
            if (!rs.wasNull()) {
                policy.setWarrantyPeriodMonths(warrantyPeriodMonths);
            }
        } else if (hasColumn(rs, "warranty_months")) {
            Integer warrantyMonths = rs.getInt("warranty_months");
            if (!rs.wasNull()) {
                policy.setWarrantyPeriodMonths(warrantyMonths);
            }
        }

        if (hasColumn(rs, "warranty_km_limit")) {
            Integer warrantyKmLimit = rs.getInt("warranty_km_limit");
            if (!rs.wasNull()) {
                policy.setWarrantyKmLimit(warrantyKmLimit);
            }
        } else if (hasColumn(rs, "warranty_km")) {
            Integer warrantyKm = rs.getInt("warranty_km");
            if (!rs.wasNull()) {
                policy.setWarrantyKmLimit(warrantyKm);
            }
        }

        if (hasColumn(rs, "coverage_details")) {
            policy.setCoverageDetails(rs.getString("coverage_details"));
        }

        Boolean isActive = rs.getBoolean("is_active");
        policy.setIsActive(rs.wasNull() ? false : isActive);

        Boolean isDefault = rs.getBoolean("is_default");
        policy.setIsDefault(rs.wasNull() ? false : isDefault);

        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            policy.setCreatedAt(createdAt.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        }

        if (hasColumn(rs, "created_by_user_id")) {
            Long createdByUserId = rs.getLong("created_by_user_id");
            policy.setCreatedByUserId(rs.wasNull() ? null : createdByUserId);
        }

        java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            policy.setUpdatedAt(updatedAt.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        }

        if (hasColumn(rs, "updated_by_user_id")) {
            Long updatedByUserId = rs.getLong("updated_by_user_id");
            policy.setUpdatedByUserId(rs.wasNull() ? null : updatedByUserId);
        }

        return policy;
    };

    @Override
    public WarrantyPolicy save(WarrantyPolicy warrantyPolicy) {
        if (warrantyPolicy.getId() == null) {
            // Insert new policy
            // Legacy-compatible INSERT (dbpost.sql columns)
            String sql = """
                    INSERT INTO aoem.warranty_policies
                    (oem_id, policy_name, policy_code, warranty_months, warranty_km,
                     is_active, is_default, effective_from, effective_to, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(sql,
                    warrantyPolicy.getOemId(),
                    warrantyPolicy.getPolicyName(),
                    warrantyPolicy.getPolicyCode(),
                    warrantyPolicy.getWarrantyPeriodMonths(),
                    // 0 or null -> NULL means unlimited
                    (warrantyPolicy.getWarrantyKmLimit() == null || warrantyPolicy.getWarrantyKmLimit() == 0)
                            ? null
                            : warrantyPolicy.getWarrantyKmLimit(),
                    Boolean.TRUE.equals(warrantyPolicy.getIsActive()),
                    Boolean.TRUE.equals(warrantyPolicy.getIsDefault()),
                    warrantyPolicy.getEffectiveFrom() == null ? java.time.LocalDate.now()
                            : warrantyPolicy.getEffectiveFrom(),
                    warrantyPolicy.getEffectiveTo(),
                    java.time.LocalDateTime.now(),
                    java.time.LocalDateTime.now());

            // Get the generated ID
            Long id = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);
            warrantyPolicy.setId(id);
        } else {
            // Update existing policy
            String sql = """
                    UPDATE aoem.warranty_policies SET
                    oem_id = ?, policy_name = ?, policy_code = ?,
                    warranty_months = ?, warranty_km = ?,
                    is_active = ?, is_default = ?,
                    effective_from = ?, effective_to = ?,
                    updated_at = ?
                    WHERE id = ?
                    """;

            jdbcTemplate.update(sql,
                    warrantyPolicy.getOemId(),
                    warrantyPolicy.getPolicyName(),
                    warrantyPolicy.getPolicyCode(),
                    warrantyPolicy.getWarrantyPeriodMonths(),
                    (warrantyPolicy.getWarrantyKmLimit() == null || warrantyPolicy.getWarrantyKmLimit() == 0)
                            ? null
                            : warrantyPolicy.getWarrantyKmLimit(),
                    Boolean.TRUE.equals(warrantyPolicy.getIsActive()),
                    Boolean.TRUE.equals(warrantyPolicy.getIsDefault()),
                    warrantyPolicy.getEffectiveFrom(),
                    warrantyPolicy.getEffectiveTo(),
                    java.time.LocalDateTime.now(),
                    warrantyPolicy.getId());
        }

        return warrantyPolicy;
    }

    @Override
    public Optional<WarrantyPolicy> findById(Long id) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE id = ?";
        try {
            WarrantyPolicy policy = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(policy);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WarrantyPolicy> findAll() {
        String sql = "SELECT * FROM aoem.warranty_policies ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
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
            WarrantyPolicy policy = jdbcTemplate.queryForObject(sql, rowMapper, policyCode);
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
        return jdbcTemplate.query(sql, rowMapper, oemId);
    }

    @Override
    public List<WarrantyPolicy> findByOemIdAndIsActive(Long oemId, Boolean isActive) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE oem_id = ? AND is_active = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, oemId, isActive);
    }

    @Override
    public Long countByOemId(Long oemId) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_policies WHERE oem_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, oemId);
    }

    @Override
    public List<WarrantyPolicy> findByIsActive(Boolean isActive) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_active = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, isActive);
    }

    @Override
    public List<WarrantyPolicy> findAllActive() {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_active = true ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
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
            WarrantyPolicy policy = jdbcTemplate.queryForObject(sql, rowMapper, isDefault, oemId);
            return Optional.ofNullable(policy);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WarrantyPolicy> findByIsDefault(Boolean isDefault) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE is_default = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, isDefault);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveFromBefore(LocalDate date) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_from < ? ORDER BY effective_from DESC";
        return jdbcTemplate.query(sql, rowMapper, date);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveToAfter(LocalDate date) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_to > ? ORDER BY effective_to ASC";
        return jdbcTemplate.query(sql, rowMapper, date);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveFromBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_from BETWEEN ? AND ? ORDER BY effective_from ASC";
        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    @Override
    public List<WarrantyPolicy> findByEffectiveToBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE effective_to BETWEEN ? AND ? ORDER BY effective_to ASC";
        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
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
        return jdbcTemplate.query(sql, rowMapper, date, date);
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
        return jdbcTemplate.query(sql, rowMapper, beforeDate);
    }

    @Override
    public List<WarrantyPolicy> findByPolicyNameContainingIgnoreCase(String policyName) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE LOWER(policy_name) LIKE LOWER(?) ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, "%" + policyName + "%");
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
        return jdbcTemplate.query(sql, rowMapper, searchTerm, searchTerm);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyMonthsGreaterThanEqual(Integer months) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_period_months >= ? ORDER BY warranty_period_months DESC";
        return jdbcTemplate.query(sql, rowMapper, months);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyKmGreaterThanEqual(Integer km) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_km_limit >= ? ORDER BY warranty_km_limit DESC";
        return jdbcTemplate.query(sql, rowMapper, km);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyMonthsBetween(Integer minMonths, Integer maxMonths) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_period_months BETWEEN ? AND ? ORDER BY warranty_period_months ASC";
        return jdbcTemplate.query(sql, rowMapper, minMonths, maxMonths);
    }

    @Override
    public List<WarrantyPolicy> findByWarrantyKmBetween(Integer minKm, Integer maxKm) {
        String sql = "SELECT * FROM aoem.warranty_policies WHERE warranty_km_limit BETWEEN ? AND ? ORDER BY warranty_km_limit ASC";
        return jdbcTemplate.query(sql, rowMapper, minKm, maxKm);
    }

    @Override
    public List<WarrantyPolicy> findByBatteryCoverageMonthsGreaterThanEqual(Integer months) {
        // This would require JSON parsing of coverage_details field
        // For now, return empty list or implement JSON query
        String sql = "SELECT * FROM aoem.warranty_policies WHERE coverage_details::jsonb->'battery'->>'months' >= ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, months.toString());
    }

    @Override
    public List<WarrantyPolicy> findByMotorCoverageMonthsGreaterThanEqual(Integer months) {
        // This would require JSON parsing of coverage_details field
        String sql = "SELECT * FROM aoem.warranty_policies WHERE coverage_details::jsonb->'motor'->>'months' >= ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, months.toString());
    }

    @Override
    public List<WarrantyPolicy> findByInverterCoverageMonthsGreaterThanEqual(Integer months) {
        // This would require JSON parsing of coverage_details field
        String sql = "SELECT * FROM aoem.warranty_policies WHERE coverage_details::jsonb->'inverter'->>'months' >= ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, months.toString());
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