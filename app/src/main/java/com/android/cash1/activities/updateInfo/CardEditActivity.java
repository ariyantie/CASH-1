package com.android.cash1.activities.updateInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class CardEditActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        setupActionBar();
        setupFooter();

        if (getIntent().getBooleanExtra("pre-filled", false)) {
            ((TextView) findViewById(R.id.header)).setText("Edit debit/credit card");
            ((TextView) findViewById(R.id.name)).setText("Wellsfargo");
            ((TextView) findViewById(R.id.card_type)).setText("Debit");
            ((TextView) findViewById(R.id.card_number)).setText("1827 7912 1203 5084");
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
