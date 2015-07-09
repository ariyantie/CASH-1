package com.android.cash1.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.Cash1ApiService;
import com.android.cash1.rest.Cash1Client;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SecurityQuestionActivity extends Cash1Activity {

    private Spinner mQuestionSpinner;
    private EditText mAnswerEditText;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        setupActionBar();


        mAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        mQuestionSpinner = (Spinner) findViewById(R.id.question);
        mQuestionSpinner.setAdapter(mAdapter);

        mAnswerEditText = (EditText) findViewById(R.id.answer);

        Cash1ApiService service = new Cash1Client().getApiService();
        service.listSecurityQuestions(new Callback<List<HashMap<String, String>>>() {
            @Override
            public void success(List<HashMap<String, String>> questionHashMaps, Response response) {
                for (HashMap<String, String> questionHashMap : questionHashMaps) {
                    String question = questionHashMap.get("Question");
                    mAdapter.add(question);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void submit(View view) {
        String questionId = String.valueOf(mQuestionSpinner.getSelectedItemPosition() + 1);
        String answer = mAnswerEditText.getText().toString();
        int userId = getUserId();

        if (answer.isEmpty()) {
            Toast.makeText(this, "Please enter your answer", Toast.LENGTH_SHORT).show();
            return;
        }

        Cash1ApiService service = new Cash1Client().getApiService();
        service.saveSecurityQuestion(questionId, answer, userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                if (responseObj.getAsJsonPrimitive("Message").getAsString().contains("information has been updated")) {
                    if (!PreferenceManager.getDefaultSharedPreferences(SecurityQuestionActivity.this).getBoolean("return_to_login", false)) {
                        navigateToCongratsScreen();
                    } else {
                        returnToLoginScreen();
                        Toast.makeText(SecurityQuestionActivity.this, "Now please try to login again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showIncorrectUsernameOrPasswordPopup();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void navigateToCongratsScreen() {
        startActivity(new Intent(this, CongratsActivity.class));
    }

    private void returnToLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
