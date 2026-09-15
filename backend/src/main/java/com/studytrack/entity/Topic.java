package com.studytrack.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    @JsonBackReference
    private RoadmapModule module;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    public Topic() {}

    public Topic(Long id, RoadmapModule module, String title, String description, Integer orderIndex) {
        this.id = id;
        this.module = module;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoadmapModule getModule() {
        return this.module;
    }

    public void setModule(RoadmapModule module) {
        this.module = module;
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


    public static TopicBuilder builder() {
        return new TopicBuilder();
    }

    public static class TopicBuilder {
        private Long id;
        private RoadmapModule module;
        private String title;
        private String description;
        private Integer orderIndex;

        public TopicBuilder() {}

        public TopicBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public TopicBuilder module(RoadmapModule module) {
            this.module = module;
            return this;
        }
        public TopicBuilder title(String title) {
            this.title = title;
            return this;
        }
        public TopicBuilder description(String description) {
            this.description = description;
            return this;
        }
        public TopicBuilder orderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }

        public Topic build() {
            return new Topic(this.id, this.module, this.title, this.description, this.orderIndex);
        }
    }

}
