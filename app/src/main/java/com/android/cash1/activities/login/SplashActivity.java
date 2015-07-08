package com.android.cash1.activities.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;


public class SplashActivity extends Cash1Activity {

    private TextAppearanceSpan mHintAppearance;
    private TextAppearanceSpan mBodyAppearance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHintAppearance = new TextAppearanceSpan(this, R.style.SplashButtonTextHint);
        mBodyAppearance = new TextAppearanceSpan(this, R.style.SplashButtonTextBody);

        Button loginButton = (Button) findViewById(R.id.login);
        Button registerButton = (Button) findViewById(R.id.register);

        String loginHint = getString(R.string.splash_login_button_hint);
        String loginBody = getString(R.string.splash_login_button_body);

        SpannableString loginButtonText =
                new SpannableString(loginHint + "\n" +
                        loginBody);
        int start = 0; int end = loginHint.length();
        loginButtonText.setSpan(mHintAppearance, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        start = end; end=loginButtonText.length();
        loginButtonText.setSpan(mBodyAppearance, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginButton.setText(loginButtonText);


        String registerHint = getString(R.string.splash_register_button_hint);
        String registerBody = getString(R.string.splash_register_button_body);

        SpannableString registerButtonText =
                new SpannableString(registerHint + "\n" +
                        registerBody);
        start = 0; end = registerHint.length();
        registerButtonText.setSpan(mHintAppearance, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        start = end; end=registerButtonText.length();
        registerButtonText.setSpan(mBodyAppearance, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        registerButton.setText(registerButtonText);
    }

    public void navigateToLoginScreen(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("registerMode", true)
                .putBoolean("remember", false).apply();
    }

    public void browseOriginalWebsite(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.cash1loans.com/apply-now.aspx"));
        startActivity(i);
    }
}
