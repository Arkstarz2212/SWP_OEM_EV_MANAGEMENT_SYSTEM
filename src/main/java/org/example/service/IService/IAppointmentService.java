package org.example.service.IService;

import java.util.List;
import java.util.Map;

public interface IAppointmentService {
    // Appointment Creation and Management
    Map<String, Object> createAppointment(String serviceType, Long serviceCenterId, String vin, String appointmentDate,
            String appointmentTime, String customerName, String customerPhone, String notes);

    Map<String, Object> getAppointment(Long appointmentId);

    boolean updateAppointmentStatus(Long appointmentId, String status, String notes);

    // Appointment Scheduling
    boolean rescheduleAppointment(Long appointmentId, String newDate, String newTime, String reason);

    boolean cancelAppointment(Long appointmentId, String reason);

    boolean completeAppointment(Long appointmentId, String completionNotes, String technicianId);

    // Query Operations
    List<Map<String, Object>> getAppointmentsByServiceType(String serviceType, Long serviceCenterId);

    List<Map<String, Object>> getAppointmentsByServiceCenter(Long serviceCenterId, String date, String status);

    List<Map<String, Object>> getAppointmentsByVin(String vin);

    // Availability Management
    List<Map<String, Object>> checkAvailability(Long serviceCenterId, String date, String timeSlot);

    boolean blockTimeSlot(Long serviceCenterId, String date, String timeSlot, String reason);

    boolean releaseTimeSlot(Long serviceCenterId, String date, String timeSlot);

    // Notifications
    boolean sendAppointmentReminder(Long appointmentId);

    boolean sendAppointmentConfirmation(Long appointmentId);

    // Analytics
    Map<String, Object> getAppointmentStatistics(Long serviceCenterId, String dateRange);

    List<Map<String, Object>> getNoShowAppointments(Long serviceCenterId, String dateRange);

    Double getAverageAppointmentDuration(Long serviceCenterId, String dateRange);
}
