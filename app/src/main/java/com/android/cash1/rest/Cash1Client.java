package com.android.cash1.rest;

import retrofit.RestAdapter;

import static retrofit.RestAdapter.LogLevel.FULL;

public class Cash1Client {

    private Cash1ApiService mService;

    public Cash1Client() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://mobile.cash1loans.com/Cash1WF.svc")
                .setLogLevel(FULL)
                .build();

        mService = restAdapter.create(Cash1ApiService.class);
    }

    public Cash1ApiService getApiService() {
        return mService;
    }
}
