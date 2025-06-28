package com.gyaanbot.course_service.dto;

public class UserResponse {
    private Long id;
    private String email;
    private String role;

    public UserResponse(String role, String email, Long id) {
        this.role = role;
        this.email = email;
        this.id = id;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
