package com.android.cash1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.activities.updateInfo.CardEditActivity;
import com.android.cash1.model.Cash1Activity;

public class NewAccountActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        setupActionBar();
        setupFooter();
    }

    public void comingSoon(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }

    public void addCard(View view) {
        startActivity(new Intent(this, CardEditActivity.class));
    }
}
