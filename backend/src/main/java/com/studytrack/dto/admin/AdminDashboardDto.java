package com.studytrack.dto.admin;

import com.studytrack.dto.progress.WeeklyProgressDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDashboardDto {
    private long totalStudents;
    private long activeStudents;
    private long totalRoadmaps;
    private long tasksCompletedToday;
    private double averageStudentProgress;
    private double totalStudyHours;
    private long studentsFallingBehindCount;

    private List<StudentSummaryDto> studentsFallingBehind = new ArrayList<>();

    private List<Map<String, Object>> recentActivity = new ArrayList<>();

    private List<Map<String, Object>> studentProgressDistribution = new ArrayList<>();

    private List<WeeklyProgressDto> weeklyStudyHoursTrend = new ArrayList<>();

    private Map<String, Long> taskStatusBreakdown = Map.of();

    private List<Map<String, Object>> roadmapCompletionComparison = new ArrayList<>();

    public AdminDashboardDto() {}

    public AdminDashboardDto(long totalStudents, long activeStudents, long totalRoadmaps, long tasksCompletedToday, double averageStudentProgress, double totalStudyHours, long studentsFallingBehindCount, List<StudentSummaryDto> studentsFallingBehind, List<Map<String, Object>> recentActivity, List<Map<String, Object>> studentProgressDistribution, List<WeeklyProgressDto> weeklyStudyHoursTrend, Map<String, Long> taskStatusBreakdown, List<Map<String, Object>> roadmapCompletionComparison) {
        this.totalStudents = totalStudents;
        this.activeStudents = activeStudents;
        this.totalRoadmaps = totalRoadmaps;
        this.tasksCompletedToday = tasksCompletedToday;
        this.averageStudentProgress = averageStudentProgress;
        this.totalStudyHours = totalStudyHours;
        this.studentsFallingBehindCount = studentsFallingBehindCount;
        this.studentsFallingBehind = studentsFallingBehind;
        this.recentActivity = recentActivity;
        this.studentProgressDistribution = studentProgressDistribution;
        this.weeklyStudyHoursTrend = weeklyStudyHoursTrend;
        this.taskStatusBreakdown = taskStatusBreakdown;
        this.roadmapCompletionComparison = roadmapCompletionComparison;
    }

    public long getTotalStudents() {
        return this.totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getActiveStudents() {
        return this.activeStudents;
    }

    public void setActiveStudents(long activeStudents) {
        this.activeStudents = activeStudents;
    }

    public long getTotalRoadmaps() {
        return this.totalRoadmaps;
    }

    public void setTotalRoadmaps(long totalRoadmaps) {
        this.totalRoadmaps = totalRoadmaps;
    }

    public long getTasksCompletedToday() {
        return this.tasksCompletedToday;
    }

    public void setTasksCompletedToday(long tasksCompletedToday) {
        this.tasksCompletedToday = tasksCompletedToday;
    }

    public double getAverageStudentProgress() {
        return this.averageStudentProgress;
    }

    public void setAverageStudentProgress(double averageStudentProgress) {
        this.averageStudentProgress = averageStudentProgress;
    }

    public double getTotalStudyHours() {
        return this.totalStudyHours;
    }

    public void setTotalStudyHours(double totalStudyHours) {
        this.totalStudyHours = totalStudyHours;
    }

    public long getStudentsFallingBehindCount() {
        return this.studentsFallingBehindCount;
    }

    public void setStudentsFallingBehindCount(long studentsFallingBehindCount) {
        this.studentsFallingBehindCount = studentsFallingBehindCount;
    }

    public List<StudentSummaryDto> getStudentsFallingBehind() {
        return this.studentsFallingBehind;
    }

    public void setStudentsFallingBehind(List<StudentSummaryDto> studentsFallingBehind) {
        this.studentsFallingBehind = studentsFallingBehind;
    }

    public List<Map<String, Object>> getRecentActivity() {
        return this.recentActivity;
    }

    public void setRecentActivity(List<Map<String, Object>> recentActivity) {
        this.recentActivity = recentActivity;
    }

    public List<Map<String, Object>> getStudentProgressDistribution() {
        return this.studentProgressDistribution;
    }

    public void setStudentProgressDistribution(List<Map<String, Object>> studentProgressDistribution) {
        this.studentProgressDistribution = studentProgressDistribution;
    }

    public List<WeeklyProgressDto> getWeeklyStudyHoursTrend() {
        return this.weeklyStudyHoursTrend;
    }

    public void setWeeklyStudyHoursTrend(List<WeeklyProgressDto> weeklyStudyHoursTrend) {
        this.weeklyStudyHoursTrend = weeklyStudyHoursTrend;
    }

    public Map<String, Long> getTaskStatusBreakdown() {
        return this.taskStatusBreakdown;
    }

    public void setTaskStatusBreakdown(Map<String, Long> taskStatusBreakdown) {
        this.taskStatusBreakdown = taskStatusBreakdown;
    }

    public List<Map<String, Object>> getRoadmapCompletionComparison() {
        return this.roadmapCompletionComparison;
    }

    public void setRoadmapCompletionComparison(List<Map<String, Object>> roadmapCompletionComparison) {
        this.roadmapCompletionComparison = roadmapCompletionComparison;
    }


    public static AdminDashboardDtoBuilder builder() {
        return new AdminDashboardDtoBuilder();
    }

    public static class AdminDashboardDtoBuilder {
        private long totalStudents;
        private long activeStudents;
        private long totalRoadmaps;
        private long tasksCompletedToday;
        private double averageStudentProgress;
        private double totalStudyHours;
        private long studentsFallingBehindCount;
        private List<StudentSummaryDto> studentsFallingBehind;
        private List<Map<String, Object>> recentActivity;
        private List<Map<String, Object>> studentProgressDistribution;
        private List<WeeklyProgressDto> weeklyStudyHoursTrend;
        private Map<String, Long> taskStatusBreakdown;
        private List<Map<String, Object>> roadmapCompletionComparison;

        public AdminDashboardDtoBuilder() {}

        public AdminDashboardDtoBuilder totalStudents(long totalStudents) {
            this.totalStudents = totalStudents;
            return this;
        }
        public AdminDashboardDtoBuilder activeStudents(long activeStudents) {
            this.activeStudents = activeStudents;
            return this;
        }
        public AdminDashboardDtoBuilder totalRoadmaps(long totalRoadmaps) {
            this.totalRoadmaps = totalRoadmaps;
            return this;
        }
        public AdminDashboardDtoBuilder tasksCompletedToday(long tasksCompletedToday) {
            this.tasksCompletedToday = tasksCompletedToday;
            return this;
        }
        public AdminDashboardDtoBuilder averageStudentProgress(double averageStudentProgress) {
            this.averageStudentProgress = averageStudentProgress;
            return this;
        }
        public AdminDashboardDtoBuilder totalStudyHours(double totalStudyHours) {
            this.totalStudyHours = totalStudyHours;
            return this;
        }
        public AdminDashboardDtoBuilder studentsFallingBehindCount(long studentsFallingBehindCount) {
            this.studentsFallingBehindCount = studentsFallingBehindCount;
            return this;
        }
        public AdminDashboardDtoBuilder studentsFallingBehind(List<StudentSummaryDto> studentsFallingBehind) {
            this.studentsFallingBehind = studentsFallingBehind;
            return this;
        }
        public AdminDashboardDtoBuilder recentActivity(List<Map<String, Object>> recentActivity) {
            this.recentActivity = recentActivity;
            return this;
        }
        public AdminDashboardDtoBuilder studentProgressDistribution(List<Map<String, Object>> studentProgressDistribution) {
            this.studentProgressDistribution = studentProgressDistribution;
            return this;
        }
        public AdminDashboardDtoBuilder weeklyStudyHoursTrend(List<WeeklyProgressDto> weeklyStudyHoursTrend) {
            this.weeklyStudyHoursTrend = weeklyStudyHoursTrend;
            return this;
        }
        public AdminDashboardDtoBuilder taskStatusBreakdown(Map<String, Long> taskStatusBreakdown) {
            this.taskStatusBreakdown = taskStatusBreakdown;
            return this;
        }
        public AdminDashboardDtoBuilder roadmapCompletionComparison(List<Map<String, Object>> roadmapCompletionComparison) {
            this.roadmapCompletionComparison = roadmapCompletionComparison;
            return this;
        }

        public AdminDashboardDto build() {
            return new AdminDashboardDto(this.totalStudents, this.activeStudents, this.totalRoadmaps, this.tasksCompletedToday, this.averageStudentProgress, this.totalStudyHours, this.studentsFallingBehindCount, this.studentsFallingBehind, this.recentActivity, this.studentProgressDistribution, this.weeklyStudyHoursTrend, this.taskStatusBreakdown, this.roadmapCompletionComparison);
        }
    }

}
