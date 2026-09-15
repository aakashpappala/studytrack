package com.studytrack.dto.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminAnalyticsDto {
    private long totalStudents;
    private double averageProgress;
    private double averageStudyHours;
    private String mostCompletedRoadmapTitle;
    private double dailyCompletionRate;
    private double weeklyCompletionRate;
    private double monthlyCompletionRate;

    private List<StudentSummaryDto> highestPerformingStudents = new ArrayList<>();

    private List<StudentSummaryDto> studentsFallingBehind = new ArrayList<>();

    private List<Map<String, Object>> roadmapStats = new ArrayList<>();

    private List<Map<String, Object>> dailyCompletionTrend = new ArrayList<>();

    public AdminAnalyticsDto() {}

    public AdminAnalyticsDto(long totalStudents, double averageProgress, double averageStudyHours, String mostCompletedRoadmapTitle, double dailyCompletionRate, double weeklyCompletionRate, double monthlyCompletionRate, List<StudentSummaryDto> highestPerformingStudents, List<StudentSummaryDto> studentsFallingBehind, List<Map<String, Object>> roadmapStats, List<Map<String, Object>> dailyCompletionTrend) {
        this.totalStudents = totalStudents;
        this.averageProgress = averageProgress;
        this.averageStudyHours = averageStudyHours;
        this.mostCompletedRoadmapTitle = mostCompletedRoadmapTitle;
        this.dailyCompletionRate = dailyCompletionRate;
        this.weeklyCompletionRate = weeklyCompletionRate;
        this.monthlyCompletionRate = monthlyCompletionRate;
        this.highestPerformingStudents = highestPerformingStudents;
        this.studentsFallingBehind = studentsFallingBehind;
        this.roadmapStats = roadmapStats;
        this.dailyCompletionTrend = dailyCompletionTrend;
    }

    public long getTotalStudents() {
        return this.totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public double getAverageProgress() {
        return this.averageProgress;
    }

    public void setAverageProgress(double averageProgress) {
        this.averageProgress = averageProgress;
    }

    public double getAverageStudyHours() {
        return this.averageStudyHours;
    }

    public void setAverageStudyHours(double averageStudyHours) {
        this.averageStudyHours = averageStudyHours;
    }

    public String getMostCompletedRoadmapTitle() {
        return this.mostCompletedRoadmapTitle;
    }

    public void setMostCompletedRoadmapTitle(String mostCompletedRoadmapTitle) {
        this.mostCompletedRoadmapTitle = mostCompletedRoadmapTitle;
    }

    public double getDailyCompletionRate() {
        return this.dailyCompletionRate;
    }

    public void setDailyCompletionRate(double dailyCompletionRate) {
        this.dailyCompletionRate = dailyCompletionRate;
    }

    public double getWeeklyCompletionRate() {
        return this.weeklyCompletionRate;
    }

    public void setWeeklyCompletionRate(double weeklyCompletionRate) {
        this.weeklyCompletionRate = weeklyCompletionRate;
    }

    public double getMonthlyCompletionRate() {
        return this.monthlyCompletionRate;
    }

    public void setMonthlyCompletionRate(double monthlyCompletionRate) {
        this.monthlyCompletionRate = monthlyCompletionRate;
    }

    public List<StudentSummaryDto> getHighestPerformingStudents() {
        return this.highestPerformingStudents;
    }

    public void setHighestPerformingStudents(List<StudentSummaryDto> highestPerformingStudents) {
        this.highestPerformingStudents = highestPerformingStudents;
    }

    public List<StudentSummaryDto> getStudentsFallingBehind() {
        return this.studentsFallingBehind;
    }

    public void setStudentsFallingBehind(List<StudentSummaryDto> studentsFallingBehind) {
        this.studentsFallingBehind = studentsFallingBehind;
    }

    public List<Map<String, Object>> getRoadmapStats() {
        return this.roadmapStats;
    }

    public void setRoadmapStats(List<Map<String, Object>> roadmapStats) {
        this.roadmapStats = roadmapStats;
    }

    public List<Map<String, Object>> getDailyCompletionTrend() {
        return this.dailyCompletionTrend;
    }

    public void setDailyCompletionTrend(List<Map<String, Object>> dailyCompletionTrend) {
        this.dailyCompletionTrend = dailyCompletionTrend;
    }


    public static AdminAnalyticsDtoBuilder builder() {
        return new AdminAnalyticsDtoBuilder();
    }

    public static class AdminAnalyticsDtoBuilder {
        private long totalStudents;
        private double averageProgress;
        private double averageStudyHours;
        private String mostCompletedRoadmapTitle;
        private double dailyCompletionRate;
        private double weeklyCompletionRate;
        private double monthlyCompletionRate;
        private List<StudentSummaryDto> highestPerformingStudents;
        private List<StudentSummaryDto> studentsFallingBehind;
        private List<Map<String, Object>> roadmapStats;
        private List<Map<String, Object>> dailyCompletionTrend;

        public AdminAnalyticsDtoBuilder() {}

        public AdminAnalyticsDtoBuilder totalStudents(long totalStudents) {
            this.totalStudents = totalStudents;
            return this;
        }
        public AdminAnalyticsDtoBuilder averageProgress(double averageProgress) {
            this.averageProgress = averageProgress;
            return this;
        }
        public AdminAnalyticsDtoBuilder averageStudyHours(double averageStudyHours) {
            this.averageStudyHours = averageStudyHours;
            return this;
        }
        public AdminAnalyticsDtoBuilder mostCompletedRoadmapTitle(String mostCompletedRoadmapTitle) {
            this.mostCompletedRoadmapTitle = mostCompletedRoadmapTitle;
            return this;
        }
        public AdminAnalyticsDtoBuilder dailyCompletionRate(double dailyCompletionRate) {
            this.dailyCompletionRate = dailyCompletionRate;
            return this;
        }
        public AdminAnalyticsDtoBuilder weeklyCompletionRate(double weeklyCompletionRate) {
            this.weeklyCompletionRate = weeklyCompletionRate;
            return this;
        }
        public AdminAnalyticsDtoBuilder monthlyCompletionRate(double monthlyCompletionRate) {
            this.monthlyCompletionRate = monthlyCompletionRate;
            return this;
        }
        public AdminAnalyticsDtoBuilder highestPerformingStudents(List<StudentSummaryDto> highestPerformingStudents) {
            this.highestPerformingStudents = highestPerformingStudents;
            return this;
        }
        public AdminAnalyticsDtoBuilder studentsFallingBehind(List<StudentSummaryDto> studentsFallingBehind) {
            this.studentsFallingBehind = studentsFallingBehind;
            return this;
        }
        public AdminAnalyticsDtoBuilder roadmapStats(List<Map<String, Object>> roadmapStats) {
            this.roadmapStats = roadmapStats;
            return this;
        }
        public AdminAnalyticsDtoBuilder dailyCompletionTrend(List<Map<String, Object>> dailyCompletionTrend) {
            this.dailyCompletionTrend = dailyCompletionTrend;
            return this;
        }

        public AdminAnalyticsDto build() {
            return new AdminAnalyticsDto(this.totalStudents, this.averageProgress, this.averageStudyHours, this.mostCompletedRoadmapTitle, this.dailyCompletionRate, this.weeklyCompletionRate, this.monthlyCompletionRate, this.highestPerformingStudents, this.studentsFallingBehind, this.roadmapStats, this.dailyCompletionTrend);
        }
    }

}
