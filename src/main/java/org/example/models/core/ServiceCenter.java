package org.example.models.core;

public class ServiceCenter {
    private Long id; // aoem.service_centers.id
    private String code; // aoem.service_centers.code
    private String name; // aoem.service_centers.name
    private String region; // aoem.service_centers.region
    private String contact_info; // aoem.service_centers.contact_info (JSON text)
    private Long oemId; // aoem.service_centers.oem_id
    private Boolean active; // aoem.service_centers.active
    private String status; // aoem.service_centers.status

    public ServiceCenter() {
    }

    public ServiceCenter(String code, String name, String region, String contact_info) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.contact_info = contact_info;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public Long getOemId() {
        return oemId;
    }

    public void setOemId(Long oemId) {
        this.oemId = oemId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
