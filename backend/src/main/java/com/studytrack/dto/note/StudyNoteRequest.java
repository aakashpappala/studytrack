package com.studytrack.dto.note;

import jakarta.validation.constraints.NotBlank;
public class StudyNoteRequest {
    private Long topicId;
    private Long taskId;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;
    private String tags;

    public StudyNoteRequest() {}

    public StudyNoteRequest(Long topicId, Long taskId, String title, String content, String tags) {
        this.topicId = topicId;
        this.taskId = taskId;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public Long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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


    public static StudyNoteRequestBuilder builder() {
        return new StudyNoteRequestBuilder();
    }

    public static class StudyNoteRequestBuilder {
        private Long topicId;
        private Long taskId;
        private String title;
        private String content;
        private String tags;

        public StudyNoteRequestBuilder() {}

        public StudyNoteRequestBuilder topicId(Long topicId) {
            this.topicId = topicId;
            return this;
        }
        public StudyNoteRequestBuilder taskId(Long taskId) {
            this.taskId = taskId;
            return this;
        }
        public StudyNoteRequestBuilder title(String title) {
            this.title = title;
            return this;
        }
        public StudyNoteRequestBuilder content(String content) {
            this.content = content;
            return this;
        }
        public StudyNoteRequestBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public StudyNoteRequest build() {
            return new StudyNoteRequest(this.topicId, this.taskId, this.title, this.content, this.tags);
        }
    }

}
