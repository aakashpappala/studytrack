package com.studytrack.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_proofs")
public class TaskProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Legacy task relation
     *
     * Existing database lo task_id column already undi.
     * Kabatti new history system tho paatu old task_id ni kuda maintain chestunnam.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    /*
     * New submission history relation
     *
     * Each proof belongs to one submission.
     */
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
            Task task,
            TaskSubmission submission,
            String proofType,
            String proofUrl,
            LocalDateTime uploadedAt) {

        this.id = id;
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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