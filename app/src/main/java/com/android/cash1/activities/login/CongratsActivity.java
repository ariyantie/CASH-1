package com.android.cash1.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.activities.MainActivity;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.DialogContents;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CongratsActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_layout,
                null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);


        ApiService service = new RestClient().getApiService();
        service.getDialogContents(25, "I", new Callback<DialogContents>() {
            @Override
            public void success(DialogContents contents, Response response) {
                findViewById(R.id.spinner).setVisibility(View.GONE);

                TextView headerTextView = (TextView) findViewById(R.id.header);
                headerTextView.setVisibility(View.VISIBLE);
                headerTextView.setText(contents.getTitle());

                TextView bodyTextView = (TextView) findViewById(R.id.body);
                bodyTextView.setVisibility(View.VISIBLE);
                bodyTextView.setText(contents.getBody());

                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(CongratsActivity.this);
                TextView usernameTextView = (TextView) findViewById(R.id.username);
                usernameTextView.setText("Username: " + sharedPrefs.getString("username", ""));
                TextView passwordTextView = (TextView) findViewById(R.id.password);
                passwordTextView.setText("Password: " + sharedPrefs.getString("password", ""));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void navigateToMainMenu(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
