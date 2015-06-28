package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AgreementActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_layout,
                null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);


        ApiService service = new RestClient().getApiService();
        service.getAgreementText(new Callback<HashMap<String, String>>() {
            @Override
            public void success(HashMap<String, String> responseHashMap, Response response) {
                findViewById(R.id.spinner).setVisibility(View.GONE);
                findViewById(R.id.container).setVisibility(View.VISIBLE);

                String agreementHtmlText = responseHashMap.get("html");

                TextView tv = (TextView) findViewById(R.id.agreement);
                tv.setText(Html.fromHtml(agreementHtmlText));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void agree(View view) {
        int userId = getUserId();

        ApiService service = new RestClient().getApiService();
        service.saveAgreement(userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                returnToLoginScreen();
                Toast.makeText(AgreementActivity.this, "Now please try to login again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void returnToLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
