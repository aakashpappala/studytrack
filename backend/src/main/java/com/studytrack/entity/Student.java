package com.studytrack.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String college;

    @Column(length = 50)
    private String enrollmentNo;

    @Column(nullable = false)
    private Integer currentStreak = 0;

    @Column(nullable = false)
    private Integer longestStreak = 0;

    private LocalDate lastStudyDate;

    @Column(nullable = false)
    private Long totalStudyMinutes = 0L;

    public Student() {}

    public Student(Long id, User user, String phone, String college, String enrollmentNo, Integer currentStreak, Integer longestStreak, LocalDate lastStudyDate, Long totalStudyMinutes) {
        this.id = id;
        this.user = user;
        this.phone = phone;
        this.college = college;
        this.enrollmentNo = enrollmentNo;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastStudyDate = lastStudyDate;
        this.totalStudyMinutes = totalStudyMinutes;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Integer getCurrentStreak() {
        return this.currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return this.longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public LocalDate getLastStudyDate() {
        return this.lastStudyDate;
    }

    public void setLastStudyDate(LocalDate lastStudyDate) {
        this.lastStudyDate = lastStudyDate;
    }

    public Long getTotalStudyMinutes() {
        return this.totalStudyMinutes;
    }

    public void setTotalStudyMinutes(Long totalStudyMinutes) {
        this.totalStudyMinutes = totalStudyMinutes;
    }


    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private Long id;
        private User user;
        private String phone;
        private String college;
        private String enrollmentNo;
        private Integer currentStreak;
        private Integer longestStreak;
        private LocalDate lastStudyDate;
        private Long totalStudyMinutes;

        public StudentBuilder() {}

        public StudentBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public StudentBuilder user(User user) {
            this.user = user;
            return this;
        }
        public StudentBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public StudentBuilder college(String college) {
            this.college = college;
            return this;
        }
        public StudentBuilder enrollmentNo(String enrollmentNo) {
            this.enrollmentNo = enrollmentNo;
            return this;
        }
        public StudentBuilder currentStreak(Integer currentStreak) {
            this.currentStreak = currentStreak;
            return this;
        }
        public StudentBuilder longestStreak(Integer longestStreak) {
            this.longestStreak = longestStreak;
            return this;
        }
        public StudentBuilder lastStudyDate(LocalDate lastStudyDate) {
            this.lastStudyDate = lastStudyDate;
            return this;
        }
        public StudentBuilder totalStudyMinutes(Long totalStudyMinutes) {
            this.totalStudyMinutes = totalStudyMinutes;
            return this;
        }

        public Student build() {
            return new Student(this.id, this.user, this.phone, this.college, this.enrollmentNo, this.currentStreak, this.longestStreak, this.lastStudyDate, this.totalStudyMinutes);
        }
    }

}
