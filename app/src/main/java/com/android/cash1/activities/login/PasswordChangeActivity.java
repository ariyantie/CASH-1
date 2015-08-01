package com.android.cash1.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class PasswordChangeActivity extends Cash1Activity {

    private EditText mPasswordEditText;
    private EditText mPasswordConfirmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        setupActionBar();


        mPasswordEditText = (EditText) findViewById(R.id.password);
        mPasswordConfirmEditText = (EditText) findViewById(R.id.password_confirm);
    }

    public void changePassword(View view) {
        int userId = getUserId();
        final String password = mPasswordEditText.getText().toString();
        String passwordConfirm = mPasswordConfirmEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            setError(mPasswordEditText);
            return;
        }
        if (passwordConfirm.isEmpty()) {
            setError(mPasswordConfirmEditText);
            return;
        }

        Cash1ApiService service = new Cash1Client().getApiService();
        service.changePassword(userId, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                if (!responseObj.getAsJsonPrimitive("message").getAsString().contains("Enter Valid")) {
                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(PasswordChangeActivity.this);
                    prefs.edit()
                            .putBoolean("password_restored", true)
                            .putString("password", password)
                            .apply();
                    if (prefs.getBoolean("question_set", false)) {
                        startActivity(new Intent(PasswordChangeActivity.this, CongratsActivity.class));
                    } else {
                        startActivity(new Intent(PasswordChangeActivity.this, SecurityQuestionActivity.class));
                    }
                } else {
                    Toast.makeText(PasswordChangeActivity.this,
                            "Failed to change password. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void setError(EditText editText) {
        editText.requestFocus();
        editText.setError(editText.getHint() + " is required");
    }
}
