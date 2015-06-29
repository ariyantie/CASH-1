package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class CustomerSupportPhoneNumbers {

    @SerializedName("Toll_Free_Phone_Number")
    public String tollFreePhoneNumber;

    @SerializedName("Phone_Number")
    public String phoneNumber;

    public String getTollFreePhoneNumber() {
        return formatNumber(tollFreePhoneNumber);
    }

    public String getPhoneNumber() {
        return formatNumber(phoneNumber);
    }

    private String formatNumber(String number) {
        if (number.replaceAll("[^\\d.]", "").length() < 10) {
            return number;
        }
        number = number.replaceAll("[^\\d.]", "");
        return "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10);
    }
}
