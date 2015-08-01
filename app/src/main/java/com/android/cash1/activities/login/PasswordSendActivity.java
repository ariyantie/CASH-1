package com.android.cash1.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.InfoDialogFragment;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PasswordSendActivity extends Cash1Activity {

    private EditText mPassEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_send);

        setupActionBar();

        if (getIntent().hasExtra("isEmailUsername")) {
            showSuccessPopup(
                    getIntent().getBooleanExtra("isEmailUsername", false),
                    getIntent().getBooleanExtra("isEmailPassword", false),
                    getIntent().getBooleanExtra("isTextUsername", false),
                    getIntent().getBooleanExtra("isTextPassword", false)
            );
        }


        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPrefs.getString("username", "");
        if (!username.isEmpty()) {
            TextView usernameTextView = (TextView) findViewById(R.id.username);
            usernameTextView.setText("Username: " + username);
        }

        mPassEditText = (EditText) findViewById(R.id.password);
    }

    public void checkPassword(View view) {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        String email = sharedPrefs.getString("username", "");
        int userId = getUserId();
        String pass = mPassEditText.getText().toString();

        Cash1ApiService service = new Cash1Client().getApiService();
        service.checkTempPass(email, userId, pass, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                boolean passwordMatch = responseObj.getAsJsonObject("CheckCustomerTempPasswordResult").getAsJsonPrimitive("Temp_Password_Match").getAsBoolean();
                if (passwordMatch) {
                    startActivity(new Intent(PasswordSendActivity.this, PasswordChangeActivity.class));
                } else {
                    showMismatchPopup();
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void showSuccessPopup(boolean isEmailUsername, boolean isEmailPassword, boolean isTextUsername, boolean isTextPassword) {
        DialogFragment dialog = new InfoDialogFragment();
        Bundle args = new Bundle();

        if (isEmailUsername) {
            args.putInt("dialog_id", 9);
        } else if (isEmailPassword) {
            args.putInt("dialog_id", 11);
        } else if (isTextUsername) {
            args.putInt("dialog_id", 10);
        } else if (isTextPassword) {
            args.putInt("dialog_id", 12);
        }
        args.putString("message_type", "E");
        args.putString("btn_cancel_label", "Yes, I checked");
        dialog.setArguments(args);

        dialog.show(getSupportFragmentManager(), "dialog");
    }
}
