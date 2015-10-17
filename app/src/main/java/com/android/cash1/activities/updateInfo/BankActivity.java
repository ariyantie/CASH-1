package com.android.cash1.activities.updateInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class BankActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        setupActionBar();
        setupFooter();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_bank,
                R.id.number,
                new String[] {"Account number #...8173", "Account number #...3017", "Account number #...7503"});
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Intent intent = new Intent(BankActivity.this, BankEditActivity.class);
                intent.putExtra("pre-filled", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void logout(View view) {
        exitPopupUpdateInfo();
    }

    public void addCard(View view) {
        startActivity(new Intent(this, BankEditActivity.class));
    }
}
