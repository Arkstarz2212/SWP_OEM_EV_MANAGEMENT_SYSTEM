package org.example.models.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for validating warranty eligibility and coverage
 * Used for Use Cases: Warranty validation, coverage checking
 */
public class WarrantyValidationRequest {
    @NotBlank(message = "VIN không được để trống")
    private String vin;

    private String partNumber;
    private String serialNumber;
    private Integer currentMileage;
    private LocalDate serviceDate;
    private String issueDescription;
    private String dtcCode;

    // Validation criteria
    private boolean checkWarrantyExpiry = true;
    private boolean checkMileageLimit = true;
    private boolean checkPartCoverage = true;
    private boolean checkExclusions = true;

    // Constructors
    public WarrantyValidationRequest() {
    }

    // Getters and Setters
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(Integer currentMileage) {
        this.currentMileage = currentMileage;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
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

    public boolean isCheckWarrantyExpiry() {
        return checkWarrantyExpiry;
    }

    public void setCheckWarrantyExpiry(boolean checkWarrantyExpiry) {
        this.checkWarrantyExpiry = checkWarrantyExpiry;
    }

    public boolean isCheckMileageLimit() {
        return checkMileageLimit;
    }

    public void setCheckMileageLimit(boolean checkMileageLimit) {
        this.checkMileageLimit = checkMileageLimit;
    }

    public boolean isCheckPartCoverage() {
        return checkPartCoverage;
    }

    public void setCheckPartCoverage(boolean checkPartCoverage) {
        this.checkPartCoverage = checkPartCoverage;
    }

    public boolean isCheckExclusions() {
        return checkExclusions;
    }

    public void setCheckExclusions(boolean checkExclusions) {
        this.checkExclusions = checkExclusions;
    }
}