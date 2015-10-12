package com.android.cash1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.R;
import com.android.cash1.activities.login.LogoutActivity;
import com.android.cash1.model.Cash1Activity;

public class UpdateInfoActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        setupActionBar();
        setupFooter();
    }

    public void showComingSoonToast(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logout(View view) {
        showConfirmLogoutPopup();
    }

    private void showConfirmLogoutPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog, null);
        builder.setView(rootView);

        rootView.findViewById(R.id.spinner).setVisibility(View.GONE);
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        titleView.setVisibility(View.VISIBLE);
        titleView.setText("Update Not Saved");
        TextView messageView = (TextView) rootView.findViewById(R.id.body);
        messageView.setVisibility(View.VISIBLE);
        messageView.setText("You have not completed your update of information. Are you sure you " +
                "want to log out? Your account update information will not be saved until the " +
                "update has been completed.");

        final AlertDialog dialog = builder.create();

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm);
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setText("Log Out");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateInfoActivity.this, LogoutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setText("Return to Update");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
