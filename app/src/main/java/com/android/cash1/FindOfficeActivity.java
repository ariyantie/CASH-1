package com.android.cash1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.InfoDialogFragment;
import com.android.cash1.model.Preferences;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FindOfficeActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_office);

        setupActionBar();
        setupFooter();
    }

    @Override
    public void findOffice(View view) {
        closeFooter();
    }

    public void findUsingCurrentLocation(View view) {
        ApiService service = new RestClient().getApiService();
        service.getPreferences(getUserEmail(), getUserId(), new Callback<Preferences>() {
            @Override
            public void success(Preferences preferencesObject, Response response) {
                boolean useCurrentLocation = preferencesObject.allowDetectLocation();
                if (!useCurrentLocation) {
                    promptToAllowLocation();
                } else {
                    displaySearchResults();
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void displaySearchResults() {
        startActivity(new Intent(this, FindOfficeResultActivity.class));
    }

    private void promptToAllowLocation() {
        DialogFragment dialog = new InfoDialogFragment();

        Bundle args = new Bundle();
        args.putString("message", getString(R.string.ask_for_location_message));
        args.putString("btn_confirm_label", "Allow");
        args.putString("btn_cancel_label", "Don't allow");
        dialog.setArguments(args);

        dialog.show(getSupportFragmentManager(), "dialog");
    }
}
