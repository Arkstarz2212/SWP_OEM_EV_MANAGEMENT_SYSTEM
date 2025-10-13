package org.example.repository.IRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.ClaimStatusHistory;

public interface IClaimStatusLogRepository {
    ClaimStatusHistory save(ClaimStatusHistory log);

    List<ClaimStatusHistory> findByClaimId(Long claimId);

    List<ClaimStatusHistory> findByChangedAtBetween(OffsetDateTime start, OffsetDateTime end);

    Optional<ClaimStatusHistory> findLastByClaimId(Long claimId);

    void deleteByClaimId(Long claimId);
}
