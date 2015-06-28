package com.android.cash1;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MakePaymentActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        setupActionBar();
        setupFooter();

        showAccountDetails();
    }

    private void showAccountDetails() {
        int userId = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt("user_id", 12345);

        ApiService service = new RestClient().getApiService();
        service.getAccountDetails(userId, new Callback<JsonObject>() {
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

    public void submitPayment(View view) {
        // TODO: Retrieve real bank ID from BankAccountInfo service (currently returns 404 error)
        int bankId = 1;

        String paymentFrom = ((EditText) findViewById(R.id.payment_from)).getText().toString().trim();
        String amount = ((EditText) findViewById(R.id.amount)).getText().toString().trim();
        String date = ((EditText) findViewById(R.id.date)).getText().toString().trim();

        ApiService service = new RestClient().getApiService();
        service.submitPayment(getUserId(), bankId, getStoreId(), paymentFrom, amount, date,
                false, null, false, null, false, null, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObject, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}