package com.android.cash1.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;


public class LoginHelpActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_help);

        setupActionBar();
    }

    public void submit(View view) {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        switch (view.getId()) {
            case R.id.emailUsername:
                sharedPrefs.edit().putBoolean("email_username", true).apply();
                break;
            case R.id.emailPassword:
                sharedPrefs.edit().putBoolean("email_password", true).apply();
                break;
            case R.id.textUsername:
                sharedPrefs.edit().putBoolean("text_username", true).apply();
                break;
            case R.id.textPassword:
                sharedPrefs.edit().putBoolean("text_password", true).apply();
                break;
        }

        startActivity(new Intent(this, SecurityChallengeActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void goBack(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
