package com.example.hydrativa.models;

public class HistoryPenyiraman {
    private String waktu;
    private String status;
    private String moisture;
    private String pH;

    public HistoryPenyiraman(String waktu, String status, String moisture, String pH) {
        this.waktu = waktu;
        this.status = status;
        this.moisture = moisture;
        this.pH = pH;
    }

    public String getDate() {
        return waktu;
    }

    public void setDate(String waktu) {
        this.waktu = waktu;
    }

    public String getSoilCondition() {
        return status;
    }

    public void setSoilCondition(String status) {
        this.status = status;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getPhLevel() {
        return pH;
    }

    public void setPhLevel(String pH) {
        this.pH = pH;
    }
}
