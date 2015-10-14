package com.android.cash1.activities.updateInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class EditCardActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        setupActionBar();
        setupFooter();
    }

    public void submit(View view) {
        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
        navigateToMainActivity();
    }

    @Override
    public void logout(View view) {
        exitPopupUpdateInfo();
    }
}
