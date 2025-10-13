package org.example.models.core;

import java.time.OffsetDateTime;

public class ServiceRecord {
    private Long id; // aoem.service_records.id
    private Long vehicle_id; // aoem.service_records.vehicle_id
    private Long service_center_id; // aoem.service_records.service_center_id
    private Long claim_id; // aoem.service_records.claim_id
    private Long technician_id; // aoem.service_records.technician_id
    private String service_type; // aoem.service_records.service_type
    private String description; // aoem.service_records.description
    private OffsetDateTime performed_at;// aoem.service_records.performed_at
    private String result; // aoem.service_records.result
    private OffsetDateTime handover_at; // aoem.service_records.handover_at

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(Long vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public Long getService_center_id() {
        return service_center_id;
    }

    public void setService_center_id(Long service_center_id) {
        this.service_center_id = service_center_id;
    }

    public Long getClaim_id() {
        return claim_id;
    }

    public void setClaim_id(Long claim_id) {
        this.claim_id = claim_id;
    }

    public Long getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(Long technician_id) {
        this.technician_id = technician_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getPerformed_at() {
        return performed_at;
    }

    public void setPerformed_at(OffsetDateTime performed_at) {
        this.performed_at = performed_at;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public OffsetDateTime getHandover_at() {
        return handover_at;
    }

    public void setHandover_at(OffsetDateTime handover_at) {
        this.handover_at = handover_at;
    }
}
