package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.ClaimStatusHistory;
import org.example.models.enums.ClaimStatus;
import org.example.repository.IRepository.IClaimStatusLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ClaimStatusLogRepositoryImpl implements IClaimStatusLogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ClaimStatusHistory> rowMapper = (rs, rowNum) -> {
        ClaimStatusHistory h = new ClaimStatusHistory();
        h.setId(rs.getLong("id"));
        h.setClaimId(rs.getLong("claim_id"));
        String fromStr = rs.getString("from_status");
        if (fromStr != null)
            h.setFromStatus(ClaimStatus.valueOf(fromStr));
        String toStr = rs.getString("to_status");
        if (toStr != null)
            h.setToStatus(ClaimStatus.valueOf(toStr));
        h.setChangedByUserId(rs.getLong("changed_by_user_id"));
        h.setNote(rs.getString("note"));
        java.sql.Timestamp ts = rs.getTimestamp("changed_at");
        if (ts != null)
            h.setChangedAt(ts.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        return h;
    };

    @Override
    public ClaimStatusHistory save(ClaimStatusHistory log) {
        if (log.getId() == null) {
            String sql = "INSERT INTO warranty_claim_status_logs (claim_id, from_status, to_status, changed_by_user_id, note, changed_at) VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" });
                ps.setLong(1, log.getClaimId());
                ps.setString(2, log.getFromStatus() != null ? log.getFromStatus().name() : null);
                ps.setString(3, log.getToStatus() != null ? log.getToStatus().name() : null);
                ps.setLong(4, log.getChangedByUserId());
                ps.setString(5, log.getNote());
                ps.setTimestamp(6,
                        log.getChangedAt() != null ? java.sql.Timestamp.valueOf(log.getChangedAt().toLocalDateTime())
                                : null);
                return ps;
            }, kh);
            Number genId = kh.getKey();
            if (genId != null) {
                log.setId(genId.longValue());
            }
        } else {
            String sql = "UPDATE warranty_claim_status_logs SET claim_id=?, from_status=?, to_status=?, changed_by_user_id=?, note=?, changed_at=? WHERE id=?";
            jdbcTemplate.update(sql,
                    log.getClaimId(),
                    log.getFromStatus() != null ? log.getFromStatus().name() : null,
                    log.getToStatus() != null ? log.getToStatus().name() : null,
                    log.getChangedByUserId(),
                    log.getNote(),
                    log.getChangedAt() != null ? java.sql.Timestamp.valueOf(log.getChangedAt().toLocalDateTime())
                            : null,
                    log.getId());
        }
        return log;
    }

    @Override
    public List<ClaimStatusHistory> findByClaimId(Long claimId) {
        return jdbcTemplate.query("SELECT * FROM warranty_claim_status_logs WHERE claim_id=? ORDER BY changed_at ASC",
                rowMapper, claimId);
    }

    @Override
    public List<ClaimStatusHistory> findByChangedAtBetween(OffsetDateTime start, OffsetDateTime end) {
        return jdbcTemplate.query("SELECT * FROM warranty_claim_status_logs WHERE changed_at BETWEEN ? AND ?",
                rowMapper,
                java.sql.Timestamp.valueOf(start.toLocalDateTime()),
                java.sql.Timestamp.valueOf(end.toLocalDateTime()));
    }

    @Override
    public Optional<ClaimStatusHistory> findLastByClaimId(Long claimId) {
        List<ClaimStatusHistory> list = jdbcTemplate.query(
                "SELECT * FROM warranty_claim_status_logs WHERE claim_id=? ORDER BY changed_at DESC LIMIT 1", rowMapper,
                claimId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public void deleteByClaimId(Long claimId) {
        jdbcTemplate.update("DELETE FROM warranty_claim_status_logs WHERE claim_id=?", claimId);
    }
}
