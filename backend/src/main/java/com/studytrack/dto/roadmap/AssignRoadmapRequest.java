package com.studytrack.dto.roadmap;

import jakarta.validation.constraints.NotNull;
public class AssignRoadmapRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Roadmap ID is required")
    private Long roadmapId;

    public AssignRoadmapRequest() {}

    public AssignRoadmapRequest(Long studentId, Long roadmapId) {
        this.studentId = studentId;
        this.roadmapId = roadmapId;
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


    public static AssignRoadmapRequestBuilder builder() {
        return new AssignRoadmapRequestBuilder();
    }

    public static class AssignRoadmapRequestBuilder {
        private Long studentId;
        private Long roadmapId;

        public AssignRoadmapRequestBuilder() {}

        public AssignRoadmapRequestBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public AssignRoadmapRequestBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }

        public AssignRoadmapRequest build() {
            return new AssignRoadmapRequest(this.studentId, this.roadmapId);
        }
    }

}
