package com.akarin.webapp.controllers;

import com.akarin.webapp.databases.YaaposDb;
import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.structure.ExpenditureLog;
import com.akarin.webapp.structure.YaaposJsonApiClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
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
        ExpenditureLog expenditureLogs = new ExpenditureLog();
        try {
            expenditureLogs = YaaposDb.getExpendituresGivenUserId(userId, spendingWeekId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        if (!expenditureLogs.isPropertyGood()) {
            logger.debug(this.getClass().toString(), "Yaapos JSON API class was not successfully populated");
        }
        return expenditureLogs;
    }

    @RequestMapping("yaapos/post")
    public YaaposJsonApiClass postYaaposSpending(@RequestParam(value = "userId") int userId,
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
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        YaaposJsonApiClass yacc = new YaaposJsonApiClass();
        return yacc.setPropertyAsGood();
    }
}
