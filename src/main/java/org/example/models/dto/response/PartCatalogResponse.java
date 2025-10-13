package org.example.models.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.example.models.enums.PartCategory;

public class PartCatalogResponse {
    private Long id;
    private String partNumber;
    private String name;
    private PartCategory category;
    private Boolean isActive;
    private OffsetDateTime createdAt;

    // OEM info
    private Long oemId;
    private String oemName;

    // Part data
    private String description;
    private List<String> vehicleModels;
    private BigDecimal unitCost;
    private String manufacturer;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public PartCategory getCategory() { return category; }
    public void setCategory(PartCategory category) { this.category = category; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public Long getOemId() { return oemId; }
    public void setOemId(Long oemId) { this.oemId = oemId; }
    public String getOemName() { return oemName; }
    public void setOemName(String oemName) { this.oemName = oemName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getVehicleModels() { return vehicleModels; }
    public void setVehicleModels(List<String> vehicleModels) { this.vehicleModels = vehicleModels; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
}
