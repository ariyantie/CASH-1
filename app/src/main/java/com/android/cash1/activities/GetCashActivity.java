package com.android.cash1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.activities.support.GetCashNotifyActivity;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GetCashActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cash);

        setupActionBar();
        setupFooter();

        showAccountDetails();
    }

    private void showAccountDetails() {
        Cash1ApiService service = new Cash1Client().getApiService();
        service.getAccountDetails(getUserId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String accountName = getStringFromPrimitive(responseObj, "Accountname");
                String accountNumber = getStringFromPrimitive(responseObj, "Accountnumber");
                String accountType = getStringFromPrimitive(responseObj, "AccountType");
                String outBalance = getStringFromPrimitive(responseObj, "OutstandingBalance");
                String creditAvailable = getStringFromPrimitive(responseObj, "CreditAvailable");
                String nextPaymentDue = getStringFromPrimitive(responseObj, "NextPaymentDueDate");
                String nextPaymentAmount = getStringFromPrimitive(responseObj, "NextPaymentAmount");

                if (!accountName.equals("null")) {
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                    outBalance = formatter.format(Float.parseFloat(outBalance));
                    creditAvailable = formatter.format(Float.parseFloat(creditAvailable));
                    nextPaymentAmount = formatter.format(Float.parseFloat(nextPaymentAmount));
                }

                ((TextView) findViewById(R.id.account_name)).setText(accountName);
                ((TextView) findViewById(R.id.account_number)).setText(accountNumber);
                ((TextView) findViewById(R.id.account_type)).setText(accountType);
                ((TextView) findViewById(R.id.out_balance)).setText(outBalance);
                ((TextView) findViewById(R.id.credit_available)).setText(creditAvailable);
                ((TextView) findViewById(R.id.next_payment_due)).setText(nextPaymentDue);
                ((TextView) findViewById(R.id.next_payment_amount)).setText(nextPaymentAmount);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void getCash(View view) {
        EditText amountEditText = (EditText) findViewById(R.id.amount_field);
        String amount = amountEditText.getText().toString();

        if (amount.isEmpty()) {
            Toast.makeText(this, "Amount can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Cash1ApiService service = new Cash1Client().getApiService();
        service.sendCashRequest(getUserId(), amount, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                Log.v("GetCashActivity", "Cash request successfully sent.");
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        service.saveRequestNote(getUserId(), "Cash Requested in the amount of $" + amount, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                Log.v("GetCashActivity", "Request note was successfully saved.");
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        navigateToNotifyActivity();
    }

    private void navigateToNotifyActivity() {
        Intent intent = new Intent(this, GetCashNotifyActivity.class);
        intent.putExtra("message_id", 20);
        startActivity(intent);
    }
}
