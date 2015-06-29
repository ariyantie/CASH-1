package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("Latitude")
    public float latitude;

    @SerializedName("Longitude")
    public float longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
