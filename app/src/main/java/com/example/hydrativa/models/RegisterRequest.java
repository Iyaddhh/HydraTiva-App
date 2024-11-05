package com.example.hydrativa.models;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;

    public RegisterRequest(String username, String password, String email, String name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
