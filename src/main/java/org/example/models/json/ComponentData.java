package org.example.models.json;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentData {
    @JsonProperty("component_name")
    private String componentName;

    @JsonProperty("part_number")
    private String partNumber;

    @JsonProperty("installed_at")
    private OffsetDateTime installedAt;

    @JsonProperty("replaced_at")
    private OffsetDateTime replacedAt;

    // Extended fields for AOEM warranty management
    @JsonProperty("warranty_start_date")
    private OffsetDateTime warrantyStartDate;

    @JsonProperty("installation_date")
    private OffsetDateTime installationDate;

    @JsonProperty("deactivation_date")
    private OffsetDateTime deactivationDate;

    @JsonProperty("deactivation_reason")
    private String deactivationReason;

    @JsonProperty("replacement_reason")
    private String replacementReason;

    @JsonProperty("replaced_component_serial")
    private String replacedComponentSerial;

    @JsonProperty("performed_by_user_id")
    private Long performedByUserId;

    // Default constructor
    public ComponentData() {
    }

    // Constructor with main fields
    public ComponentData(String componentName, String partNumber, OffsetDateTime installedAt) {
        this.componentName = componentName;
        this.partNumber = partNumber;
        this.installedAt = installedAt;
        this.installationDate = installedAt;
        this.warrantyStartDate = installedAt;
    }

    // Getters and Setters
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public OffsetDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(OffsetDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public OffsetDateTime getReplacedAt() {
        return replacedAt;
    }

    public void setReplacedAt(OffsetDateTime replacedAt) {
        this.replacedAt = replacedAt;
    }

    public OffsetDateTime getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public void setWarrantyStartDate(OffsetDateTime warrantyStartDate) {
        this.warrantyStartDate = warrantyStartDate;
    }

    public OffsetDateTime getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(OffsetDateTime installationDate) {
        this.installationDate = installationDate;
    }

    public OffsetDateTime getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(OffsetDateTime deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public String getDeactivationReason() {
        return deactivationReason;
    }

    public void setDeactivationReason(String deactivationReason) {
        this.deactivationReason = deactivationReason;
    }

    public String getReplacementReason() {
        return replacementReason;
    }

    public void setReplacementReason(String replacementReason) {
        this.replacementReason = replacementReason;
    }

    public String getReplacedComponentSerial() {
        return replacedComponentSerial;
    }

    public void setReplacedComponentSerial(String replacedComponentSerial) {
        this.replacedComponentSerial = replacedComponentSerial;
    }

    public Long getPerformedByUserId() {
        return performedByUserId;
    }

    public void setPerformedByUserId(Long performedByUserId) {
        this.performedByUserId = performedByUserId;
    }
}