package com.studytrack.dto.note;

import java.time.LocalDateTime;

public class StudyNoteDto {
    private Long id;
    private Long studentId;
    private Long topicId;
    private String topicTitle;
    private Long taskId;
    private String taskTitle;
    private String title;
    private String content;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudyNoteDto() {}

    public StudyNoteDto(Long id, Long studentId, Long topicId, String topicTitle, Long taskId, String taskTitle, String title, String content, String tags, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.taskId = taskId;
        this.taskTitle = taskTitle;
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

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicTitle() {
        return this.topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return this.taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
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


    public static StudyNoteDtoBuilder builder() {
        return new StudyNoteDtoBuilder();
    }

    public static class StudyNoteDtoBuilder {
        private Long id;
        private Long studentId;
        private Long topicId;
        private String topicTitle;
        private Long taskId;
        private String taskTitle;
        private String title;
        private String content;
        private String tags;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StudyNoteDtoBuilder() {}

        public StudyNoteDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudyNoteDtoBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public StudyNoteDtoBuilder topicId(Long topicId) {
            this.topicId = topicId;
            return this;
        }
        public StudyNoteDtoBuilder topicTitle(String topicTitle) {
            this.topicTitle = topicTitle;
            return this;
        }
        public StudyNoteDtoBuilder taskId(Long taskId) {
            this.taskId = taskId;
            return this;
        }
        public StudyNoteDtoBuilder taskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
            return this;
        }
        public StudyNoteDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public StudyNoteDtoBuilder content(String content) {
            this.content = content;
            return this;
        }
        public StudyNoteDtoBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }
        public StudyNoteDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public StudyNoteDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudyNoteDto build() {
            return new StudyNoteDto(this.id, this.studentId, this.topicId, this.topicTitle, this.taskId, this.taskTitle, this.title, this.content, this.tags, this.createdAt, this.updatedAt);
        }
    }

}
