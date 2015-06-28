package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class DialogContents {

    @SerializedName("Configuration_Message_Header")
    public String title;

    @SerializedName("Configuration_Message_Description")
    public String body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
