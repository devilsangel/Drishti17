package com.drishti.drishti17.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevin on 2/9/17.
 */

public class Student {
    public int id,score;
    @SerializedName("normalizedScore")
    public int gunt_score;
    public String name,uid,phone,picture,collegeId;
    public enum accomodation{none,male,female};
    public boolean registered;
}
