package org.example.models.dto.request;

import java.time.LocalDate;

public class ReportGenerateRequest {
    private String reportType; // WARRANTY_CLAIMS, RECALL_PROGRESS, TECHNICIAN_PERFORMANCE, etc.
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long serviceCenterId;
    private String vehicleModel;
    private String format; // PDF, EXCEL, CSV

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Long getServiceCenterId() {
        return serviceCenterId;
    }

    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}