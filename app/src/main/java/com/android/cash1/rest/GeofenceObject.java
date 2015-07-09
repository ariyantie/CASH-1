package com.android.cash1.rest;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeofenceObject {

    @SerializedName("results")
    private List<JsonObject> mContentObjects;

    @SerializedName("status")
    private String mStatus;

    public List<JsonObject> getContentObjects() {
        return mContentObjects;
    }

    public String getStatus() {
        return mStatus;
    }
}
