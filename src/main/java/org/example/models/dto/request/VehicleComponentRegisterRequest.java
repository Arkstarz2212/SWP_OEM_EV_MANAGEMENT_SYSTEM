package org.example.models.dto.request;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleComponentRegisterRequest {

    @JsonProperty("components")
    private List<ComponentInfo> components;

    @JsonProperty("vin")
    private String vin;

    @JsonProperty("registered_by")
    private Long registeredBy;

    @JsonProperty("registration_notes")
    private String registrationNotes;

    // Default constructor
    public VehicleComponentRegisterRequest() {
    }

    // Constructor with main fields
    public VehicleComponentRegisterRequest(String vin, List<ComponentInfo> components) {
        this.vin = vin;
        this.components = components;
    }

    // Getters and Setters
    public List<ComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentInfo> components) {
        this.components = components;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Long getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(Long registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getRegistrationNotes() {
        return registrationNotes;
    }

    public void setRegistrationNotes(String registrationNotes) {
        this.registrationNotes = registrationNotes;
    }

    // Inner class for component information
    public static class ComponentInfo {

        @JsonProperty("serial_number")
        private String serialNumber;

        @JsonProperty("component_name")
        private String componentName;

        @JsonProperty("part_number")
        private String partNumber;

        @JsonProperty("manufacturer")
        private String manufacturer;

        @JsonProperty("model")
        private String model;

        @JsonProperty("installation_date")
        private OffsetDateTime installationDate;

        @JsonProperty("warranty_start_date")
        private OffsetDateTime warrantyStartDate;

        @JsonProperty("warranty_end_date")
        private OffsetDateTime warrantyEndDate;

        @JsonProperty("installation_notes")
        private String installationNotes;

        @JsonProperty("is_critical_component")
        private Boolean isCriticalComponent;

        // Default constructor
        public ComponentInfo() {
        }

        // Constructor with main fields
        public ComponentInfo(String serialNumber, String componentName, String partNumber) {
            this.serialNumber = serialNumber;
            this.componentName = componentName;
            this.partNumber = partNumber;
        }

        // Getters and Setters
        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getComponentName() {
            return componentName;
        }

        public void setComponentName(String componentName) {
            this.componentName = componentName;
        }

        public String getPartNumber() {
            return partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public OffsetDateTime getInstallationDate() {
            return installationDate;
        }

        public void setInstallationDate(OffsetDateTime installationDate) {
            this.installationDate = installationDate;
        }

        public OffsetDateTime getWarrantyStartDate() {
            return warrantyStartDate;
        }

        public void setWarrantyStartDate(OffsetDateTime warrantyStartDate) {
            this.warrantyStartDate = warrantyStartDate;
        }

        public OffsetDateTime getWarrantyEndDate() {
            return warrantyEndDate;
        }

        public void setWarrantyEndDate(OffsetDateTime warrantyEndDate) {
            this.warrantyEndDate = warrantyEndDate;
        }

        public String getInstallationNotes() {
            return installationNotes;
        }

        public void setInstallationNotes(String installationNotes) {
            this.installationNotes = installationNotes;
        }

        public Boolean getIsCriticalComponent() {
            return isCriticalComponent;
        }

        public void setIsCriticalComponent(Boolean isCriticalComponent) {
            this.isCriticalComponent = isCriticalComponent;
        }
    }
}
