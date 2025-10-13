package org.example.models.core;

public class OemServiceCenter {
    private Long id; // aoem.oem_service_centers.id
    private Long oem_id; // aoem.oem_service_centers.oem_id
    private Long service_center_id;// aoem.oem_service_centers.service_center_id

    public OemServiceCenter() {
    }

    public OemServiceCenter(Long oem_id, Long service_center_id) {
        this.oem_id = oem_id;
        this.service_center_id = service_center_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOem_id() {
        return oem_id;
    }

    public void setOem_id(Long oem_id) {
        this.oem_id = oem_id;
    }

    public Long getService_center_id() {
        return service_center_id;
    }

    public void setService_center_id(Long service_center_id) {
        this.service_center_id = service_center_id;
    }
}