package com.studytrack.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_proofs")
public class TaskProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private TaskSubmission submission;

    @Column(nullable = false)
    private String proofType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String proofUrl;

    private LocalDateTime uploadedAt;

    public TaskProof() {
    }

    public TaskProof(
            Long id,
            TaskSubmission submission,
            String proofType,
            String proofUrl,
            LocalDateTime uploadedAt) {

        this.id = id;
        this.submission = submission;
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

    public TaskSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(TaskSubmission submission) {
        this.submission = submission;
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