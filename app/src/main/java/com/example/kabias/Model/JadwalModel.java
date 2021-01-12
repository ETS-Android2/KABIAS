package com.example.kabias.Model;

public class JadwalModel {

    private String stasiun, detail, kategori, waktu;


    public JadwalModel() {

    }

    public JadwalModel(String stasiun, String detail, String kategori, String waktu) {
        this.stasiun = stasiun;
        this.detail = detail;
        this.kategori = kategori;
        this.waktu = waktu;
    }

    public String getStasiun() {
        return stasiun;
    }

    public void setStasiun(String stasiun) {
        this.stasiun = stasiun;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
