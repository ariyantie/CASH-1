package com.android.cash1.model;

import android.telephony.PhoneNumberUtils;

import com.google.gson.annotations.SerializedName;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

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

    private String formatNumber(String phoneNumber) {
        if (SDK_INT >= LOLLIPOP) {
            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "US");
        }
        return phoneNumber;
    }
}
