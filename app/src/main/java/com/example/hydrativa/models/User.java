package com.example.hydrativa.models;

public class User {
    private String username;
    private String jenis_kelamin;
    private String name;
    private String telp;
    private String gambar;

    public User(String username, String jenis_kelamin, String name, String telp) {
        this.username = username;
        this.jenis_kelamin = jenis_kelamin;
        this.name = name;
        this.telp = telp;
        this.gambar = gambar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
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

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
