package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.models.core.Vehicle;
import org.example.models.dto.request.VehicleComponentRegisterRequest;
import org.example.models.dto.request.VehicleRegisterRequest;
import org.example.models.dto.request.VehicleSearchRequest;
import org.example.models.dto.response.VehicleDetailResponse;
import org.example.models.dto.response.VehicleResponse;
import org.example.models.json.VehicleData;
import org.example.models.json.WarrantyInfo;
import org.example.repository.IRepository.IVehicleRepository;
import org.example.service.IService.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    private IVehicleRepository vehicleRepository;

    @Override
    public VehicleDetailResponse registerVehicleByVin(VehicleRegisterRequest request) {
        // Validate input
        if (request.getVin() == null || request.getOemId() == null ||
                request.getModel() == null || request.getModelYear() == null) {
            throw new IllegalArgumentException("VIN, OEM ID, model, and model year are required");
        }

        // Check if vehicle already exists
        if (vehicleRepository.existsByVin(request.getVin())) {
            throw new RuntimeException("Vehicle with VIN " + request.getVin() + " already exists");
        }

        // Create vehicle entity
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(request.getVin());
        vehicle.setOemId(request.getOemId());
        vehicle.setModel(request.getModel());
        vehicle.setModelYear(request.getModelYear());
        vehicle.setCustomerId(request.getCustomerId());
        vehicle.setCreatedAt(OffsetDateTime.now());

        // Set vehicle data
        VehicleData vehicleData = new VehicleData();
        vehicleData.setVariant(request.getVariant());
        vehicleData.setColor(request.getColor());
        vehicleData.setBatteryCapacity(request.getBatteryCapacity());
        vehicleData.setOdometerKm(request.getOdometerKm() != null ? request.getOdometerKm() : 0);
        vehicle.setVehicleData(vehicleData);

        // Set warranty info
        WarrantyInfo warrantyInfo = new WarrantyInfo();
        warrantyInfo.setStartDate(request.getWarrantyStartDate());
        warrantyInfo.setEndDate(request.getWarrantyEndDate());
        warrantyInfo.setKmLimit(request.getKmLimit());
        vehicle.setWarrantyInfo(warrantyInfo);

        // Save vehicle
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Convert to detail response
        return convertToVehicleDetailResponse(savedVehicle);
    }

    @Override
    public VehicleDetailResponse attachComponentSerials(String vin, VehicleComponentRegisterRequest request) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Vehicle with VIN " + vin + " not found");
        }

        Vehicle vehicle = vehicleOpt.get();

        // TODO: Implement component attachment logic
        // This would involve creating VehicleComponent entities and linking them to the
        // vehicle

        return convertToVehicleDetailResponse(vehicle);
    }

    @Override
    public VehicleDetailResponse updateVehicleInfo(String vin, VehicleRegisterRequest request) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Vehicle with VIN " + vin + " not found");
        }

        Vehicle vehicle = vehicleOpt.get();

        // Update basic info
        if (request.getModel() != null) {
            vehicle.setModel(request.getModel());
        }
        if (request.getModelYear() != null) {
            vehicle.setModelYear(request.getModelYear());
        }
        if (request.getCustomerId() != null) {
            vehicle.setCustomerId(request.getCustomerId());
        }

        // Update vehicle data
        VehicleData vehicleData = vehicle.getVehicleData();
        if (vehicleData == null) {
            vehicleData = new VehicleData();
        }

        if (request.getVariant() != null) {
            vehicleData.setVariant(request.getVariant());
        }
        if (request.getColor() != null) {
            vehicleData.setColor(request.getColor());
        }
        if (request.getBatteryCapacity() != null) {
            vehicleData.setBatteryCapacity(request.getBatteryCapacity());
        }
        if (request.getOdometerKm() != null) {
            vehicleData.setOdometerKm(request.getOdometerKm());
        }

        vehicle.setVehicleData(vehicleData);

        // Update warranty info
        WarrantyInfo warrantyInfo = vehicle.getWarrantyInfo();
        if (warrantyInfo == null) {
            warrantyInfo = new WarrantyInfo();
        }

        if (request.getWarrantyStartDate() != null) {
            warrantyInfo.setStartDate(request.getWarrantyStartDate());
        }
        if (request.getWarrantyEndDate() != null) {
            warrantyInfo.setEndDate(request.getWarrantyEndDate());
        }
        if (request.getKmLimit() != null) {
            warrantyInfo.setKmLimit(request.getKmLimit());
        }

        vehicle.setWarrantyInfo(warrantyInfo);

        // Save updated vehicle
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToVehicleDetailResponse(savedVehicle);
    }

    @Override
    public boolean deactivateVehicle(String vin, String reason) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        // In this simple implementation, we don't have a deactivated flag
        // In a real system, you might add a status field or soft delete
        // For now, we'll just return true
        return true;
    }

    @Override
    public VehicleDetailResponse getVehicleByVin(String vin) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Vehicle with VIN " + vin + " not found");
        }

        return convertToVehicleDetailResponse(vehicleOpt.get());
    }

    @Override
    public VehicleDetailResponse getVehicleById(Long vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Vehicle with ID " + vehicleId + " not found");
        }

        return convertToVehicleDetailResponse(vehicleOpt.get());
    }

    @Override
    public List<VehicleResponse> getVehiclesByOem(Long oemId) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .filter(vehicle -> oemId.equals(vehicle.getOemId()))
                .map(this::convertToVehicleResponse)
                .toList();
    }

    @Override
    public List<VehicleResponse> getVehiclesByCustomer(Long customerId) {
        List<Vehicle> vehicles = vehicleRepository.findByUserId(customerId);
        return vehicles.stream().map(this::convertToVehicleResponse).toList();
    }

    @Override
    public List<VehicleResponse> searchVehicles(VehicleSearchRequest request) {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        // Apply filters
        if (request.getVin() != null && !request.getVin().trim().isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getVin().toLowerCase().contains(request.getVin().toLowerCase()))
                    .toList();
        }

        if (request.getModel() != null && !request.getModel().trim().isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getModel().toLowerCase().contains(request.getModel().toLowerCase()))
                    .toList();
        }

        // TODO: Add more search filters as needed

        return vehicles.stream().map(this::convertToVehicleResponse).toList();
    }

    @Override
    public boolean updateOdometer(String vin, Integer newOdometerKm, String source) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicleOpt.get();
        VehicleData vehicleData = vehicle.getVehicleData();
        if (vehicleData == null) {
            vehicleData = new VehicleData();
        }

        vehicleData.setOdometerKm(newOdometerKm);
        vehicle.setVehicleData(vehicleData);
        vehicleRepository.save(vehicle);

        return true;
    }

    @Override
    public boolean updateVehicleLocation(String vin, String location) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicleOpt.get();
        VehicleData vehicleData = vehicle.getVehicleData();
        if (vehicleData == null) {
            vehicleData = new VehicleData();
        }

        // TODO: Add location field to VehicleData
        vehicle.setVehicleData(vehicleData);
        vehicleRepository.save(vehicle);

        return true;
    }

    @Override
    public boolean recordServiceHistory(String vin, String serviceType, Long serviceCenterId, String notes) {
        // TODO: Implement service history recording
        // This would involve creating a ServiceHistory entity
        return true;
    }

    @Override
    public VehicleDetailResponse getVehicleWarrantyInfo(String vin) {
        return getVehicleByVin(vin);
    }

    @Override
    public boolean updateWarrantyInfo(String vin, String warrantyType, String startDate, String endDate) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicleOpt.get();
        WarrantyInfo warrantyInfo = vehicle.getWarrantyInfo();
        if (warrantyInfo == null) {
            warrantyInfo = new WarrantyInfo();
        }

        if (startDate != null) {
            warrantyInfo.setStartDate(LocalDate.parse(startDate));
        }
        if (endDate != null) {
            warrantyInfo.setEndDate(LocalDate.parse(endDate));
        }

        vehicle.setWarrantyInfo(warrantyInfo);
        vehicleRepository.save(vehicle);

        return true;
    }

    @Override
    public boolean checkWarrantyStatus(String vin) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicleOpt.get();
        WarrantyInfo warrantyInfo = vehicle.getWarrantyInfo();
        if (warrantyInfo == null || warrantyInfo.getEndDate() == null) {
            return false;
        }

        return warrantyInfo.getEndDate().isAfter(LocalDate.now());
    }

    @Override
    public List<VehicleResponse> getVehiclesWithExpiringWarranty(int daysFromNow) {
        LocalDate expiryDate = LocalDate.now().plusDays(daysFromNow);
        List<Vehicle> vehicles = vehicleRepository.findByWarrantyEndDateBefore(expiryDate);
        return vehicles.stream().map(this::convertToVehicleResponse).toList();
    }

    @Override
    public List<String> getVehiclesByModelAndYear(String model, Integer modelYear, Long oemId) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .filter(vehicle -> model.equals(vehicle.getModel()) &&
                        modelYear.equals(vehicle.getModelYear()) &&
                        oemId.equals(vehicle.getOemId()))
                .map(Vehicle::getVin)
                .toList();
    }

    @Override
    public List<String> getVinsByComponentCriteria(String partNumber, Integer modelYearFrom, Integer modelYearTo) {
        // TODO: Implement component-based VIN lookup
        // This would involve querying VehicleComponent entities
        return new ArrayList<>();
    }

    @Override
    public boolean isVehicleAffectedByRecall(String vin, String recallDetails) {
        // TODO: Implement recall check
        return false;
    }

    @Override
    public List<VehicleResponse> getVehiclesServicingAtCenter(Long serviceCenterId) {
        // TODO: Implement service center vehicle lookup
        return new ArrayList<>();
    }

    @Override
    public boolean assignVehicleToServiceCenter(String vin, Long serviceCenterId) {
        // TODO: Implement vehicle assignment to service center
        return true;
    }

    @Override
    public List<VehicleResponse> getVehicleServiceHistory(String vin) {
        // TODO: Implement service history lookup
        return new ArrayList<>();
    }

    @Override
    public boolean updateCustomerInfo(String vin, Long customerId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setCustomerId(customerId);
        vehicleRepository.save(vehicle);

        return true;
    }

    @Override
    public VehicleDetailResponse getVehicleWithCustomerInfo(String vin) {
        return getVehicleByVin(vin);
    }

    @Override
    public Long getVehicleCountByOem(Long oemId) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .filter(vehicle -> oemId.equals(vehicle.getOemId()))
                .count();
    }

    @Override
    public Long getVehicleCountByModel(String model, Long oemId) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .filter(vehicle -> model.equals(vehicle.getModel()) && oemId.equals(vehicle.getOemId()))
                .count();
    }

    @Override
    public List<VehicleResponse> getRecentlyRegisteredVehicles(int days, Long oemId) {
        OffsetDateTime cutoffDate = OffsetDateTime.now().minusDays(days);
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .filter(vehicle -> oemId.equals(vehicle.getOemId()) &&
                        vehicle.getCreatedAt().isAfter(cutoffDate))
                .map(this::convertToVehicleResponse)
                .toList();
    }

    @Override
    public boolean syncVehicleWithExternalSystem(String vin, String externalSystemId) {
        // TODO: Implement external system sync
        return true;
    }

    @Override
    public VehicleDetailResponse getVehicleByExternalId(String externalId) {
        // TODO: Implement external ID lookup
        throw new RuntimeException("External ID lookup not implemented");
    }

    @Override
    public boolean markVehicleAsRecalled(String vin, String recallDetails) {
        // TODO: Implement recall marking
        return true;
    }

    @Override
    public boolean markVehicleAsRepaired(String vin, Long claimId) {
        // TODO: Implement repair marking
        return true;
    }

    @Override
    public List<VehicleResponse> getVehiclesByStatus(String status, Long oemId) {
        // TODO: Implement status-based filtering
        return new ArrayList<>();
    }

    // Helper methods
    private VehicleResponse convertToVehicleResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setVin(vehicle.getVin());
        response.setModel(vehicle.getModel());
        response.setModelYear(vehicle.getModelYear());
        response.setCustomerId(vehicle.getCustomerId());
        response.setCreatedAt(vehicle.getCreatedAt());
        response.setOemId(vehicle.getOemId());
        response.setOemName("OEM " + vehicle.getOemId()); // TODO: Get real OEM name

        // Set vehicle data
        VehicleData vehicleData = vehicle.getVehicleData();
        if (vehicleData != null) {
            response.setVariant(vehicleData.getVariant());
            response.setOdometerKm(vehicleData.getOdometerKm());
        }

        // Set warranty info
        WarrantyInfo warrantyInfo = vehicle.getWarrantyInfo();
        if (warrantyInfo != null) {
            response.setWarrantyStartDate(warrantyInfo.getStartDate());
            response.setWarrantyEndDate(warrantyInfo.getEndDate());
            response.setWarrantyKmLimit(warrantyInfo.getKmLimit());
        }

        return response;
    }

    private VehicleDetailResponse convertToVehicleDetailResponse(Vehicle vehicle) {
        VehicleDetailResponse response = new VehicleDetailResponse();
        response.setVehicleId(vehicle.getId());
        response.setVin(vehicle.getVin());
        response.setModel(vehicle.getModel());
        response.setModelYear(vehicle.getModelYear());

        // Set vehicle data
        VehicleData vehicleData = vehicle.getVehicleData();
        if (vehicleData != null) {
            response.setVariant(vehicleData.getVariant());
            response.setCurrentOdometerKm(vehicleData.getOdometerKm());
        }

        // Set warranty info
        WarrantyInfo warrantyInfo = vehicle.getWarrantyInfo();
        if (warrantyInfo != null) {
            response.setWarrantyStartDate(warrantyInfo.getStartDate());
            response.setWarrantyEndDate(warrantyInfo.getEndDate());
            response.setWarrantyStatus(checkWarrantyStatus(vehicle.getVin()) ? "Active" : "Expired");
        }

        // Set customer info
        if (vehicle.getCustomerId() != null) {
            VehicleDetailResponse.CustomerBasicInfo customerInfo = new VehicleDetailResponse.CustomerBasicInfo();
            customerInfo.setFullName("Customer " + vehicle.getCustomerId()); // TODO: Get real customer name
            response.setCustomer(customerInfo);
        }

        // Set empty lists for now
        response.setComponents(new ArrayList<>());
        response.setServiceHistory(new ArrayList<>());
        response.setWarrantyClaims(new ArrayList<>());

        return response;
    }
}
