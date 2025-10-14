package org.example.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.example.service.IService.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Analytics and reporting endpoints for dashboards, statistics, and predictions")
public class AnalyticsController {

    @Autowired
    private IAnalyticsService analyticsService;

    @GetMapping("/dashboard/evm/{oemId}")
    @Operation(summary = "EVM Executive Dashboard", description = "Generate executive dashboard with key metrics for EVM (Electric Vehicle Manufacturer).", parameters = {
            @Parameter(name = "oemId", description = "OEM ID", required = true, example = "1"),
            @Parameter(name = "dateRange", description = "Date range for analytics (e.g., 30d, 90d, 1y)", required = false, example = "30d", schema = @Schema(defaultValue = "30d"))
    })
    public ResponseEntity<?> getEvmDashboard(@PathVariable("oemId") Long oemId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            Map<String, Object> dashboard = analyticsService.generateExecutiveDashboard(oemId,
                    dateRange != null ? dateRange : "30d");
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/dashboard/service-center/{serviceCenterId}")
    @Operation(summary = "Service Center Dashboard", description = "Get performance metrics and analytics for a specific service center.", parameters = {
            @Parameter(name = "serviceCenterId", description = "Service center ID", required = true, example = "2"),
            @Parameter(name = "dateRange", description = "Date range for analytics", required = false, example = "30d", schema = @Schema(defaultValue = "30d"))
    })
    public ResponseEntity<?> getServiceCenterDashboard(@PathVariable("serviceCenterId") Long serviceCenterId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            Map<String, Object> dashboard = analyticsService.getServiceCenterPerformanceMetrics(serviceCenterId,
                    dateRange != null ? dateRange : "30d");
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/claims/statistics")
    @Operation(summary = "Warranty Claims Statistics", description = "Get warranty cost breakdown and claim statistics for the last 30 days.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID", required = false, example = "1"),
            @Parameter(name = "serviceCenterId", description = "Service center ID", required = false, example = "2"),
            @Parameter(name = "dateRange", description = "Date range for statistics", required = false, example = "30d")
    })
    public ResponseEntity<?> getClaimStatistics(@RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "serviceCenterId", required = false) Long serviceCenterId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            LocalDate fromDate = LocalDate.now().minusDays(30);
            LocalDate toDate = LocalDate.now();
            Map<String, Object> statistics = analyticsService.getWarrantyCostBreakdown(oemId, fromDate, toDate);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/recalls/progress")
    @Operation(summary = "Recall Progress Analytics", description = "Analyze recall effectiveness and progress metrics.", parameters = {
            @Parameter(name = "recallDetails", description = "Recall Details (required)", required = true, example = "Battery Safety Recall 2024"),
            @Parameter(name = "oemId", description = "OEM ID", required = false, example = "1")
    })
    public ResponseEntity<?> getRecallProgress(
            @RequestParam(value = "recallDetails", required = false) String recallDetails,
            @RequestParam(value = "oemId", required = false) Long oemId) {
        try {
            if (recallDetails != null) {
                Map<String, Object> progress = analyticsService.analyzeRecallEffectiveness(recallDetails);
                return ResponseEntity.ok(progress);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Recall Details is required"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/parts/usage")
    @Operation(summary = "Parts Usage Analytics", description = "Analyze parts usage patterns and predict failure patterns.", parameters = {
            @Parameter(name = "partNumber", description = "Part number (required)", required = true, example = "BAT001"),
            @Parameter(name = "oemId", description = "OEM ID", required = false, example = "1"),
            @Parameter(name = "dateRange", description = "Date range for analysis", required = false, example = "90d")
    })
    public ResponseEntity<?> getPartsUsageAnalytics(
            @RequestParam(value = "partNumber", required = false) String partNumber,
            @RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            if (partNumber != null) {
                Map<String, Object> analytics = analyticsService.predictFailurePatterns(partNumber, "ModelX", 2023);
                return ResponseEntity.ok(analytics);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Part number is required"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/vehicles/warranty-status")
    @Operation(summary = "Vehicle Warranty Status", description = "Identify high-risk vehicles based on warranty status and patterns.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID (required)", required = true, example = "1"),
            @Parameter(name = "model", description = "Vehicle model filter", required = false, example = "Model 3"),
            @Parameter(name = "region", description = "Geographic region filter", required = false, example = "North")
    })
    public ResponseEntity<?> getVehicleWarrantyStatus(@RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "region", required = false) String region) {
        try {
            if (oemId != null) {
                List<Map<String, Object>> highRiskVehicles = analyticsService.identifyHighRiskVehicles(oemId,
                        "warranty");
                return ResponseEntity.ok(Map.of("highRiskVehicles", highRiskVehicles));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "OEM ID is required"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/costs/breakdown")
    @Operation(summary = "Warranty Cost Breakdown", description = "Get detailed warranty cost breakdown for the last 30 days.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID", required = false, example = "1"),
            @Parameter(name = "serviceCenterId", description = "Service center ID", required = false, example = "2"),
            @Parameter(name = "dateRange", description = "Date range for cost analysis", required = false, example = "30d")
    })
    public ResponseEntity<?> getCostBreakdown(@RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "serviceCenterId", required = false) Long serviceCenterId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            LocalDate fromDate = LocalDate.now().minusDays(30);
            LocalDate toDate = LocalDate.now();
            Map<String, Object> breakdown = analyticsService.getWarrantyCostBreakdown(oemId, fromDate, toDate);
            return ResponseEntity.ok(breakdown);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/performance/technicians")
    @Operation(summary = "Technician Performance", description = "Get performance metrics for technicians at a service center.", parameters = {
            @Parameter(name = "serviceCenterId", description = "Service center ID", required = true, example = "2"),
            @Parameter(name = "dateRange", description = "Date range for performance analysis", required = false, example = "30d", schema = @Schema(defaultValue = "30d"))
    })
    public ResponseEntity<?> getTechnicianPerformance(@RequestParam("serviceCenterId") Long serviceCenterId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            Map<String, Object> performance = analyticsService.getServiceCenterPerformanceMetrics(serviceCenterId,
                    dateRange != null ? dateRange : "30d");
            return ResponseEntity.ok(performance);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/trends/failure-patterns")
    @Operation(summary = "Failure Pattern Analysis", description = "Analyze common failure modes and patterns for specific component types.", parameters = {
            @Parameter(name = "componentType", description = "Component type (required)", required = true, example = "BATTERY"),
            @Parameter(name = "oemId", description = "OEM ID (required)", required = true, example = "1"),
            @Parameter(name = "dateRange", description = "Date range for pattern analysis", required = false, example = "180d")
    })
    public ResponseEntity<?> getFailurePatterns(
            @RequestParam(value = "componentType", required = false) String componentType,
            @RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "dateRange", required = false) String dateRange) {
        try {
            if (componentType != null && oemId != null) {
                List<Map<String, Object>> patterns = analyticsService.getCommonFailureModes(componentType, oemId);
                return ResponseEntity.ok(Map.of("failurePatterns", patterns));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Component type and OEM ID are required"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/reports/custom")
    @Operation(summary = "Custom Report Generation", description = "Generate custom analytics reports based on specified parameters.", parameters = {
            @Parameter(name = "reportType", description = "Type of report to generate", required = true, example = "warranty_summary"),
            @Parameter(name = "parameters", description = "Additional parameters for report customization", required = false, example = "model=Model3&region=North")
    })
    public ResponseEntity<?> generateCustomReport(@RequestParam("reportType") String reportType,
            @RequestParam(value = "parameters", required = false) String parameters) {
        try {
            Map<String, Object> report = analyticsService.runCustomAnalysis(reportType,
                    Map.of("parameters", parameters));
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/predictions/warranty-costs")
    @Operation(summary = "Warranty Cost Prediction", description = "Predict future warranty costs based on historical data and trends.", parameters = {
            @Parameter(name = "oemId", description = "OEM ID (required)", required = true, example = "1"),
            @Parameter(name = "timeframe", description = "Prediction timeframe in months", required = false, example = "12", schema = @Schema(defaultValue = "12"))
    })
    public ResponseEntity<?> predictWarrantyCosts(@RequestParam(value = "oemId", required = false) Long oemId,
            @RequestParam(value = "timeframe", required = false, defaultValue = "12") Integer timeframe) {
        try {
            if (oemId != null) {
                LocalDate fromDate = LocalDate.now();
                LocalDate toDate = LocalDate.now().plusMonths(timeframe);
                Double prediction = analyticsService.predictWarrantyCost(oemId, fromDate, toDate);
                return ResponseEntity.ok(Map.of("predictedCost", prediction));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "OEM ID is required"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}