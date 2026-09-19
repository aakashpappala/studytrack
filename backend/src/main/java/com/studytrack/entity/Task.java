package com.studytrack.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private RoadmapModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate assignedDate;

    private LocalDate dueDate;

    @Column(nullable = false)
    private Integer estimatedDurationMinutes = 60;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.NOT_STARTED;

    private LocalDateTime completedAt;
    private String proofType;

    @Column(columnDefinition = "TEXT")
    private String proofUrl;

    private LocalDateTime submittedAt;

    private LocalDateTime verifiedAt;

    @Column(columnDefinition = "TEXT")
    private String adminMessage;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.assignedDate == null) {
            this.assignedDate = LocalDate.now();
        }
        if (this.priority == null) {
            this.priority = TaskPriority.MEDIUM;
        }
        if (this.status == null) {
            this.status = TaskStatus.NOT_STARTED;
        }
    }

    public Task() {}

    public Task(
            Long id,
            Student student,
            Roadmap roadmap,
            Subject subject,
            RoadmapModule module,
            Topic topic,
            String title,
            String description,
            LocalDate assignedDate,
            LocalDate dueDate,
            Integer estimatedDurationMinutes,
            TaskPriority priority,
            TaskStatus status,
            LocalDateTime completedAt,
            String proofType,
            String proofUrl,
            LocalDateTime submittedAt,
            LocalDateTime verifiedAt,
            String adminMessage,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.student = student;
        this.roadmap = roadmap;
        this.subject = subject;
        this.module = module;
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.priority = priority;
        this.status = status;
        this.completedAt = completedAt;
        this.proofType = proofType;
        this.proofUrl = proofUrl;
        this.submittedAt = submittedAt;
        this.verifiedAt = verifiedAt;
        this.adminMessage = adminMessage;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Roadmap getRoadmap() {
        return this.roadmap;
    }

    public void setRoadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public RoadmapModule getModule() {
        return this.module;
    }

    public void setModule(RoadmapModule module) {
        this.module = module;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAssignedDate() {
        return this.assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimatedDurationMinutes() {
        return this.estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public TaskPriority getPriority() {
        return this.priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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


    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private Long id;
        private Student student;
        private Roadmap roadmap;
        private Subject subject;
        private RoadmapModule module;
        private Topic topic;
        private String title;
        private String description;
        private LocalDate assignedDate;
        private LocalDate dueDate;
        private Integer estimatedDurationMinutes;
        private TaskPriority priority;
        private TaskStatus status;

        private LocalDateTime completedAt;
        private String proofType;
        private String proofUrl;
        private LocalDateTime submittedAt;
        private LocalDateTime verifiedAt;
        private String adminMessage;
        private LocalDateTime createdAt;

        public TaskBuilder() {}

        public TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public TaskBuilder student(Student student) {
            this.student = student;
            return this;
        }
        public TaskBuilder roadmap(Roadmap roadmap) {
            this.roadmap = roadmap;
            return this;
        }
        public TaskBuilder subject(Subject subject) {
            this.subject = subject;
            return this;
        }
        public TaskBuilder module(RoadmapModule module) {
            this.module = module;
            return this;
        }
        public TaskBuilder topic(Topic topic) {
            this.topic = topic;
            return this;
        }
        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }
        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }
        public TaskBuilder assignedDate(LocalDate assignedDate) {
            this.assignedDate = assignedDate;
            return this;
        }
        public TaskBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }
        public TaskBuilder estimatedDurationMinutes(Integer estimatedDurationMinutes) {
            this.estimatedDurationMinutes = estimatedDurationMinutes;
            return this;
        }
        public TaskBuilder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }
        public TaskBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }
        public TaskBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }
        public TaskBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public TaskBuilder proofType(String proofType) {
            this.proofType = proofType;
            return this;
        }

        public TaskBuilder proofUrl(String proofUrl) {
            this.proofUrl = proofUrl;
            return this;
        }

        public TaskBuilder submittedAt(LocalDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public TaskBuilder verifiedAt(LocalDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
            return this;
        }

        public TaskBuilder adminMessage(String adminMessage) {
            this.adminMessage = adminMessage;
            return this;
        }

        public Task build() {
            return new Task(
                    this.id,
                    this.student,
                    this.roadmap,
                    this.subject,
                    this.module,
                    this.topic,
                    this.title,
                    this.description,
                    this.assignedDate,
                    this.dueDate,
                    this.estimatedDurationMinutes,
                    this.priority,
                    this.status,
                    this.completedAt,
                    this.proofType,
                    this.proofUrl,
                    this.submittedAt,
                    this.verifiedAt,
                    this.adminMessage,
                    this.createdAt
            );        }
    }

}
