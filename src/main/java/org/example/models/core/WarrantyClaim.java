package org.example.models.core;

import java.time.OffsetDateTime;

public class WarrantyClaim {
    private Long id; // aoem.warranty_claims.id
    private String claim_number; // aoem.warranty_claims.claim_number
    private Long vehicle_id; // aoem.warranty_claims.vehicle_id
    private Long service_center_id; // aoem.warranty_claims.service_center_id
    private Long created_by_user_id; // aoem.warranty_claims.created_by_user_id
    private Long assigned_technician_id; // aoem.warranty_claims.assigned_technician_id
    private Long evm_reviewer_user_id; // aoem.warranty_claims.evm_reviewer_user_id
    private String status; // aoem.warranty_claims.status (VARCHAR)
    private String issue_description; // aoem.warranty_claims.issue_description
    private String dtc_code; // aoem.warranty_claims.dtc_code
    private Integer mileage_km_at_claim; // aoem.warranty_claims.mileage_km_at_claim
    private OffsetDateTime submitted_at; // aoem.warranty_claims.submitted_at
    private OffsetDateTime approved_at; // aoem.warranty_claims.approved_at

    public WarrantyClaim() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaim_number() {
        return claim_number;
    }

    public void setClaim_number(String claim_number) {
        this.claim_number = claim_number;
    }

    public Long getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(Long vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public Long getService_center_id() {
        return service_center_id;
    }

    public void setService_center_id(Long service_center_id) {
        this.service_center_id = service_center_id;
    }

    public Long getCreated_by_user_id() {
        return created_by_user_id;
    }

    public void setCreated_by_user_id(Long created_by_user_id) {
        this.created_by_user_id = created_by_user_id;
    }

    public Long getAssigned_technician_id() {
        return assigned_technician_id;
    }

    public void setAssigned_technician_id(Long assigned_technician_id) {
        this.assigned_technician_id = assigned_technician_id;
    }

    public Long getEvm_reviewer_user_id() {
        return evm_reviewer_user_id;
    }

    public void setEvm_reviewer_user_id(Long evm_reviewer_user_id) {
        this.evm_reviewer_user_id = evm_reviewer_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssue_description() {
        return issue_description;
    }

    public void setIssue_description(String issue_description) {
        this.issue_description = issue_description;
    }

    public String getDtc_code() {
        return dtc_code;
    }

    public void setDtc_code(String dtc_code) {
        this.dtc_code = dtc_code;
    }

    public Integer getMileage_km_at_claim() {
        return mileage_km_at_claim;
    }

    public void setMileage_km_at_claim(Integer mileage_km_at_claim) {
        this.mileage_km_at_claim = mileage_km_at_claim;
    }

    public OffsetDateTime getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(OffsetDateTime submitted_at) {
        this.submitted_at = submitted_at;
    }

    public OffsetDateTime getApproved_at() {
        return approved_at;
    }

    public void setApproved_at(OffsetDateTime approved_at) {
        this.approved_at = approved_at;
    }
}
