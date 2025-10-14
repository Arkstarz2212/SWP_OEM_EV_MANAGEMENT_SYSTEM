package org.example.models.json;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CostBreakdown {
    @JsonProperty("estimated_amount")
    private BigDecimal estimatedAmount;

    @JsonProperty("approved_amount")
    private BigDecimal approvedAmount;

    @JsonProperty("labor_cost")
    private BigDecimal laborCost;

    @JsonProperty("parts_cost")
    private BigDecimal partsCost;

    @JsonProperty("coverage_decision")
    private String coverageDecision;

    @JsonProperty("service_coverage")
    private BigDecimal serviceCoverage;

    @JsonProperty("parts_coverage")
    private BigDecimal partsCoverage;

    @JsonProperty("customer_payable")
    private BigDecimal customerPayable;

    @JsonProperty("oem_payable")
    private BigDecimal oemPayable;

    @JsonProperty("total_cost")
    private BigDecimal totalCost;

    // Default constructor
    public CostBreakdown() {
    }

    // Constructor with main fields
    public CostBreakdown(BigDecimal estimatedAmount, BigDecimal approvedAmount, BigDecimal laborCost,
            BigDecimal partsCost) {
        this.estimatedAmount = estimatedAmount;
        this.approvedAmount = approvedAmount;
        this.laborCost = laborCost;
        this.partsCost = partsCost;
    }

    // Getters and Setters
    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getPartsCost() {
        return partsCost;
    }

    public void setPartsCost(BigDecimal partsCost) {
        this.partsCost = partsCost;
    }

    public String getCoverageDecision() {
        return coverageDecision;
    }

    public void setCoverageDecision(String coverageDecision) {
        this.coverageDecision = coverageDecision;
    }

    public BigDecimal getServiceCoverage() {
        return serviceCoverage;
    }

    public void setServiceCoverage(BigDecimal serviceCoverage) {
        this.serviceCoverage = serviceCoverage;
    }

    public BigDecimal getPartsCoverage() {
        return partsCoverage;
    }

    public void setPartsCoverage(BigDecimal partsCoverage) {
        this.partsCoverage = partsCoverage;
    }

    public BigDecimal getCustomerPayable() {
        return customerPayable;
    }

    public void setCustomerPayable(BigDecimal customerPayable) {
        this.customerPayable = customerPayable;
    }

    public BigDecimal getOemPayable() {
        return oemPayable;
    }

    public void setOemPayable(BigDecimal oemPayable) {
        this.oemPayable = oemPayable;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}