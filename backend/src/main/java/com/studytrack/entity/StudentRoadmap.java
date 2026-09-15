package com.studytrack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_roadmaps")
public class StudentRoadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, COMPLETED

    @Column(nullable = false)
    private Double completionPercentage = 0.0;

    @PrePersist
    protected void onCreate() {
        if (this.assignedAt == null) {
            this.assignedAt = LocalDateTime.now();
        }
        if (this.completionPercentage == null) {
            this.completionPercentage = 0.0;
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }

    public StudentRoadmap() {}

    public StudentRoadmap(Long id, Student student, Roadmap roadmap, LocalDateTime assignedAt, String status, Double completionPercentage) {
        this.id = id;
        this.student = student;
        this.roadmap = roadmap;
        this.assignedAt = assignedAt;
        this.status = status;
        this.completionPercentage = completionPercentage;
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

    public LocalDateTime getAssignedAt() {
        return this.assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCompletionPercentage() {
        return this.completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }


    public static StudentRoadmapBuilder builder() {
        return new StudentRoadmapBuilder();
    }

    public static class StudentRoadmapBuilder {
        private Long id;
        private Student student;
        private Roadmap roadmap;
        private LocalDateTime assignedAt;
        private String status;
        private Double completionPercentage;

        public StudentRoadmapBuilder() {}

        public StudentRoadmapBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudentRoadmapBuilder student(Student student) {
            this.student = student;
            return this;
        }
        public StudentRoadmapBuilder roadmap(Roadmap roadmap) {
            this.roadmap = roadmap;
            return this;
        }
        public StudentRoadmapBuilder assignedAt(LocalDateTime assignedAt) {
            this.assignedAt = assignedAt;
            return this;
        }
        public StudentRoadmapBuilder status(String status) {
            this.status = status;
            return this;
        }
        public StudentRoadmapBuilder completionPercentage(Double completionPercentage) {
            this.completionPercentage = completionPercentage;
            return this;
        }

        public StudentRoadmap build() {
            return new StudentRoadmap(this.id, this.student, this.roadmap, this.assignedAt, this.status, this.completionPercentage);
        }
    }

}
