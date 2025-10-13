package org.example.models.dto.request;

import java.util.List;

public class WarrantyClaimResultRequest {
    private Long claimId;
    private String workPerformed;
    private List<PartsUsed> partsUsed;
    private Double totalLaborHours;
    private Double totalCost;
    private String technicianNotes;
    private Integer finalOdometerKm;

    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }

    public String getWorkPerformed() { return workPerformed; }
    public void setWorkPerformed(String workPerformed) { this.workPerformed = workPerformed; }

    public List<PartsUsed> getPartsUsed() { return partsUsed; }
    public void setPartsUsed(List<PartsUsed> partsUsed) { this.partsUsed = partsUsed; }

    public Double getTotalLaborHours() { return totalLaborHours; }
    public void setTotalLaborHours(Double totalLaborHours) { this.totalLaborHours = totalLaborHours; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }

    public String getTechnicianNotes() { return technicianNotes; }
    public void setTechnicianNotes(String technicianNotes) { this.technicianNotes = technicianNotes; }

    public Integer getFinalOdometerKm() { return finalOdometerKm; }
    public void setFinalOdometerKm(Integer finalOdometerKm) { this.finalOdometerKm = finalOdometerKm; }

    public static class PartsUsed {
        private String partNumber;
        private String partName;
        private Integer quantity;
        private Double unitCost;
        private String serialNumber;

        public String getPartNumber() { return partNumber; }
        public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

        public String getPartName() { return partName; }
        public void setPartName(String partName) { this.partName = partName; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Double getUnitCost() { return unitCost; }
        public void setUnitCost(Double unitCost) { this.unitCost = unitCost; }

        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    }
}