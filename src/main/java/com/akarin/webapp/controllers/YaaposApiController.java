package com.akarin.webapp.controllers;

import com.akarin.webapp.databases.YaaposDb;
import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.structure.ExpenditureLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

@RestController
public class YaaposApiController {

    private static final Logger logger = LoggerFactory.getLogger(YaaposApiController.class);

    @RequestMapping("yaapos/test")
    public ArrayList<String> yaaposTest(@RequestParam(value = "firstName", defaultValue = "first") String firstName, @RequestParam(value = "lastName", defaultValue = "last") String lastName) {
        ArrayList<String> als = new ArrayList<>();
        als.add(firstName);
        als.add(lastName);

        return als;
    }

    @RequestMapping("yaapos/get")
    public ExpenditureLog getYaaposSpending(@RequestParam(value = "userId") int userId, @RequestParam(value = "spendingWeekId") int spendingWeekId) {
        ExpenditureLog expenditureLogs = new ExpenditureLog("Created from function getYaaposSpending");
        try {
            expenditureLogs = YaaposDb.getExpendituresGivenUserId(userId, spendingWeekId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return expenditureLogs;
    }

    @RequestMapping("yaapos/post")
    public ExpenditureItem postYaaposSpending(@RequestParam(value = "userId") int userId,
                                              @RequestParam(value = "spendingName") String spendingName,
                                              @RequestParam(value = "spendingPrice") double spendingPrice,
                                              @RequestParam(value = "spendingDescription") String spendingDescription,
                                              @RequestParam(value = "spendingWeekId") int spendingWeekId) {
        try {
            final Connection connection = getConnection();
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
            logger.debug(e.getMessage());
        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }

        return new ExpenditureItem(userId, spendingName, spendingPrice, spendingDescription, spendingWeekId);
    }
}
