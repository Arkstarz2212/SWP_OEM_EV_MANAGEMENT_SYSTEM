package org.example.service.IService;

import java.util.List;

import org.example.models.dto.request.OemManufacturerCreateRequest;
import org.example.models.dto.response.OemManufacturerResponse;

/**
 * Service interface for OEM Manufacturer management
 * Handles business logic for OEM manufacturer operations
 */
public interface IOemManufacturerService {
    // CRUD Operations
    OemManufacturerResponse createOemManufacturer(OemManufacturerCreateRequest request);

    OemManufacturerResponse getOemManufacturerById(Long id);

    OemManufacturerResponse getOemManufacturerByCode(String code);

    OemManufacturerResponse updateOemManufacturer(Long id, OemManufacturerCreateRequest request);

    boolean deleteOemManufacturer(Long id);

    // List and Search Operations
    List<OemManufacturerResponse> getAllOemManufacturers(int limit, int offset);

    List<OemManufacturerResponse> searchOemManufacturers(String keyword, int limit, int offset);

    List<OemManufacturerResponse> getOemManufacturersByServiceCenter(Long serviceCenterId, int limit, int offset);

    // Validation Operations
    boolean existsByCode(String code);

    boolean existsByName(String name);

    boolean existsById(Long id);

    // Statistics
    Long countOemManufacturers();

    Long countOemManufacturersByServiceCenter(Long serviceCenterId);
}
