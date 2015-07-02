package com.android.cash1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.OfficeDetails;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OfficeDetailActivity extends Cash1Activity {

    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_detail);

        setupActionBar();
        setupFooter();

        int storeId = getIntent().getIntExtra("store_id", -1);
        mLatitude = getIntent().getDoubleExtra("latitude", -1);
        mLongitude = getIntent().getDoubleExtra("longitude", -1);

        ApiService service = new RestClient().getApiService();
        service.getStoreDetails(storeId + "", new Callback<OfficeDetails>() {
            @Override
            public void success(OfficeDetails details, Response response) {
                TextView streetTextView = (TextView) findViewById(R.id.street);
                streetTextView.setText(details.getStreet());
                TextView addressTextView = (TextView) findViewById(R.id.address);
                addressTextView.setText(details.getAddress().replace("LAS VEGAS", "LAS\u00A0VEGAS"));
                TextView phoneTextView = (TextView) findViewById(R.id.phone);
                phoneTextView.setText(details.getPhoneNumber());
                TextView faxTextView = (TextView) findViewById(R.id.fax);
                faxTextView.setText(details.getFaxNumber());
                ImageView iconImageView = (ImageView) findViewById(R.id.icon);
                Picasso.with(OfficeDetailActivity.this)
                        .load(details.getImageUrl())
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.ic_image_error)
                        .into(iconImageView);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void showDirectionsOnMap(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + mLatitude + "," + mLongitude + "&mode=driving"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Install Google Maps to see the map", Toast.LENGTH_LONG).show();
        }
    }
}
