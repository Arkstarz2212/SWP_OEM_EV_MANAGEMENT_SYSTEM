package org.example.models.dto.response;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class WarrantyPolicyResponse {
    private Long id;

    // Basic policy information
    private Long oemId;
    private String policyCode;
    private String policyName;
    private String description;

    // Policy validity period
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    // Warranty terms
    private Integer warrantyPeriodMonths;
    private Integer warrantyKmLimit;

    // Coverage details (stored as JSON for flexibility)
    private String coverageDetails;

    // Policy status
    private Boolean isActive;
    private Boolean isDefault;

    // Metadata
    private OffsetDateTime createdAt;
    private Long createdByUserId;
    private OffsetDateTime updatedAt;
    private Long updatedByUserId;

    // Legacy fields for backward compatibility
    private String model;
    private String componentCategory;
    private Integer monthsCoverage;
    private Integer kmCoverage;
    private String notes;

    // Constructors
    public WarrantyPolicyResponse() {
    }

    public WarrantyPolicyResponse(Long id, String model, String componentCategory, Integer monthsCoverage,
            Integer kmCoverage) {
        this.id = id;
        this.model = model;
        this.componentCategory = componentCategory;
        this.monthsCoverage = monthsCoverage;
        this.kmCoverage = kmCoverage;
    }

    // Getters and Setters for database fields
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Long updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    // Legacy fields getters and setters for backward compatibility
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getComponentCategory() {
        return componentCategory;
    }

    public void setComponentCategory(String componentCategory) {
        this.componentCategory = componentCategory;
    }

    public Integer getMonthsCoverage() {
        return monthsCoverage;
    }

    public void setMonthsCoverage(Integer monthsCoverage) {
        this.monthsCoverage = monthsCoverage;
    }

    public Integer getKmCoverage() {
        return kmCoverage;
    }

    public void setKmCoverage(Integer kmCoverage) {
        this.kmCoverage = kmCoverage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        return "WarrantyPolicyResponse{" +
                "id=" + id +
                ", oemId=" + oemId +
                ", policyCode='" + policyCode + '\'' +
                ", policyName='" + policyName + '\'' +
                ", description='" + description + '\'' +
                ", effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                ", warrantyPeriodMonths=" + warrantyPeriodMonths +
                ", warrantyKmLimit=" + warrantyKmLimit +
                ", coverageDetails='" + coverageDetails + '\'' +
                ", isActive=" + isActive +
                ", isDefault=" + isDefault +
                ", createdAt=" + createdAt +
                ", createdByUserId=" + createdByUserId +
                ", updatedAt=" + updatedAt +
                ", updatedByUserId=" + updatedByUserId +
                '}';
    }
}