package com.example.hydrativa.models;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;
    private String telp;

    public RegisterRequest(String username, String password, String email, String name, String telp) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.telp = telp;
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
    public String getTelp(){return telp;}
}
