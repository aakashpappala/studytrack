package com.studytrack.dto.progress;

public class MonthlyProgressDto {
    private String monthName;
    private int year;
    private int completedTasks;
    private long studyMinutes;

    public MonthlyProgressDto() {}

    public MonthlyProgressDto(String monthName, int year, int completedTasks, long studyMinutes) {
        this.monthName = monthName;
        this.year = year;
        this.completedTasks = completedTasks;
        this.studyMinutes = studyMinutes;
    }

    public String getMonthName() {
        return this.monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
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


    public static MonthlyProgressDtoBuilder builder() {
        return new MonthlyProgressDtoBuilder();
    }

    public static class MonthlyProgressDtoBuilder {
        private String monthName;
        private int year;
        private int completedTasks;
        private long studyMinutes;

        public MonthlyProgressDtoBuilder() {}

        public MonthlyProgressDtoBuilder monthName(String monthName) {
            this.monthName = monthName;
            return this;
        }
        public MonthlyProgressDtoBuilder year(int year) {
            this.year = year;
            return this;
        }
        public MonthlyProgressDtoBuilder completedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
            return this;
        }
        public MonthlyProgressDtoBuilder studyMinutes(long studyMinutes) {
            this.studyMinutes = studyMinutes;
            return this;
        }

        public MonthlyProgressDto build() {
            return new MonthlyProgressDto(this.monthName, this.year, this.completedTasks, this.studyMinutes);
        }
    }

}
