package com.studytrack.dto.roadmap;

import java.util.ArrayList;
import java.util.List;

public class ModuleDto {
    private Long id;
    private Long subjectId;
    private String title;
    private String description;
    private Integer orderIndex;
    private List<TopicDto> topics = new ArrayList<>();
    private Double completionPercentage;

    public ModuleDto() {}

    public ModuleDto(Long id, Long subjectId, String title, String description, Integer orderIndex, List<TopicDto> topics, Double completionPercentage) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.topics = topics;
        this.completionPercentage = completionPercentage;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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

    public List<TopicDto> getTopics() {
        return this.topics;
    }

    public void setTopics(List<TopicDto> topics) {
        this.topics = topics;
    }

    public Double getCompletionPercentage() {
        return this.completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }


    public static ModuleDtoBuilder builder() {
        return new ModuleDtoBuilder();
    }

    public static class ModuleDtoBuilder {
        private Long id;
        private Long subjectId;
        private String title;
        private String description;
        private Integer orderIndex;
        private List<TopicDto> topics;
        private Double completionPercentage;

        public ModuleDtoBuilder() {}

        public ModuleDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public ModuleDtoBuilder subjectId(Long subjectId) {
            this.subjectId = subjectId;
            return this;
        }
        public ModuleDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public ModuleDtoBuilder description(String description) {
            this.description = description;
            return this;
        }
        public ModuleDtoBuilder orderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public ModuleDtoBuilder topics(List<TopicDto> topics) {
            this.topics = topics;
            return this;
        }
        public ModuleDtoBuilder completionPercentage(Double completionPercentage) {
            this.completionPercentage = completionPercentage;
            return this;
        }

        public ModuleDto build() {
            return new ModuleDto(this.id, this.subjectId, this.title, this.description, this.orderIndex, this.topics, this.completionPercentage);
        }
    }

}
