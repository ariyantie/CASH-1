package com.android.cash1.model;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

public class Cash1Application extends Application {
    public void onCreate() {
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "AJJMRCqxNh5ZWXN9i6BRFXpEdewJbLLqo5U1QYKc", "Kp8f0lrhGBktfs5Qwu9f2LmjPa4tJASJCuXUUE58");
    }
}
