package org.example.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.core.OemServiceCenter;

/**
 * Repository interface for OEM ↔ Service Center mappings (Many-to-Many)
 * Maps to: aoem.oem_service_centers table
 */
public interface IOemServiceCenterRepository {
    // Basic CRUD operations
    OemServiceCenter save(OemServiceCenter oemServiceCenter);

    Optional<OemServiceCenter> findById(Long id);

    List<OemServiceCenter> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Composite key operations
    Optional<OemServiceCenter> findByOemIdAndServiceCenterId(Long oemId, Long serviceCenterId);

    boolean existsByOemIdAndServiceCenterId(Long oemId, Long serviceCenterId);

    void deleteByOemIdAndServiceCenterId(Long oemId, Long serviceCenterId);

    // OEM-based queries
    List<OemServiceCenter> findByOemId(Long oemId);

    List<Long> findServiceCenterIdsByOemId(Long oemId);

    Long countByOemId(Long oemId);

    void deleteByOemId(Long oemId);

    // Service Center-based queries
    List<OemServiceCenter> findByServiceCenterId(Long serviceCenterId);

    List<Long> findOemIdsByServiceCenterId(Long serviceCenterId);

    Long countByServiceCenterId(Long serviceCenterId);

    void deleteByServiceCenterId(Long serviceCenterId);

    // Date range queries
    List<OemServiceCenter> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<OemServiceCenter> findByCreatedAtAfter(LocalDateTime date);

    // Bulk operations
    List<OemServiceCenter> findByOemIdIn(List<Long> oemIds);

    List<OemServiceCenter> findByServiceCenterIdIn(List<Long> serviceCenterIds);

    void deleteByOemIdIn(List<Long> oemIds);

    void deleteByServiceCenterIdIn(List<Long> serviceCenterIds);

    // Batch mapping operations
    void saveAllMappings(Long oemId, List<Long> serviceCenterIds);

    void removeAllMappings(Long oemId, List<Long> serviceCenterIds);

    void replaceAllMappings(Long oemId, List<Long> newServiceCenterIds);

    // Validation methods
    boolean isValidMapping(Long oemId, Long serviceCenterId);

    List<Long> findUnmappedServiceCenters(Long oemId);

    List<Long> findUnmappedOems(Long serviceCenterId);
}