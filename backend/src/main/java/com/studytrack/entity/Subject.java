package com.studytrack.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @JsonBackReference
    private Roadmap roadmap;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<RoadmapModule> modules = new ArrayList<>();

    public Subject() {}

    public Subject(Long id, Roadmap roadmap, String title, String description, Integer orderIndex, List<RoadmapModule> modules) {
        this.id = id;
        this.roadmap = roadmap;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.modules = modules;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Roadmap getRoadmap() {
        return this.roadmap;
    }

    public void setRoadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
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

    public List<RoadmapModule> getModules() {
        return this.modules;
    }

    public void setModules(List<RoadmapModule> modules) {
        this.modules = modules;
    }


    public static SubjectBuilder builder() {
        return new SubjectBuilder();
    }

    public static class SubjectBuilder {
        private Long id;
        private Roadmap roadmap;
        private String title;
        private String description;
        private Integer orderIndex;
        private List<RoadmapModule> modules;

        public SubjectBuilder() {}

        public SubjectBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public SubjectBuilder roadmap(Roadmap roadmap) {
            this.roadmap = roadmap;
            return this;
        }
        public SubjectBuilder title(String title) {
            this.title = title;
            return this;
        }
        public SubjectBuilder description(String description) {
            this.description = description;
            return this;
        }
        public SubjectBuilder orderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public SubjectBuilder modules(List<RoadmapModule> modules) {
            this.modules = modules;
            return this;
        }

        public Subject build() {
            return new Subject(this.id, this.roadmap, this.title, this.description, this.orderIndex, this.modules);
        }
    }

}
