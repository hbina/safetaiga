package com.akarin.webapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
@Configuration
public class AppConfig {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    @Value(value = "${com.auth0.domain}")
    private String domain = null;

    /**
     * This is the client id of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientId}")
    private String clientId = null;

    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret = null;

    @Bean
    public FilterRegistrationBean filterRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Auth0Filter());
        registration.addUrlPatterns("/portal/*");
        registration.setName(Auth0Filter.class.getSimpleName());
        return registration;
    }

    public String getDomain() {
        if (domain != null) {
            return domain;
        } else {
            return System.getenv().get("AUTH0_DOMAIN");
        }
    }

    public String getClientId() {
        if (clientId != null) {
            return clientId;
        } else {
            return System.getenv().get("AUTH0_CLIENT_ID");
        }
    }

    public String getClientSecret() {
        if (clientSecret != null) {
            return clientSecret;
        } else {
            return System.getenv().get("AUTH0_CLIENT_SECRET");
        }
    }
}
