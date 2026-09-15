package com.studytrack.dto.auth;

public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String status;
    private Long studentId;

    public UserDto() {}

    public UserDto(Long id, String email, String fullName, String role, String status, Long studentId) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.studentId = studentId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }


    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private Long id;
        private String email;
        private String fullName;
        private String role;
        private String status;
        private Long studentId;

        public UserDtoBuilder() {}

        public UserDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserDtoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public UserDtoBuilder role(String role) {
            this.role = role;
            return this;
        }
        public UserDtoBuilder status(String status) {
            this.status = status;
            return this;
        }
        public UserDtoBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public UserDto build() {
            return new UserDto(this.id, this.email, this.fullName, this.role, this.status, this.studentId);
        }
    }

}
