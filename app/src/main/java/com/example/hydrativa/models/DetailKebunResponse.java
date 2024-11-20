package com.example.hydrativa.models;

public class DetailKebunResponse {
    private Kebun kebun;
    private String status;

    public DetailKebunResponse(Kebun kebun, String status) {
        this.kebun = kebun;
        this.status = status;
    }

    public Kebun getKebun() {
        return kebun;
    }

    public void setKebun(Kebun kebun) {
        this.kebun = kebun;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
