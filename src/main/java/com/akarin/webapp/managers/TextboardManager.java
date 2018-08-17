package com.akarin.webapp.managers;

import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.Tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TextboardManager {


    /**
     * DATABASE TEXTBOARD VOCABULARIES
     */
    public final static String BOARDLINK = "boardlink";
    public final static String BOARDNAME = "boardname";
    public final static String THREADID = "threadid";
    public final static String THREADTEXT = "threadtext";
    public final static String POSTID = "postid";
    public final static String POSTTEXT = "posttext";
    public final static String BOARDDESCRIPTION = "boarddescription";

    public static boolean checkIfBoardIsAvailable(final String boardlink) {
        final String SCRIPT_SELECT_GIVEN_BOARDLINK = "SELECT * FROM boards WHERE boardlink = '" + boardlink + "';";
        Tools.coutln("SCRIPT_SELECT_GIVEN_BOARDLINK:" + SCRIPT_SELECT_GIVEN_BOARDLINK);

        try (Connection connection = DatabaseManager.getConnection()) {

            final Statement stmt = connection.createStatement();
            final ResultSet resultSet = stmt.executeQuery(SCRIPT_SELECT_GIVEN_BOARDLINK);
            // this is how you get a column given the column name in string
            // rs.getString(columnLabel)
            if (resultSet.next()) {
                // means resultSet is non-empty
                Tools.coutln("END:checkIfBoardIsAvailable:0");
                return false;
            } else {
                // means resultSet is empty
                Tools.coutln("END:checkIfBoardIsAvailable:1");
                return true;
            }
        } catch (final Exception exception) {
            Tools.coutln("ERROR:" + exception.getMessage());
        }

        // connection to database likely experienced an error
        return false;
    }

    public static boolean checkIfTextIsAcceptable(final String givenText) {
        if (givenText.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String getPREVIOUSBOARDLINK(final String previousBoardLink) {
        return Reference.CommonStrings.LINK_TEXTBOARD + previousBoardLink + "/";
    }

    public static String getPREVIOUSTHREAD(final String previousBoardLink, final String previousThreadId) {
        return Reference.CommonStrings.LINK_TEXTBOARD + previousBoardLink + "/" + previousThreadId + "/";
    }
}