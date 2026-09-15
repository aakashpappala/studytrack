package com.studytrack.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class UpdateStudentRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String college;
    private String enrollmentNo;
    private String status; // ACTIVE, INACTIVE
    private Long roadmapId;
    private String newPassword; // Optional password reset

    public UpdateStudentRequest() {}

    public UpdateStudentRequest(String fullName, String email, String phone, String college, String enrollmentNo, String status, Long roadmapId, String newPassword) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.college = college;
        this.enrollmentNo = enrollmentNo;
        this.status = status;
        this.roadmapId = roadmapId;
        this.newPassword = newPassword;
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

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public static UpdateStudentRequestBuilder builder() {
        return new UpdateStudentRequestBuilder();
    }

    public static class UpdateStudentRequestBuilder {
        private String fullName;
        private String email;
        private String phone;
        private String college;
        private String enrollmentNo;
        private String status;
        private Long roadmapId;
        private String newPassword;

        public UpdateStudentRequestBuilder() {}

        public UpdateStudentRequestBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public UpdateStudentRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UpdateStudentRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public UpdateStudentRequestBuilder college(String college) {
            this.college = college;
            return this;
        }
        public UpdateStudentRequestBuilder enrollmentNo(String enrollmentNo) {
            this.enrollmentNo = enrollmentNo;
            return this;
        }
        public UpdateStudentRequestBuilder status(String status) {
            this.status = status;
            return this;
        }
        public UpdateStudentRequestBuilder roadmapId(Long roadmapId) {
            this.roadmapId = roadmapId;
            return this;
        }
        public UpdateStudentRequestBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public UpdateStudentRequest build() {
            return new UpdateStudentRequest(this.fullName, this.email, this.phone, this.college, this.enrollmentNo, this.status, this.roadmapId, this.newPassword);
        }
    }

}
