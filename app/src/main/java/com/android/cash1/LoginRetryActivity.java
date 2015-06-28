package com.android.cash1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginRetryActivity extends Cash1Activity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_retry);

        setupActionBar();


        mUsernameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void login(View view) {
        login();
    }

    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (username.isEmpty()) {
            showError(mUsernameEditText);
            return;
        }
        if (password.isEmpty()) {
            showError(mPasswordEditText);
            return;
        }

        ApiService service = new RestClient().getApiService();
        service.login(username, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                int userId = responseObj.getAsJsonPrimitive("CustomerID").getAsInt();
                if (userId > 0) {
                    checkUserDevice();
                } else {
                    int attempts = mSharedPrefs.getInt("attempts_left", 2);
                    if (attempts > 0) {
                        attempts = attempts - 1;
                        mSharedPrefs.edit().putInt("attempts_left", attempts).apply();
                        showIncorrectUsernameOrPasswordPopup();
                        mPasswordEditText.setText("");
                        mPasswordEditText.requestFocus();
                    } else {
                        startActivity(new Intent(LoginRetryActivity.this, SplashActivity.class));
                        showAttemptsExceededPopup();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                Log.i("LoginRetryActivity", "Login retry request failed. Show main screen by default");
                startActivity(new Intent(LoginRetryActivity.this, MainActivity.class));
            }
        });
    }

    private void checkUserDevice() {
        final String deviceId = getDeviceId();
        final String username = mUsernameEditText.getText().toString();
        final int userId = getUserId();

        final ApiService service = new RestClient().getApiService();
        service.checkUserDevice(deviceId, username, userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                boolean isFound = responseObj.getAsJsonObject("CheckUserDeviceLinkResult").getAsJsonPrimitive("User_Device_Link").getAsBoolean();
                if (isFound) {
                    int loanId = mSharedPrefs.getInt("loan_id", -1);
                    if (loanId == 7) {
                        navigateToHomeScreen();
                    } else if (loanId == 5 || loanId == 6 || loanId == 9 || loanId == 10 || loanId == 37) {
                        boolean isFirstCashAdvance = mSharedPrefs.getBoolean("first_cash_advance", false);
                        if (isFirstCashAdvance) {
                            navigateToHomeScreen();
                        } else {
                            showLoanInProgressPopup();
                        }
                    } else {
                        showCreditDenialPopup();
                    }
                } else {
                    service.saveUserDevice(deviceId, username, userId, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonObject, Response response) {
                            login();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void loginHelp(View view) {
        startActivity(new Intent(this, LoginHelpActivity.class));
    }

    private void showError(EditText editText) {
        editText.requestFocus();
        editText.setError(editText.getHint() + " is required");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = mSharedPrefs.getString("username", "");
        String password = mSharedPrefs.getString("password", "");

        mUsernameEditText.setText(username);
        mPasswordEditText.requestFocus();
        if (!username.isEmpty() && mSharedPrefs.getInt("login_attempts", 2) < 2) {
            mPasswordEditText.setText(password);
            mPasswordEditText.setSelection(password.length());
        }
    }
}
