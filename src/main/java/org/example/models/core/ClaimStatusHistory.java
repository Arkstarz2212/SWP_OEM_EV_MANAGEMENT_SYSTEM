package org.example.models.core;

import java.time.OffsetDateTime;

import org.example.models.enums.ClaimStatus;

public class ClaimStatusHistory {
    private Long id;
    private Long claimId;
    private ClaimStatus fromStatus; // Previous status
    private ClaimStatus toStatus; // New status
    private Long changedByUserId;
    private String note;
    private OffsetDateTime changedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

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

    public Long getChangedByUserId() {
        return changedByUserId;
    }

    public void setChangedByUserId(Long changedByUserId) {
        this.changedByUserId = changedByUserId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(OffsetDateTime changedAt) {
        this.changedAt = changedAt;
    }

    // Alias method for compatibility
    public Long getWarrantyClaimId() {
        return claimId;
    }

    public void setWarrantyClaimId(Long warrantyClaimId) {
        this.claimId = warrantyClaimId;
    }

    public void setStatus(ClaimStatus status) {
        this.toStatus = status;
    }

    public ClaimStatus getStatus() {
        return toStatus;
    }
}
