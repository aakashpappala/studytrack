package com.studytrack.dto.notification;

import java.time.LocalDateTime;

public class AnnouncementDto {
    private Long id;
    private Long adminId;
    private String adminName;
    private String title;
    private String content;
    private String priority; // LOW, MEDIUM, HIGH
    private LocalDateTime createdAt;

    public AnnouncementDto() {}

    public AnnouncementDto(Long id, Long adminId, String adminName, String title, String content, String priority, LocalDateTime createdAt) {
        this.id = id;
        this.adminId = adminId;
        this.adminName = adminName;
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

    public Long getAdminId() {
        return this.adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return this.adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
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

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public static AnnouncementDtoBuilder builder() {
        return new AnnouncementDtoBuilder();
    }

    public static class AnnouncementDtoBuilder {
        private Long id;
        private Long adminId;
        private String adminName;
        private String title;
        private String content;
        private String priority;
        private LocalDateTime createdAt;

        public AnnouncementDtoBuilder() {}

        public AnnouncementDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public AnnouncementDtoBuilder adminId(Long adminId) {
            this.adminId = adminId;
            return this;
        }
        public AnnouncementDtoBuilder adminName(String adminName) {
            this.adminName = adminName;
            return this;
        }
        public AnnouncementDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public AnnouncementDtoBuilder content(String content) {
            this.content = content;
            return this;
        }
        public AnnouncementDtoBuilder priority(String priority) {
            this.priority = priority;
            return this;
        }
        public AnnouncementDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AnnouncementDto build() {
            return new AnnouncementDto(this.id, this.adminId, this.adminName, this.title, this.content, this.priority, this.createdAt);
        }
    }

}
