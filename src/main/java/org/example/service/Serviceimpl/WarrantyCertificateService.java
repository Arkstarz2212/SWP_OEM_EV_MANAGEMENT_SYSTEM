package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.service.IService.IWarrantyCertificateService;
import org.springframework.stereotype.Service;

@Service
public class WarrantyCertificateService implements IWarrantyCertificateService {

    private final Map<Long, Map<String, Object>> storeById = new ConcurrentHashMap<>();
    private final Map<String, Long> activeCertByVin = new ConcurrentHashMap<>();
    private long idSeq = 1L;

    @Override
    public Map<String, Object> createWarrantyCertificate(String vin, Long policyId, LocalDate startDate) {
        long id = idSeq++;
        Map<String, Object> cert = new HashMap<>();
        cert.put("id", id);
        cert.put("vin", vin);
        cert.put("policyId", policyId);
        cert.put("startDate", startDate);
        cert.put("active", true);
        storeById.put(id, cert);
        activeCertByVin.put(vin, id);
        return cert;
    }

    @Override
    public Map<String, Object> getCertificateByVin(String vin) {
        Long id = activeCertByVin.get(vin);
        return id != null ? storeById.get(id) : null;
    }

    @Override
    public Map<String, Object> getCertificateById(Long certificateId) {
        return storeById.get(certificateId);
    }

    @Override
    public boolean activateCertificate(Long certificateId) {
        Map<String, Object> cert = storeById.get(certificateId);
        if (cert == null)
            return false;
        cert.put("active", true);
        activeCertByVin.put((String) cert.get("vin"), certificateId);
        return true;
    }

    @Override
    public boolean deactivateCertificate(Long certificateId, String reason) {
        Map<String, Object> cert = storeById.get(certificateId);
        if (cert == null)
            return false;
        cert.put("active", false);
        String vin = (String) cert.get("vin");
        activeCertByVin.remove(vin);
        cert.put("deactivationReason", reason);
        return true;
    }

    @Override
    public boolean validateCertificate(String vin, String componentName) {
        Map<String, Object> cert = getCertificateByVin(vin);
        return cert != null && Boolean.TRUE.equals(cert.get("active"));
    }

    @Override
    public boolean isCertificateValid(Long certificateId) {
        Map<String, Object> cert = storeById.get(certificateId);
        return cert != null && Boolean.TRUE.equals(cert.get("active"));
    }

    @Override
    public LocalDate getCertificateExpiryDate(String vin, String componentName) {
        // Placeholder without policy duration info
        return null;
    }

    @Override
    public Integer getRemainingWarrantyDays(String vin, String componentName) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getCertificatesExpiringInDays(int daysFromNow) {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getExpiredCertificates() {
        return new ArrayList<>();
    }

    @Override
    public boolean sendExpiryNotification(Long certificateId) {
        return true;
    }

    @Override
    public boolean scheduleExpiryReminders(Long certificateId) {
        return true;
    }

    @Override
    public boolean extendCertificate(Long certificateId, Integer additionalMonths, String reason) {
        // Placeholder: record extension
        Map<String, Object> cert = storeById.get(certificateId);
        if (cert == null)
            return false;
        cert.put("extendedMonths", additionalMonths);
        cert.put("extensionReason", reason);
        return true;
    }

    @Override
    public Map<String, Object> renewCertificate(Long certificateId, Long newPolicyId) {
        Map<String, Object> cert = storeById.get(certificateId);
        if (cert == null)
            return null;
        cert.put("policyId", newPolicyId);
        cert.put("renewed", true);
        return cert;
    }

    @Override
    public boolean offerExtension(String vin, Integer extensionMonths, Double cost) {
        return true;
    }

    @Override
    public boolean recordCertificateUsage(Long certificateId, Long claimId, String usageType) {
        Map<String, Object> cert = storeById.get(certificateId);
        if (cert == null)
            return false;
        cert.put("lastUsageClaimId", claimId);
        cert.put("lastUsageType", usageType);
        return true;
    }

    @Override
    public List<Map<String, Object>> getCertificateUsageHistory(Long certificateId) {
        return new ArrayList<>();
    }

    @Override
    public boolean canUseCertificateForClaim(Long certificateId, String componentName, Double claimAmount) {
        return isCertificateValid(certificateId);
    }

    @Override
    public List<Map<String, Object>> getCustomerCertificates(Long customerId) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getActiveCertificateForVehicle(String vin) {
        return getCertificateByVin(vin);
    }

    @Override
    public List<Map<String, Object>> getCertificatesByOem(Long oemId) {
        return new ArrayList<>();
    }

    @Override
    public Integer getCertificateCountByStatus(String status, Long oemId) {
        return 0;
    }

    @Override
    public Double getCertificateUtilizationRate(Long oemId) {
        return 0.0;
    }

    @Override
    public List<Map<String, Object>> searchCertificates(String searchQuery, String status, Long oemId) {
        return new ArrayList<>();
    }
}
