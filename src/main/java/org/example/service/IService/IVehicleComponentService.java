package org.example.service.IService;

import java.util.List;

import org.example.models.dto.response.VehicleDetailResponse;

public interface IVehicleComponentService {
        VehicleDetailResponse registerComponentsByVin(String vin,
                        List<Object> components);

        VehicleDetailResponse getComponentById(Long componentId);

        VehicleDetailResponse updateComponent(Long componentId, Object request);

        boolean deactivateComponent(Long componentId, String reason);

        // VIN-to-Serial Mapping (Core AOEM Functionality)
        List<Object> getComponentsByVin(String vin);

        Object getComponentBySerialNumber(String serialNumber);

        List<Object> getActiveComponentsByVehicle(Long vehicleId);

        // Component Replacement & Warranty History
        boolean replaceComponent(Long oldComponentId,
                        Object newComponent,
                        String replacementReason, Long performedByUserId);

        List<Object> getComponentReplacementHistory(String originalSerialNumber);

        // Warranty & Service Operations
        boolean isComponentUnderWarranty(String serialNumber);

        List<Object> getComponentsNearingWarrantyExpiry(int daysBeforeExpiry);

        // Search & Filtering
        List<Object> searchComponentsByPartNumber(String partNumber);

        List<Object> getComponentsByManufacturerAndModel(String manufacturer, String model);

        // Recall Support
        List<String> getVinsByComponentCriteria(String partNumber, String componentName,
                        Integer modelYearFrom, Integer modelYearTo);
}
