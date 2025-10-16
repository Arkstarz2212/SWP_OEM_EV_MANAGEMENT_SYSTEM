package org.example.service.Serviceimpl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.models.core.ClaimStatusHistory;
import org.example.models.core.Vehicle;
import org.example.models.core.WarrantyClaim;
import org.example.models.dto.request.AddClaimAttachmentRequest;
import org.example.models.dto.request.CreateWarrantyClaimRequest;
import org.example.models.dto.request.TechnicianAssignmentRequest;
import org.example.models.dto.request.WarrantyClaimSearchRequest;
import org.example.models.dto.request.WarrantyClaimUpdateRequest;
import org.example.models.dto.response.DashboardResponse;
import org.example.models.dto.response.WarrantyClaimDetailResponse;
import org.example.models.dto.response.WarrantyClaimResponse;
import org.example.models.enums.ClaimStatus;
import org.example.models.json.WarrantyInfo;
import org.example.repository.IRepository.IVehicleRepository;
import org.example.repository.IRepository.IWarrantyClaimRepository;
import org.example.service.IService.IWarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarrantyClaimService implements IWarrantyClaimService {

    @Autowired
    private IWarrantyClaimRepository claimRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    @Autowired
    private org.example.service.IService.IClaimStatusLogService statusLogService;

    @Override
    public WarrantyClaimDetailResponse createDraftClaimByVin(String vin, CreateWarrantyClaimRequest request,
            Long serviceCenterId, Long createdByUserId) {
        if (vin == null || request == null) {
            throw new IllegalArgumentException("VIN and request are required");
        }
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Vehicle with VIN " + vin + " not found");
        }
        Vehicle vehicle = vehicleOpt.get();

        String claimNumber = generateClaimNumber();
        WarrantyClaim claim = new WarrantyClaim();
        claim.setClaim_number(claimNumber);
        claim.setVehicle_id(vehicle.getId());
        claim.setService_center_id(serviceCenterId);
        claim.setCreated_by_user_id(createdByUserId);
        claim.setAssigned_technician_id(null);
        claim.setEvm_reviewer_user_id(null);
        claim.setIssue_description(request.getIssueDescription());
        claim.setDtc_code(request.getDtcCode());
        claim.setMileage_km_at_claim(request.getMileageKmAtClaim());
        claim.setStatus(ClaimStatus.draft.name());
        claim.setCreated_at(OffsetDateTime.now());
        claim.setSubmitted_at(null);
        claim.setApproved_at(null);

        WarrantyClaim saved = claimRepository.save(claim);
        // log initial status
        ClaimStatusHistory log = new ClaimStatusHistory();
        log.setClaimId(saved.getId());
        log.setFromStatus(null);
        log.setToStatus(ClaimStatus.draft);
        log.setChangedByUserId(saved.getCreated_by_user_id());
        log.setChangedAt(OffsetDateTime.now());
        statusLogService.log(log);
        return toDetailResponse(saved, vin, vehicle.getModel());
    }

    @Override
    public boolean addAttachment(Long claimId, AddClaimAttachmentRequest request) {
        return false;
    }

    @Override
    public boolean submitClaimToEvm(Long claimId) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return false;
        WarrantyClaim claim = claimOpt.get();
        ClaimStatus old = ClaimStatus.valueOf(claim.getStatus());
        claim.setStatus(ClaimStatus.submitted.name());
        claim.setSubmitted_at(OffsetDateTime.now());
        claimRepository.save(claim);
        ClaimStatusHistory log = new ClaimStatusHistory();
        log.setClaimId(claim.getId());
        log.setFromStatus(old);
        log.setToStatus(ClaimStatus.submitted);
        log.setChangedByUserId(claim.getCreated_by_user_id());
        log.setChangedAt(OffsetDateTime.now());
        statusLogService.log(log);
        return true;
    }

    @Override
    public WarrantyClaimDetailResponse getClaimDetail(Long claimId) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty()) {
            throw new RuntimeException("Claim not found");
        }
        WarrantyClaim claim = claimOpt.get();
        String vin = null;
        String model = null;
        if (claim.getVehicle_id() != null) {
            Optional<Vehicle> vehicleOpt = vehicleRepository.findById(claim.getVehicle_id());
            if (vehicleOpt.isPresent()) {
                vin = vehicleOpt.get().getVin();
                model = vehicleOpt.get().getModel();
            }
        }
        return toDetailResponse(claim, vin, model);
    }

    @Override
    public List<WarrantyClaimResponse> getClaimsByServiceCenter(Long serviceCenterId, ClaimStatus status) {
        List<WarrantyClaim> claims;
        if (status != null) {
            claims = claimRepository.findByServiceCenterIdAndStatus(serviceCenterId, status);
        } else {
            claims = claimRepository.findByServiceCenterId(serviceCenterId);
        }
        return claims.stream().map(this::toResponse).toList();
    }

    @Override
    public boolean assignTechnicianToClaim(Long claimId, TechnicianAssignmentRequest request) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return false;
        WarrantyClaim claim = claimOpt.get();
        claim.setAssigned_technician_id(request.getTechnicianId());
        claimRepository.save(claim);
        return true;
    }

    @Override
    public boolean startRepairWork(Long claimId, Long technicianId) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return false;
        WarrantyClaim claim = claimOpt.get();
        claim.setAssigned_technician_id(technicianId);
        claim.setStatus(ClaimStatus.in_repair.name());
        claimRepository.save(claim);
        return true;
    }

    @Override
    public boolean updateRepairProgress(Long claimId, String progressNotes, Integer percentComplete) {
        // TODO: record progress notes and completion percent in claimData JSON
        return true;
    }

    @Override
    public boolean completeRepairWork(Long claimId, WarrantyClaimUpdateRequest completionDetails) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return false;
        WarrantyClaim claim = claimOpt.get();
        claim.setStatus(ClaimStatus.completed.name());
        claimRepository.save(claim);
        return true;
    }

    @Override
    public List<WarrantyClaimResponse> getClaimsAssignedToTechnician(Long technicianId) {
        // Repository does not expose direct method; filter in-memory for now
        return claimRepository.findAll().stream()
                .filter(c -> technicianId.equals(c.getAssigned_technician_id()))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<WarrantyClaimResponse> getPendingClaimsForReview(Long oemId) {
        return claimRepository.findByStatus(ClaimStatus.submitted).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public boolean reviewClaim(Long claimId, ClaimStatus decision, String reviewNotes, Long reviewerId) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return false;
        WarrantyClaim claim = claimOpt.get();
        claim.setEvm_reviewer_user_id(reviewerId);
        claim.setStatus(decision.name());
        if (decision == ClaimStatus.approved) {
            claim.setApproved_at(OffsetDateTime.now());
        }
        claimRepository.save(claim);
        return true;
    }

    @Override
    public boolean approveClaim(Long claimId, Long approverId, String approvalNotes) {
        return reviewClaim(claimId, ClaimStatus.approved, approvalNotes, approverId);
    }

    @Override
    public boolean rejectClaim(Long claimId, String rejectionReason, Long rejectedById) {
        return reviewClaim(claimId, ClaimStatus.rejected, rejectionReason, rejectedById);
    }

    @Override
    public boolean requestAdditionalInfo(Long claimId, String infoRequest, Long requestedById) {
        // TODO: record additional info request
        return true;
    }

    @Override
    public boolean shipReplacementParts(Long claimId, List<String> partNumbers, String trackingNumber) {
        // TODO: integrate with inventory/shipping
        return true;
    }

    @Override
    public boolean confirmPartsReceived(Long claimId, Long serviceCenterId) {
        // TODO: update claimData to reflect parts receipt
        return true;
    }

    @Override
    public boolean updateClaimCosts(Long claimId, Double partsCost, Double laborCost, String costBreakdown) {
        // TODO: update cost breakdown JSON
        return true;
    }

    @Override
    public Double calculateTotalClaimCost(Long claimId) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return 0.0;
        // TODO: compute from cost breakdown
        return 0.0;
    }

    @Override
    public List<WarrantyClaimResponse> searchClaims(WarrantyClaimSearchRequest request) {
        // Basic search: filter all until repository supports richer query
        List<WarrantyClaim> claims = claimRepository.findAll();
        if (request.getStatus() != null) {
            claims = claims.stream().filter(c -> request.getStatus().name().equals(c.getStatus())).toList();
        }
        if (request.getVin() != null && !request.getVin().isBlank()) {
            String vin = request.getVin().toLowerCase();
            claims = claims.stream().filter(c -> {
                if (c.getVehicle_id() == null)
                    return false;
                Optional<Vehicle> v = vehicleRepository.findById(c.getVehicle_id());
                return v.isPresent() && v.get().getVin().toLowerCase().contains(vin);
            }).toList();
        }
        return claims.stream().map(this::toResponse).toList();
    }

    @Override
    public DashboardResponse getServiceCenterDashboard(Long serviceCenterId) {
        // TODO: aggregate stats
        return new DashboardResponse();
    }

    @Override
    public DashboardResponse getEvmDashboard(Long oemId) {
        // TODO: aggregate stats
        return new DashboardResponse();
    }

    @Override
    public boolean validateWarrantyEligibility(String vin, String componentSerial) {
        if (vin == null || vin.isBlank()) {
            return false;
        }
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }
        Vehicle vehicle = vehicleOpt.get();
        WarrantyInfo info = vehicle.getWarrantyInfo();
        if (info == null) {
            return false;
        }
        // Basic check: current date within start/end; mileage within km limit if
        // present
        OffsetDateTime now = OffsetDateTime.now();
        boolean withinDates = true;
        if (info.getStartDate() != null && info.getEndDate() != null) {
            try {
                OffsetDateTime start = info.getStartDate().atStartOfDay().atOffset(now.getOffset());
                OffsetDateTime end = info.getEndDate().plusDays(1).atStartOfDay().atOffset(now.getOffset());
                withinDates = !now.isBefore(start) && now.isBefore(end);
            } catch (Exception ignored) {
                withinDates = true; // be lenient if date conversion fails
            }
        }
        boolean withinKm = true;
        if (info.getKmLimit() != null) {
            Integer currentOdometer = vehicle.getVehicleData() != null ? vehicle.getVehicleData().getOdometerKm()
                    : null;
            withinKm = currentOdometer == null || currentOdometer <= info.getKmLimit();
        }
        return withinDates && withinKm;
    }

    @Override
    public boolean isClaimWithinWarrantyPeriod(Long claimId) {
        if (claimId == null) {
            return false;
        }
        Optional<org.example.models.core.WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty()) {
            return false;
        }
        org.example.models.core.WarrantyClaim claim = claimOpt.get();
        if (claim.getVehicle_id() == null) {
            return false;
        }
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(claim.getVehicle_id());
        if (vehicleOpt.isEmpty()) {
            return false;
        }
        Vehicle vehicle = vehicleOpt.get();
        WarrantyInfo info = vehicle.getWarrantyInfo();
        if (info == null) {
            return false;
        }
        // Use submitted_at as claim date if present, otherwise now
        OffsetDateTime claimDate = claim.getSubmitted_at() != null ? claim.getSubmitted_at() : OffsetDateTime.now();
        try {
            if (info.getStartDate() != null && info.getEndDate() != null) {
                OffsetDateTime start = info.getStartDate().atStartOfDay().atOffset(claimDate.getOffset());
                OffsetDateTime end = info.getEndDate().plusDays(1).atStartOfDay().atOffset(claimDate.getOffset());
                if (claimDate.isBefore(start) || !claimDate.isBefore(end)) {
                    return false;
                }
            }
        } catch (Exception ignored) {
            // if conversion fails, skip date validation
        }
        if (info.getKmLimit() != null && vehicle.getVehicleData() != null
                && vehicle.getVehicleData().getOdometerKm() != null) {
            if (vehicle.getVehicleData().getOdometerKm() > info.getKmLimit()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<WarrantyClaimResponse> getClaimsByStatus(ClaimStatus status, Long organizationId) {
        return claimRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    @Override
    public boolean updateClaimStatus(Long claimId, ClaimStatus newStatus, String statusNotes, Long updatedById) {
        Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty())
            return false;
        WarrantyClaim claim = claimOpt.get();
        ClaimStatus old = ClaimStatus.valueOf(claim.getStatus());
        claim.setStatus(newStatus.name());
        if (newStatus == ClaimStatus.approved) {
            claim.setApproved_at(OffsetDateTime.now());
        } else if (newStatus == ClaimStatus.submitted) {
            claim.setSubmitted_at(OffsetDateTime.now());
        }
        claimRepository.save(claim);
        ClaimStatusHistory log = new ClaimStatusHistory();
        log.setClaimId(claim.getId());
        log.setFromStatus(old);
        log.setToStatus(newStatus);
        log.setChangedByUserId(updatedById);
        log.setChangedAt(OffsetDateTime.now());
        statusLogService.log(log);
        return true;
    }

    @Override
    public List<String> getStakeholdersForNotification(Long claimId) {
        // TODO: collect stakeholder user IDs/emails
        return new ArrayList<>();
    }

    @Override
    public List<WarrantyClaimResponse> getClaimHistory(Long vehicleId) {
        return claimRepository.findByVehicleId(vehicleId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<WarrantyClaimResponse> getClaimsByDateRange(OffsetDateTime startDate, OffsetDateTime endDate,
            Long organizationId) {
        // Filter by submitted_at as createdAt is not present in core
        return claimRepository.findAll().stream()
                .filter(c -> c.getSubmitted_at() != null && !c.getSubmitted_at().isBefore(startDate)
                        && !c.getSubmitted_at().isAfter(endDate))
                .map(this::toResponse)
                .toList();
    }

    // Helpers
    private String generateClaimNumber() {
        return "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    }

    private WarrantyClaimDetailResponse toDetailResponse(WarrantyClaim claim, String vin, String vehicleModel) {
        WarrantyClaimDetailResponse res = new WarrantyClaimDetailResponse();
        res.setClaimId(claim.getId());
        res.setClaimNumber(claim.getClaim_number());
        res.setVin(vin);
        res.setVehicleModel(vehicleModel);
        res.setDtcCode(claim.getDtc_code());
        res.setIssueDescription(claim.getIssue_description());
        res.setMileageKmAtClaim(claim.getMileage_km_at_claim());
        res.setStatus(ClaimStatus.valueOf(claim.getStatus()));
        res.setSubmitDate(claim.getSubmitted_at());
        res.setApprovalDate(claim.getApproved_at());
        // completion date handled by service records/logs; omitted here
        return res;
    }

    private WarrantyClaimResponse toResponse(WarrantyClaim claim) {
        WarrantyClaimResponse res = new WarrantyClaimResponse();
        res.setId(claim.getId());
        res.setClaimNumber(claim.getClaim_number());
        res.setStatus(ClaimStatus.valueOf(claim.getStatus()));
        res.setIssueDescription(claim.getIssue_description());
        res.setDtcCode(claim.getDtc_code());
        res.setMileageKmAtClaim(claim.getMileage_km_at_claim());
        res.setSubmittedAt(claim.getSubmitted_at());
        res.setApprovedAt(claim.getApproved_at());
        res.setVehicleId(claim.getVehicle_id());
        res.setServiceCenterId(claim.getService_center_id());
        res.setCreatedByUserId(claim.getCreated_by_user_id());
        res.setAssignedTechnicianId(claim.getAssigned_technician_id());
        res.setEvmReviewerUserId(claim.getEvm_reviewer_user_id());
        return res;
    }

    @Override
    public boolean deleteClaim(Long claimId) {
        try {
            Optional<WarrantyClaim> claimOpt = claimRepository.findById(claimId);
            if (claimOpt.isPresent()) {
                claimRepository.deleteById(claimId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<WarrantyClaimResponse> getAllClaims(int limit, int offset) {
        try {
            return claimRepository.findAll().stream()
                    .skip(offset)
                    .limit(limit)
                    .map(this::toResponse)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
