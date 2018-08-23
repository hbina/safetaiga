package com.akarin.webapp.controllers;

import com.akarin.webapp.structure.MyBean;
import com.akarin.webapp.structure.YaaposSpending;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YaaposApiController {

    private static final String template = "Hello, %s!";

    @RequestMapping("yaapos/json")
    public MyBean greeting(@RequestParam(value = "firstName", defaultValue = "first") String firstName, @RequestParam(value = "lastName", defaultValue = "last") String lastName) {
        return new MyBean(firstName, lastName);
    }

    @RequestMapping("yaapos/spending")
    public YaaposSpending spending(@RequestParam(value = "userId", defaultValue = "userId") String userId, @RequestParam(value = "weekId", defaultValue = "weekId") String weekId) {
        YaaposSpending yaaposSpending = new YaaposSpending(userId);
        //query database
        //populate spending
        return yaaposSpending;
    }
}
