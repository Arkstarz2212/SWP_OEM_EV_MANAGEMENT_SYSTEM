package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;
import org.example.repository.IRepository.IWarrantyPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WarrantyPolicyRepositoryImpl implements IWarrantyPolicyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<WarrantyPolicy> mapper = (rs, rowNum) -> {
        WarrantyPolicy policy = new WarrantyPolicy();
        policy.setId(rs.getLong("id"));
        policy.setOemId(rs.getLong("oem_id"));
        policy.setPolicyName(rs.getString("policy_name"));
        policy.setPolicyCode(rs.getString("policy_code"));

        // Map warranty period
        try {
            Integer warrantyMonths = rs.getInt("warranty_months");
            if (!rs.wasNull()) {
                policy.setWarrantyPeriodMonths(warrantyMonths);
            }
        } catch (Exception ignored) {
        }
        try {
            Integer warrantyKm = rs.getInt("warranty_km");
            if (!rs.wasNull()) {
                policy.setWarrantyKmLimit(warrantyKm);
            }
        } catch (Exception ignored) {
        }

        // Map coverage details - we'll store as JSON string
        // For now, map individual fields to legacy methods
        try {
            Integer batteryMonths = rs.getInt("battery_coverage_months");
            Integer batteryKm = rs.getInt("battery_coverage_km");
            Integer motorMonths = rs.getInt("motor_coverage_months");
            Integer motorKm = rs.getInt("motor_coverage_km");
            Integer inverterMonths = rs.getInt("inverter_coverage_months");
            Integer inverterKm = rs.getInt("inverter_coverage_km");

            // Build JSON coverage details
            StringBuilder coverage = new StringBuilder();
            coverage.append("{");
            coverage.append("\"battery\":{\"months\":").append(batteryMonths != null ? batteryMonths : "null");
            coverage.append(",\"km\":").append(batteryKm != null ? batteryKm : "null").append("},");
            coverage.append("\"motor\":{\"months\":").append(motorMonths != null ? motorMonths : "null");
            coverage.append(",\"km\":").append(motorKm != null ? motorKm : "null").append("},");
            coverage.append("\"inverter\":{\"months\":").append(inverterMonths != null ? inverterMonths : "null");
            coverage.append(",\"km\":").append(inverterKm != null ? inverterKm : "null").append("}");
            coverage.append("}");
            policy.setCoverageDetails(coverage.toString());
        } catch (Exception ignored) {
        }

        // Map status
        try {
            policy.setIsActive(rs.getBoolean("is_active"));
        } catch (Exception ignored) {
        }
        try {
            policy.setIsDefault(rs.getBoolean("is_default"));
        } catch (Exception ignored) {
        }

        // Map effective dates
        try {
            java.sql.Date effectiveFrom = rs.getDate("effective_from");
            if (effectiveFrom != null) {
                policy.setEffectiveFrom(effectiveFrom.toLocalDate());
            }
        } catch (Exception ignored) {
        }
        try {
            java.sql.Date effectiveTo = rs.getDate("effective_to");
            if (effectiveTo != null) {
                policy.setEffectiveTo(effectiveTo.toLocalDate());
            }
        } catch (Exception ignored) {
        }

        // Map timestamps
        try {
            var createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                policy.setCreatedAt(createdAt.toInstant().atOffset(java.time.ZoneOffset.UTC));
            }
        } catch (Exception ignored) {
        }
        try {
            var updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                policy.setUpdatedAt(updatedAt.toInstant().atOffset(java.time.ZoneOffset.UTC));
            }
        } catch (Exception ignored) {
        }

        return policy;
    };

    @Override
    public WarrantyPolicy save(WarrantyPolicy warrantyPolicy) {
        if (warrantyPolicy.getId() == null) {
            // Insert
            String sql = "INSERT INTO aoem.warranty_policies (oem_id, policy_name, policy_code, " +
                    "warranty_months, warranty_km, " +
                    "battery_coverage_months, battery_coverage_km, " +
                    "motor_coverage_months, motor_coverage_km, " +
                    "inverter_coverage_months, inverter_coverage_km, " +
                    "is_active, is_default, effective_from, effective_to, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
                ps.setLong(1, warrantyPolicy.getOemId());
                ps.setString(2, warrantyPolicy.getPolicyName());
                ps.setString(3, warrantyPolicy.getPolicyCode());
                ps.setInt(4, warrantyPolicy.getWarrantyPeriodMonths() != null ? warrantyPolicy.getWarrantyPeriodMonths()
                        : 36);
                ps.setObject(5, warrantyPolicy.getWarrantyKmLimit());

                // Parse coverage details from JSON or use defaults
                Integer batteryMonths = extractBatteryMonths(warrantyPolicy.getCoverageDetails());
                Integer batteryKm = extractBatteryKm(warrantyPolicy.getCoverageDetails());
                Integer motorMonths = extractMotorMonths(warrantyPolicy.getCoverageDetails());
                Integer motorKm = extractMotorKm(warrantyPolicy.getCoverageDetails());
                Integer inverterMonths = extractInverterMonths(warrantyPolicy.getCoverageDetails());
                Integer inverterKm = extractInverterKm(warrantyPolicy.getCoverageDetails());

                ps.setInt(6, batteryMonths != null ? batteryMonths : 36);
                ps.setObject(7, batteryKm);
                ps.setInt(8, motorMonths != null ? motorMonths : 36);
                ps.setObject(9, motorKm);
                ps.setInt(10, inverterMonths != null ? inverterMonths : 24);
                ps.setObject(11, inverterKm);

                ps.setBoolean(12, warrantyPolicy.getIsActive() != null ? warrantyPolicy.getIsActive() : true);
                ps.setBoolean(13, warrantyPolicy.getIsDefault() != null ? warrantyPolicy.getIsDefault() : false);
                ps.setDate(14, warrantyPolicy.getEffectiveFrom() != null
                        ? java.sql.Date.valueOf(warrantyPolicy.getEffectiveFrom())
                        : java.sql.Date.valueOf(LocalDate.now()));
                ps.setObject(15, warrantyPolicy.getEffectiveTo() != null
                        ? java.sql.Date.valueOf(warrantyPolicy.getEffectiveTo())
                        : null);
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                warrantyPolicy.setId(key.longValue());
            }
        } else {
            // Update
            String sql = "UPDATE aoem.warranty_policies SET " +
                    "policy_name = ?, policy_code = ?, " +
                    "warranty_months = ?, warranty_km = ?, " +
                    "battery_coverage_months = ?, battery_coverage_km = ?, " +
                    "motor_coverage_months = ?, motor_coverage_km = ?, " +
                    "inverter_coverage_months = ?, inverter_coverage_km = ?, " +
                    "is_active = ?, is_default = ?, " +
                    "effective_from = ?, effective_to = ?, " +
                    "updated_at = NOW() " +
                    "WHERE id = ?";

            // Parse coverage details
            Integer batteryMonths = extractBatteryMonths(warrantyPolicy.getCoverageDetails());
            Integer batteryKm = extractBatteryKm(warrantyPolicy.getCoverageDetails());
            Integer motorMonths = extractMotorMonths(warrantyPolicy.getCoverageDetails());
            Integer motorKm = extractMotorKm(warrantyPolicy.getCoverageDetails());
            Integer inverterMonths = extractInverterMonths(warrantyPolicy.getCoverageDetails());
            Integer inverterKm = extractInverterKm(warrantyPolicy.getCoverageDetails());

            jdbcTemplate.update(sql,
                    warrantyPolicy.getPolicyName(),
                    warrantyPolicy.getPolicyCode(),
                    warrantyPolicy.getWarrantyPeriodMonths(),
                    warrantyPolicy.getWarrantyKmLimit(),
                    batteryMonths,
                    batteryKm,
                    motorMonths,
                    motorKm,
                    inverterMonths,
                    inverterKm,
                    warrantyPolicy.getIsActive(),
                    warrantyPolicy.getIsDefault(),
                    warrantyPolicy.getEffectiveFrom() != null
                            ? java.sql.Date.valueOf(warrantyPolicy.getEffectiveFrom())
                            : java.sql.Date.valueOf(LocalDate.now()),
                    warrantyPolicy.getEffectiveTo() != null
                            ? java.sql.Date.valueOf(warrantyPolicy.getEffectiveTo())
                            : null,
                    warrantyPolicy.getId());
        }
        return warrantyPolicy;
    }

    private Integer extractBatteryMonths(String coverageDetails) {
        if (coverageDetails == null)
            return null;
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(coverageDetails);
            if (node.has("battery") && node.get("battery").has("months")) {
                return node.get("battery").get("months").asInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Integer extractBatteryKm(String coverageDetails) {
        if (coverageDetails == null)
            return null;
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(coverageDetails);
            if (node.has("battery") && node.get("battery").has("km")) {
                return node.get("battery").get("km").asInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Integer extractMotorMonths(String coverageDetails) {
        if (coverageDetails == null)
            return null;
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(coverageDetails);
            if (node.has("motor") && node.get("motor").has("months")) {
                return node.get("motor").get("months").asInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Integer extractMotorKm(String coverageDetails) {
        if (coverageDetails == null)
            return null;
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(coverageDetails);
            if (node.has("motor") && node.get("motor").has("km")) {
                return node.get("motor").get("km").asInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Integer extractInverterMonths(String coverageDetails) {
        if (coverageDetails == null)
            return null;
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(coverageDetails);
            if (node.has("inverter") && node.get("inverter").has("months")) {
                return node.get("inverter").get("months").asInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Integer extractInverterKm(String coverageDetails) {
        if (coverageDetails == null)
            return null;
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(coverageDetails);
            if (node.has("inverter") && node.get("inverter").has("km")) {
                return node.get("inverter").get("km").asInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public Optional<WarrantyPolicy> findById(Long id) {
        List<WarrantyPolicy> list = jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE id = ?", mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<WarrantyPolicy> findAll() {
        return jdbcTemplate.query("SELECT * FROM aoem.warranty_policies ORDER BY id DESC", mapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM aoem.warranty_policies WHERE id = ?", id);
    }

    @Override
    public boolean existsById(Long id) {
        Long cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM aoem.warranty_policies WHERE id = ?", Long.class, id);
        return cnt != null && cnt > 0;
    }

    @Override
    public Optional<WarrantyPolicy> findByPolicyCode(String policyCode) {
        List<WarrantyPolicy> list = jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE policy_code = ?", mapper, policyCode);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public boolean existsByPolicyCode(String policyCode) {
        Long cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM aoem.warranty_policies WHERE policy_code = ?", Long.class, policyCode);
        return cnt != null && cnt > 0;
    }

    @Override
    public List<WarrantyPolicy> findByOemId(Long oemId) {
        return jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE oem_id = ? ORDER BY id DESC", mapper, oemId);
    }

    @Override
    public List<WarrantyPolicy> findByOemIdAndIsActive(Long oemId, Boolean isActive) {
        return jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE oem_id = ? AND is_active = ? ORDER BY id DESC",
                mapper, oemId, isActive);
    }

    @Override
    public List<WarrantyPolicy> findByIsActive(Boolean isActive) {
        return jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE is_active = ? ORDER BY id DESC", mapper, isActive);
    }

    @Override
    public List<WarrantyPolicy> findActivePolicies() {
        return jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE is_active = true ORDER BY id DESC", mapper);
    }

    @Override
    public Optional<WarrantyPolicy> findByOemIdAndIsDefault(Long oemId, Boolean isDefault) {
        List<WarrantyPolicy> list = jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE oem_id = ? AND is_default = ?",
                mapper, oemId, isDefault);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<WarrantyPolicy> findByPolicyNameContainingIgnoreCase(String policyName) {
        String like = "%" + policyName + "%";
        return jdbcTemplate.query(
                "SELECT * FROM aoem.warranty_policies WHERE LOWER(policy_name) LIKE LOWER(?) ORDER BY id DESC",
                mapper, like);
    }

    @Override
    public Long countByOemId(Long oemId) {
        Long cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM aoem.warranty_policies WHERE oem_id = ?", Long.class, oemId);
        return cnt == null ? 0L : cnt;
    }

    @Override
    public Long countByIsActive(Boolean isActive) {
        Long cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM aoem.warranty_policies WHERE is_active = ?", Long.class, isActive);
        return cnt == null ? 0L : cnt;
    }
}
