package com.olshevchenko.webshop.web.servlets.servletutils;

import com.olshevchenko.webshop.ServiceLocator;
import com.olshevchenko.webshop.entity.Session;
import com.olshevchenko.webshop.service.SecurityService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
        String userToken = getUserToken(request);
        if (userToken != null && securityService.isTokenValid(userToken)) {
            return securityService.getSessions().get(userToken);
        }
        return null;
    }

    private String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
