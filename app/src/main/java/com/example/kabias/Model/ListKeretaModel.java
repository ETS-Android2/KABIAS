package com.example.kabias.Model;

public class ListKeretaModel {

    private String awal, tujuan, kereta, rute;

    public ListKeretaModel() {
    }

    public ListKeretaModel(String awal, String tujuan, String kereta, String rute) {
        this.awal = awal;
        this.tujuan = tujuan;
        this.kereta = kereta;
        this.rute = rute;
    }

    public String getAwal() {
        return awal;
    }

    public void setAwal(String awal) {
        this.awal = awal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getKereta() {
        return kereta;
    }

    public void setKereta(String kereta) {
        this.kereta = kereta;
    }

    public String getRute() {
        return rute;
    }

    public void setRute(String rute) {
        this.rute = rute;
    }
}
