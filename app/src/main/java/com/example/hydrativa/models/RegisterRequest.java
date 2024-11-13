package com.example.hydrativa.models;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;
    private String telp;
    private String jenis_kelamin;

    public RegisterRequest(String username, String password, String email, String name, String telp, String jenis_kelamin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.telp = telp;
        this.jenis_kelamin = jenis_kelamin;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }
}
