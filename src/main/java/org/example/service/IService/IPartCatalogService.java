package org.example.service.IService;

import java.util.List;

import org.example.models.dto.request.PartCatalogCreateRequest;
import org.example.models.dto.response.PartCatalogResponse;

public interface IPartCatalogService {
    // CRUD Operations
    PartCatalogResponse createPart(PartCatalogCreateRequest request);

    PartCatalogResponse getPartById(Long partId);

    PartCatalogResponse updatePart(Long partId, PartCatalogCreateRequest request);

    boolean softDeletePart(Long partId);

    boolean hardDeletePart(Long partId);

    // Search & Pagination
    List<PartCatalogResponse> searchParts(String keyword, String category, String manufacturer, int limit, int offset);

    List<PartCatalogResponse> getAllParts(int limit, int offset);

    // Business Operations
    PartCatalogResponse getPartByPartNumber(String partNumber);

    List<PartCatalogResponse> getPartsByCategory(String category);

    List<PartCatalogResponse> getPartsByVehicleModel(String vehicleModel);

    boolean activatePart(Long partId);

    boolean deactivatePart(Long partId);

    boolean deletePart(Long partId);

    boolean updatePartStatus(Long partId, boolean active);
}
