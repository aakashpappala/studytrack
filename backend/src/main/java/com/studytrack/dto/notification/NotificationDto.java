package com.studytrack.dto.notification;

import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private Long studentId;
    private String title;
    private String message;
    private String type; // TASK_ASSIGNED, TASK_DUE, ROADMAP_UPDATE, ANNOUNCEMENT, GENERAL
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public NotificationDto(Long id, Long studentId, String title, String message, String type, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isIsRead() {
        return this.isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public static NotificationDtoBuilder builder() {
        return new NotificationDtoBuilder();
    }

    public static class NotificationDtoBuilder {
        private Long id;
        private Long studentId;
        private String title;
        private String message;
        private String type;
        private Boolean isRead;
        private LocalDateTime createdAt;

        public NotificationDtoBuilder() {}

        public NotificationDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public NotificationDtoBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public NotificationDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public NotificationDtoBuilder message(String message) {
            this.message = message;
            return this;
        }
        public NotificationDtoBuilder type(String type) {
            this.type = type;
            return this;
        }
        public NotificationDtoBuilder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }
        public NotificationDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationDto build() {
            return new NotificationDto(this.id, this.studentId, this.title, this.message, this.type, this.isRead, this.createdAt);
        }
    }

}
