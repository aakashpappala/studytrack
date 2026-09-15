package com.studytrack.dto.studylog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateStudyLogRequest {
    private LocalDate date;

    @NotBlank(message = "Topic name is required")
    private String topicName;

    private String startTime;
    private String endTime;

    @NotNull(message = "Duration in minutes is required")
    private Integer durationMinutes;

    private String notes;

    public CreateStudyLogRequest() {}

    public CreateStudyLogRequest(LocalDate date, String topicName, String startTime, String endTime, Integer durationMinutes, String notes) {
        this.date = date;
        this.topicName = topicName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
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


    public static CreateStudyLogRequestBuilder builder() {
        return new CreateStudyLogRequestBuilder();
    }

    public static class CreateStudyLogRequestBuilder {
        private LocalDate date;
        private String topicName;
        private String startTime;
        private String endTime;
        private Integer durationMinutes;
        private String notes;

        public CreateStudyLogRequestBuilder() {}

        public CreateStudyLogRequestBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public CreateStudyLogRequestBuilder topicName(String topicName) {
            this.topicName = topicName;
            return this;
        }
        public CreateStudyLogRequestBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }
        public CreateStudyLogRequestBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }
        public CreateStudyLogRequestBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }
        public CreateStudyLogRequestBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public CreateStudyLogRequest build() {
            return new CreateStudyLogRequest(this.date, this.topicName, this.startTime, this.endTime, this.durationMinutes, this.notes);
        }
    }

}
