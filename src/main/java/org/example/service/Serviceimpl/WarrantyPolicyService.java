package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.example.models.core.Vehicle;
import org.example.models.core.WarrantyPolicy;
import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.json.WarrantyInfo;
import org.example.repository.IRepository.IVehicleRepository;
import org.example.service.IService.IWarrantyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarrantyPolicyService implements IWarrantyPolicyService {

    // Simple in-memory storage until repository is available
    private final Map<Long, WarrantyPolicy> policies = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    @Autowired
    private IVehicleRepository vehicleRepository;

    @Override
    public WarrantyPolicy createWarrantyPolicy(WarrantyPolicyCreateRequest request) {
        if (request.getModel() == null || request.getComponentCategory() == null) {
            throw new IllegalArgumentException("Model and componentCategory are required");
        }
        WarrantyPolicy policy = new WarrantyPolicy(
                request.getModel(),
                request.getComponentCategory(),
                request.getMonthsCoverage(),
                request.getKmCoverage());
        policy.setId(idSeq.getAndIncrement());
        policy.setNotes(request.getNotes());
        policies.put(policy.getId(), policy);
        return policy;
    }

    @Override
    public WarrantyPolicy updateWarrantyPolicy(Long policyId, WarrantyPolicyCreateRequest request) {
        WarrantyPolicy existing = policies.get(policyId);
        if (existing == null) {
            throw new RuntimeException("Policy not found");
        }
        if (request.getModel() != null)
            existing.setModel(request.getModel());
        if (request.getComponentCategory() != null)
            existing.setComponentCategory(request.getComponentCategory());
        if (request.getMonthsCoverage() != null)
            existing.setMonthsCoverage(request.getMonthsCoverage());
        if (request.getKmCoverage() != null)
            existing.setKmCoverage(request.getKmCoverage());
        if (request.getNotes() != null)
            existing.setNotes(request.getNotes());
        policies.put(policyId, existing);
        return existing;
    }

    @Override
    public Optional<WarrantyPolicy> getPolicyById(Long policyId) {
        return Optional.ofNullable(policies.get(policyId));
    }

    @Override
    public List<WarrantyPolicy> getPoliciesByOem(Long oemId) {
        // No OEM relation on model; return all for now
        return new ArrayList<>(policies.values());
    }

    @Override
    public boolean activatePolicy(Long policyId) {
        // No active flag on model; consider activation as existence
        return policies.containsKey(policyId);
    }

    @Override
    public boolean deactivatePolicy(Long policyId, String reason) {
        // Without a status flag, simulate deactivation by removing
        return policies.remove(policyId) != null;
    }

    @Override
    public List<WarrantyPolicy> getPoliciesByModel(String model, Long oemId) {
        return policies.values().stream()
                .filter(p -> model.equals(p.getModel()))
                .toList();
    }

    @Override
    public List<WarrantyPolicy> getPoliciesByComponent(String componentName, Long oemId) {
        return policies.values().stream()
                .filter(p -> componentName.equals(p.getComponentCategory()))
                .toList();
    }

    @Override
    public WarrantyPolicy getPolicyForVehicleComponent(String model, String componentName, Integer modelYear,
            Long oemId) {
        return policies.values().stream()
                .filter(p -> model.equals(p.getModel()) && componentName.equals(p.getComponentCategory()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addComponentCoverage(Long policyId, String componentName, Integer monthsCoverage,
            Integer kmCoverage) {
        WarrantyPolicy policy = policies.get(policyId);
        if (policy == null)
            return false;
        policy.setComponentCategory(componentName);
        if (monthsCoverage != null)
            policy.setMonthsCoverage(monthsCoverage);
        if (kmCoverage != null)
            policy.setKmCoverage(kmCoverage);
        return true;
    }

    @Override
    public boolean removeComponentCoverage(Long policyId, String componentName) {
        WarrantyPolicy policy = policies.get(policyId);
        if (policy == null)
            return false;
        if (componentName != null && componentName.equals(policy.getComponentCategory())) {
            policy.setComponentCategory(null);
            return true;
        }
        return false;
    }

    @Override
    public boolean validateWarranty(String vin, String componentSerial) {
        if (vin == null)
            return false;
        java.util.Optional<Vehicle> opt = vehicleRepository.findByVin(vin);
        if (opt.isEmpty())
            return false;
        Vehicle v = opt.get();
        WarrantyInfo info = v.getWarrantyInfo();
        if (info == null)
            return false;
        OffsetDateTime now = OffsetDateTime.now();
        boolean withinDates = true;
        if (info.getStartDate() != null && info.getEndDate() != null) {
            OffsetDateTime start = info.getStartDate().atStartOfDay().atOffset(now.getOffset());
            OffsetDateTime end = info.getEndDate().plusDays(1).atStartOfDay().atOffset(now.getOffset());
            withinDates = !now.isBefore(start) && now.isBefore(end);
        }
        boolean withinKm = true;
        if (info.getKmLimit() != null && v.getVehicleData() != null && v.getVehicleData().getOdometerKm() != null) {
            withinKm = v.getVehicleData().getOdometerKm() <= info.getKmLimit();
        }
        return withinDates && withinKm;
    }

    @Override
    public boolean validateWarrantyByDetails(String model, Integer modelYear, String componentName,
            LocalDate purchaseDate,
            Integer currentKm) {
        // Use stored policies if present; otherwise fallback to
        // vehicle.warranty_info-based simple decision
        boolean policyMatch = policies.values().stream()
                .filter(p -> model != null && model.equals(p.getModel())
                        && componentName != null && componentName.equals(p.getComponentCategory()))
                .anyMatch(p -> currentKm == null || p.getKmCoverage() == null || currentKm <= p.getKmCoverage());
        if (policyMatch)
            return true;
        // Fallback minimal check: if currentKm is not excessive, allow
        return currentKm == null || currentKm <= 200000;
    }

    @Override
    public boolean isComponentUnderWarranty(String vin, String componentSerial) {
        // Placeholder
        return true;
    }

    @Override
    public boolean isVehicleUnderWarranty(String vin) {
        if (vin == null)
            return false;
        java.util.Optional<Vehicle> opt = vehicleRepository.findByVin(vin);
        if (opt.isEmpty())
            return false;
        Vehicle v = opt.get();
        WarrantyInfo info = v.getWarrantyInfo();
        if (info == null)
            return false;
        OffsetDateTime now = OffsetDateTime.now();
        if (info.getStartDate() != null && info.getEndDate() != null) {
            OffsetDateTime start = info.getStartDate().atStartOfDay().atOffset(now.getOffset());
            OffsetDateTime end = info.getEndDate().plusDays(1).atStartOfDay().atOffset(now.getOffset());
            return !now.isBefore(start) && now.isBefore(end);
        }
        return true;
    }

    @Override
    public LocalDate getWarrantyExpiryDate(String vin, String componentName) {
        if (vin == null)
            return null;
        java.util.Optional<Vehicle> opt = vehicleRepository.findByVin(vin);
        if (opt.isEmpty())
            return null;
        Vehicle v = opt.get();
        WarrantyInfo info = v.getWarrantyInfo();
        return info != null ? info.getEndDate() : null;
    }

    @Override
    public boolean addWarrantyExclusion(Long policyId, String exclusionType, String description) {
        // Placeholder: no exclusions relation persisted
        return policies.containsKey(policyId);
    }

    @Override
    public boolean removeWarrantyExclusion(Long policyId, String exclusionType) {
        return policies.containsKey(policyId);
    }

    @Override
    public List<String> getWarrantyExclusions(Long policyId) {
        return List.of();
    }

    @Override
    public boolean checkExclusionApplies(String vin, String exclusionType) {
        return false;
    }

    @Override
    public Double getMaxCoverageAmount(Long policyId, String componentName) {
        return null;
    }

    @Override
    public Double calculateCoveragePercentage(String vin, String componentName, LocalDate claimDate) {
        return null;
    }

    @Override
    public boolean isClaimEligibleForCoverage(String vin, String componentName, Double claimAmount) {
        return true;
    }

    @Override
    public WarrantyPolicy createPolicyFromTemplate(String templateName, Long oemId) {
        // Placeholder template logic
        WarrantyPolicyCreateRequest req = new WarrantyPolicyCreateRequest("GenericModel", templateName, 24, 50000);
        return createWarrantyPolicy(req);
    }

    @Override
    public List<String> getAvailableTemplates() {
        return List.of("Battery", "Motor", "Inverter", "ChargingSystem");
    }

    @Override
    public boolean updatePolicyTemplate(String templateName, WarrantyPolicyCreateRequest template) {
        return true;
    }

    @Override
    public WarrantyPolicy createBatteryWarrantyPolicy(Long oemId, Integer yearsOrKm, Double degradationThreshold) {
        WarrantyPolicyCreateRequest req = new WarrantyPolicyCreateRequest("GenericModel", "Battery", yearsOrKm,
                yearsOrKm);
        return createWarrantyPolicy(req);
    }

    @Override
    public WarrantyPolicy createMotorWarrantyPolicy(Long oemId, Integer yearsOrKm) {
        WarrantyPolicyCreateRequest req = new WarrantyPolicyCreateRequest("GenericModel", "Motor", yearsOrKm,
                yearsOrKm);
        return createWarrantyPolicy(req);
    }

    @Override
    public WarrantyPolicy createInverterWarrantyPolicy(Long oemId, Integer yearsOrKm) {
        WarrantyPolicyCreateRequest req = new WarrantyPolicyCreateRequest("GenericModel", "Inverter", yearsOrKm,
                yearsOrKm);
        return createWarrantyPolicy(req);
    }

    @Override
    public WarrantyPolicy createChargingSystemWarrantyPolicy(Long oemId, Integer yearsOrKm) {
        WarrantyPolicyCreateRequest req = new WarrantyPolicyCreateRequest("GenericModel", "ChargingSystem", yearsOrKm,
                yearsOrKm);
        return createWarrantyPolicy(req);
    }

    @Override
    public List<WarrantyPolicy> searchPolicies(String searchQuery, String componentName, Long oemId) {
        return policies.values().stream()
                .filter(p -> (searchQuery == null || p.getModel().toLowerCase().contains(searchQuery.toLowerCase()))
                        && (componentName == null || componentName.equals(p.getComponentCategory())))
                .toList();
    }

    @Override
    public List<WarrantyPolicy> getActivePolicies(Long oemId) {
        return new ArrayList<>(policies.values());
    }

    @Override
    public List<WarrantyPolicy> getExpiredPolicies(Long oemId) {
        return List.of();
    }

    @Override
    public Long getPolicyCountByOem(Long oemId) {
        return (long) policies.size();
    }

    @Override
    public Long getPolicyCountByComponent(String componentName, Long oemId) {
        return policies.values().stream().filter(p -> componentName.equals(p.getComponentCategory())).count();
    }

    @Override
    public List<WarrantyPolicy> getPoliciesWithUpcomingExpiry(int daysFromNow, Long oemId) {
        return List.of();
    }

    @Override
    public Double getAverageCoverageByComponent(String componentName, Long oemId) {
        return null;
    }

    @Override
    public boolean updateRegulatoryCompliance(Long policyId, String region, String complianceStandard) {
        return policies.containsKey(policyId);
    }

    @Override
    public List<String> getRegulatoryRequirements(String region) {
        return List.of();
    }

    @Override
    public boolean validatePolicyCompliance(Long policyId, String region) {
        return true;
    }

    @Override
    public boolean syncPolicyWithExternalSystem(Long policyId, String externalSystemId) {
        return true;
    }

    @Override
    public WarrantyPolicy getPolicyByExternalId(String externalId) {
        return null;
    }

    @Override
    public List<WarrantyPolicy> getApplicablePoliciesForClaim(String vin, String componentName, LocalDate claimDate) {
        return new ArrayList<>(policies.values());
    }

    @Override
    public boolean approveCoverageForClaim(Long policyId, String vin, String componentName, Double amount) {
        return policies.containsKey(policyId);
    }

    @Override
    public boolean extendWarrantyPeriod(Long policyId, String vin, Integer additionalMonths, String reason) {
        WarrantyPolicy policy = policies.get(policyId);
        if (policy == null)
            return false;
        if (additionalMonths != null)
            policy.setMonthsCoverage(
                    (policy.getMonthsCoverage() == null ? 0 : policy.getMonthsCoverage()) + additionalMonths);
        return true;
    }

    @Override
    public boolean offerWarrantyExtension(String vin, String componentName, Integer extensionMonths, Double cost) {
        return true;
    }
}
