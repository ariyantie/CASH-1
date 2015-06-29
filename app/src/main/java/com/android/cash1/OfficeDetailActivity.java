package com.android.cash1;

import android.os.Bundle;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OfficeDetailActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_detail);

        setupActionBar();
        setupFooter();

        int storeId = getIntent().getIntExtra("store_id", -1);

        ApiService service = new RestClient().getApiService();
        service.getStoreDetails(storeId + "", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
