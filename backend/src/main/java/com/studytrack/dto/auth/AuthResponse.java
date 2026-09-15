package com.studytrack.dto.auth;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private Long studentId;

    public AuthResponse() {}

    public AuthResponse(String token, String tokenType, Long id, String email, String fullName, String role, Long studentId) {
        this.token = token;
        this.tokenType = tokenType;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.studentId = studentId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
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

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }


    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private String tokenType;
        private Long id;
        private String email;
        private String fullName;
        private String role;
        private Long studentId;

        public AuthResponseBuilder() {}

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }
        public AuthResponseBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
        public AuthResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }
        public AuthResponseBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public AuthResponseBuilder role(String role) {
            this.role = role;
            return this;
        }
        public AuthResponseBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(this.token, this.tokenType, this.id, this.email, this.fullName, this.role, this.studentId);
        }
    }

}
