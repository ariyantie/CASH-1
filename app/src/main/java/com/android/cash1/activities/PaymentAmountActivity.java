package com.android.cash1.activities;

import android.os.Bundle;
import android.view.View;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class PaymentAmountActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_amount);

        setupActionBar();
        setupFooter();
    }

    public void select(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
