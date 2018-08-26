package com.akarin.webapp.structure;


public class ExpenditureItem {

    private final String spendingName;
    private final double spendingPrice;

    public ExpenditureItem(String spendingName, double spendingPrice) {
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
    }

    public String getSpendingName() {
        return spendingName;
    }

    public double getSpendingPrice() {
        return spendingPrice;
    }

}