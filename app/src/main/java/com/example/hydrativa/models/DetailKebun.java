package com.example.hydrativa.models;

public class DetailKebun {
    private int moisture;
    private float ph;
    private String status;
    private String tanggalPenyiraman;

    public int getMoisture() {
        return moisture;
    }

    public void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggalPenyiraman() {
        return tanggalPenyiraman;
    }

    public void setTanggalPenyiraman(String tanggalPenyiraman) {
        this.tanggalPenyiraman = tanggalPenyiraman;
    }
}
