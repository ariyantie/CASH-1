package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

public class FaqItem {

    @SerializedName("Question_No")
    public String id;

    @SerializedName("Question")
    public String question;

    @SerializedName("Answer")
    public String answer;

    public int getId() {
        return Integer.parseInt(id);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
