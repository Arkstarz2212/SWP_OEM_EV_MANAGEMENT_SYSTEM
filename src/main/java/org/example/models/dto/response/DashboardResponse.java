package org.example.models.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private ClaimStatistics claimStatistics;
    private RecallStatistics recallStatistics;
    private TechnicianPerformance technicianPerformance;
    private InventoryStatus inventoryStatus;
    private List<RecentActivity> recentActivities;

    public ClaimStatistics getClaimStatistics() {
        return claimStatistics;
    }

    public void setClaimStatistics(ClaimStatistics claimStatistics) {
        this.claimStatistics = claimStatistics;
    }

    public RecallStatistics getRecallStatistics() {
        return recallStatistics;
    }

    public void setRecallStatistics(RecallStatistics recallStatistics) {
        this.recallStatistics = recallStatistics;
    }

    public TechnicianPerformance getTechnicianPerformance() {
        return technicianPerformance;
    }

    public void setTechnicianPerformance(TechnicianPerformance technicianPerformance) {
        this.technicianPerformance = technicianPerformance;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public List<RecentActivity> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<RecentActivity> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public static class ClaimStatistics {
        private Integer totalClaims;
        private Integer pendingClaims;
        private Integer approvedClaims;
        private Integer rejectedClaims;
        private Integer completedClaims;
        private Double totalCost;
        private Map<String, Integer> claimsByStatus;

        public Integer getTotalClaims() {
            return totalClaims;
        }

        public void setTotalClaims(Integer totalClaims) {
            this.totalClaims = totalClaims;
        }

        public Integer getPendingClaims() {
            return pendingClaims;
        }

        public void setPendingClaims(Integer pendingClaims) {
            this.pendingClaims = pendingClaims;
        }

        public Integer getApprovedClaims() {
            return approvedClaims;
        }

        public void setApprovedClaims(Integer approvedClaims) {
            this.approvedClaims = approvedClaims;
        }

        public Integer getRejectedClaims() {
            return rejectedClaims;
        }

        public void setRejectedClaims(Integer rejectedClaims) {
            this.rejectedClaims = rejectedClaims;
        }

        public Integer getCompletedClaims() {
            return completedClaims;
        }

        public void setCompletedClaims(Integer completedClaims) {
            this.completedClaims = completedClaims;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }

        public Map<String, Integer> getClaimsByStatus() {
            return claimsByStatus;
        }

        public void setClaimsByStatus(Map<String, Integer> claimsByStatus) {
            this.claimsByStatus = claimsByStatus;
        }
    }

    public static class RecallStatistics {
        private Integer activeRecalls;
        private Integer completedRecalls;
        private Integer totalAffectedVehicles;
        private Integer completedVehicles;
        private Double totalCost;

        public Integer getActiveRecalls() {
            return activeRecalls;
        }

        public void setActiveRecalls(Integer activeRecalls) {
            this.activeRecalls = activeRecalls;
        }

        public Integer getCompletedRecalls() {
            return completedRecalls;
        }

        public void setCompletedRecalls(Integer completedRecalls) {
            this.completedRecalls = completedRecalls;
        }

        public Integer getTotalAffectedVehicles() {
            return totalAffectedVehicles;
        }

        public void setTotalAffectedVehicles(Integer totalAffectedVehicles) {
            this.totalAffectedVehicles = totalAffectedVehicles;
        }

        public Integer getCompletedVehicles() {
            return completedVehicles;
        }

        public void setCompletedVehicles(Integer completedVehicles) {
            this.completedVehicles = completedVehicles;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }
    }

    public static class TechnicianPerformance {
        private Integer totalTechnicians;
        private Integer activeTechnicians;
        private Double averageRepairTime;
        private List<TechnicianInfo> topPerformers;

        public Integer getTotalTechnicians() {
            return totalTechnicians;
        }

        public void setTotalTechnicians(Integer totalTechnicians) {
            this.totalTechnicians = totalTechnicians;
        }

        public Integer getActiveTechnicians() {
            return activeTechnicians;
        }

        public void setActiveTechnicians(Integer activeTechnicians) {
            this.activeTechnicians = activeTechnicians;
        }

        public Double getAverageRepairTime() {
            return averageRepairTime;
        }

        public void setAverageRepairTime(Double averageRepairTime) {
            this.averageRepairTime = averageRepairTime;
        }

        public List<TechnicianInfo> getTopPerformers() {
            return topPerformers;
        }

        public void setTopPerformers(List<TechnicianInfo> topPerformers) {
            this.topPerformers = topPerformers;
        }

        public static class TechnicianInfo {
            private String name;
            private Integer completedJobs;
            private Double averageTime;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Integer getCompletedJobs() {
                return completedJobs;
            }

            public void setCompletedJobs(Integer completedJobs) {
                this.completedJobs = completedJobs;
            }

            public Double getAverageTime() {
                return averageTime;
            }

            public void setAverageTime(Double averageTime) {
                this.averageTime = averageTime;
            }
        }
    }

    public static class InventoryStatus {
        private Integer totalParts;
        private Integer lowStockParts;
        private Integer outOfStockParts;
        private List<String> criticalParts;

        public Integer getTotalParts() {
            return totalParts;
        }

        public void setTotalParts(Integer totalParts) {
            this.totalParts = totalParts;
        }

        public Integer getLowStockParts() {
            return lowStockParts;
        }

        public void setLowStockParts(Integer lowStockParts) {
            this.lowStockParts = lowStockParts;
        }

        public Integer getOutOfStockParts() {
            return outOfStockParts;
        }

        public void setOutOfStockParts(Integer outOfStockParts) {
            this.outOfStockParts = outOfStockParts;
        }

        public List<String> getCriticalParts() {
            return criticalParts;
        }

        public void setCriticalParts(List<String> criticalParts) {
            this.criticalParts = criticalParts;
        }
    }

    public static class RecentActivity {
        private String type;
        private String description;
        private String timestamp;
        private String user;

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

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}