package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.Office;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.android.cash1.FindOfficeResultActivity.RESULT_NOT_FOUND;

public class FindOfficeWithAddressActivity extends Cash1Activity {

    private static final int REQUEST_FIND_OFFICE = 101;

    private AutoCompleteTextView mZipCodeEditText;
    private AutoCompleteTextView mAddressEditText;
    private AutoCompleteTextView mCityEditText;
    private AutoCompleteTextView mStateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_office_with_address);

        setupActionBar();
        setupFooter();


        mZipCodeEditText = (AutoCompleteTextView ) findViewById(R.id.zip_code_edittext);
        mAddressEditText = (AutoCompleteTextView) findViewById(R.id.address_edittext);
        mCityEditText = (AutoCompleteTextView) findViewById(R.id.city_edittext);
        mStateEditText = (AutoCompleteTextView) findViewById(R.id.state_edittext);

        setAutoCompleteValuesToFields();

        TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchForOffice();
                }
                return false;
            }
        };
        mZipCodeEditText.setOnEditorActionListener(editTextListener);
        mStateEditText.setOnEditorActionListener(editTextListener);

        Button findButton = (Button) findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForOffice();
            }
        });
    }

    private void setAutoCompleteValuesToFields() {
        ApiService service = new RestClient().getApiService();
        service.listAllOffices(new Callback<List<Office>>() {
            @Override
            public void success(List<Office> officeList, Response response) {
                Set<String> zipCodesSet = new HashSet<>();
                Set<String> addressesSet = new HashSet<>();
                Set<String> citiesSet = new HashSet<>();
                Set<String> statesSet = new HashSet<>();

                for (Office office : officeList) {
                    zipCodesSet.add(office.getZipCodeString());
                    addressesSet.add(office.getAddress());
                    citiesSet.add(office.getCity());
                    statesSet.add(office.getState());
                }

                ArrayAdapter<String> zipCodesAdapter = new ArrayAdapter<>(FindOfficeWithAddressActivity.this,
                        android.R.layout.simple_dropdown_item_1line, new ArrayList<>(zipCodesSet));
                ArrayAdapter<String> addressesAdapter = new ArrayAdapter<>(FindOfficeWithAddressActivity.this,
                        android.R.layout.simple_dropdown_item_1line, new ArrayList<>(addressesSet));
                ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(FindOfficeWithAddressActivity.this,
                        android.R.layout.simple_dropdown_item_1line, new ArrayList<>(citiesSet));
                ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(FindOfficeWithAddressActivity.this,
                        android.R.layout.simple_dropdown_item_1line, new ArrayList<>(statesSet));

                mZipCodeEditText.setAdapter(zipCodesAdapter);
                mAddressEditText.setAdapter(addressesAdapter);
                mCityEditText.setAdapter(citiesAdapter);
                mStateEditText.setAdapter(statesAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void searchForOffice() {
        String zipCodeString = mZipCodeEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        String city = mCityEditText.getText().toString();
        String state = mStateEditText.getText().toString();

        Intent intent = new Intent(this, FindOfficeResultActivity.class);

        if (!address.isEmpty() || !city.isEmpty() || !state.isEmpty()) {
            // search with address, city and state
            intent.putExtra("address", address);
            intent.putExtra("city", city);
            intent.putExtra("state", state);
            intent.putExtra("where_to_search", "cityaddressstate");
        } else {
            if (!zipCodeString.isEmpty()) {
                // search with zip code
                intent.putExtra("zipcode_string", zipCodeString);
                intent.putExtra("where_to_search", "zipcode");
            } else {
                Toast.makeText(this, "Enter search parameters", Toast.LENGTH_LONG).show();
                return;
            }
        }
        startActivityForResult(intent, REQUEST_FIND_OFFICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FIND_OFFICE && resultCode == RESULT_NOT_FOUND) {
            showDialog(19, "E");
        }
    }
}
