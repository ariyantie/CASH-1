package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.cash1.model.Cash1Activity;

public class SettingsActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // TODO: Add functionality for settings

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
}
