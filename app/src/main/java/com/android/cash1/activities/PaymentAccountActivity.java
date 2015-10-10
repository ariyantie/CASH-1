package com.android.cash1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class PaymentAccountActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_account);

        setupActionBar();
        setupFooter();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.account_list_item,
                R.id.title,
                new String[] {"Account name: Wellsfargo", "Account name: Wellsfargo", "Account name: Wellsfargo", "Account name: Wellsfargo"});
        ListView accountListView = (ListView) findViewById(R.id.account_list_view);
        accountListView.setAdapter(adapter);
    }

    public void addAccount(View view) {
        startActivity(new Intent(this, NewAccountActivity.class));
    }
}
