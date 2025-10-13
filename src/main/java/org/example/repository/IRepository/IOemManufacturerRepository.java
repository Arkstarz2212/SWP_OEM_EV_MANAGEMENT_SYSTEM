package org.example.repository.IRepository;

import java.util.List;
import java.util.Optional;

import org.example.models.core.OemManufacturer;

/**
 * Repository interface for OEM Manufacturers
 * Maps to: aoem.oem_manufacturers table
 */
public interface IOemManufacturerRepository {
    // Basic CRUD operations
    OemManufacturer save(OemManufacturer oemManufacturer);

    Optional<OemManufacturer> findById(Long id);

    List<OemManufacturer> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // OEM code methods
    Optional<OemManufacturer> findByCode(String code);

    boolean existsByCode(String code);

    // OEM name methods
    Optional<OemManufacturer> findByName(String name);

    boolean existsByName(String name);

    // Active status methods
    List<OemManufacturer> findByIsActive(boolean isActive);

    List<OemManufacturer> findAllActive();

    Long countByIsActive(boolean isActive);

    // Search methods
    List<OemManufacturer> findByCodeContainingIgnoreCase(String code);

    List<OemManufacturer> findByNameContainingIgnoreCase(String name);

    // Contact info search (JSON field search would be database-specific)
    List<OemManufacturer> searchByContactInfo(String searchTerm);

    // Service center relationship methods
    List<OemManufacturer> findByServiceCenterIds(List<Long> serviceCenterIds);

    boolean hasServiceCenterMapping(Long oemId, Long serviceCenterId);
}