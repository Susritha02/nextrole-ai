package com.jobrec.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String fullName;
    private String email;
    private String headline;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, Long id, String fullName, String email, String headline, String role) {
        this.token = token;
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.headline = headline;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
