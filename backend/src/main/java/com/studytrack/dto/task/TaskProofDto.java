package com.studytrack.dto.task;

import java.time.LocalDateTime;

public class TaskProofDto {

    private Long id;
    private String proofType;
    private String proofUrl;
    private LocalDateTime uploadedAt;

    public TaskProofDto() {
    }

    public TaskProofDto(
            Long id,
            String proofType,
            String proofUrl,
            LocalDateTime uploadedAt) {

        this.id = id;
        this.proofType = proofType;
        this.proofUrl = proofUrl;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProofType() {
        return proofType;
    }

    public void setProofType(String proofType) {
        this.proofType = proofType;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}