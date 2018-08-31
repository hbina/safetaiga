package com.akarin.webapp.databases;

import com.akarin.webapp.controllers.YaaposRestController;
import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.structure.ExpenditureLog;
import com.akarin.webapp.structure.YaaposUser;
import com.akarin.webapp.util.AkarinMath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

public class YaaposDb {

    private static final Logger logger = LoggerFactory.getLogger(YaaposDb.class);

    public static void getExpendituresGivenUserIdAndWeek(ExpenditureLog el, final int userId, final int week) {
        try (Connection connection = getConnection()) {

            // Resolves Unix difference
            final String script = "SELECT * FROM yaapos_user WHERE yaapos_user.userId = ?;";
            final PreparedStatement pitt = connection.prepareStatement(script);
            pitt.setInt(1, userId);

            logger.info(pitt.toString());
            final ResultSet rs = pitt.executeQuery();
            if (rs.next()) {
                long userUnixRegistrationTime = rs.getLong("userRegistrationUnixTime");
                pitt.close();

                // Perform query on the Unix range
                long lowerBoundWeekUnix = userUnixRegistrationTime + ((week - 1) * AkarinMath.SECONDS_IN_A_WEEK);
                long upperBoundWeekUnix = lowerBoundWeekUnix + AkarinMath.SECONDS_IN_A_WEEK;
                logger.info(String.format("User registered at userUnixRegistrationTime:%s lowerBoundWeekUnix: %s upperBoundWeekUnix: %s", userUnixRegistrationTime, lowerBoundWeekUnix, upperBoundWeekUnix));
                final String script2 = "SELECT * FROM yaapos_spending WHERE yaapos_spending.userId = ? AND yaapos_spending.spendingUnixTime BETWEEN ? AND ?;";
                final PreparedStatement pitt2 = connection.prepareStatement(script2);
                pitt2.setInt(1, userId);
                pitt2.setLong(2, lowerBoundWeekUnix);
                pitt2.setLong(3, upperBoundWeekUnix);

                final ResultSet rs2 = pitt2.executeQuery();
                while (rs2.next()) {
                    el.addItem(new ExpenditureItem(rs2.getInt("userId"), rs2.getString("spendingName"), rs2.getDouble("spendingPrice"), rs2.getString("spendingDescription"), rs2.getLong("spendingUnixTime")));
                }
                pitt2.close();
            }
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
    }

    public static void postYaaposSpendingGivenExpenditureItems(ExpenditureItem item) {
        try (Connection connection = getConnection()) {
            final String script = "INSERT INTO yaapos_spending (userId, spendingName, spendingPrice, spendingDescription, spendingUnixTime) VALUES (?, ?, ?, ?, ?);";
            final PreparedStatement pitt = connection.prepareStatement(script);

            pitt.setInt(1, item.getUserId());
            pitt.setString(2, item.getSpendingName());
            pitt.setDouble(3, item.getSpendingPrice());
            pitt.setString(4, item.getSpendingDescription());
            pitt.setLong(5, item.getSpendingUnixTime());

            logger.info(pitt.toString());
            pitt.executeUpdate();
            pitt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }

    public static void registerYaaposUser(YaaposUser newUser) {
        try (Connection connection = getConnection()) {
            final String script = "INSERT INTO yaapos_user (userName, userRegistrationUnixTime) VALUES (?, ?);";
            final PreparedStatement pitt = connection.prepareStatement(script);

            pitt.setString(1, newUser.getUserName());
            pitt.setLong(2, newUser.getUserRegistrationUnixTime());

            logger.info(pitt.toString());
            pitt.executeUpdate();
            pitt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }
}
