package org.example.models.dto.request;

import java.math.BigDecimal;
import java.util.List;

import org.example.models.enums.PartCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PartCatalogCreateRequest {
    @NotNull(message = "OEM ID không được để trống")
    private Long oemId;

    @NotBlank(message = "Part number không được để trống")
    private String partNumber;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotNull(message = "Category không được để trống")
    private PartCategory category;

    private String description;
    private List<String> vehicleModels;
    private BigDecimal unitCost;
    private String manufacturer;
    private String image; // Đường dẫn ảnh phụ tùng (part image path)

    public Long getOemId() {
        return oemId;
    }

    public void setOemId(Long oemId) {
        this.oemId = oemId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PartCategory getCategory() {
        return category;
    }

    public void setCategory(PartCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getVehicleModels() {
        return vehicleModels;
    }

    public void setVehicleModels(List<String> vehicleModels) {
        this.vehicleModels = vehicleModels;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
