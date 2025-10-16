package org.example.service.Serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.models.core.ServiceCenter;
import org.example.models.dto.request.ServiceCenterCreateRequest;
import org.example.models.dto.response.DashboardResponse;
import org.example.models.dto.response.ServiceCenterResponse;
import org.example.repository.IRepository.IServiceCenterRepository;
import org.example.service.IService.IServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServiceCenterService implements IServiceCenterService {

    @Autowired
    private IServiceCenterRepository serviceCenterRepository;

    @Override
    public ServiceCenterResponse createServiceCenter(ServiceCenterCreateRequest request) {
        // Validate input
        if (request.getCode() == null || request.getName() == null) {
            throw new IllegalArgumentException("Code and name are required");
        }

        // Create service center entity
        ServiceCenter serviceCenter = new ServiceCenter();
        serviceCenter.setCode(request.getCode());
        serviceCenter.setName(request.getName());
        serviceCenter.setRegion(request.getRegion());
        serviceCenter.setOemId(request.getOemId());
        serviceCenter.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);
        serviceCenter.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        // Set contact_info JSON string
        org.example.models.json.ContactInfo contactInfo = new org.example.models.json.ContactInfo();
        contactInfo.setEmail(request.getEmail());
        contactInfo.setPhone(request.getPhone());
        contactInfo.setAddress(request.getAddress());
        contactInfo.setContactPerson(request.getContactPerson());
        contactInfo.setLicense(request.getLicense());
        try {
            ObjectMapper om = new ObjectMapper();
            serviceCenter.setContact_info(om.writeValueAsString(contactInfo));
        } catch (Exception ignored) {
        }

        // Save service center
        ServiceCenter savedServiceCenter = serviceCenterRepository.save(serviceCenter);

        // Convert to response
        return convertToServiceCenterResponse(savedServiceCenter);
    }

    @Override
    public ServiceCenterResponse updateServiceCenter(Long serviceCenterId, ServiceCenterCreateRequest request) {
        Optional<ServiceCenter> serviceCenterOpt = serviceCenterRepository.findById(serviceCenterId);
        if (serviceCenterOpt.isEmpty()) {
            throw new RuntimeException("Service center with ID " + serviceCenterId + " not found");
        }

        ServiceCenter serviceCenter = serviceCenterOpt.get();

        // Update basic info
        if (request.getName() != null) {
            serviceCenter.setName(request.getName());
        }
        if (request.getRegion() != null) {
            serviceCenter.setRegion(request.getRegion());
        }
        if (request.getOemId() != null) {
            serviceCenter.setOemId(request.getOemId());
        }
        if (request.getActive() != null) {
            serviceCenter.setActive(request.getActive());
        }
        if (request.getStatus() != null) {
            serviceCenter.setStatus(request.getStatus());
        }

        // Update contact_info JSON string
        org.example.models.json.ContactInfo contactInfo = null;
        try {
            String json = serviceCenter.getContact_info();
            if (json != null) {
                ObjectMapper om = new ObjectMapper();
                contactInfo = om.readValue(json, org.example.models.json.ContactInfo.class);
            }
        } catch (Exception ignored) {
        }
        if (contactInfo == null) {
            contactInfo = new org.example.models.json.ContactInfo();
        }
        if (request.getEmail() != null)
            contactInfo.setEmail(request.getEmail());
        if (request.getPhone() != null)
            contactInfo.setPhone(request.getPhone());
        if (request.getAddress() != null)
            contactInfo.setAddress(request.getAddress());
        if (request.getContactPerson() != null)
            contactInfo.setContactPerson(request.getContactPerson());
        if (request.getLicense() != null)
            contactInfo.setLicense(request.getLicense());
        try {
            ObjectMapper om = new ObjectMapper();
            serviceCenter.setContact_info(om.writeValueAsString(contactInfo));
        } catch (Exception ignored) {
        }

        // Save updated service center
        ServiceCenter savedServiceCenter = serviceCenterRepository.save(serviceCenter);
        return convertToServiceCenterResponse(savedServiceCenter);
    }

    @Override
    public ServiceCenterResponse getServiceCenterById(Long serviceCenterId) {
        Optional<ServiceCenter> serviceCenterOpt = serviceCenterRepository.findById(serviceCenterId);
        if (serviceCenterOpt.isEmpty()) {
            throw new RuntimeException("Service center with ID " + serviceCenterId + " not found");
        }

        return convertToServiceCenterResponse(serviceCenterOpt.get());
    }

    @Override
    public ServiceCenterResponse getServiceCenterByCode(String code) {
        // TODO: Implement findByCode method in repository
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();
        Optional<ServiceCenter> serviceCenterOpt = serviceCenters.stream()
                .filter(sc -> code.equals(sc.getCode()))
                .findFirst();

        if (serviceCenterOpt.isEmpty()) {
            throw new RuntimeException("Service center with code " + code + " not found");
        }

        return convertToServiceCenterResponse(serviceCenterOpt.get());
    }

    @Override
    public boolean activateServiceCenter(Long serviceCenterId) {
        Optional<ServiceCenter> serviceCenterOpt = serviceCenterRepository.findById(serviceCenterId);
        if (serviceCenterOpt.isEmpty())
            return false;
        ServiceCenter sc = serviceCenterOpt.get();
        sc.setActive(true);
        sc.setStatus("ACTIVE");
        serviceCenterRepository.save(sc);
        return true;
    }

    @Override
    public boolean deactivateServiceCenter(Long serviceCenterId, String reason) {
        Optional<ServiceCenter> serviceCenterOpt = serviceCenterRepository.findById(serviceCenterId);
        if (serviceCenterOpt.isEmpty())
            return false;
        ServiceCenter sc = serviceCenterOpt.get();
        sc.setActive(false);
        sc.setStatus("INACTIVE");
        serviceCenterRepository.save(sc);
        return true;
    }

    @Override
    public boolean addOemPartnership(Long serviceCenterId, Long oemId) {
        // TODO: Implement OEM partnership management
        // This would require a separate table to track OEM-ServiceCenter relationships
        return true;
    }

    @Override
    public boolean removeOemPartnership(Long serviceCenterId, Long oemId) {
        // TODO: Implement OEM partnership removal
        return true;
    }

    @Override
    public List<ServiceCenterResponse> getServiceCentersByOem(Long oemId) {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findByOemId(oemId);
        return serviceCenters.stream()
                .map(this::convertToServiceCenterResponse)
                .toList();
    }

    @Override
    public List<Long> getOemPartnerships(Long serviceCenterId) {
        // TODO: Implement OEM partnership lookup
        return new ArrayList<>();
    }

    @Override
    public boolean canServiceOemVehicles(Long serviceCenterId, Long oemId) {
        // TODO: Implement OEM service capability check
        return true;
    }

    @Override
    public List<ServiceCenterResponse> getServiceCentersByRegion(String region) {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();
        return serviceCenters.stream()
                .filter(sc -> region.equals(sc.getRegion()))
                .map(this::convertToServiceCenterResponse)
                .toList();
    }

    @Override
    public List<ServiceCenterResponse> getServiceCentersByCity(String city) {
        // TODO: Implement city-based lookup
        return new ArrayList<>();
    }

    @Override
    public ServiceCenterResponse findNearestServiceCenter(String location, Long oemId) {
        // TODO: Implement nearest service center lookup
        throw new RuntimeException("Nearest service center lookup not implemented");
    }

    @Override
    public List<ServiceCenterResponse> findServiceCentersInRadius(String location, double radiusKm, Long oemId) {
        // TODO: Implement radius-based lookup
        return new ArrayList<>();
    }

    @Override
    public boolean updateServiceCapacity(Long serviceCenterId, Integer maxConcurrentVehicles, Integer maxTechnicians) {
        // TODO: Implement capacity management
        return true;
    }

    @Override
    public Integer getCurrentWorkload(Long serviceCenterId) {
        // TODO: Implement workload calculation
        return 0;
    }

    @Override
    public Integer getAvailableCapacity(Long serviceCenterId) {
        // TODO: Implement capacity calculation
        return 10;
    }

    @Override
    public List<ServiceCenterResponse> getServiceCentersWithCapacity(Long oemId) {
        // TODO: Implement capacity-based filtering
        return new ArrayList<>();
    }

    @Override
    public boolean addStaffMember(Long serviceCenterId, Long userId) {
        // TODO: Implement staff management
        return true;
    }

    @Override
    public boolean removeStaffMember(Long serviceCenterId, Long userId) {
        // TODO: Implement staff removal
        return true;
    }

    @Override
    public List<Long> getServiceCenterStaff(Long serviceCenterId) {
        // TODO: Implement staff lookup
        return new ArrayList<>();
    }

    @Override
    public List<Long> getServiceCenterTechnicians(Long serviceCenterId) {
        // TODO: Implement technician lookup
        return new ArrayList<>();
    }

    @Override
    public Integer getAvailableTechnicianCount(Long serviceCenterId) {
        // TODO: Implement technician count
        return 5;
    }

    @Override
    public boolean recordRecallCompletion(Long serviceCenterId, String recallDetails, String vin, String notes) {
        // TODO: Implement recall completion recording
        return true;
    }

    @Override
    public List<Long> getPendingWarrantyClaims(Long serviceCenterId) {
        // TODO: Implement pending claims lookup
        return new ArrayList<>();
    }

    @Override
    public List<Long> getCompletedWarrantyClaims(Long serviceCenterId, String dateRange) {
        // TODO: Implement completed claims lookup
        return new ArrayList<>();
    }

    @Override
    public boolean updateClaimProcessingCapability(Long serviceCenterId, List<String> supportedClaimTypes) {
        // TODO: Implement claim processing capability management
        return true;
    }

    @Override
    public boolean updatePartsInventory(Long serviceCenterId, String partNumber, Integer quantity) {
        // TODO: Implement parts inventory management
        return true;
    }

    @Override
    public Integer getPartsInventory(Long serviceCenterId, String partNumber) {
        // TODO: Implement parts inventory lookup
        return 0;
    }

    @Override
    public List<String> getLowStockParts(Long serviceCenterId) {
        // TODO: Implement low stock parts lookup
        return new ArrayList<>();
    }

    @Override
    public boolean requestPartsFromOem(Long serviceCenterId, Long oemId, List<String> partNumbers) {
        // TODO: Implement parts request management
        return true;
    }

    @Override
    public DashboardResponse getServiceCenterDashboard(Long serviceCenterId) {
        // TODO: Implement dashboard data
        DashboardResponse dashboard = new DashboardResponse();
        return dashboard;
    }

    @Override
    public Double getAverageRepairTime(Long serviceCenterId) {
        // TODO: Implement repair time calculation
        return 2.5;
    }

    @Override
    public Double getCustomerSatisfactionRating(Long serviceCenterId) {
        // TODO: Implement satisfaction rating calculation
        return 4.2;
    }

    @Override
    public Integer getMonthlyClaimCount(Long serviceCenterId) {
        // TODO: Implement monthly claim count
        return 15;
    }

    @Override
    public List<ServiceCenterResponse> searchServiceCenters(String keyword, String region, Long oemId) {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();

        // Apply filters
        if (keyword != null && !keyword.trim().isEmpty()) {
            serviceCenters = serviceCenters.stream()
                    .filter(sc -> sc.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                            sc.getCode().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }

        if (region != null && !region.trim().isEmpty()) {
            serviceCenters = serviceCenters.stream()
                    .filter(sc -> region.equals(sc.getRegion()))
                    .toList();
        }

        return serviceCenters.stream().map(this::convertToServiceCenterResponse).toList();
    }

    @Override
    public List<ServiceCenterResponse> getActiveServiceCenters(Long oemId) {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findByIsActive(true);
        return serviceCenters.stream().map(this::convertToServiceCenterResponse).toList();
    }

    @Override
    public List<ServiceCenterResponse> getServiceCentersByCapability(String capability, Long oemId) {
        // TODO: Implement capability-based filtering
        return new ArrayList<>();
    }

    @Override
    public boolean updateCertifications(Long serviceCenterId, List<String> certifications) {
        // TODO: Implement certification management
        return true;
    }

    @Override
    public List<String> getServiceCenterCertifications(Long serviceCenterId) {
        // TODO: Implement certification lookup
        return new ArrayList<>();
    }

    @Override
    public boolean recordQualityAudit(Long serviceCenterId, String auditResult, String notes) {
        // TODO: Implement quality audit recording
        return true;
    }

    @Override
    public List<String> getQualityAuditHistory(Long serviceCenterId) {
        // TODO: Implement quality audit history
        return new ArrayList<>();
    }

    @Override
    public boolean sendNotificationToServiceCenter(Long serviceCenterId, String message, String type) {
        // TODO: Implement notification sending
        return true;
    }

    @Override
    public boolean updateContactInformation(Long serviceCenterId, String email, String phone, String address) {
        Optional<ServiceCenter> serviceCenterOpt = serviceCenterRepository.findById(serviceCenterId);
        if (serviceCenterOpt.isEmpty()) {
            return false;
        }

        ServiceCenter serviceCenter = serviceCenterOpt.get();
        org.example.models.json.ContactInfo contactInfo = null;
        try {
            String json = serviceCenter.getContact_info();
            if (json != null) {
                ObjectMapper om = new ObjectMapper();
                contactInfo = om.readValue(json, org.example.models.json.ContactInfo.class);
            }
        } catch (Exception ignored) {
        }
        if (contactInfo == null)
            contactInfo = new org.example.models.json.ContactInfo();
        if (email != null)
            contactInfo.setEmail(email);
        if (phone != null)
            contactInfo.setPhone(phone);
        if (address != null)
            contactInfo.setAddress(address);
        try {
            ObjectMapper om = new ObjectMapper();
            serviceCenter.setContact_info(om.writeValueAsString(contactInfo));
        } catch (Exception ignored) {
        }
        serviceCenterRepository.save(serviceCenter);
        return true;
    }

    @Override
    public boolean syncWithExternalSystem(Long serviceCenterId, String externalSystemId) {
        // TODO: Implement external system sync
        return true;
    }

    @Override
    public ServiceCenterResponse getServiceCenterByExternalId(String externalId) {
        // TODO: Implement external ID lookup
        throw new RuntimeException("External ID lookup not implemented");
    }

    // Helper methods
    private ServiceCenterResponse convertToServiceCenterResponse(ServiceCenter serviceCenter) {
        ServiceCenterResponse response = new ServiceCenterResponse();
        response.setId(serviceCenter.getId());
        response.setCode(serviceCenter.getCode());
        response.setName(serviceCenter.getName());
        response.setRegion(serviceCenter.getRegion());
        response.setOemId(serviceCenter.getOemId());
        response.setActive(serviceCenter.getActive());
        response.setStatus(serviceCenter.getStatus());

        // contact_info JSON -> Map
        try {
            String json = serviceCenter.getContact_info();
            if (json != null && !json.isBlank()) {
                ObjectMapper om = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, Object> ci = om.readValue(json, Map.class);
                response.setContactInfo(ci);
            }
        } catch (Exception ignored) {
        }

        return response;
    }

    @Override
    public boolean deleteServiceCenter(Long serviceCenterId) {
        try {
            Optional<ServiceCenter> serviceCenterOpt = serviceCenterRepository.findById(serviceCenterId);
            if (serviceCenterOpt.isPresent()) {
                serviceCenterRepository.deleteById(serviceCenterId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateServiceCenterStatus(Long serviceCenterId, boolean active) {
        try {
            if (active) {
                return activateServiceCenter(serviceCenterId);
            } else {
                return deactivateServiceCenter(serviceCenterId, "Status updated via API");
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ServiceCenterResponse> getAllServiceCenters(int limit, int offset) {
        try {
            return serviceCenterRepository.findAll().stream()
                    .skip(offset)
                    .limit(limit)
                    .map(this::convertToServiceCenterResponse)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
