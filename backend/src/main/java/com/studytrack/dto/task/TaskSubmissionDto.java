package com.studytrack.dto.task;

import java.time.LocalDateTime;
import java.util.List;

public class TaskSubmissionDto {

    private Long id;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;
    private String adminMessage;
    private List<TaskProofDto> proofs;

    public TaskSubmissionDto() {
    }

    public TaskSubmissionDto(
            Long id,
            String status,
            LocalDateTime submittedAt,
            LocalDateTime verifiedAt,
            String adminMessage,
            List<TaskProofDto> proofs) {

        this.id = id;
        this.status = status;
        this.submittedAt = submittedAt;
        this.verifiedAt = verifiedAt;
        this.adminMessage = adminMessage;
        this.proofs = proofs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }

    public List<TaskProofDto> getProofs() {
        return proofs;
    }

    public void setProofs(List<TaskProofDto> proofs) {
        this.proofs = proofs;
    }
}