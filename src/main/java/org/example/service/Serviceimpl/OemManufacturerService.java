package org.example.service.Serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.models.core.OemManufacturer;
import org.example.models.dto.request.OemManufacturerCreateRequest;
import org.example.models.dto.response.OemManufacturerResponse;
import org.example.repository.IRepository.IOemManufacturerRepository;
import org.example.service.IService.IOemManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for OEM Manufacturer management
 * Handles business logic for OEM manufacturer operations
 */
@Service
@Transactional
public class OemManufacturerService implements IOemManufacturerService {

    @Autowired
    private IOemManufacturerRepository oemManufacturerRepository;

    @Override
    public OemManufacturerResponse createOemManufacturer(OemManufacturerCreateRequest request) {
        // Validate code uniqueness
        if (oemManufacturerRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("OEM code already exists: " + request.getCode());
        }

        // Validate name uniqueness
        if (oemManufacturerRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("OEM name already exists: " + request.getName());
        }

        // Create new OEM manufacturer
        OemManufacturer oem = new OemManufacturer();
        oem.setCode(request.getCode());
        oem.setName(request.getName());
        oem.setContact(request.getContact());

        OemManufacturer savedOem = oemManufacturerRepository.save(oem);
        return convertToResponse(savedOem);
    }

    @Override
    public OemManufacturerResponse getOemManufacturerById(Long id) {
        Optional<OemManufacturer> oem = oemManufacturerRepository.findById(id);
        if (oem.isPresent()) {
            return convertToResponse(oem.get());
        }
        throw new IllegalArgumentException("OEM manufacturer not found with ID: " + id);
    }

    @Override
    public OemManufacturerResponse getOemManufacturerByCode(String code) {
        Optional<OemManufacturer> oem = oemManufacturerRepository.findByCode(code);
        if (oem.isPresent()) {
            return convertToResponse(oem.get());
        }
        throw new IllegalArgumentException("OEM manufacturer not found with code: " + code);
    }

    @Override
    public OemManufacturerResponse updateOemManufacturer(Long id, OemManufacturerCreateRequest request) {
        Optional<OemManufacturer> existingOemOpt = oemManufacturerRepository.findById(id);
        if (existingOemOpt.isEmpty()) {
            throw new IllegalArgumentException("OEM manufacturer not found with ID: " + id);
        }

        OemManufacturer existingOem = existingOemOpt.get();

        // Check if code is being changed and if new code already exists
        if (!existingOem.getCode().equals(request.getCode()) &&
                oemManufacturerRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("OEM code already exists: " + request.getCode());
        }

        // Check if name is being changed and if new name already exists
        if (!existingOem.getName().equals(request.getName()) &&
                oemManufacturerRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("OEM name already exists: " + request.getName());
        }

        // Update OEM manufacturer details
        existingOem.setCode(request.getCode());
        existingOem.setName(request.getName());
        existingOem.setContact(request.getContact());

        OemManufacturer updatedOem = oemManufacturerRepository.save(existingOem);
        return convertToResponse(updatedOem);
    }

    @Override
    public boolean deleteOemManufacturer(Long id) {
        if (!oemManufacturerRepository.existsById(id)) {
            throw new IllegalArgumentException("OEM manufacturer not found with ID: " + id);
        }

        // Check if OEM can be deleted (business rules)
        if (!canDeleteOem(id)) {
            throw new IllegalStateException(
                    "Cannot delete OEM: it may be referenced by service centers or warranty policies");
        }

        oemManufacturerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<OemManufacturerResponse> getAllOemManufacturers(int limit, int offset) {
        List<OemManufacturer> oems = oemManufacturerRepository.findAll();
        return oems.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OemManufacturerResponse> searchOemManufacturers(String keyword, int limit, int offset) {
        List<OemManufacturer> oems = oemManufacturerRepository.searchByContactInfo(keyword);
        // Also search by name and code
        oems.addAll(oemManufacturerRepository.findByNameContainingIgnoreCase(keyword));
        oems.addAll(oemManufacturerRepository.findByCodeContainingIgnoreCase(keyword));

        // Remove duplicates and apply pagination
        return oems.stream()
                .distinct()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OemManufacturerResponse> getOemManufacturersByServiceCenter(Long serviceCenterId, int limit,
            int offset) {
        List<OemManufacturer> oems = oemManufacturerRepository.findByServiceCenterIds(List.of(serviceCenterId));
        return oems.stream()
                .skip(offset)
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCode(String code) {
        return oemManufacturerRepository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return oemManufacturerRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return oemManufacturerRepository.existsById(id);
    }

    @Override
    public Long countOemManufacturers() {
        return (long) oemManufacturerRepository.findAll().size();
    }

    @Override
    public Long countOemManufacturersByServiceCenter(Long serviceCenterId) {
        return (long) oemManufacturerRepository.findByServiceCenterIds(List.of(serviceCenterId)).size();
    }

    /**
     * Convert OemManufacturer entity to OemManufacturerResponse DTO
     */
    private OemManufacturerResponse convertToResponse(OemManufacturer oem) {
        OemManufacturerResponse response = new OemManufacturerResponse();
        response.setId(oem.getId());
        response.setCode(oem.getCode());
        response.setName(oem.getName());
        response.setContact(oem.getContact());
        return response;
    }

    /**
     * Check if OEM can be deleted based on business rules
     */
    private boolean canDeleteOem(Long oemId) {
        // Add business rules here (e.g., check if referenced by service centers,
        // warranty policies, etc.)
        // For now, allow deletion
        return oemId != null;
    }
}
