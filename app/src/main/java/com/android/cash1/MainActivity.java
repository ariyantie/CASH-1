package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupFooter();
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();
        findViewById(R.id.button_back).setVisibility(View.GONE);
    }

    public void getCash(View view) {
        int userId = getUserId();

        ApiService service = new RestClient().getApiService();
        service.checkCashAdvance(userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                try {
                    int appStage = responseObj.getAsJsonPrimitive("App_Stage").getAsInt();
                    if (appStage > 0) {
                        navigateToGetCashActivity();
                    } else {
                        navigateToNotifyActivity();
                    }
                } catch (Exception e) {
                    Log.i("MainActivity", "Cash advance ID is null. " +
                            "Treat as \"0\" and prevent access.");
                    navigateToNotifyActivity();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void navigateToGetCashActivity() {
        startActivity(new Intent(this, GetCashActivity.class));
    }

    private void navigateToNotifyActivity() {
        Intent intent = new Intent(this, GetCashNotifyActivity.class);
        intent.putExtra("message_id", 22);
        startActivity(intent);
    }

    public void makePayment(View view) {
        startActivity(new Intent(this, MakePaymentActivity.class));
    }

    public void updateInfo(View view) {
        startActivity(new Intent(this, UpdateInfoActivity.class));
    }

    public void accountDetails(View view) {
        startActivity(new Intent(this, AccountDetailsActivity.class));
    }

    public void creditLimit(View view) {
        startActivity(new Intent(this, IncreaseLimitActivity.class));
    }

    @Override
    public void goHome(View view) {
        Toast.makeText(this, "Already opened", Toast.LENGTH_SHORT).show();
        closeFooter();
    }
}
