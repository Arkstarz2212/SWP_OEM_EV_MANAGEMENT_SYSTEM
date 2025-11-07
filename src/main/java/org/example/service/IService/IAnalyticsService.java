package org.example.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IAnalyticsService {
    // Predictive Analytics
    Map<String, Object> predictFailurePatterns(String partNumber, String vehicleModel, Integer modelYear);

    List<Map<String, Object>> identifyHighRiskVehicles(Long oemId, String riskType);

    Double predictWarrantyCost(Long oemId, LocalDate fromDate, LocalDate toDate);

    Map<String, Object> analyzeBatteryDegradationTrends(List<String> vins);

    // Failure Pattern Recognition
    List<Map<String, Object>> getCommonFailureModes(String componentName, Long oemId);

    Map<String, Object> analyzeFailureCorrelations(String primaryComponent, Long oemId);

    List<Map<String, Object>> identifyRecurringIssues(Long serviceCenterId, Integer timeFrameDays);

    Map<String, Object> getFailureRateByRegion(String componentName, Long oemId);

    // Warranty Cost Analytics
    Map<String, Object> getWarrantyCostBreakdown(Long oemId, LocalDate fromDate, LocalDate toDate);

    List<Map<String, Object>> getTopCostDrivers(Long oemId, String timeFrame);

    Double calculateAverageClaimCost(String componentName, Long oemId);

    Map<String, Object> analyzeCostTrendsByModel(String vehicleModel, Long oemId);

    // Performance Analytics
    Map<String, Object> getServiceCenterPerformanceMetrics(Long serviceCenterId, String timeFrame);

    Double calculateCustomerSatisfactionScore(Long serviceCenterId);

    Map<String, Object> getRepairTimeAnalytics(String componentName, Long serviceCenterId);

    List<Map<String, Object>> identifyBottleneckProcesses(Long organizationId);

    // Recall Effectiveness
    Map<String, Object> analyzeRecallEffectiveness(String recallDetails);

    Double getRecallCompletionRate(String recallDetails, String timeFrame);

    Map<String, Object> compareRecallPerformance(List<String> recallDetails);

    List<Map<String, Object>> identifySlowRecallRegions(String recallDetails);

    // Quality & Reliability Metrics
    Double calculateFirstTimeFixRate(Long serviceCenterId, String timeFrame);

    Map<String, Object> getQualityMetricsByTechnician(Long technicianId, String timeFrame);

    List<Map<String, Object>> identifyQualityIssues(Long oemId, String severityLevel);

    Double getRecurrenceRate(String componentName, Long oemId);

    // Business Intelligence
    Map<String, Object> generateExecutiveDashboard(Long oemId, String timeFrame);

    List<Map<String, Object>> getKPITrends(Long organizationId, List<String> kpiNames, String timeFrame);

    Map<String, Object> benchmarkPerformance(Long organizationId, String benchmarkType);

    List<Map<String, Object>> identifyImprovementOpportunities(Long organizationId);

    // Inventory & Supply Chain Analytics
    Map<String, Object> optimizeInventoryLevels(Long locationId, String optimizationCriteria);

    List<Map<String, Object>> predictPartsDemand(Long oemId, Integer forecastDays);

    Map<String, Object> analyzeSupplyChainEfficiency(Long oemId, String timeFrame);

    Double calculateInventoryTurnoverRate(Long locationId, String timeFrame);

    // ML Model Management
    Map<String, Object> trainPredictionModel(String modelType, String datasetName, Map<String, Object> parameters);

    Map<String, Object> evaluateModelPerformance(String modelId, String evaluationMetric);

    boolean deployModel(String modelId, String deploymentEnvironment);

    List<Map<String, Object>> getAvailableModels(String modelType);

    // Custom Analytics
    Map<String, Object> runCustomAnalysis(String analysisQuery, Map<String, Object> parameters);

    Map<String, Object> createAnalyticsDashboard(String dashboardName, List<Map<String, Object>> widgets);

    boolean scheduleAnalyticsReport(String reportName, String frequency, List<String> recipients);

    // Inventory Statistics
    Map<String, Object> getPartStatistics(Long oemId);

    Map<String, Object> getVehicleStatistics(Long oemId);
}