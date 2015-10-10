package com.android.cash1.activities;

import android.os.Bundle;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class PaymentAccountActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_account);

        setupActionBar();
        setupFooter();
    }

}
