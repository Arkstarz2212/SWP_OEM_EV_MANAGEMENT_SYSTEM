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
import org.example.models.json.CustomerInfo;
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
        vehicle.setStatus("active"); // Set default status
        vehicle.setCreatedAt(OffsetDateTime.now());

        // Set customer info directly from request
        if (request.getCustomerFullName() != null || request.getCustomerPhoneNumber() != null
                || request.getCustomerEmail() != null) {
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setFullName(request.getCustomerFullName());
            customerInfo.setPhoneNumber(request.getCustomerPhoneNumber());
            customerInfo.setEmail(request.getCustomerEmail());
            customerInfo.setAddress(request.getCustomerAddress());
            customerInfo.setCity(request.getCustomerCity());
            customerInfo.setProvince(request.getCustomerProvince());
            customerInfo.setPostalCode(request.getCustomerPostalCode());
            vehicle.setCustomerInfo(customerInfo);
        }

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

        if (request.getImage() != null) {
            vehicle.setImage(request.getImage());
        }

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

        // Update customer info if provided
        if (request.getCustomerFullName() != null || request.getCustomerPhoneNumber() != null
                || request.getCustomerEmail() != null) {
            CustomerInfo customerInfo = vehicle.getCustomerInfo();
            if (customerInfo == null) {
                customerInfo = new CustomerInfo();
            }

            if (request.getCustomerFullName() != null) {
                customerInfo.setFullName(request.getCustomerFullName());
            }
            if (request.getCustomerPhoneNumber() != null) {
                customerInfo.setPhoneNumber(request.getCustomerPhoneNumber());
            }
            if (request.getCustomerEmail() != null) {
                customerInfo.setEmail(request.getCustomerEmail());
            }
            if (request.getCustomerAddress() != null) {
                customerInfo.setAddress(request.getCustomerAddress());
            }
            if (request.getCustomerCity() != null) {
                customerInfo.setCity(request.getCustomerCity());
            }
            if (request.getCustomerProvince() != null) {
                customerInfo.setProvince(request.getCustomerProvince());
            }
            if (request.getCustomerPostalCode() != null) {
                customerInfo.setPostalCode(request.getCustomerPostalCode());
            }

            vehicle.setCustomerInfo(customerInfo);
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

        if (request.getImage() != null) {
            vehicle.setImage(request.getImage());
        }

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

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setStatus("inactive");
        vehicleRepository.save(vehicle);
        return true;
    }

    @Override
    public boolean softDeleteVehicle(String vin) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setStatus("deleted");
        vehicleRepository.save(vehicle);
        return true;
    }

    @Override
    public boolean restoreVehicle(String vin) {
        // First check if vehicle exists (including deleted ones)
        List<Vehicle> vehicles = vehicleRepository.findByVinIn(List.of(vin));
        if (vehicles.isEmpty()) {
            return false;
        }

        Vehicle vehicle = vehicles.get(0);
        if (!"deleted".equals(vehicle.getStatus())) {
            return false; // Vehicle is not deleted
        }

        vehicle.setStatus("active");
        vehicleRepository.save(vehicle);
        return true;
    }

    @Override
    public List<VehicleResponse> getDeletedVehicles() {
        List<Vehicle> deletedVehicles = vehicleRepository.findDeletedVehicles();
        return deletedVehicles.stream()
                .map(this::convertToVehicleResponse)
                .toList();
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
        // This method is deprecated since we no longer use customerId mapping
        // Return empty list as customer info is now stored directly in vehicles
        return new ArrayList<>();
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
        // This method is deprecated since we no longer use customerId mapping
        // Customer info should be updated via updateVehicleInfo method
        return false;
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
        List<Vehicle> vehicles = vehicleRepository.findByStatus(status);
        return vehicles.stream()
                .filter(vehicle -> oemId == null || oemId.equals(vehicle.getOemId()))
                .map(this::convertToVehicleResponse)
                .toList();
    }

    // Helper methods
    private VehicleResponse convertToVehicleResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setVin(vehicle.getVin());
        response.setModel(vehicle.getModel());
        response.setModelYear(vehicle.getModelYear());
        response.setCreatedAt(vehicle.getCreatedAt());
        response.setOemId(vehicle.getOemId());
        response.setOemName("OEM " + vehicle.getOemId()); // TODO: Get real OEM name

        // Set customer name from customer info
        CustomerInfo customerInfo = vehicle.getCustomerInfo();
        if (customerInfo != null && customerInfo.getFullName() != null) {
            response.setCustomerName(customerInfo.getFullName());
        }

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

        // Set status
        response.setStatus(vehicle.getStatus());

        return response;
    }

    private VehicleDetailResponse convertToVehicleDetailResponse(Vehicle vehicle) {
        VehicleDetailResponse response = new VehicleDetailResponse();
        response.setVehicleId(vehicle.getId());
        response.setVin(vehicle.getVin());
        response.setModel(vehicle.getModel());
        response.setModelYear(vehicle.getModelYear());
        response.setImage(vehicle.getImage());

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

        // Set vehicle status
        response.setStatus(vehicle.getStatus());

        // Set customer info from direct customer info
        CustomerInfo customerInfo = vehicle.getCustomerInfo();
        if (customerInfo != null && customerInfo.getFullName() != null) {
            VehicleDetailResponse.CustomerBasicInfo customerBasicInfo = new VehicleDetailResponse.CustomerBasicInfo();
            customerBasicInfo.setFullName(customerInfo.getFullName());
            customerBasicInfo.setPhoneNumber(customerInfo.getPhoneNumber());
            customerBasicInfo.setEmail(customerInfo.getEmail());
            customerBasicInfo.setAddress(customerInfo.getAddress());
            customerBasicInfo.setCity(customerInfo.getCity());
            customerBasicInfo.setProvince(customerInfo.getProvince());
            customerBasicInfo.setPostalCode(customerInfo.getPostalCode());
            response.setCustomer(customerBasicInfo);
        }

        // Set empty lists for now
        response.setComponents(new ArrayList<>());
        response.setServiceHistory(new ArrayList<>());
        response.setWarrantyClaims(new ArrayList<>());

        return response;
    }
}
