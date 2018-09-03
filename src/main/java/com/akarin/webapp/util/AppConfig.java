package com.akarin.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
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
        return System.getenv().get("AUTH0_DOMAIN");
    }

    public String getClientId() {
        return System.getenv().get("AUTH0_CLIENT_ID");
    }

    public String getClientSecret() {
        return System.getenv().get("AUTH0_CLIENT_SECRET");
    }
}
