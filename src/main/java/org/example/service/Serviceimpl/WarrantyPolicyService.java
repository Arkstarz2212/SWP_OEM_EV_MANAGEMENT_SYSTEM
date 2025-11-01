package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.models.core.WarrantyPolicy;
import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.dto.response.WarrantyPolicyResponse;
import org.example.repository.IRepository.IWarrantyPolicyRepository;
import org.example.service.IService.IWarrantyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for Warranty Policy management
 * Handles business logic for warranty policy operations
 */
@Service
@Transactional
public class WarrantyPolicyService implements IWarrantyPolicyService {

    @Autowired
    private IWarrantyPolicyRepository warrantyPolicyRepository;

    @Override
    public WarrantyPolicyResponse createWarrantyPolicy(WarrantyPolicyCreateRequest request, Long oemId) {
        // Validate policy code uniqueness
        if (warrantyPolicyRepository.existsByPolicyCode(request.getModel() + "_" + request.getComponentCategory())) {
            throw new IllegalArgumentException("Policy code already exists");
        }

        // Create new warranty policy
        WarrantyPolicy policy = new WarrantyPolicy();
        policy.setOemId(oemId);
        policy.setPolicyName(request.getModel() + " - " + request.getComponentCategory());
        policy.setPolicyCode(request.getModel() + "_" + request.getComponentCategory());

        // Map to database fields
        policy.setWarrantyPeriodMonths(request.getMonthsCoverage());
        policy.setWarrantyKmLimit(request.getKmCoverage());

        // Set coverage details as JSON
        String category = request.getComponentCategory().toLowerCase();
        String coverageJson = String.format("""
                {
                    "%s": {
                        "months": %d,
                        "km": %d,
                        "service_coverage": "free",
                        "parts_coverage": "free",
                        "conditions": ["normal_use"]
                    }
                }
                """, category, request.getMonthsCoverage(), request.getKmCoverage());
        policy.setCoverageDetails(coverageJson);

        policy.setIsActive(true);
        policy.setIsDefault(false);
        policy.setEffectiveFrom(LocalDate.now());
        policy.setEffectiveTo(LocalDate.now().plusYears(10)); // Default 10 years validity
        policy.setCreatedAt(OffsetDateTime.now());
        policy.setUpdatedAt(OffsetDateTime.now());

        WarrantyPolicy savedPolicy = warrantyPolicyRepository.save(policy);
        return convertToResponse(savedPolicy);
    }

    @Override
    public WarrantyPolicyResponse getWarrantyPolicyById(Long policyId) {
        Optional<WarrantyPolicy> policy = warrantyPolicyRepository.findById(policyId);
        if (policy.isPresent()) {
            return convertToResponse(policy.get());
        }
        throw new IllegalArgumentException("Warranty policy not found with ID: " + policyId);
    }

    @Override
    public WarrantyPolicyResponse getWarrantyPolicyByCode(String policyCode) {
        Optional<WarrantyPolicy> policy = warrantyPolicyRepository.findByPolicyCode(policyCode);
        if (policy.isPresent()) {
            return convertToResponse(policy.get());
        }
        throw new IllegalArgumentException("Warranty policy not found with code: " + policyCode);
    }

    @Override
    public WarrantyPolicyResponse updateWarrantyPolicy(Long policyId, WarrantyPolicyCreateRequest request) {
        Optional<WarrantyPolicy> existingPolicyOpt = warrantyPolicyRepository.findById(policyId);
        if (existingPolicyOpt.isEmpty()) {
            throw new IllegalArgumentException("Warranty policy not found with ID: " + policyId);
        }

        WarrantyPolicy existingPolicy = existingPolicyOpt.get();

        // Update policy details
        existingPolicy.setPolicyName(request.getModel() + " - " + request.getComponentCategory());
        existingPolicy.setWarrantyPeriodMonths(request.getMonthsCoverage());
        existingPolicy.setWarrantyKmLimit(request.getKmCoverage());
        existingPolicy.setUpdatedAt(OffsetDateTime.now());

        // Update coverage details as JSON
        String category = request.getComponentCategory().toLowerCase();
        String coverageJson = String.format("""
                {
                    "%s": {
                        "months": %d,
                        "km": %d,
                        "service_coverage": "free",
                        "parts_coverage": "free",
                        "conditions": ["normal_use"]
                    }
                }
                """, category, request.getMonthsCoverage(), request.getKmCoverage());
        existingPolicy.setCoverageDetails(coverageJson);

        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(existingPolicy);
        return convertToResponse(updatedPolicy);
    }

