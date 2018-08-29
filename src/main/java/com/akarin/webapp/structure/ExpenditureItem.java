package com.akarin.webapp.structure;

@SuppressWarnings("unused")
public class ExpenditureItem extends YaaposJsonApiClass {

    private int userId;
    private String spendingName;
    private double spendingPrice;
    private String spendingDescription;
    private int spendingWeekId;

    public ExpenditureItem() {
        super("DEFAULT_RETURN_MESSAGE");
    }

    public ExpenditureItem(int userId, String spendingName, double spendingPrice, String spendingDescription, int spendingWeekId) {
        super("DEFAULT_RETURN_MESSAGE");
        this.userId = userId;
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
        this.spendingDescription = spendingDescription;
        this.spendingWeekId = spendingWeekId;
    }

    public ExpenditureItem(String returnMessage, int userId, String spendingName, double spendingPrice, String spendingDescription, int spendingWeekId) {
        super(returnMessage);
        this.userId = userId;
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
        this.spendingDescription = spendingDescription;
        this.spendingWeekId = spendingWeekId;
    }

    public ExpenditureItem(String returnMessage) {
        super(returnMessage);

    }

    public String getSpendingName() {
        return spendingName;
    }

    public void setSpendingName(String spendingName) {
        this.spendingName = spendingName;
    }

    public double getSpendingPrice() {
        return spendingPrice;
    }

    public void setSpendingPrice(double spendingPrice) {
        this.spendingPrice = spendingPrice;
    }

    public String getSpendingDescription() {
        return spendingDescription;
    }

    public void setSpendingDescription(String spendingDescription) {
        this.spendingDescription = spendingDescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpendingWeekId() {
        return spendingWeekId;
    }

    public void setSpendingWeekId(int spendingWeekId) {
        this.spendingWeekId = spendingWeekId;
    }
}