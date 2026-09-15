package com.studytrack.dto.progress;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentProgressSummaryDto {
    private Long studentId;
    private String studentName;
    private String roadmapTitle;
    private double overallProgress;
    private double todayProgress;
    private int todayTotalTasks;
    private int todayCompletedTasks;
    private int totalTasks;
    private int completedTasks;
    private int currentStreak;
    private int longestStreak;
    private LocalDate lastStudyDate;
    private String todayStudyTime;
    private String weeklyStudyTime;
    private String monthlyStudyTime;
    private long totalStudyMinutes;
    private List<SubjectProgressDto> subjectProgress = new ArrayList<>();

    public StudentProgressSummaryDto() {}

    public StudentProgressSummaryDto(Long studentId, String studentName, String roadmapTitle, double overallProgress, double todayProgress, int todayTotalTasks, int todayCompletedTasks, int totalTasks, int completedTasks, int currentStreak, int longestStreak, LocalDate lastStudyDate, String todayStudyTime, String weeklyStudyTime, String monthlyStudyTime, long totalStudyMinutes, List<SubjectProgressDto> subjectProgress) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.roadmapTitle = roadmapTitle;
        this.overallProgress = overallProgress;
        this.todayProgress = todayProgress;
        this.todayTotalTasks = todayTotalTasks;
        this.todayCompletedTasks = todayCompletedTasks;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastStudyDate = lastStudyDate;
        this.todayStudyTime = todayStudyTime;
        this.weeklyStudyTime = weeklyStudyTime;
        this.monthlyStudyTime = monthlyStudyTime;
        this.totalStudyMinutes = totalStudyMinutes;
        this.subjectProgress = subjectProgress;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public int getTodayTotalTasks() {
        return this.todayTotalTasks;
    }

    public void setTodayTotalTasks(int todayTotalTasks) {
        this.todayTotalTasks = todayTotalTasks;
    }

    public int getTodayCompletedTasks() {
        return this.todayCompletedTasks;
    }

    public void setTodayCompletedTasks(int todayCompletedTasks) {
        this.todayCompletedTasks = todayCompletedTasks;
    }

    public int getTotalTasks() {
        return this.totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return this.completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getCurrentStreak() {
        return this.currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return this.longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public LocalDate getLastStudyDate() {
        return this.lastStudyDate;
    }

    public void setLastStudyDate(LocalDate lastStudyDate) {
        this.lastStudyDate = lastStudyDate;
    }

    public String getTodayStudyTime() {
        return this.todayStudyTime;
    }

    public void setTodayStudyTime(String todayStudyTime) {
        this.todayStudyTime = todayStudyTime;
    }

    public String getWeeklyStudyTime() {
        return this.weeklyStudyTime;
    }

    public void setWeeklyStudyTime(String weeklyStudyTime) {
        this.weeklyStudyTime = weeklyStudyTime;
    }

    public String getMonthlyStudyTime() {
        return this.monthlyStudyTime;
    }

    public void setMonthlyStudyTime(String monthlyStudyTime) {
        this.monthlyStudyTime = monthlyStudyTime;
    }

    public long getTotalStudyMinutes() {
        return this.totalStudyMinutes;
    }

    public void setTotalStudyMinutes(long totalStudyMinutes) {
        this.totalStudyMinutes = totalStudyMinutes;
    }

    public List<SubjectProgressDto> getSubjectProgress() {
        return this.subjectProgress;
    }

    public void setSubjectProgress(List<SubjectProgressDto> subjectProgress) {
        this.subjectProgress = subjectProgress;
    }


    public static StudentProgressSummaryDtoBuilder builder() {
        return new StudentProgressSummaryDtoBuilder();
    }

    public static class StudentProgressSummaryDtoBuilder {
        private Long studentId;
        private String studentName;
        private String roadmapTitle;
        private double overallProgress;
        private double todayProgress;
        private int todayTotalTasks;
        private int todayCompletedTasks;
        private int totalTasks;
        private int completedTasks;
        private int currentStreak;
        private int longestStreak;
        private LocalDate lastStudyDate;
        private String todayStudyTime;
        private String weeklyStudyTime;
        private String monthlyStudyTime;
        private long totalStudyMinutes;
        private List<SubjectProgressDto> subjectProgress;

        public StudentProgressSummaryDtoBuilder() {}

        public StudentProgressSummaryDtoBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }
        public StudentProgressSummaryDtoBuilder studentName(String studentName) {
            this.studentName = studentName;
            return this;
        }
        public StudentProgressSummaryDtoBuilder roadmapTitle(String roadmapTitle) {
            this.roadmapTitle = roadmapTitle;
            return this;
        }
        public StudentProgressSummaryDtoBuilder overallProgress(double overallProgress) {
            this.overallProgress = overallProgress;
            return this;
        }
        public StudentProgressSummaryDtoBuilder todayProgress(double todayProgress) {
            this.todayProgress = todayProgress;
            return this;
        }
        public StudentProgressSummaryDtoBuilder todayTotalTasks(int todayTotalTasks) {
            this.todayTotalTasks = todayTotalTasks;
            return this;
        }
        public StudentProgressSummaryDtoBuilder todayCompletedTasks(int todayCompletedTasks) {
            this.todayCompletedTasks = todayCompletedTasks;
            return this;
        }
        public StudentProgressSummaryDtoBuilder totalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
            return this;
        }
        public StudentProgressSummaryDtoBuilder completedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
            return this;
        }
        public StudentProgressSummaryDtoBuilder currentStreak(int currentStreak) {
            this.currentStreak = currentStreak;
            return this;
        }
        public StudentProgressSummaryDtoBuilder longestStreak(int longestStreak) {
            this.longestStreak = longestStreak;
            return this;
        }
        public StudentProgressSummaryDtoBuilder lastStudyDate(LocalDate lastStudyDate) {
            this.lastStudyDate = lastStudyDate;
            return this;
        }
        public StudentProgressSummaryDtoBuilder todayStudyTime(String todayStudyTime) {
            this.todayStudyTime = todayStudyTime;
            return this;
        }
        public StudentProgressSummaryDtoBuilder weeklyStudyTime(String weeklyStudyTime) {
            this.weeklyStudyTime = weeklyStudyTime;
            return this;
        }
        public StudentProgressSummaryDtoBuilder monthlyStudyTime(String monthlyStudyTime) {
            this.monthlyStudyTime = monthlyStudyTime;
            return this;
        }
        public StudentProgressSummaryDtoBuilder totalStudyMinutes(long totalStudyMinutes) {
            this.totalStudyMinutes = totalStudyMinutes;
            return this;
        }
        public StudentProgressSummaryDtoBuilder subjectProgress(List<SubjectProgressDto> subjectProgress) {
            this.subjectProgress = subjectProgress;
            return this;
        }

        public StudentProgressSummaryDto build() {
            return new StudentProgressSummaryDto(this.studentId, this.studentName, this.roadmapTitle, this.overallProgress, this.todayProgress, this.todayTotalTasks, this.todayCompletedTasks, this.totalTasks, this.completedTasks, this.currentStreak, this.longestStreak, this.lastStudyDate, this.todayStudyTime, this.weeklyStudyTime, this.monthlyStudyTime, this.totalStudyMinutes, this.subjectProgress);
        }
    }

}
