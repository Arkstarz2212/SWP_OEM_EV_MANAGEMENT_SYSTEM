package org.example.service.Serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.example.service.IService.IShipmentService;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService implements IShipmentService {

    private final AtomicLong shipmentIdGenerator = new AtomicLong(1);
    private final Map<Long, Map<String, Object>> shipments = new HashMap<>();

    @Override
    public Map<String, Object> createShipment(Long claimId, Long serviceCenterId, List<String> partNumbers,
            String priority, String notes) {
        Long shipmentId = shipmentIdGenerator.getAndIncrement();

        Map<String, Object> shipment = new HashMap<>();
        shipment.put("shipmentId", shipmentId);
        shipment.put("claimId", claimId);
        shipment.put("serviceCenterId", serviceCenterId);
        shipment.put("partNumbers", partNumbers);
        shipment.put("priority", priority);
        shipment.put("notes", notes);
        shipment.put("status", "pending");
        shipment.put("createdAt", java.time.LocalDateTime.now().toString());

        shipments.put(shipmentId, shipment);
        return shipment;
    }

    @Override
    public Map<String, Object> getShipment(Long shipmentId) {
        return shipments.get(shipmentId);
    }

    @Override
    public boolean updateShipmentStatus(Long shipmentId, String status, String notes) {
        Map<String, Object> shipment = shipments.get(shipmentId);
        if (shipment != null) {
            shipment.put("status", status);
            if (notes != null) {
                shipment.put("statusNotes", notes);
            }
            shipment.put("updatedAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean addTrackingInfo(Long shipmentId, String trackingNumber, String carrier, String estimatedDelivery) {
        Map<String, Object> shipment = shipments.get(shipmentId);
        if (shipment != null) {
            shipment.put("trackingNumber", trackingNumber);
            shipment.put("carrier", carrier);
            shipment.put("estimatedDelivery", estimatedDelivery);
            shipment.put("updatedAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getShipmentByTracking(String trackingNumber) {
        return shipments.values().stream()
                .filter(shipment -> trackingNumber.equals(shipment.get("trackingNumber")))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean markAsDelivered(Long shipmentId, String deliveredBy, String deliveryNotes) {
        Map<String, Object> shipment = shipments.get(shipmentId);
        if (shipment != null) {
            shipment.put("status", "delivered");
            shipment.put("deliveredBy", deliveredBy);
            shipment.put("deliveryNotes", deliveryNotes);
            shipment.put("deliveredAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getShipmentsByClaim(Long claimId) {
        return shipments.values().stream()
                .filter(shipment -> claimId.equals(shipment.get("claimId")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Map<String, Object>> getShipmentsByServiceCenter(Long serviceCenterId, String status) {
        return shipments.values().stream()
                .filter(shipment -> serviceCenterId.equals(shipment.get("serviceCenterId")))
                .filter(shipment -> status == null || status.equals(shipment.get("status")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Map<String, Object>> getPendingShipments(Long serviceCenterId) {
        return getShipmentsByServiceCenter(serviceCenterId, "pending");
    }

    @Override
    public boolean updateShipmentPriority(Long shipmentId, String priority) {
        Map<String, Object> shipment = shipments.get(shipmentId);
        if (shipment != null) {
            shipment.put("priority", priority);
            shipment.put("updatedAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelShipment(Long shipmentId, String reason) {
        Map<String, Object> shipment = shipments.get(shipmentId);
        if (shipment != null) {
            shipment.put("status", "cancelled");
            shipment.put("cancellationReason", reason);
            shipment.put("cancelledAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getShipmentStatistics(Long serviceCenterId, String dateRange) {
        Map<String, Object> stats = new HashMap<>();
        List<Map<String, Object>> serviceCenterShipments = getShipmentsByServiceCenter(serviceCenterId, null);

        stats.put("totalShipments", serviceCenterShipments.size());
        stats.put("pendingShipments",
                serviceCenterShipments.stream().filter(s -> "pending".equals(s.get("status"))).count());
        stats.put("deliveredShipments",
                serviceCenterShipments.stream().filter(s -> "delivered".equals(s.get("status"))).count());
        stats.put("cancelledShipments",
                serviceCenterShipments.stream().filter(s -> "cancelled".equals(s.get("status"))).count());

        return stats;
    }

    @Override
    public List<Map<String, Object>> getDelayedShipments(Long serviceCenterId) {
        return shipments.values().stream()
                .filter(shipment -> serviceCenterId.equals(shipment.get("serviceCenterId")))
                .filter(shipment -> "shipped".equals(shipment.get("status")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public Double getAverageDeliveryTime(Long serviceCenterId, String dateRange) {
        // Placeholder implementation
        return 2.5; // Average delivery time in days
    }
}
