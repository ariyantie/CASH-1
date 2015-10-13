package com.android.cash1.activities.updateInfo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PaydaysActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paydays);

        setupActionBar();
        setupFooter();

        showAccountDetails();
    }

    private void showAccountDetails() {
        int userId = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt("user_id", 12345);

        Cash1ApiService service = new Cash1Client().getApiService();
        service.getAccountDetails(userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String accountName = getStringFromPrimitive(responseObj, "Accountname");
                String accountNumber = getStringFromPrimitive(responseObj, "Accountnumber");
                String accountType = getStringFromPrimitive(responseObj, "AccountType");

                ((TextView) findViewById(R.id.account_name)).setText(accountName);
                ((TextView) findViewById(R.id.account_number)).setText(accountNumber);
                ((TextView) findViewById(R.id.account_type)).setText(accountType);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
