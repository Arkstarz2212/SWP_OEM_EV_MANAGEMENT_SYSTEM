package org.example.models.dto.request;

import java.time.LocalDate;

public class VehicleRegisterRequest {
    private String vin;
    private Long oemId;
    private String model;
    private Integer modelYear;
    private Long customerId;
    private String variant;
    private String color;
    private String batteryCapacity;
    private Integer odometerKm;
    private LocalDate warrantyStartDate;
    private LocalDate warrantyEndDate;
    private Integer kmLimit;

    public VehicleRegisterRequest() {
    }

    public VehicleRegisterRequest(String vin, Long oemId, String model, Integer modelYear) {
        this.vin = vin;
        this.oemId = oemId;
        this.model = model;
        this.modelYear = modelYear;
    }

    // Getters and setters
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Long getOemId() {
        return oemId;
    }

    public void setOemId(Long oemId) {
        this.oemId = oemId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
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

    public Integer getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(Integer odometerKm) {
        this.odometerKm = odometerKm;
    }

    public LocalDate getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public void setWarrantyStartDate(LocalDate warrantyStartDate) {
        this.warrantyStartDate = warrantyStartDate;
    }

    public LocalDate getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public void setWarrantyEndDate(LocalDate warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public Integer getKmLimit() {
        return kmLimit;
    }

    public void setKmLimit(Integer kmLimit) {
        this.kmLimit = kmLimit;
    }
}