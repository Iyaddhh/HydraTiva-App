package com.example.hydrativa.models;

public class EditProfileRequest {
    private String name;
    private String username;
    private String email;
    private String telp;

    public EditProfileRequest(String username, String jenis_kelamin, String name, String telp) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.telp = telp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
