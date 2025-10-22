package org.example.repository.IRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;

/**
 * Repository interface for Warranty Policies
 * Maps to: aoem.warranty_policies table
 */
public interface IWarrantyPolicyRepository {
    // Basic CRUD operations
    WarrantyPolicy save(WarrantyPolicy warrantyPolicy);

    Optional<WarrantyPolicy> findById(Long id);

    List<WarrantyPolicy> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Policy code methods
    Optional<WarrantyPolicy> findByPolicyCode(String policyCode);

    boolean existsByPolicyCode(String policyCode);

    // OEM related methods
    List<WarrantyPolicy> findByOemId(Long oemId);

    List<WarrantyPolicy> findByOemIdAndIsActive(Long oemId, Boolean isActive);

    Long countByOemId(Long oemId);

    // Active status methods
    List<WarrantyPolicy> findByIsActive(Boolean isActive);

    List<WarrantyPolicy> findAllActive();

    Long countByIsActive(Boolean isActive);

    // Default policy methods
    Optional<WarrantyPolicy> findByIsDefaultAndOemId(Boolean isDefault, Long oemId);

    List<WarrantyPolicy> findByIsDefault(Boolean isDefault);

    // Effective date methods
    List<WarrantyPolicy> findByEffectiveFromBefore(LocalDate date);

    List<WarrantyPolicy> findByEffectiveToAfter(LocalDate date);

    List<WarrantyPolicy> findByEffectiveFromBetween(LocalDate startDate, LocalDate endDate);

    List<WarrantyPolicy> findByEffectiveToBetween(LocalDate startDate, LocalDate endDate);

    List<WarrantyPolicy> findActivePoliciesForDate(LocalDate date);

    List<WarrantyPolicy> findExpiringPolicies(LocalDate beforeDate);

    // Search methods
    List<WarrantyPolicy> findByPolicyNameContainingIgnoreCase(String policyName);

    List<WarrantyPolicy> searchPolicies(String keyword);

    // Warranty coverage methods
    List<WarrantyPolicy> findByWarrantyMonthsGreaterThanEqual(Integer months);

    List<WarrantyPolicy> findByWarrantyKmGreaterThanEqual(Integer km);

    List<WarrantyPolicy> findByWarrantyMonthsBetween(Integer minMonths, Integer maxMonths);

    List<WarrantyPolicy> findByWarrantyKmBetween(Integer minKm, Integer maxKm);

    // Component coverage methods
    List<WarrantyPolicy> findByBatteryCoverageMonthsGreaterThanEqual(Integer months);

    List<WarrantyPolicy> findByMotorCoverageMonthsGreaterThanEqual(Integer months);

    List<WarrantyPolicy> findByInverterCoverageMonthsGreaterThanEqual(Integer months);

    // Statistics methods
    Long countByOemIdAndIsActive(Long oemId, Boolean isActive);

    Double averageWarrantyMonthsByOem(Long oemId);

    Double averageWarrantyKmByOem(Long oemId);
}
