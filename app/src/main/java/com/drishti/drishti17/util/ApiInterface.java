package com.drishti.drishti17.util;

import com.drishti.drishti17.network.models.College;
import com.drishti.drishti17.network.models.HighLightModel;
import com.drishti.drishti17.network.models.Student;
import com.drishti.drishti17.network.models.EventModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by kevin on 2/9/17
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("student/login")
    Call<Student> login(@Field("idToken") String idToken);

    @FormUrlEncoded
    @POST("student/register")
    Call<String> register(@Header("x-auth-token") String idToken,
                          @Field("phone") String phone,
                          @Field("accomodation") Student.Accomodation accomodation,
                          @Field("collegeId")int collegeId);


    @GET("public/event")
    Call<List<EventModel>> getEventList();

    @GET("/public/highlight")
    Call<List<HighLightModel>> getHightlightList();

    @GET("public/college")
    Call<List<College>> getAllColleges();

    @GET("student/event")
    Call<List<EventModel>> getUserEvents(@Header("x-auth-token") String idToken);
}
