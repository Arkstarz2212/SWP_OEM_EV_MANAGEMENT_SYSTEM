package org.example.models.core;

public class WarrantyPolicy {

    private Long id;

    // Basic policy information
    private Long oemId;
    private String policyCode;
    private String policyName;
    private String description;

    // Policy validity period
    private java.time.LocalDate effectiveFrom;
    private java.time.LocalDate effectiveTo;

    // Warranty terms
    private Integer warrantyPeriodMonths;
    private Integer warrantyKmLimit;

    // Coverage details (stored as JSON for flexibility)
    private String coverageDetails;

    // Policy status
    private Boolean isActive;
    private Boolean isDefault;

    // Metadata
    private java.time.OffsetDateTime createdAt;
    private Long createdByUserId;
    private java.time.OffsetDateTime updatedAt;
    private Long updatedByUserId;

    public WarrantyPolicy() {
    }

    public WarrantyPolicy(String model, String componentCategory, Integer monthsCoverage, Integer kmCoverage) {
        // Legacy constructor for backward compatibility
        this.policyName = model + " - " + componentCategory;
        this.policyCode = model + "_" + componentCategory;
        this.warrantyPeriodMonths = monthsCoverage;
        this.warrantyKmLimit = kmCoverage;
        this.coverageDetails = "{}";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOemId() {
        return oemId;
    }

    public void setOemId(Long oemId) {
        this.oemId = oemId;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.time.LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(java.time.LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public java.time.LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(java.time.LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Integer getWarrantyPeriodMonths() {
        return warrantyPeriodMonths;
    }

    public void setWarrantyPeriodMonths(Integer warrantyPeriodMonths) {
        this.warrantyPeriodMonths = warrantyPeriodMonths;
    }

    public Integer getWarrantyKmLimit() {
        return warrantyKmLimit;
    }

    public void setWarrantyKmLimit(Integer warrantyKmLimit) {
        this.warrantyKmLimit = warrantyKmLimit;
    }

    public String getCoverageDetails() {
        return coverageDetails;
    }

    public void setCoverageDetails(String coverageDetails) {
        this.coverageDetails = coverageDetails;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public java.time.OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public java.time.OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Long updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    // Legacy methods for backward compatibility
    public String getModel() {
        if (policyName != null && policyName.contains(" - ")) {
            return policyName.split(" - ")[0];
        }
        return "Unknown";
    }

    public void setModel(String model) {
        // Legacy setter - updates policyName
        if (policyName != null && policyName.contains(" - ")) {
            this.policyName = model + " - " + policyName.split(" - ")[1];
        } else {
            this.policyName = model + " - General";
        }
    }

    public String getComponentCategory() {
        if (policyName != null && policyName.contains(" - ")) {
            return policyName.split(" - ")[1];
        }
        return "General";
    }

    public void setComponentCategory(String componentCategory) {
        // Legacy setter - updates policyName
        if (policyName != null && policyName.contains(" - ")) {
            this.policyName = policyName.split(" - ")[0] + " - " + componentCategory;
        } else {
            this.policyName = "Unknown - " + componentCategory;
        }
    }

    public Integer getMonthsCoverage() {
        return warrantyPeriodMonths;
    }

    public void setMonthsCoverage(Integer monthsCoverage) {
        this.warrantyPeriodMonths = monthsCoverage;
    }

    public Integer getKmCoverage() {
        return warrantyKmLimit;
    }

    public void setKmCoverage(Integer kmCoverage) {
        this.warrantyKmLimit = kmCoverage;
    }

    public String getNotes() {
        return description;
    }

    public void setNotes(String notes) {
        this.description = notes;
    }

    // Legacy fields for backward compatibility (deprecated)
    @Deprecated
    public Integer getWarrantyMonths() {
        return warrantyPeriodMonths;
    }

    @Deprecated
    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyPeriodMonths = warrantyMonths;
    }

    @Deprecated
    public Integer getWarrantyKm() {
        return warrantyKmLimit;
    }

    @Deprecated
    public void setWarrantyKm(Integer warrantyKm) {
        this.warrantyKmLimit = warrantyKm;
    }

    @Deprecated
    public Integer getBatteryCoverageMonths() {
        // Extract from JSON coverage_details
        return null; // Would need JSON parsing
    }

    @Deprecated
    public void setBatteryCoverageMonths(Integer batteryCoverageMonths) {
        // Update JSON coverage_details
    }

    @Deprecated
    public Integer getBatteryCoverageKm() {
        // Extract from JSON coverage_details
        return null; // Would need JSON parsing
    }

    @Deprecated
    public void setBatteryCoverageKm(Integer batteryCoverageKm) {
        // Update JSON coverage_details
    }

    @Deprecated
    public Integer getMotorCoverageMonths() {
        // Extract from JSON coverage_details
        return null; // Would need JSON parsing
    }

    @Deprecated
    public void setMotorCoverageMonths(Integer motorCoverageMonths) {
        // Update JSON coverage_details
    }

    @Deprecated
    public Integer getMotorCoverageKm() {
        // Extract from JSON coverage_details
        return null; // Would need JSON parsing
    }

    @Deprecated
    public void setMotorCoverageKm(Integer motorCoverageKm) {
        // Update JSON coverage_details
    }

    @Deprecated
    public Integer getInverterCoverageMonths() {
        // Extract from JSON coverage_details
        return null; // Would need JSON parsing
    }

    @Deprecated
    public void setInverterCoverageMonths(Integer inverterCoverageMonths) {
        // Update JSON coverage_details
    }

    @Deprecated
    public Integer getInverterCoverageKm() {
        // Extract from JSON coverage_details
        return null; // Would need JSON parsing
    }

    @Deprecated
    public void setInverterCoverageKm(Integer inverterCoverageKm) {
        // Update JSON coverage_details
    }
}