    @Override
    public boolean deleteWarrantyPolicy(Long policyId) {
        if (!warrantyPolicyRepository.existsById(policyId)) {
            throw new IllegalArgumentException("Warranty policy not found with ID: " + policyId);
        }

        // Check if policy can be deleted (business rules)
        if (!canDeletePolicy(policyId)) {
            throw new IllegalStateException(
                    "Cannot delete policy: it may be referenced by active claims or is the default policy");
        }

        warrantyPolicyRepository.deleteById(policyId);
        return true;
    }

    @Override
    public List<WarrantyPolicyResponse> getAllWarrantyPolicies(int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findAll();
        return policies.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getWarrantyPoliciesByOem(Long oemId, int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByOemId(oemId);
        return policies.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getActiveWarrantyPolicies(int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findAllActive();
        return policies.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getActiveWarrantyPoliciesByOem(Long oemId, int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByOemIdAndIsActive(oemId, true);
        return policies.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> searchWarrantyPolicies(String keyword, int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.searchPolicies(keyword);
        return policies.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> searchWarrantyPoliciesByOem(Long oemId, String keyword, int limit, int offset) {
        List<WarrantyPolicy> allPolicies = warrantyPolicyRepository.findByOemId(oemId);
        List<WarrantyPolicy> filteredPolicies = allPolicies.stream()
                .filter(policy -> policy.getPolicyName().toLowerCase().contains(keyword.toLowerCase()) ||
                        policy.getPolicyCode().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return filteredPolicies.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WarrantyPolicyResponse getDefaultPolicyByOem(Long oemId) {
        Optional<WarrantyPolicy> defaultPolicy = warrantyPolicyRepository.findByIsDefaultAndOemId(true, oemId);
        if (defaultPolicy.isPresent()) {
            return convertToResponse(defaultPolicy.get());
        }
        throw new IllegalArgumentException("No default policy found for OEM ID: " + oemId);
    }

    @Override
    public boolean setDefaultPolicy(Long policyId, Long oemId) {
        Optional<WarrantyPolicy> policyOpt = warrantyPolicyRepository.findById(policyId);
        if (policyOpt.isEmpty()) {
            throw new IllegalArgumentException("Warranty policy not found with ID: " + policyId);
        }

        WarrantyPolicy policy = policyOpt.get();

        // Remove default status from other policies of the same OEM
        List<WarrantyPolicy> existingDefaults = warrantyPolicyRepository.findByIsDefaultAndOemId(true, oemId).stream()
                .filter(p -> !p.getId().equals(policyId))
                .collect(Collectors.toList());

        for (WarrantyPolicy existingDefault : existingDefaults) {
            existingDefault.setIsDefault(false);
            warrantyPolicyRepository.save(existingDefault);
        }

        // Set this policy as default
        policy.setIsDefault(true);
        policy.setUpdatedAt(OffsetDateTime.now());
        warrantyPolicyRepository.save(policy);

        return true;
    }

    @Override
    public boolean removeDefaultPolicy(Long policyId) {
        Optional<WarrantyPolicy> policyOpt = warrantyPolicyRepository.findById(policyId);
        if (policyOpt.isEmpty()) {
            throw new IllegalArgumentException("Warranty policy not found with ID: " + policyId);
        }

        WarrantyPolicy policy = policyOpt.get();
        policy.setIsDefault(false);
        policy.setUpdatedAt(OffsetDateTime.now());
        warrantyPolicyRepository.save(policy);

        return true;
    }

    @Override
    public boolean activateWarrantyPolicy(Long policyId) {
        return updateWarrantyPolicyStatus(policyId, true);
    }

    @Override
    public boolean deactivateWarrantyPolicy(Long policyId, String reason) {
        return updateWarrantyPolicyStatus(policyId, false);
    }

    @Override
    public boolean updateWarrantyPolicyStatus(Long policyId, boolean isActive) {
        Optional<WarrantyPolicy> policyOpt = warrantyPolicyRepository.findById(policyId);
        if (policyOpt.isEmpty()) {
            throw new IllegalArgumentException("Warranty policy not found with ID: " + policyId);
        }

        WarrantyPolicy policy = policyOpt.get();
        policy.setIsActive(isActive);
        policy.setUpdatedAt(OffsetDateTime.now());
        warrantyPolicyRepository.save(policy);

        return true;
    }

    @Override
    public List<WarrantyPolicyResponse> getActivePoliciesForDate(LocalDate date) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findActivePoliciesForDate(date);
        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getExpiringPolicies(LocalDate beforeDate) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findExpiringPolicies(beforeDate);
        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getPoliciesByEffectiveDateRange(LocalDate startDate, LocalDate endDate) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByEffectiveFromBetween(startDate, endDate);
        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getPoliciesByWarrantyMonths(Integer minMonths, Integer maxMonths) {
        List<WarrantyPolicy> policies;
        if (minMonths != null && maxMonths != null) {
            policies = warrantyPolicyRepository.findByWarrantyMonthsBetween(minMonths, maxMonths);
        } else if (minMonths != null) {
            policies = warrantyPolicyRepository.findByWarrantyMonthsGreaterThanEqual(minMonths);
        } else {
            policies = warrantyPolicyRepository.findAll();
        }

        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getPoliciesByWarrantyKm(Integer minKm, Integer maxKm) {
        List<WarrantyPolicy> policies;
        if (minKm != null && maxKm != null) {
            policies = warrantyPolicyRepository.findByWarrantyKmBetween(minKm, maxKm);
        } else if (minKm != null) {
            policies = warrantyPolicyRepository.findByWarrantyKmGreaterThanEqual(minKm);
        } else {
            policies = warrantyPolicyRepository.findAll();
        }

        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarrantyPolicyResponse> getPoliciesByComponentCoverage(String component, Integer minMonths) {
        List<WarrantyPolicy> policies;
        String componentLower = component.toLowerCase();

        if (componentLower.contains("battery")) {
            policies = warrantyPolicyRepository.findByBatteryCoverageMonthsGreaterThanEqual(minMonths);
        } else if (componentLower.contains("motor")) {
            policies = warrantyPolicyRepository.findByMotorCoverageMonthsGreaterThanEqual(minMonths);
        } else if (componentLower.contains("inverter")) {
            policies = warrantyPolicyRepository.findByInverterCoverageMonthsGreaterThanEqual(minMonths);
        } else {
            policies = warrantyPolicyRepository.findAll();
        }

        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validatePolicyCode(String policyCode, Long excludePolicyId) {
        Optional<WarrantyPolicy> existingPolicy = warrantyPolicyRepository.findByPolicyCode(policyCode);
        return existingPolicy.isEmpty() || existingPolicy.get().getId().equals(excludePolicyId);
    }

    @Override
    public boolean validatePolicyDates(LocalDate effectiveFrom, LocalDate effectiveTo) {
        if (effectiveFrom == null || effectiveTo == null) {
            return true; // Allow null dates
        }
        return !effectiveFrom.isAfter(effectiveTo); // From should not be after To
    }

    @Override
    public boolean canDeletePolicy(Long policyId) {
        Optional<WarrantyPolicy> policyOpt = warrantyPolicyRepository.findById(policyId);
        if (policyOpt.isEmpty()) {
            return false;
        }

        WarrantyPolicy policy = policyOpt.get();

        // Cannot delete if it's the default policy
        // Add more business rules here (e.g., check if referenced by active claims)
        return !Boolean.TRUE.equals(policy.getIsDefault());
    }

    @Override
    public Long countWarrantyPoliciesByOem(Long oemId) {
        return warrantyPolicyRepository.countByOemId(oemId);
    }

    @Override
    public Long countActiveWarrantyPoliciesByOem(Long oemId) {
        return warrantyPolicyRepository.countByOemIdAndIsActive(oemId, true);
    }

    @Override
    public Double getAverageWarrantyMonthsByOem(Long oemId) {
        return warrantyPolicyRepository.averageWarrantyMonthsByOem(oemId);
    }

    @Override
    public Double getAverageWarrantyKmByOem(Long oemId) {
        return warrantyPolicyRepository.averageWarrantyKmByOem(oemId);
    }

    @Override
    public List<WarrantyPolicyResponse> comparePoliciesByOem(Long oemId) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByOemId(oemId);
        return policies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WarrantyPolicyResponse getMostGenerousPolicyByOem(Long oemId) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByOemIdAndIsActive(oemId, true);
        if (policies.isEmpty()) {
            throw new IllegalArgumentException("No active policies found for OEM ID: " + oemId);
        }

        WarrantyPolicy mostGenerous = policies.stream()
                .max((p1, p2) -> {
                    int months1 = Optional.ofNullable(p1.getWarrantyPeriodMonths()).orElse(0);
                    int months2 = Optional.ofNullable(p2.getWarrantyPeriodMonths()).orElse(0);
                    int km1 = Optional.ofNullable(p1.getWarrantyKmLimit()).orElse(0);
                    int km2 = Optional.ofNullable(p2.getWarrantyKmLimit()).orElse(0);

                    // Compare by months first, then by km
                    int monthsCompare = Integer.compare(months1, months2);
                    return monthsCompare != 0 ? monthsCompare : Integer.compare(km1, km2);
                })
                .orElse(policies.get(0));

        return convertToResponse(mostGenerous);
    }

    @Override
    public WarrantyPolicyResponse getMostRestrictivePolicyByOem(Long oemId) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByOemIdAndIsActive(oemId, true);
        if (policies.isEmpty()) {
            throw new IllegalArgumentException("No active policies found for OEM ID: " + oemId);
        }

        WarrantyPolicy mostRestrictive = policies.stream()
                .min((p1, p2) -> {
                    int months1 = Optional.ofNullable(p1.getWarrantyPeriodMonths()).orElse(Integer.MAX_VALUE);
                    int months2 = Optional.ofNullable(p2.getWarrantyPeriodMonths()).orElse(Integer.MAX_VALUE);
                    int km1 = Optional.ofNullable(p1.getWarrantyKmLimit()).orElse(Integer.MAX_VALUE);
                    int km2 = Optional.ofNullable(p2.getWarrantyKmLimit()).orElse(Integer.MAX_VALUE);

                    // Compare by months first, then by km
                    int monthsCompare = Integer.compare(months1, months2);
                    return monthsCompare != 0 ? monthsCompare : Integer.compare(km1, km2);
                })
                .orElse(policies.get(0));

        return convertToResponse(mostRestrictive);
    }

    /**
     * Convert WarrantyPolicy entity to WarrantyPolicyResponse DTO
     */
    private WarrantyPolicyResponse convertToResponse(WarrantyPolicy policy) {
        WarrantyPolicyResponse response = new WarrantyPolicyResponse();

        // Basic fields
        response.setId(policy.getId());
        response.setOemId(policy.getOemId());
        response.setPolicyName(policy.getPolicyName());
        response.setPolicyCode(policy.getPolicyCode());
        response.setDescription(policy.getDescription());

        // Warranty coverage fields
        response.setWarrantyPeriodMonths(policy.getWarrantyPeriodMonths());
        response.setWarrantyKmLimit(policy.getWarrantyKmLimit());

        // Coverage details (JSON)
        response.setCoverageDetails(policy.getCoverageDetails());

        // Status fields
        response.setIsActive(policy.getIsActive());
        response.setIsDefault(policy.getIsDefault());

        // Effective dates
        response.setEffectiveFrom(policy.getEffectiveFrom());
        response.setEffectiveTo(policy.getEffectiveTo());

        // Timestamps
        response.setCreatedAt(policy.getCreatedAt());
        response.setCreatedByUserId(policy.getCreatedByUserId());
        response.setUpdatedAt(policy.getUpdatedAt());
        response.setUpdatedByUserId(policy.getUpdatedByUserId());

        // Legacy fields for backward compatibility
        response.setModel(extractModelFromPolicyName(policy.getPolicyName()));
        response.setComponentCategory(extractCategoryFromPolicyName(policy.getPolicyName()));
        response.setMonthsCoverage(policy.getWarrantyPeriodMonths());
        response.setKmCoverage(policy.getWarrantyKmLimit());
        response.setNotes("Policy: " + policy.getPolicyName() +
                (Boolean.TRUE.equals(policy.getIsDefault()) ? " (Default)" : "") +
                (Boolean.TRUE.equals(policy.getIsActive()) ? " (Active)" : " (Inactive)"));

        return response;
    }

    /**
     * Extract model name from policy name
     */
    private String extractModelFromPolicyName(String policyName) {
        if (policyName == null || !policyName.contains(" - ")) {
            return "Unknown";
        }
        return policyName.split(" - ")[0];
    }

    /**
     * Extract component category from policy name
     */
    private String extractCategoryFromPolicyName(String policyName) {
        if (policyName == null || !policyName.contains(" - ")) {
            return "General";
        }
        return policyName.split(" - ")[1];
    }
}
