package com.example.hydrativa.models;

import java.util.List;

public class KebunResponse {
    private String status; // Tambahkan status jika ada
    private List<Kebun> data; // Gantilah sesuai dengan struktur JSON Anda

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
}