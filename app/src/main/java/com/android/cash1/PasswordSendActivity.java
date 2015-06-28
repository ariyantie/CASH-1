package com.android.cash1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
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


        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPrefs.getString("username", "");
        if (username != null && !username.isEmpty()) {
            TextView usernameTextView = (TextView) findViewById(R.id.username);
            usernameTextView.setText("Username: " + username);
        }

        mPassEditText = (EditText) findViewById(R.id.password);
    }

    public void checkPassword(View view) {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        String email = sharedPrefs.getString("email", "");
        int userId = getUserId();
        String pass = mPassEditText.getText().toString();

        ApiService service = new RestClient().getApiService();
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
}
