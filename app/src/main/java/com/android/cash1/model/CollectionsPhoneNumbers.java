package com.android.cash1.model;

import android.telephony.PhoneNumberUtils;

import com.google.gson.annotations.SerializedName;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

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

    private String formatNumber(String phoneNumber) {
        if (SDK_INT >= LOLLIPOP) {
            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "US");
        }
        return phoneNumber;
    }
}
