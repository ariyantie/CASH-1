package com.android.cash1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.activities.updateInfo.CardActivity;
import com.android.cash1.activities.updateInfo.PaydaysActivity;
import com.android.cash1.activities.updateInfo.PersonalInfoActivity;
import com.android.cash1.activities.updateInfo.PrimaryAccountActivity;
import com.android.cash1.activities.updateInfo.WorkInfoActivity;
import com.android.cash1.model.Cash1Activity;

public class UpdateInfoActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        setupActionBar();
        setupFooter();
    }

    public void showComingSoonToast(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logout(View view) {
        exitPopupUpdateInfo();
    }

    public void personalInfo(View view) {
        startActivity(new Intent(this, PersonalInfoActivity.class));
    }

    public void workInfo(View view) {
        startActivity(new Intent(this, WorkInfoActivity.class));
    }

    public void primaryAccount(View view) {
        startActivity(new Intent(this, PrimaryAccountActivity.class));
    }

    public void paydays(View view) {
        startActivity(new Intent(this, PaydaysActivity.class));
    }

    public void card(View view) {
        startActivity(new Intent(this, CardActivity.class));
    }
}
