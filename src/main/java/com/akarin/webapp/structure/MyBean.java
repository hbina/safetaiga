package com.akarin.webapp.structure;

public class MyBean {
    private final String firstName;
    private final String lastName;


    public MyBean(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}