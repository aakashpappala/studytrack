package com.studytrack.dto.studylog;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudyLogDto {
    private Long id;
    private Long studentId;
    private LocalDate date;
    private String topicName;
    private String startTime;
    private String endTime;
    private Integer durationMinutes;
    private String notes;
    private LocalDateTime createdAt;

    public StudyLogDto() {}

    public StudyLogDto(Long id, Long studentId, LocalDate date, String topicName, String startTime, String endTime, Integer durationMinutes, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
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

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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


    public static StudyLogDtoBuilder builder() {
        return new StudyLogDtoBuilder();
    }

    public static class StudyLogDtoBuilder {
        private Long id;
        private Long studentId;
        private LocalDate date;
        private String topicName;
        private String startTime;
        private String endTime;
        private Integer durationMinutes;
        private String notes;
        private LocalDateTime createdAt;

        public StudyLogDtoBuilder() {}

        public StudyLogDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudyLogDtoBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public StudyLogDtoBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public StudyLogDtoBuilder topicName(String topicName) {
            this.topicName = topicName;
            return this;
        }
        public StudyLogDtoBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }
        public StudyLogDtoBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }
        public StudyLogDtoBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }
        public StudyLogDtoBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }
        public StudyLogDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudyLogDto build() {
            return new StudyLogDto(this.id, this.studentId, this.date, this.topicName, this.startTime, this.endTime, this.durationMinutes, this.notes, this.createdAt);
        }
    }

}
