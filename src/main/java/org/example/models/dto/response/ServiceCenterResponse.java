package org.example.models.dto.response;

import java.util.Map;

public class ServiceCenterResponse {
    private Long id; // core: ServiceCenter.id
    private String code; // core: ServiceCenter.code
    private String name; // core: ServiceCenter.name
    private String region; // core: ServiceCenter.region
    private Map<String, Object> contactInfo; // parsed from core: ServiceCenter.contact_info (JSON)

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

    public Map<String, Object> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Map<String, Object> contactInfo) {
        this.contactInfo = contactInfo;
    }
}