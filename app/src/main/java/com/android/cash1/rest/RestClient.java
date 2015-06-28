package com.android.cash1.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestClient {

    private ApiService mService;

    public RestClient() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://mobile.cash1loans.com/Cash1WF.svc")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mService;
    }
}
