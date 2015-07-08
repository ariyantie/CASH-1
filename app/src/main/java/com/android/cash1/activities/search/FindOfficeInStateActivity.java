package com.android.cash1.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.Office;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;

import java.util.List;
import java.util.TreeSet;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FindOfficeInStateActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_office_in_state);

        setupActionBar();
        setupFooter();

        String stateAbbreviation = getIntent().getStringExtra("state_abbreviation");
        displayOfficesForSelectedState(stateAbbreviation);
    }

    private void displayOfficesForSelectedState(String stateAbbreviation) {
        ApiService service = new RestClient().getApiService();
        service.listOfficesInState(stateAbbreviation, new Callback<List<Office>>() {
            @Override
            public void success(List<Office> officeList, Response response) {
                findViewById(R.id.loading).setVisibility(View.GONE);

                TreeSet<String> headersHashSet = new TreeSet<>();
                for (Office office : officeList) {
                    headersHashSet.add(office.getCity());
                }

                LinearLayout mainContainer = (LinearLayout) findViewById(R.id.container);
                for (String cityHeader : headersHashSet) {
                    LinearLayout sectionsContainer = (LinearLayout) View.inflate(
                            FindOfficeInStateActivity.this, R.layout.office_in_state_section, null);

                    TextView sectionHeaderView = (TextView) sectionsContainer.findViewById(R.id.header_textview);
                    sectionHeaderView.setText(cityHeader);
                    mainContainer.addView(sectionsContainer);

                    for (int i = 0; i < officeList.size(); i++) {
                        final Office office = officeList.get(i);
                        if (office.getCity().equals(cityHeader)) {
                            FrameLayout listItemContainer = (FrameLayout) View.inflate(
                                    FindOfficeInStateActivity.this, R.layout.office_list_item, null);

                            listItemContainer.findViewById(R.id.position).setVisibility(View.GONE);

                            TextView phoneTextView = (TextView) listItemContainer.findViewById(R.id.distance_to);
                            String phone = office.getPhone();
                            phoneTextView.setText(phone);

                            // selectively hide bottom dividers for office list items
                            switch (cityHeader) {
                                case "TEMPE":
                                case "RENO":
                                case "KENT":
                                    listItemContainer.findViewById(R.id.divider).setVisibility(View.GONE);
                            }
                            switch (phone) {
                                case "(623) 376-8888":
                                case "(480) 833-0674":
                                case "(602) 674-5079":
                                case "(775) 240-6317":
                                    listItemContainer.findViewById(R.id.divider).setVisibility(View.GONE);
                            }

                            TextView streetTextView = (TextView) listItemContainer.findViewById(R.id.street);
                            String street = office.getStreet();
                            streetTextView.setText(street);

                            TextView addressTextView = (TextView) listItemContainer.findViewById(R.id.address);
                            String address = office.getAddress();
                            addressTextView.setText(address);

                            listItemContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(FindOfficeInStateActivity.this, OfficeDetailActivity.class);
                                    intent.putExtra("store_id", office.getId());
                                    intent.putExtra("latitude", office.getLatitude());
                                    intent.putExtra("longitude", office.getLongitude());
                                    startActivity(intent);
                                }
                            });

                            sectionsContainer.addView(listItemContainer);
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
