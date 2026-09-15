package com.studytrack.dto.progress;

public class SubjectProgressDto {
    private Long subjectId;
    private String subjectTitle;
    private int totalTopics;
    private int completedTopics;
    private double progressPercentage;

    public SubjectProgressDto() {}

    public SubjectProgressDto(Long subjectId, String subjectTitle, int totalTopics, int completedTopics, double progressPercentage) {
        this.subjectId = subjectId;
        this.subjectTitle = subjectTitle;
        this.totalTopics = totalTopics;
        this.completedTopics = completedTopics;
        this.progressPercentage = progressPercentage;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectTitle() {
        return this.subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getTotalTopics() {
        return this.totalTopics;
    }

    public void setTotalTopics(int totalTopics) {
        this.totalTopics = totalTopics;
    }

    public int getCompletedTopics() {
        return this.completedTopics;
    }

    public void setCompletedTopics(int completedTopics) {
        this.completedTopics = completedTopics;
    }

    public double getProgressPercentage() {
        return this.progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }


    public static SubjectProgressDtoBuilder builder() {
        return new SubjectProgressDtoBuilder();
    }

    public static class SubjectProgressDtoBuilder {
        private Long subjectId;
        private String subjectTitle;
        private int totalTopics;
        private int completedTopics;
        private double progressPercentage;

        public SubjectProgressDtoBuilder() {}

        public SubjectProgressDtoBuilder subjectId(Long subjectId) {
            this.subjectId = subjectId;
            return this;
        }
        public SubjectProgressDtoBuilder subjectTitle(String subjectTitle) {
            this.subjectTitle = subjectTitle;
            return this;
        }
        public SubjectProgressDtoBuilder totalTopics(int totalTopics) {
            this.totalTopics = totalTopics;
            return this;
        }
        public SubjectProgressDtoBuilder completedTopics(int completedTopics) {
            this.completedTopics = completedTopics;
            return this;
        }
        public SubjectProgressDtoBuilder progressPercentage(double progressPercentage) {
            this.progressPercentage = progressPercentage;
            return this;
        }

        public SubjectProgressDto build() {
            return new SubjectProgressDto(this.subjectId, this.subjectTitle, this.totalTopics, this.completedTopics, this.progressPercentage);
        }
    }

}
