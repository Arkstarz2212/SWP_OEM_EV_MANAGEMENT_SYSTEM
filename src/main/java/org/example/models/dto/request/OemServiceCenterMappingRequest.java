package org.example.models.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for mapping OEM manufacturers with Service Centers (Many-to-Many
 * relationship)
 * Maps to: aoem.oem_service_centers table
 */
public class OemServiceCenterMappingRequest {
    @NotNull(message = "OEM ID không được để trống")
    private Long oemId;

    @NotNull(message = "Danh sách Service Center ID không được để trống")
    private List<Long> serviceCenterIds;

    // Constructors
    public OemServiceCenterMappingRequest() {
    }

    public OemServiceCenterMappingRequest(Long oemId, List<Long> serviceCenterIds) {
        this.oemId = oemId;
        this.serviceCenterIds = serviceCenterIds;
    }

    // Getters and Setters
    public Long getOemId() {
        return oemId;
    }

    public void setOemId(Long oemId) {
        this.oemId = oemId;
    }

    public List<Long> getServiceCenterIds() {
        return serviceCenterIds;
    }

    public void setServiceCenterIds(List<Long> serviceCenterIds) {
        this.serviceCenterIds = serviceCenterIds;
    }
}