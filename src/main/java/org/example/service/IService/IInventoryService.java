package org.example.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IInventoryService {
        // Parts Inventory Management
        Map<String, Object> addPartToInventory(Long locationId, String partNumber, Integer quantity, Double unitCost);

        boolean updatePartQuantity(Long locationId, String partNumber, Integer newQuantity);

        Integer getPartQuantity(Long locationId, String partNumber);

        Map<String, Object> getPartInventoryDetails(Long locationId, String partNumber);

        // Stock Level Management
        List<Map<String, Object>> getLowStockParts(Long locationId, Integer thresholdQuantity);

        List<Map<String, Object>> getOutOfStockParts(Long locationId);

        boolean setStockThreshold(Long locationId, String partNumber, Integer minQuantity, Integer maxQuantity);

        List<Map<String, Object>> getPartsNeedingReorder(Long locationId);

        // OEM-to-ServiceCenter Distribution
        boolean transferPartsToServiceCenter(Long oemInventoryId, Long serviceCenterId,
                        String partNumber, Integer quantity, String transferReason);

        Map<String, Object> createPartTransferRequest(Long serviceCenterId, Long oemId,
                        String partNumber, Integer requestedQuantity, String urgency);

        boolean approveTransferRequest(Long transferRequestId, Long approvedBy);

        boolean fulfillTransferRequest(Long transferRequestId, String trackingNumber);

        // Supply Chain Management
        Map<String, Object> createSupplyOrder(Long oemId, String partNumber, Integer quantity, String supplierId);

        boolean receiveSupplyOrder(Long orderId, Integer receivedQuantity, LocalDate receivedDate);

        List<Map<String, Object>> getPendingSupplyOrders(Long oemId);

        Double calculateReorderPoint(String partNumber, Long locationId);

        // Warranty Parts Allocation
        boolean reservePartsForClaim(Long claimId, String partNumber, Integer quantity);

        boolean releaseReservedParts(Long claimId, String partNumber);

        List<Map<String, Object>> getReservedParts(Long locationId);

        boolean allocatePartsForRecall(String recallDetails, String partNumber, Integer estimatedNeed);

        // Cost & Financial Management
        Double getPartCost(String partNumber, Long locationId);

        Double getInventoryValue(Long locationId);

        Map<String, Object> getInventoryCostAnalysis(Long locationId, LocalDate fromDate, LocalDate toDate);

        boolean updatePartCost(String partNumber, Double newCost, String reason);

        // Location & Warehouse Management
        Map<String, Object> createInventoryLocation(String locationName, String locationType, Long organizationId);

        List<Map<String, Object>> getInventoryLocationsByOrganization(Long organizationId);

        boolean updateLocationDetails(Long locationId, Map<String, Object> locationData);

        // Reporting & Analytics
        Map<String, Object> getInventoryDashboard(Long locationId);

        List<Map<String, Object>> getInventoryTurnoverReport(Long locationId, LocalDate fromDate, LocalDate toDate);

        List<Map<String, Object>> getSlowMovingParts(Long locationId, Integer daysSinceLastMovement);

        Map<String, Object> getPartUsageStatistics(String partNumber, Long locationId);

        // Search & Discovery
        List<Map<String, Object>> searchPartsInInventory(String searchQuery, Long locationId);

        List<Map<String, Object>> findPartAvailability(String partNumber, List<Long> locationIds);
}