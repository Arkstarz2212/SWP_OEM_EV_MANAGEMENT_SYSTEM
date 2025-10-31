package org.example.repository.IRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.Vehicle;

public interface IVehicleRepository {
    // Basic CRUD operations
    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(Long id);

    List<Vehicle> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // VIN related methods
    Optional<Vehicle> findByVin(String vin);

    boolean existsByVin(String vin);

    List<Vehicle> findByVinIn(List<String> vins);

    // Customer related methods
    List<Vehicle> findByUserId(Long userId);

    Long countByUserId(Long userId);

    // Model and variant methods
    List<Vehicle> findByModel(String model);

    List<Vehicle> findByModelAndVariant(String model, String variant);

    List<Vehicle> findByModelYear(Integer modelYear);

    // Warranty related methods
    List<Vehicle> findByWarrantyEndDateBefore(LocalDate date);

    List<Vehicle> findByWarrantyEndDateAfter(LocalDate date);

    List<Vehicle> findByWarrantyEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<Vehicle> findActiveWarrantyVehicles();

    List<Vehicle> findExpiredWarrantyVehicles();

    // Search methods
    List<Vehicle> searchVehicles(String keyword);

    List<Vehicle> findByModelContainingIgnoreCase(String model);

    // JSON field search methods (using vehicle_data JSON)
    List<Vehicle> findByVariantInVehicleData(String variant);

    List<Vehicle> findByColorInVehicleData(String color);

    List<Vehicle> findByBatteryCapacityInVehicleData(String batteryCapacity);

    List<Vehicle> findByCustomerTypeInVehicleData(String customerType);

    // JSON field search methods (using warranty_info JSON)
    List<Vehicle> findByWarrantyStartDateInWarrantyInfo(LocalDate startDate);

    List<Vehicle> findByKmLimitInWarrantyInfo(Integer kmLimit);

    List<Vehicle> findByCoverageRuleInWarrantyInfo(String coverageType, boolean isFullCoverage);

    // Complex JSON queries
    List<Vehicle> findVehiclesWithActiveBatteryCoverage();

    List<Vehicle> findVehiclesWithExpiredWarrantyButActiveParts();

    List<Vehicle> searchInAllJsonFields(String searchTerm);

    // Statistics methods
    Long countByModel(String model);

    Long countByModelYear(Integer modelYear);

    Long countActiveWarranties();

    Long countExpiredWarranties();

    // Soft delete methods
    List<Vehicle> findDeletedVehicles();

    void restoreById(Long id);

    List<Vehicle> findByStatus(String status);
}