package com.android.cash1.model;

import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class CollectionsPhoneNumbers {

    @SerializedName("Toll_Free_Phone_Number")
    public String tollFreePhoneNumber;

    @SerializedName("Fax_Number")
    public String faxNumber;

    public Spanned getTollFreePhoneNumber() {
        return formatNumber(tollFreePhoneNumber);
    }

    public Spanned getFaxNumber() {
        return formatNumber(faxNumber);
    }

    private Spanned formatNumber(String phoneNumber) {
        if (SDK_INT >= LOLLIPOP) {
            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "US");
        }
        return Html.fromHtml("<u>" + phoneNumber + "</u>"); // underline phone number
    }
}
