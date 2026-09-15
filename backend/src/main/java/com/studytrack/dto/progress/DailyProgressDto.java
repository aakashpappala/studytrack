package com.studytrack.dto.progress;

import java.time.LocalDate;

public class DailyProgressDto {
    private LocalDate date;
    private int totalTasks;
    private int completedTasks;
    private double progressPercentage;
    private long studyMinutes;

    public DailyProgressDto() {}

    public DailyProgressDto(LocalDate date, int totalTasks, int completedTasks, double progressPercentage, long studyMinutes) {
        this.date = date;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.progressPercentage = progressPercentage;
        this.studyMinutes = studyMinutes;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public double getProgressPercentage() {
        return this.progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public long getStudyMinutes() {
        return this.studyMinutes;
    }

    public void setStudyMinutes(long studyMinutes) {
        this.studyMinutes = studyMinutes;
    }


    public static DailyProgressDtoBuilder builder() {
        return new DailyProgressDtoBuilder();
    }

    public static class DailyProgressDtoBuilder {
        private LocalDate date;
        private int totalTasks;
        private int completedTasks;
        private double progressPercentage;
        private long studyMinutes;

        public DailyProgressDtoBuilder() {}

        public DailyProgressDtoBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public DailyProgressDtoBuilder totalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
            return this;
        }
        public DailyProgressDtoBuilder completedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
            return this;
        }
        public DailyProgressDtoBuilder progressPercentage(double progressPercentage) {
            this.progressPercentage = progressPercentage;
            return this;
        }
        public DailyProgressDtoBuilder studyMinutes(long studyMinutes) {
            this.studyMinutes = studyMinutes;
            return this;
        }

        public DailyProgressDto build() {
            return new DailyProgressDto(this.date, this.totalTasks, this.completedTasks, this.progressPercentage, this.studyMinutes);
        }
    }

}
