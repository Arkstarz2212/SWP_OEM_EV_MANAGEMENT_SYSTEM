package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.ServiceRecord;
import org.example.repository.IRepository.IServiceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceRecordRepositoryImpl implements IServiceRecordRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ServiceRecord> rowMapper = (rs, rowNum) -> {
        ServiceRecord r = new ServiceRecord();
        r.setId(rs.getLong("id"));
        r.setVehicle_id(rs.getLong("vehicle_id"));
        r.setService_center_id(rs.getLong("service_center_id"));
        Long claimId = rs.getLong("claim_id");
        if (rs.wasNull())
            claimId = null;
        r.setClaim_id(claimId);
        Long techId = rs.getLong("technician_id");
        if (rs.wasNull())
            techId = null;
        r.setTechnician_id(techId);
        r.setService_type(rs.getString("service_type"));
        r.setDescription(rs.getString("description"));
        java.sql.Timestamp perf = rs.getTimestamp("performed_at");
        if (perf != null)
            r.setPerformed_at(perf.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        r.setResult(rs.getString("result"));
        java.sql.Timestamp hand = rs.getTimestamp("handover_at");
        if (hand != null)
            r.setHandover_at(hand.toLocalDateTime().atOffset(java.time.ZoneOffset.UTC));
        return r;
    };

    @Override
    public ServiceRecord save(ServiceRecord record) {
        if (record.getId() == null) {
            String sql = "INSERT INTO aoem.service_records (vehicle_id, service_center_id, claim_id, technician_id, service_type, description, performed_at, result, handover_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" });
                ps.setLong(1, record.getVehicle_id());
                ps.setLong(2, record.getService_center_id());
                if (record.getClaim_id() != null)
                    ps.setLong(3, record.getClaim_id());
                else
                    ps.setNull(3, java.sql.Types.BIGINT);
                if (record.getTechnician_id() != null)
                    ps.setLong(4, record.getTechnician_id());
                else
                    ps.setNull(4, java.sql.Types.BIGINT);
                ps.setString(5, record.getService_type());
                ps.setString(6, record.getDescription());
                if (record.getPerformed_at() != null)
                    ps.setTimestamp(7, java.sql.Timestamp.valueOf(record.getPerformed_at().toLocalDateTime()));
                else
                    ps.setNull(7, java.sql.Types.TIMESTAMP);
                ps.setString(8, record.getResult());
                if (record.getHandover_at() != null)
                    ps.setTimestamp(9, java.sql.Timestamp.valueOf(record.getHandover_at().toLocalDateTime()));
                else
                    ps.setNull(9, java.sql.Types.TIMESTAMP);
                return ps;
            }, kh);
            Number genId = kh.getKey();
            if (genId != null) {
                record.setId(genId.longValue());
            }
        } else {
            String sql = "UPDATE aoem.service_records SET vehicle_id=?, service_center_id=?, claim_id=?, technician_id=?, service_type=?, description=?, performed_at=?, result=?, handover_at=? WHERE id=?";
            jdbcTemplate.update(sql,
                    record.getVehicle_id(),
                    record.getService_center_id(),
                    record.getClaim_id(),
                    record.getTechnician_id(),
                    record.getService_type(),
                    record.getDescription(),
                    record.getPerformed_at() != null
                            ? java.sql.Timestamp.valueOf(record.getPerformed_at().toLocalDateTime())
                            : null,
                    record.getResult(),
                    record.getHandover_at() != null
                            ? java.sql.Timestamp.valueOf(record.getHandover_at().toLocalDateTime())
                            : null,
                    record.getId());
        }
        return record;
    }

    @Override
    public Optional<ServiceRecord> findById(Long id) {
        List<ServiceRecord> list = jdbcTemplate.query("SELECT * FROM aoem.service_records WHERE id=?", rowMapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<ServiceRecord> findByVehicleId(Long vehicleId) {
        return jdbcTemplate.query("SELECT * FROM aoem.service_records WHERE vehicle_id=?", rowMapper, vehicleId);
    }

    @Override
    public List<ServiceRecord> findByClaimId(Long claimId) {
        return jdbcTemplate.query("SELECT * FROM aoem.service_records WHERE claim_id=?", rowMapper, claimId);
    }

    @Override
    public List<ServiceRecord> findByServiceCenterId(Long serviceCenterId) {
        return jdbcTemplate.query("SELECT * FROM aoem.service_records WHERE service_center_id=?", rowMapper,
                serviceCenterId);
    }

    @Override
    public List<ServiceRecord> findByPerformedAtBetween(OffsetDateTime start, OffsetDateTime end) {
        return jdbcTemplate.query("SELECT * FROM aoem.service_records WHERE performed_at BETWEEN ? AND ?", rowMapper,
                java.sql.Timestamp.valueOf(start.toLocalDateTime()),
                java.sql.Timestamp.valueOf(end.toLocalDateTime()));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM aoem.service_records WHERE id=?", id);
    }
}
