package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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

    public static final int RESULT_NOT_FOUND = -101;
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
        if (mCity != null) {
            mCity = mCity.toUpperCase();
        }
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

                TextView queryTextView = (TextView) findViewById(R.id.query_textview);
                List<Office> filteredList = null;

                switch (mWhereToSearch) {
                    case "currentlocation":
                        filteredList = selectOnlyNearest(officeList);
                        queryTextView.setText("Current location" + "\n");
                        break;
                    case "zipcode":
                        filteredList = selectOnlyWithZipCode(officeList, mZipCodeString);
                        queryTextView.setText("ZIP code “" + mZipCodeString + "”" + "\n");
                        break;
                    case "cityaddressstate":
                        filteredList = displayOnlyWithAddressCityAndState(officeList, mAddress, mCity, mState);
                        queryTextView.setText(mAddress + ", " + mCity + ", " + mState + "\n");
                        break;
                }

                if (filteredList == null || filteredList.size() == 0) {
                    setResult(RESULT_NOT_FOUND);
                    finish();
                    return;
                }

                setUpMapIfNeeded(filteredList);

                LinearLayout container = (LinearLayout) findViewById(R.id.list_container);
                for (int i = 0; i < filteredList.size(); i++) {
                    final Office office = filteredList.get(i);

                    FrameLayout listItemContainer = (FrameLayout) View.inflate(
                            FindOfficeResultActivity.this, R.layout.store_list_item, null);

                    TextView positionTextView = (TextView) listItemContainer.findViewById(R.id.position);
                    positionTextView.setText((i + 1) + "");

                    TextView streetTextView = (TextView) listItemContainer.findViewById(R.id.street);
                    Spanned street = highlightMatches(office.getStreet());
                    streetTextView.setText(street, TextView.BufferType.SPANNABLE);

                    TextView addressTextView = (TextView) listItemContainer.findViewById(R.id.address);
                    Spanned address = highlightMatches(office.getAddress());
                    addressTextView.setText(address, TextView.BufferType.SPANNABLE);

                    if (i + 1 == filteredList.size()) {
                        listItemContainer.findViewById(R.id.divider).setVisibility(View.GONE);
                    }

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

    private Spanned highlightMatches(String string) {
        if (mZipCodeString != null) {
            string = string.replaceAll("(?i)" + mZipCodeString, "<font color='red'>" + mZipCodeString + "</font>");
        }
        if (mAddress != null && mCity != null && mState != null) {
            string = string.replaceAll("(?i)" + mAddress, "<font color='red'>" + mAddress + "</font>");
            string = string.replaceAll("(?i)" + mCity, "<font color='red'>" + mCity + "</font>");
            string = string.replaceAll("(?i)" + mState, "<font color='red'>" + mState + "</font>");
        }
        return Html.fromHtml(string);
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

    private List<Office> displayOnlyWithAddressCityAndState(List<Office> officeList, String address, String city, String state) {
        List<Office> filteredList = new ArrayList<>();
        for (Office office : officeList) {
            if (office.getAddress().contains(mAddress) ||
                    office.getAddress().contains(mCity) ||
                    office.getAddress().contains(mState)) {
                filteredList.add(office);
            } else if (office.getStreet().contains(mAddress) ||
                    office.getStreet().contains(mCity) ||
                    office.getStreet().contains(mState)) {
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

        if (officeList.size() == 1) {
            Office office = officeList.get(0);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(office.getLatitude(), office.getLongitude()), 14.0f));
        } else {
            mMap.moveCamera(cu);
        }
    }
}
