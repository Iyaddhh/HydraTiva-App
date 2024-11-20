package com.example.hydrativa.models;

import java.util.List;

public class KebunResponse {
    private String status;
    private List<Kebun> data;
    private String image_url;

    // Getter dan Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Kebun> getData() {
        return data;
    }

    public void setData(List<Kebun> data) {
        this.data = data;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}