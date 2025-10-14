package org.example.models.json;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EligibilityRules {
    @JsonProperty("purchase_date_range")
    private PurchaseDateRange purchaseDateRange;

    @JsonProperty("model_criteria")
    private String modelCriteria;

    // Default constructor
    public EligibilityRules() {
    }

    public static class PurchaseDateRange {
        @JsonProperty("start_date")
        private LocalDate startDate;

        @JsonProperty("end_date")
        private LocalDate endDate;

        public PurchaseDateRange() {
        }

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
    }

    public PurchaseDateRange getPurchaseDateRange() {
        return purchaseDateRange;
    }

    public void setPurchaseDateRange(PurchaseDateRange purchaseDateRange) {
        this.purchaseDateRange = purchaseDateRange;
    }

    public String getModelCriteria() {
        return modelCriteria;
    }

    public void setModelCriteria(String modelCriteria) {
        this.modelCriteria = modelCriteria;
    }
}