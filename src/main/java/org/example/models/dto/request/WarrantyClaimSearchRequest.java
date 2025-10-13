package org.example.models.dto.request;

import org.example.models.enums.ClaimStatus;
import java.time.LocalDate;

public class WarrantyClaimSearchRequest {
    private String claimNumber;
    private String vin;
    private ClaimStatus status;
    private Long serviceCenterId;
    private LocalDate submitDateFrom;
    private LocalDate submitDateTo;
    private String customerName;
    private String customerPhone;

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public Long getServiceCenterId() { return serviceCenterId; }
    public void setServiceCenterId(Long serviceCenterId) { this.serviceCenterId = serviceCenterId; }

    public LocalDate getSubmitDateFrom() { return submitDateFrom; }
    public void setSubmitDateFrom(LocalDate submitDateFrom) { this.submitDateFrom = submitDateFrom; }

    public LocalDate getSubmitDateTo() { return submitDateTo; }
    public void setSubmitDateTo(LocalDate submitDateTo) { this.submitDateTo = submitDateTo; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
}