package com.olshevchenko.webshop.web.servlets.servletutils;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.SecurityService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class SessionFetcher {
    private final SecurityService securityService = ServiceLocator.get(SecurityService.class);

    public void validateAndPutSessionToPageParameters(HttpServletRequest request, Map<String, Object> parameters) {
        Session session = getSession(request);
        if (session != null) {
            parameters.put("session", session);
        }
    }

    public Session getSession(HttpServletRequest request) {
        Session session = null;
        String userToken = getUserToken(request);
        if (userToken != null && securityService.isTokenValid(userToken)) {
            Map<String, Session> sessions = securityService.getSessions();
            session = sessions.get(userToken);
        }
        return session;
    }

    String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("user-token"))
                    .findFirst()
                    .map(Cookie::getValue).orElse(null);
        }
        return null;
    }


}
