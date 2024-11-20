package com.example.hydrativa.models;

public class LinkResponse {
    private String message;

    public LinkResponse(String message){
        this.message= message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
