package org.example.service.IService;

import java.time.LocalDate;
import java.util.List;

import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.dto.response.WarrantyPolicyResponse;

/**
 * Service interface for Warranty Policy management
 * Handles business logic for warranty policy operations
 */
public interface IWarrantyPolicyService {
    // CRUD Operations
    WarrantyPolicyResponse createWarrantyPolicy(WarrantyPolicyCreateRequest request, Long oemId);

    WarrantyPolicyResponse getWarrantyPolicyById(Long policyId);

    WarrantyPolicyResponse getWarrantyPolicyByCode(String policyCode);

    WarrantyPolicyResponse updateWarrantyPolicy(Long policyId, WarrantyPolicyCreateRequest request);

    boolean deleteWarrantyPolicy(Long policyId);

    // List and Search Operations
    List<WarrantyPolicyResponse> getAllWarrantyPolicies(int limit, int offset);

    List<WarrantyPolicyResponse> getWarrantyPoliciesByOem(Long oemId, int limit, int offset);

    List<WarrantyPolicyResponse> getActiveWarrantyPolicies(int limit, int offset);

    List<WarrantyPolicyResponse> getActiveWarrantyPoliciesByOem(Long oemId, int limit, int offset);

    List<WarrantyPolicyResponse> searchWarrantyPolicies(String keyword, int limit, int offset);

    List<WarrantyPolicyResponse> searchWarrantyPoliciesByOem(Long oemId, String keyword, int limit, int offset);

    // Default Policy Management
    WarrantyPolicyResponse getDefaultPolicyByOem(Long oemId);

    boolean setDefaultPolicy(Long policyId, Long oemId);

    boolean removeDefaultPolicy(Long policyId);

    // Status Management
    boolean activateWarrantyPolicy(Long policyId);

    boolean deactivateWarrantyPolicy(Long policyId, String reason);

    boolean updateWarrantyPolicyStatus(Long policyId, boolean isActive);

    // Effective Date Management
    List<WarrantyPolicyResponse> getActivePoliciesForDate(LocalDate date);

    List<WarrantyPolicyResponse> getExpiringPolicies(LocalDate beforeDate);

    List<WarrantyPolicyResponse> getPoliciesByEffectiveDateRange(LocalDate startDate, LocalDate endDate);

    // Coverage Analysis
    List<WarrantyPolicyResponse> getPoliciesByWarrantyMonths(Integer minMonths, Integer maxMonths);

    List<WarrantyPolicyResponse> getPoliciesByWarrantyKm(Integer minKm, Integer maxKm);

    List<WarrantyPolicyResponse> getPoliciesByComponentCoverage(String component, Integer minMonths);

    // Validation and Business Rules
    boolean validatePolicyCode(String policyCode, Long excludePolicyId);

    boolean validatePolicyDates(LocalDate effectiveFrom, LocalDate effectiveTo);

    boolean canDeletePolicy(Long policyId);

    // Statistics and Analytics
    Long countWarrantyPoliciesByOem(Long oemId);

    Long countActiveWarrantyPoliciesByOem(Long oemId);

    Double getAverageWarrantyMonthsByOem(Long oemId);

    Double getAverageWarrantyKmByOem(Long oemId);

    // Policy Comparison and Analysis
    List<WarrantyPolicyResponse> comparePoliciesByOem(Long oemId);

    WarrantyPolicyResponse getMostGenerousPolicyByOem(Long oemId);

    WarrantyPolicyResponse getMostRestrictivePolicyByOem(Long oemId);
}
