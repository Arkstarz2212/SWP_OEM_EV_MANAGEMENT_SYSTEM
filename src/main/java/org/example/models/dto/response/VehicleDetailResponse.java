package org.example.models.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class VehicleDetailResponse {
    private Long vehicleId;
    private String vin;
    private String model;
    private String variant;
    private Integer modelYear;
    private Integer currentOdometerKm;
    private LocalDate warrantyStartDate;
    private LocalDate warrantyEndDate;
    private String warrantyStatus;
    private String status; // Vehicle status for soft delete
    private String image; // Đường dẫn ảnh xe (vehicle image path)
    private CustomerBasicInfo customer;
    private List<ComponentInfo> components;
    private List<ServiceHistoryInfo> serviceHistory;
    private List<WarrantyClaimInfo> warrantyClaims;

    // Getters and Setters
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Integer getCurrentOdometerKm() {
        return currentOdometerKm;
    }

    public void setCurrentOdometerKm(Integer currentOdometerKm) {
        this.currentOdometerKm = currentOdometerKm;
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

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CustomerBasicInfo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasicInfo customer) {
        this.customer = customer;
    }

    public List<ComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentInfo> components) {
        this.components = components;
    }

    public List<ServiceHistoryInfo> getServiceHistory() {
        return serviceHistory;
    }

    public void setServiceHistory(List<ServiceHistoryInfo> serviceHistory) {
        this.serviceHistory = serviceHistory;
    }

    public List<WarrantyClaimInfo> getWarrantyClaims() {
        return warrantyClaims;
    }

    public void setWarrantyClaims(List<WarrantyClaimInfo> warrantyClaims) {
        this.warrantyClaims = warrantyClaims;
    }

    // Nested classes
    public static class CustomerBasicInfo {
        private String fullName;
        private String phoneNumber;
        private String email;
        private String address;
        private String city;
        private String province;
        private String postalCode;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
    }

    public static class ComponentInfo {
        private String componentName;
        private String serialNumber;
        private String partNumber;
        private LocalDate installDate;

        public String getComponentName() {
            return componentName;
        }

        public void setComponentName(String componentName) {
            this.componentName = componentName;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getPartNumber() {
            return partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        public LocalDate getInstallDate() {
            return installDate;
        }

        public void setInstallDate(LocalDate installDate) {
            this.installDate = installDate;
        }
    }

    public static class ServiceHistoryInfo {
        private LocalDate serviceDate;
        private String serviceType;
        private String serviceCenterName;
        private Integer odometerKm;
        private String description;

        public LocalDate getServiceDate() {
            return serviceDate;
        }

        public void setServiceDate(LocalDate serviceDate) {
            this.serviceDate = serviceDate;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getServiceCenterName() {
            return serviceCenterName;
        }

        public void setServiceCenterName(String serviceCenterName) {
            this.serviceCenterName = serviceCenterName;
        }

        public Integer getOdometerKm() {
            return odometerKm;
        }

        public void setOdometerKm(Integer odometerKm) {
            this.odometerKm = odometerKm;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class WarrantyClaimInfo {
        private String claimNumber;
        private LocalDateTime submitDate;
        private String status;
        private String issueDescription;

        public String getClaimNumber() {
            return claimNumber;
        }

        public void setClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
        }

        public LocalDateTime getSubmitDate() {
            return submitDate;
        }

        public void setSubmitDate(LocalDateTime submitDate) {
            this.submitDate = submitDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIssueDescription() {
            return issueDescription;
        }

        public void setIssueDescription(String issueDescription) {
            this.issueDescription = issueDescription;
        }
    }
}