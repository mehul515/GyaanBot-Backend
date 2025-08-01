package com.gyaanbot.user_service.dto;

import com.gyaanbot.user_service.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String email;

    public AuthResponse(String token, String role, String email) {
        this.token = token;
        this.role = role;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role.toString();
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
