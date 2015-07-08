package com.android.cash1.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.R;
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
}
