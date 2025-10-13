package org.example.service.IService;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.core.ServiceRecord;

public interface IServiceRecordService {
    ServiceRecord create(ServiceRecord record);

    ServiceRecord update(ServiceRecord record);

    List<ServiceRecord> getByVehicle(Long vehicleId);

    List<ServiceRecord> getByClaim(Long claimId);

    List<ServiceRecord> getByServiceCenter(Long serviceCenterId);

    List<ServiceRecord> getByPerformedBetween(OffsetDateTime start, OffsetDateTime end);

    void delete(Long id);
}
