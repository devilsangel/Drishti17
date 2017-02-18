package com.drishti.drishti17.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin on 2/9/17
 */

public class ApiClient {
    public static final String  HOST_URL = "http://server.drishticet.org/" ;
    public static final String  NODE_PORT = "";
    public static final String  BASE_URL = HOST_URL ;

    private static Retrofit retrofit = null;


    public static ApiInterface getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiInterface.class);
    }

}
