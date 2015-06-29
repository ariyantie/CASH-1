package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class CollectionsPhoneNumbers {

    @SerializedName("Toll_Free_Phone_Number")
    public String tollFreePhoneNumber;

    @SerializedName("Fax_Number")
    public String faxNumber;

    public String getTollFreePhoneNumber() {
        return formatNumber(tollFreePhoneNumber);
    }

    public String getFaxNumber() {
        return formatNumber(faxNumber);
    }

    private String formatNumber(String number) {
        if (number.replaceAll("[^\\d.]", "").length() < 10) {
            return number;
        }
        number = number.replaceAll("[^\\d.]", "");
        return "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10);
    }
}
