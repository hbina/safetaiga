package com.akarin.webapp.structure;

/**
 * TODO: Figure out how to properly handle a classes that are used by two different project to make sure that they are properly maintained....
 */
public class YaaposJsonApiClass {

    private static final String DEFAULT_RETURN_MESSAGE = "DEFAULT_RETURN_MESSAGE";
    private String returnMessage;

    YaaposJsonApiClass() {
        this.returnMessage = DEFAULT_RETURN_MESSAGE;
    }

    YaaposJsonApiClass(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
