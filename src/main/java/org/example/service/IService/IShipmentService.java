package org.example.service.IService;

import java.util.List;
import java.util.Map;

public interface IShipmentService {
    // Shipment Creation and Management
    Map<String, Object> createShipment(Long claimId, Long serviceCenterId, List<String> partNumbers, String priority,
            String notes);

    Map<String, Object> getShipment(Long shipmentId);

    boolean updateShipmentStatus(Long shipmentId, String status, String notes);

    // Tracking and Delivery
    boolean addTrackingInfo(Long shipmentId, String trackingNumber, String carrier, String estimatedDelivery);

    Map<String, Object> getShipmentByTracking(String trackingNumber);

    boolean markAsDelivered(Long shipmentId, String deliveredBy, String deliveryNotes);

    // Query Operations
    List<Map<String, Object>> getShipmentsByClaim(Long claimId);

    List<Map<String, Object>> getShipmentsByServiceCenter(Long serviceCenterId, String status);

    List<Map<String, Object>> getPendingShipments(Long serviceCenterId);

    // Status Management
    boolean updateShipmentPriority(Long shipmentId, String priority);

    boolean cancelShipment(Long shipmentId, String reason);

    // Analytics and Reporting
    Map<String, Object> getShipmentStatistics(Long serviceCenterId, String dateRange);

    List<Map<String, Object>> getDelayedShipments(Long serviceCenterId);

    Double getAverageDeliveryTime(Long serviceCenterId, String dateRange);
}
