package org.example.service.Serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.example.service.IService.IAppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService implements IAppointmentService {

    private final AtomicLong appointmentIdGenerator = new AtomicLong(1);
    private final Map<Long, Map<String, Object>> appointments = new HashMap<>();

    @Override
    public Map<String, Object> createAppointment(String serviceType, Long serviceCenterId, String vin,
            String appointmentDate, String appointmentTime, String customerName, String customerPhone, String notes) {
        Long appointmentId = appointmentIdGenerator.getAndIncrement();

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("appointmentId", appointmentId);
        appointment.put("serviceType", serviceType);
        appointment.put("serviceCenterId", serviceCenterId);
        appointment.put("vin", vin);
        appointment.put("appointmentDate", appointmentDate);
        appointment.put("appointmentTime", appointmentTime);
        appointment.put("customerName", customerName);
        appointment.put("customerPhone", customerPhone);
        appointment.put("notes", notes);
        appointment.put("status", "scheduled");
        appointment.put("createdAt", java.time.LocalDateTime.now().toString());

        appointments.put(appointmentId, appointment);
        return appointment;
    }

    @Override
    public Map<String, Object> getAppointment(Long appointmentId) {
        return appointments.get(appointmentId);
    }

    @Override
    public boolean updateAppointmentStatus(Long appointmentId, String status, String notes) {
        Map<String, Object> appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.put("status", status);
            if (notes != null) {
                appointment.put("statusNotes", notes);
            }
            appointment.put("updatedAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean rescheduleAppointment(Long appointmentId, String newDate, String newTime, String reason) {
        Map<String, Object> appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.put("appointmentDate", newDate);
            appointment.put("appointmentTime", newTime);
            appointment.put("rescheduleReason", reason);
            appointment.put("rescheduledAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelAppointment(Long appointmentId, String reason) {
        Map<String, Object> appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.put("status", "cancelled");
            appointment.put("cancellationReason", reason);
            appointment.put("cancelledAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean completeAppointment(Long appointmentId, String completionNotes, String technicianId) {
        Map<String, Object> appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.put("status", "completed");
            appointment.put("completionNotes", completionNotes);
            appointment.put("technicianId", technicianId);
            appointment.put("completedAt", java.time.LocalDateTime.now().toString());
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getAppointmentsByServiceType(String serviceType, Long serviceCenterId) {
        return appointments.values().stream()
                .filter(appointment -> serviceType.equals(appointment.get("serviceType")))
                .filter(appointment -> serviceCenterId == null
                        || serviceCenterId.equals(appointment.get("serviceCenterId")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Map<String, Object>> getAppointmentsByServiceCenter(Long serviceCenterId, String date, String status) {
        return appointments.values().stream()
                .filter(appointment -> serviceCenterId.equals(appointment.get("serviceCenterId")))
                .filter(appointment -> date == null || date.equals(appointment.get("appointmentDate")))
                .filter(appointment -> status == null || status.equals(appointment.get("status")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Map<String, Object>> getAppointmentsByVin(String vin) {
        return appointments.values().stream()
                .filter(appointment -> vin.equals(appointment.get("vin")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Map<String, Object>> checkAvailability(Long serviceCenterId, String date, String timeSlot) {
        List<Map<String, Object>> availability = new ArrayList<>();

        // Placeholder implementation - return available time slots
        String[] timeSlots = { "09:00", "10:00", "11:00", "14:00", "15:00", "16:00" };
        for (String slot : timeSlots) {
            Map<String, Object> slotInfo = new HashMap<>();
            slotInfo.put("timeSlot", slot);
            slotInfo.put("available", true);
            availability.add(slotInfo);
        }

        return availability;
    }

    @Override
    public boolean blockTimeSlot(Long serviceCenterId, String date, String timeSlot, String reason) {
        // Placeholder implementation
        return true;
    }

    @Override
    public boolean releaseTimeSlot(Long serviceCenterId, String date, String timeSlot) {
        // Placeholder implementation
        return true;
    }

    @Override
    public boolean sendAppointmentReminder(Long appointmentId) {
        // Placeholder implementation
        return true;
    }

    @Override
    public boolean sendAppointmentConfirmation(Long appointmentId) {
        // Placeholder implementation
        return true;
    }

    @Override
    public Map<String, Object> getAppointmentStatistics(Long serviceCenterId, String dateRange) {
        Map<String, Object> stats = new HashMap<>();
        List<Map<String, Object>> serviceCenterAppointments = getAppointmentsByServiceCenter(serviceCenterId, null,
                null);

        stats.put("totalAppointments", serviceCenterAppointments.size());
        stats.put("scheduledAppointments",
                serviceCenterAppointments.stream().filter(a -> "scheduled".equals(a.get("status"))).count());
        stats.put("completedAppointments",
                serviceCenterAppointments.stream().filter(a -> "completed".equals(a.get("status"))).count());
        stats.put("cancelledAppointments",
                serviceCenterAppointments.stream().filter(a -> "cancelled".equals(a.get("status"))).count());

        return stats;
    }

    @Override
    public List<Map<String, Object>> getNoShowAppointments(Long serviceCenterId, String dateRange) {
        return appointments.values().stream()
                .filter(appointment -> serviceCenterId.equals(appointment.get("serviceCenterId")))
                .filter(appointment -> "no_show".equals(appointment.get("status")))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public Double getAverageAppointmentDuration(Long serviceCenterId, String dateRange) {
        // Placeholder implementation
        return 1.5; // Average duration in hours
    }
}
