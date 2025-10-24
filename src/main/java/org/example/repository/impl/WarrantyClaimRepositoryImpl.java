package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyClaim;
import org.example.models.enums.ClaimStatus;
import org.example.repository.IRepository.IWarrantyClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WarrantyClaimRepositoryImpl implements IWarrantyClaimRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<WarrantyClaim> claimRowMapper = (rs, rowNum) -> {
        WarrantyClaim claim = new WarrantyClaim();
        claim.setId(rs.getLong("id"));
        claim.setClaimNumber(rs.getString("claim_number"));
        claim.setVehicleId(rs.getLong("vehicle_id"));
        claim.setServiceCenterId(rs.getLong("service_center_id"));
        claim.setCreatedByUserId(rs.getLong("created_by_user_id"));

        Long assignedTechnicianId = rs.getLong("assigned_technician_id");
        if (rs.wasNull())
            assignedTechnicianId = null;
        claim.setAssignedTechnicianId(assignedTechnicianId);

        Long evmReviewerUserId = rs.getLong("evm_reviewer_user_id");
        if (rs.wasNull())
            evmReviewerUserId = null;
        claim.setEvmReviewerUserId(evmReviewerUserId);

        String statusStr = rs.getString("status");
        claim.setStatus(statusStr);

        claim.setIssueDescription(rs.getString("issue_description"));
        claim.setDtcCode(rs.getString("dtc_code"));

        Integer mileage = rs.getInt("mileage_km_at_claim");
        if (rs.wasNull())
            mileage = null;
        claim.setMileageKmAtClaim(mileage);

        java.sql.Timestamp submittedAt = rs.getTimestamp("submitted_at");
        if (submittedAt != null) {
            claim.setSubmittedAt(submittedAt.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        }

        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            claim.setCreatedAt(createdAt.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        }

        java.sql.Timestamp approvedAt = rs.getTimestamp("approved_at");
        if (approvedAt != null) {
            claim.setApprovedAt(approvedAt.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        }

        return claim;
    };

    @Override
    public WarrantyClaim save(WarrantyClaim warrantyClaim) {
        if (warrantyClaim.getId() == null) {
            String sql = "INSERT INTO aoem.warranty_claims (claim_number, vehicle_id, service_center_id, created_by_user_id, "
                    + "assigned_technician_id, evm_reviewer_user_id, status, issue_description, dtc_code, "
                    + "mileage_km_at_claim, submitted_at, approved_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
                ps.setString(1, warrantyClaim.getClaimNumber());
                ps.setLong(2, warrantyClaim.getVehicleId());
                if (warrantyClaim.getServiceCenterId() != null) {
                    ps.setLong(3, warrantyClaim.getServiceCenterId());
                } else {
                    ps.setNull(3, java.sql.Types.BIGINT);
                }
                ps.setLong(4, warrantyClaim.getCreatedByUserId());

                if (warrantyClaim.getAssignedTechnicianId() != null) {
                    ps.setLong(5, warrantyClaim.getAssignedTechnicianId());
                } else {
                    ps.setNull(5, java.sql.Types.BIGINT);
                }

                if (warrantyClaim.getEvmReviewerUserId() != null) {
                    ps.setLong(6, warrantyClaim.getEvmReviewerUserId());
                } else {
                    ps.setNull(6, java.sql.Types.BIGINT);
                }

                ps.setString(7, warrantyClaim.getStatus() != null ? warrantyClaim.getStatus() : "draft");
                ps.setString(8, warrantyClaim.getIssueDescription());
                ps.setString(9, warrantyClaim.getDtcCode());

                if (warrantyClaim.getMileageKmAtClaim() != null) {
                    ps.setInt(10, warrantyClaim.getMileageKmAtClaim());
                } else {
                    ps.setNull(10, java.sql.Types.INTEGER);
                }

                if (warrantyClaim.getSubmittedAt() != null) {
                    ps.setTimestamp(11, java.sql.Timestamp.valueOf(warrantyClaim.getSubmittedAt().toLocalDateTime()));
                } else {
                    ps.setNull(11, java.sql.Types.TIMESTAMP);
                }

                if (warrantyClaim.getApprovedAt() != null) {
                    ps.setTimestamp(12, java.sql.Timestamp.valueOf(warrantyClaim.getApprovedAt().toLocalDateTime()));
                } else {
                    ps.setNull(12, java.sql.Types.TIMESTAMP);
                }

                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                warrantyClaim.setId(key.longValue());
            }
        } else {
            String sql = "UPDATE aoem.warranty_claims SET claim_number = ?, vehicle_id = ?, service_center_id = ?, "
                    + "created_by_user_id = ?, assigned_technician_id = ?, evm_reviewer_user_id = ?, status = ?, "
                    + "issue_description = ?, dtc_code = ?, mileage_km_at_claim = ?, submitted_at = ?, approved_at = ? WHERE id = ?";

            jdbcTemplate.update(sql,
                    warrantyClaim.getClaimNumber(),
                    warrantyClaim.getVehicleId(),
                    warrantyClaim.getServiceCenterId(),
                    warrantyClaim.getCreatedByUserId(),
                    warrantyClaim.getAssignedTechnicianId(),
                    warrantyClaim.getEvmReviewerUserId(),
                    warrantyClaim.getStatus() != null ? warrantyClaim.getStatus() : "draft",
                    warrantyClaim.getIssueDescription(),
                    warrantyClaim.getDtcCode(),
                    warrantyClaim.getMileageKmAtClaim(),
                    warrantyClaim.getSubmittedAt() != null
                            ? java.sql.Timestamp.valueOf(warrantyClaim.getSubmittedAt().toLocalDateTime())
                            : null,
                    warrantyClaim.getApprovedAt() != null
                            ? java.sql.Timestamp.valueOf(warrantyClaim.getApprovedAt().toLocalDateTime())
                            : null,
                    warrantyClaim.getId());
        }
        return warrantyClaim;
    }

    @Override
    public Optional<WarrantyClaim> findById(Long id) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE id = ?";
        List<WarrantyClaim> claims = jdbcTemplate.query(sql, claimRowMapper, id);
        return claims.isEmpty() ? Optional.empty() : Optional.of(claims.get(0));
    }

    @Override
    public List<WarrantyClaim> findAll() {
        String sql = "SELECT * FROM aoem.warranty_claims";
        return jdbcTemplate.query(sql, claimRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.warranty_claims WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_claims WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<WarrantyClaim> findByClaimNumber(String claimNumber) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE claim_number = ?";
        List<WarrantyClaim> claims = jdbcTemplate.query(sql, claimRowMapper, claimNumber);
        return claims.isEmpty() ? Optional.empty() : Optional.of(claims.get(0));
    }

    @Override
    public boolean existsByClaimNumber(String claimNumber) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_claims WHERE claim_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, claimNumber);
        return count != null && count > 0;
    }

    @Override
    public List<WarrantyClaim> findByStatus(ClaimStatus status) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE status = ?";
        return jdbcTemplate.query(sql, claimRowMapper, status.name());
    }

    @Override
    public List<WarrantyClaim> findByStatusIn(List<ClaimStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return List.of();
        }
        String inClause = String.join(",", statuses.stream().map(s -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM aoem.warranty_claims WHERE status IN (" + inClause + ")";
        Object[] params = statuses.stream().map(ClaimStatus::name).toArray();
        return jdbcTemplate.query(sql, claimRowMapper, params);
    }

    @Override
    public Long countByStatus(ClaimStatus status) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_claims WHERE status = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, status.name());
    }

    @Override
    public List<WarrantyClaim> findByVehicleId(Long vehicleId) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE vehicle_id = ?";
        return jdbcTemplate.query(sql, claimRowMapper, vehicleId);
    }

    @Override
    public List<WarrantyClaim> findByVehicleVin(String vin) {
        String sql = "SELECT wc.* FROM aoem.warranty_claims wc " +
                "JOIN aoem.vehicles v ON wc.vehicle_id = v.id " +
                "WHERE v.vin = ?";
        return jdbcTemplate.query(sql, claimRowMapper, vin);
    }

    @Override
    public Long countByVehicleId(Long vehicleId) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_claims WHERE vehicle_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, vehicleId);
    }

    @Override
    public List<WarrantyClaim> findByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE service_center_id = ?";
        return jdbcTemplate.query(sql, claimRowMapper, serviceCenterId);
    }

    @Override
    public List<WarrantyClaim> findByServiceCenterIdAndStatus(Long serviceCenterId, ClaimStatus status) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE service_center_id = ? AND status = ?";
        return jdbcTemplate.query(sql, claimRowMapper, serviceCenterId, status.name());
    }

    @Override
    public Long countByServiceCenterId(Long serviceCenterId) {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_claims WHERE service_center_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, serviceCenterId);
    }

    @Override
    public List<WarrantyClaim> findBySubmitDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE submitted_at BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, claimRowMapper,
                java.sql.Timestamp.valueOf(startDate),
                java.sql.Timestamp.valueOf(endDate));
    }

    @Override
    public List<WarrantyClaim> findByApprovalDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE approved_at BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, claimRowMapper,
                java.sql.Timestamp.valueOf(startDate),
                java.sql.Timestamp.valueOf(endDate));
    }

    @Override
    public List<WarrantyClaim> findByCompletionDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT wc.* FROM aoem.warranty_claims wc " +
                "JOIN aoem.service_records sr ON sr.claim_id = wc.id " +
                "WHERE sr.handover_at BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, claimRowMapper,
                java.sql.Timestamp.valueOf(startDate),
                java.sql.Timestamp.valueOf(endDate));
    }

    @Override
    public List<WarrantyClaim> searchClaims(String keyword) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE " +
                "LOWER(claim_number) LIKE LOWER(?) OR " +
                "LOWER(issue_description) LIKE LOWER(?) OR " +
                "LOWER(dtc_code) LIKE LOWER(?)";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, claimRowMapper, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<WarrantyClaim> findByDtcCodeContaining(String dtcCode) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE dtc_code LIKE ?";
        return jdbcTemplate.query(sql, claimRowMapper, "%" + dtcCode + "%");
    }

    @Override
    public List<WarrantyClaim> findByIssueDescriptionContainingIgnoreCase(String description) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE LOWER(issue_description) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, claimRowMapper, "%" + description + "%");
    }

    @Override
    public List<WarrantyClaim> findByCustomerNameOrPhone(String customerName, String phoneNumber) {
        String sql = "SELECT wc.* FROM aoem.warranty_claims wc " +
                "JOIN aoem.vehicles v ON wc.vehicle_id = v.id " +
                "JOIN aoem.users u ON v.customer_id = u.id " +
                "WHERE LOWER(u.full_name) LIKE LOWER(?) OR u.profile_data->>'phone' = ?";
        return jdbcTemplate.query(sql, claimRowMapper, "%" + customerName + "%", phoneNumber);
    }

    @Override
    public List<WarrantyClaim> findPendingClaims() {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE status IN ('submitted', 'under_review')";
        return jdbcTemplate.query(sql, claimRowMapper);
    }

    @Override
    public List<WarrantyClaim> findOverdueClaims(LocalDateTime overdueDate) {
        String sql = "SELECT * FROM aoem.warranty_claims WHERE submitted_at < ? AND status IN ('submitted', 'under_review')";
        return jdbcTemplate.query(sql, claimRowMapper, java.sql.Timestamp.valueOf(overdueDate));
    }

    @Override
    public Long countAll() {
        String sql = "SELECT COUNT(*) FROM aoem.warranty_claims";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public Double sumApprovedAmountByStatus(ClaimStatus status) {
        return 0.0;
    }

    @Override
    public Double averageProcessingTimeByServiceCenter(Long serviceCenterId) {
        return 0.0;
    }
}