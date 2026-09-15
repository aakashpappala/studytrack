package com.studytrack.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
public class RoadmapModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonBackReference
    private Subject subject;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<Topic> topics = new ArrayList<>();

    public RoadmapModule() {}

    public RoadmapModule(Long id, Subject subject, String title, String description, Integer orderIndex, List<Topic> topics) {
        this.id = id;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex != null ? orderIndex : 0;
        this.topics = topics != null ? topics : new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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

    public List<Topic> getTopics() {
        return this.topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public static RoadmapModuleBuilder builder() {
        return new RoadmapModuleBuilder();
    }

    public static class RoadmapModuleBuilder {
        private Long id;
        private Subject subject;
        private String title;
        private String description;
        private Integer orderIndex = 0;
        private List<Topic> topics = new ArrayList<>();

        public RoadmapModuleBuilder() {}

        public RoadmapModuleBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public RoadmapModuleBuilder subject(Subject subject) {
            this.subject = subject;
            return this;
        }
        public RoadmapModuleBuilder title(String title) {
            this.title = title;
            return this;
        }
        public RoadmapModuleBuilder description(String description) {
            this.description = description;
            return this;
        }
        public RoadmapModuleBuilder orderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public RoadmapModuleBuilder topics(List<Topic> topics) {
            this.topics = topics;
            return this;
        }

        public RoadmapModule build() {
            return new RoadmapModule(this.id, this.subject, this.title, this.description, this.orderIndex, this.topics);
        }
    }
}
