package com.studytrack.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phone;
    private String college;
    private String enrollmentNo;
    private Long roadmapId;

    public RegisterRequest() {}

    public RegisterRequest(String fullName, String email, String password, String phone, String college, String enrollmentNo, Long roadmapId) {
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


    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }

    public static class RegisterRequestBuilder {
        private String fullName;
        private String email;
        private String password;
        private String phone;
        private String college;
        private String enrollmentNo;
        private Long roadmapId;

        public RegisterRequestBuilder() {}

        public RegisterRequestBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public RegisterRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        public RegisterRequestBuilder password(String password) {
            this.password = password;
            return this;
        }
        public RegisterRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public RegisterRequestBuilder college(String college) {
            this.college = college;
            return this;
        }
        public RegisterRequestBuilder enrollmentNo(String enrollmentNo) {
            this.enrollmentNo = enrollmentNo;
            return this;
        }
        public RegisterRequestBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this.fullName, this.email, this.password, this.phone, this.college, this.enrollmentNo, this.roadmapId);
        }
    }

}
