package com.studytrack.dto.progress;

import java.time.LocalDate;

public class WeeklyProgressDto {
    private String weekLabel;
    private LocalDate startDate;
    private LocalDate endDate;
    private int completedTasks;
    private long studyMinutes;
    private double progressPercentage;

    public WeeklyProgressDto() {}

    public WeeklyProgressDto(String weekLabel, LocalDate startDate, LocalDate endDate, int completedTasks, long studyMinutes, double progressPercentage) {
        this.weekLabel = weekLabel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completedTasks = completedTasks;
        this.studyMinutes = studyMinutes;
        this.progressPercentage = progressPercentage;
    }

    public String getWeekLabel() {
        return this.weekLabel;
    }

    public void setWeekLabel(String weekLabel) {
        this.weekLabel = weekLabel;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCompletedTasks() {
        return this.completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getStudyMinutes() {
        return this.studyMinutes;
    }

    public void setStudyMinutes(long studyMinutes) {
        this.studyMinutes = studyMinutes;
    }

    public double getProgressPercentage() {
        return this.progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }


    public static WeeklyProgressDtoBuilder builder() {
        return new WeeklyProgressDtoBuilder();
    }

    public static class WeeklyProgressDtoBuilder {
        private String weekLabel;
        private LocalDate startDate;
        private LocalDate endDate;
        private int completedTasks;
        private long studyMinutes;
        private double progressPercentage;

        public WeeklyProgressDtoBuilder() {}

        public WeeklyProgressDtoBuilder weekLabel(String weekLabel) {
            this.weekLabel = weekLabel;
            return this;
        }
        public WeeklyProgressDtoBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }
        public WeeklyProgressDtoBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public WeeklyProgressDtoBuilder completedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
            return this;
        }
        public WeeklyProgressDtoBuilder studyMinutes(long studyMinutes) {
            this.studyMinutes = studyMinutes;
            return this;
        }
        public WeeklyProgressDtoBuilder progressPercentage(double progressPercentage) {
            this.progressPercentage = progressPercentage;
            return this;
        }

        public WeeklyProgressDto build() {
            return new WeeklyProgressDto(this.weekLabel, this.startDate, this.endDate, this.completedTasks, this.studyMinutes, this.progressPercentage);
        }
    }

}
