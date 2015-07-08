package com.android.cash1.activities.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DispatchActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        getSupportActionBar().hide();

        if (!isOnline()) {
            showNetworkErrorPopup();
            return;
        }

        if (rememberMe()) {
            navigateToLoginScreen();
            return;
        }

        checkDeviceRegistration(getDeviceId());
    }

    private void checkDeviceRegistration(String deviceId) {
        ApiService service = new RestClient().getApiService();
        service.checkDeviceRegistration(deviceId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObject, Response response) {
                boolean deviceRegistered = responseObject.getAsJsonObject("CheckDeviceRegResult")
                        .getAsJsonPrimitive("Is_Device_Registered").getAsBoolean();
                if (deviceRegistered) {
                    checkUserRegistration(getDeviceId());
                } else {
                    registerDevice(getDeviceId());
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void registerDevice(String deviceId) {
        ApiService service = new RestClient().getApiService();
        service.registerDevice(deviceId, Build.MODEL, "phone", Build.MANUFACTURER, Build.MODEL, "1", "Android", Build.VERSION.RELEASE, "1.0", Build.VERSION.RELEASE, "1", "Android", getTimeZone(), "English", "true", "true", Build.MODEL, getDeviceId(), "NotDefinedYet", getUserId() + "", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObject, Response response) {
                String responseMessage = responseObject.getAsJsonPrimitive("Message").getAsString();
                if (responseMessage.contains("success")) {
                    navigateToSplashScreen();
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void checkUserRegistration(String deviceId) {
        ApiService service = new RestClient().getApiService();
        service.checkUserReg(deviceId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObject, Response response) {
                boolean userRegistered = responseObject.getAsJsonObject("CheckUserRegResult")
                        .getAsJsonPrimitive("User_Registered").getAsBoolean();
                if (userRegistered) {
                    navigateToLoginScreen();
                } else {
                    navigateToLoginRegisterScreen();
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void navigateToSplashScreen() {
        startActivity(new Intent(this, SplashActivity.class));
    }

    private void navigateToLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void navigateToLoginRegisterScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("registerMode", true);
        startActivity(intent);
    }

    public String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void showNetworkErrorPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network lost");
        builder.setMessage("Please connect to a network to use CASH 1");
        builder.setIcon(R.drawable.ic_network_not_found);
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void exit(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
