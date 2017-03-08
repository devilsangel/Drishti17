package com.drishti.drishti17.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirmal on 2/28/2017.
 */

public class HighLightModel {


    public HighLightModel(String name, String promo, String image,
                          int id, int server_serial_id, boolean is_event, int server_id) {
        this.name = name;
        this.promo = promo;
        this.image = image;
        this.id = id;
        this.server_serial_id = server_serial_id;
        this.server_id = server_id;
        this.is_event = is_event;
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

    public boolean getIsEvent() {
        return is_event;
    }

    public int getServerId() {
        return server_id;
    }



    transient public int id;
    @SerializedName("id")
    public int server_serial_id;
    public int server_id;
    public boolean is_event;
    public String name, promo, image;


}
