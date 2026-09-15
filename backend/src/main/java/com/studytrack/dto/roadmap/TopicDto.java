package com.studytrack.dto.roadmap;

public class TopicDto {
    private Long id;
    private Long moduleId;
    private String title;
    private String description;
    private Integer orderIndex;
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED

    public TopicDto() {}

    public TopicDto(Long id, Long moduleId, String title, String description, Integer orderIndex, String status) {
        this.id = id;
        this.moduleId = moduleId;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static TopicDtoBuilder builder() {
        return new TopicDtoBuilder();
    }

    public static class TopicDtoBuilder {
        private Long id;
        private Long moduleId;
        private String title;
        private String description;
        private Integer orderIndex;
        private String status;

        public TopicDtoBuilder() {}

        public TopicDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public TopicDtoBuilder moduleId(Long moduleId) {
            this.moduleId = moduleId;
            return this;
        }
        public TopicDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public TopicDtoBuilder description(String description) {
            this.description = description;
            return this;
        }
        public TopicDtoBuilder orderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public TopicDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public TopicDto build() {
            return new TopicDto(this.id, this.moduleId, this.title, this.description, this.orderIndex, this.status);
        }
    }

}
