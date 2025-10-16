package org.example.service.IService;

import java.util.List;

import org.example.models.dto.request.ServiceCenterCreateRequest;
import org.example.models.dto.response.DashboardResponse;
import org.example.models.dto.response.ServiceCenterResponse;

public interface IServiceCenterService {
    // Service Center Management
    ServiceCenterResponse createServiceCenter(ServiceCenterCreateRequest request);

    ServiceCenterResponse updateServiceCenter(Long serviceCenterId, ServiceCenterCreateRequest request);

    ServiceCenterResponse getServiceCenterById(Long serviceCenterId);

    ServiceCenterResponse getServiceCenterByCode(String code);

    boolean activateServiceCenter(Long serviceCenterId);

    boolean deactivateServiceCenter(Long serviceCenterId, String reason);

    boolean deleteServiceCenter(Long serviceCenterId);

    boolean updateServiceCenterStatus(Long serviceCenterId, boolean active);

    List<ServiceCenterResponse> getAllServiceCenters(int limit, int offset);

    // OEM-Service Center Relationships
    boolean addOemPartnership(Long serviceCenterId, Long oemId);

    boolean removeOemPartnership(Long serviceCenterId, Long oemId);

    List<ServiceCenterResponse> getServiceCentersByOem(Long oemId);

    List<Long> getOemPartnerships(Long serviceCenterId);

    boolean canServiceOemVehicles(Long serviceCenterId, Long oemId);

    // Geographic and Regional Operations
    List<ServiceCenterResponse> getServiceCentersByRegion(String region);

    List<ServiceCenterResponse> getServiceCentersByCity(String city);

    ServiceCenterResponse findNearestServiceCenter(String location, Long oemId);

    List<ServiceCenterResponse> findServiceCentersInRadius(String location, double radiusKm, Long oemId);

    // Capacity and Workload Management
    boolean updateServiceCapacity(Long serviceCenterId, Integer maxConcurrentVehicles, Integer maxTechnicians);

    Integer getCurrentWorkload(Long serviceCenterId);

    Integer getAvailableCapacity(Long serviceCenterId);

    List<ServiceCenterResponse> getServiceCentersWithCapacity(Long oemId);

    // Staff and Technician Management
    boolean addStaffMember(Long serviceCenterId, Long userId);

    boolean removeStaffMember(Long serviceCenterId, Long userId);

    List<Long> getServiceCenterStaff(Long serviceCenterId);

    List<Long> getServiceCenterTechnicians(Long serviceCenterId);

    Integer getAvailableTechnicianCount(Long serviceCenterId);

    // Recall Management
    boolean recordRecallCompletion(Long serviceCenterId, String recallDetails, String vin, String notes);

    // Warranty Claim Processing
    List<Long> getPendingWarrantyClaims(Long serviceCenterId);

    List<Long> getCompletedWarrantyClaims(Long serviceCenterId, String dateRange);

    boolean updateClaimProcessingCapability(Long serviceCenterId, List<String> supportedClaimTypes);

    // Inventory and Parts Management
    boolean updatePartsInventory(Long serviceCenterId, String partNumber, Integer quantity);

    Integer getPartsInventory(Long serviceCenterId, String partNumber);

    List<String> getLowStockParts(Long serviceCenterId);

    boolean requestPartsFromOem(Long serviceCenterId, Long oemId, List<String> partNumbers);

    // Performance and Analytics
    DashboardResponse getServiceCenterDashboard(Long serviceCenterId);

    Double getAverageRepairTime(Long serviceCenterId);

    Double getCustomerSatisfactionRating(Long serviceCenterId);

    Integer getMonthlyClaimCount(Long serviceCenterId);

    // Search and Discovery
    List<ServiceCenterResponse> searchServiceCenters(String keyword, String region, Long oemId);

    List<ServiceCenterResponse> getActiveServiceCenters(Long oemId);

    List<ServiceCenterResponse> getServiceCentersByCapability(String capability, Long oemId);

    // Quality and Compliance
    boolean updateCertifications(Long serviceCenterId, List<String> certifications);

    List<String> getServiceCenterCertifications(Long serviceCenterId);

    boolean recordQualityAudit(Long serviceCenterId, String auditResult, String notes);

    List<String> getQualityAuditHistory(Long serviceCenterId);

    // Communication and Notifications
    boolean sendNotificationToServiceCenter(Long serviceCenterId, String message, String type);

    boolean updateContactInformation(Long serviceCenterId, String email, String phone, String address);

    // Integration and External Systems
    boolean syncWithExternalSystem(Long serviceCenterId, String externalSystemId);

    ServiceCenterResponse getServiceCenterByExternalId(String externalId);
}
