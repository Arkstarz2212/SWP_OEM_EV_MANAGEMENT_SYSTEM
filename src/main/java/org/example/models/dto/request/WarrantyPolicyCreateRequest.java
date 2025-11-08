package org.example.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WarrantyPolicyCreateRequest {
    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Component category is required")
    private String componentCategory;

    @NotNull(message = "Months coverage is required")
    @Min(value = 1, message = "Months coverage must be at least 1")
    private Integer monthsCoverage;

    @Min(value = 0, message = "Km coverage cannot be negative")
    private Integer kmCoverage;

    private String notes;

    // Constructors
    public WarrantyPolicyCreateRequest() {
    }

    public WarrantyPolicyCreateRequest(String model, String componentCategory, Integer monthsCoverage,
            Integer kmCoverage) {
        this.model = model;
        this.componentCategory = componentCategory;
        this.monthsCoverage = monthsCoverage;
        this.kmCoverage = kmCoverage;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "WarrantyPolicyCreateRequest{" +
                "model='" + model + '\'' +
                ", componentCategory='" + componentCategory + '\'' +
                ", monthsCoverage=" + monthsCoverage +
                ", kmCoverage=" + kmCoverage +
                ", notes='" + notes + '\'' +
                '}';
    }
}
