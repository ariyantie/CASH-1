package com.android.cash1.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class PaymentAmountActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_amount);

        setupActionBar();
        setupFooter();

        findViewById(R.id.focusable_container).requestFocus();
        final RadioGroup group = (RadioGroup) findViewById(R.id.radio_group);
        final EditText amountField = (EditText) findViewById(R.id.amount);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (findViewById(radioGroup.getCheckedRadioButtonId()) != null) {
                    amountField.setText("");
                    findViewById(R.id.focusable_container).requestFocus();
                }
            }
        });
    }

    public void select(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
