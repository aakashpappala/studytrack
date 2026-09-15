package com.studytrack.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class CreateStudentRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phone;
    private String college;
    private String enrollmentNo;
    private Long roadmapId;

    public CreateStudentRequest() {}

    public CreateStudentRequest(String fullName, String email, String password, String phone, String college, String enrollmentNo, Long roadmapId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.college = college;
        this.enrollmentNo = enrollmentNo;
        this.roadmapId = roadmapId;
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getRoadmapId() {
        return this.roadmapId;
    }

    public void setRoadmapId(Long roadmapId) {
        this.roadmapId = roadmapId;
    }


    public static CreateStudentRequestBuilder builder() {
        return new CreateStudentRequestBuilder();
    }

    public static class CreateStudentRequestBuilder {
        private String fullName;
        private String email;
        private String password;
        private String phone;
        private String college;
        private String enrollmentNo;
        private Long roadmapId;

        public CreateStudentRequestBuilder() {}

        public CreateStudentRequestBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public CreateStudentRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        public CreateStudentRequestBuilder password(String password) {
            this.password = password;
            return this;
        }
        public CreateStudentRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public CreateStudentRequestBuilder college(String college) {
            this.college = college;
            return this;
        }
        public CreateStudentRequestBuilder enrollmentNo(String enrollmentNo) {
            this.enrollmentNo = enrollmentNo;
            return this;
        }
        public CreateStudentRequestBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }

        public CreateStudentRequest build() {
            return new CreateStudentRequest(this.fullName, this.email, this.password, this.phone, this.college, this.enrollmentNo, this.roadmapId);
        }
    }

}
