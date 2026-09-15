package com.studytrack.dto.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long roadmapId;
    private String roadmapTitle;
    private Long subjectId;
    private String subjectTitle;
    private Long moduleId;
    private String moduleTitle;
    private Long topicId;
    private String topicTitle;
    private String title;
    private String description;
    private LocalDate assignedDate;
    private LocalDate dueDate;
    private Integer estimatedDurationMinutes;
    private String priority; // LOW, MEDIUM, HIGH
    private String status;   // NOT_STARTED, IN_PROGRESS, COMPLETED
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    public TaskDto() {}

    public TaskDto(Long id, Long studentId, String studentName, Long roadmapId, String roadmapTitle, Long subjectId, String subjectTitle, Long moduleId, String moduleTitle, Long topicId, String topicTitle, String title, String description, LocalDate assignedDate, LocalDate dueDate, Integer estimatedDurationMinutes, String priority, String status, LocalDateTime completedAt, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.roadmapId = roadmapId;
        this.roadmapTitle = roadmapTitle;
        this.subjectId = subjectId;
        this.subjectTitle = subjectTitle;
        this.moduleId = moduleId;
        this.moduleTitle = moduleTitle;
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.title = title;
        this.description = description;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.priority = priority;
        this.status = status;
        this.completedAt = completedAt;
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

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getRoadmapId() {
        return this.roadmapId;
    }

    public void setRoadmapId(Long roadmapId) {
        this.roadmapId = roadmapId;
    }

    public String getRoadmapTitle() {
        return this.roadmapTitle;
    }

    public void setRoadmapTitle(String roadmapTitle) {
        this.roadmapTitle = roadmapTitle;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectTitle() {
        return this.subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleTitle() {
        return this.moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAssignedDate() {
        return this.assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimatedDurationMinutes() {
        return this.estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public static TaskDtoBuilder builder() {
        return new TaskDtoBuilder();
    }

    public static class TaskDtoBuilder {
        private Long id;
        private Long studentId;
        private String studentName;
        private Long roadmapId;
        private String roadmapTitle;
        private Long subjectId;
        private String subjectTitle;
        private Long moduleId;
        private String moduleTitle;
        private Long topicId;
        private String topicTitle;
        private String title;
        private String description;
        private LocalDate assignedDate;
        private LocalDate dueDate;
        private Integer estimatedDurationMinutes;
        private String priority;
        private String status;
        private LocalDateTime completedAt;
        private LocalDateTime createdAt;

        public TaskDtoBuilder() {}

        public TaskDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public TaskDtoBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public TaskDtoBuilder studentName(String studentName) {
            this.studentName = studentName;
            return this;
        }
        public TaskDtoBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }
        public TaskDtoBuilder roadmapTitle(String roadmapTitle) {
            this.roadmapTitle = roadmapTitle;
            return this;
        }
        public TaskDtoBuilder subjectId(Long subjectId) {
            this.subjectId = subjectId;
            return this;
        }
        public TaskDtoBuilder subjectTitle(String subjectTitle) {
            this.subjectTitle = subjectTitle;
            return this;
        }
        public TaskDtoBuilder moduleId(Long moduleId) {
            this.moduleId = moduleId;
            return this;
        }
        public TaskDtoBuilder moduleTitle(String moduleTitle) {
            this.moduleTitle = moduleTitle;
            return this;
        }
        public TaskDtoBuilder topicId(Long topicId) {
            this.topicId = topicId;
            return this;
        }
        public TaskDtoBuilder topicTitle(String topicTitle) {
            this.topicTitle = topicTitle;
            return this;
        }
        public TaskDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public TaskDtoBuilder description(String description) {
            this.description = description;
            return this;
        }
        public TaskDtoBuilder assignedDate(LocalDate assignedDate) {
            this.assignedDate = assignedDate;
            return this;
        }
        public TaskDtoBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }
        public TaskDtoBuilder estimatedDurationMinutes(Integer estimatedDurationMinutes) {
            this.estimatedDurationMinutes = estimatedDurationMinutes;
            return this;
        }
        public TaskDtoBuilder priority(String priority) {
            this.priority = priority;
            return this;
        }
        public TaskDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public TaskDtoBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }
        public TaskDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TaskDto build() {
            return new TaskDto(this.id, this.studentId, this.studentName, this.roadmapId, this.roadmapTitle, this.subjectId, this.subjectTitle, this.moduleId, this.moduleTitle, this.topicId, this.topicTitle, this.title, this.description, this.assignedDate, this.dueDate, this.estimatedDurationMinutes, this.priority, this.status, this.completedAt, this.createdAt);
        }
    }

}
