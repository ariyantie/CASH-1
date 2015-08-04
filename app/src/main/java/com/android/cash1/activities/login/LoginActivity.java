package com.android.cash1.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.cash1.R;
import com.android.cash1.activities.MainActivity;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Cash1Activity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (getIntent().hasExtra("registerMode")) {
            findViewById(R.id.register).setVisibility(View.VISIBLE);
        }

        ToggleButton rememberToggle = (ToggleButton) findViewById(R.id.toggle_remember);
        rememberToggle.setChecked(rememberMe());

        if (rememberMe()) {
            login();
        }

        mUsernameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mPasswordEditText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

        String username = mSharedPrefs.getString("username", "");
        String password = mSharedPrefs.getString("password", "");
        if (!username.isEmpty()) {
            mUsernameEditText.setText(username);
        }
        if (!password.isEmpty()) {
            mPasswordEditText.setText(password);
        }
    }

    public void login(View view) {
        login();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.loading).setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPrefs.edit()
                .putString("username", mUsernameEditText.getText().toString())
                .apply();
    }

    private void login() {

        String username;
        String password;

        if (!mSharedPrefs.getBoolean("remember", false)) {
            ToggleButton rememberToggle = (ToggleButton) findViewById(R.id.toggle_remember);
            mSharedPrefs.edit().putBoolean("remember", rememberToggle.isChecked()).apply();

            username = mUsernameEditText.getText().toString().trim();
            password = mPasswordEditText.getText().toString().trim();

            if (username.isEmpty()) {
                showError(mUsernameEditText);
                return;
            }
            if (password.isEmpty()) {
                showError(mPasswordEditText);
                return;
            }
            findViewById(R.id.loading).setVisibility(View.VISIBLE);

            mSharedPrefs.edit()
                    .putString("username", username)
                    .putString("password", password).apply();
        } else {
            username = mSharedPrefs.getString("username", "");
            password = mSharedPrefs.getString("password", "");
        }

        Cash1ApiService service = new Cash1Client().getApiService();
        service.login(username, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                int userId = responseObj.getAsJsonPrimitive("CustomerID").getAsInt();
                if (userId > 0) {
                    int loanId = responseObj.getAsJsonPrimitive("LoaninfoCompleted").getAsInt();
                    boolean isFirstCashAdvance = responseObj.getAsJsonPrimitive("isFirstCashAdvance").getAsBoolean();
                    int storeId = responseObj.getAsJsonPrimitive("StoreID").getAsInt();
                    String redirectView = responseObj.getAsJsonPrimitive("RedirectView").getAsString();
                    mSharedPrefs.edit()
                            .putInt("loan_id", loanId)
                            .putBoolean("first_cash_advance", isFirstCashAdvance)
                            .putInt("user_id", userId)
                            .putInt("store_id", storeId)
                            .putString("redirect_view", redirectView)
                            .apply();

                    if (redirectView.equals("LoginFailed")) {
                        startActivity(new Intent(LoginActivity.this, LoginRetryActivity.class));
                        return;
                    } else if (redirectView.equals("UserNotFound")) {
                        showBasicLoginErrorDialog();
                        return;
                    }

                    boolean isEsign = responseObj.getAsJsonPrimitive("IsEsign").getAsBoolean();
                    boolean isQuestionSet = responseObj.getAsJsonPrimitive("IsSecurity").getAsBoolean();

                    mSharedPrefs.edit()
                            .putBoolean("agreement_confirmed", isEsign)
                            .putBoolean("question_set", isQuestionSet)
                            .apply();

                    if (!isEsign) {
                        Toast.makeText(LoginActivity.this, "Review and accept this document to continue", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AgreementActivity.class));
                        return;
                    }
                    if (!isQuestionSet) {
                        Toast.makeText(LoginActivity.this, "Set up security question to continue", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, SecurityQuestionActivity.class));
                        mSharedPrefs.edit().putBoolean("return_to_login", true).apply();
                        return;
                    }
                    checkUserDevice();
                } else {
                    String redirectView = responseObj.getAsJsonPrimitive("RedirectView").getAsString();
                    mSharedPrefs.edit().putString("redirect_view", redirectView).apply();
                    if (redirectView.equals("LoginFailed")) {
                        startActivity(new Intent(LoginActivity.this, LoginRetryActivity.class));
                    } else if (redirectView.equals("UserNotFound")) {
                        showBasicLoginErrorDialog();
                    }
                }
            }

            @Override
            public void failure(RetrofitError e) {
                e.printStackTrace();

                Toast.makeText(LoginActivity.this, "Failed to connect to a network. " +
                        "Please try to login again.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, LoginRetryActivity.class));
            }
        });
    }

    private void checkUserDevice() {
        final String deviceId = getDeviceId();
        final String username = mUsernameEditText.getText().toString();
        final int userId = getUserId();

        final Cash1ApiService service = new Cash1Client().getApiService();
        service.checkUserDevice(deviceId, username, userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                boolean isFound = responseObj.getAsJsonObject("CheckUserDeviceLinkResult").getAsJsonPrimitive("User_Device_Link").getAsBoolean();
                if (isFound) {
                    int loanId = mSharedPrefs.getInt("loan_id", -1);
                    if (loanId == 7) {
                        navigateToMainActivity();
                    } else if (loanId == 5 || loanId == 6 || loanId == 9 || loanId == 10 || loanId == 37) {
                        boolean isFirstCashAdvance = mSharedPrefs.getBoolean("first_cash_advance", false);
                        if (isFirstCashAdvance) {
                            navigateToMainActivity();
                        } else {
                            showLoanInProgressDialog();
                        }
                    } else {
                        String redirectViewTitle = getRedirectViewTitle();
                        if (!redirectViewTitle.isEmpty()) {
                            switch (getRedirectViewTitle()) {
                                case "forgotpassword":
                                    SharedPreferences sharedPreferences =
                                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    if (!sharedPreferences.getBoolean("password_restored", false)) {
                                        Toast.makeText(LoginActivity.this, "You need to setup temporary password to continue", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(LoginActivity.this, PasswordChangeActivity.class));
                                    } else if (mSharedPrefs.getBoolean("agreement_confirmed", false)
                                            && mSharedPrefs.getBoolean("question_set", false)) {
                                        showCreditDenialPopup();
                                    } else {
                                        showBasicLoginErrorDialog();
                                    }
                                    break;
                                case "CreditDenial":
                                case "Credit Denial":
                                    showCreditDenialPopup();
                                    break;
                                default:
                                    showBasicLoginErrorDialog();
                                    break;
                            }
                        } else {
                            showBasicLoginErrorDialog();
                        }
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
            public void failure(RetrofitError e) {
                e.printStackTrace();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void navigateToRegisterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void showError(EditText editText) {
        editText.requestFocus();
        editText.setError(editText.getHint() + " is required");
    }

    public void helpLogin(View view) {
        startActivity(new Intent(this, LoginHelpActivity.class));
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        finish();
    }
}