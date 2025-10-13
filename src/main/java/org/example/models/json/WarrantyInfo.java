package org.example.models.json;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WarrantyInfo {
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("km_limit")
    private Integer kmLimit;

    @JsonProperty("policies")
    private List<String> policies;

    @JsonProperty("certificates")
    private List<String> certificates;

    @JsonProperty("coverage_rules")
    private String coverageRules;

    // Default constructor
    public WarrantyInfo() {
    }

    // Constructor with main fields
    public WarrantyInfo(LocalDate startDate, LocalDate endDate, Integer kmLimit) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.kmLimit = kmLimit;
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

    public String getCoverageRules() {
        return coverageRules;
    }

    public void setCoverageRules(String coverageRules) {
        this.coverageRules = coverageRules;
    }
}