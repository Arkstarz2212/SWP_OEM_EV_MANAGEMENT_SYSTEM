package org.example.service.Serviceimpl;

import java.util.List;
import java.util.Optional;

import org.example.models.core.PartCatalog;
import org.example.models.dto.request.PartCatalogCreateRequest;
import org.example.models.dto.response.PartCatalogResponse;
import org.example.models.json.PartData;
import org.example.repository.IRepository.IPartCatalogRepository;
import org.example.service.IService.IPartCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartCatalogService implements IPartCatalogService {

    @Autowired
    private IPartCatalogRepository partRepository;

    @Override
    public PartCatalogResponse createPart(PartCatalogCreateRequest request) {
        validateCreateRequest(request);
        PartCatalog part = new PartCatalog();
        part.setOemId(request.getOemId());
        part.setPartNumber(request.getPartNumber());
        part.setName(request.getName());
        part.setCategory(request.getCategory() != null ? request.getCategory().name() : null);
        part.setIsActive(true); // Default to active

        if (request.getImage() != null) {
            part.setImage(request.getImage());
        }

        PartData data = new PartData();
        data.setDescription(request.getDescription());
        data.setVehicleModels(request.getVehicleModels());
        data.setUnitCost(request.getUnitCost());
        data.setManufacturer(request.getManufacturer());
        part.setPart_data(serializePartData(data));

        PartCatalog saved = partRepository.save(part);
        return toResponse(saved);
    }

    @Override
    public PartCatalogResponse getPartById(Long partId) {
        Optional<PartCatalog> opt = partRepository.findById(partId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Part not found");
        }
        return toResponse(opt.get());
    }

    @Override
    public PartCatalogResponse updatePart(Long partId, PartCatalogCreateRequest request) {
        Optional<PartCatalog> opt = partRepository.findById(partId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Part not found");
        }
        PartCatalog part = opt.get();
        if (request.getPartNumber() != null)
            part.setPartNumber(request.getPartNumber());
        if (request.getName() != null)
            part.setName(request.getName());
        if (request.getCategory() != null)
            part.setCategory(request.getCategory().name());

        PartData data = deserializePartData(part.getPart_data());
        if (data == null)
            data = new PartData();
        if (request.getDescription() != null)
            data.setDescription(request.getDescription());
        if (request.getVehicleModels() != null)
            data.setVehicleModels(request.getVehicleModels());
        if (request.getUnitCost() != null)
            data.setUnitCost(request.getUnitCost());
        if (request.getManufacturer() != null)
            data.setManufacturer(request.getManufacturer());
        part.setPart_data(serializePartData(data));

        PartCatalog saved = partRepository.save(part);
        return toResponse(saved);
    }

    @Override
    public boolean softDeletePart(Long partId) {
        Optional<PartCatalog> opt = partRepository.findById(partId);
        if (opt.isEmpty())
            return false;
        // No isActive flag in schema; keep record as-is (logical soft delete could be
        // implemented via app-level filtering)
        partRepository.save(opt.get());
        return true;
    }

    @Override
    public boolean hardDeletePart(Long partId) {
        Optional<PartCatalog> opt = partRepository.findById(partId);
        if (opt.isEmpty())
            return false;
        partRepository.deleteById(partId);
        return true;
    }

    @Override
    public List<PartCatalogResponse> searchParts(String keyword, String category, String manufacturer, int limit,
            int offset) {
        List<PartCatalog> parts = partRepository.searchParts(keyword != null ? keyword : "");
        return parts.stream()
                .filter(p -> category == null
                        || (p.getCategory() != null && p.getCategory().equalsIgnoreCase(category)))
                .filter(p -> manufacturer == null || matchesManufacturer(p.getPart_data(), manufacturer))
                .skip(Math.max(0, offset))
                .limit(limit > 0 ? limit : parts.size())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PartCatalogResponse> getAllParts(int limit, int offset) {
        List<PartCatalog> parts = partRepository.findAll();
        return parts.stream()
                .skip(Math.max(0, offset))
                .limit(limit > 0 ? limit : parts.size())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PartCatalogResponse getPartByPartNumber(String partNumber) {
        Optional<PartCatalog> opt = partRepository.findByPartNumber(partNumber);
        if (opt.isEmpty()) {
            throw new RuntimeException("Part not found");
        }
        return toResponse(opt.get());
    }

    @Override
    public List<PartCatalogResponse> getPartsByCategory(String category) {
        return partRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PartCatalogResponse> getPartsByVehicleModel(String vehicleModel) {
        return partRepository.findAll().stream()
                .filter(p -> containsVehicleModel(p.getPart_data(), vehicleModel))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public boolean activatePart(Long partId) {
        Optional<PartCatalog> opt = partRepository.findById(partId);
        if (opt.isEmpty())
            return false;
        PartCatalog part = opt.get();
        part.setIsActive(true);
        partRepository.save(part);
        return true;
    }

    @Override
    public boolean deactivatePart(Long partId) {
        Optional<PartCatalog> opt = partRepository.findById(partId);
        if (opt.isEmpty())
            return false;
        PartCatalog part = opt.get();
        part.setIsActive(false);
        partRepository.save(part);
        return true;
    }

    private void validateCreateRequest(PartCatalogCreateRequest request) {
        if (request.getOemId() == null || request.getPartNumber() == null || request.getName() == null
                || request.getCategory() == null) {
            throw new IllegalArgumentException("Missing required fields");
        }
    }

    private PartCatalogResponse toResponse(PartCatalog part) {
        PartCatalogResponse res = new PartCatalogResponse();
        res.setId(part.getId());
        res.setOemId(part.getOemId());
        res.setPartNumber(part.getPartNumber());
        res.setName(part.getName());
        res.setCategory(
                part.getCategory() != null ? org.example.models.enums.PartCategory.valueOf(part.getCategory()) : null);
        res.setImage(part.getImage());
        res.setIsActive(part.getIsActive());
        res.setCreatedAt(part.getCreatedAt());
        PartData pd = deserializePartData(part.getPart_data());
        if (pd != null) {
            res.setDescription(pd.getDescription());
            res.setVehicleModels(pd.getVehicleModels());
            res.setUnitCost(pd.getUnitCost());
            res.setManufacturer(pd.getManufacturer());
        }
        return res;
    }

    private String serializePartData(PartData data) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            return null;
        }
    }

    private PartData deserializePartData(String json) {
        if (json == null)
            return null;
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, PartData.class);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean matchesManufacturer(String partDataJson, String manufacturer) {
        if (manufacturer == null)
            return true;
        PartData data = deserializePartData(partDataJson);
        return data != null && data.getManufacturer() != null && manufacturer.equalsIgnoreCase(data.getManufacturer());
    }

    private boolean containsVehicleModel(String partDataJson, String vehicleModel) {
        if (vehicleModel == null)
            return true;
        PartData data = deserializePartData(partDataJson);
        return data != null && data.getVehicleModels() != null && data.getVehicleModels().contains(vehicleModel);
    }

    @Override
    public boolean deletePart(Long partId) {
        try {
            return hardDeletePart(partId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updatePartStatus(Long partId, boolean active) {
        try {
            if (active) {
                return activatePart(partId);
            } else {
                return deactivatePart(partId);
            }
        } catch (Exception e) {
            return false;
        }
    }
}
