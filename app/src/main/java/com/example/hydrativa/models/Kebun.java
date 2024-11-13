package com.example.hydrativa.models;

public class Kebun {
    private int kebun_id;
    private String nama_kebun;
    private int luas_lahan;
    private String lokasi_kebun;
    private int moisture;
    private Float pH;
    private String status;
    private String gambar;

    public Kebun(int kebun_id, String nama_kebun, int luas_lahan, String lokasi_kebun, int moisture, Float pH, String status, String gambar) {
        this.kebun_id = kebun_id;
        this.nama_kebun = nama_kebun;
        this.luas_lahan = luas_lahan;
        this.lokasi_kebun = lokasi_kebun;
        this.moisture = moisture;
        this.pH = pH;
        this.status = status;
        this.gambar = gambar;
    }

    public int getKebun_id() {
        return kebun_id;
    }

    public void setKebun_id(int kebun_id) {
        this.kebun_id = kebun_id;
    }

    public String getNama_kebun() {
        return nama_kebun;
    }

    public void setNama_kebun(String nama_kebun) {
        this.nama_kebun = nama_kebun;
    }

    public int getLuas_lahan() {
        return luas_lahan;
    }

    public void setLuas_lahan(int luas_lahan) {
        this.luas_lahan = luas_lahan;
    }

    public String getLokasi_kebun() {
        return lokasi_kebun;
    }

    public void setLokasi_kebun(String lokasi_kebun) {
        this.lokasi_kebun = lokasi_kebun;
    }

    public int getMoisture() {
        return moisture;
    }

    public void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public Float getpH() {
        return pH;
    }

    public void setpH(Float pH) {
        this.pH = pH;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }}