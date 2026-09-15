package com.studytrack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_notes")
public class StudyNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String tags;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public StudyNote() {}

    public StudyNote(Long id, Student student, Topic topic, Task task, String title, String content, String tags, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.student = student;
        this.topic = topic;
        this.task = task;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public static StudyNoteBuilder builder() {
        return new StudyNoteBuilder();
    }

    public static class StudyNoteBuilder {
        private Long id;
        private Student student;
        private Topic topic;
        private Task task;
        private String title;
        private String content;
        private String tags;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StudyNoteBuilder() {}

        public StudyNoteBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudyNoteBuilder student(Student student) {
            this.student = student;
            return this;
        }
        public StudyNoteBuilder topic(Topic topic) {
            this.topic = topic;
            return this;
        }
        public StudyNoteBuilder task(Task task) {
            this.task = task;
            return this;
        }
        public StudyNoteBuilder title(String title) {
            this.title = title;
            return this;
        }
        public StudyNoteBuilder content(String content) {
            this.content = content;
            return this;
        }
        public StudyNoteBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }
        public StudyNoteBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public StudyNoteBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudyNote build() {
            return new StudyNote(this.id, this.student, this.topic, this.task, this.title, this.content, this.tags, this.createdAt, this.updatedAt);
        }
    }

}
