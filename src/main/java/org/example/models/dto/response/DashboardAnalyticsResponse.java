package org.example.models.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for dashboard analytics based on user role
 */
public class DashboardAnalyticsResponse {
    private String userRole;
    private LocalDate reportDate;
    private String period; // "daily", "weekly", "monthly", "yearly"

    // General metrics
    private GeneralMetrics generalMetrics;
    private List<ChartData> chartData;
    private List<RecentActivity> recentActivities;
    private List<Alert> alerts;

    // Role-specific metrics
    private ServiceCenterMetrics serviceCenterMetrics; // For SC_Staff, SC_Technician
    private EvmMetrics evmMetrics; // For EVM_Staff
    private SystemMetrics systemMetrics; // For Admin

    // Inner classes
    public static class GeneralMetrics {
        private Integer totalVehicles;
        private Integer activeWarrantyClaims;
        private Integer activeRecalls;
        private Integer pendingApprovals;
        private BigDecimal totalWarrantyCosts;
        private Double avgClaimResolutionDays;

        // Getters and Setters
        public Integer getTotalVehicles() {
            return totalVehicles;
        }

        public void setTotalVehicles(Integer totalVehicles) {
            this.totalVehicles = totalVehicles;
        }

        public Integer getActiveWarrantyClaims() {
            return activeWarrantyClaims;
        }

        public void setActiveWarrantyClaims(Integer activeWarrantyClaims) {
            this.activeWarrantyClaims = activeWarrantyClaims;
        }

        public Integer getActiveRecalls() {
            return activeRecalls;
        }

        public void setActiveRecalls(Integer activeRecalls) {
            this.activeRecalls = activeRecalls;
        }

        public Integer getPendingApprovals() {
            return pendingApprovals;
        }

        public void setPendingApprovals(Integer pendingApprovals) {
            this.pendingApprovals = pendingApprovals;
        }

        public BigDecimal getTotalWarrantyCosts() {
            return totalWarrantyCosts;
        }

        public void setTotalWarrantyCosts(BigDecimal totalWarrantyCosts) {
            this.totalWarrantyCosts = totalWarrantyCosts;
        }

        public Double getAvgClaimResolutionDays() {
            return avgClaimResolutionDays;
        }

        public void setAvgClaimResolutionDays(Double avgClaimResolutionDays) {
            this.avgClaimResolutionDays = avgClaimResolutionDays;
        }
    }

    public static class ChartData {
        private String label;
        private String type; // "line", "bar", "pie", "doughnut"
        private List<String> labels;
        private List<Map<String, Object>> datasets;

        // Getters and Setters
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public List<Map<String, Object>> getDatasets() {
            return datasets;
        }

        public void setDatasets(List<Map<String, Object>> datasets) {
            this.datasets = datasets;
        }
    }

