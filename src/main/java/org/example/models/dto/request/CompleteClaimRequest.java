package org.example.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class CompleteClaimRequest {
    @NotBlank(message = "Báo cáo hoàn thành không được để trống")
    private String completionReport;

    private BigDecimal actualCost;
    private String technicianNotes;
    private String customerFeedback;

    public String getCompletionReport() {
        return completionReport;
    }

    public void setCompletionReport(String completionReport) {
        this.completionReport = completionReport;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getTechnicianNotes() {
        return technicianNotes;
    }

    public void setTechnicianNotes(String technicianNotes) {
        this.technicianNotes = technicianNotes;
    }

    public String getCustomerFeedback() {
        return customerFeedback;
    }

    public void setCustomerFeedback(String customerFeedback) {
        this.customerFeedback = customerFeedback;
    }
}
