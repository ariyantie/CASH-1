package com.android.cash1;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cash1.model.Cash1Activity;
import com.android.cash1.model.FaqItem;
import com.android.cash1.rest.ApiService;
import com.android.cash1.rest.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FaqActivity extends Cash1Activity {

    private ViewGroup mFooterContainer;
    private TextView mFooterToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setupActionBar();
        setupFooter();

        loadQuestionsAndAnswers();
    }

    private void loadQuestionsAndAnswers() {
        ApiService service = new RestClient().getApiService();
        service.listQuestionsAndAnswers(new Callback<List<FaqItem>>() {
            @Override
            public void success(List<FaqItem> itemList, Response response) {
                final ViewGroup container = (ViewGroup) findViewById(R.id.container);

                for (final FaqItem item : itemList) {
                    LinearLayout itemLayout = (LinearLayout) getLayoutInflater().inflate(
                            R.layout.list_item_faq, null);

                    final TextView answerView = (TextView) itemLayout.findViewById(R.id.answer_textview);
                    answerView.setText(item.getAnswer());
                    answerView.setId(item.getId());

                    final TextView questionView = (TextView) itemLayout.findViewById(R.id.question_textview);
                    questionView.setText(item.getQuestion());
                    questionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < 10; i++) {
                                TextView answerView = (TextView) container.findViewById(i);
                                if (answerView != null) {
                                    answerView.setVisibility(View.GONE);
                                }
                            }

                            if (answerView.getVisibility() == View.GONE) {
                                answerView.setVisibility(View.VISIBLE);
                            } else {
                                answerView.setVisibility(View.GONE);
                            }
                        }
                    });

                    container.addView(itemLayout);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(FaqActivity.this, getString(R.string.faq_load_error),
                        Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    @Override
    public void showFaq(View view) {
        Toast.makeText(this, "Already opened", Toast.LENGTH_SHORT).show();
        closeFooter();
    }
}
