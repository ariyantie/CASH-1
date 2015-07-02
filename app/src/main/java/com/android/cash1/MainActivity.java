package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

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

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        double ratio = ((float) (width))/268;
        int height = (int)(ratio*50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);

        findViewById(R.id.get_cash).setLayoutParams(layoutParams);
        findViewById(R.id.make_payment).setLayoutParams(layoutParams);
        findViewById(R.id.update_info).setLayoutParams(layoutParams);
        findViewById(R.id.view_details).setLayoutParams(layoutParams);
        findViewById(R.id.increase_limit).setLayoutParams(layoutParams);
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();
        findViewById(R.id.button_back).setVisibility(View.GONE);
    }

    public void getCash(View view) {
        ApiService service = new RestClient().getApiService();
        service.checkCashAdvance(getUserId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                try {
                    boolean appStage = responseObj.getAsJsonPrimitive("App_Stage").getAsBoolean();
                    if (appStage) {
                        startActivity(new Intent(MainActivity.this, GetCashActivity.class));
                    } else {
                        navigateToPendingRequestActivity();
                    }
                } catch (Exception e) {
                    Log.i("MainActivity", "Cash advance ID is null. " +
                            "Treat as \"false\" and prevent access.");
                    navigateToPendingRequestActivity();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void navigateToPendingRequestActivity() {
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
        closeFooter();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }
}
