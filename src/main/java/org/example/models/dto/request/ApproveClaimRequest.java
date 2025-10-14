package org.example.models.dto.request;

public class ApproveClaimRequest {
    // Simplified: approval action aligns to setting status to APPROVED in core
    private String note; // optional note stored via status log, not in core table

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
