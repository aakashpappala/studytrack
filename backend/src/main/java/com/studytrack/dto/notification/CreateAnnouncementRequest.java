package com.studytrack.dto.notification;

import com.studytrack.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
public class CreateAnnouncementRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private TaskPriority priority = TaskPriority.MEDIUM;

    public CreateAnnouncementRequest() {}

    public CreateAnnouncementRequest(String title, String content, TaskPriority priority) {
        this.title = title;
        this.content = content;
        this.priority = priority;
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


    public static CreateAnnouncementRequestBuilder builder() {
        return new CreateAnnouncementRequestBuilder();
    }

    public static class CreateAnnouncementRequestBuilder {
        private String title;
        private String content;
        private TaskPriority priority;

        public CreateAnnouncementRequestBuilder() {}

        public CreateAnnouncementRequestBuilder title(String title) {
            this.title = title;
            return this;
        }
        public CreateAnnouncementRequestBuilder content(String content) {
            this.content = content;
            return this;
        }
        public CreateAnnouncementRequestBuilder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }

        public CreateAnnouncementRequest build() {
            return new CreateAnnouncementRequest(this.title, this.content, this.priority);
        }
    }

}
