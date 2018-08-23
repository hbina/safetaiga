package com.akarin.webapp.structure;

import java.util.ArrayList;

public class YaaposSpending {

    private static ArrayList<Expenditure> expenditures;
    private final String userId;

    public YaaposSpending(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<Expenditure> getExpenditures() {
        return expenditures;
    }

    public void addExpenditure(String name, double price) {
        if (expenditures == null){
            expenditures = new ArrayList<>();
        }
        expenditures.add(new Expenditure(name, price));
    }
}
