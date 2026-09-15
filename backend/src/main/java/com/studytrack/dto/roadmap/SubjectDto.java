package com.studytrack.dto.roadmap;

import java.util.ArrayList;
import java.util.List;

public class SubjectDto {
    private Long id;
    private Long roadmapId;
    private String title;
    private String description;
    private Integer orderIndex;
    private List<ModuleDto> modules = new ArrayList<>();
    private Double completionPercentage;
    private Integer totalTopics;
    private Integer completedTopics;

    public SubjectDto() {}

    public SubjectDto(Long id, Long roadmapId, String title, String description, Integer orderIndex, List<ModuleDto> modules, Double completionPercentage, Integer totalTopics, Integer completedTopics) {
        this.id = id;
        this.roadmapId = roadmapId;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.modules = modules;
        this.completionPercentage = completionPercentage;
        this.totalTopics = totalTopics;
        this.completedTopics = completedTopics;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoadmapId() {
        return this.roadmapId;
    }

    public void setRoadmapId(Long roadmapId) {
        this.roadmapId = roadmapId;
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

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public List<ModuleDto> getModules() {
        return this.modules;
    }

    public void setModules(List<ModuleDto> modules) {
        this.modules = modules;
    }

    public Double getCompletionPercentage() {
        return this.completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public Integer getTotalTopics() {
        return this.totalTopics;
    }

    public void setTotalTopics(Integer totalTopics) {
        this.totalTopics = totalTopics;
    }

    public Integer getCompletedTopics() {
        return this.completedTopics;
    }

    public void setCompletedTopics(Integer completedTopics) {
        this.completedTopics = completedTopics;
    }


    public static SubjectDtoBuilder builder() {
        return new SubjectDtoBuilder();
    }

    public static class SubjectDtoBuilder {
        private Long id;
        private Long roadmapId;
        private String title;
        private String description;
        private Integer orderIndex;
        private List<ModuleDto> modules;
        private Double completionPercentage;
        private Integer totalTopics;
        private Integer completedTopics;

        public SubjectDtoBuilder() {}

        public SubjectDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public SubjectDtoBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }
        public SubjectDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public SubjectDtoBuilder description(String description) {
            this.description = description;
            return this;
        }
        public SubjectDtoBuilder orderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public SubjectDtoBuilder modules(List<ModuleDto> modules) {
            this.modules = modules;
            return this;
        }
        public SubjectDtoBuilder completionPercentage(Double completionPercentage) {
            this.completionPercentage = completionPercentage;
            return this;
        }
        public SubjectDtoBuilder totalTopics(Integer totalTopics) {
            this.totalTopics = totalTopics;
            return this;
        }
        public SubjectDtoBuilder completedTopics(Integer completedTopics) {
            this.completedTopics = completedTopics;
            return this;
        }

        public SubjectDto build() {
            return new SubjectDto(this.id, this.roadmapId, this.title, this.description, this.orderIndex, this.modules, this.completionPercentage, this.totalTopics, this.completedTopics);
        }
    }

}
