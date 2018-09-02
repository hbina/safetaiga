package com.akarin.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthController controller;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        logger.info("Performing login");
String redirectUri = System.getenv().get("AUTH0_CALLBACK_URL");
        //String redirectUri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/callback";
        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri);
        logger.info(String.format("redirectUri: %s", redirectUri));
        logger.info(String.format("authorizeUrl: %s", authorizeUrl));
        return "redirect:" + authorizeUrl;
    }

}
