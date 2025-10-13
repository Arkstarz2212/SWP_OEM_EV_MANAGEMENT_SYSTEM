package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.service.IService.IWarrantyCertificateService;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/warranty-certificates")
@Tag(name = "Warranty Certificates", description = "Warranty certificate management including creation, validation, activation, and extension")
public class WarrantyCertificateController {

    @Autowired
    private IWarrantyCertificateService warrantyCertificateService;

    @PostMapping
    @Operation(summary = "Create Warranty Certificate", description = "Create a new warranty certificate for a vehicle with specified policy and start date.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Certificate creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create Certificate Example", value = "{\"vin\": \"1HGBH41JXMN109186\", \"policyId\": 1, \"startDate\": \"2024-01-01\"}"))))
    public ResponseEntity<?> createCertificate(@RequestBody Map<String, Object> body) {
        try {
            String vin = String.valueOf(body.get("vin"));
            Long policyId = body.get("policyId") != null ? Long.valueOf(body.get("policyId").toString()) : 1L;
            String startDateStr = String.valueOf(body.get("startDate"));
            java.time.LocalDate startDate = java.time.LocalDate.parse(startDateStr);

            Map<String, Object> result = warrantyCertificateService.createWarrantyCertificate(vin, policyId, startDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{certificateId}")
    @Operation(summary = "Get Certificate by ID", description = "Retrieve warranty certificate details by certificate ID.", parameters = {
            @Parameter(name = "certificateId", description = "Certificate ID", required = true, example = "1001") })
    public ResponseEntity<?> getCertificate(@PathVariable("certificateId") Long certificateId) {
        try {
            Map<String, Object> certificate = warrantyCertificateService.getCertificateById(certificateId);
            return ResponseEntity.ok(certificate);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/vin/{vin}")
    @Operation(summary = "Get Certificate by VIN", description = "Retrieve warranty certificate for a specific vehicle by VIN.", parameters = {
            @Parameter(name = "vin", description = "Vehicle VIN", required = true, example = "1HGBH41JXMN109186") })
    public ResponseEntity<?> getCertificatesByVin(@PathVariable("vin") String vin) {
        try {
            Map<String, Object> certificate = warrantyCertificateService.getCertificateByVin(vin);
            return ResponseEntity.ok(certificate);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{certificateId}/activate")
    @Operation(summary = "Activate Certificate", description = "Activate a warranty certificate to make it effective.", parameters = {
            @Parameter(name = "certificateId", description = "Certificate ID", required = true, example = "1001") })
    public ResponseEntity<?> activateCertificate(@PathVariable("certificateId") Long certificateId) {
        try {
            boolean success = warrantyCertificateService.activateCertificate(certificateId);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{certificateId}/deactivate")
    @Operation(summary = "Deactivate Certificate", description = "Deactivate a warranty certificate with optional reason.", parameters = {
            @Parameter(name = "certificateId", description = "Certificate ID", required = true, example = "1001"),
            @Parameter(name = "reason", description = "Deactivation reason", required = false, example = "Vehicle sold")
    })
    public ResponseEntity<?> deactivateCertificate(@PathVariable("certificateId") Long certificateId,
            @RequestParam(value = "reason", required = false) String reason) {
        try {
            boolean success = warrantyCertificateService.deactivateCertificate(certificateId,
                    reason != null ? reason : "Manual deactivation");
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/expiring")
    @Operation(summary = "Expiring Certificates", description = "Get certificates that are expiring within the specified number of days.", parameters = {
            @Parameter(name = "days", description = "Days ahead to check for expiration", required = false, example = "30", schema = @Schema(defaultValue = "30")) })
    public ResponseEntity<?> getExpiringCertificates(
            @RequestParam(value = "days", required = false, defaultValue = "30") Integer days) {
        try {
            List<Map<String, Object>> certificates = warrantyCertificateService.getCertificatesExpiringInDays(days);
            return ResponseEntity.ok(certificates);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{certificateId}/extend")
    @Operation(summary = "Extend Certificate", description = "Extend the validity period of a warranty certificate.", parameters = {
            @Parameter(name = "certificateId", description = "Certificate ID", required = true, example = "1001") }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Extension details", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Extend Certificate Example", value = "{\"additionalMonths\": 12, \"reason\": \"Goodwill extension\"}"))))
    public ResponseEntity<?> extendCertificate(@PathVariable("certificateId") Long certificateId,
            @RequestBody Map<String, Object> body) {
        try {
            Integer additionalMonths = body.get("additionalMonths") != null
                    ? Integer.valueOf(body.get("additionalMonths").toString())
                    : 12;
            String reason = String.valueOf(body.getOrDefault("reason", "Extension"));

            boolean success = warrantyCertificateService.extendCertificate(certificateId, additionalMonths, reason);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate/{vin}")
    @Operation(summary = "Validate Warranty", description = "Validate if a vehicle's warranty certificate covers a specific component type.", parameters = {
            @Parameter(name = "vin", description = "Vehicle VIN", required = true, example = "1HGBH41JXMN109186"),
            @Parameter(name = "componentType", description = "Component type to validate", required = true, example = "BATTERY")
    })
    public ResponseEntity<?> validateWarranty(@PathVariable("vin") String vin,
            @RequestParam("componentType") String componentType) {
        try {
            boolean isValid = warrantyCertificateService.validateCertificate(vin, componentType);
            return ResponseEntity.ok(Map.of("valid", isValid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
