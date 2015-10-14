package com.android.cash1.activities.updateInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;

public class CardActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        setupActionBar();
        setupFooter();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_card,
                R.id.title,
                new String[] {"Debit/credit card: Wellsfargo", "Debit/credit card: Wellsfargo", "Debit/credit card: Wellsfargo"});
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                startActivity(new Intent(CardActivity.this, EditCardActivity.class));
            }
        });
    }

    @Override
    public void logout(View view) {
        exitPopupUpdateInfo();
    }
}
