package com.android.cash1.model;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

public class Cash1LocationsNumber {

    @SerializedName("Toll_Free_Phone_Number")
    public String nearestLocationNumber;

    public Spanned getNearestLocationNumber() {
        return formatNumber(nearestLocationNumber);
    }

    private Spanned formatNumber(String phoneNumber) {
        return Html.fromHtml("<u>" + phoneNumber + "</u>");
    }
}
