package org.example.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IWarrantyCertificateService {
    // Certificate Lifecycle Management
    Map<String, Object> createWarrantyCertificate(String vin, Long policyId, LocalDate startDate);

    Map<String, Object> getCertificateByVin(String vin);

    Map<String, Object> getCertificateById(Long certificateId);

    boolean activateCertificate(Long certificateId);

    boolean deactivateCertificate(Long certificateId, String reason);

    // Certificate Validation
    boolean validateCertificate(String vin, String componentName);

    boolean isCertificateValid(Long certificateId);

    LocalDate getCertificateExpiryDate(String vin, String componentName);

    Integer getRemainingWarrantyDays(String vin, String componentName);

    // Expiry Management & Notifications
    List<Map<String, Object>> getCertificatesExpiringInDays(int daysFromNow);

    List<Map<String, Object>> getExpiredCertificates();

    boolean sendExpiryNotification(Long certificateId);

    boolean scheduleExpiryReminders(Long certificateId);

    // Certificate Extensions & Renewals
    boolean extendCertificate(Long certificateId, Integer additionalMonths, String reason);

    Map<String, Object> renewCertificate(Long certificateId, Long newPolicyId);

    boolean offerExtension(String vin, Integer extensionMonths, Double cost);

    // Certificate Usage & Claims
    boolean recordCertificateUsage(Long certificateId, Long claimId, String usageType);

    List<Map<String, Object>> getCertificateUsageHistory(Long certificateId);

    boolean canUseCertificateForClaim(Long certificateId, String componentName, Double claimAmount);

    // Customer Certificate Management
    List<Map<String, Object>> getCustomerCertificates(Long customerId);

    Map<String, Object> getActiveCertificateForVehicle(String vin);

    // Reporting & Analytics
    List<Map<String, Object>> getCertificatesByOem(Long oemId);

    Integer getCertificateCountByStatus(String status, Long oemId);

    Double getCertificateUtilizationRate(Long oemId);

    // Search & Discovery
    List<Map<String, Object>> searchCertificates(String searchQuery, String status, Long oemId);
}