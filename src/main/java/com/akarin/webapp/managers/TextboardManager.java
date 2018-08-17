package com.akarin.webapp.managers;

import com.akarin.webapp.util.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(TextboardManager.class);

    public static boolean checkIfBoardIsAvailable(final String boardlink) {
        final String SCRIPT_SELECT_GIVEN_BOARDLINK = "SELECT * FROM boards WHERE boardlink = '" + boardlink + "';";
        logger.info("SCRIPT_SELECT_GIVEN_BOARDLINK:" + SCRIPT_SELECT_GIVEN_BOARDLINK);

        try (Connection connection = DatabaseManager.getConnection()) {

            final Statement stmt = connection.createStatement();
            final ResultSet resultSet = stmt.executeQuery(SCRIPT_SELECT_GIVEN_BOARDLINK);
            // this is how you get a column given the column name in string
            // rs.getString(columnLabel)
            if (resultSet.next()) {
                // means resultSet is non-empty
                logger.info("END:checkIfBoardIsAvailable:0");
                return false;
            } else {
                // means resultSet is empty
                logger.info("END:checkIfBoardIsAvailable:1");
                return true;
            }
        } catch (final Exception e) {
            logger.warn(e.getMessage());
        }

        // connection to database likely experienced an error
        return false;
    }

    public static boolean checkIfTextIsAcceptable(final String givenText) {
        return givenText.length() != 0;
    }

    public static String getPREVIOUSBOARDLINK(final String previousBoardLink) {
        return Reference.CommonStrings.LINK_TEXTBOARD + previousBoardLink + "/";
    }

    public static String getPREVIOUSTHREAD(final String previousBoardLink, final String previousThreadId) {
        return Reference.CommonStrings.LINK_TEXTBOARD + previousBoardLink + "/" + previousThreadId + "/";
    }
}