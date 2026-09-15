package com.studytrack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.priority == null) {
            this.priority = TaskPriority.MEDIUM;
        }
    }

    public Announcement() {}

    public Announcement(Long id, User admin, String title, String content, TaskPriority priority, LocalDateTime createdAt) {
        this.id = id;
        this.admin = admin;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAdmin() {
        return this.admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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

    public TaskPriority getPriority() {
        return this.priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public static AnnouncementBuilder builder() {
        return new AnnouncementBuilder();
    }

    public static class AnnouncementBuilder {
        private Long id;
        private User admin;
        private String title;
        private String content;
        private TaskPriority priority;
        private LocalDateTime createdAt;

        public AnnouncementBuilder() {}

        public AnnouncementBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public AnnouncementBuilder admin(User admin) {
            this.admin = admin;
            return this;
        }
        public AnnouncementBuilder title(String title) {
            this.title = title;
            return this;
        }
        public AnnouncementBuilder content(String content) {
            this.content = content;
            return this;
        }
        public AnnouncementBuilder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }
        public AnnouncementBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Announcement build() {
            return new Announcement(this.id, this.admin, this.title, this.content, this.priority, this.createdAt);
        }
    }

}
