package org.example.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;
import org.example.models.dto.request.WarrantyPolicyCreateRequest;

public interface IWarrantyPolicyService {
    // Policy Management
    WarrantyPolicy createWarrantyPolicy(WarrantyPolicyCreateRequest request);

    WarrantyPolicy updateWarrantyPolicy(Long policyId, WarrantyPolicyCreateRequest request);

    Optional<WarrantyPolicy> getPolicyById(Long policyId);

    List<WarrantyPolicy> getPoliciesByOem(Long oemId);

    boolean activatePolicy(Long policyId);

    boolean deactivatePolicy(Long policyId, String reason);

    // Vehicle Model and Component Coverage
    List<WarrantyPolicy> getPoliciesByModel(String model, Long oemId);

    List<WarrantyPolicy> getPoliciesByComponent(String componentName, Long oemId);

    WarrantyPolicy getPolicyForVehicleComponent(String model, String componentName, Integer modelYear, Long oemId);

    boolean addComponentCoverage(Long policyId, String componentName, Integer monthsCoverage, Integer kmCoverage);

    boolean removeComponentCoverage(Long policyId, String componentName);

    // Warranty Validation and Eligibility
    boolean validateWarranty(String vin, String componentSerial);

    boolean validateWarrantyByDetails(String model, Integer modelYear, String componentName,
            LocalDate purchaseDate, Integer currentKm);

    boolean isComponentUnderWarranty(String vin, String componentSerial);

    boolean isVehicleUnderWarranty(String vin);

    LocalDate getWarrantyExpiryDate(String vin, String componentName);

    // Exclusions and Conditions Management
    boolean addWarrantyExclusion(Long policyId, String exclusionType, String description);

    boolean removeWarrantyExclusion(Long policyId, String exclusionType);

    List<String> getWarrantyExclusions(Long policyId);

    boolean checkExclusionApplies(String vin, String exclusionType);

    // Cost and Coverage Analysis
    Double getMaxCoverageAmount(Long policyId, String componentName);

    Double calculateCoveragePercentage(String vin, String componentName, LocalDate claimDate);

    boolean isClaimEligibleForCoverage(String vin, String componentName, Double claimAmount);

    // Policy Templates and Standards
    WarrantyPolicy createPolicyFromTemplate(String templateName, Long oemId);

    List<String> getAvailableTemplates();

    boolean updatePolicyTemplate(String templateName, WarrantyPolicyCreateRequest template);

    // EV-Specific Warranty Features
    WarrantyPolicy createBatteryWarrantyPolicy(Long oemId, Integer yearsOrKm, Double degradationThreshold);

    WarrantyPolicy createMotorWarrantyPolicy(Long oemId, Integer yearsOrKm);

    WarrantyPolicy createInverterWarrantyPolicy(Long oemId, Integer yearsOrKm);

    WarrantyPolicy createChargingSystemWarrantyPolicy(Long oemId, Integer yearsOrKm);

    // Search and Discovery
    List<WarrantyPolicy> searchPolicies(String searchQuery, String componentName, Long oemId);

    List<WarrantyPolicy> getActivePolicies(Long oemId);

    List<WarrantyPolicy> getExpiredPolicies(Long oemId);

    // Coverage Statistics and Analytics
    Long getPolicyCountByOem(Long oemId);

    Long getPolicyCountByComponent(String componentName, Long oemId);

    List<WarrantyPolicy> getPoliciesWithUpcomingExpiry(int daysFromNow, Long oemId);

    Double getAverageCoverageByComponent(String componentName, Long oemId);

    // Compliance and Regulatory
    boolean updateRegulatoryCompliance(Long policyId, String region, String complianceStandard);

    List<String> getRegulatoryRequirements(String region);

    boolean validatePolicyCompliance(Long policyId, String region);

    // Integration and External Systems
    boolean syncPolicyWithExternalSystem(Long policyId, String externalSystemId);

    WarrantyPolicy getPolicyByExternalId(String externalId);

    // Claim Support
    List<WarrantyPolicy> getApplicablePoliciesForClaim(String vin, String componentName, LocalDate claimDate);

    boolean approveCoverageForClaim(Long policyId, String vin, String componentName, Double amount);

    // Warranty Extensions and Renewals
    boolean extendWarrantyPeriod(Long policyId, String vin, Integer additionalMonths, String reason);

    boolean offerWarrantyExtension(String vin, String componentName, Integer extensionMonths, Double cost);
}
