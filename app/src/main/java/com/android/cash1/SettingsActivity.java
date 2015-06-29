package com.android.cash1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.cash1.model.Cash1Activity;

public class SettingsActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // TODO: Add functionality for settings

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

        setupActionBar();
        setupFooter();
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
