package com.example.hydrativa.models;

public class RegisterResponse {
    private String token;

    // Constructor (optional)
    public RegisterResponse(String token) {
        this.token = token;
    }

    // Getter for token
    public String getToken() {
        return token;
    }

    // Setter for token (if needed)
    public void setToken(String token) {
        this.token = token;
    }
}
