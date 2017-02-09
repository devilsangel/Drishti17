package com.drishti.drishti17.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by droidcafe on 2/5/2017
 */

public class EventListModel {

    public String name,http_url;

    @SerializedName(value = "storage_url", alternate = {"url"})
    public String storage_url;
}
