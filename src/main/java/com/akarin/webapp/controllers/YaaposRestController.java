package com.akarin.webapp.controllers;

import com.akarin.webapp.databases.YaaposDb;
import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.structure.ExpenditureLog;
import com.akarin.webapp.structure.YaaposUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class YaaposRestController {

    private static final Logger logger = LoggerFactory.getLogger(YaaposRestController.class);

    @RequestMapping(path = "/yaapos/test")
    public ArrayList<String> yaaposTest(@RequestParam(value = "firstName", defaultValue = "first") String firstName, @RequestParam(value = "lastName", defaultValue = "last") String lastName) {
        ArrayList<String> als = new ArrayList<>();
        als.add(firstName);
        als.add(lastName);

        return als;
    }

    @RequestMapping(path = "/yaapos/user/{userId}/spendingWeekId/{spendingWeekId}")
    public ExpenditureLog getYaaposSpending(@PathVariable(value = "userId") int userId, @PathVariable(value = "spendingWeekId") int spendingWeekId) {
        ExpenditureLog expenditureLog = new ExpenditureLog(String.format("This is the ExpenditureLog for user:%s spendingWeekId:%s", userId, spendingWeekId));
        /**
         * TODO: If user is requesting something that does not exist then it should return the appropriate message telling that
         */
        YaaposDb.getExpendituresGivenUserIdAndWeek(expenditureLog, userId, spendingWeekId);
        return expenditureLog;
    }

    @RequestMapping(path = "/yaapos/user/submit")
    public ExpenditureItem postYaaposSpending(@RequestParam(value = "userId") int userId,
                                              @RequestParam(value = "spendingName") String spendingName,
                                              @RequestParam(value = "spendingPrice") double spendingPrice,
                                              @RequestParam(value = "spendingDescription") String spendingDescription) throws IllegalArgumentException {
        String returnMessage;
        long spendingUnixTime = System.currentTimeMillis();
        ExpenditureItem newExpenditure = new ExpenditureItem(userId, spendingName, spendingPrice, spendingDescription, spendingUnixTime);
        if (checkUserId(userId) && checkStringIsNoNullAndEmpty(spendingName) && checkSpendingPrice(spendingPrice)) {
            YaaposDb.postYaaposSpendingGivenExpenditureItems(newExpenditure);
            returnMessage = "Database operation did not throw any exception";
        } else {
            returnMessage = "The parameters provided failed to pass the test" + "spendingName:" + checkStringIsNoNullAndEmpty(spendingName) + " spendingPrice:" +
                    checkSpendingPrice(spendingPrice) + " spendingDescription:";
        }

        newExpenditure.setReturnMessage(returnMessage);
        return newExpenditure;
    }

    @RequestMapping(path = "/yaapos/user/register")
    public YaaposUser registerYaaposUser(@RequestParam(value = "userName") String userName) throws IllegalArgumentException {
        String returnMessage;
        long userRegistrationUnixTime = System.currentTimeMillis();
        YaaposUser newUser = new YaaposUser(userName, userRegistrationUnixTime);
        if (checkStringIsNoNullAndEmpty(userName)) {
            YaaposDb.registerYaaposUser(newUser);
            returnMessage = "Database operation did not throw any exception";
        } else {
            returnMessage = "The parameters provided failed to pass the test" + " userName:" + checkStringIsNoNullAndEmpty(userName);
        }

        newUser.setReturnMessage(returnMessage);
        return newUser;
    }

    private boolean checkSpendingPrice(double spendingPrice) {
        return spendingPrice > 0;
    }

    private boolean checkStringIsNoNullAndEmpty(String spendingName) {
        return spendingName != null && spendingName.length() > 0;
    }

    private boolean checkUserId(int userId) {
        return userId > 0;
    }
}
