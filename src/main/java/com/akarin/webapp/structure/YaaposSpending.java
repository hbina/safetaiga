package com.akarin.webapp.structure;

import java.util.ArrayList;

public class YaaposSpending {

    private final String userId;
    private ArrayList<Expenditure> expenditures;

    public YaaposSpending(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<Expenditure> getExpenditures() {
        return expenditures;
    }
}
