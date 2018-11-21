package com.akarin.webapp.databases;

import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.structure.ExpenditureLog;
import com.akarin.webapp.structure.YaaposUser;
import com.akarin.webapp.util.AkarinMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

public class YaaposDb {

    private static final Logger logger = LoggerFactory.getLogger(YaaposDb.class);

    public static void getExpendituresGivenUserIdAndWeek(ExpenditureLog el, final int userId, final int week) {
        try (Connection connection = getConnection()) {

            // Resolves Unix difference
            final String script = "SELECT * FROM yaapos_user WHERE yaapos_user.userId = ?;";
            final PreparedStatement stmt = connection.prepareStatement(script);
            stmt.setInt(1, userId);

            logger.info(stmt.toString());
            final ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long userUnixRegistrationTime = rs.getLong("userRegistrationUnixTime");
                stmt.close();

                // Perform query on the Unix range
                long lowerBoundWeekUnix = userUnixRegistrationTime + ((week - 1) * AkarinMath.SECONDS_IN_A_WEEK);
                long upperBoundWeekUnix = lowerBoundWeekUnix + AkarinMath.SECONDS_IN_A_WEEK;
                logger.info(String.format("User registered at userUnixRegistrationTime:%s lowerBoundWeekUnix: %s upperBoundWeekUnix: %s", userUnixRegistrationTime, lowerBoundWeekUnix, upperBoundWeekUnix));
                final String script2 = "SELECT * FROM yaapos_spending WHERE yaapos_spending.userId = ? AND yaapos_spending.spendingUnixTime BETWEEN ? AND ?;";
                final PreparedStatement stmt2 = connection.prepareStatement(script2);
                stmt2.setInt(1, userId);
                stmt2.setLong(2, lowerBoundWeekUnix);
                stmt2.setLong(3, upperBoundWeekUnix);

                logger.info(stmt2.toString());
                final ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    el.addItem(new ExpenditureItem(rs2.getInt("userId"), rs2.getString("spendingName"), rs2.getDouble("spendingPrice"), rs2.getString("spendingDescription"), rs2.getLong("spendingUnixTime")));
                }
                stmt2.close();
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
            final PreparedStatement stmt = connection.prepareStatement(script);

            stmt.setInt(1, item.getUserId());
            stmt.setString(2, item.getSpendingName());
            stmt.setDouble(3, item.getSpendingPrice());
            stmt.setString(4, item.getSpendingDescription());
            stmt.setLong(5, item.getSpendingUnixTime());

            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
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
            final PreparedStatement stmt = connection.prepareStatement(script);

            stmt.setString(1, newUser.getUserName());
            stmt.setLong(2, newUser.getUserRegistrationUnixTime());

            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }

    public static void createYaaposSpending() {
        try (Connection connection = getConnection()) {
            final String script = "CREATE TABLE IF NOT EXISTS yaapos_spending(spendingId SERIAL, userId INTEGER, spendingName TEXT NOT NULL, spendingPrice DECIMAL, spendingDescription TEXT, spendingUnixTime BIGINT NOT NULL, PRIMARY KEY (spendingId), FOREIGN KEY (userId) REFERENCES yaapos_user(userId));";
            final PreparedStatement stmt = connection.prepareStatement(script);

            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }

    public static void createYaaposUser() {
        try (Connection connection = getConnection()) {
            final String script = "CREATE TABLE IF NOT EXISTS yaapos_user(userId SERIAL, userName TEXT NOT NULL, userRegistrationUnixTime BIGINT NOT NULL, PRIMARY KEY(userId));";
            final PreparedStatement stmt = connection.prepareStatement(script);

            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }
}
