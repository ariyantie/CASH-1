package com.android.cash1.rest;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface LocationApiService {

    @GET("/json")
    void getCoordinatesForAddress(@Query("address") String address, Callback<GeofenceObject> callback);
}
