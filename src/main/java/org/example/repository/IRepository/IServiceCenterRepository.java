package org.example.repository.IRepository;

import java.util.List;
import java.util.Optional;

import org.example.models.core.ServiceCenter;

public interface IServiceCenterRepository {
    // Basic CRUD operations
    ServiceCenter save(ServiceCenter serviceCenter);

    Optional<ServiceCenter> findById(Long id);

    List<ServiceCenter> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Search methods
    List<ServiceCenter> findByNameContainingIgnoreCase(String name);

    List<ServiceCenter> findByAddressContainingIgnoreCase(String address);

    List<ServiceCenter> findByContactPersonContainingIgnoreCase(String contactPerson);

    Optional<ServiceCenter> findByPhoneNumber(String phoneNumber);

    Optional<ServiceCenter> findByEmail(String email);

    // Status methods
    List<ServiceCenter> findByIsActive(Boolean isActive);

    List<ServiceCenter> findActiveServiceCenters();

    // License methods
    Optional<ServiceCenter> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    // Search and filter methods
    List<ServiceCenter> searchServiceCenters(String keyword);

    // Validation methods
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    // Statistics methods
    Long countByIsActive(Boolean isActive);

    Long countActiveServiceCenters();
}