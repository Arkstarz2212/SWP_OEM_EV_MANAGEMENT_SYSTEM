package org.example.models.core;

import java.time.OffsetDateTime;

/**
 * Warranty Claim model mapping to aoem.warranty_claims table
 */
public class WarrantyClaim {
    private Long id;
    private String claimNumber;
    private Long vehicleId;
    private Long serviceCenterId;
    private Long createdByUserId;
    private Long assignedTechnicianId;
    private Long evmReviewerUserId;
    private String status; // draft, submitted, under_review, approved, completed
    private String issueDescription;
    private String dtcCode;
    private Integer mileageKmAtClaim;
    private OffsetDateTime createdAt;
    private OffsetDateTime submittedAt;
    private OffsetDateTime approvedAt;

    public WarrantyClaim() {
    }

    // Getters and Setters
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

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }

    public Long getEvmReviewerUserId() {
        return evmReviewerUserId;
    }

    public void setEvmReviewerUserId(Long evmReviewerUserId) {
        this.evmReviewerUserId = evmReviewerUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
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

    // Legacy methods for backward compatibility (deprecated)
    @Deprecated
    public String getClaim_number() {
        return claimNumber;
    }

    @Deprecated
    public void setClaim_number(String claim_number) {
        this.claimNumber = claim_number;
    }

    @Deprecated
    public Long getVehicle_id() {
        return vehicleId;
    }

    @Deprecated
    public void setVehicle_id(Long vehicle_id) {
        this.vehicleId = vehicle_id;
    }

    @Deprecated
    public Long getService_center_id() {
        return serviceCenterId;
    }

    @Deprecated
    public void setService_center_id(Long service_center_id) {
        this.serviceCenterId = service_center_id;
    }

    @Deprecated
    public Long getCreated_by_user_id() {
        return createdByUserId;
    }

    @Deprecated
    public void setCreated_by_user_id(Long created_by_user_id) {
        this.createdByUserId = created_by_user_id;
    }

    @Deprecated
    public Long getAssigned_technician_id() {
        return assignedTechnicianId;
    }

    @Deprecated
    public void setAssigned_technician_id(Long assigned_technician_id) {
        this.assignedTechnicianId = assigned_technician_id;
    }

    @Deprecated
    public Long getEvm_reviewer_user_id() {
        return evmReviewerUserId;
    }

    @Deprecated
    public void setEvm_reviewer_user_id(Long evm_reviewer_user_id) {
        this.evmReviewerUserId = evm_reviewer_user_id;
    }

    @Deprecated
    public String getIssue_description() {
        return issueDescription;
    }

    @Deprecated
    public void setIssue_description(String issue_description) {
        this.issueDescription = issue_description;
    }

    @Deprecated
    public String getDtc_code() {
        return dtcCode;
    }

    @Deprecated
    public void setDtc_code(String dtc_code) {
        this.dtcCode = dtc_code;
    }

    @Deprecated
    public Integer getMileage_km_at_claim() {
        return mileageKmAtClaim;
    }

    @Deprecated
    public void setMileage_km_at_claim(Integer mileage_km_at_claim) {
        this.mileageKmAtClaim = mileage_km_at_claim;
    }

    @Deprecated
    public OffsetDateTime getCreated_at() {
        return createdAt;
    }

    @Deprecated
    public void setCreated_at(OffsetDateTime created_at) {
        this.createdAt = created_at;
    }

    @Deprecated
    public OffsetDateTime getSubmitted_at() {
        return submittedAt;
    }

    @Deprecated
    public void setSubmitted_at(OffsetDateTime submitted_at) {
        this.submittedAt = submitted_at;
    }

    @Deprecated
    public OffsetDateTime getApproved_at() {
        return approvedAt;
    }

    @Deprecated
    public void setApproved_at(OffsetDateTime approved_at) {
        this.approvedAt = approved_at;
    }

    @Override
    public String toString() {
        return "WarrantyClaim{" +
                "id=" + id +
                ", claimNumber='" + claimNumber + '\'' +
                ", vehicleId=" + vehicleId +
                ", serviceCenterId=" + serviceCenterId +
                ", createdByUserId=" + createdByUserId +
                ", assignedTechnicianId=" + assignedTechnicianId +
                ", evmReviewerUserId=" + evmReviewerUserId +
                ", status='" + status + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", dtcCode='" + dtcCode + '\'' +
                ", mileageKmAtClaim=" + mileageKmAtClaim +
                ", createdAt=" + createdAt +
                ", submittedAt=" + submittedAt +
                ", approvedAt=" + approvedAt +
                '}';
    }
}