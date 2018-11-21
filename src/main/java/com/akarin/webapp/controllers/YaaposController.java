package com.akarin.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
class YaaposController {


    @SuppressWarnings("SameReturnValue")
    @RequestMapping(path = "portal/{username}")
    public String getYaaposUser(@PathVariable("username") String username, Map<String, Object> model) {
        model.put("username_attribute", username);
        return "yaapos_user_index";
    }
}
