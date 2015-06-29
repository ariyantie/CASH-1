package com.android.cash1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.StoreDetails;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OfficeDetailActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_detail);

        setupActionBar();
        setupFooter();

        int storeId = getIntent().getIntExtra("store_id", -1);

        ApiService service = new RestClient().getApiService();
        service.getStoreDetails(storeId + "", new Callback<StoreDetails>() {
            @Override
            public void success(StoreDetails details, Response response) {
                TextView streetTextView = (TextView) findViewById(R.id.street);
                streetTextView.setText(details.getStreet());
                TextView addressTextView = (TextView) findViewById(R.id.address);
                addressTextView.setText(details.getAddress());
                TextView phoneTextView = (TextView) findViewById(R.id.phone);
                phoneTextView.setText(details.getPhoneNumber());
                TextView faxTextView = (TextView) findViewById(R.id.fax);
                faxTextView.setText(details.getFaxNumber());
                ImageView iconImageView = (ImageView) findViewById(R.id.icon);
                Picasso.with(OfficeDetailActivity.this)
                        .load(details.getImageUrl())
                        .fit()
                        .into(iconImageView);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
