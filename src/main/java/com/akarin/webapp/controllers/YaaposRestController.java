package com.akarin.webapp.controllers;

import com.akarin.webapp.databases.YaaposDb;
import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.structure.ExpenditureLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

@RestController
public class YaaposRestController {

    private static final Logger logger = LoggerFactory.getLogger(YaaposRestController.class);

    @RequestMapping("/yaapos/test")
    public ArrayList<String> yaaposTest(@RequestParam(value = "firstName", defaultValue = "first") String firstName, @RequestParam(value = "lastName", defaultValue = "last") String lastName) {
        ArrayList<String> als = new ArrayList<>();
        als.add(firstName);
        als.add(lastName);

        return als;
    }

    @GetMapping("/yaapos/user/{userId}/spendingWeekId/{spendingWeekId}")
    public ExpenditureLog getYaaposSpending(@PathVariable(value = "userId") int userId, @PathVariable(value = "spendingWeekId") int spendingWeekId) {
        ExpenditureLog expenditureLogs = new ExpenditureLog(String.format("This is the ExpenditureLog for user:%s spendingWeekId:%s", userId, spendingWeekId));
        try {
            YaaposDb.getExpendituresGivenUserId(expenditureLogs, userId, spendingWeekId);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return expenditureLogs;
    }

    @PostMapping("/yaapos/user")
    public ExpenditureItem postYaaposSpending(@RequestParam(value = "userId") int userId,
                                              @RequestParam(value = "spendingName") String spendingName,
                                              @RequestParam(value = "spendingPrice") double spendingPrice,
                                              @RequestParam(value = "spendingDescription") String spendingDescription,
                                              @RequestParam(value = "spendingWeekId") int spendingWeekId) throws IllegalArgumentException {
        String returnMessage;
        ExpenditureItem item = new ExpenditureItem();
        if (checkUserId(userId) &&
                checkSpendingName(spendingName) &&
                checkSpendingPrice(spendingPrice) &&
                checkSpendingDescription(spendingDescription) &&
                checkSpendingWeekId(spendingWeekId)) {

            try (Connection connection = getConnection()) {
                logger.info(String.format("Insert into the database a new expenditure item with the following properties(userId, spendingName, spendingPrice, spendingDescription, spendingWeekId) VALUES (%s,%s,%s,%s,%s)", userId, spendingName, spendingPrice, spendingDescription, spendingWeekId));
                final String script = "INSERT INTO yaapos_spending (userId, spendingName, spendingPrice, spendingDescription, spendingWeekId) VALUES (?, ?, ?, ?, ?);";
                final PreparedStatement pitt = connection.prepareStatement(script);
                pitt.setInt(1, userId);
                pitt.setString(2, spendingName);
                pitt.setDouble(3, spendingPrice);
                pitt.setString(4, spendingDescription);
                pitt.setInt(5, spendingWeekId);
                pitt.executeUpdate();
                pitt.close();
            } catch (URISyntaxException e) {
                logger.info(e.getMessage());
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }

            returnMessage = "Database operation did not throw any exception";
        } else {
            returnMessage = "The parameters provided failed to pass the test" + "spendingName:" + checkSpendingName(spendingName) + " spendingPrice:" +
                    checkSpendingPrice(spendingPrice) + " spendingDescription:" +
                    checkSpendingDescription(spendingDescription) + " spendingWeekId:" +
                    checkSpendingWeekId(spendingWeekId);
        }

        item.setReturnMessage(returnMessage);
        return item;
    }

    private boolean checkSpendingWeekId(int spendingWeekId) {
        return spendingWeekId > 0;
    }

    private boolean checkSpendingDescription(String spendingDescription) {
        return true;
    }

    private boolean checkSpendingPrice(double spendingPrice) {
        return spendingPrice > 0;
    }

    private boolean checkSpendingName(String spendingName) {
        return spendingName.length() > 0;
    }

    private boolean checkUserId(int userId) {
        return userId > 0;
    }
}
