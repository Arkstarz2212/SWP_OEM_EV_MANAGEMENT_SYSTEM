package org.example.models.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClaimData {
    @JsonProperty("parts_used")
    private List<ClaimPartInfo> partsUsed;

    @JsonProperty("attachments")
    private List<ClaimAttachmentInfo> attachments;

    @JsonProperty("status_history")
    private List<ClaimStatusHistoryInfo> statusHistory;

    @JsonProperty("rejection_reason")
    private String rejectionReason;

    // Default constructor
    public ClaimData() {
    }

    // Nested classes for complex data structures
    public static class ClaimPartInfo {
        @JsonProperty("part_id")
        private Long partId;

        @JsonProperty("part_number")
        private String partNumber;

        @JsonProperty("quantity")
        private Integer quantity;

        @JsonProperty("serial_number")
        private String serialNumber;

        // Constructors, getters and setters
        public ClaimPartInfo() {
        }

        public ClaimPartInfo(Long partId, String partNumber, Integer quantity, String serialNumber) {
            this.partId = partId;
            this.partNumber = partNumber;
            this.quantity = quantity;
            this.serialNumber = serialNumber;
        }

        public Long getPartId() {
            return partId;
        }

        public void setPartId(Long partId) {
            this.partId = partId;
        }

        public String getPartNumber() {
            return partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }
    }

    public static class ClaimAttachmentInfo {
        @JsonProperty("file_name")
        private String fileName;

        @JsonProperty("file_path")
        private String filePath;

        @JsonProperty("attachment_type")
        private String attachmentType;

        // Constructors, getters and setters
        public ClaimAttachmentInfo() {
        }

        public ClaimAttachmentInfo(String fileName, String filePath, String attachmentType) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.attachmentType = attachmentType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getAttachmentType() {
            return attachmentType;
        }

        public void setAttachmentType(String attachmentType) {
            this.attachmentType = attachmentType;
        }
    }

    public static class ClaimStatusHistoryInfo {
        @JsonProperty("status")
        private String status;

        @JsonProperty("changed_by")
        private Long changedBy;

        @JsonProperty("changed_at")
        private String changedAt;

        @JsonProperty("note")
        private String note;

        // Constructors, getters and setters
        public ClaimStatusHistoryInfo() {
        }

        public ClaimStatusHistoryInfo(String status, Long changedBy, String changedAt, String note) {
            this.status = status;
            this.changedBy = changedBy;
            this.changedAt = changedAt;
            this.note = note;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getChangedBy() {
            return changedBy;
        }

        public void setChangedBy(Long changedBy) {
            this.changedBy = changedBy;
        }

        public String getChangedAt() {
            return changedAt;
        }

        public void setChangedAt(String changedAt) {
            this.changedAt = changedAt;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    // Main class getters and setters
    public List<ClaimPartInfo> getPartsUsed() {
        return partsUsed;
    }

    public void setPartsUsed(List<ClaimPartInfo> partsUsed) {
        this.partsUsed = partsUsed;
    }

    public List<ClaimAttachmentInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ClaimAttachmentInfo> attachments) {
        this.attachments = attachments;
    }

    public List<ClaimStatusHistoryInfo> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<ClaimStatusHistoryInfo> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}