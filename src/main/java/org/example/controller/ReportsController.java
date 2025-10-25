package org.example.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.example.models.core.WarrantyClaim;
import org.example.models.enums.ClaimStatus;
import org.example.repository.IRepository.IServiceRecordRepository;
import org.example.repository.IRepository.IVehicleRepository;
import org.example.repository.IRepository.IWarrantyClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Aggregated reporting endpoints")
public class ReportsController {

    @Autowired
    private IWarrantyClaimRepository claimRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    @Autowired
    private IServiceRecordRepository serviceRecordRepository;

    @GetMapping
    @Operation(summary = "Reporting check", description = "Return aggregated numbers for health check and dashboards. Roles: Admin, SC_Staff, EVM_Staff. Performance sub-resources may allow SC_Technician.")
    public ResponseEntity<?> getAggregates() {
        Map<String, Object> res = new LinkedHashMap<>();

        // Claims aggregates
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("total", safeCount(() -> claimRepository.findAll().size()));
        for (ClaimStatus st : ClaimStatus.values()) {
            List<WarrantyClaim> list = claimRepository.findByStatus(st);
            claims.put(st.name(), list != null ? list.size() : 0);
        }
        res.put("claims", claims);

        // Vehicles aggregate
        res.put("vehicles", Map.of("total", safeCount(() -> vehicleRepository.findAll().size())));

        // Service records aggregate
        res.put("serviceRecords", Map.of("total", safeCount(() -> serviceRecordRepository.findByPerformedAtBetween(
                java.time.OffsetDateTime.MIN.plusYears(1000), // avoid DB min timestamp issues
                java.time.OffsetDateTime.now()).size())));

        return ResponseEntity.ok(res);
    }

    private int safeCount(java.util.concurrent.Callable<Integer> supplier) {
        try {
            return supplier.call();
        } catch (Exception ignored) {
            return 0;
        }
    }
}
