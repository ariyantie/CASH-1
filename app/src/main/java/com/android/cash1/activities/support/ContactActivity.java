package com.android.cash1.activities.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.Cash1LocationsNumber;
import com.android.cash1.model.CollectionsPhoneNumbers;
import com.android.cash1.model.CustomerSupportPhoneNumbers;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ContactActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setupActionBar();
        setupFooter();

        displayAccountName();
        displayPhoneNumbers();
    }

    private void displayPhoneNumbers() {
        Cash1ApiService service = new Cash1Client().getApiService();

        service.getCustomerSupportPhoneNumbers(new Callback<CustomerSupportPhoneNumbers>() {
            @Override
            public void success(CustomerSupportPhoneNumbers phoneNumbersObject, Response response) {
                ((TextView) findViewById(R.id.customer_toll_free)).setText(
                        phoneNumbersObject.getTollFreePhoneNumber());
                ((TextView) findViewById(R.id.customer_phone)).setText(
                        phoneNumbersObject.getPhoneNumber());
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
        service.getCollectionsPhoneNumbers(new Callback<CollectionsPhoneNumbers>() {
            @Override
            public void success(CollectionsPhoneNumbers phoneNumbersObject, Response response) {
                ((TextView) findViewById(R.id.collections_toll_free)).setText(
                        phoneNumbersObject.getTollFreePhoneNumber());
                ((TextView) findViewById(R.id.collections_fax)).setText(
                        phoneNumbersObject.getFaxNumber());
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
        service.getCash1LocationsPhoneNumber(new Callback<Cash1LocationsNumber>() {
            @Override
            public void success(Cash1LocationsNumber phoneNumberObject, Response response) {
                ((TextView) findViewById(R.id.nearest_location_number)).setText(
                        phoneNumberObject.getNearestLocationNumber());
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void displayAccountName() {
        Cash1ApiService service = new Cash1Client().getApiService();
        service.getAccountHeaderDetails(getUserId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String accountName = getStringFromPrimitive(responseObj, "Accountname");
                if (accountName.equals("null")) {
                    accountName = getString(R.string.null_value_placeholder);
                }
                ((TextView) findViewById(R.id.account_name)).setText(accountName);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void contactUs(View view) {
        closeFooter();
    }

    public void dialNumber(View view) {
        String number = ((TextView) view).getText().toString();

        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }
}
