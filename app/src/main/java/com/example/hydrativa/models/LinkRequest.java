package com.example.hydrativa.models;

public class LinkRequest {
    private String email;

    public LinkRequest(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
