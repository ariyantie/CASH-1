package com.android.cash1.activities.support;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.DialogContents;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TermsActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ((TextView) findViewById(R.id.screen_header)).setText("Terms of use");

        setupActionBar();
        setupFooter();

        displayPrivacyPolicy();

        findViewById(R.id.confirm).setVisibility(View.GONE);
    }

    private void displayPrivacyPolicy() {
        Cash1ApiService service = new Cash1Client().getApiService();
        service.getDialogContents(17, "I", new Callback<DialogContents>() {
            @Override
            public void success(DialogContents contents, Response response) {
                findViewById(R.id.spinner).setVisibility(View.GONE);

                TextView headerTextView = (TextView) findViewById(R.id.header);
                headerTextView.setVisibility(View.VISIBLE);
                headerTextView.setText(contents.getTitle());

                TextView bodyTextView = (TextView) findViewById(R.id.body);
                bodyTextView.setVisibility(View.VISIBLE);
                bodyTextView.setText(contents.getBody()
                        .replace("https:// ", "https://")
                        .replace(" aspx", "aspx"));
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
