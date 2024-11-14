package com.example.hydrativa.models;

public class EditProfileRequest {
    private String username;
    private String jenis_kelamin;
    private String name;
    private String telp;

    public EditProfileRequest(String username, String jenis_kelamin, String name, String telp) {
        this.username = username;
        this.jenis_kelamin = jenis_kelamin;
        this.name = name;
        this.telp = telp;
    }
    // Getters and setters, if needed
}
