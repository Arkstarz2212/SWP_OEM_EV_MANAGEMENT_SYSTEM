package org.example.service.Serviceimpl;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.core.ClaimStatusHistory;
import org.example.repository.IRepository.IClaimStatusLogRepository;
import org.example.service.IService.IClaimStatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimStatusLogService implements IClaimStatusLogService {

    @Autowired
    private IClaimStatusLogRepository repository;

    @Override
    public ClaimStatusHistory log(ClaimStatusHistory log) {
        return repository.save(log);
    }

    @Override
    public List<ClaimStatusHistory> getByClaim(Long claimId) {
        return repository.findByClaimId(claimId);
    }

    @Override
    public List<ClaimStatusHistory> getByChangedBetween(OffsetDateTime start, OffsetDateTime end) {
        return repository.findByChangedAtBetween(start, end);
    }
}
