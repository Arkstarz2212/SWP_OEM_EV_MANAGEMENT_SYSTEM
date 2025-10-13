package org.example.service.IService;

import java.util.List;

import org.example.models.dto.request.VehicleComponentRegisterRequest;
import org.example.models.dto.request.VehicleRegisterRequest;
import org.example.models.dto.request.VehicleSearchRequest;
import org.example.models.dto.response.VehicleDetailResponse;
import org.example.models.dto.response.VehicleResponse;

public interface IVehicleService {
    // Core Vehicle Registration and Management
    VehicleDetailResponse registerVehicleByVin(VehicleRegisterRequest request);

    VehicleDetailResponse attachComponentSerials(String vin, VehicleComponentRegisterRequest request);

    VehicleDetailResponse updateVehicleInfo(String vin, VehicleRegisterRequest request);

    boolean deactivateVehicle(String vin, String reason);

    // Vehicle Lookup and Search
    VehicleDetailResponse getVehicleByVin(String vin);

    VehicleDetailResponse getVehicleById(Long vehicleId);

    List<VehicleResponse> getVehiclesByOem(Long oemId);

    List<VehicleResponse> getVehiclesByCustomer(Long customerId);

    List<VehicleResponse> searchVehicles(VehicleSearchRequest request);

    // Vehicle Data Management
    boolean updateOdometer(String vin, Integer newOdometerKm, String source);

    boolean updateVehicleLocation(String vin, String location);

    boolean recordServiceHistory(String vin, String serviceType, Long serviceCenterId, String notes);

    // Warranty Information
    VehicleDetailResponse getVehicleWarrantyInfo(String vin);

    boolean updateWarrantyInfo(String vin, String warrantyType, String startDate, String endDate);

    boolean checkWarrantyStatus(String vin);

    List<VehicleResponse> getVehiclesWithExpiringWarranty(int daysFromNow);

    // Recall Support
    List<String> getVehiclesByModelAndYear(String model, Integer modelYear, Long oemId);

    List<String> getVinsByComponentCriteria(String partNumber, Integer modelYearFrom, Integer modelYearTo);

    boolean isVehicleAffectedByRecall(String vin, String recallDetails);

    // Service Center Operations
    List<VehicleResponse> getVehiclesServicingAtCenter(Long serviceCenterId);

    boolean assignVehicleToServiceCenter(String vin, Long serviceCenterId);

    List<VehicleResponse> getVehicleServiceHistory(String vin);

    // Customer Information
    boolean updateCustomerInfo(String vin, Long customerId);

    VehicleDetailResponse getVehicleWithCustomerInfo(String vin);

    // Analytics and Reporting
    Long getVehicleCountByOem(Long oemId);

    Long getVehicleCountByModel(String model, Long oemId);

    List<VehicleResponse> getRecentlyRegisteredVehicles(int days, Long oemId);

    // Integration Support
    boolean syncVehicleWithExternalSystem(String vin, String externalSystemId);

    VehicleDetailResponse getVehicleByExternalId(String externalId);

    // Vehicle Status Management
    boolean markVehicleAsRecalled(String vin, String recallDetails);

    boolean markVehicleAsRepaired(String vin, Long claimId);

    List<VehicleResponse> getVehiclesByStatus(String status, Long oemId);
}
