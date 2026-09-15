package com.studytrack.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roadmaps")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 60)
    private String category;

    @Column(length = 30)
    private String level;

    private Integer estimatedHours;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<Subject> subjects = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Roadmap() {}

    public Roadmap(Long id, String title, String description, String category, String level, Integer estimatedHours, List<Subject> subjects, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.level = level;
        this.estimatedHours = estimatedHours;
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

    public List<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(List<Subject> subjects) {
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


    public static RoadmapBuilder builder() {
        return new RoadmapBuilder();
    }

    public static class RoadmapBuilder {
        private Long id;
        private String title;
        private String description;
        private String category;
        private String level;
        private Integer estimatedHours;
        private List<Subject> subjects;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RoadmapBuilder() {}

        public RoadmapBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public RoadmapBuilder title(String title) {
            this.title = title;
            return this;
        }
        public RoadmapBuilder description(String description) {
            this.description = description;
            return this;
        }
        public RoadmapBuilder category(String category) {
            this.category = category;
            return this;
        }
        public RoadmapBuilder level(String level) {
            this.level = level;
            return this;
        }
        public RoadmapBuilder estimatedHours(Integer estimatedHours) {
            this.estimatedHours = estimatedHours;
            return this;
        }
        public RoadmapBuilder subjects(List<Subject> subjects) {
            this.subjects = subjects;
            return this;
        }
        public RoadmapBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public RoadmapBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Roadmap build() {
            return new Roadmap(this.id, this.title, this.description, this.category, this.level, this.estimatedHours, this.subjects, this.createdAt, this.updatedAt);
        }
    }

}
