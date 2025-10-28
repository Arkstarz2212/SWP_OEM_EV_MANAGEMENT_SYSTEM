package org.example.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.example.models.core.ServiceRecord;
import org.example.models.enums.ClaimStatus;
import org.example.models.enums.UserRole;
import org.example.service.IService.IServiceRecordService;
import org.example.service.IService.IWarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-records")
public class ServiceRecordsController {

    @Autowired
    private IServiceRecordService service;

    /**
     * Helper method to convert role from session to UserRole enum
     */
    private UserRole convertToUserRole(Object roleObj) {
        if (roleObj instanceof UserRole) {
            return (UserRole) roleObj;
        } else if (roleObj instanceof String) {
            try {
                return UserRole.valueOf((String) roleObj);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    @Autowired
    private IWarrantyClaimService warrantyClaimService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServiceRecord record) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff can create service records; SC_Technician if
            // assigned
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }

            // Get role from authentication details
            Object details = authentication.getDetails();
            if (details instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> authDetails = (Map<String, Object>) details;
                Object roleObj = authDetails.get("role");
                UserRole role = convertToUserRole(roleObj);

                if (role == null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid user role"));
                }
                if (role == UserRole.SC_Technician) {
                    // allow if technician_id matches
                    Long userId = (Long) authDetails.get("userId");
                    if (record.getTechnician_id() == null || !record.getTechnician_id().equals(userId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "Technician can only create records for themselves"));
                    }
                } else if (role != UserRole.Admin && role != UserRole.EVM_Staff && role != UserRole.SC_Staff) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Insufficient permissions to create service record"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Invalid authentication details"));
            }
            // Basic FK sanity checks (prevent obvious bad ids like 0/null)
            if (record.getVehicle_id() == null || record.getVehicle_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid vehicle_id: must reference an existing vehicle (>0)"));
            }
            if (record.getService_center_id() == null || record.getService_center_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Invalid service_center_id: must reference an existing service center (>0)"));
            }
            if (record.getTechnician_id() != null && record.getTechnician_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Invalid technician_id: must reference an existing user (>0) or be null"));
            }
            if (record.getClaim_id() != null && record.getClaim_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid claim_id: must reference an existing claim (>0) or be null"));
            }

            ServiceRecord saved = service.create(record);
            return ResponseEntity.ok(saved);
        } catch (DataIntegrityViolationException dive) {
            // Return clearer message for FK errors
            String msg = dive.getMostSpecificCause() != null ? dive.getMostSpecificCause().getMessage()
                    : dive.getMessage();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Foreign key violation while saving service record",
                    "details", msg,
                    "payload", Map.of(
                            "vehicle_id", record.getVehicle_id(),
                            "service_center_id", record.getService_center_id(),
                            "claim_id", record.getClaim_id(),
                            "technician_id", record.getTechnician_id())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ServiceRecord record) {
        try {
            // RBAC similar to create
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }

            // Get role from authentication details
            Object details = authentication.getDetails();
            if (details instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> authDetails = (Map<String, Object>) details;
                Object roleObj = authDetails.get("role");
                UserRole role = convertToUserRole(roleObj);

                if (role == null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid user role"));
                }
                if (role == UserRole.SC_Technician) {
                    Long userId = (Long) authDetails.get("userId");
                    if (record.getTechnician_id() == null || !record.getTechnician_id().equals(userId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "Technician can only update their own records"));
                    }
                } else if (role != UserRole.Admin && role != UserRole.EVM_Staff && role != UserRole.SC_Staff) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Insufficient permissions to update service record"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Invalid authentication details"));
            }
            // Basic FK sanity checks
            if (record.getVehicle_id() != null && record.getVehicle_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid vehicle_id: must reference an existing vehicle (>0)"));
            }
            if (record.getService_center_id() != null && record.getService_center_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Invalid service_center_id: must reference an existing service center (>0)"));
            }
            if (record.getTechnician_id() != null && record.getTechnician_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Invalid technician_id: must reference an existing user (>0) or be null"));
            }
            if (record.getClaim_id() != null && record.getClaim_id() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid claim_id: must reference an existing claim (>0) or be null"));
            }

            record.setId(id);
            return ResponseEntity.ok(service.update(record));
        } catch (DataIntegrityViolationException dive) {
            String msg = dive.getMostSpecificCause() != null ? dive.getMostSpecificCause().getMessage()
                    : dive.getMessage();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Foreign key violation while updating service record",
                    "details", msg,
                    "payload", Map.of(
                            "id", id,
                            "vehicle_id", record.getVehicle_id(),
                            "service_center_id", record.getService_center_id(),
                            "claim_id", record.getClaim_id(),
                            "technician_id", record.getTechnician_id())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<ServiceRecord> list(@RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long claimId,
            @RequestParam(required = false) Long serviceCenterId) {
        if (vehicleId != null)
            return service.getByVehicle(vehicleId);
        if (claimId != null)
            return service.getByClaim(claimId);
        if (serviceCenterId != null)
            return service.getByServiceCenter(serviceCenterId);
        return List.of();
    }

    @GetMapping("/range")
    public List<ServiceRecord> byRange(@RequestParam String start, @RequestParam String end) {
        return service.getByPerformedBetween(OffsetDateTime.parse(start), OffsetDateTime.parse(end));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // RBAC: Admin only
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        // Get role from authentication details
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> authDetails = (Map<String, Object>) details;
            Object roleObj = authDetails.get("role");
            UserRole role = convertToUserRole(roleObj);

            if (role == null || role != UserRole.Admin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Admin only"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid authentication details"));
        }

        service.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/{id}/handover")
    public ResponseEntity<?> handover(@PathVariable Long id,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "completeClaim", required = false, defaultValue = "false") boolean completeClaim) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff, SC_Technician (own) can handover
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }

            // Get role from authentication details
            Object details = authentication.getDetails();
            if (details instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> authDetails = (Map<String, Object>) details;
                Object roleObj = authDetails.get("role");
                UserRole role = convertToUserRole(roleObj);

                if (role == null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid user role"));
                }

                // Read current record by id
                ServiceRecord current = service.getById(id);
                if (current == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Service record not found"));
                }
                if (role == UserRole.SC_Technician) {
                    Long userId = (Long) authDetails.get("userId");
                    if (current.getTechnician_id() == null || !current.getTechnician_id().equals(userId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "Technician can only handover own records"));
                    }
                }
                OffsetDateTime handoverAt = time != null ? OffsetDateTime.parse(time) : OffsetDateTime.now();
                current.setHandover_at(handoverAt);
                service.update(current);

                if (completeClaim && current.getClaim_id() != null) {
                    Long userId = (Long) authDetails.get("userId");
                    warrantyClaimService.updateClaimStatus(current.getClaim_id(), ClaimStatus.completed, "handover",
                            userId);
                }

                return ResponseEntity
                        .ok(Map.of("success", true, "handover_at", handoverAt.toString(), "claim_completed",
                                completeClaim && current.getClaim_id() != null));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Invalid authentication details"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
