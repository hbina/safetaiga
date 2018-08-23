package com.akarin.webapp.structure;

public class Expenditure {

    private final String name;
    private final double price;

    public Expenditure(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
