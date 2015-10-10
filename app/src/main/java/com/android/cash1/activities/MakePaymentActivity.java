package com.android.cash1.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.activities.login.LogoutActivity;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MakePaymentActivity extends Cash1Activity {

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        setupActionBar();
        setupFooter();

        showAccountDetails();

        EditText dateField = (EditText) findViewById(R.id.date);
        dateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(MakePaymentActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText dateField = (EditText) findViewById(R.id.date);
        dateField.setText(sdf.format(myCalendar.getTime()));
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
        // TODO: Implement make payment funcionallity
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        return;

        // TODO: Retrieve real bank ID from BankAccountInfo service (currently returns 404 error)
        /*
        int bankId = 1;

        String paymentFrom = ((EditText) findViewById(R.id.payment_from)).getText().toString().trim();
        String amount = ((EditText) findViewById(R.id.amount)).getText().toString().trim();
        String date = ((EditText) findViewById(R.id.date)).getText().toString().trim();

        Cash1ApiService service = new Cash1Client().getApiService();
        service.submitPayment(getUserId(), bankId, getStoreId(), paymentFrom, amount, date,
                false, null, false, null, false, null, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObject, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
        */
    }

    @Override
    public void logout(View view) {
        showConfirmLogoutPopup();
    }

    private void showConfirmLogoutPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog, null);
        builder.setView(rootView);

        rootView.findViewById(R.id.spinner).setVisibility(View.GONE);
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        titleView.setVisibility(View.VISIBLE);
        titleView.setText("Payment Has Not Made");
        TextView messageView = (TextView) rootView.findViewById(R.id.body);
        messageView.setVisibility(View.VISIBLE);
        messageView.setText("Are you sure you want to log out? Your current payment information will not be saved.");

        final AlertDialog dialog = builder.create();

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm);
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setText("Log Out");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakePaymentActivity.this, LogoutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setText("Return to Payment");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}