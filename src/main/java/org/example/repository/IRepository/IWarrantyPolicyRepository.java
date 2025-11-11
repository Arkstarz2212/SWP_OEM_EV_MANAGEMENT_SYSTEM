package org.example.repository.IRepository;

import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;

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

    // OEM methods
    List<WarrantyPolicy> findByOemId(Long oemId);

    List<WarrantyPolicy> findByOemIdAndIsActive(Long oemId, Boolean isActive);

    // Status methods
    List<WarrantyPolicy> findByIsActive(Boolean isActive);

    List<WarrantyPolicy> findActivePolicies();

    Optional<WarrantyPolicy> findByOemIdAndIsDefault(Long oemId, Boolean isDefault);

    // Search methods
    List<WarrantyPolicy> findByPolicyNameContainingIgnoreCase(String policyName);

    // Count methods
    Long countByOemId(Long oemId);

    Long countByIsActive(Boolean isActive);
}
