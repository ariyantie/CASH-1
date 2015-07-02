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

    @SerializedName("Phone_No")
    public String phone;

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

    public String getCity() {
        return city.toUpperCase();
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        if (phone != null) {
            return formatNumber(phone);
        } else {
            return "";
        }
    }

    private String formatNumber(String number) {
        if (number.replaceAll("[^\\d.]", "").length() < 10) {
            return number;
        }
        number = number.replaceAll("[^\\d.]", "");
        return "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10);
    }
}
