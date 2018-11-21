package com.akarin.webapp.structure;

import java.util.ArrayList;

public class ExpenditureLog extends YaaposJsonApiClass {

    private final ArrayList<ExpenditureItem> expenditureItemsList;

    public ExpenditureLog(String message) {
        super(message);
        expenditureItemsList = new ArrayList<>();
    }

    public ArrayList<ExpenditureItem> getExpenditureItemsList() {
        return expenditureItemsList;
    }

    public void addItem(ExpenditureItem i) {
        expenditureItemsList.add(i);
    }
}

