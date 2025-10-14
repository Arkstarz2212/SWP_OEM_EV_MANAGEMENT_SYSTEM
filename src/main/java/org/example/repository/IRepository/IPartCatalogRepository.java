package org.example.repository.IRepository;

import java.util.List;
import java.util.Optional;

import org.example.models.core.PartCatalog;
import org.example.models.enums.PartCategory;

public interface IPartCatalogRepository {
    // Basic CRUD operations
    PartCatalog save(PartCatalog partCatalog);

    Optional<PartCatalog> findById(Long id);

    List<PartCatalog> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Part number methods
    Optional<PartCatalog> findByPartNumber(String partNumber);

    boolean existsByPartNumber(String partNumber);

    List<PartCatalog> findByPartNumberIn(List<String> partNumbers);

    // Category methods
    List<PartCatalog> findByCategory(PartCategory category);

    Long countByCategory(PartCategory category);

    // Vehicle model methods
    List<PartCatalog> findByVehicleModel(String vehicleModel);

    List<PartCatalog> findByVehicleModelContainingIgnoreCase(String vehicleModel);

    // Search methods
    List<PartCatalog> searchParts(String keyword);

    List<PartCatalog> findByPartNameContainingIgnoreCase(String partName);

    List<PartCatalog> findByDescriptionContainingIgnoreCase(String description);

    List<PartCatalog> findByManufacturerContainingIgnoreCase(String manufacturer);

    // Status and availability methods
    List<PartCatalog> findByIsActive(Boolean isActive);

    List<PartCatalog> findActiveParts();

    List<PartCatalog> findByIsActiveAndCategory(Boolean isActive, PartCategory category);

    // Cost methods
    List<PartCatalog> findByUnitCostBetween(Double minCost, Double maxCost);

    List<PartCatalog> findByUnitCostGreaterThan(Double cost);

    List<PartCatalog> findByUnitCostLessThan(Double cost);

    // Statistics methods
    Long countByIsActive(Boolean isActive);

    Double averageUnitCostByCategory(PartCategory category);
}