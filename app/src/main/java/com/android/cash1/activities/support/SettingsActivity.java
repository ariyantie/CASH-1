package com.android.cash1.activities.support;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ToggleButton rememberToggle = (ToggleButton) findViewById(R.id.toggle_remember);
        rememberToggle.setChecked(rememberMe());
        rememberToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                sharedPreferences.edit().putBoolean("remember", isChecked).apply();
            }
        });

        ToggleButton locationToggle = (ToggleButton) findViewById(R.id.location_toggle);
        locationToggle.setChecked(useCurrentLocation());
        locationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                sharedPreferences.edit().putBoolean("use_location", isChecked).apply();

                submitUpdatedPreferences();
            }
        });

        setupActionBar();
        setupFooter();
    }

    private void submitUpdatedPreferences() {
        ApiService service = new RestClient().getApiService();
        service.setPreferences(getDeviceId(), getUserEmail(), getUserId(), null, useCurrentLocation(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObject, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    public void openSettings(View view) {
        closeFooter();
    }

    public void navigateToNotificationSettingsActivity(View view) {
        startActivity(new Intent(this, NotificationSettingsActivity.class));
    }

    public void showNotificationsGuide(View view) {
        showDialog(13, "I");
    }

    public void showLoginSecurityGuide(View view) {
        showDialog(14, "I");
    }

    public void showLocationServicesGuide(View view) {
        showDialog(15, "I");
    }
}
