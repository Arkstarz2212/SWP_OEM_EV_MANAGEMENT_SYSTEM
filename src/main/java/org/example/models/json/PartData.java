package org.example.models.json;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PartData {
    @JsonProperty("description")
    private String description;

    @JsonProperty("vehicle_models")
    private List<String> vehicleModels;

    @JsonProperty("unit_cost")
    private BigDecimal unitCost;

    @JsonProperty("manufacturer")
    private String manufacturer;

    // Default constructor
    public PartData() {
    }

    // Constructor with main fields
    public PartData(String description, BigDecimal unitCost, String manufacturer) {
        this.description = description;
        this.unitCost = unitCost;
        this.manufacturer = manufacturer;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getVehicleModels() {
        return vehicleModels;
    }

    public void setVehicleModels(List<String> vehicleModels) {
        this.vehicleModels = vehicleModels;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}