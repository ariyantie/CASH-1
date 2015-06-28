package com.android.cash1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DispatchActivity extends AppCompatActivity {

    private String LOG_TAG = DispatchActivity.class.getSimpleName();
    private ResponseCallback mLogCallback = new ResponseCallback() {
        @Override
        public void success(Response response) {
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };
    private ApiService mService = new RestClient().getApiService();

    private String mThread = "Thread ID: " + Thread.currentThread().getId();
    private String mContext = DispatchActivity.class.getCanonicalName();
    private String mLogLevel = "Error";
    private String mLogger = "{Not available}";
    private String mScreenName = "Initial screen (to choose where to redirect)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        getSupportActionBar().hide();

        if (!isOnline()) {
            showNetworkErrorPopup();
            return;
        }

        boolean remember = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("remember", false);
        if (remember) {
            navigateToLoginScreen();
            return;
        }

        try {
            registerDevice();
        } catch (Exception e) {
            Log.i(LOG_TAG, "Exception caught during device registration. Skip this step.");
            mService.log(mThread, mContext, mLogLevel, mLogger,
                    getStackTraceString(e), e.getMessage(),
                    getDeviceId(), mScreenName, mLogCallback);
        }

        try {
            checkUserReg();
        } catch (Exception e) {
            Log.i(LOG_TAG, "Exception caught during user registration check. " +
                    "Navigate to splash screen by default.");
            mService.log(mThread, mContext, mLogLevel, mLogger,
                    getStackTraceString(e), e.getMessage(),
                    getDeviceId(), mScreenName, mLogCallback);
            navigateToLoginScreen();
        }
    }

    private String getStackTraceString(Exception e) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    private void checkUserReg() {
        ApiService service = new RestClient().getApiService();
        service.checkUserReg(getDeviceId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                try {
                    boolean isRegistered = responseObj.getAsJsonObject("CheckUserRegResult").getAsJsonPrimitive("User_Registered").getAsBoolean();
                    if (isRegistered) {
                        navigateToLoginScreen();
                    } else {
                        navigateToLoginRegisterScreen();
                    }
                } catch (Exception e) {
                    Log.i(LOG_TAG, "Failed to read response for user registration. " +
                            "Navigate to login screen by default.");
                    mService.log(mThread, mContext, mLogLevel, mLogger,
                            getStackTraceString(e), e.getMessage(),
                            getDeviceId(), mScreenName, mLogCallback);
                    navigateToLoginScreen();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.i(LOG_TAG, "Network is not available in a moment. Navigate to splash screen by default.");
                mService.log(mThread, mContext, mLogLevel, mLogger,
                        getStackTraceString(error), error.getMessage(),
                        getDeviceId(), mScreenName, mLogCallback);
                navigateToLoginScreen();
            }
        });
    }

    private void registerDevice() {
        ApiService service = new RestClient().getApiService();
        service.registerDevice(
                getDeviceId(), Build.MODEL, "phone", Build.MANUFACTURER, Build.MODEL, "1", "Android", Build.VERSION.RELEASE, "1.0", Build.VERSION.RELEASE, "1", "Android", getTimeZone(), "English", "true", "true", Build.MODEL, getDeviceId(), "NotDefinedYet", getUserId() + "",
                new Callback<HashMap<String, String>>() {
                    @Override
                    public void success(HashMap<String, String> responseHashMap, Response response) {
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        mService.log(mThread, mContext, mLogLevel, mLogger,
                                getStackTraceString(error), error.getMessage(),
                                getDeviceId(), mScreenName, mLogCallback);
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
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putBoolean("registerMode", true).apply();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private String getDeviceId() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.i(LOG_TAG, "Failed to get device id. Return 123456789 by default");
            mService.log(mThread, mContext, mLogLevel, mLogger,
                    getStackTraceString(e), e.getMessage(),
                    getDeviceId(), mScreenName, mLogCallback);
            return "123456789";
        }
    }

    private int getUserId() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id", 0);
    }

    public String getTimeZone() {
        try {
            return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
        } catch (Exception e) {
            Log.i(LOG_TAG, "Failed to get time zone. Return EST by default");
            mService.log(mThread, mContext, mLogLevel, mLogger,
                    getStackTraceString(e), e.getMessage(),
                    getDeviceId(), mScreenName, mLogCallback);
            return "EST";
        }
    }

    public boolean isOnline() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.i(LOG_TAG, "Failed to check network availability. " +
                    "Return isConnected = true in case of unstable connection.");
            mService.log(mThread, mContext, mLogLevel, mLogger,
                    getStackTraceString(e), e.getMessage(),
                    getDeviceId(), mScreenName, mLogCallback);
            return true;
        }
    }

    private void showNetworkErrorPopup() {
        try {
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
        } catch (Exception e) {
            Log.d(LOG_TAG, "User cancelled the progress bar. Dialog is not shown.");
            mService.log(mThread, mContext, mLogLevel, mLogger,
                    getStackTraceString(e), e.getMessage(),
                    getDeviceId(), mScreenName, mLogCallback);
        }
    }

    public void exit(View view) {
        finish();
    }
}
