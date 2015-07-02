package com.android.cash1.rest;

import retrofit.RestAdapter;

import static retrofit.RestAdapter.LogLevel.FULL;

public class RestClient {

    private ApiService mService;

    public RestClient() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://mobile.cash1loans.com/Cash1WF.svc")
                .setLogLevel(FULL)
                .build();

        mService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mService;
    }
}
