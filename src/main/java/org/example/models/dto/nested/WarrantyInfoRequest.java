package org.example.models.dto.nested;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Nested DTO for vehicles.warranty_info JSON field
 * Contains all warranty-related information for a vehicle
 */
public class WarrantyInfoRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer kmLimit;
    private List<String> policies;
    private List<String> certificates;
    private Map<String, Object> coverageRules;

    // Coverage rules specific fields
    private boolean batteryFullCoverage = true;
    private boolean motorFullCoverage = true;
    private boolean inverterFullCoverage = true;
    private boolean bmsFullCoverage = true;
    private boolean chargerPartialCoverage = true;

    // Constructors
    public WarrantyInfoRequest() {
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getKmLimit() {
        return kmLimit;
    }

    public void setKmLimit(Integer kmLimit) {
        this.kmLimit = kmLimit;
    }

    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }

    public List<String> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<String> certificates) {
        this.certificates = certificates;
    }

    public Map<String, Object> getCoverageRules() {
        return coverageRules;
    }

    public void setCoverageRules(Map<String, Object> coverageRules) {
        this.coverageRules = coverageRules;
    }

    public boolean isBatteryFullCoverage() {
        return batteryFullCoverage;
    }

    public void setBatteryFullCoverage(boolean batteryFullCoverage) {
        this.batteryFullCoverage = batteryFullCoverage;
    }

    public boolean isMotorFullCoverage() {
        return motorFullCoverage;
    }

    public void setMotorFullCoverage(boolean motorFullCoverage) {
        this.motorFullCoverage = motorFullCoverage;
    }

    public boolean isInverterFullCoverage() {
        return inverterFullCoverage;
    }

    public void setInverterFullCoverage(boolean inverterFullCoverage) {
        this.inverterFullCoverage = inverterFullCoverage;
    }

    public boolean isBmsFullCoverage() {
        return bmsFullCoverage;
    }

    public void setBmsFullCoverage(boolean bmsFullCoverage) {
        this.bmsFullCoverage = bmsFullCoverage;
    }

    public boolean isChargerPartialCoverage() {
        return chargerPartialCoverage;
    }

    public void setChargerPartialCoverage(boolean chargerPartialCoverage) {
        this.chargerPartialCoverage = chargerPartialCoverage;
    }
}