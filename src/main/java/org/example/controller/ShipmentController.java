package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.enums.UserRole;
import org.example.service.IService.IShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipments", description = "Shipment management for parts delivery to service centers with tracking and status updates")
public class ShipmentController {

    @Autowired
    private IShipmentService shipmentService;

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

    @PostMapping
    @Operation(summary = "Create Shipment", description = "Create a new shipment for parts delivery to a service center for a warranty claim. Roles: EVM_Staff, Admin.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Shipment creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create Shipment Example", value = "{\"claimId\": 2001, \"serviceCenterId\": 2, \"partNumbers\": [\"BAT001\", \"MOTOR002\"], \"priority\": \"urgent\", \"notes\": \"Rush delivery for customer\"}"))))
    public ResponseEntity<?> createShipment(@RequestBody Map<String, Object> body) {
        try {
            // RBAC: Only EVM_Staff and Admin can create shipments
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
                UserRole currentRole = convertToUserRole(roleObj);

                if (currentRole == null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid user role"));
                }
                if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Only Admin or EVM_Staff can create shipments"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Invalid authentication details"));
            }

            Long claimId = body.get("claimId") != null ? Long.valueOf(body.get("claimId").toString()) : null;
            Long serviceCenterId = body.get("serviceCenterId") != null
                    ? Long.valueOf(body.get("serviceCenterId").toString())
                    : null;
            Object partNumbersObj = body.get("partNumbers");
            List<String> partNumbers = partNumbersObj instanceof List<?> list
                    ? list.stream().map(Object::toString).toList()
                    : List.of();
            String priority = String.valueOf(body.getOrDefault("priority", "normal"));
            String notes = String.valueOf(body.getOrDefault("notes", ""));

            Map<String, Object> result = shipmentService.createShipment(claimId, serviceCenterId, partNumbers, priority,
                    notes);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{shipmentId}")
    @Operation(summary = "Get Shipment Details", description = "Retrieve detailed information about a specific shipment. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "shipmentId", description = "Shipment ID", required = true, example = "1001") })
    public ResponseEntity<?> getShipment(@PathVariable("shipmentId") Long shipmentId) {
        try {
            Map<String, Object> shipment = shipmentService.getShipment(shipmentId);
            return ResponseEntity.ok(shipment);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{shipmentId}/status")
    @Operation(summary = "Update Shipment Status", description = "Update the status of a shipment with optional notes. Roles: EVM_Staff.", parameters = {
            @Parameter(name = "shipmentId", description = "Shipment ID", required = true, example = "1001"),
            @Parameter(name = "status", description = "New status (e.g., shipped, in_transit, delivered)", required = true, example = "shipped"),
            @Parameter(name = "notes", description = "Optional status update notes", required = false, example = "Package picked up by carrier")
    })
    public ResponseEntity<?> updateShipmentStatus(@PathVariable("shipmentId") Long shipmentId,
            @RequestParam("status") String status,
            @RequestParam(value = "notes", required = false) String notes) {
        try {
            boolean success = shipmentService.updateShipmentStatus(shipmentId, status, notes);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{shipmentId}/track")
    @Operation(summary = "Add Tracking Information", description = "Add tracking number and carrier information to a shipment. Roles: EVM_Staff.", parameters = {
            @Parameter(name = "shipmentId", description = "Shipment ID", required = true, example = "1001") }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tracking information", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Add Tracking Example", value = "{\"trackingNumber\": \"1Z999AA1234567890\", \"carrier\": \"UPS\", \"estimatedDelivery\": \"2024-12-05\"}"))))
    public ResponseEntity<?> addTrackingInfo(@PathVariable("shipmentId") Long shipmentId,
            @RequestBody Map<String, Object> body) {
        try {
            String trackingNumber = String.valueOf(body.get("trackingNumber"));
            String carrier = String.valueOf(body.get("carrier"));
            String estimatedDelivery = String.valueOf(body.get("estimatedDelivery"));

            boolean success = shipmentService.addTrackingInfo(shipmentId, trackingNumber, carrier, estimatedDelivery);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/claim/{claimId}")
    @Operation(summary = "Shipments by Claim", description = "Get all shipments associated with a specific warranty claim. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "claimId", description = "Warranty claim ID", required = true, example = "2001") })
    public ResponseEntity<?> getShipmentsByClaim(@PathVariable("claimId") Long claimId) {
        try {
            List<Map<String, Object>> shipments = shipmentService.getShipmentsByClaim(claimId);
            return ResponseEntity.ok(shipments);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/service-center/{serviceCenterId}")
    @Operation(summary = "Shipments by Service Center", description = "Get all shipments for a service center with optional status filtering. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "serviceCenterId", description = "Service center ID", required = true, example = "2"),
            @Parameter(name = "status", description = "Filter by shipment status", required = false, example = "pending")
    })
    public ResponseEntity<?> getShipmentsByServiceCenter(@PathVariable("serviceCenterId") Long serviceCenterId,
            @RequestParam(value = "status", required = false) String status) {
        try {
            List<Map<String, Object>> shipments = shipmentService.getShipmentsByServiceCenter(serviceCenterId, status);
            return ResponseEntity.ok(shipments);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{shipmentId}/deliver")
    @Operation(summary = "Mark as Delivered", description = "Mark a shipment as delivered with delivery details. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "shipmentId", description = "Shipment ID", required = true, example = "1001") }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Delivery information", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Mark Delivered Example", value = "{\"deliveredBy\": \"John Smith\", \"deliveryNotes\": \"Delivered to service center reception\"}"))))
    public ResponseEntity<?> markAsDelivered(@PathVariable("shipmentId") Long shipmentId,
            @RequestBody Map<String, Object> body) {
        try {
            String deliveredBy = String.valueOf(body.get("deliveredBy"));
            String deliveryNotes = String.valueOf(body.getOrDefault("deliveryNotes", ""));

            boolean success = shipmentService.markAsDelivered(shipmentId, deliveredBy, deliveryNotes);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Get Shipment by Tracking Number", description = "Lookup shipment details using tracking number. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "trackingNumber", description = "Tracking number", required = true, example = "1Z999AA1234567890") })
    public ResponseEntity<?> getShipmentByTracking(@PathVariable("trackingNumber") String trackingNumber) {
        try {
            Map<String, Object> shipment = shipmentService.getShipmentByTracking(trackingNumber);
            return ResponseEntity.ok(shipment);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/pending")
    @Operation(summary = "Pending Shipments", description = "Get all pending shipments, optionally filtered by service center. Roles: Admin, SC_Staff, EVM_Staff.", parameters = {
            @Parameter(name = "serviceCenterId", description = "Service center ID filter", required = false, example = "2") })
    public ResponseEntity<?> getPendingShipments(
            @RequestParam(value = "serviceCenterId", required = false) Long serviceCenterId) {
        try {
            List<Map<String, Object>> shipments = shipmentService.getPendingShipments(serviceCenterId);
            return ResponseEntity.ok(shipments);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
