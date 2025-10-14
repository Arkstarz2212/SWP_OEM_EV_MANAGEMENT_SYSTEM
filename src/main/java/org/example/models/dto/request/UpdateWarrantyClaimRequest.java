package org.example.models.dto.request;

public class UpdateWarrantyClaimRequest {
    private String issueDescription;
    private String dtcCode;
    private Integer mileageKmAtClaim;
    // removed estimatedAmount/reviewNotes to match simplified core schema

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
