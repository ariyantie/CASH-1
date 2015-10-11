package com.android.cash1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class PaymentConfirmationActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        setupActionBar();
        setupFooter();
    }

    public void submitPayment(View view) {
        Toast.makeText(this, "Thank you, your payment was successfully sent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
