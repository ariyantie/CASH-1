package com.android.cash1.rest;

import retrofit.RestAdapter;

import static retrofit.RestAdapter.LogLevel.FULL;

public class LocationClient {

    private LocationApiService mService;

    public LocationClient() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://maps.googleapis.com/maps/api/geocode")
                .setLogLevel(FULL)
                .build();

        mService = restAdapter.create(LocationApiService.class);
    }

    public LocationApiService getApiService() {
        return mService;
    }
}
