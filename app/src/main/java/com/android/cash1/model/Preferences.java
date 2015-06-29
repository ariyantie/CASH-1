package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class Preferences {

    @SerializedName("Location_Services")
    public boolean useCurrentLocation;

    public boolean useCurrentLocation() {
        return useCurrentLocation;
    }
}
