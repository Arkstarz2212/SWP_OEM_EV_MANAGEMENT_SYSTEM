package org.example.service.Serviceimpl;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.core.ServiceRecord;
import org.example.repository.IRepository.IServiceRecordRepository;
import org.example.service.IService.IServiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRecordService implements IServiceRecordService {

    @Autowired
    private IServiceRecordRepository repository;

    @Override
    public ServiceRecord create(ServiceRecord record) {
        return repository.save(record);
    }

    @Override
    public ServiceRecord update(ServiceRecord record) {
        return repository.save(record);
    }

    @Override
    public ServiceRecord getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ServiceRecord> getByVehicle(Long vehicleId) {
        return repository.findByVehicleId(vehicleId);
    }

    @Override
    public List<ServiceRecord> getByClaim(Long claimId) {
        return repository.findByClaimId(claimId);
    }

    @Override
    public List<ServiceRecord> getByServiceCenter(Long serviceCenterId) {
        return repository.findByServiceCenterId(serviceCenterId);
    }

    @Override
    public List<ServiceRecord> getByPerformedBetween(OffsetDateTime start, OffsetDateTime end) {
        return repository.findByPerformedAtBetween(start, end);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
