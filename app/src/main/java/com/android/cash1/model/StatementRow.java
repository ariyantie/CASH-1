package com.android.cash1.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class StatementRow {

    @SerializedName("Balance")
    private String mBalance;

    @SerializedName("Payment")
    private String mPayment;

    @SerializedName("PaymentType")
    private String mPaymentType;

    @SerializedName("PaymentTypeId")
    private String mPaymentTypeId;

    @SerializedName("TransactionDate")
    private String mTransactionDate;

    public String getBalance() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(Float.parseFloat(mBalance));
    }

    public String getPayment() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(Float.parseFloat(mPayment));
    }

    public String getPaymentType() {
        return mPaymentType;
    }

    public String getPaymentTypeId() {
        return mPaymentTypeId;
    }

    public String getTransactionDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/LL/yyyy");
        return dateFormat.format(Date.parse(mTransactionDate));
    }
}
