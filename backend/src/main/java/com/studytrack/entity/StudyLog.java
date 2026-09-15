package com.studytrack.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_logs")
public class StudyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 150)
    private String topicName;

    @Column(length = 20)
    private String startTime;

    @Column(length = 20)
    private String endTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }

    public StudyLog() {}

    public StudyLog(Long id, Student student, LocalDate date, String topicName, String startTime, String endTime, Integer durationMinutes, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.student = student;
        this.date = date;
        this.topicName = topicName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
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

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return this.durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public static StudyLogBuilder builder() {
        return new StudyLogBuilder();
    }

    public static class StudyLogBuilder {
        private Long id;
        private Student student;
        private LocalDate date;
        private String topicName;
        private String startTime;
        private String endTime;
        private Integer durationMinutes;
        private String notes;
        private LocalDateTime createdAt;

        public StudyLogBuilder() {}

        public StudyLogBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudyLogBuilder student(Student student) {
            this.student = student;
            return this;
        }
        public StudyLogBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public StudyLogBuilder topicName(String topicName) {
            this.topicName = topicName;
            return this;
        }
        public StudyLogBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }
        public StudyLogBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }
        public StudyLogBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }
        public StudyLogBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }
        public StudyLogBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudyLog build() {
            return new StudyLog(this.id, this.student, this.date, this.topicName, this.startTime, this.endTime, this.durationMinutes, this.notes, this.createdAt);
        }
    }

}
