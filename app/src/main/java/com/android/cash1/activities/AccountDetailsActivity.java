package com.android.cash1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.activities.support.AccountStatementActivity;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AccountDetailsActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        setupActionBar();
        setupFooter();

        showAccountDetails();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/LL/yyyy");
        TextView dateTextView = (TextView) findViewById(R.id.date);
        dateTextView.setText("Updated as of " +
                dateFormat.format(new Date()));
    }

    private void showAccountDetails() {
        Cash1ApiService service = new Cash1Client().getApiService();
        service.getAccountDetails(getUserId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String accountName = getStringFromPrimitive(responseObj, "Accountname");
                String accountNumber = getStringFromPrimitive(responseObj, "Accountnumber");
                String accountType = getStringFromPrimitive(responseObj, "AccountType");
                String nextPaymentDue = getStringFromPrimitive(responseObj, "NextPaymentDueDate");

                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                String outBalance = formatter.format(Float.parseFloat(
                        getStringFromPrimitive(responseObj, "OutstandingBalance")));
                String creditAvailable = formatter.format(Float.parseFloat(
                        getStringFromPrimitive(responseObj, "CreditAvailable")));
                String nextPaymentAmount = formatter.format(Float.parseFloat(
                        getStringFromPrimitive(responseObj, "NextPaymentAmount")));

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

    public void showStatement(View view) {
        startActivity(new Intent(this, AccountStatementActivity.class));
    }
}
