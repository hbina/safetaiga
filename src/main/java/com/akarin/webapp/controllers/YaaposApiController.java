package com.akarin.webapp.controllers;

import com.akarin.webapp.databases.YaaposDb;
import com.akarin.webapp.structure.Expenditure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class YaaposApiController {

    private static final Logger logger = LoggerFactory.getLogger(YaaposApiController.class);

    @RequestMapping("yaapos/json")
    public ArrayList<String> greeting(@RequestParam(value = "firstName", defaultValue = "first") String firstName, @RequestParam(value = "lastName", defaultValue = "last") String lastName) {
        ArrayList<String> als = new ArrayList<>();
        als.add(firstName);
        als.add(lastName);

        return als;
    }

    @RequestMapping("yaapos/spending")
    public ArrayList<Expenditure> spending(@RequestParam(value = "userId", defaultValue = "userId") int userId, @RequestParam(value = "weekId", defaultValue = "weekId") int weekId) {
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        try {
            expenditures = YaaposDb.getExpendituresGivenUserId(userId, weekId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return expenditures;
    }
}
