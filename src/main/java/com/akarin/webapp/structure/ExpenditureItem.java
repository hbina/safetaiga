package com.akarin.webapp.structure;

@SuppressWarnings("unused")
public class ExpenditureItem extends YaaposJsonApiClass {

    public static final int DEFAULT_USER_ID = -1;
    public static final String DEFAULT_SPENDING_NAME = "DEFAULT_SPENDING_NAME";
    public static final double DEFAULT_SPENDING_PRICE = -1.0;
    public static final String DEFAULT_SPENDING_DESCRIPTION = "DEFAULT_SPENDING_DESCRIPTION";
    public static final long DEFAULT_SPENDING_UNIX_TIME = 0L;

    private int userId;
    private String spendingName;
    private double spendingPrice;
    private String spendingDescription;
    private long spendingUnixTime;

    public ExpenditureItem() {
        super();
        this.userId = DEFAULT_USER_ID;
        this.spendingName = DEFAULT_SPENDING_NAME;
        this.spendingPrice = DEFAULT_SPENDING_PRICE;
        this.spendingDescription = DEFAULT_SPENDING_DESCRIPTION;
        this.spendingUnixTime = DEFAULT_SPENDING_UNIX_TIME;
    }

    public ExpenditureItem(int userId, String spendingName, double spendingPrice, String spendingDescription, long spendingUnixTime) {
        super();
        this.userId = userId;
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
        this.spendingDescription = spendingDescription;
        this.spendingUnixTime = spendingUnixTime;
    }

    public ExpenditureItem(String returnMessage, int userId, String spendingName, double spendingPrice, String spendingDescription, long spendingUnixTime) {
        super(returnMessage);
        this.userId = userId;
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
        this.spendingDescription = spendingDescription;
        this.spendingUnixTime = spendingUnixTime;
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

    public long getSpendingUnixTime() {
        return spendingUnixTime;
    }

    public void setSpendingUnixTime(long spendingUnixTime) {
        this.spendingUnixTime = spendingUnixTime;
    }
}