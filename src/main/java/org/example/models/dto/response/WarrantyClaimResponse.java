package org.example.models.dto.response;

import java.time.OffsetDateTime;

import org.example.models.enums.ClaimStatus;

public class WarrantyClaimResponse {
    private Long id;
    private String claimNumber;
    private ClaimStatus status;
    private String issueDescription;
    private String dtcCode;
    private Integer mileageKmAtClaim;
    private OffsetDateTime submittedAt;
    private OffsetDateTime approvedAt;
    // completedAt/createdAt are not in core table; keep only submitted/approved

    // Vehicle info (from core: WarrantyClaim.vehicle_id + join vehicles)
    private Long vehicleId;
    private String vehicleVin;
    private String vehicleModel;

    // Service center info (from core: WarrantyClaim.service_center_id + join)
    private Long serviceCenterId;
    private String serviceCenterName;

    // User info (from core IDs)
    private Long createdByUserId;
    private String createdByUserName;
    private Long assignedTechnicianId;
    private String assignedTechnicianName;
    private Long evmReviewerUserId;
    private String evmReviewerName;

    // Simplified: remove cost/parts/attachments summary to match schema

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getDtcCode() {
        return dtcCode;
    }

    public void setDtcCode(String dtcCode) {
        this.dtcCode = dtcCode;
    }

    public Integer getMileageKmAtClaim() {
        return mileageKmAtClaim;
    }

    public void setMileageKmAtClaim(Integer mileageKmAtClaim) {
        this.mileageKmAtClaim = mileageKmAtClaim;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public OffsetDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(OffsetDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    // removed createdAt/completedAt accessors to match core table

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public Long getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }

    public String getAssignedTechnicianName() {
        return assignedTechnicianName;
    }

    public void setAssignedTechnicianName(String assignedTechnicianName) {
        this.assignedTechnicianName = assignedTechnicianName;
    }

    public Long getEvmReviewerUserId() {
        return evmReviewerUserId;
    }

    public void setEvmReviewerUserId(Long evmReviewerUserId) {
        this.evmReviewerUserId = evmReviewerUserId;
    }

    public String getEvmReviewerName() {
        return evmReviewerName;
    }

    public void setEvmReviewerName(String evmReviewerName) {
        this.evmReviewerName = evmReviewerName;
    }
    // Removed getters/setters for removed fields
}
