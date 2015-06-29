package com.android.cash1.model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.cash1.FindOfficeActivity;
import com.android.cash1.R;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class InfoDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.dialog, null);
        builder.setView(rootView);

        int dialogId = getArguments().getInt("dialog_id", -1);
        String messageType = getArguments().getString("message_type");
        String bodyMessage = getArguments().getString("message");

        String cancelButtonLabel = getArguments().getString("btn_cancel_label");
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        cancelButton.setText(cancelButtonLabel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (getArguments().getString("btn_confirm_label") != null) {
            String confirmButtonLabel = getArguments().getString("btn_confirm_label");
            Button confirmButton = (Button) rootView.findViewById(R.id.confirm);
            confirmButton.setVisibility(View.VISIBLE);
            confirmButton.setText(confirmButtonLabel);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Cash1Activity)getActivity()).callUs();
                }
            });
        }

        if (dialogId == -1) {
            rootView.findViewById(R.id.spinner).setVisibility(View.GONE);
            TextView bodyTextView = (TextView) rootView.findViewById(R.id.body);
            bodyTextView.setVisibility(View.VISIBLE);
            switch (messageType) {
                case "general login error":
                    bodyTextView.setText(getString(R.string.basic_error_message_login));
                    break;
                case "general error":
                    bodyTextView.setText(getString(R.string.basic_error_message));
                    break;
            }
        }

        if (bodyMessage != null) {
            rootView.findViewById(R.id.spinner).setVisibility(View.GONE);

            TextView bodyTextView = (TextView) rootView.findViewById(R.id.body);
            bodyTextView.setVisibility(View.VISIBLE);
            bodyTextView.setText(bodyMessage);

            Button confirmButton = (Button) rootView.findViewById(R.id.confirm);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FindOfficeActivity activity = (FindOfficeActivity) getActivity();
                    String deviceId = activity.getDeviceId();
                    String userEmail = activity.getUserEmail();
                    int userId = activity.getUserId();
                    String notice = null;
                    boolean userCurrentLocation = true;

                    ApiService service = new RestClient().getApiService();
                    service.setPreferences(deviceId, userEmail, userId, notice, userCurrentLocation, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject responseObject, Response response) {
                            dismiss();
                            activity.displaySearchResults();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Cash1Activity activity = (Cash1Activity) getActivity();
                    activity.showDialog(18, "I");
                }
            });

            return builder.create();
        }

        ApiService service = new RestClient().getApiService();
        service.getDialogContents(dialogId, messageType, new Callback<DialogContents>() {
            @Override
            public void success(DialogContents contents, Response response) {
                rootView.findViewById(R.id.spinner).setVisibility(View.GONE);

                TextView titleTextView = (TextView) rootView.findViewById(R.id.title);
                if (contents.getTitle() != null) {
                    titleTextView.setVisibility(View.VISIBLE);
                    titleTextView.setText(
                            contents.getTitle()
                                    .replace("Cash", "CASH").replace("CASH1", "CASH 1"));
                }

                TextView bodyTextView = (TextView) rootView.findViewById(R.id.body);
                if (contents.getBody() != null) {
                    bodyTextView.setVisibility(View.VISIBLE);
                    bodyTextView.setText(
                            contents.getBody()
                                    .replace(".", ". ").replace(".  ", ". ") // adds spaces after dots
                                    .replace(",", ", ").replace(",  ", ", ") // adds spaces after commas
                                    .replace("Cash1for", "Cash1 for").replace("Cash", "CASH").replace("CASH1", "CASH 1")
                                    .trim());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
        
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getActivity() != null && getActivity().findViewById(R.id.loading) != null) {
            getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
        }
        super.onDismiss(dialog);
    }
}
