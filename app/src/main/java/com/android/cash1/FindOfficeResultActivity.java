package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.Office;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FindOfficeResultActivity extends Cash1Activity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private String mWhereToSearch;
    private String mZipCodeString;
    private String mAddress;
    private String mCity;
    private String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_office_result);

        setupActionBar();
        setupFooter();

        mZipCodeString = getIntent().getStringExtra("zipcode_string");
        mAddress = getIntent().getStringExtra("address");
        mCity = getIntent().getStringExtra("city");
        mState = getIntent().getStringExtra("state");

        mWhereToSearch = getIntent().getStringExtra("where_to_search");
        listAllStores();
    }

    private void listAllStores() {
        ApiService service = new RestClient().getApiService();
        service.listAllStores(new Callback<List<Office>>() {
            @Override
            public void success(List<Office> officeList, Response response) {
                findViewById(R.id.loading).setVisibility(View.GONE);

                List<Office> filteredList = null;
                switch (mWhereToSearch) {
                    case "currentlocation":
                        filteredList = selectOnlyNearest(officeList);
                        break;
                    case "zipcode":
                        filteredList = selectOnlyWithZipCode(officeList, mZipCodeString);
                        break;
                    case "cityaddressstate":
                        filteredList = displayOnlyWithAddressCityAndState(officeList, mAddress, mCity, mState);
                        break;
                }

                if (filteredList == null || filteredList.size() == 0) {
                    setResult(RESULT_CANCELED);
                    finish();
                    return;
                }

                LinearLayout container = (LinearLayout) findViewById(R.id.list_container);
                for (int i = 0; i < filteredList.size(); i++) {
                    final Office office = filteredList.get(i);

                    FrameLayout listItemContainer = (FrameLayout) View.inflate(
                            FindOfficeResultActivity.this, R.layout.store_list_item, null);

                    TextView positionTextView = (TextView) listItemContainer.findViewById(R.id.position);
                    positionTextView.setText((i + 1) + "");

                    TextView addressTextView = (TextView) listItemContainer.findViewById(R.id.address);
                    addressTextView.setText(office.getStreet());
                    TextView cityTextView = (TextView) listItemContainer.findViewById(R.id.city);
                    cityTextView.setText(office.getAddress());

                    listItemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FindOfficeResultActivity.this, OfficeDetailActivity.class);
                            intent.putExtra("store_id", office.getId());
                            intent.putExtra("latitude", office.getLatitude());
                            intent.putExtra("longitude", office.getLongitude());
                            startActivity(intent);
                        }
                    });

                    container.addView(listItemContainer);
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private List<Office> displayOnlyWithAddressCityAndState(List<Office> officeList, String address, String city, String state) {
        return officeList;
    }

    private List<Office> selectOnlyNearest(List<Office> officeList) {
        return officeList;
    }

    private List<Office> selectOnlyWithZipCode(List<Office> officeList, String zipCodeQueryString) {
        List<Office> filteredList = new ArrayList<>();
        for (Office office : officeList) {
            if (office.getZipCodeString().contains(zipCodeQueryString)) {
                filteredList.add(office);
            }
        }
        return filteredList;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     *
     * @param officeList
     */
    private void setUpMapIfNeeded(List<Office> officeList) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(officeList);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(List<Office> officeList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Office office : officeList) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(
                    office.getLatitude(), office.getLongitude())).title(office.getAddress()));

            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.moveCamera(cu);
    }
}
