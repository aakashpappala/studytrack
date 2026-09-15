package com.studytrack.dto.admin;

import java.time.LocalDate;

public class StudentSummaryDto {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String college;
    private String enrollmentNo;
    private String status; // ACTIVE, INACTIVE
    private Long roadmapId;
    private String roadmapTitle;
    private double overallProgress;
    private double todayProgress;
    private int currentStreak;
    private long totalStudyMinutes;
    private LocalDate lastStudyDate;

    public StudentSummaryDto() {}

    public StudentSummaryDto(Long id, Long userId, String fullName, String email, String phone, String college, String enrollmentNo, String status, Long roadmapId, String roadmapTitle, double overallProgress, double todayProgress, int currentStreak, long totalStudyMinutes, LocalDate lastStudyDate) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.college = college;
        this.enrollmentNo = enrollmentNo;
        this.status = status;
        this.roadmapId = roadmapId;
        this.roadmapTitle = roadmapTitle;
        this.overallProgress = overallProgress;
        this.todayProgress = todayProgress;
        this.currentStreak = currentStreak;
        this.totalStudyMinutes = totalStudyMinutes;
        this.lastStudyDate = lastStudyDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEnrollmentNo() {
        return this.enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRoadmapId() {
        return this.roadmapId;
    }

    public void setRoadmapId(Long roadmapId) {
        this.roadmapId = roadmapId;
    }

    public String getRoadmapTitle() {
        return this.roadmapTitle;
    }

    public void setRoadmapTitle(String roadmapTitle) {
        this.roadmapTitle = roadmapTitle;
    }

    public double getOverallProgress() {
        return this.overallProgress;
    }

    public void setOverallProgress(double overallProgress) {
        this.overallProgress = overallProgress;
    }

    public double getTodayProgress() {
        return this.todayProgress;
    }

    public void setTodayProgress(double todayProgress) {
        this.todayProgress = todayProgress;
    }

    public int getCurrentStreak() {
        return this.currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public long getTotalStudyMinutes() {
        return this.totalStudyMinutes;
    }

    public void setTotalStudyMinutes(long totalStudyMinutes) {
        this.totalStudyMinutes = totalStudyMinutes;
    }

    public LocalDate getLastStudyDate() {
        return this.lastStudyDate;
    }

    public void setLastStudyDate(LocalDate lastStudyDate) {
        this.lastStudyDate = lastStudyDate;
    }


    public static StudentSummaryDtoBuilder builder() {
        return new StudentSummaryDtoBuilder();
    }

    public static class StudentSummaryDtoBuilder {
        private Long id;
        private Long userId;
        private String fullName;
        private String email;
        private String phone;
        private String college;
        private String enrollmentNo;
        private String status;
        private Long roadmapId;
        private String roadmapTitle;
        private double overallProgress;
        private double todayProgress;
        private int currentStreak;
        private long totalStudyMinutes;
        private LocalDate lastStudyDate;

        public StudentSummaryDtoBuilder() {}

        public StudentSummaryDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudentSummaryDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public StudentSummaryDtoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public StudentSummaryDtoBuilder email(String email) {
            this.email = email;
            return this;
        }
        public StudentSummaryDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public StudentSummaryDtoBuilder college(String college) {
            this.college = college;
            return this;
        }
        public StudentSummaryDtoBuilder enrollmentNo(String enrollmentNo) {
            this.enrollmentNo = enrollmentNo;
            return this;
        }
        public StudentSummaryDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public StudentSummaryDtoBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }
        public StudentSummaryDtoBuilder roadmapTitle(String roadmapTitle) {
            this.roadmapTitle = roadmapTitle;
            return this;
        }
        public StudentSummaryDtoBuilder overallProgress(double overallProgress) {
            this.overallProgress = overallProgress;
            return this;
        }
        public StudentSummaryDtoBuilder todayProgress(double todayProgress) {
            this.todayProgress = todayProgress;
            return this;
        }
        public StudentSummaryDtoBuilder currentStreak(int currentStreak) {
            this.currentStreak = currentStreak;
            return this;
        }
        public StudentSummaryDtoBuilder totalStudyMinutes(long totalStudyMinutes) {
            this.totalStudyMinutes = totalStudyMinutes;
            return this;
        }
        public StudentSummaryDtoBuilder lastStudyDate(LocalDate lastStudyDate) {
            this.lastStudyDate = lastStudyDate;
            return this;
        }

        public StudentSummaryDto build() {
            return new StudentSummaryDto(this.id, this.userId, this.fullName, this.email, this.phone, this.college, this.enrollmentNo, this.status, this.roadmapId, this.roadmapTitle, this.overallProgress, this.todayProgress, this.currentStreak, this.totalStudyMinutes, this.lastStudyDate);
        }
    }

}
