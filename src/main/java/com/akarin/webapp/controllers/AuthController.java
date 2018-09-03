package com.akarin.webapp.controllers;

import com.akarin.webapp.util.AppConfig;
import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.akarin.webapp.util.Tools.printHttpServletRequest;

@Component
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationController controller;
    private final String userInfoAudience;

    @Autowired
    public AuthController(AppConfig config) {
        controller = AuthenticationController.newBuilder(config.getDomain(), config.getClientId(), config.getClientSecret())
                .build();
        logger.info(String.format("Auth0 domain: %s", config.getDomain()));
        logger.info(String.format("Auth0 clientId: %s", config.getClientId()));
        logger.info(String.format("Auth0 clientSecret: %s", config.getClientSecret()));
        userInfoAudience = String.format("https://%s/userinfo", config.getDomain());
    }

    public Tokens handle(HttpServletRequest req) throws IdentityVerificationException {
        logger.info("Handling request");
        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        return controller.handle(req);
    }

    public String buildAuthorizeUrl(HttpServletRequest req, String redirectUri) {
        logger.info(String.format("Building authorization URL with redirectUri:%s", redirectUri));
        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        return controller
                .buildAuthorizeUrl(req, redirectUri)
                .withAudience(userInfoAudience)
                .build();
    }

}
