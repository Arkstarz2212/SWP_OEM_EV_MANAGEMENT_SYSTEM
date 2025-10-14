package org.example.models.dto.response;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class VehicleResponse {
    private Long id;
    private String vin;
    private String model;
    private Integer modelYear;
    private Long customerId;
    private String customerName;
    private OffsetDateTime createdAt;

    // OEM info
    private Long oemId;
    private String oemName;

    // Vehicle data
    private String variant;
    private Integer odometerKm;

    // Warranty info
    private LocalDate warrantyStartDate;
    private LocalDate warrantyEndDate;
    private Integer warrantyKmLimit;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public Long getOemId() { return oemId; }
    public void setOemId(Long oemId) { this.oemId = oemId; }
    public String getOemName() { return oemName; }
    public void setOemName(String oemName) { this.oemName = oemName; }
    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }
    public Integer getOdometerKm() { return odometerKm; }
    public void setOdometerKm(Integer odometerKm) { this.odometerKm = odometerKm; }
    public LocalDate getWarrantyStartDate() { return warrantyStartDate; }
    public void setWarrantyStartDate(LocalDate warrantyStartDate) { this.warrantyStartDate = warrantyStartDate; }
    public LocalDate getWarrantyEndDate() { return warrantyEndDate; }
    public void setWarrantyEndDate(LocalDate warrantyEndDate) { this.warrantyEndDate = warrantyEndDate; }
    public Integer getWarrantyKmLimit() { return warrantyKmLimit; }
    public void setWarrantyKmLimit(Integer warrantyKmLimit) { this.warrantyKmLimit = warrantyKmLimit; }
}
