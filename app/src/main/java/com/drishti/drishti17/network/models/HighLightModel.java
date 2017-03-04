package com.drishti.drishti17.network.models;

/**
 * Created by nirmal on 2/28/2017.
 */

public class HighLightModel {

    public HighLightModel() {

    }

    public HighLightModel(String name, String promo, String image, int id) {
        this.name = name;
        this.promo = promo;
        this.image = image;
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getPromo() {
        return promo;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int id;
    public String name,promo,image;


}
