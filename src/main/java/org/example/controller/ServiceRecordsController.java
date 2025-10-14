package org.example.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.example.models.core.ServiceRecord;
import org.example.models.enums.ClaimStatus;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IServiceRecordService;
import org.example.service.IService.IWarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/service-records")
public class ServiceRecordsController {

    @Autowired
    private IServiceRecordService service;

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private IWarrantyClaimService warrantyClaimService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServiceRecord record) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff can create service records; SC_Technician if
            // assigned
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole role = (UserRole) roleObj;
            if (role == UserRole.SC_Technician) {
                // allow if technician_id matches
                Long userId = (Long) session.get("userId");
                if (record.getTechnician_id() == null || !record.getTechnician_id().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Technician can only create records for themselves"));
                }
            } else if (role != UserRole.Admin && role != UserRole.EVM_Staff && role != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Insufficient permissions to create service record"));
            }
            ServiceRecord saved = service.create(record);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ServiceRecord record) {
        try {
            // RBAC similar to create
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole role = (UserRole) roleObj;
            if (role == UserRole.SC_Technician) {
                Long userId = (Long) session.get("userId");
                if (record.getTechnician_id() == null || !record.getTechnician_id().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Technician can only update their own records"));
                }
            } else if (role != UserRole.Admin && role != UserRole.EVM_Staff && role != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Insufficient permissions to update service record"));
            }
            record.setId(id);
            return ResponseEntity.ok(service.update(record));
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
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        Map<String, Object> session = authenticationService.getSessionByToken(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired session"));
        }
        Object roleObj = session.get("role");
        if (!(roleObj instanceof UserRole) || roleObj != UserRole.Admin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Admin only"));
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
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole role = (UserRole) roleObj;

            ServiceRecord rec = service.update(new ServiceRecord() {
                {
                    setId(id);
                }
            });
            // Re-read to get current
            List<ServiceRecord> list = service.getByPerformedBetween(OffsetDateTime.MIN, OffsetDateTime.MAX);
            ServiceRecord current = list.stream().filter(r -> id.equals(r.getId())).findFirst().orElse(null);
            if (current == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Service record not found"));
            }
            if (role == UserRole.SC_Technician) {
                Long userId = (Long) session.get("userId");
                if (current.getTechnician_id() == null || !current.getTechnician_id().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Technician can only handover own records"));
                }
            }
            OffsetDateTime handoverAt = time != null ? OffsetDateTime.parse(time) : OffsetDateTime.now();
            current.setHandover_at(handoverAt);
            service.update(current);

            if (completeClaim && current.getClaim_id() != null) {
                Long userId = (Long) session.get("userId");
                warrantyClaimService.updateClaimStatus(current.getClaim_id(), ClaimStatus.completed, "handover",
                        userId);
            }

            return ResponseEntity.ok(Map.of("success", true, "handover_at", handoverAt.toString(), "claim_completed",
                    completeClaim && current.getClaim_id() != null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
