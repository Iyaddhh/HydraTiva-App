package com.example.hydrativa.models;

public class RegisterResponse {
    private String message;

    // Constructor (optional)
    public RegisterResponse(String message) {
        this.message = message;
    }

    // Getter for token
    public String getMessage() {
        return message;
    }

    // Setter for token (if needed)
    public void setMessage(String message) {
        this.message = message;
    }
}
