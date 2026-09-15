package com.studytrack.dto.roadmap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoadmapDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String level;
    private Integer estimatedHours;
    private Integer totalSubjects;
    private Integer totalModules;
    private Integer totalTopics;
    private Double completionPercentage;
    private List<SubjectDto> subjects = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RoadmapDto() {}

    public RoadmapDto(Long id, String title, String description, String category, String level, Integer estimatedHours, Integer totalSubjects, Integer totalModules, Integer totalTopics, Double completionPercentage, List<SubjectDto> subjects, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.level = level;
        this.estimatedHours = estimatedHours;
        this.totalSubjects = totalSubjects;
        this.totalModules = totalModules;
        this.totalTopics = totalTopics;
        this.completionPercentage = completionPercentage;
        this.subjects = subjects;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getEstimatedHours() {
        return this.estimatedHours;
    }

    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Integer getTotalSubjects() {
        return this.totalSubjects;
    }

    public void setTotalSubjects(Integer totalSubjects) {
        this.totalSubjects = totalSubjects;
    }

    public Integer getTotalModules() {
        return this.totalModules;
    }

    public void setTotalModules(Integer totalModules) {
        this.totalModules = totalModules;
    }

    public Integer getTotalTopics() {
        return this.totalTopics;
    }

    public void setTotalTopics(Integer totalTopics) {
        this.totalTopics = totalTopics;
    }

    public Double getCompletionPercentage() {
        return this.completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<SubjectDto> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(List<SubjectDto> subjects) {
        this.subjects = subjects;
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


    public static RoadmapDtoBuilder builder() {
        return new RoadmapDtoBuilder();
    }

    public static class RoadmapDtoBuilder {
        private Long id;
        private String title;
        private String description;
        private String category;
        private String level;
        private Integer estimatedHours;
        private Integer totalSubjects;
        private Integer totalModules;
        private Integer totalTopics;
        private Double completionPercentage;
        private List<SubjectDto> subjects;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RoadmapDtoBuilder() {}

        public RoadmapDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public RoadmapDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public RoadmapDtoBuilder description(String description) {
            this.description = description;
            return this;
        }
        public RoadmapDtoBuilder category(String category) {
            this.category = category;
            return this;
        }
        public RoadmapDtoBuilder level(String level) {
            this.level = level;
            return this;
        }
        public RoadmapDtoBuilder estimatedHours(Integer estimatedHours) {
            this.estimatedHours = estimatedHours;
            return this;
        }
        public RoadmapDtoBuilder totalSubjects(Integer totalSubjects) {
            this.totalSubjects = totalSubjects;
            return this;
        }
        public RoadmapDtoBuilder totalModules(Integer totalModules) {
            this.totalModules = totalModules;
            return this;
        }
        public RoadmapDtoBuilder totalTopics(Integer totalTopics) {
            this.totalTopics = totalTopics;
            return this;
        }
        public RoadmapDtoBuilder completionPercentage(Double completionPercentage) {
            this.completionPercentage = completionPercentage;
            return this;
        }
        public RoadmapDtoBuilder subjects(List<SubjectDto> subjects) {
            this.subjects = subjects;
            return this;
        }
        public RoadmapDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public RoadmapDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoadmapDto build() {
            return new RoadmapDto(this.id, this.title, this.description, this.category, this.level, this.estimatedHours, this.totalSubjects, this.totalModules, this.totalTopics, this.completionPercentage, this.subjects, this.createdAt, this.updatedAt);
        }
    }

}
