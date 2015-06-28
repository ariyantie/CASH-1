package com.android.cash1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.Cash1LocationsNumber;
import com.android.cash1.model.CollectionsPhoneNumbers;
import com.android.cash1.model.CustomerSupportPhoneNumbers;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ContactActivity extends Cash1Activity {

    private ViewGroup mFooterContainer;
    private TextView mFooterToggle;

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
        ApiService service = new RestClient().getApiService();

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
        ApiService service = new RestClient().getApiService();
        service.getAccountHeaderDetails(getUserId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                String accountName = getStringFromPrimitive(responseObj, "Accountname");
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
        Toast.makeText(this, "Already opened", Toast.LENGTH_SHORT).show();
        closeFooter();
    }

    public void dialNumber(View view) {
        String number = ((TextView) view).getText().toString();

        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }
}
