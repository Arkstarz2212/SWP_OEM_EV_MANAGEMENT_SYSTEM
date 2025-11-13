package org.example.controller;

import java.util.Map;

import org.example.service.IService.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Analytics statistics endpoints for dashboard")
public class AnalyticsController {

    @Autowired
    private IAnalyticsService analyticsService;


    @GetMapping("/statistics/parts")
    @Operation(summary = "Get Parts Statistics", description = "Get comprehensive statistics about parts catalog including total count, active/inactive parts, and breakdown by category.", parameters = {
            @Parameter(name = "oemId", description = "Optional OEM ID to filter statistics. If not provided, returns statistics for all OEMs.", required = false, example = "1")
    })
    public ResponseEntity<?> getPartStatistics(@RequestParam(value = "oemId", required = false) Long oemId) {
        try {
            Map<String, Object> statistics = analyticsService.getPartStatistics(oemId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/statistics/vehicles")
    @Operation(summary = "Get Vehicles Statistics", description = "Get comprehensive statistics about vehicles including total count, status breakdown (active/inactive/deleted), and distribution by model and year.", parameters = {
            @Parameter(name = "oemId", description = "Optional OEM ID to filter statistics. If not provided, returns statistics for all OEMs.", required = false, example = "1")
    })
    public ResponseEntity<?> getVehicleStatistics(@RequestParam(value = "oemId", required = false) Long oemId) {
        try {
            Map<String, Object> statistics = analyticsService.getVehicleStatistics(oemId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/statistics/claims")
    @Operation(summary = "Get Claims Statistics", description = "Get comprehensive statistics about warranty claims including total count and breakdown by status.", parameters = {
            @Parameter(name = "oemId", description = "Optional OEM ID to filter statistics. If not provided, returns statistics for all OEMs.", required = false, example = "1")
    })
    public ResponseEntity<?> getClaimStatistics(@RequestParam(value = "oemId", required = false) Long oemId) {
        try {
            Map<String, Object> statistics = analyticsService.getClaimStatistics(oemId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/statistics/warrantypolicy")
    @Operation(summary = "Get Warranty Policy Statistics", description = "Get comprehensive statistics about warranty policies including total count, active/inactive policies, and breakdown by OEM.", parameters = {
            @Parameter(name = "oemId", description = "Optional OEM ID to filter statistics. If not provided, returns statistics for all OEMs.", required = false, example = "1")
    })
    public ResponseEntity<?> getWarrantyPolicyStatistics(@RequestParam(value = "oemId", required = false) Long oemId) {
        try {
            Map<String, Object> statistics = analyticsService.getWarrantyPolicyStatistics(oemId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}