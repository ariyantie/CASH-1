package com.android.cash1.activities.updateInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class PrimaryAccountActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_account);

        setupActionBar();
        setupFooter();
    }

    @Override
    public void logout(View view) {
        exitPopupUpdateInfo();
    }

    public void submit(View view) {
        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
        navigateToMainActivity();
    }

    public void comingSoon(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }
}
