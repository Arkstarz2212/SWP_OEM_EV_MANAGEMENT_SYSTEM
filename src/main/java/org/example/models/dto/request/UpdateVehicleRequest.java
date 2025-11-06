package org.example.models.dto.request;

import org.example.models.dto.nested.CustomerInfoRequest;
import org.example.models.dto.nested.VehicleDataRequest;
import org.example.models.dto.nested.WarrantyInfoRequest;

public class UpdateVehicleRequest {
    private String model; // core: Vehicle.model
    private Integer modelYear; // core: Vehicle.modelYear

    // Customer information - no longer using customerId mapping
    private CustomerInfoRequest customerInfo;

    // JSON fields mapped via nested DTOs (serialized into core Vehicle.vehicle_data
    // / warranty_info)
    private VehicleDataRequest vehicleData;
    private WarrantyInfoRequest warrantyInfo;
    private String image; // vehicle image path

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

    public CustomerInfoRequest getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfoRequest customerInfo) {
        this.customerInfo = customerInfo;
    }

    public VehicleDataRequest getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(VehicleDataRequest vehicleData) {
        this.vehicleData = vehicleData;
    }

    public WarrantyInfoRequest getWarrantyInfo() {
        return warrantyInfo;
    }

    public void setWarrantyInfo(WarrantyInfoRequest warrantyInfo) {
        this.warrantyInfo = warrantyInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
