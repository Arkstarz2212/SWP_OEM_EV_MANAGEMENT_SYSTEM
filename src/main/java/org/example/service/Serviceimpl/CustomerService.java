package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.service.IService.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    private final Map<Long, Map<String, Object>> customers = new ConcurrentHashMap<>();
    private final Map<String, Long> vehicleOwner = new ConcurrentHashMap<>();
    private long idSeq = 1L;

    @Override
    public Map<String, Object> createCustomer(Map<String, Object> request) {
        long id = idSeq++;
        Map<String, Object> c = new HashMap<>(request);
        c.put("id", id);
        c.putIfAbsent("active", true);
        customers.put(id, c);
        return c;
    }

    @Override
    public Map<String, Object> updateCustomer(Long customerId, Map<String, Object> request) {
        Map<String, Object> c = customers.get(customerId);
        if (c == null)
            throw new RuntimeException("Customer not found");
        c.putAll(request);
        return c;
    }

    @Override
    public Map<String, Object> getCustomerById(Long customerId) {
        return customers.get(customerId);
    }

    @Override
    public Map<String, Object> getCustomerByPhone(String phoneNumber) {
        return customers.values().stream()
                .filter(c -> phoneNumber.equals(c.get("phone")))
                .findFirst().orElse(null);
    }

    @Override
    public Map<String, Object> getCustomerByEmail(String email) {
        return customers.values().stream()
                .filter(c -> email.equals(c.get("email")))
                .findFirst().orElse(null);
    }

    @Override
    public boolean deactivateCustomer(Long customerId, String reason) {
        Map<String, Object> c = customers.get(customerId);
        if (c == null)
            return false;
        c.put("active", false);
        c.put("deactivationReason", reason);
        return true;
    }

    @Override
    public boolean linkVehicleToCustomer(String vin, Long customerId) {
        vehicleOwner.put(vin, customerId);
        return true;
    }

    @Override
    public boolean unlinkVehicleFromCustomer(String vin, Long customerId) {
        Long owner = vehicleOwner.get(vin);
        if (owner != null && owner.equals(customerId)) {
            vehicleOwner.remove(vin);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getCustomerVehicles(Long customerId) {
        List<String> vins = new ArrayList<>();
        for (Map.Entry<String, Long> e : vehicleOwner.entrySet()) {
            if (customerId.equals(e.getValue()))
                vins.add(e.getKey());
        }
        return vins;
    }

    @Override
    public Map<String, Object> getVehicleOwner(String vin) {
        Long cid = vehicleOwner.get(vin);
        if (cid == null)
            return null;
        return customers.get(cid);
    }

    @Override
    public boolean updateContactPreferences(Long customerId, List<String> preferredChannels) {
        Map<String, Object> c = customers.get(customerId);
        if (c == null)
            return false;
        c.put("preferredChannels", preferredChannels);
        return true;
    }

    @Override
    public List<String> getContactPreferences(Long customerId) {
        Map<String, Object> c = customers.get(customerId);
        if (c == null)
            return List.of();
        Object v = c.get("preferredChannels");
        if (v instanceof List<?> list) {
            List<String> out = new ArrayList<>();
            for (Object o : list)
                if (o instanceof String s)
                    out.add(s);
            return out;
        }
        return List.of();
    }

    @Override
    public boolean recordCustomerInteraction(Long customerId, String interactionType, String notes, Long recordedBy) {
        return true;
    }

    @Override
    public List<Map<String, Object>> searchCustomers(String searchQuery, int limit, int offset) {
        List<Map<String, Object>> list = new ArrayList<>(customers.values());
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> c : list) {
            String name = String.valueOf(c.getOrDefault("name", ""));
            String email = String.valueOf(c.getOrDefault("email", ""));
            if (searchQuery == null || name.contains(searchQuery) || email.contains(searchQuery)) {
                filtered.add(c);
            }
        }
        int from = Math.min(offset, filtered.size());
        int to = Math.min(from + limit, filtered.size());
        return filtered.subList(from, to);
    }

    @Override
    public List<Map<String, Object>> getCustomersByServiceCenter(Long serviceCenterId) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> getCustomersWithExpiringWarranties(int daysFromNow) {
        return List.of();
    }

    @Override
    public boolean recordServiceVisit(Long customerId, String vin, Long serviceCenterId, LocalDate visitDate) {
        return true;
    }

    @Override
    public Integer getCustomerVehicleCount(Long customerId) {
        return getCustomerVehicles(customerId).size();
    }

    @Override
    public LocalDate getLastServiceDate(Long customerId) {
        return null;
    }

    @Override
    public Double getCustomerLifetimeValue(Long customerId) {
        return 0.0;
    }
}
