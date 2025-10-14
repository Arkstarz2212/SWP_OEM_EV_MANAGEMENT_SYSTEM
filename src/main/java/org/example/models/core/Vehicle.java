package org.example.models.core;

import java.time.OffsetDateTime;

import org.example.models.json.VehicleData;
import org.example.models.json.WarrantyInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Vehicle {
    private Long id; // aoem.vehicles.id
    private String vin; // aoem.vehicles.vin
    private Long oemId; // aoem.vehicles.oem_id
    private String model; // aoem.vehicles.model
    private Integer modelYear; // aoem.vehicles.model_year
    private Long customerId; // aoem.vehicles.customer_id
    private String vehicle_data; // aoem.vehicles.vehicle_data (JSON text)
    private String warranty_info;// aoem.vehicles.warranty_info (JSON text)

    // Backward-compatible transient helpers
    private transient VehicleData vehicleDataCache;
    private transient WarrantyInfo warrantyInfoCache;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private transient OffsetDateTime createdAt;

    public Vehicle() {
    }

    public Vehicle(String vin, Long oemId, String model, Integer modelYear, String vehicle_data, String warranty_info) {
        this.vin = vin;
        this.oemId = oemId;
        this.model = model;
        this.modelYear = modelYear;
        this.vehicle_data = vehicle_data;
        this.warranty_info = warranty_info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getVehicle_data() {
        return vehicle_data;
    }

    public void setVehicle_data(String vehicle_data) {
        this.vehicle_data = vehicle_data;
    }

    public String getWarranty_info() {
        return warranty_info;
    }

    public void setWarranty_info(String warranty_info) {
        this.warranty_info = warranty_info;
    }

    // Backward-compatible helpers used across services
    public VehicleData getVehicleData() {
        if (vehicleDataCache == null && vehicle_data != null) {
            try {
                vehicleDataCache = objectMapper.readValue(vehicle_data, VehicleData.class);
            } catch (Exception ignored) {
            }
        }
        return vehicleDataCache;
    }

    public void setVehicleData(VehicleData vehicleData) {
        this.vehicleDataCache = vehicleData;
        if (vehicleData != null) {
            try {
                this.vehicle_data = objectMapper.writeValueAsString(vehicleData);
            } catch (Exception ignored) {
                this.vehicle_data = "{}";
            }
        } else {
            this.vehicle_data = null;
        }
    }

    public WarrantyInfo getWarrantyInfo() {
        if (warrantyInfoCache == null && warranty_info != null) {
            try {
                warrantyInfoCache = objectMapper.readValue(warranty_info, WarrantyInfo.class);
            } catch (Exception ignored) {
            }
        }
        return warrantyInfoCache;
    }

    public void setWarrantyInfo(WarrantyInfo warrantyInfo) {
        this.warrantyInfoCache = warrantyInfo;
        if (warrantyInfo != null) {
            try {
                this.warranty_info = objectMapper.writeValueAsString(warrantyInfo);
            } catch (Exception ignored) {
                this.warranty_info = "{}";
            }
        } else {
            this.warranty_info = null;
        }
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
