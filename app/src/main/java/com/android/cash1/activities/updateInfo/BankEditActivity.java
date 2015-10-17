package com.android.cash1.activities.updateInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class BankEditActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank);

        setupActionBar();
        setupFooter();

        if (getIntent().getBooleanExtra("pre-filled", false)) {
            ((TextView) findViewById(R.id.header)).setText("Edit bank account");
            ((TextView) findViewById(R.id.name)).setText("Wellsfargo");
            ((TextView) findViewById(R.id.bank_name)).setText("Bank of America");
            ((TextView) findViewById(R.id.account_number)).setText("4695");
            ((TextView) findViewById(R.id.account_type)).setText("Checking");
        }
    }

    public void submit(View view) {
        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
        navigateToMainActivity();
    }

    @Override
    public void logout(View view) {
        exitPopupUpdateInfo();
    }

    public void cvvPopup(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }
}
