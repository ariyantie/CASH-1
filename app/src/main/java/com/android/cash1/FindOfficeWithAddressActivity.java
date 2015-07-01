package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;

import static com.android.cash1.FindOfficeResultActivity.RESULT_NOT_FOUND;

public class FindOfficeWithAddressActivity extends Cash1Activity {

    private static final int REQUEST_FIND_OFFICE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_office_with_address);

        setupActionBar();
        setupFooter();

        Button findButton = (Button) findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
        findButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View button, boolean hasFocus) {
                if (button.isInTouchMode()) {
                    button.performClick();
                }
            }
        });
    }

    public void find() {
        EditText zipCodeEditText = (EditText) findViewById(R.id.zip_code_edittext);
        EditText addressEditText = (EditText) findViewById(R.id.address_edittext);
        EditText cityEditText = (EditText) findViewById(R.id.city_edittext);
        EditText stateEditText = (EditText) findViewById(R.id.state_edittext);

        String zipCodeString = zipCodeEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();

        Intent intent = new Intent(this, FindOfficeResultActivity.class);

        if (!address.isEmpty() && !city.isEmpty() && !state.isEmpty()) {
            // search with address, city and state
            intent.putExtra("address", address);
            intent.putExtra("city", city);
            intent.putExtra("state", state);
            intent.putExtra("where_to_search", "cityaddressstate");
        } else {
            if (!address.isEmpty() || !city.isEmpty() || !state.isEmpty()) {
                Toast.makeText(this, "Make sure to fill out address, city and state", Toast.LENGTH_LONG).show();
                return;
            } else if (!zipCodeString.isEmpty()) {
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
