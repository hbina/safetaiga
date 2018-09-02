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
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    //@Value(value = "${com.auth0.domain}")
    private String domain = System.getenv().get("AUTH0_DOMAIN");

    /**
     * This is the client id of your auth0 application (see Settings page on auth0 dashboard)
     */
    //@Value(value = "${com.auth0.clientId}")
    private String clientId = System.getenv().get("AUTH0_DOMAIN");

    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    //@Value(value = "${com.auth0.clientSecret}")
    private String clientSecret = System.getenv().get("AUTH0_DOMAIN");

    @Bean
    public FilterRegistrationBean filterRegistration() {
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
