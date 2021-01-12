package com.example.kabias.Model;

public class ListStasiunModel {

    private String name, code, address, map, image1, image2, image3;
    private boolean isAtm, isHotel, isMarket, isMall, isRestoran, isTerminal;

        public ListStasiunModel() {
        }

    public ListStasiunModel(String name, String code, String address, String map, String image1, String image2, String image3, boolean isAtm, boolean isHotel, boolean isMarket, boolean isMall, boolean isRestoran, boolean isTerminal) {
        this.name = name;
        this.code = code;
        this.address = address;
        this.map = map;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.isAtm = isAtm;
        this.isHotel = isHotel;
        this.isMarket = isMarket;
        this.isMall = isMall;
        this.isRestoran = isRestoran;
        this.isTerminal = isTerminal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public boolean isAtm() {
        return isAtm;
    }

    public void setAtm(boolean atm) {
        isAtm = atm;
    }

    public boolean isHotel() {
        return isHotel;
    }

    public void setHotel(boolean hotel) {
        isHotel = hotel;
    }

    public boolean isMarket() {
        return isMarket;
    }

    public void setMarket(boolean market) {
        isMarket = market;
    }

    public boolean isMall() {
        return isMall;
    }

    public void setMall(boolean mall) {
        isMall = mall;
    }

    public boolean isRestoran() {
        return isRestoran;
    }

    public void setRestoran(boolean restoran) {
        isRestoran = restoran;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }
}
