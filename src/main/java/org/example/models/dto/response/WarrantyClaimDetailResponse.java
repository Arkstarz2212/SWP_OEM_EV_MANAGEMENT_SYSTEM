package org.example.models.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.enums.ClaimStatus;

public class WarrantyClaimDetailResponse {
    private Long claimId;
    private String claimNumber;
    private String vin;
    private String vehicleModel;
    private CustomerInfo customer;
    private ServiceCenterInfo serviceCenter;
    private String dtcCode;
    private String issueDescription;
    private Integer mileageKmAtClaim;
    private ClaimStatus status;
    private OffsetDateTime submitDate;
    private OffsetDateTime approvalDate;
    // Simplified: drop notes/cost/attachments, keep status history mapping from
    // logs
    private List<StatusHistoryInfo> statusHistory;

    // Getters and Setters
    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public CustomerInfo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInfo customer) {
        this.customer = customer;
    }

    public ServiceCenterInfo getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(ServiceCenterInfo serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String getDtcCode() {
        return dtcCode;
    }

    public void setDtcCode(String dtcCode) {
        this.dtcCode = dtcCode;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public Integer getMileageKmAtClaim() {
        return mileageKmAtClaim;
    }

    public void setMileageKmAtClaim(Integer mileageKmAtClaim) {
        this.mileageKmAtClaim = mileageKmAtClaim;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public OffsetDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(OffsetDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public OffsetDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(OffsetDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    // completionDate removed to match core schema

    public List<StatusHistoryInfo> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<StatusHistoryInfo> statusHistory) {
        this.statusHistory = statusHistory;
    }

    // Nested classes
    public static class CustomerInfo {
        private String fullName;
        private String phoneNumber;
        private String email;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ServiceCenterInfo {
        private String name;
        private String address;
        private String contactPerson;
        private String phoneNumber;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    public static class StatusHistoryInfo {
        private ClaimStatus fromStatus;
        private ClaimStatus toStatus;
        private java.time.OffsetDateTime changeDate;
        private String changedBy;
        private String notes;

        public ClaimStatus getFromStatus() {
            return fromStatus;
        }

        public void setFromStatus(ClaimStatus fromStatus) {
            this.fromStatus = fromStatus;
        }

        public ClaimStatus getToStatus() {
            return toStatus;
        }

        public void setToStatus(ClaimStatus toStatus) {
            this.toStatus = toStatus;
        }

        public java.time.OffsetDateTime getChangeDate() {
            return changeDate;
        }

        public void setChangeDate(java.time.OffsetDateTime changeDate) {
            this.changeDate = changeDate;
        }

        public String getChangedBy() {
            return changedBy;
        }

        public void setChangedBy(String changedBy) {
            this.changedBy = changedBy;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

}