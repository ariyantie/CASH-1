package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("StoreName")
    public String name;

    @SerializedName("Latitude")
    public float latitude;

    @SerializedName("Longitude")
    public float longitude;

    @SerializedName("City")
    public String city;

    @SerializedName("Street")
    public String street;

    public int getId() {
        return Integer.parseInt(name);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return street;
    }
}
