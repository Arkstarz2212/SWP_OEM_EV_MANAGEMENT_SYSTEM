package org.example.models.dto.request;

import org.example.models.enums.ClaimStatus;

public class WarrantyClaimUpdateRequest {
    private Long claimId; // maps to core: WarrantyClaim.id
    private ClaimStatus status; // maps to core: WarrantyClaim.status
    private String issueDescription; // maps to core: WarrantyClaim.issue_description
    private String dtcCode; // maps to core: WarrantyClaim.dtc_code
    private Integer mileageKmAtClaim; // maps to core: WarrantyClaim.mileage_km_at_claim

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
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
}