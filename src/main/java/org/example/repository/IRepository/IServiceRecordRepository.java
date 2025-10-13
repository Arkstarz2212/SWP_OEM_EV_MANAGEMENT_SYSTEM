package org.example.repository.IRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.ServiceRecord;

public interface IServiceRecordRepository {
    ServiceRecord save(ServiceRecord record);

    Optional<ServiceRecord> findById(Long id);

    List<ServiceRecord> findByVehicleId(Long vehicleId);

    List<ServiceRecord> findByClaimId(Long claimId);

    List<ServiceRecord> findByServiceCenterId(Long serviceCenterId);

    List<ServiceRecord> findByPerformedAtBetween(OffsetDateTime start, OffsetDateTime end);

    void deleteById(Long id);
}