    public static class RecentActivity {
        private String type; // "claim_created", "recall_launched", "approval_given"
        private String description;
        private LocalDate timestamp;
        private String priority; // "high", "medium", "low"

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDate timestamp) {
            this.timestamp = timestamp;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }
    }

    public static class Alert {
        private String level; // "info", "warning", "error", "success"
        private String message;
        private String actionRequired;
        private LocalDate timestamp;

        // Getters and Setters
        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getActionRequired() {
            return actionRequired;
        }

        public void setActionRequired(String actionRequired) {
            this.actionRequired = actionRequired;
        }

        public LocalDate getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDate timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class ServiceCenterMetrics {
        private Integer myAssignedClaims;
        private Integer claimsInProgress;
        private Integer completedThisMonth;
        private Double myPerformanceRating;
        private Integer partsAwaitingDelivery;

        // Getters and Setters
        public Integer getMyAssignedClaims() {
            return myAssignedClaims;
        }

        public void setMyAssignedClaims(Integer myAssignedClaims) {
            this.myAssignedClaims = myAssignedClaims;
        }

        public Integer getClaimsInProgress() {
            return claimsInProgress;
        }

        public void setClaimsInProgress(Integer claimsInProgress) {
            this.claimsInProgress = claimsInProgress;
        }

        public Integer getCompletedThisMonth() {
            return completedThisMonth;
        }

        public void setCompletedThisMonth(Integer completedThisMonth) {
            this.completedThisMonth = completedThisMonth;
        }

        public Double getMyPerformanceRating() {
            return myPerformanceRating;
        }

        public void setMyPerformanceRating(Double myPerformanceRating) {
            this.myPerformanceRating = myPerformanceRating;
        }

        public Integer getPartsAwaitingDelivery() {
            return partsAwaitingDelivery;
        }

        public void setPartsAwaitingDelivery(Integer partsAwaitingDelivery) {
            this.partsAwaitingDelivery = partsAwaitingDelivery;
        }
    }

    public static class EvmMetrics {
        private Integer claimsAwaitingApproval;
        private BigDecimal monthlyWarrantyCosts;
        private Integer activeRecalls;
        private Integer partsLowInventory;
        private Double claimApprovalRate;

        // Getters and Setters
        public Integer getClaimsAwaitingApproval() {
            return claimsAwaitingApproval;
        }

        public void setClaimsAwaitingApproval(Integer claimsAwaitingApproval) {
            this.claimsAwaitingApproval = claimsAwaitingApproval;
        }

        public BigDecimal getMonthlyWarrantyCosts() {
            return monthlyWarrantyCosts;
        }

        public void setMonthlyWarrantyCosts(BigDecimal monthlyWarrantyCosts) {
            this.monthlyWarrantyCosts = monthlyWarrantyCosts;
        }

        public Integer getActiveRecalls() {
            return activeRecalls;
        }

        public void setActiveRecalls(Integer activeRecalls) {
            this.activeRecalls = activeRecalls;
        }

        public Integer getPartsLowInventory() {
            return partsLowInventory;
        }

        public void setPartsLowInventory(Integer partsLowInventory) {
            this.partsLowInventory = partsLowInventory;
        }

        public Double getClaimApprovalRate() {
            return claimApprovalRate;
        }

        public void setClaimApprovalRate(Double claimApprovalRate) {
            this.claimApprovalRate = claimApprovalRate;
        }
    }

    public static class SystemMetrics {
        private Integer totalUsers;
        private Integer activeUsers;
        private Integer systemErrors;
        private Double systemUptime;
        private Integer dataBackupStatus;

        // Getters and Setters
        public Integer getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Integer totalUsers) {
            this.totalUsers = totalUsers;
        }

        public Integer getActiveUsers() {
            return activeUsers;
        }

        public void setActiveUsers(Integer activeUsers) {
            this.activeUsers = activeUsers;
        }

        public Integer getSystemErrors() {
            return systemErrors;
        }

        public void setSystemErrors(Integer systemErrors) {
            this.systemErrors = systemErrors;
        }

        public Double getSystemUptime() {
            return systemUptime;
        }

        public void setSystemUptime(Double systemUptime) {
            this.systemUptime = systemUptime;
        }

        public Integer getDataBackupStatus() {
            return dataBackupStatus;
        }

        public void setDataBackupStatus(Integer dataBackupStatus) {
            this.dataBackupStatus = dataBackupStatus;
        }
    }

    // Constructors
    public DashboardAnalyticsResponse() {
    }

    // Getters and Setters
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public GeneralMetrics getGeneralMetrics() {
        return generalMetrics;
    }

    public void setGeneralMetrics(GeneralMetrics generalMetrics) {
        this.generalMetrics = generalMetrics;
    }

    public List<ChartData> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartData> chartData) {
        this.chartData = chartData;
    }

    public List<RecentActivity> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<RecentActivity> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public ServiceCenterMetrics getServiceCenterMetrics() {
        return serviceCenterMetrics;
    }

    public void setServiceCenterMetrics(ServiceCenterMetrics serviceCenterMetrics) {
        this.serviceCenterMetrics = serviceCenterMetrics;
    }

    public EvmMetrics getEvmMetrics() {
        return evmMetrics;
    }

    public void setEvmMetrics(EvmMetrics evmMetrics) {
        this.evmMetrics = evmMetrics;
    }

    public SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public void setSystemMetrics(SystemMetrics systemMetrics) {
        this.systemMetrics = systemMetrics;
    }
}