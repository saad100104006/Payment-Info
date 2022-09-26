package com.qpxficgbgi.paymentdetail.models;

import java.io.Serializable;

public class Payment implements Serializable {
    private int paymentId;
    private String amount;
    private String paymentType;
    private String provider;
    private String transactionRef;

    public Payment(int paymentId, String amount, String paymentType, String provider, String transactionRef) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.provider = provider;
        this.transactionRef = transactionRef;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}
