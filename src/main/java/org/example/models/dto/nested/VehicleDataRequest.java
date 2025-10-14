package org.example.models.dto.nested;

import java.time.LocalDate;

/**
 * Nested DTO for vehicles.vehicle_data JSON field
 * Contains additional vehicle information
 */
public class VehicleDataRequest {
    private String variant;
    private Integer odometerKm;
    private String color;
    private String batteryCapacity;
    private String motorType;
    private LocalDate deliveryDate;
    private LocalDate purchaseDate;
    private String dealerCode;
    private String customerType; // "individual", "corporate"

    // EV specific data
    private String chargingType; // "AC", "DC", "BOTH"
    private Integer maxChargingPower; // kW
    private Integer drivingRange; // km
    private String driveTrain; // "FWD", "RWD", "AWD"

    // Constructors
    public VehicleDataRequest() {
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

    public String getMotorType() {
        return motorType;
    }

    public void setMotorType(String motorType) {
        this.motorType = motorType;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getChargingType() {
        return chargingType;
    }

    public void setChargingType(String chargingType) {
        this.chargingType = chargingType;
    }

    public Integer getMaxChargingPower() {
        return maxChargingPower;
    }

    public void setMaxChargingPower(Integer maxChargingPower) {
        this.maxChargingPower = maxChargingPower;
    }

    public Integer getDrivingRange() {
        return drivingRange;
    }

    public void setDrivingRange(Integer drivingRange) {
        this.drivingRange = drivingRange;
    }

    public String getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }
}