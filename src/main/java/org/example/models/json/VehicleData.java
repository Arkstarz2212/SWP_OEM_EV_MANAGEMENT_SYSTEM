package org.example.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleData {
    @JsonProperty("variant")
    private String variant;

    @JsonProperty("odometer_km")
    private Integer odometerKm;

    @JsonProperty("color")
    private String color;

    @JsonProperty("battery_capacity")
    private String batteryCapacity;

    // Default constructor
    public VehicleData() {
    }

    // Constructor with all fields
    public VehicleData(String variant, Integer odometerKm) {
        this.variant = variant;
        this.odometerKm = odometerKm;
    }

    // Getters and Setters
    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Integer getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(Integer odometerKm) {
        this.odometerKm = odometerKm;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
}