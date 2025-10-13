package org.example.models.dto.nested;

import java.math.BigDecimal;

/**
 * Nested DTO for warranty_claims.cost_breakdown JSON field
 * Contains detailed cost information for warranty claims
 */
public class CostBreakdownRequest {
    private BigDecimal estimatedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal laborCost;
    private BigDecimal partsCost;

    // Coverage decisions
    private String coverageDecision; // "full", "partial", "none"
    private BigDecimal serviceCoverage; // Percentage covered by warranty for service
    private BigDecimal partsCoverage; // Percentage covered by warranty for parts

    // Payment breakdown
    private BigDecimal customerPayable;
    private BigDecimal oemPayable;
    private BigDecimal totalCost;

    // Additional costs
    private BigDecimal diagnosticCost;
    private BigDecimal shippingCost;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;

    // Cost justification
    private String costJustification;
    private String coverageReason;

    // Constructors
    public CostBreakdownRequest() {
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

    public BigDecimal getDiagnosticCost() {
        return diagnosticCost;
    }

    public void setDiagnosticCost(BigDecimal diagnosticCost) {
        this.diagnosticCost = diagnosticCost;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCostJustification() {
        return costJustification;
    }

    public void setCostJustification(String costJustification) {
        this.costJustification = costJustification;
    }

    public String getCoverageReason() {
        return coverageReason;
    }

    public void setCoverageReason(String coverageReason) {
        this.coverageReason = coverageReason;
    }
}