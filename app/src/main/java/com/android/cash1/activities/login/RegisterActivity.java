package com.android.cash1.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RegisterActivity extends Cash1Activity {

    private String LOG_TAG = RegisterActivity.class.getSimpleName();

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mEmailEditText;

    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupActionBar();


        mFirstNameEditText = (EditText) findViewById(R.id.first_name);
        mLastNameEditText = (EditText) findViewById(R.id.last_name);
        mEmailEditText = (EditText) findViewById(R.id.email);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void register(View view) {
        String firstName = mFirstNameEditText.getText().toString();
        if (firstName.isEmpty()) {
            showError(mFirstNameEditText);
            return;
        }
        String lastName = mLastNameEditText.getText().toString();
        if (lastName.isEmpty()) {
            showError(mLastNameEditText);
            return;
        }
        String email = mEmailEditText.getText().toString();
        if (email.isEmpty()) {
            showError(mEmailEditText);
            return;
        }
        if (!isValidEmail(email)) {
            mEmailEditText.requestFocus();
            mEmailEditText.setError("Email format is invalid");
            return;
        }

        Cash1ApiService service = new Cash1Client().getApiService();
        service.checkEmailReg(email, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                JsonObject regObj = responseObj.getAsJsonObject("CheckCustomerforRegistrationResult");
                boolean isFound = regObj.getAsJsonPrimitive("Customer_Found").getAsBoolean();
                boolean isRegistered = regObj.getAsJsonPrimitive("Customer_Registered").getAsBoolean();

                if (!isFound && !isRegistered) {
                    showCallUsPopup();
                } else if (isFound && !isRegistered) {
                    try {
                        String username = regObj.getAsJsonPrimitive("UserName").getAsString();
                        mSharedPrefs.edit().putString("username", username).apply();
                    } catch (ClassCastException e) {
                        Log.w(LOG_TAG, "Username is null. Do not show related section in PasswordSendActivity.");
                    }
                    navigateToTempPassScreen();
                } else if (isFound && isRegistered) {
                    navigateToLoginScreen();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void navigateToTempPassScreen() {
        setSupportProgressBarIndeterminateVisibility(true);

        String email = mEmailEditText.getText().toString();

        Cash1ApiService service = new Cash1Client().getApiService();
        service.sendTempPass(email, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                setSupportProgressBarIndeterminateVisibility(false);

                boolean isPasswordSent = responseObj.getAsJsonObject("SendCustomerTempPasswordResult").getAsJsonPrimitive("PasswordRecieved").getAsBoolean();
                if (isPasswordSent) {
                    startActivity(new Intent(RegisterActivity.this, PasswordSendActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "We could not find your email in our system. " +
                                    "Please try to register again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(false);
            }
        });
    }

    private void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("registerMode", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateToLoginScreen();
    }

    @Override
    public void goBack(View view) {
        navigateToLoginScreen();
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showError(EditText editText) {
        editText.requestFocus();
        editText.setError(editText.getHint() + " is required");
    }

    @Override
    protected void onPause() {
        mSharedPrefs.edit()
                .putString("first_name", mFirstNameEditText.getText().toString())
                .putString("last_name", mLastNameEditText.getText().toString())
                .putString("email", mEmailEditText.getText().toString())
                .putString("username", mEmailEditText.getText().toString())
                .apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String firstName = mSharedPrefs.getString("first_name", "");
        mFirstNameEditText.setText(firstName);
        mFirstNameEditText.setSelection(firstName.length());

        String lastName = mSharedPrefs.getString("last_name", "");
        mLastNameEditText.setText(lastName);
        mLastNameEditText.setSelection(lastName.length());

        String email = mSharedPrefs.getString("username", "");
        mEmailEditText.setText(email);
        mEmailEditText.setSelection(email.length());
    }
}
