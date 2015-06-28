package com.android.cash1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.DialogContents;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GetCashNotifyActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cash_notify);

        setupActionBar();
        setupFooter();

        int messageId = getIntent().getIntExtra("message_id", -1);
        showScreenMessage(messageId);
    }

    private void showScreenMessage(int messageId) {
        ApiService service = new RestClient().getApiService();
        service.getDialogContents(messageId, "I", new Callback<DialogContents>() {
            @Override
            public void success(DialogContents contents, Response response) {
                findViewById(R.id.spinner).setVisibility(View.GONE);

                TextView headerTextView = (TextView) findViewById(R.id.header);
                headerTextView.setVisibility(View.VISIBLE);
                headerTextView.setText(contents.getTitle());

                TextView bodyTextView = (TextView) findViewById(R.id.body);
                bodyTextView.setVisibility(View.VISIBLE);
                bodyTextView.setText(contents.getBody().replace("Cash", "CASH"));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        int storeId = PreferenceManager.getDefaultSharedPreferences(this).getInt("store_id", 12345);
        service.getPhoneForStore(storeId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String phoneNumber;
                try {
                    try {
                        phoneNumber = String.valueOf(responseObj.getAsJsonPrimitive("Phone_No").getAsInt());
                    } catch (Exception e) {
                        phoneNumber = responseObj.getAsJsonPrimitive("Phone_No").getAsString();
                    }
                    TextView bodyTextView = (TextView) findViewById(R.id.body);
                    String currentMsg = bodyTextView.getText().toString();
                    if (currentMsg.endsWith("call")) {
                        currentMsg += " " + phoneNumber;
                        bodyTextView.setText(currentMsg);
                    }

                } catch (ClassCastException e) {
                    Log.i("MainActivity", "Phone number is null. " +
                            "Set the default one.");

                    TextView bodyTextView = (TextView) findViewById(R.id.body);
                    String currentMsg = bodyTextView.getText().toString();
                    if (currentMsg.endsWith("call")) {
                        currentMsg += " 775-321-3566";
                        bodyTextView.setText(currentMsg);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void callUs(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:775-321-3566"));
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int messageId = intent.getIntExtra("message_id", -1);
        showScreenMessage(messageId);
    }
}
