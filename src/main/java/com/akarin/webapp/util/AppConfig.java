package com.akarin.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AppConfig {
    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public FilterRegistrationBean filterRegistration() {
        logger.info("filterRegistration");
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Auth0Filter());
        registration.addUrlPatterns("/portal/*");
        registration.setName(Auth0Filter.class.getSimpleName());
        return registration;
    }

    public String getDomain() {
        String domain = System.getenv("AUTH0_DOMAIN");
        logger.info(domain);
        return domain;
    }

    public String getClientId() {
        String clientId = System.getenv("AUTH0_CLIENT_ID");
        logger.info(clientId);
        return clientId;
    }

    public String getClientSecret() {
        String clientSecret = System.getenv("AUTH0_CLIENT_SECRET");
        logger.info(clientSecret);
        return clientSecret;
    }
}
