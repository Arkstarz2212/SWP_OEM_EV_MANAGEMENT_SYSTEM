package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.service.IService.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Service appointment booking, status updates, and availability checks")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, Object> body) {
        try {
            String serviceType = body.get("serviceType") != null ? body.get("serviceType").toString() : "WARRANTY";
            Long serviceCenterId = body.get("serviceCenterId") != null
                    ? Long.valueOf(body.get("serviceCenterId").toString())
                    : null;
            String vin = String.valueOf(body.get("vin"));
            String appointmentDate = String.valueOf(body.get("appointmentDate"));
            String appointmentTime = String.valueOf(body.get("appointmentTime"));
            String customerName = String.valueOf(body.get("customerName"));
            String customerPhone = String.valueOf(body.get("customerPhone"));
            String notes = String.valueOf(body.getOrDefault("notes", ""));

            Map<String, Object> result = appointmentService.createAppointment(serviceType, serviceCenterId, vin,
                    appointmentDate, appointmentTime, customerName, customerPhone, notes);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointment(@PathVariable("appointmentId") Long appointmentId) {
        try {
            Map<String, Object> appointment = appointmentService.getAppointment(appointmentId);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable("appointmentId") Long appointmentId,
            @RequestParam("status") String status,
            @RequestParam(value = "notes", required = false) String notes) {
        try {
            boolean success = appointmentService.updateAppointmentStatus(appointmentId, status, notes);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/service-type/{serviceType}")
    public ResponseEntity<?> getAppointmentsByServiceType(@PathVariable("serviceType") String serviceType,
            @RequestParam(value = "serviceCenterId", required = false) Long serviceCenterId) {
        try {
            List<Map<String, Object>> appointments = appointmentService.getAppointmentsByServiceType(serviceType,
                    serviceCenterId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/service-center/{serviceCenterId}")
    public ResponseEntity<?> getAppointmentsByServiceCenter(@PathVariable("serviceCenterId") Long serviceCenterId,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "status", required = false) String status) {
        try {
            List<Map<String, Object>> appointments = appointmentService.getAppointmentsByServiceCenter(serviceCenterId,
                    date, status);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/customer/{vin}")
    public ResponseEntity<?> getAppointmentsByVin(@PathVariable("vin") String vin) {
        try {
            List<Map<String, Object>> appointments = appointmentService.getAppointmentsByVin(vin);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{appointmentId}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@PathVariable("appointmentId") Long appointmentId,
            @RequestBody Map<String, Object> body) {
        try {
            String newDate = String.valueOf(body.get("newDate"));
            String newTime = String.valueOf(body.get("newTime"));
            String reason = String.valueOf(body.getOrDefault("reason", "Reschedule"));

            boolean success = appointmentService.rescheduleAppointment(appointmentId, newDate, newTime, reason);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable("appointmentId") Long appointmentId,
            @RequestParam(value = "reason", required = false) String reason) {
        try {
            boolean success = appointmentService.cancelAppointment(appointmentId,
                    reason != null ? reason : "Cancelled by customer");
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable("appointmentId") Long appointmentId,
            @RequestBody Map<String, Object> body) {
        try {
            String completionNotes = String.valueOf(body.getOrDefault("completionNotes", ""));
            String technicianId = String.valueOf(body.get("technicianId"));

            boolean success = appointmentService.completeAppointment(appointmentId, completionNotes, technicianId);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<?> checkAvailability(@RequestParam("serviceCenterId") Long serviceCenterId,
            @RequestParam("date") String date,
            @RequestParam(value = "timeSlot", required = false) String timeSlot) {
        try {
            List<Map<String, Object>> availability = appointmentService.checkAvailability(serviceCenterId, date,
                    timeSlot);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
