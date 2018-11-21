package com.akarin.webapp.structure;

public class YaaposUser extends YaaposJsonApiClass {

    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final long DEFAULT_USER_REGISTRATION_UNIX_TIME = 0L;
    private String userName;
    private long userRegistrationUnixTime;

    public YaaposUser() {
        super();
        this.userName = DEFAULT_USER_NAME;
        this.userRegistrationUnixTime = DEFAULT_USER_REGISTRATION_UNIX_TIME;
    }

    public YaaposUser(String userName, long userRegistrationUnixTime) {
        super();
        this.userName = userName;
        this.userRegistrationUnixTime = userRegistrationUnixTime;
    }

    @SuppressWarnings("unused")
    public String getUserName() {
        return userName;
    }

    @SuppressWarnings("unused")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserRegistrationUnixTime() {
        return userRegistrationUnixTime;
    }

    public void setUserRegistrationUnixTime(long userRegistrationUnixTime) {
        this.userRegistrationUnixTime = userRegistrationUnixTime;
    }
}
