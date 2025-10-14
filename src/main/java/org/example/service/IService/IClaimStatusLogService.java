package org.example.service.IService;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.core.ClaimStatusHistory;

public interface IClaimStatusLogService {
    ClaimStatusHistory log(ClaimStatusHistory log);

    List<ClaimStatusHistory> getByClaim(Long claimId);

    List<ClaimStatusHistory> getByChangedBetween(OffsetDateTime start, OffsetDateTime end);
}
