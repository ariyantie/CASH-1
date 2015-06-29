package com.android.cash1;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.DialogContents;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PrivacyPolicyActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setupActionBar();
        setupFooter();

        displayPrivacyPolicy();

        findViewById(R.id.confirm).setVisibility(View.GONE);
    }

    private void displayPrivacyPolicy() {
        ApiService service = new RestClient().getApiService();
        service.getDialogContents(16, "I", new Callback<DialogContents>() {
            @Override
            public void success(DialogContents contents, Response response) {
                findViewById(R.id.spinner).setVisibility(View.GONE);

                TextView headerTextView = (TextView) findViewById(R.id.header);
                headerTextView.setVisibility(View.VISIBLE);
                headerTextView.setText(contents.getTitle());

                TextView bodyTextView = (TextView) findViewById(R.id.body);
                bodyTextView.setVisibility(View.VISIBLE);
                bodyTextView.setText(contents.getBody()
                        .replace("Cash", "CASH 1").replace("CASH 1 1", "CASH 1")
                        .replace(",", ", ").replace(",  ", ", ")
                        .replace(".", ". ").replace(".  ", ". ")
                        .replace("www.", "").replace(". com", ".com"));
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
