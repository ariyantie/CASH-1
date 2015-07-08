package com.android.cash1.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.DialogContents;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class IncreaseLimitActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase_limit);

        setupActionBar();
        setupFooter();

        showAccountHeaderDetails();
        showScreenMessage();
    }

    private void showAccountHeaderDetails() {
        int userId = getUserId();

        ApiService service = new RestClient().getApiService();
        service.getAccountHeaderDetails(userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String accountName = getStringFromPrimitive(responseObj, "Accountname");
                String accountNumber = getStringFromPrimitive(responseObj, "Accountnumber");
                String accountType = getStringFromPrimitive(responseObj, "AccountType");

                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                String creditLimit = formatter.format(Float.parseFloat(
                        getStringFromPrimitive(responseObj, "Creditlimit")));
                String creditAvailable = formatter.format(Float.parseFloat(
                        getStringFromPrimitive(responseObj, "CreditAvailable")));

                ((TextView) findViewById(R.id.account_name)).setText(accountName);
                ((TextView) findViewById(R.id.account_number)).setText(accountNumber);
                ((TextView) findViewById(R.id.account_type)).setText(accountType);
                ((TextView) findViewById(R.id.increase_limit)).setText(creditLimit);
                ((TextView) findViewById(R.id.available_credit)).setText(creditAvailable);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void showScreenMessage() {
        ApiService service = new RestClient().getApiService();
        service.getDialogContents(21, "I", new Callback<DialogContents>() {
            @Override
            public void success(DialogContents contents, Response response) {
                findViewById(R.id.spinner).setVisibility(View.GONE);

                TextView headerTextView = (TextView) findViewById(R.id.header);
                headerTextView.setVisibility(View.VISIBLE);
                headerTextView.setText(contents.getTitle());

                TextView bodyTextView = (TextView) findViewById(R.id.body);
                bodyTextView.setVisibility(View.VISIBLE);
                bodyTextView.setText(contents.getBody());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        int storeId = PreferenceManager.getDefaultSharedPreferences(this).getInt("store_id", 12345);
        service.getPhoneForStore(storeId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String phoneNumber;
                try {
                    try {
                        phoneNumber = String.valueOf(responseObj.getAsJsonPrimitive("Phone_No").getAsInt());
                    } catch (Exception e) {
                        phoneNumber = responseObj.getAsJsonPrimitive("Phone_No").getAsString();
                    }
                    TextView bodyTextView = (TextView) findViewById(R.id.body);
                    String currentMsg = bodyTextView.getText().toString();
                    if (currentMsg.endsWith("number ")) {
                        currentMsg += phoneNumber;
                        bodyTextView.setText(currentMsg);
                    }

                } catch (ClassCastException e) {
                    Log.i("MainActivity", "Phone number is null. " +
                            "Set the default one.");

                    TextView bodyTextView = (TextView) findViewById(R.id.body);
                    String currentMsg = bodyTextView.getText().toString();
                    if (currentMsg.endsWith("number ")) {
                        currentMsg += "775-321-3566";
                        bodyTextView.setText(currentMsg);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void callUs(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:775-321-3566"));
        startActivity(intent);
    }
}
