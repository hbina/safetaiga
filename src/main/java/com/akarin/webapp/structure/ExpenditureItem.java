package com.akarin.webapp.structure;


public class ExpenditureItem {

    private String spendingName;
    private double spendingPrice;

    public ExpenditureItem() {
    }

    public ExpenditureItem(String spendingName, double spendingPrice) {
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
    }

    public void setSpendingName() {
        this.spendingName = spendingName;
    }

    public void setSpendingPrice() {
        this.spendingPrice = spendingPrice;
    }

    public String getSpendingName() {
        return spendingName;
    }

    public double getSpendingPrice() {
        return spendingPrice;
    }

}