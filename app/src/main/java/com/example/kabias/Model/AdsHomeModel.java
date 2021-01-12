package com.example.kabias.Model;

public class AdsHomeModel {

    private String image, category;

    public AdsHomeModel() {
    }

    public AdsHomeModel(String image, String category) {
        this.image = image;
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

