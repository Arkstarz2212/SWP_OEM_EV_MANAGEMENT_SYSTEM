package org.example.models.dto.response;

import java.time.OffsetDateTime;

public class WarrantyPolicyResponse {
    private Long id;
    private String model;
    private String componentCategory;
    private Integer monthsCoverage;
    private Integer kmCoverage;
    private String notes;
    private OffsetDateTime createdAt;

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
    public String toString() {
        return "WarrantyPolicyResponse{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", componentCategory='" + componentCategory + '\'' +
                ", monthsCoverage=" + monthsCoverage +
                ", kmCoverage=" + kmCoverage +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
