package com.example.hydrativa.models;

public class MateriResponse {
    private int id;
    private String judul;
    private String waktu;
    private String sumber;
    private String deskripsi;
    private String gambar;

    public MateriResponse(int id, String judul, String waktu, String sumber, String deskripsi, String gambar) {
        this.id = id;
        this.judul = judul;
        this.waktu = waktu;
        this.sumber = sumber;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getSumber() {
        return sumber;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambar() {
        return gambar;
    }
}
