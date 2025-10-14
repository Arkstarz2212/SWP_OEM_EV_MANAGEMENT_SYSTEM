package org.example.models.json;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountInfo {
    @JsonProperty("discount_type")
    private String discountType; // "percentage", "fixed_amount"

    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    // Default constructor
    public DiscountInfo() {
    }

    // Constructor with all fields
    public DiscountInfo(String discountType, BigDecimal discountValue) {
        this.discountType = discountType;
        this.discountValue = discountValue;
    }

    // Getters and Setters
    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
}