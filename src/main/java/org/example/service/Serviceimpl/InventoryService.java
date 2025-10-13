package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.service.IService.IInventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryService implements IInventoryService {

    private final Map<String, Integer> quantities = new HashMap<>();
    private final Map<String, Double> costs = new HashMap<>();

    private String key(Long locationId, String partNumber) {
        return locationId + ":" + partNumber;
    }

    @Override
    public Map<String, Object> addPartToInventory(Long locationId, String partNumber, Integer quantity,
            Double unitCost) {
        String k = key(locationId, partNumber);
        int existing = quantities.getOrDefault(k, 0);
        quantities.put(k, existing + (quantity != null ? quantity : 0));
        if (unitCost != null)
            costs.put(k, unitCost);
        Map<String, Object> res = new HashMap<>();
        res.put("locationId", locationId);
        res.put("partNumber", partNumber);
        res.put("quantity", quantities.get(k));
        res.put("unitCost", costs.get(k));
        return res;
    }

    @Override
    public boolean updatePartQuantity(Long locationId, String partNumber, Integer newQuantity) {
        String k = key(locationId, partNumber);
        if (newQuantity == null || newQuantity < 0)
            return false;
        quantities.put(k, newQuantity);
        return true;
    }

    @Override
    public Integer getPartQuantity(Long locationId, String partNumber) {
        return quantities.getOrDefault(key(locationId, partNumber), 0);
    }

    @Override
    public Map<String, Object> getPartInventoryDetails(Long locationId, String partNumber) {
        String k = key(locationId, partNumber);
        Map<String, Object> res = new HashMap<>();
        res.put("locationId", locationId);
        res.put("partNumber", partNumber);
        res.put("quantity", quantities.getOrDefault(k, 0));
        res.put("unitCost", costs.get(k));
        return res;
    }

    @Override
    public List<Map<String, Object>> getLowStockParts(Long locationId, Integer thresholdQuantity) {
        List<Map<String, Object>> list = new ArrayList<>();
        int threshold = thresholdQuantity != null ? thresholdQuantity : 5;
        for (Map.Entry<String, Integer> e : quantities.entrySet()) {
            String[] parts = e.getKey().split(":", 2);
            if (String.valueOf(locationId).equals(parts[0]) && e.getValue() <= threshold) {
                list.add(Map.of("partNumber", parts[1], "quantity", e.getValue()));
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getOutOfStockParts(Long locationId) {
        return getLowStockParts(locationId, 0);
    }

    @Override
    public boolean setStockThreshold(Long locationId, String partNumber, Integer minQuantity, Integer maxQuantity) {
        return true;
    }

    @Override
    public List<Map<String, Object>> getPartsNeedingReorder(Long locationId) {
        return getLowStockParts(locationId, 3);
    }

    @Override
    public boolean transferPartsToServiceCenter(Long oemInventoryId, Long serviceCenterId, String partNumber,
            Integer quantity, String transferReason) {
        Integer srcQty = getPartQuantity(oemInventoryId, partNumber);
        if (srcQty < (quantity != null ? quantity : 0))
            return false;
        updatePartQuantity(oemInventoryId, partNumber, srcQty - quantity);
        Integer dstQty = getPartQuantity(serviceCenterId, partNumber);
        updatePartQuantity(serviceCenterId, partNumber, dstQty + quantity);
        return true;
    }

    @Override
    public Map<String, Object> createPartTransferRequest(Long serviceCenterId, Long oemId, String partNumber,
            Integer requestedQuantity, String urgency) {
        return Map.of("requestId", System.nanoTime(), "serviceCenterId", serviceCenterId, "oemId", oemId, "partNumber",
                partNumber, "quantity", requestedQuantity, "urgency", urgency);
    }

    @Override
    public boolean approveTransferRequest(Long transferRequestId, Long approvedBy) {
        return true;
    }

    @Override
    public boolean fulfillTransferRequest(Long transferRequestId, String trackingNumber) {
        return true;
    }

    @Override
    public Map<String, Object> createSupplyOrder(Long oemId, String partNumber, Integer quantity, String supplierId) {
        return Map.of("orderId", System.nanoTime(), "oemId", oemId, "partNumber", partNumber, "quantity", quantity,
                "supplierId", supplierId);
    }

    @Override
    public boolean receiveSupplyOrder(Long orderId, Integer receivedQuantity, LocalDate receivedDate) {
        return true;
    }

    @Override
    public List<Map<String, Object>> getPendingSupplyOrders(Long oemId) {
        return List.of();
    }

    @Override
    public Double calculateReorderPoint(String partNumber, Long locationId) {
        return 0.0;
    }

    @Override
    public boolean reservePartsForClaim(Long claimId, String partNumber, Integer quantity) {
        return true;
    }

    @Override
    public boolean releaseReservedParts(Long claimId, String partNumber) {
        return true;
    }

    @Override
    public List<Map<String, Object>> getReservedParts(Long locationId) {
        return List.of();
    }

    @Override
    public boolean allocatePartsForRecall(String recallDetails, String partNumber, Integer estimatedNeed) {
        return true;
    }

    @Override
    public Double getPartCost(String partNumber, Long locationId) {
        return costs.getOrDefault(key(locationId, partNumber), 0.0);
    }

    @Override
    public Double getInventoryValue(Long locationId) {
        double sum = 0.0;
        for (Map.Entry<String, Integer> e : quantities.entrySet()) {
            String[] parts = e.getKey().split(":", 2);
            if (String.valueOf(locationId).equals(parts[0])) {
                Double cost = costs.getOrDefault(e.getKey(), 0.0);
                sum += cost * e.getValue();
            }
        }
        return sum;
    }

    @Override
    public Map<String, Object> getInventoryCostAnalysis(Long locationId, LocalDate fromDate, LocalDate toDate) {
        return Map.of("locationId", locationId, "from", fromDate, "to", toDate, "value", getInventoryValue(locationId));
    }

    @Override
    public boolean updatePartCost(String partNumber, Double newCost, String reason) {
        // Update all locations for simplicity
        costs.replaceAll((k, v) -> k.endsWith(":" + partNumber) ? newCost : v);
        return true;
    }

    @Override
    public Map<String, Object> createInventoryLocation(String locationName, String locationType, Long organizationId) {
        return Map.of("locationId", System.nanoTime(), "name", locationName, "type", locationType, "orgId",
                organizationId);
    }

    @Override
    public List<Map<String, Object>> getInventoryLocationsByOrganization(Long organizationId) {
        return List.of();
    }

    @Override
    public boolean updateLocationDetails(Long locationId, Map<String, Object> locationData) {
        return true;
    }

    @Override
    public Map<String, Object> getInventoryDashboard(Long locationId) {
        return Map.of("locationId", locationId, "totalValue", getInventoryValue(locationId));
    }

    @Override
    public List<Map<String, Object>> getInventoryTurnoverReport(Long locationId, LocalDate fromDate, LocalDate toDate) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> getSlowMovingParts(Long locationId, Integer daysSinceLastMovement) {
        return List.of();
    }

    @Override
    public Map<String, Object> getPartUsageStatistics(String partNumber, Long locationId) {
        return Map.of("partNumber", partNumber, "locationId", locationId, "usage", 0);
    }

    @Override
    public List<Map<String, Object>> searchPartsInInventory(String searchQuery, Long locationId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (String k : quantities.keySet()) {
            String[] parts = k.split(":", 2);
            if (String.valueOf(locationId).equals(parts[0])
                    && (searchQuery == null || parts[1].contains(searchQuery))) {
                list.add(Map.of("partNumber", parts[1], "quantity", quantities.get(k)));
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findPartAvailability(String partNumber, List<Long> locationIds) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Long loc : locationIds) {
            list.add(Map.of("locationId", loc, "quantity", getPartQuantity(loc, partNumber)));
        }
        return list;
    }
}
