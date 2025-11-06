package org.example.models.dto.request;

import org.example.models.dto.nested.CustomerInfoRequest;
import org.example.models.dto.nested.VehicleDataRequest;
import org.example.models.dto.nested.WarrantyInfoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateVehicleRequest {
    @NotBlank(message = "VIN không được để trống")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN phải có 17 ký tự và không chứa I, O, Q")
    private String vin;

    @NotNull(message = "OEM ID không được để trống")
    private Long oemId;

    @NotBlank(message = "Model xe không được để trống")
    private String model;

    @NotNull(message = "Năm sản xuất không được để trống")
    private Integer modelYear;

    // Customer information - no longer using customerId mapping
    private CustomerInfoRequest customerInfo;

    // Use nested DTOs for JSON fields
    private VehicleDataRequest vehicleData;
    private WarrantyInfoRequest warrantyInfo;

    private String image; // Đường dẫn ảnh xe (vehicle image path)

    // Constructors
    public CreateVehicleRequest() {
    }

    // Getters and Setters
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
