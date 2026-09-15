package com.studytrack.dto.admin;

import com.studytrack.dto.progress.DailyProgressDto;
import com.studytrack.dto.progress.MonthlyProgressDto;
import com.studytrack.dto.progress.SubjectProgressDto;
import com.studytrack.dto.progress.WeeklyProgressDto;
import com.studytrack.dto.task.TaskDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDetailDto {
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
    private int longestStreak;
    private LocalDate lastStudyDate;

    private String todayStudyTime;
    private String weeklyStudyTime;
    private String monthlyStudyTime;
    private long totalStudyMinutes;

    private List<SubjectProgressDto> subjectProgress = new ArrayList<>();

    private List<TaskDto> todayTasks = new ArrayList<>();

    private List<TaskDto> completedTasks = new ArrayList<>();

    private List<TaskDto> pendingTasks = new ArrayList<>();

    private List<WeeklyProgressDto> weeklyProgress = new ArrayList<>();

    private List<MonthlyProgressDto> monthlyProgress = new ArrayList<>();

    private List<DailyProgressDto> recentDailyActivity = new ArrayList<>();

    public StudentDetailDto() {}

    public StudentDetailDto(Long id, Long userId, String fullName, String email, String phone, String college, String enrollmentNo, String status, Long roadmapId, String roadmapTitle, double overallProgress, double todayProgress, int currentStreak, int longestStreak, LocalDate lastStudyDate, String todayStudyTime, String weeklyStudyTime, String monthlyStudyTime, long totalStudyMinutes, List<SubjectProgressDto> subjectProgress, List<TaskDto> todayTasks, List<TaskDto> completedTasks, List<TaskDto> pendingTasks, List<WeeklyProgressDto> weeklyProgress, List<MonthlyProgressDto> monthlyProgress, List<DailyProgressDto> recentDailyActivity) {
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
        this.longestStreak = longestStreak;
        this.lastStudyDate = lastStudyDate;
        this.todayStudyTime = todayStudyTime;
        this.weeklyStudyTime = weeklyStudyTime;
        this.monthlyStudyTime = monthlyStudyTime;
        this.totalStudyMinutes = totalStudyMinutes;
        this.subjectProgress = subjectProgress;
        this.todayTasks = todayTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.weeklyProgress = weeklyProgress;
        this.monthlyProgress = monthlyProgress;
        this.recentDailyActivity = recentDailyActivity;
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

    public List<TaskDto> getTodayTasks() {
        return this.todayTasks;
    }

    public void setTodayTasks(List<TaskDto> todayTasks) {
        this.todayTasks = todayTasks;
    }

    public List<TaskDto> getCompletedTasks() {
        return this.completedTasks;
    }

    public void setCompletedTasks(List<TaskDto> completedTasks) {
        this.completedTasks = completedTasks;
    }

    public List<TaskDto> getPendingTasks() {
        return this.pendingTasks;
    }

    public void setPendingTasks(List<TaskDto> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public List<WeeklyProgressDto> getWeeklyProgress() {
        return this.weeklyProgress;
    }

    public void setWeeklyProgress(List<WeeklyProgressDto> weeklyProgress) {
        this.weeklyProgress = weeklyProgress;
    }

    public List<MonthlyProgressDto> getMonthlyProgress() {
        return this.monthlyProgress;
    }

    public void setMonthlyProgress(List<MonthlyProgressDto> monthlyProgress) {
        this.monthlyProgress = monthlyProgress;
    }

    public List<DailyProgressDto> getRecentDailyActivity() {
        return this.recentDailyActivity;
    }

    public void setRecentDailyActivity(List<DailyProgressDto> recentDailyActivity) {
        this.recentDailyActivity = recentDailyActivity;
    }


    public static StudentDetailDtoBuilder builder() {
        return new StudentDetailDtoBuilder();
    }

    public static class StudentDetailDtoBuilder {
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
        private int longestStreak;
        private LocalDate lastStudyDate;
        private String todayStudyTime;
        private String weeklyStudyTime;
        private String monthlyStudyTime;
        private long totalStudyMinutes;
        private List<SubjectProgressDto> subjectProgress;
        private List<TaskDto> todayTasks;
        private List<TaskDto> completedTasks;
        private List<TaskDto> pendingTasks;
        private List<WeeklyProgressDto> weeklyProgress;
        private List<MonthlyProgressDto> monthlyProgress;
        private List<DailyProgressDto> recentDailyActivity;

        public StudentDetailDtoBuilder() {}

        public StudentDetailDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudentDetailDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public StudentDetailDtoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public StudentDetailDtoBuilder email(String email) {
            this.email = email;
            return this;
        }
        public StudentDetailDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public StudentDetailDtoBuilder college(String college) {
            this.college = college;
            return this;
        }
        public StudentDetailDtoBuilder enrollmentNo(String enrollmentNo) {
            this.enrollmentNo = enrollmentNo;
            return this;
        }
        public StudentDetailDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public StudentDetailDtoBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }
        public StudentDetailDtoBuilder roadmapTitle(String roadmapTitle) {
            this.roadmapTitle = roadmapTitle;
            return this;
        }
        public StudentDetailDtoBuilder overallProgress(double overallProgress) {
            this.overallProgress = overallProgress;
            return this;
        }
        public StudentDetailDtoBuilder todayProgress(double todayProgress) {
            this.todayProgress = todayProgress;
            return this;
        }
        public StudentDetailDtoBuilder currentStreak(int currentStreak) {
            this.currentStreak = currentStreak;
            return this;
        }
        public StudentDetailDtoBuilder longestStreak(int longestStreak) {
            this.longestStreak = longestStreak;
            return this;
        }
        public StudentDetailDtoBuilder lastStudyDate(LocalDate lastStudyDate) {
            this.lastStudyDate = lastStudyDate;
            return this;
        }
        public StudentDetailDtoBuilder todayStudyTime(String todayStudyTime) {
            this.todayStudyTime = todayStudyTime;
            return this;
        }
        public StudentDetailDtoBuilder weeklyStudyTime(String weeklyStudyTime) {
            this.weeklyStudyTime = weeklyStudyTime;
            return this;
        }
        public StudentDetailDtoBuilder monthlyStudyTime(String monthlyStudyTime) {
            this.monthlyStudyTime = monthlyStudyTime;
            return this;
        }
        public StudentDetailDtoBuilder totalStudyMinutes(long totalStudyMinutes) {
            this.totalStudyMinutes = totalStudyMinutes;
            return this;
        }
        public StudentDetailDtoBuilder subjectProgress(List<SubjectProgressDto> subjectProgress) {
            this.subjectProgress = subjectProgress;
            return this;
        }
        public StudentDetailDtoBuilder todayTasks(List<TaskDto> todayTasks) {
            this.todayTasks = todayTasks;
            return this;
        }
        public StudentDetailDtoBuilder completedTasks(List<TaskDto> completedTasks) {
            this.completedTasks = completedTasks;
            return this;
        }
        public StudentDetailDtoBuilder pendingTasks(List<TaskDto> pendingTasks) {
            this.pendingTasks = pendingTasks;
            return this;
        }
        public StudentDetailDtoBuilder weeklyProgress(List<WeeklyProgressDto> weeklyProgress) {
            this.weeklyProgress = weeklyProgress;
            return this;
        }
        public StudentDetailDtoBuilder monthlyProgress(List<MonthlyProgressDto> monthlyProgress) {
            this.monthlyProgress = monthlyProgress;
            return this;
        }
        public StudentDetailDtoBuilder recentDailyActivity(List<DailyProgressDto> recentDailyActivity) {
            this.recentDailyActivity = recentDailyActivity;
            return this;
        }

        public StudentDetailDto build() {
            return new StudentDetailDto(this.id, this.userId, this.fullName, this.email, this.phone, this.college, this.enrollmentNo, this.status, this.roadmapId, this.roadmapTitle, this.overallProgress, this.todayProgress, this.currentStreak, this.longestStreak, this.lastStudyDate, this.todayStudyTime, this.weeklyStudyTime, this.monthlyStudyTime, this.totalStudyMinutes, this.subjectProgress, this.todayTasks, this.completedTasks, this.pendingTasks, this.weeklyProgress, this.monthlyProgress, this.recentDailyActivity);
        }
    }

}
