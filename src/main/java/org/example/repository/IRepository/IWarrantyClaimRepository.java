package org.example.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyClaim;
import org.example.models.enums.ClaimStatus;

public interface IWarrantyClaimRepository {
    // Basic CRUD operations
    WarrantyClaim save(WarrantyClaim warrantyClaim);

    Optional<WarrantyClaim> findById(Long id);

    List<WarrantyClaim> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Claim number methods
    Optional<WarrantyClaim> findByClaimNumber(String claimNumber);

    boolean existsByClaimNumber(String claimNumber);

    // Status related methods
    List<WarrantyClaim> findByStatus(ClaimStatus status);

    List<WarrantyClaim> findByStatusOrderByCreatedAtDesc(ClaimStatus status);

    List<WarrantyClaim> findByStatusIn(List<ClaimStatus> statuses);

    Long countByStatus(ClaimStatus status);

    // Vehicle related methods
    List<WarrantyClaim> findByVehicleId(Long vehicleId);

    List<WarrantyClaim> findByVehicleVin(String vin);

    Long countByVehicleId(Long vehicleId);

    // Service Center related methods
    List<WarrantyClaim> findByServiceCenterId(Long serviceCenterId);

    List<WarrantyClaim> findByServiceCenterIdAndStatus(Long serviceCenterId, ClaimStatus status);

    Long countByServiceCenterId(Long serviceCenterId);

    // Date range methods
    List<WarrantyClaim> findBySubmitDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<WarrantyClaim> findByApprovalDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<WarrantyClaim> findByCompletionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Search methods
    List<WarrantyClaim> searchClaims(String keyword);

    List<WarrantyClaim> findByDtcCodeContaining(String dtcCode);

    List<WarrantyClaim> findByIssueDescriptionContainingIgnoreCase(String description);

    // Removed JSON-dependent search methods per simplified schema

    // Advanced filtering
    List<WarrantyClaim> findByCustomerNameOrPhone(String customerName, String phoneNumber);

    List<WarrantyClaim> findPendingClaims();

    List<WarrantyClaim> findOverdueClaims(LocalDateTime overdueDate);

    // Statistics methods
    Long countAll();

    Double sumApprovedAmountByStatus(ClaimStatus status);

    Double averageProcessingTimeByServiceCenter(Long serviceCenterId);
}