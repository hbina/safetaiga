package com.akarin.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static com.akarin.webapp.util.Tools.printHttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private AuthController controller;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        logger.info("A user is accessing the login page");
        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        String redirectUri = System.getenv().get("AUTH0_CALLBACK_URL");
        //String redirectUri = req.getScheme() + "://" + req.getServerName() + "/callback";
        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri);
        logger.info(String.format("redirectUri: %s", redirectUri));
        logger.info(String.format("authorizeUrl: %s", authorizeUrl));
        return "redirect:" + authorizeUrl;
    }

}
