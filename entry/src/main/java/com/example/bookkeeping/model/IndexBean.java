package com.example.bookkeeping.model;

import java.util.Calendar;

public class IndexBean extends RecordBean{
    private String bigDate;
    private String smallDate;
    private double balance;
    private double inputCnt;
    private double outputCnt;


    public IndexBean(String bigDate, String smallDate, double balance, double inputCnt, double outputCnt) {
        super(-1, "null", "null", 0.0, "null", Calendar.getInstance());
        this.bigDate = bigDate;
        this.smallDate = smallDate;
        this.balance = balance;
        this.inputCnt = inputCnt;
        this.outputCnt = outputCnt;
    }

    public String getBigDate() {
        return bigDate;
    }

    public void setBigDate(String bigDate) {
        this.bigDate = bigDate;
    }

    public String getSmallDate() {
        return smallDate;
    }

    public void setSmallDate(String smallDate) {
        this.smallDate = smallDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInputCnt() {
        return inputCnt;
    }

    public void setInputCnt(double inputCnt) {
        this.inputCnt = inputCnt;
    }

    public double getOutputCnt() {
        return outputCnt;
    }

    public void setOutputCnt(double outputCnt) {
        this.outputCnt = outputCnt;
    }
}
