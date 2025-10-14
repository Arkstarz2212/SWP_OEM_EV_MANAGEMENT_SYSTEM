package org.example.models.core;

public class OemManufacturer {
    private Long id; // aoem.oem_manufacturers.id
    private String code; // aoem.oem_manufacturers.code
    private String name; // aoem.oem_manufacturers.name
    private String contact; // aoem.oem_manufacturers.contact (JSON text)

    public OemManufacturer() {
    }

    public OemManufacturer(String code, String name, String contact) {
        this.code = code;
        this.name = name;
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}