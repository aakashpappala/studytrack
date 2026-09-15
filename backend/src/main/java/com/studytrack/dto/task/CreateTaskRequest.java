package com.studytrack.dto.task;

import com.studytrack.entity.TaskPriority;
import com.studytrack.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateTaskRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    private Long roadmapId;
    private Long subjectId;
    private Long moduleId;
    private Long topicId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private LocalDate assignedDate;
    private LocalDate dueDate;

    private Integer estimatedDurationMinutes = 60;
    private TaskPriority priority = TaskPriority.MEDIUM;
    private TaskStatus status = TaskStatus.NOT_STARTED;

    public CreateTaskRequest() {}

    public CreateTaskRequest(Long studentId, Long roadmapId, Long subjectId, Long moduleId, Long topicId, String title, String description, LocalDate assignedDate, LocalDate dueDate, Integer estimatedDurationMinutes, TaskPriority priority, TaskStatus status) {
        this.studentId = studentId;
        this.roadmapId = roadmapId;
        this.subjectId = subjectId;
        this.moduleId = moduleId;
        this.topicId = topicId;
        this.title = title;
        this.description = description;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.priority = priority;
        this.status = status;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getRoadmapId() {
        return this.roadmapId;
    }

    public void setRoadmapId(Long roadmapId) {
        this.roadmapId = roadmapId;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
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

    public TaskPriority getPriority() {
        return this.priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }


    public static CreateTaskRequestBuilder builder() {
        return new CreateTaskRequestBuilder();
    }

    public static class CreateTaskRequestBuilder {
        private Long studentId;
        private Long roadmapId;
        private Long subjectId;
        private Long moduleId;
        private Long topicId;
        private String title;
        private String description;
        private LocalDate assignedDate;
        private LocalDate dueDate;
        private Integer estimatedDurationMinutes;
        private TaskPriority priority;
        private TaskStatus status;

        public CreateTaskRequestBuilder() {}

        public CreateTaskRequestBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public CreateTaskRequestBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }
        public CreateTaskRequestBuilder subjectId(Long subjectId) {
            this.subjectId = subjectId;
            return this;
        }
        public CreateTaskRequestBuilder moduleId(Long moduleId) {
            this.moduleId = moduleId;
            return this;
        }
        public CreateTaskRequestBuilder topicId(Long topicId) {
            this.topicId = topicId;
            return this;
        }
        public CreateTaskRequestBuilder title(String title) {
            this.title = title;
            return this;
        }
        public CreateTaskRequestBuilder description(String description) {
            this.description = description;
            return this;
        }
        public CreateTaskRequestBuilder assignedDate(LocalDate assignedDate) {
            this.assignedDate = assignedDate;
            return this;
        }
        public CreateTaskRequestBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }
        public CreateTaskRequestBuilder estimatedDurationMinutes(Integer estimatedDurationMinutes) {
            this.estimatedDurationMinutes = estimatedDurationMinutes;
            return this;
        }
        public CreateTaskRequestBuilder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }
        public CreateTaskRequestBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public CreateTaskRequest build() {
            return new CreateTaskRequest(this.studentId, this.roadmapId, this.subjectId, this.moduleId, this.topicId, this.title, this.description, this.assignedDate, this.dueDate, this.estimatedDurationMinutes, this.priority, this.status);
        }
    }

}
