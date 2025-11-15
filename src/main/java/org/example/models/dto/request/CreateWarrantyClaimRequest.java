package org.example.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWarrantyClaimRequest {
    @NotNull(message = "ID xe không được để trống")
    private Long vehicleId;

    @NotBlank(message = "Mô tả sự cố không được để trống")
    private String issueDescription;

    private String dtcCode;
    private Integer mileageKmAtClaim;

    // Optional: Admin/EVM_Staff can specify service center via request body
    private Long serviceCenterId;

    // Removed cost breakdown and arbitrary claimData per simplified schema

    // Constructors
    public CreateWarrantyClaimRequest() {
    }

    // Getters and Setters
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    // No-op accessors retained intentionally removed
}
