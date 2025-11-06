package org.example.models.core;

public class PartCatalog {
    private Long id; // aoem.parts_catalog.id
    private Long oemId; // aoem.parts_catalog.oem_id
    private String partNumber; // aoem.parts_catalog.part_number
    private String name; // aoem.parts_catalog.name
    private String category; // aoem.parts_catalog.category (VARCHAR)
    private String part_data; // aoem.parts_catalog.part_data (JSON text)
    private String image; // aoem.parts_catalog.image (đường dẫn hình ảnh)

    public PartCatalog() {
    }

    public PartCatalog(Long oemId, String partNumber, String name, String category, String part_data) {
        this.oemId = oemId;
        this.partNumber = partNumber;
        this.name = name;
        this.category = category;
        this.part_data = part_data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPart_data() {
        return part_data;
    }

    public void setPart_data(String part_data) {
        this.part_data = part_data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
