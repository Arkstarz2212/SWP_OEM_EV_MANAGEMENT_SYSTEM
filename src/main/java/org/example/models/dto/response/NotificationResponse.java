package org.example.models.dto.response;

import java.time.OffsetDateTime;

public class NotificationResponse {
    private Long id;
    private String serviceType;
    private Long vehicleId;
    private String vin;
    private String customerContact;
    private String channel;
    private String status;
    private OffsetDateTime sentAt;

    // Constructors
    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String serviceType, String vin, String channel, String status) {
        this.id = id;
        this.serviceType = serviceType;
        this.vin = vin;
        this.channel = channel;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNotificationId(Long notificationId) {
        this.id = notificationId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String toString() {
        return "NotificationResponse{" +
                "id=" + id +
                ", serviceType=" + serviceType +
                ", vehicleId=" + vehicleId +
                ", vin='" + vin + '\'' +
                ", customerContact='" + customerContact + '\'' +
                ", channel='" + channel + '\'' +
                ", status='" + status + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
