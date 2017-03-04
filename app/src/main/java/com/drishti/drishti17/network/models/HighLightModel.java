package com.drishti.drishti17.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirmal on 2/28/2017.
 */

public class HighLightModel {

    public HighLightModel() {

    }

    public HighLightModel(String name, String promo, String image, int id,int server_id) {
        this.name = name;
        this.promo = promo;
        this.image = image;
        this.id = id;
        this.server_id = server_id;
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

    transient public int id;
    @SerializedName("id")
    public int server_id;
    public String name, promo, image;


}
