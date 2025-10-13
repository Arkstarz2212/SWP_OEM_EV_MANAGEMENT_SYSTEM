package org.example.models.dto.request;

public class VehicleSearchRequest {
    private String vin;
    private String customerName;
    private String phoneNumber;
    private String model;
    private String licensePlate;

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
}