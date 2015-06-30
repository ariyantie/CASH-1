package com.android.cash1.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class OfficeDetails {

    @SerializedName("Street")
    public String street;

    @SerializedName("State")
    public String state;

    @SerializedName("Zip_Code")
    public String zipCode;

    @SerializedName("City")
    public String city;

    @SerializedName("Phone_No")
    public String phoneNumber;

    @SerializedName("Fax_No")
    public String faxNumber;

    @SerializedName("PicPath")
    public String imageUrl;

    public String getStreet() {
        return street;
    }

    public String getAddress() {
        return state + " " + zipCode + ", " + city;
    }

    public String getPhoneNumber() {
        return formatNumber(phoneNumber);
    }

    public String getFaxNumber() {
        return formatNumber(faxNumber);
    }

    public String formatNumber(String number) {
        if (number == null) {
            return "Not specified";
        }
        if (number.replaceAll("[^\\d.]", "").length() < 10) {
            return number;
        }
        number = number.replaceAll("[^\\d.]", "");
        return "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10);
    }

    public String getImageUrl() {
        Log.d("OfficeDetails", "Image URL: " + imageUrl.replaceAll("\\\\", ""));
        return imageUrl.replaceAll("\\\\", "");
    }
}
