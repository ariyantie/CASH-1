package com.android.cash1.activities.support;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.cash1.R;
import com.android.cash1.model.Cash1Activity;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;
import com.android.cash1.model.StatementRow;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AccountStatementActivity extends Cash1Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_statement);

        setupActionBar();
        setupFooter();

        getStatement();
    }

    private void getStatement() {
        int userId = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt("user_id", 12345);

        ApiService service = new RestClient().getApiService();
        service.getAccountStatement(userId, new Callback<List<StatementRow>>() {
            @Override
            public void success(List<StatementRow> statementList, Response response) {
                ViewGroup container = (ViewGroup) findViewById(R.id.statement_container);

                if (statementList.size() == 0) {
                    findViewById(R.id.empty).setVisibility(View.VISIBLE);
                    return;
                }

                for (StatementRow row : statementList) {
                    ViewGroup rowContainer = (ViewGroup) View.inflate(
                            AccountStatementActivity.this,
                            R.layout.statement_row,
                            null);

                    ((TextView) rowContainer.findViewById(R.id.date)).setText(row.getTransactionDate());
                    ((TextView) rowContainer.findViewById(R.id.type)).setText(row.getPaymentType());
                    ((TextView) rowContainer.findViewById(R.id.amount)).setText(row.getPayment());
                    ((TextView) rowContainer.findViewById(R.id.balance)).setText(row.getBalance());

                    container.addView(rowContainer);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
