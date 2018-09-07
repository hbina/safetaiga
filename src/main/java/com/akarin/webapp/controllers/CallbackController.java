package com.akarin.webapp.controllers;

import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.akarin.webapp.util.Tools.printHttpServletRequest;
import static com.akarin.webapp.util.Tools.printHttpServletResponse;

@SuppressWarnings("unused")
@Controller
public class CallbackController {

    private final static Logger logger = LoggerFactory.getLogger(CallbackController.class);

    private final String redirectOnFail;
    private final String redirectOnSuccess;
    @Autowired
    private AuthController controller;

    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "/portal/home";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        logger.info("Performing getCallback");
        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        logger.info(String.format("res:%s", printHttpServletResponse(res)));
        handle(req, res);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        logger.info("Performing postCallback");
        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        logger.info(String.format("res:%s", printHttpServletResponse(res)));
        handle(req, res);
    }

    private void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.info("Handling request and response");
        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        logger.info(String.format("res:%s", printHttpServletResponse(res)));
        try {
            Tokens tokens = controller.handle(req);
            SessionUtils.set(req, "accessToken", tokens.getAccessToken());
            SessionUtils.set(req, "idToken", tokens.getIdToken());
            logger.info(String.format("accessToken:%s ", tokens.getAccessToken()));
            logger.info(String.format("idToken:%s", tokens.getIdToken()));
            res.sendRedirect(redirectOnSuccess);
        } catch (IdentityVerificationException e) {
            logger.info(e.getMessage());
            res.sendRedirect(redirectOnFail);
        }
    }
}
