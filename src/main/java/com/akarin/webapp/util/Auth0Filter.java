package com.akarin.webapp.util;

import com.auth0.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.akarin.webapp.util.Tools.printHttpServletRequest;
import static com.akarin.webapp.util.Tools.printHttpServletResponse;

public class Auth0Filter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(Auth0Filter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String accessToken = (String) SessionUtils.get(req, "accessToken");
        String idToken = (String) SessionUtils.get(req, "idToken");

        logger.info(String.format("req:%s", printHttpServletRequest(req)));
        logger.info(String.format("res:%s", printHttpServletResponse(res)));
        logger.info("accessToken:" + accessToken);
        logger.info("idToken:" + idToken);

        if (accessToken == null && idToken == null) {
            res.sendRedirect("/login");
            return;
        }
        next.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
