package org.example.models.dto.request;

import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public class WarrantyPolicyUpdateRequest {
    private String policyName;

    private String policyCode;

    @Min(value = 1, message = "Warranty months must be at least 1")
    private Integer warrantyMonths;

    @Min(value = 0, message = "Warranty km cannot be negative")
    private Integer warrantyKm;

    @Min(value = 0, message = "Battery coverage months cannot be negative")
    private Integer batteryCoverageMonths;

    @Min(value = 0, message = "Battery coverage km cannot be negative")
    private Integer batteryCoverageKm;

    @Min(value = 0, message = "Motor coverage months cannot be negative")
    private Integer motorCoverageMonths;

    @Min(value = 0, message = "Motor coverage km cannot be negative")
    private Integer motorCoverageKm;

    @Min(value = 0, message = "Inverter coverage months cannot be negative")
    private Integer inverterCoverageMonths;

    @Min(value = 0, message = "Inverter coverage km cannot be negative")
    private Integer inverterCoverageKm;

    private Boolean isDefault;

    private Boolean isActive;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    // Constructors
    public WarrantyPolicyUpdateRequest() {
    }

    // Getters and Setters
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public Integer getWarrantyKm() {
        return warrantyKm;
    }

    public void setWarrantyKm(Integer warrantyKm) {
        this.warrantyKm = warrantyKm;
    }

    public Integer getBatteryCoverageMonths() {
        return batteryCoverageMonths;
    }

    public void setBatteryCoverageMonths(Integer batteryCoverageMonths) {
        this.batteryCoverageMonths = batteryCoverageMonths;
    }

    public Integer getBatteryCoverageKm() {
        return batteryCoverageKm;
    }

    public void setBatteryCoverageKm(Integer batteryCoverageKm) {
        this.batteryCoverageKm = batteryCoverageKm;
    }

    public Integer getMotorCoverageMonths() {
        return motorCoverageMonths;
    }

    public void setMotorCoverageMonths(Integer motorCoverageMonths) {
        this.motorCoverageMonths = motorCoverageMonths;
    }

    public Integer getMotorCoverageKm() {
        return motorCoverageKm;
    }

    public void setMotorCoverageKm(Integer motorCoverageKm) {
        this.motorCoverageKm = motorCoverageKm;
    }

    public Integer getInverterCoverageMonths() {
        return inverterCoverageMonths;
    }

    public void setInverterCoverageMonths(Integer inverterCoverageMonths) {
        this.inverterCoverageMonths = inverterCoverageMonths;
    }

    public Integer getInverterCoverageKm() {
        return inverterCoverageKm;
    }

    public void setInverterCoverageKm(Integer inverterCoverageKm) {
        this.inverterCoverageKm = inverterCoverageKm;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
}

