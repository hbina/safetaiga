package com.akarin.webapp.structure;

/**
 * TODO: Figure out how to properly handle a classes that are used by two different project to make sure that they are properly maintained....
 */
public class YaaposJsonApiClass {

    public static final int YAAPOS_API_CALL_ERROR_CODE = -1;
    public static final String YAAPOS_API_CALL_DEFAULT_MESSAGE = "This is the default message returned by Yaapos API call";
    public static final String YAAPOS_API_CALL_IS_GOOD = "The JSON API class was successfully populated";
    public static final int YAAPOS_API_CALL_CODE_GOOD = 0;

    private String returnMessage;
    private int returnStatus;

    public YaaposJsonApiClass() {
        returnMessage = YAAPOS_API_CALL_DEFAULT_MESSAGE;
        returnStatus = YAAPOS_API_CALL_ERROR_CODE;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String message) {
        this.returnMessage = message;
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(int returnStatus) {
        this.returnStatus = returnStatus;
    }

    public void setPropertyAsGood() {
        returnMessage = YAAPOS_API_CALL_IS_GOOD;
        returnStatus = YAAPOS_API_CALL_CODE_GOOD;
    }

    public boolean isPropertyGood() {
        return (returnMessage.equals(YAAPOS_API_CALL_IS_GOOD) && returnStatus == YAAPOS_API_CALL_CODE_GOOD);
    }
}
