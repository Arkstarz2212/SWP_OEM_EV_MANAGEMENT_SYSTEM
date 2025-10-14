package org.example.models.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RejectClaimRequest {
    @NotBlank(message = "Lý do từ chối không được để trống")
    private String rejectionReason;

    private String reviewerComments;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getReviewerComments() {
        return reviewerComments;
    }

    public void setReviewerComments(String reviewerComments) {
        this.reviewerComments = reviewerComments;
    }
}
