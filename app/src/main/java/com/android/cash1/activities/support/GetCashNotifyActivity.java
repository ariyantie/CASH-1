package com.android.cash1.activities.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.DialogContents;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GetCashNotifyActivity extends Cash1Activity {

    private String mPhoneNumber;

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
                bodyTextView.setText(
                        contents.getBody()
                                .replace("Cash", "CASH")
                                .replace(" at (877) 854-2241.", ""));

                displayPhoneNumberForCurrentStore();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void displayPhoneNumberForCurrentStore() {
        ApiService service = new RestClient().getApiService();
        service.getPhoneForStore(getStoreId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                try {
                    try {
                        mPhoneNumber = String.valueOf(responseObj.getAsJsonPrimitive("Phone_No").getAsInt());
                    } catch (Exception e) {
                        mPhoneNumber = responseObj.getAsJsonPrimitive("Phone_No").getAsString();
                    }
                    mPhoneNumber = formatNumber(mPhoneNumber);

                    TextView bodyTextView = (TextView) findViewById(R.id.body);
                    String bodyMessage = bodyTextView.getText().toString();
                    bodyMessage += " at " + mPhoneNumber;
                    bodyTextView.setText(bodyMessage);

                } catch (ClassCastException e) {
                    Log.i("MainActivity", "Phone number is null. " +
                            "Leave the default one.");
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void callUs(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
            intent.setData(Uri.parse("tel:775-321-3566"));
        } else {
            intent.setData(Uri.parse("tel:" + mPhoneNumber));
        }
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int messageId = intent.getIntExtra("message_id", -1);
        showScreenMessage(messageId);
    }
}
