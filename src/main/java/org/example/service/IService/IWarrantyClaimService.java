package org.example.service.IService;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.dto.request.AddClaimAttachmentRequest;
import org.example.models.dto.request.CreateWarrantyClaimRequest;
import org.example.models.dto.request.TechnicianAssignmentRequest;
import org.example.models.dto.request.WarrantyClaimSearchRequest;
import org.example.models.dto.request.WarrantyClaimUpdateRequest;
import org.example.models.dto.response.DashboardResponse;
import org.example.models.dto.response.WarrantyClaimDetailResponse;
import org.example.models.dto.response.WarrantyClaimResponse;
import org.example.models.enums.ClaimStatus;

public interface IWarrantyClaimService {
        // SC Staff Operations - Creating and managing warranty claims
        WarrantyClaimDetailResponse createDraftClaimByVin(String vin, CreateWarrantyClaimRequest request,
                        Long serviceCenterId, Long createdByUserId);

        boolean addAttachment(Long claimId, AddClaimAttachmentRequest request);

        boolean submitClaimToEvm(Long claimId);

        WarrantyClaimDetailResponse getClaimDetail(Long claimId);

        List<WarrantyClaimResponse> getClaimsByServiceCenter(Long serviceCenterId, ClaimStatus status);

        // SC Technician Operations - Handling approved claims
        boolean assignTechnicianToClaim(Long claimId, TechnicianAssignmentRequest request);

        boolean startRepairWork(Long claimId, Long technicianId);

        boolean updateRepairProgress(Long claimId, String progressNotes, Integer percentComplete);

        boolean completeRepairWork(Long claimId, WarrantyClaimUpdateRequest completionDetails);

        List<WarrantyClaimResponse> getClaimsAssignedToTechnician(Long technicianId);

        // EVM Staff Operations - Reviewing and approving claims
        List<WarrantyClaimResponse> getPendingClaimsForReview(Long oemId);

        boolean reviewClaim(Long claimId, ClaimStatus decision, String reviewNotes, Long reviewerId);

        boolean approveClaim(Long claimId, Long approverId, String approvalNotes);

        boolean rejectClaim(Long claimId, String rejectionReason, Long rejectedById);

        boolean requestAdditionalInfo(Long claimId, String infoRequest, Long requestedById);

        // Parts and Inventory Management
        boolean shipReplacementParts(Long claimId, List<String> partNumbers, String trackingNumber);

        boolean confirmPartsReceived(Long claimId, Long serviceCenterId);

        // Cost Management
        boolean updateClaimCosts(Long claimId, Double partsCost, Double laborCost, String costBreakdown);

        Double calculateTotalClaimCost(Long claimId);

        // Reporting and Analytics
        List<WarrantyClaimResponse> searchClaims(WarrantyClaimSearchRequest request);

        DashboardResponse getServiceCenterDashboard(Long serviceCenterId);

        DashboardResponse getEvmDashboard(Long oemId);

        // Warranty Validation
        boolean validateWarrantyEligibility(String vin, String componentSerial);

        boolean isClaimWithinWarrantyPeriod(Long claimId);

        // Status Tracking
        List<WarrantyClaimResponse> getClaimsByStatus(ClaimStatus status, Long organizationId);

        boolean updateClaimStatus(Long claimId, ClaimStatus newStatus, String statusNotes, Long updatedById);

        boolean deleteClaim(Long claimId);

        List<WarrantyClaimResponse> getAllClaims(int limit, int offset);

        // Notification Support
        List<String> getStakeholdersForNotification(Long claimId);

        // Audit and History
        List<WarrantyClaimResponse> getClaimHistory(Long vehicleId);

        List<WarrantyClaimResponse> getClaimsByDateRange(OffsetDateTime startDate, OffsetDateTime endDate,
                        Long organizationId);
}
