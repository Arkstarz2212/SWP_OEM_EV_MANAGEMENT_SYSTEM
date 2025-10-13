package org.example.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ICustomerService {
    // Customer Profile Management
    Map<String, Object> createCustomer(Map<String, Object> request);

    Map<String, Object> updateCustomer(Long customerId, Map<String, Object> request);

    Map<String, Object> getCustomerById(Long customerId);

    Map<String, Object> getCustomerByPhone(String phoneNumber);

    Map<String, Object> getCustomerByEmail(String email);

    boolean deactivateCustomer(Long customerId, String reason);

    // Vehicle Ownership Management
    boolean linkVehicleToCustomer(String vin, Long customerId);

    boolean unlinkVehicleFromCustomer(String vin, Long customerId);

    List<String> getCustomerVehicles(Long customerId);

    Map<String, Object> getVehicleOwner(String vin);

    // Contact & Communication
    boolean updateContactPreferences(Long customerId, List<String> preferredChannels);

    List<String> getContactPreferences(Long customerId);

    boolean recordCustomerInteraction(Long customerId, String interactionType, String notes, Long recordedBy);

    // Search & Discovery
    List<Map<String, Object>> searchCustomers(String searchQuery, int limit, int offset);

    List<Map<String, Object>> getCustomersByServiceCenter(Long serviceCenterId);

    // Warranty & Service History
    List<Map<String, Object>> getCustomersWithExpiringWarranties(int daysFromNow);

    boolean recordServiceVisit(Long customerId, String vin, Long serviceCenterId, LocalDate visitDate);

    // Customer Analytics
    Integer getCustomerVehicleCount(Long customerId);

    LocalDate getLastServiceDate(Long customerId);

    Double getCustomerLifetimeValue(Long customerId);
}