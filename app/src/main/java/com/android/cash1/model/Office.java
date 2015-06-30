package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class Office {

    @SerializedName("StoreName")
    public String name;

    @SerializedName("Latitude")
    public float latitude;

    @SerializedName("Longitude")
    public float longitude;

    @SerializedName("Street")
    public String street;

    @SerializedName("State")
    public String state;

    @SerializedName("Zip_Code")
    public String zipCode;

    @SerializedName("City")
    public String city;

    public int getId() {
        return Integer.parseInt(name);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return state + " " + zipCode + ", " + city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCodeString() {
        return zipCode;
    }
}
